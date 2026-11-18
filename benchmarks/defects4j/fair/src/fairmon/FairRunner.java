package fairmon;

import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Runs a suite's test classes with a {@link RunListener} that turns the contract
 * monitor's log into each test's outcome. {@code MutantTestMain} (unchanged) runs
 * {@link FairEntry}, which uses this runner, so every (mutant, suite) pair is still
 * one child JVM started through MutantTestMain.
 *
 * <p>System properties (set by the driver on the child JVM):
 * <pre>
 *   fair.classes  file with the test class names, one per line
 *   fair.keep     optional: file with the kept test ids (class#method); others are not run
 *   fair.skip     optional: file with test ids not to run (a baseline run resuming after a hang)
 *   fair.out      file the per-test outcomes are written to
 *   fair.stop     true: stop at the first killing test (mutant runs)
 *   fair.ref      optional (mutant / buggy runs): file with, per kept test, "id TAB invalid_at TAB calls"
 *                 from the fixed version (invalid_at -1 = no invalid call there)
 * </pre>
 *
 * <p>Outcome of one test, from its log only (a try/catch in the test cannot hide anything):
 * <ul>
 *   <li>a precondition is false (or a state field was written by the test, or the test
 *       called the library directly before this call) at call k: the test is INVALID from
 *       call k, nothing from call k on counts;</li>
 *   <li>before that, a postcondition is false, or the library throws, at call k:
 *       FAIL (a contract failure);</li>
 *   <li>PRE divergence (only with {@code fair.ref}): a precondition is false at call k on
 *       this (mutant or buggy) version, but call k was valid on the fixed version (same test,
 *       same call index, k within the calls the test made there): PREDIV, a kill. Validity
 *       is decided on the fixed version only;</li>
 *   <li>JUnit's per-test time limit: TIMEOUT.</li>
 * </ul>
 * Output lines (tab-separated):
 * <pre>
 *   START  id
 *   TEST   id  outcome  calls  invalid_at  fail_at  fresh  junit  obs  message  ops
 *   CLASSFAIL  class  message
 * </pre>
 * {@code outcome} is FAIL, PREDIV, TIMEOUT, NOCALL (no adapter call), INVALID1 (invalid from the
 * first call) or PASS (possibly invalid from a later call, see {@code invalid_at}).
 * {@code fresh} is yes when the adapter was reset after its last call before this
 * test's first call. {@code obs} lists a short hash of every call's observation (what it
 * returned or threw, and the plain state fields after it), for the report's reasons.
 * {@code ops} lists, per adapter operation, {@code op:calls:invalid}: every call the test made
 * to it, and how many of those the monitor flagged (a false precondition, a state field the
 * test wrote, or a direct library call before it), whether or not an earlier call was invalid.
 */
public final class FairRunner extends Runner {

    private final Class<?> entry;

    public FairRunner(Class<?> entry) {
        this.entry = entry;
        FairLog.events();   // create the shared log before any EvoSuite scaffolding copies the properties
    }

    @Override
    public Description getDescription() {
        Description d = Description.createSuiteDescription(entry);
        d.addChild(Description.createTestDescription(entry, "fair"));
        return d;
    }

    @Override
    public void run(RunNotifier outer) {
        Description test = getDescription().getChildren().get(0);
        outer.fireTestStarted(test);
        String verdict;
        try {
            verdict = runAll();
        } catch (Throwable t) {
            verdict = "ERROR " + t;
        }
        if (verdict != null) outer.fireTestFailure(new Failure(test, new AssertionError("POST " + verdict)));
        outer.fireTestFinished(test);
    }

    /** Runs every class; returns null if nothing was killed, else a short description of the kill. */
    private String runAll() throws IOException {
        List<String> classes = readLines(System.getProperty("fair.classes"));
        String keepFile = System.getProperty("fair.keep");
        final Set<String> keep = keepFile == null ? null : new HashSet<String>(readLines(keepFile));
        String skipFile = System.getProperty("fair.skip");
        final Set<String> skip = skipFile == null ? new HashSet<String>() : new HashSet<String>(readLines(skipFile));
        final boolean stop = Boolean.getBoolean("fair.stop");
        final java.util.Map<String, int[]> ref = new java.util.HashMap<String, int[]>();   // id -> {invalid_at, calls} on fixed
        String refFile = System.getProperty("fair.ref");
        if (refFile != null) {
            for (String l : readLines(refFile)) {
                String[] f = l.split("\t");
                ref.put(f[0], new int[] { Integer.parseInt(f[1]), Integer.parseInt(f[2]) });
            }
        }
        final PrintWriter out = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(System.getProperty("fair.out"), true), StandardCharsets.UTF_8));
        final String[] kill = new String[1];
        final int[] callsSinceReset = new int[1];
        try {
            for (String name : classes) {
                if (stop && kill[0] != null) break;
                if (keep != null && !hasKept(keep, name)) continue;
                Class<?> cls;
                try {
                    cls = Class.forName(name);
                } catch (Throwable t) {
                    out.println("CLASSFAIL\t" + name + "\t" + oneLine(t.toString()));
                    out.flush();
                    continue;
                }
                Request req = Request.aClass(cls);
                if (keep != null || !skip.isEmpty()) {
                    req = req.filterWith(new Filter() {
                        @Override public boolean shouldRun(Description d) {
                            if (d.getMethodName() == null) return true;
                            return (keep == null || keep.contains(id(d))) && !skip.contains(id(d));
                        }
                        @Override public String describe() { return "kept tests"; }
                    });
                }
                Runner runner = req.getRunner();
                final RunNotifier inner = new RunNotifier();
                inner.addListener(new RunListener() {
                    String current;
                    Throwable failure;

                    @Override public void testStarted(Description d) {
                        // events logged between tests (class initialisation, EvoSuite's resets) only move the reset counter
                        account(FairLog.drain(), callsSinceReset, null);
                        current = id(d);
                        failure = null;
                        out.println("START\t" + current);
                        out.flush();
                    }

                    @Override public void testFailure(Failure f) {
                        if (f.getDescription().getMethodName() == null || current == null) {
                            out.println("CLASSFAIL\t" + f.getDescription().getClassName() + "\t"
                                    + oneLine(String.valueOf(f.getException())));
                            out.flush();
                        } else {
                            failure = f.getException();
                        }
                    }

                    @Override public void testAssumptionFailure(Failure f) {
                        failure = f.getException();
                    }

                    @Override public void testFinished(Description d) {
                        Outcome o = new Outcome();
                        o.ref = ref.get(current);
                        account(FairLog.drain(), callsSinceReset, o);
                        boolean junitTimeout = failure != null
                                && failure.getClass().getName().equals("org.junit.runners.model.TestTimedOutException");
                        String outcome;
                        if (o.failAt > 0) outcome = o.preDivergence ? "PREDIV" : "FAIL";
                        else if (junitTimeout) outcome = "TIMEOUT";
                        else if (o.calls == 0) outcome = "NOCALL";
                        else if (o.invalidAt == 1) outcome = "INVALID1";
                        else outcome = "PASS";
                        String junit = failure == null ? "PASS" : oneLine(String.valueOf(failure.getMessage() != null
                                && failure instanceof AssertionError ? failure.getMessage() : failure.toString()));
                        if (junit.length() > 160) junit = junit.substring(0, 160);
                        out.println("TEST\t" + current + "\t" + outcome + "\t" + o.calls + "\t" + o.invalidAt + "\t"
                                + o.failAt + "\t" + (o.fresh ? "yes" : "no") + "\t" + junit + "\t"
                                + String.join(",", o.obs) + "\t" + oneLine(o.message) + "\t" + o.ops());
                        out.flush();
                        if ((outcome.equals("FAIL") || outcome.equals("PREDIV") || outcome.equals("TIMEOUT")) && kill[0] == null) {
                            kill[0] = current + " " + outcome + " at call " + Math.max(o.failAt, 0) + ": " + o.message;
                            if (stop) inner.pleaseStop();
                        }
                        current = null;
                    }
                });
                try {
                    runner.run(inner);
                } catch (StoppedByUserException stopped) {
                    // stopped at the first kill
                } catch (Throwable t) {
                    out.println("CLASSFAIL\t" + name + "\t" + oneLine(t.toString()));
                    out.flush();
                }
            }
        } finally {
            out.close();
        }
        return kill[0];
    }

    static final class Outcome {
        int calls, invalidAt = -1, failAt = -1;
        boolean fresh = true, sawCall;
        boolean pendingOffApi;
        int[] ref;              // {invalid_at, calls} of this test on the fixed version, or null (baseline runs)
        boolean preDivergence;  // failAt is a PRE false on a call that was valid on the fixed version
        String message = "";
        final List<String> obs = new ArrayList<String>();
        final java.util.Map<String, int[]> perOp = new java.util.LinkedHashMap<String, int[]>();   // op -> {calls, invalid}
        String op;
        int flaggedCall;   // the last call counted as invalid in perOp

        /** Counts the current call as invalid in perOp (once per call). */
        void flag() {
            if (op != null && flaggedCall != calls) { perOp.get(op)[1]++; flaggedCall = calls; }
        }

        String ops() {
            StringBuilder sb = new StringBuilder();
            for (java.util.Map.Entry<String, int[]> e : perOp.entrySet()) {
                if (sb.length() > 0) sb.append(',');
                sb.append(e.getKey()).append(':').append(e.getValue()[0]).append(':').append(e.getValue()[1]);
            }
            return sb.toString();
        }
    }

    /** Walks one test's events in order. With {@code o == null} it only updates the reset counter. */
    static void account(List<String> events, int[] callsSinceReset, Outcome o) {
        for (String e : events) {
            String[] f = e.split("\t", 3);
            String kind = f[0];
            if (kind.equals("RESET")) { callsSinceReset[0] = 0; continue; }
            if (kind.equals("CALL")) {
                if (o != null) {
                    if (!o.sawCall) { o.fresh = callsSinceReset[0] == 0; o.sawCall = true; }
                    o.calls++;
                    o.op = f.length > 1 ? f[1] : "?";
                    if (!o.perOp.containsKey(o.op)) o.perOp.put(o.op, new int[2]);
                    o.perOp.get(o.op)[0]++;
                    if (o.pendingOffApi) { invalid(o, "OFFAPI before this call"); o.flag(); o.pendingOffApi = false; }
                }
                callsSinceReset[0]++;
                continue;
            }
            if (o == null) continue;
            String detail = f.length > 2 ? f[1] + ": " + f[2] : (f.length > 1 ? f[1] : "");
            switch (kind) {
                case "OFFAPI":
                    o.pendingOffApi = true;
                    break;
                case "PRE":
                    o.flag();
                    if (o.ref != null && o.invalidAt < 0 && o.failAt < 0 && o.calls > 0 && o.calls <= o.ref[1]
                            && (o.ref[0] < 0 || o.calls < o.ref[0])) {
                        // valid on the fixed version, invalid here: the version under test changed the state
                        o.failAt = o.calls;
                        o.preDivergence = true;
                        o.message = "PRE_DIVERGENCE " + detail;
                    } else {
                        invalid(o, kind + " " + detail);
                    }
                    break;
                case "TAMPER":
                    o.flag();
                    invalid(o, kind + " " + detail);
                    break;
                case "PREX":
                case "THROW":
                case "POST":
                case "POSTX":
                    if (o.invalidAt < 0 && o.failAt < 0 && o.calls > 0) {
                        o.failAt = o.calls;
                        o.message = kind + " " + detail;
                    }
                    break;
                case "OBS":
                    o.obs.add(Integer.toHexString((f.length > 2 ? f[2] : "").hashCode()));
                    break;
                default:
                    if (o.message.isEmpty()) o.message = e;   // LOGERR
            }
        }
    }

    private static void invalid(Outcome o, String why) {
        if (o.invalidAt < 0 && o.failAt < 0 && o.calls > 0) {
            o.invalidAt = o.calls;
            if (o.message.isEmpty()) o.message = "invalid from call " + o.calls + ": " + why;
        }
    }

    static String id(Description d) {
        return d.getClassName() + "#" + d.getMethodName();
    }

    private static boolean hasKept(Set<String> keep, String cls) {
        for (String k : keep) if (k.startsWith(cls + "#")) return true;
        return false;
    }

    private static List<String> readLines(String file) throws IOException {
        List<String> out = new ArrayList<String>();
        for (String l : Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8)) {
            if (!l.trim().isEmpty()) out.add(l.trim());
        }
        return new ArrayList<String>(new LinkedHashSet<String>(out));
    }

    private static String oneLine(String s) {
        return s == null ? "" : s.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
    }
}

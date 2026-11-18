package in.ac.iiitb.plproject.fair;

import in.ac.iiitb.plproject.mutation.MutantTestMain;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The fair run for one class (mutants) or one bug.
 *
 * <pre>
 *   FairDriver &lt;run.properties&gt;
 * </pre>
 *
 * Properties: {@code mode} (mutants | bug), {@code scope} (class key or bug name),
 * {@code suites} (tool TAB suite TAB sources), {@code monitor} (compiled monitored
 * adapter + HelperImpl), {@code projectClasses}, {@code runtimeCp}, {@code mutationCp}
 * (MutantTestMain), {@code fairmonCp}, {@code work}, {@code threads}, {@code timeoutSeconds},
 * {@code validity} (validity rows are written here).
 * Mutants mode: {@code orig} (the class compiled alone, first on the classpath),
 * {@code mutantsDir} (one {@code <id>/classes} per planted mutant, by D4jMutationRun),
 * {@code sample} (mutation/&lt;key&gt;.status.tsv: rows with sampled=yes are scored),
 * {@code rows} (output). Bug mode: {@code buggyMonitor}, {@code buggyClasses}, {@code rows}.
 *
 * <p>Every test JVM is started like D4jMutationRun's: MutantTestMain, -Xmx512m, headless,
 * TZ=America/Los_Angeles, the same wall-clock limit. MutantTestMain runs {@code fairmon.FairEntry},
 * whose runner writes each test's outcome (see FairRunner).
 */
public final class FairDriver {

    private final Properties p;
    private final String mode, scope, runtimeCp, mutationCp, fairmonCp, projectClasses, monitor, javaBin;
    private final Path work;
    private final int threads, timeoutSeconds;

    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream(args[0])) { p.load(in); }
        new FairDriver(p).run();
    }

    private FairDriver(Properties p) throws IOException {
        this.p = p;
        mode = p.getProperty("mode");
        scope = p.getProperty("scope");
        runtimeCp = p.getProperty("runtimeCp");
        mutationCp = p.getProperty("mutationCp");
        fairmonCp = p.getProperty("fairmonCp");
        projectClasses = p.getProperty("projectClasses");
        monitor = p.getProperty("monitor");
        work = Paths.get(p.getProperty("work"));
        threads = Integer.parseInt(p.getProperty("threads", "8"));
        timeoutSeconds = Integer.parseInt(p.getProperty("timeoutSeconds", "30"));
        javaBin = System.getProperty("java.home") + "/bin/java";
        Files.createDirectories(work);
    }

    // ── suites ──

    static final class Suite {
        final String tool, name;
        final List<String> sources = new ArrayList<String>();
        final List<String> classes = new ArrayList<String>();
        Path classDir, classesFile, keepFile, refFile;
        String status = "KEPT", reason = "";
        String assertionsRemoved = "", offApiMarkers = "";
        int tests, invalid1, invalidAny, noCall, falseAlarms, flaky, notRun, notFresh, kept;
        final List<Integer> calls = new ArrayList<Integer>();
        final Map<String, int[]> ops = new java.util.TreeMap<String, int[]>();   // op -> {calls, invalid}, run 1
        Map<String, String[]> reference = new LinkedHashMap<String, String[]>();   // kept test -> TEST fields (run 1)
        final List<String> details = new ArrayList<String>();
        Suite(String tool, String name) { this.tool = tool; this.name = name; }
    }

    private void run() throws Exception {
        List<Suite> suites = readSuites();
        String fixedFirst = mode.equals("mutants") ? p.getProperty("orig") + ":" : "";
        String fixedCp = fixedFirst + monitor + ":" + projectClasses;

        // 1. compile every suite against the monitored adapter on the fixed version
        for (Suite s : suites) {
            if (!s.status.equals("KEPT")) continue;
            s.classDir = work.resolve("suites").resolve(s.name);
            deleteRecursively(s.classDir);
            String err = javac(s.classDir, fixedCp + ":" + runtimeCp + ":" + fairmonCp, s.sources);
            if (err != null) {
                s.status = "DROPPED";
                s.reason = "does not compile against the monitored adapter: " + firstLine(err);
                continue;
            }
            for (String f : s.sources) {
                String text = new String(Files.readAllBytes(Paths.get(f)), StandardCharsets.UTF_8);
                if (!text.contains("@Test")) continue;
                java.util.regex.Matcher pk = java.util.regex.Pattern.compile("(?m)^\\s*package\\s+([\\w.]+)\\s*;").matcher(text);
                String simple = Paths.get(f).getFileName().toString().replaceFirst("\\.java$", "");
                s.classes.add((pk.find() ? pk.group(1) + "." : "") + simple);
            }
            Collections.sort(s.classes);
            s.classesFile = work.resolve("suites").resolve(s.name + ".classes");
            Files.write(s.classesFile, String.join("\n", s.classes).getBytes(StandardCharsets.UTF_8));
        }

        // 2. baseline on the fixed version, twice; keep the in-contract, passing, stable tests
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<?>> fs = new ArrayList<Future<?>>();
        for (Suite s : suites) {
            if (!s.status.equals("KEPT")) continue;
            fs.add(pool.submit(() -> { baseline(s, fixedCp); return null; }));
        }
        for (Future<?> f : fs) f.get();
        writeValidity(suites);
        int problem = checks(suites);
        if (problem != 0) System.exit(problem);

        if (mode.equals("mutants")) runMutants(suites, pool);
        else runBug(suites, pool);
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }

    private void baseline(Suite s, String fixedCp) throws Exception {
        Map<String, String[]> r1 = runAll(s, fixedCp, "base1"), r2 = runAll(s, fixedCp, "base2");
        Set<String> ids = new HashSet<String>(r1.keySet());
        ids.addAll(r2.keySet());
        List<String> kept = new ArrayList<String>();
        for (String id : new java.util.TreeSet<String>(ids)) {
            String[] a = r1.get(id), b = r2.get(id);
            String verdict;
            if (a == null || b == null) { s.notRun++; verdict = "NOT_RUN"; }
            else {
                s.tests++;
                int calls = Integer.parseInt(a[3]);
                s.calls.add(calls);
                if (a.length > 10 && !a[10].isEmpty()) {
                    for (String op : a[10].split(",")) {
                        String[] q = op.split(":");
                        int[] c = s.ops.computeIfAbsent(q[0], k -> new int[2]);
                        c[0] += Integer.parseInt(q[1]);
                        c[1] += Integer.parseInt(q[2]);
                    }
                }
                if (a[2].equals("INVALID1")) s.invalid1++;
                if (!a[4].equals("-1")) s.invalidAny++;
                if (a[6].equals("no") && calls > 0) s.notFresh++;
                if (isKill(a[2]) || isKill(b[2])) { s.falseAlarms++; verdict = "FALSE_ALARM"; }
                else if (!signature(a).equals(signature(b))) { s.flaky++; verdict = "FLAKY"; }
                else if (a[2].equals("INVALID1")) verdict = "INVALID1";
                else if (a[2].equals("NOCALL")) { s.noCall++; verdict = "NOCALL"; }
                else { verdict = "KEPT"; kept.add(id); s.reference.put(id, a); }
            }
            String[] d = a != null ? a : b;
            s.details.add(id + "\t" + verdict + "\t" + (d == null ? "" : d[2] + "\t" + d[3] + "\t" + d[4] + "\t" + d[5]
                    + "\t" + d[7] + "\t" + (d.length > 9 ? d[9] : "")));
        }
        s.kept = kept.size();
        s.keepFile = work.resolve("suites").resolve(s.name + ".keep");
        Files.write(s.keepFile, String.join("\n", kept).getBytes(StandardCharsets.UTF_8));
        // PRE divergence rule: validity is decided on the fixed version; the runner gets each kept test's
        // invalid_at and number of calls there
        StringBuilder ref = new StringBuilder();
        for (Map.Entry<String, String[]> e : s.reference.entrySet()) {
            ref.append(e.getKey()).append('\t').append(e.getValue()[4]).append('\t').append(e.getValue()[3]).append('\n');
        }
        s.refFile = work.resolve("suites").resolve(s.name + ".ref");
        Files.write(s.refFile, ref.toString().getBytes(StandardCharsets.UTF_8));
        if (kept.isEmpty()) { s.status = "NO_TESTS"; s.reason = "no test left after the baseline check"; }
    }

    private static boolean isKill(String outcome) {
        return outcome.equals("FAIL") || outcome.equals("PREDIV") || outcome.equals("TIMEOUT");
    }

    private static String signature(String[] t) {
        return t[2] + "|" + t[3] + "|" + t[4] + "|" + t[5] + "|" + t[8];
    }

    /** Runs a whole suite; resumes past a test that hangs the JVM (it is recorded as TIMEOUT). */
    private Map<String, String[]> runAll(Suite s, String cp, String tag) throws Exception {
        Map<String, String[]> all = new LinkedHashMap<String, String[]>();
        Set<String> skip = new HashSet<String>();
        for (int attempt = 0; attempt < 50; attempt++) {
            Path skipFile = null;
            if (!skip.isEmpty()) {
                skipFile = work.resolve("suites").resolve(s.name + "." + tag + ".skip");
                Files.write(skipFile, String.join("\n", skip).getBytes(StandardCharsets.UTF_8));
            }
            Path out = work.resolve("suites").resolve(s.name + "." + tag + "." + attempt + ".out");
            Files.deleteIfExists(out);
            Result r = runJvm(cp, s, null, skipFile, out, false);
            all.putAll(r.tests);
            skip.addAll(r.tests.keySet());
            if (!r.timedOut) break;
            if (r.hung == null) break;   // hung outside any test (class initialisation)
            all.put(r.hung, new String[] { "TEST", r.hung, "TIMEOUT", "0", "-1", "-1", "yes", "JVM wall-clock limit", "", "" });
            skip.add(r.hung);
        }
        return all;
    }

    /**
     * The checks before any mutant/bug run: (1) TestGen's suites have no INVALID call and no
     * false alarm under the monitor on the fixed version; (2) every test that calls the adapter
     * starts from a fresh adapter. Exit code 3 / 4, with the tests that differ.
     */
    private int checks(List<Suite> suites) {
        Set<String> checked = new HashSet<String>(Arrays.asList(p.getProperty("checkTools", "").split(",")));
        boolean allowNative = Boolean.parseBoolean(p.getProperty("allowNativeFalseAlarms", "false"));
        StringBuilder bad = new StringBuilder();
        boolean onlyNative = true;
        for (Suite s : suites) {
            if (!checked.contains(s.tool) || s.status.equals("DROPPED")) continue;
            if (s.invalidAny == 0 && s.falseAlarms == 0 && s.flaky == 0 && s.notRun == 0) continue;
            bad.append("  ").append(s.name).append(": ").append(s.invalidAny).append(" test(s) with an INVALID call, ")
               .append(s.falseAlarms).append(" false alarm(s), ").append(s.flaky).append(" flaky, ")
               .append(s.notRun).append(" not run\n");
            for (String d : s.details) {
                String[] f = d.split("\t", -1);
                if (f[1].equals("KEPT") || f[1].equals("NOCALL")) continue;
                if (f[1].equals("INVALID1") || !f[4].equals("-1")) onlyNative = false;
                if (f[1].equals("FALSE_ALARM") && (f.length < 7 || f[6].equals("PASS"))) onlyNative = false;
                if (f[1].equals("FLAKY") || f[1].equals("NOT_RUN")) onlyNative = false;
                bad.append("    ").append(f[0]).append("  monitor: ").append(f[1]).append(' ')
                   .append(f.length > 7 ? f[7] : "").append("  |  TestGen's own assertions: ")
                   .append(f.length > 6 ? f[6] : "?").append('\n');
            }
        }
        if (bad.length() > 0) {
            System.out.println("\nTESTGEN CHECK on " + scope + " (fixed version): the monitor does not agree with a clean run of TestGen's suites\n" + bad);
            if (!(allowNative && onlyNative)) return 3;
            System.out.println("  continuing: every flagged test is also failed by TestGen's own assertions (--allow-native-false-alarms)");
        }
        StringBuilder stale = new StringBuilder();
        for (Suite s : suites) {
            if (s.notFresh > 0) stale.append("  ").append(s.name).append(": ").append(s.notFresh)
                                     .append(" test(s) start from an adapter another test already used\n");
        }
        if (stale.length() > 0) {
            System.out.println("\nFRESH-STATE CHECK on " + scope + " failed:\n" + stale);
            return 4;
        }
        return 0;
    }

    // ── mutants ──

    private void runMutants(List<Suite> suites, ExecutorService pool) throws Exception {
        List<String> sample = new ArrayList<String>();
        for (String l : Files.readAllLines(Paths.get(p.getProperty("sample")), StandardCharsets.UTF_8)) {
            String[] f = l.split("\t", -1);
            if (f.length > 2 && f[1].equals("VALID") && f[2].equals("yes")) sample.add(f[0]);
        }
        Path mutantsDir = Paths.get(p.getProperty("mutantsDir"));
        for (String id : sample) {
            if (!Files.isDirectory(mutantsDir.resolve(id).resolve("classes"))) {
                throw new IllegalStateException("mutant " + id + " was not planted/compiled in " + mutantsDir);
            }
        }
        Path rowsFile = Paths.get(p.getProperty("rows"));
        String header = "id\tsuite\ttool\toutcome\tkind\ttest\tcall\tms\ttimeout_s\tdiff\tmessage\n";
        Set<String> done = new HashSet<String>();
        if (Files.exists(rowsFile)) {
            for (String l : Files.readAllLines(rowsFile, StandardCharsets.UTF_8)) {
                String[] f = l.split("\t", -1);
                if (f.length > 8 && !f[0].equals("id") && f[8].equals(String.valueOf(timeoutSeconds))) done.add(f[0] + "\t" + f[1]);
            }
        } else {
            Files.write(rowsFile, header.getBytes(StandardCharsets.UTF_8));
        }
        List<String[]> jobs = new ArrayList<String[]>();
        for (String id : sample) for (Suite s : suites) {
            if (s.status.equals("KEPT") || s.status.equals("NO_TESTS")) {
                if (!done.contains(id + "\t" + s.name)) jobs.add(new String[] { id, s.name });
            }
        }
        Map<String, Suite> byName = new HashMap<String, Suite>();
        for (Suite s : suites) byName.put(s.name, s);
        System.out.println(scope + ": " + sample.size() + " scored mutants, rows to run " + jobs.size() + " ("
                + done.size() + " done), timeout " + timeoutSeconds + " s, " + threads + " in parallel");
        AtomicInteger ran = new AtomicInteger();
        long t0 = System.nanoTime();
        List<Future<?>> fs = new ArrayList<Future<?>>();
        try (Writer rows = Files.newBufferedWriter(rowsFile, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            for (String[] job : jobs) {
                fs.add(pool.submit(() -> {
                    Suite s = byName.get(job[1]);
                    String row;
                    if (s.status.equals("NO_TESTS")) {
                        row = job[0] + "\t" + s.name + "\t" + s.tool + "\tSURVIVED\tNO_TESTS\t\t\t0\t" + timeoutSeconds + "\t-\t" + s.reason;
                    } else {
                        Path out = work.resolve("runs").resolve(job[0]).resolve(s.name + ".out");
                        Files.createDirectories(out.getParent());
                        Files.deleteIfExists(out);
                        long start = System.nanoTime();
                        Result r = runJvm(mutantsDir.resolve(job[0]).resolve("classes") + ":" + monitor + ":" + projectClasses,
                                s, s.keepFile, null, out, true);
                        long ms = (System.nanoTime() - start) / 1_000_000;
                        row = job[0] + "\t" + s.name + "\t" + s.tool + "\t" + verdict(r, s) + "\t" + ms + "\t" + timeoutSeconds
                                + "\t" + (r.kill == null ? diff(r, s) : "-") + "\t" + clip(message(r));
                        Files.deleteIfExists(out);
                    }
                    synchronized (rows) { rows.write(row + "\n"); rows.flush(); }
                    int n = ran.incrementAndGet();
                    if (n % 500 == 0 || n == jobs.size()) {
                        System.out.println("  " + scope + " rows " + n + "/" + jobs.size() + " ("
                                + (System.nanoTime() - t0) / 1_000_000_000L + " s)");
                    }
                    return null;
                }));
            }
            for (Future<?> f : fs) f.get();
        }
    }

    /** outcome TAB kind TAB test TAB call. */
    private String verdict(Result r, Suite s) {
        if (r.kill != null) {
            String kind = r.kill[2].equals("FAIL") ? "CONTRACT" : r.kill[2].equals("PREDIV") ? "PRE_DIVERGENCE" : "TIMEOUT";
            return "KILLED\t" + kind + "\t" + r.kill[1] + "\t" + r.kill[5];
        }
        if (r.timedOut) return "KILLED\tTIMEOUT\t" + (r.hung == null ? "" : r.hung) + "\t";
        int missing = 0;
        for (String id : s.reference.keySet()) if (!r.tests.containsKey(id)) missing++;
        if (missing > 0) return "SURVIVED\tCRASH\t\t";   // the JVM ended early without a contract failure
        return "SURVIVED\t\t\t";
    }

    private static String message(Result r) {
        if (r.kill != null) return r.kill.length > 9 ? r.kill[9] : "";
        if (r.timedOut) return "test JVM killed at the wall-clock limit";
        return r.classFail == null ? "" : r.classFail;
    }

    /**
     * For a survivor, how the mutant showed through the adapter compared with the fixed version:
     * valid (a valid call returned/threw something else or left other plain state),
     * earlier-invalid (a call became invalid earlier), invalid (only calls already
     * invalid differ), none (no adapter call shows any difference).
     */
    private static String diff(Result r, Suite s) {
        boolean valid = false, earlier = false, invalid = false;
        for (Map.Entry<String, String[]> e : s.reference.entrySet()) {
            String[] ref = e.getValue(), mut = r.tests.get(e.getKey());
            if (mut == null) continue;
            List<String> a = obs(ref), b = obs(mut);
            int refInv = Integer.parseInt(ref[4]), mutInv = Integer.parseInt(mut[4]);
            int validCalls = refInv > 0 ? refInv - 1 : Integer.MAX_VALUE;
            int n = Math.max(a.size(), b.size());
            for (int i = 0; i < n; i++) {
                String x = i < a.size() ? a.get(i) : null, y = i < b.size() ? b.get(i) : null;
                if (x != null && x.equals(y)) continue;
                if (i < validCalls) valid = true; else invalid = true;
                break;
            }
            if (mutInv > 0 && (refInv < 0 || mutInv < refInv)) earlier = true;
        }
        return valid ? "valid" : earlier ? "earlier-invalid" : invalid ? "invalid" : "none";
    }

    private static List<String> obs(String[] t) {
        return t[8].isEmpty() ? Collections.<String>emptyList() : Arrays.asList(t[8].split(","));
    }

    // ── bug ──

    private void runBug(List<Suite> suites, ExecutorService pool) throws Exception {
        Path rowsFile = Paths.get(p.getProperty("rows"));
        String buggyCp = p.getProperty("buggyMonitor") + ":" + p.getProperty("buggyClasses");
        List<Future<String>> fs = new ArrayList<Future<String>>();
        for (Suite s : suites) {
            fs.add(pool.submit(() -> {
                String pre = scope + "\t" + s.tool + "\t" + s.name + "\t" + s.tests + "\t" + s.kept + "\t";
                if (!s.status.equals("KEPT")) return pre + "NO_TESTS\t\t\t" + clip(s.reason);
                Path out = work.resolve("buggy").resolve(s.name + ".out");
                Files.createDirectories(out.getParent());
                Files.deleteIfExists(out);
                Result r = runJvm(buggyCp, s, s.keepFile, null, out, true);
                if (r.kill != null && (r.kill[2].equals("FAIL") || r.kill[2].equals("PREDIV"))) {
                    // a PRE divergence is labelled by its message (PRE_DIVERGENCE ...)
                    return pre + "DETECTED\t" + r.kill[1] + "\t" + r.kill[5] + "\t" + clip(r.kill.length > 9 ? r.kill[9] : "");
                }
                if (r.kill != null || r.timedOut) return pre + "TIMEOUT\t" + (r.kill != null ? r.kill[1] : "") + "\t\t";
                return pre + "NOT_DETECTED\t\t\t";
            }));
        }
        StringBuilder sb = new StringBuilder();
        for (Future<String> f : fs) sb.append(f.get()).append('\n');
        Files.write(rowsFile, ("bug\ttool\tsuite\ttests\tkept\tresult\ttest\tcall\tmessage\n" + sb)
                .getBytes(StandardCharsets.UTF_8));
        System.out.print(sb);
    }

    // ── child JVM ──

    static final class Result {
        final Map<String, String[]> tests = new LinkedHashMap<String, String[]>();
        boolean timedOut;
        String hung, classFail;
        String[] kill;
    }

    private Result runJvm(String libCp, Suite s, Path keep, Path skip, Path out, boolean stop)
            throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<String>(Arrays.asList(javaBin, "-Xmx512m", "-XX:+UseSerialGC",
                "-XX:TieredStopAtLevel=1", "-Djava.awt.headless=true",
                "-Dfair.classes=" + s.classesFile, "-Dfair.out=" + out, "-Dfair.stop=" + stop));
        if (keep != null) cmd.add("-Dfair.keep=" + keep);
        if (skip != null) cmd.add("-Dfair.skip=" + skip);
        if (keep != null && s.refFile != null) cmd.add("-Dfair.ref=" + s.refFile);   // mutant / buggy runs only
        cmd.addAll(Arrays.asList("-cp", libCp + ":" + s.classDir + ":" + runtimeCp + ":" + mutationCp + ":" + fairmonCp,
                MutantTestMain.class.getName(), "fairmon.FairEntry"));
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.environment().put("TZ", "America/Los_Angeles");
        pb.directory(work.toFile());
        pb.redirectErrorStream(true);
        pb.redirectOutput(out.resolveSibling(out.getFileName() + ".log").toFile());
        Process proc = pb.start();
        boolean finished = proc.waitFor(timeoutSeconds, TimeUnit.SECONDS);
        if (!finished) {
            proc.descendants().forEach(ProcessHandle::destroyForcibly);
            proc.destroyForcibly();
            proc.waitFor(10, TimeUnit.SECONDS);
        }
        Result r = new Result();
        r.timedOut = !finished;
        if (!Files.exists(out)) return r;
        String started = null;
        for (String l : Files.readAllLines(out, StandardCharsets.UTF_8)) {
            String[] f = l.split("\t", -1);
            if (f[0].equals("START")) started = f[1];
            else if (f[0].equals("TEST") && f.length >= 10) {
                r.tests.put(f[1], f);
                started = null;
                if (r.kill == null && isKill(f[2])) r.kill = f;
            } else if (f[0].equals("CLASSFAIL") && r.classFail == null) {
                r.classFail = l;
            }
        }
        if (r.timedOut) r.hung = started;
        return r;
    }

    // ── output ──

    private void writeValidity(List<Suite> suites) throws IOException {
        Path f = Paths.get(p.getProperty("validity"));
        StringBuilder sb = new StringBuilder();
        for (Suite s : suites) {
            List<Integer> c = new ArrayList<Integer>(s.calls);
            Collections.sort(c);
            String median = c.isEmpty() ? "" : String.valueOf(c.size() % 2 == 1 ? c.get(c.size() / 2)
                    : (c.get(c.size() / 2 - 1) + c.get(c.size() / 2)) / 2.0);
            String max = c.isEmpty() ? "" : String.valueOf(c.get(c.size() - 1));
            String pct = s.tests == 0 ? "" : String.format("%.1f", 100.0 * s.invalid1 / s.tests);
            sb.append(scope).append('\t').append(s.tool).append('\t').append(s.name).append('\t').append(s.status)
              .append('\t').append(s.tests).append('\t').append(s.invalid1).append('\t').append(pct)
              .append('\t').append(s.invalidAny).append('\t').append(s.noCall).append('\t').append(s.falseAlarms)
              .append('\t').append(s.flaky).append('\t').append(s.notRun).append('\t').append(s.notFresh)
              .append('\t').append(s.kept).append('\t').append(median).append('\t').append(max)
              .append('\t').append(s.assertionsRemoved).append('\t').append(s.offApiMarkers)
              .append('\t').append(s.ops.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()[0] + "/" + e.getValue()[1])
                                   .collect(Collectors.joining(";")))
              .append('\t').append(clip(s.reason)).append('\n');
            Path d = work.resolve("baseline-tests");
            Files.createDirectories(d);
            Files.write(d.resolve(s.name + ".tsv"), ("test\tverdict\toutcome\tcalls\tinvalid_at\tfail_at\tjunit\tmessage\n"
                    + String.join("\n", s.details) + "\n").getBytes(StandardCharsets.UTF_8));
        }
        Files.write(f, sb.toString().getBytes(StandardCharsets.UTF_8));
        System.out.print(sb);
    }

    // ── helpers ──

    private List<Suite> readSuites() throws IOException {
        List<Suite> out = new ArrayList<Suite>();
        for (String l : Files.readAllLines(Paths.get(p.getProperty("suites")), StandardCharsets.UTF_8)) {
            if (l.trim().isEmpty() || l.startsWith("#")) continue;
            String[] c = l.split("\t");
            Suite s = new Suite(c[0], c[1]);
            Path src = Paths.get(c[2]);
            if (Files.isDirectory(src)) {
                try (Stream<Path> w = Files.walk(src)) {
                    w.filter(x -> x.toString().endsWith(".java")).sorted().forEach(x -> s.sources.add(x.toString()));
                }
            } else if (Files.exists(src)) {
                s.sources.add(src.toString());
            }
            if (s.sources.isEmpty()) { s.status = "NO_TESTS"; s.reason = "no source files"; }
            Path strip = src.resolveSibling("strip.txt");   // written by StripOracles next to the stripped sources
            if (Files.exists(strip)) {
                String[] w = new String(Files.readAllBytes(strip), StandardCharsets.UTF_8).trim().split("\\s+");
                for (int i = 0; i + 1 < w.length; i++) {
                    if (w[i].equals("assertions_removed")) s.assertionsRemoved = w[i + 1];
                    if (w[i].equals("offapi_markers")) s.offApiMarkers = w[i + 1];
                }
            }
            out.add(s);
        }
        return out;
    }

    private static String javac(Path outDir, String cp, List<String> sources) throws IOException {
        Files.createDirectories(outDir);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> args = new ArrayList<String>(Arrays.asList("-nowarn", "-Xlint:none", "-proc:none",
                "-implicit:none", "-encoding", "UTF-8", "-cp", cp, "-d", outDir.toString()));
        args.addAll(sources);
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int rc = compiler.run(null, null, err, args.toArray(new String[0]));
        return rc == 0 ? null : err.toString(StandardCharsets.UTF_8.name());
    }

    private static String firstLine(String s) {
        int i = s.indexOf('\n');
        return i < 0 ? s : s.substring(0, i);
    }

    private static String clip(String s) {
        if (s == null) return "";
        s = s.replace('\t', ' ').replace('\n', ' ');
        return s.length() > 300 ? s.substring(0, 300) : s;
    }

    static void deleteRecursively(Path f) throws IOException {
        if (!Files.exists(f)) return;
        try (Stream<Path> w = Files.walk(f)) {
            for (Path x : w.sorted(Collections.reverseOrder()).collect(Collectors.toList())) Files.delete(x);
        }
    }
}

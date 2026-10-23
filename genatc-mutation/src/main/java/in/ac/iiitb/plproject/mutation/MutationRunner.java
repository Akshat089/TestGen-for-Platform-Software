package in.ac.iiitb.plproject.mutation;

import in.ac.iiitb.plproject.atc.LibraryRegistry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Runs the hand-written mutation sets against the generated test and reports
 * what it caught.
 *
 * <p>SPF only sees the contract, so the test depends only on (spec,
 * sequence). It is generated ONCE per library on the correct library,
 * compiled once ({@link TestCache}), and that same compiled test is run
 * against every mutant: seed the fault into a private copy of the library,
 * compile that copy once, run the cached test against it in one child JVM.
 * Nothing is regenerated and SPF never runs per mutant.
 *
 * <p>A mutant is <strong>killed</strong> when any PRE or POST assertion
 * fails, the library throws, or the test times out; it
 * <strong>survives</strong> when the test passes; it is
 * <strong>invalid</strong> when the fault cannot be placed, the mutant does
 * not compile, or the library has no test (no test generated, or the test
 * already fails on the correct library). Score = killed / (killed +
 * survived). A survivor is a measurement, not a bug in the runner: the spec
 * and the sequence together never constrained the behaviour it changed.
 *
 * <pre>
 *   cd genatc-mutation
 *   mvn -o -pl . -am compile    # (or: mvn -o install from the repo root once)
 *   java -cp target/classes:../pl-platform-testing/target/classes in.ac.iiitb.plproject.mutation.MutationRunner
 *   ... MutationRunner stack taskqueue
 * </pre>
 */
public class MutationRunner {

    /** Where a mutant ended up. */
    public enum Status {
        /** A PRE/POST assertion failed, the library threw, or the test timed out. */
        KILLED,
        /** The test passed: nothing it checks constrains this behaviour. */
        SURVIVED,
        /** The fault could not be placed, the mutant does not compile, or the library has no test. */
        INVALID
    }

    /** The verdict on one mutant, with the evidence behind it. */
    public static class MutantResult {
        private final Mutant mutant;
        private final Status status;
        private final String detail;
        /** KILLED only: PRE, POST, EXCEPTION or TIMEOUT. */
        private final String killKind;
        /** A SURVIVED mutant that a family test killed: that test's name (phase 3), else null. */
        private String killedByFamily;
        private String familyKillKind;

        MutantResult(Mutant mutant, Status status, String detail, String killKind) {
            this.mutant = mutant;
            this.status = status;
            this.detail = detail;
            this.killKind = killKind;
        }

        public Mutant getMutant() { return mutant; }
        public Status getStatus() { return status; }
        public String getDetail() { return detail; }
        public String getKillKind() { return killKind; }
        public String getKilledByFamily() { return killedByFamily; }
        public String getFamilyKillKind() { return familyKillKind; }

        public boolean isKilled()   { return status == Status.KILLED; }
        public boolean isSurvivor() { return status == Status.SURVIVED; }

        /** True when the run disagrees with what the mutation set declared. */
        public boolean isUnexpected() {
            if (status == Status.INVALID) return true;
            return isKilled() != mutant.isExpectedKilled();
        }

        public String expectationMismatch() {
            if (status == Status.INVALID) return "could not be run: " + detail;
            return "declared expect: " + (mutant.isExpectedKilled() ? "killed" : "survives")
                    + ", but the run reports " + status;
        }
    }

    /** Every result, plus the counts and score derived from them. */
    public static class Report {
        private final List<MutantResult> results;

        Report(List<MutantResult> results) {
            this.results = Collections.unmodifiableList(results);
        }

        public List<MutantResult> getResults() { return results; }

        public int total()     { return results.size(); }
        public int killed()    { return count(Status.KILLED); }
        public int survived()  { return count(Status.SURVIVED); }
        public int invalid()   { return count(Status.INVALID); }

        private int count(Status status) {
            int found = 0;
            for (MutantResult result : results) if (result.getStatus() == status) found++;
            return found;
        }

        public double mutationScore() {
            int scored = killed() + survived();
            return scored == 0 ? 0.0 : (100.0 * killed()) / scored;
        }

        public List<MutantResult> unexpected() {
            List<MutantResult> mismatches = new ArrayList<MutantResult>();
            for (MutantResult result : results) if (result.isUnexpected()) mismatches.add(result);
            return mismatches;
        }

        public List<MutantResult> survivors() {
            List<MutantResult> survivors = new ArrayList<MutantResult>();
            for (MutantResult result : results) if (result.isSurvivor()) survivors.add(result);
            return survivors;
        }

        public Map<String, List<MutantResult>> byLibrary() {
            Map<String, List<MutantResult>> grouped = new LinkedHashMap<String, List<MutantResult>>();
            for (MutantResult result : results) {
                String key = result.getMutant().getLibraryKey();
                if (!grouped.containsKey(key)) grouped.put(key, new ArrayList<MutantResult>());
                grouped.get(key).add(result);
            }
            return grouped;
        }

        public Map<String, List<MutantResult>> byOperator() {
            Map<String, List<MutantResult>> grouped = new LinkedHashMap<String, List<MutantResult>>();
            for (String operator : MutationOperator.names()) {
                for (MutantResult result : results) {
                    if (!result.getMutant().getOperator().equals(operator)) continue;
                    List<MutantResult> bucket = grouped.get(operator);
                    if (bucket == null) {
                        bucket = new ArrayList<MutantResult>();
                        grouped.put(operator, bucket);
                    }
                    bucket.add(result);
                }
            }
            return grouped;
        }
    }

    private static final Path DEFAULT_MUTATIONS = Paths.get("mutations");
    private static final Path DEFAULT_WORK = Paths.get("target", "mutation");

    private final Path mutationsDir;
    private final Path workDir;

    public MutationRunner() {
        this(DEFAULT_MUTATIONS, DEFAULT_WORK);
    }

    public MutationRunner(Path mutationsDir, Path workDir) {
        this.mutationsDir = mutationsDir;
        this.workDir = workDir;
    }

    /** One library's cached test for its checked-in sequence, and how it did on the correct library. */
    public static class Baseline {
        public final TestCache cache;
        public final TestCache.CachedTest test;
        /** Null when there is no test to run. */
        public final TestCache.TestRun onCorrect;

        Baseline(TestCache cache, TestCache.CachedTest test, TestCache.TestRun onCorrect) {
            this.cache = cache;
            this.test = test;
            this.onCorrect = onCorrect;
        }

        /** True when there is a test and it passes on the correct library. */
        public boolean usable() { return onCorrect != null && onCorrect.passed; }

        public String whyUnusable() {
            if (!test.hasTest()) return "the library has no test: " + test.status + " " + test.detail;
            if (onCorrect == null) return "the test did not run";
            return "the test fails on the correct library: " + onCorrect.kind + " " + onCorrect.message;
        }
    }

    /** Generates (once) and compiles the test for {@code entry}'s checked-in sequence, then runs it on the correct library. */
    public Baseline baseline(LibraryRegistry.Entry entry) throws Exception {
        TestCache cache = new TestCache(entry, workDir.resolve("cache").resolve(entry.key));
        TestCache.CachedTest test = cache.generate(entry.key + "_main", entry.testsPath);
        TestCache.TestRun run = null;
        if (test.hasTest()) {
            run = cache.runOnCorrectLibrary(Collections.singletonList(test.className)).get(test.className);
        }
        return new Baseline(cache, test, run);
    }

    /** Runs every mutant of every library named in {@code libraryKeys} (empty means all). */
    public Report run(List<String> libraryKeys, boolean verbose) throws Exception {
        List<Mutant> mutants = MutantSetParser.parseDirectory(mutationsDir);
        List<MutantResult> results = new ArrayList<MutantResult>();
        Map<String, Baseline> baselines = new LinkedHashMap<String, Baseline>();

        for (Mutant mutant : mutants) {
            if (!libraryKeys.isEmpty() && !libraryKeys.contains(mutant.getLibraryKey())) continue;
            LibraryRegistry.Entry entry = LibraryRegistry.byKey(mutant.getLibraryKey());
            Baseline baseline = null;
            if (entry != null && entry.isSupported()) {
                baseline = baselines.get(entry.key);
                if (baseline == null) {
                    baseline = baseline(entry);
                    baselines.put(entry.key, baseline);
                }
            }
            MutantResult result = run(mutant, baseline, Collections.<String>emptyList());
            results.add(result);
            if (verbose) {
                System.out.println(String.format("  %-9s %-34s %s%s",
                        result.getStatus(), mutant.getId(), result.getDetail(),
                        result.isUnexpected() ? "   <-- UNEXPECTED" : ""));
            }
        }
        return new Report(results);
    }

    /**
     * Seeds one fault, compiles the mutated library once, and runs the cached
     * test for the checked-in sequence against it — then, if it survives, the
     * cached {@code familyTests} in order until the first one that fails
     * (phase 3), all in the same child JVM.
     */
    public MutantResult run(Mutant mutant, Baseline baseline, List<String> familyTests) throws Exception {
        LibraryRegistry.Entry base = LibraryRegistry.byKey(mutant.getLibraryKey());
        if (base == null) {
            return new MutantResult(mutant, Status.INVALID, "no library named " + mutant.getLibraryKey(), null);
        }
        if (!base.isSupported()) {
            return new MutantResult(mutant, Status.INVALID,
                    "unsupported: " + base.unsupportedReason, null);
        }
        if (baseline == null || !baseline.usable()) {
            return new MutantResult(mutant, Status.INVALID,
                    baseline == null ? "the library has no test" : baseline.whyUnusable(), null);
        }

        Path sandbox = workDir.resolve("mutants").resolve(base.key).resolve(mutant.getId());
        deleteRecursively(sandbox);
        Files.createDirectories(sandbox);

        String target = mutant.getFile() != null ? mutant.getFile() : "Helper.java";
        List<String> sandboxSources = new ArrayList<String>();
        boolean seeded = false;

        for (String librarySourcePath : base.librarySourcePaths) {
            Path source = Paths.get(librarySourcePath);
            String fileName = source.getFileName().toString();
            String text = new String(Files.readAllBytes(source), StandardCharsets.UTF_8);

            if (fileName.equals(target)) {
                try {
                    text = mutant.applyTo(text);
                } catch (Mutant.MutationNotApplicable notApplicable) {
                    return new MutantResult(mutant, Status.INVALID, notApplicable.getMessage(), null);
                }
                seeded = true;
            }

            Path sandboxCopy = sandbox.resolve(fileName);
            Files.write(sandboxCopy, text.getBytes(StandardCharsets.UTF_8));
            sandboxSources.add(sandboxCopy.toString());
        }

        if (!seeded) {
            return new MutantResult(mutant, Status.INVALID,
                    "no library source named " + target + " in the " + mutant.getLibraryKey()
                    + " library, which is built from " + base.librarySourcePaths, null);
        }

        Path classes = sandbox.resolve("classes");
        String compileError = TestCache.compileLibrary(sandboxSources, classes);
        if (compileError != null) {
            return new MutantResult(mutant, Status.INVALID,
                    "the mutated library does not compile: " + firstLine(compileError), null);
        }

        List<String> tests = new ArrayList<String>();
        tests.add(baseline.test.className);
        tests.addAll(familyTests);
        Map<String, TestCache.TestRun> runs = baseline.cache.runTests(classes, tests, true);
        deleteRecursively(sandbox);

        TestCache.TestRun main = runs.get(baseline.test.className);
        if (main == null) {
            return new MutantResult(mutant, Status.INVALID, "the test did not report", null);
        }
        if (!main.passed) {
            return new MutantResult(mutant, Status.KILLED, "killed by " + main.kind + ": " + main.message,
                    main.kind);
        }
        MutantResult survived = new MutantResult(mutant, Status.SURVIVED, "the test passes", null);
        for (TestCache.TestRun r : runs.values()) {
            if (!r.passed) {
                survived.killedByFamily = r.className.substring(r.className.lastIndexOf('.') + 1);
                survived.familyKillKind = r.kind;
                break;
            }
        }
        return survived;
    }

    static void deleteRecursively(Path dir) throws IOException {
        if (!Files.exists(dir)) return;
        Files.walk(dir)
             .sorted(java.util.Comparator.reverseOrder())
             .forEach(p -> { try { Files.delete(p); } catch (IOException ignored) { } });
    }

    private static String firstLine(String text) {
        if (text == null) return "";
        int newline = text.indexOf('\n');
        return newline < 0 ? text : text.substring(0, newline);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // command line
    // ─────────────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        List<String> libraryKeys = new ArrayList<String>();
        for (String arg : args) {
            if (LibraryRegistry.byKey(arg) == null) {
                System.err.println("Unknown library: " + arg
                        + " (expected one of " + LibraryRegistry.keys() + ")");
                System.exit(2);
            }
            libraryKeys.add(arg.toLowerCase());
        }

        MutationRunner runner = new MutationRunner();
        long startedAt = System.currentTimeMillis();

        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("BASELINE — each library's test, generated once, on the correct library");
        System.out.println("════════════════════════════════════════════════════════════════");
        for (LibraryRegistry.Entry entry : LibraryRegistry.ALL) {
            if (!entry.isSupported()) continue;
            if (!libraryKeys.isEmpty() && !libraryKeys.contains(entry.key)) continue;
            Baseline b = runner.baseline(entry);
            System.out.println(String.format("  %-14s %-6s rounds=%d spf=%dms  inputs=%s  %s", entry.key,
                    b.usable() ? "PASS" : "NO", b.test.rounds, b.test.spfMillis, b.test.inputs,
                    b.usable() ? "" : b.whyUnusable()));
        }

        System.out.println();
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("MUTATION RUN — the cached test against every mutant");
        System.out.println("════════════════════════════════════════════════════════════════");
        Report report = runner.run(libraryKeys, true);

        System.out.println();
        printSummary(report, System.out);
        System.out.println();
        System.out.println("wall time: " + (System.currentTimeMillis() - startedAt) / 1000 + "s");
        System.exit(report.unexpected().isEmpty() ? 0 : 1);
    }

    public static void printSummary(Report report, java.io.PrintStream out) {
        out.println("════════════════════════════════════════════════════════════════");
        out.println("SUMMARY");
        out.println("════════════════════════════════════════════════════════════════");
        out.println(String.format("%-12s %8s %8s %9s %8s %s",
                "library", "mutants", "killed", "survived", "invalid", "score"));
        for (Map.Entry<String, List<MutantResult>> entry : report.byLibrary().entrySet()) {
            Report perLibrary = new Report(entry.getValue());
            out.println(String.format("%-12s %8d %8d %9d %8d %5.1f%%",
                    entry.getKey(), perLibrary.total(), perLibrary.killed(),
                    perLibrary.survived(), perLibrary.invalid(), perLibrary.mutationScore()));
        }
        out.println(String.format("%-12s %8d %8d %9d %8d %5.1f%%",
                "ALL", report.total(), report.killed(), report.survived(),
                report.invalid(), report.mutationScore()));

        out.println();
        out.println(String.format("%-34s %-5s %8s %8s %9s %8s %s",
                "operator", "abbr", "mutants", "killed", "survived", "invalid", "score"));
        for (String operator : MutationOperator.names()) {
            List<MutantResult> bucket = report.byOperator().get(operator);
            Report perOperator = new Report(bucket == null ? new ArrayList<MutantResult>() : bucket);
            out.println(String.format("%-34s %-5s %8d %8d %9d %8d %5.1f%%",
                    operator, MutationOperator.abbreviationOf(operator),
                    perOperator.total(), perOperator.killed(), perOperator.survived(),
                    perOperator.invalid(), perOperator.mutationScore()));
        }

        out.println();
        out.println("KILLED BY");
        Map<String, Integer> kinds = new LinkedHashMap<String, Integer>();
        for (String k : new String[] { "POST", "PRE", "EXCEPTION", "TIMEOUT" }) kinds.put(k, 0);
        for (MutantResult r : report.getResults()) {
            if (r.isKilled()) kinds.put(r.getKillKind(), kinds.getOrDefault(r.getKillKind(), 0) + 1);
        }
        for (Map.Entry<String, Integer> e : kinds.entrySet()) {
            out.println(String.format("  %-10s %d", e.getKey(), e.getValue()));
        }

        out.println();
        out.println("ALL RESULTS");
        for (MutantResult r : report.getResults()) {
            out.println(String.format("  %-9s %-9s %-40s %-34s expect:%s  %s", r.getStatus(),
                    r.getKillKind() == null ? "-" : r.getKillKind(), r.getMutant().getId(),
                    r.getMutant().getOperator(), r.getMutant().isExpectedKilled() ? "killed" : "survives",
                    r.getDetail()));
        }

        if (!report.survivors().isEmpty()) {
            out.println();
            out.println("SURVIVORS — behaviour the spec and test string do not pin down:");
            for (MutantResult survivor : report.survivors()) {
                out.println("  " + survivor.getMutant().getId());
                out.println("      " + survivor.getMutant().getDescription());
                if (!survivor.getMutant().getNote().isEmpty()) {
                    out.println("      why: " + survivor.getMutant().getNote());
                }
            }
        }

        if (!report.unexpected().isEmpty()) {
            out.println();
            out.println("UNEXPECTED — the run disagrees with the mutation set's declaration:");
            for (MutantResult mismatch : report.unexpected()) {
                out.println("  " + mismatch.getMutant().getId() + ": " + mismatch.expectationMismatch());
            }
        }
    }
}

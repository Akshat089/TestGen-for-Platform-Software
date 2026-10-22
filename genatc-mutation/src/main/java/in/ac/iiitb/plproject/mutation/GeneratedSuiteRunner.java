package in.ac.iiitb.plproject.mutation;

import in.ac.iiitb.plproject.atc.LibraryRegistry;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Builds and runs a generated test suite for every library, and measures it with
 * the generated mutation suite.
 *
 * <p>Three phases per library, each answering a different question:
 *
 * <ol>
 *   <li><b>family</b> — generate a family of sequences from the spec, run SPF on
 *       each ONCE, cache the resulting test (or the INCORRECT SEQUENCE report),
 *       and run every cached test on the correct library: passed / incorrect
 *       sequence / failed.</li>
 *   <li><b>mutants</b> — generate the operator mutants and run each against the
 *       cached test for the library's checked-in sequence. This is the
 *       per-operator score: what one sequence and the spec, together, pin down.</li>
 *   <li><b>survivors against the family</b> — every phase-2 survivor against the
 *       cached tests of the passing family sequences, first kill wins.</li>
 * </ol>
 *
 * <p>Because SPF only sees the contract, every test depends only on (spec,
 * sequence) and is generated and compiled once. Each mutant is compiled once
 * and phases 2 and 3 run in ONE child JVM: the checked-in sequence's test
 * first, then — only if it passed — the family tests in order until one fails
 * ({@link MutationRunner#run(Mutant, MutationRunner.Baseline, List)}).
 * Libraries run in parallel; each library's TSVs and the summary are written
 * as soon as that library finishes, so partial results survive a crash.
 *
 * <pre>
 *   ./run-operator-suites.sh                  # every library, all three phases
 *   ./run-operator-suites.sh stack queue      # a subset
 * </pre>
 */
public class GeneratedSuiteRunner {

    /** Everything this runner writes, relative to the genatc-mutation working directory. */
    private static final Path SUITES       = Paths.get("operator-suites");
    private static final Path TEST_STRINGS = SUITES.resolve("teststrings");
    private static final Path RESULTS      = SUITES.resolve("results");

    private static final Path MUTATIONS = Paths.get("mutations");
    private static final Path WORK      = Paths.get("target", "operator-suites");

    /** Default family size per library; {@code -Dgenatc.family.<key>=N} overrides one library. */
    public static final int TEST_STRINGS_PER_LIBRARY = Integer.getInteger("genatc.family", 260);

    /** The cap on a library's mutation suite; the generator produces what the source admits. */
    public static final int MUTANTS_PER_LIBRARY = 300;

    /** Cached tests run on the correct library per child JVM in phase 1. */
    private static final int PHASE1_BATCH = 50;

    /** What one generated test string did when it was run. */
    public enum TestStringStatus {
        /** The test passed on the correct library. */
        PASSED,
        /** SPF found no solution: a call's precondition cannot hold — no test. */
        INCORRECT_SEQUENCE,
        /** The test failed on the correct library — a finding against the contract. */
        FAILED,
        /** Every precondition can hold but a postcondition cannot — no test. */
        UNSAT_POSTCONDITION,
        /** SPF or the solver failed. */
        SOLVER_ERROR,
        /** SPF ran out of time. */
        TIMEOUT,
        /** The generated sources did not compile. */
        INVALID
    }

    /** One generated test string and what came of it. */
    public static class TestStringResult {
        final String name;
        final String sequence;
        final TestStringStatus status;
        final long spfMillis;
        final String detail;
        final TestCache.CachedTest test;

        TestStringResult(String name, String sequence, TestStringStatus status, long spfMillis, String detail,
                         TestCache.CachedTest test) {
            this.name = name;
            this.sequence = sequence;
            this.status = status;
            this.spfMillis = spfMillis;
            this.detail = detail;
            this.test = test;
        }

        public TestStringStatus getStatus() { return status; }
    }

    /** Everything one library produced across the three phases. */
    public static class LibraryReport {
        final String key;
        int familySize;
        final List<TestStringResult> testStrings = new ArrayList<TestStringResult>();
        final List<MutationRunner.MutantResult> mutants = new ArrayList<MutationRunner.MutantResult>();
        String baselineProblem;
        long testStringSeconds;
        long mutantSeconds;
        long totalSeconds;

        LibraryReport(String key) { this.key = key; }

        public String getKey() { return key; }

        int testStrings(TestStringStatus status) {
            int n = 0;
            for (TestStringResult result : testStrings) if (result.status == status) n++;
            return n;
        }

        int mutants(MutationRunner.Status status) {
            int n = 0;
            for (MutationRunner.MutantResult result : mutants) if (result.getStatus() == status) n++;
            return n;
        }

        int killedByFamily() {
            int n = 0;
            for (MutationRunner.MutantResult result : mutants) if (result.getKilledByFamily() != null) n++;
            return n;
        }

        /** killed / (killed + survived) against the checked-in test string alone. */
        double scoreAlone() {
            int killed = mutants(MutationRunner.Status.KILLED);
            int scored = killed + mutants(MutationRunner.Status.SURVIVED);
            return scored == 0 ? 0.0 : (100.0 * killed) / scored;
        }

        /** killed / (killed + survived) once the generated family has had its turn. */
        double scoreWithFamily() {
            int killed = mutants(MutationRunner.Status.KILLED) + killedByFamily();
            int scored = mutants(MutationRunner.Status.KILLED)
                       + mutants(MutationRunner.Status.SURVIVED);
            return scored == 0 ? 0.0 : (100.0 * killed) / scored;
        }
    }

    public static void main(String[] args) throws Exception {
        List<LibraryRegistry.Entry> selected = new ArrayList<LibraryRegistry.Entry>();
        for (String arg : args) {
            LibraryRegistry.Entry entry = LibraryRegistry.byKey(arg);
            if (entry == null) {
                System.err.println("Unknown library: " + arg
                        + " (expected one of " + LibraryRegistry.keys() + ")");
                System.exit(2);
            }
            selected.add(entry);
        }
        if (selected.isEmpty()) {
            for (LibraryRegistry.Entry e : LibraryRegistry.ALL) selected.add(e);
        }

        Files.createDirectories(SUITES);
        Files.createDirectories(TEST_STRINGS);
        Files.createDirectories(RESULTS);

        List<LibraryRegistry.Entry> runnable = new ArrayList<LibraryRegistry.Entry>();
        for (LibraryRegistry.Entry entry : selected) {
            if (entry.isSupported()) runnable.add(entry);
            else log(entry.key, "skipped (unsupported: " + entry.unsupportedReason + ")");
        }

        int workers = workers(runnable.size());
        log("all", "running " + runnable.size() + " libraries on " + workers + " workers ("
                + Runtime.getRuntime().availableProcessors() + " cores)");
        long startedAt = System.currentTimeMillis();

        final Map<String, LibraryReport> reports = new LinkedHashMap<String, LibraryReport>();
        ExecutorService pool = Executors.newFixedThreadPool(workers);
        List<Future<?>> futures = new ArrayList<Future<?>>();
        for (final LibraryRegistry.Entry entry : runnable) {
            futures.add(pool.submit(() -> {
                try {
                    LibraryReport report = runLibrary(entry);
                    synchronized (reports) {
                        reports.put(entry.key, report);
                        writeTestStringResults(report);
                        writeMutantResults(report);
                        writeSummary(ordered(reports, runnable));
                    }
                } catch (Throwable t) {
                    log(entry.key, "FAILED: " + t);
                    t.printStackTrace();
                }
                return null;
            }));
        }
        pool.shutdown();
        pool.awaitTermination(7, TimeUnit.DAYS);

        Map<String, LibraryReport> all = ordered(reports, runnable);
        printSummary(all, System.out);
        writeSummary(all);
        log("all", "wall time " + (System.currentTimeMillis() - startedAt) / 1000 + "s");
    }

    /**
     * One worker per core, but no more than there are libraries, and no more
     * than free memory allows: each worker keeps up to one SPF process (about
     * 170 MB resident, measured) and one test JVM alive at a time.
     */
    static int workers(int libraries) {
        Integer forced = Integer.getInteger("genatc.workers");
        if (forced != null) return Math.max(1, forced);
        int cores = Runtime.getRuntime().availableProcessors();
        long availableMb = availableMemoryMb();
        int byMemory = availableMb > 0 ? (int) Math.max(1, availableMb / 300) : cores;
        return Math.max(1, Math.min(Math.min(cores, libraries), byMemory));
    }

    private static long availableMemoryMb() {
        try {
            for (String line : Files.readAllLines(Paths.get("/proc/meminfo"))) {
                if (line.startsWith("MemAvailable:")) {
                    return Long.parseLong(line.replaceAll("[^0-9]", "")) / 1024;
                }
            }
        } catch (IOException ignored) {
            // not Linux: fall back to the core count
        }
        return -1;
    }

    private static Map<String, LibraryReport> ordered(Map<String, LibraryReport> reports,
                                                      List<LibraryRegistry.Entry> order) {
        Map<String, LibraryReport> out = new LinkedHashMap<String, LibraryReport>();
        synchronized (reports) {
            for (LibraryRegistry.Entry e : order) if (reports.containsKey(e.key)) out.put(e.key, reports.get(e.key));
        }
        return out;
    }

    static LibraryReport runLibrary(LibraryRegistry.Entry entry) throws Exception {
        long startedAt = System.currentTimeMillis();
        LibraryReport report = new LibraryReport(entry.key);
        MutationRunner runner = new MutationRunner(MUTATIONS, WORK);
        MutationRunner.Baseline baseline = runner.baseline(entry);
        log(entry.key, "checked-in sequence: " + (baseline.usable() ? "test passes on the correct library"
                : baseline.whyUnusable()));
        if (!baseline.usable()) report.baselineProblem = baseline.whyUnusable();

        List<String> passingFamily = runTestStrings(entry, baseline.cache, report);
        runMutants(entry, runner, baseline, passingFamily, report);
        report.totalSeconds = (System.currentTimeMillis() - startedAt) / 1000;
        log(entry.key, "done in " + report.totalSeconds + "s");
        return report;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // phase 1 — the generated family, each test generated and run once
    // ─────────────────────────────────────────────────────────────────────────

    private static List<String> runTestStrings(LibraryRegistry.Entry entry, TestCache cache,
                                               LibraryReport report) throws Exception {
        in.ac.iiitb.plproject.parser.ast.JmlSpecAst spec =
                in.ac.iiitb.plproject.parser.SpecToAstConverter.convertSpecToAst(read(entry.specPath));
        in.ac.iiitb.plproject.parser.ast.TestStringAst seed =
                in.ac.iiitb.plproject.parser.TestStringParser.parse(read(entry.testsPath));

        int size = Integer.getInteger("genatc.family." + entry.key, TEST_STRINGS_PER_LIBRARY);
        report.familySize = size;
        // The seed is the library key, so a family is the same on every machine and
        // in every run: a suite that changed between runs could not be a baseline.
        TestStringGenerator generator = new TestStringGenerator(spec, seed, entry.key.hashCode());
        List<String> texts = generator.generate(capitalise(entry.key) + "Generated", size);

        Path libraryDir = TEST_STRINGS.resolve(entry.key);
        MutationRunner.deleteRecursively(libraryDir);
        Files.createDirectories(libraryDir);

        log(entry.key, "phase 1 — " + texts.size() + " generated test strings");
        long startedAt = System.currentTimeMillis();

        List<TestCache.CachedTest> tests = new ArrayList<TestCache.CachedTest>();
        List<String> names = new ArrayList<String>();
        for (int index = 0; index < texts.size(); index++) {
            String name = String.format("%s-%03d", entry.key, index + 1);
            Path testsPath = libraryDir.resolve(name + ".tests");
            Files.write(testsPath, texts.get(index).getBytes(StandardCharsets.UTF_8));
            TestCache.CachedTest test;
            try {
                test = cache.generate(name, testsPath.toString());
            } catch (Exception problem) {
                test = null;
            }
            tests.add(test);
            names.add(name);
            if ((index + 1) % 50 == 0) log(entry.key, "phase 1 — " + (index + 1) + "/" + texts.size() + " generated");
        }

        // run every cached test on the correct library, a batch per JVM
        List<String> classNames = new ArrayList<String>();
        for (TestCache.CachedTest t : tests) if (t != null && t.hasTest()) classNames.add(t.className);
        Map<String, TestCache.TestRun> runs = new LinkedHashMap<String, TestCache.TestRun>();
        for (int from = 0; from < classNames.size(); from += PHASE1_BATCH) {
            runs.putAll(cache.runOnCorrectLibrary(
                    classNames.subList(from, Math.min(classNames.size(), from + PHASE1_BATCH))));
        }

        List<String> passing = new ArrayList<String>();
        for (int index = 0; index < tests.size(); index++) {
            TestCache.CachedTest t = tests.get(index);
            String sequence = sequenceOf(new String(Files.readAllBytes(
                    libraryDir.resolve(names.get(index) + ".tests")), StandardCharsets.UTF_8));
            TestStringStatus status;
            String detail;
            if (t == null) {
                status = TestStringStatus.INVALID;
                detail = "the generator threw";
            } else if (t.hasTest()) {
                TestCache.TestRun run = runs.get(t.className);
                if (run == null) {
                    status = TestStringStatus.INVALID;
                    detail = "the test did not run";
                } else if (run.passed) {
                    status = TestStringStatus.PASSED;
                    detail = "";
                    passing.add(t.className);
                } else {
                    status = TestStringStatus.FAILED;
                    detail = run.kind + ": " + run.message;
                }
            } else {
                switch (t.status) {
                    case "INCORRECT_SEQUENCE": status = TestStringStatus.INCORRECT_SEQUENCE; break;
                    case "UNSAT_POSTCONDITION": status = TestStringStatus.UNSAT_POSTCONDITION; break;
                    case "SOLVER_ERROR": status = TestStringStatus.SOLVER_ERROR; break;
                    case "TIMEOUT": status = TestStringStatus.TIMEOUT; break;
                    default: status = TestStringStatus.INVALID; break;
                }
                detail = t.detail;
            }
            report.testStrings.add(new TestStringResult(names.get(index), sequence, status,
                    t == null ? 0 : t.spfMillis, detail, t));
        }
        report.testStringSeconds = (System.currentTimeMillis() - startedAt) / 1000;

        log(entry.key, String.format(
                "phase 1 — %d passed, %d incorrect sequence, %d failed, %d other  (%ds)",
                report.testStrings(TestStringStatus.PASSED),
                report.testStrings(TestStringStatus.INCORRECT_SEQUENCE),
                report.testStrings(TestStringStatus.FAILED),
                report.testStrings.size() - report.testStrings(TestStringStatus.PASSED)
                        - report.testStrings(TestStringStatus.INCORRECT_SEQUENCE)
                        - report.testStrings(TestStringStatus.FAILED),
                report.testStringSeconds));
        return passing;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // phases 2 and 3 — each mutant compiled once, one JVM per mutant
    // ─────────────────────────────────────────────────────────────────────────

    private static void runMutants(LibraryRegistry.Entry entry, MutationRunner runner,
                                   MutationRunner.Baseline baseline, List<String> passingFamily,
                                   LibraryReport report) throws Exception {
        List<Mutant> mutants = OperatorMutantGenerator.generate(
                entry.key, entry.librarySourcePaths, MUTANTS_PER_LIBRARY);

        Files.write(SUITES.resolve(entry.key + ".mutants"),
                OperatorMutantGenerator.render(entry.key, entry.librarySourcePaths, mutants)
                        .getBytes(StandardCharsets.UTF_8));

        log(entry.key, "phases 2+3 — " + mutants.size() + " generated mutants, survivors against "
                + passingFamily.size() + " passing family tests");
        long startedAt = System.currentTimeMillis();
        int done = 0;
        for (Mutant mutant : mutants) {
            report.mutants.add(runner.run(mutant, baseline, passingFamily));
            if (++done % 50 == 0) log(entry.key, "phases 2+3 — " + done + "/" + mutants.size());
        }
        report.mutantSeconds = (System.currentTimeMillis() - startedAt) / 1000;

        log(entry.key, String.format(
                "phase 2 — %d killed, %d survived, %d invalid — %.1f%%; phase 3 — family kills %d — %.1f%%  (%ds)",
                report.mutants(MutationRunner.Status.KILLED),
                report.mutants(MutationRunner.Status.SURVIVED),
                report.mutants(MutationRunner.Status.INVALID),
                report.scoreAlone(), report.killedByFamily(), report.scoreWithFamily(), report.mutantSeconds));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // reports
    // ─────────────────────────────────────────────────────────────────────────

    private static void writeTestStringResults(LibraryReport report) throws IOException {
        StringBuilder out = new StringBuilder();
        out.append("# generated test strings — ").append(report.getKey())
           .append(" (family size ").append(report.familySize).append(", phase 1 ")
           .append(report.testStringSeconds).append("s)\n");
        out.append("# name\tstatus\tspf_ms\tsequence\tdetail\n");
        for (TestStringResult result : report.testStrings) {
            out.append(result.name).append('\t').append(result.status).append('\t')
               .append(result.spfMillis).append('\t').append(result.sequence).append('\t')
               .append(result.detail == null ? "" : result.detail.replace('\t', ' ').replace('\n', ' '))
               .append('\n');
        }
        Files.write(RESULTS.resolve(report.getKey() + ".teststrings.tsv"),
                    out.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static void writeMutantResults(LibraryReport report) throws IOException {
        StringBuilder out = new StringBuilder();
        out.append("# generated mutants — ").append(report.getKey())
           .append(" (phases 2+3 ").append(report.mutantSeconds).append("s, library total ")
           .append(report.totalSeconds).append("s)\n");
        out.append("# id\tstatus\tkill_kind\toperator\tmethod\tfile\tkilled-by-family\tfamily_kill_kind\tdetail\n");
        for (MutationRunner.MutantResult result : report.mutants) {
            Mutant mutant = result.getMutant();
            out.append(mutant.getId()).append('\t')
               .append(result.getStatus()).append('\t')
               .append(result.getKillKind() == null ? "-" : result.getKillKind()).append('\t')
               .append(mutant.getOperator()).append('\t')
               .append(mutant.getMethod() == null ? "-" : mutant.getMethod()).append('\t')
               .append(mutant.getFile() == null ? "Helper.java" : mutant.getFile()).append('\t')
               .append(result.getKilledByFamily() == null ? "-" : result.getKilledByFamily()).append('\t')
               .append(result.getFamilyKillKind() == null ? "-" : result.getFamilyKillKind()).append('\t')
               .append(result.getDetail().replace('\t', ' ').replace('\n', ' ')).append('\n');
        }
        Files.write(RESULTS.resolve(report.getKey() + ".mutants.tsv"),
                    out.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static void writeSummary(Map<String, LibraryReport> reports) throws IOException {
        StringBuilder out = new StringBuilder();
        out.append("# generated suites — summary (contract-only SPF; each test generated once)\n\n");
        appendTestStringTable(reports, out);
        out.append('\n');
        appendMutantTable(reports, out);
        out.append('\n');
        appendOperatorTable(reports, out);
        Files.write(SUITES.resolve("SUMMARY.txt"), out.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static void printSummary(Map<String, LibraryReport> reports, PrintStream out) {
        StringBuilder text = new StringBuilder();
        text.append('\n');
        text.append("════════════════════════════════════════════════════════════════\n");
        text.append("GENERATED SUITES — SUMMARY\n");
        text.append("════════════════════════════════════════════════════════════════\n\n");
        appendTestStringTable(reports, text);
        text.append('\n');
        appendMutantTable(reports, text);
        text.append('\n');
        appendOperatorTable(reports, text);
        out.print(text);
    }

    private static void appendTestStringTable(Map<String, LibraryReport> reports, StringBuilder out) {
        out.append("PHASE 1 — generated test strings\n");
        out.append(String.format("%-12s %6s %7s %10s %7s %7s %8s%n",
                "library", "family", "passed", "incorrect", "failed", "other", "seconds"));
        int total = 0, passed = 0, incorrect = 0, failed = 0, other = 0;
        for (LibraryReport report : reports.values()) {
            int p = report.testStrings(TestStringStatus.PASSED);
            int i = report.testStrings(TestStringStatus.INCORRECT_SEQUENCE);
            int f = report.testStrings(TestStringStatus.FAILED);
            int o = report.testStrings.size() - p - i - f;
            out.append(String.format("%-12s %6d %7d %10d %7d %7d %8d%n",
                    report.getKey(), report.testStrings.size(), p, i, f, o, report.testStringSeconds));
            total += report.testStrings.size();
            passed += p; incorrect += i; failed += f; other += o;
        }
        out.append(String.format("%-12s %6d %7d %10d %7d %7d%n",
                "ALL", total, passed, incorrect, failed, other));
    }

    private static void appendMutantTable(Map<String, LibraryReport> reports, StringBuilder out) {
        out.append("PHASE 2 and 3 — generated mutants, alone and against the family\n");
        out.append(String.format("%-12s %8s %8s %9s %8s %8s %9s %12s %8s%n",
                "library", "mutants", "killed", "survived", "invalid", "alone", "by family", "with family",
                "seconds"));
        int total = 0, killed = 0, survived = 0, invalid = 0, byFamily = 0;
        for (LibraryReport report : reports.values()) {
            out.append(String.format("%-12s %8d %8d %9d %8d %7.1f%% %9d %11.1f%% %8d%n",
                    report.getKey(), report.mutants.size(),
                    report.mutants(MutationRunner.Status.KILLED),
                    report.mutants(MutationRunner.Status.SURVIVED),
                    report.mutants(MutationRunner.Status.INVALID),
                    report.scoreAlone(), report.killedByFamily(),
                    report.scoreWithFamily(), report.mutantSeconds));
            total += report.mutants.size();
            killed += report.mutants(MutationRunner.Status.KILLED);
            survived += report.mutants(MutationRunner.Status.SURVIVED);
            invalid += report.mutants(MutationRunner.Status.INVALID);
            byFamily += report.killedByFamily();
        }
        int scored = killed + survived;
        out.append(String.format("%-12s %8d %8d %9d %8d %7.1f%% %9d %11.1f%%%n",
                "ALL", total, killed, survived, invalid,
                scored == 0 ? 0.0 : (100.0 * killed) / scored, byFamily,
                scored == 0 ? 0.0 : (100.0 * (killed + byFamily)) / scored));
    }

    private static void appendOperatorTable(Map<String, LibraryReport> reports, StringBuilder out) {
        out.append("BY OPERATOR\n");
        out.append(String.format("%-34s %-5s %8s %8s %9s %8s %8s %s%n",
                "operator", "abbr", "mutants", "killed", "survived", "invalid", "alone", "with family"));
        for (String operator : MutationOperator.names()) {
            int total = 0, killed = 0, survived = 0, invalid = 0, byFamily = 0;
            for (LibraryReport report : reports.values()) {
                for (MutationRunner.MutantResult result : report.mutants) {
                    if (!result.getMutant().getOperator().equals(operator)) continue;
                    total++;
                    switch (result.getStatus()) {
                        case KILLED:   killed++; break;
                        case SURVIVED:
                            survived++;
                            if (result.getKilledByFamily() != null) byFamily++;
                            break;
                        default:       invalid++; break;
                    }
                }
            }
            int scored = killed + survived;
            out.append(String.format("%-34s %-5s %8d %8d %9d %8d %7.1f%% %10.1f%%%n",
                    operator, MutationOperator.abbreviationOf(operator),
                    total, killed, survived, invalid,
                    scored == 0 ? 0.0 : (100.0 * killed) / scored,
                    scored == 0 ? 0.0 : (100.0 * (killed + byFamily)) / scored));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // small helpers
    // ─────────────────────────────────────────────────────────────────────────

    private static void log(String key, String message) {
        synchronized (System.out) {
            System.out.println(String.format("[%tT] %-10s %s", System.currentTimeMillis(), key, message));
            System.out.flush();
        }
    }

    private static String read(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }

    /** The sequence line of a generated test string, for the report. */
    private static String sequenceOf(String text) {
        int at = text.indexOf("sequence:");
        if (at < 0) return "?";
        int end = text.indexOf(';', at);
        return text.substring(at + "sequence:".length(), end < 0 ? text.length() : end).trim();
    }

    private static String capitalise(String key) {
        return Character.toUpperCase(key.charAt(0)) + key.substring(1);
    }
}

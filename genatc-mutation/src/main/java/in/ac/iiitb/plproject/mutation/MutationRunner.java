package in.ac.iiitb.plproject.mutation;

import in.ac.iiitb.plproject.atc.GenatcMain;
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
 * Runs the whole pipeline against every mutant and reports what the generated
 * JUnit suite caught.
 *
 * <p><b>Adapted for the new pipeline</b> (the generator no longer produces a
 * {@code SingularCase}/condition-by-condition report; it produces one
 * {@code GeneratedATCsTest} JUnit class, verified against the library by
 * {@link GenatcMain#runOne}). For each mutant: seed the fault into a private
 * copy of the library, ask {@link GenatcMain#runOne} to regenerate the IR,
 * run real SPF, and verify the resulting JUnit class against the mutated
 * library, then classify the outcome. Nothing is cached between mutants.
 *
 * <p>A mutant is <strong>killed</strong> when a generated JUnit assertion
 * fails against it, or when it makes the sequence infeasible where the
 * unmutated library completed it; it <strong>survives</strong> when the
 * generated suite still passes in full. A survivor is a measurement, not a
 * bug in the runner: it says the spec and the test string together never
 * constrained the behaviour that mutant changed.
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
        /** A generated assertion rejected the seeded fault, or it made the sequence infeasible. */
        KILLED,
        /** The generated suite still passed in full: nothing constrains this behaviour. */
        SURVIVED,
        /** The mutant could not be seeded, or the mutated library/generated suite did not compile. */
        INVALID
    }

    /** The verdict on one mutant, with the evidence behind it. */
    public static class MutantResult {
        private final Mutant mutant;
        private final Status status;
        private final String detail;
        private final GenatcMain.LibraryOutcome outcome;

        MutantResult(Mutant mutant, Status status, String detail, GenatcMain.LibraryOutcome outcome) {
            this.mutant = mutant;
            this.status = status;
            this.detail = detail;
            this.outcome = outcome;
        }

        public Mutant getMutant() { return mutant; }
        public Status getStatus() { return status; }
        public String getDetail() { return detail; }
        public GenatcMain.LibraryOutcome getOutcome() { return outcome; }

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

    /**
     * Verifies the unmutated library first. A mutation score only means something
     * if the suite passes on the original.
     */
    public GenatcMain.LibraryOutcome baseline(LibraryRegistry.Entry entry) throws Exception {
        return GenatcMain.runOne(entry);
    }

    /** Runs every mutant of every library named in {@code libraryKeys} (empty means all). */
    public Report run(List<String> libraryKeys, boolean verbose) throws Exception {
        List<Mutant> mutants = MutantSetParser.parseDirectory(mutationsDir);
        List<MutantResult> results = new ArrayList<MutantResult>();

        for (Mutant mutant : mutants) {
            if (!libraryKeys.isEmpty() && !libraryKeys.contains(mutant.getLibraryKey())) continue;
            MutantResult result = run(mutant);
            results.add(result);
            if (verbose) {
                System.out.println(String.format("  %-9s %-34s %s%s",
                        result.getStatus(), mutant.getId(), result.getDetail(),
                        result.isUnexpected() ? "   <-- UNEXPECTED" : ""));
            }
        }
        return new Report(results);
    }

    public MutantResult run(Mutant mutant) throws Exception {
        return run(mutant, null);
    }

    /**
     * Seeds one fault, regenerates, runs real SPF and the generated JUnit suite
     * against the mutated library; classifies what came back.
     *
     * @param testsPath the sequence to run the mutant against, or null for the
     *        library's checked-in one.
     */
    public MutantResult run(Mutant mutant, String testsPath) throws Exception {
        LibraryRegistry.Entry base = LibraryRegistry.byKey(mutant.getLibraryKey());
        if (base == null) {
            return new MutantResult(mutant, Status.INVALID,
                    "no library named " + mutant.getLibraryKey(), null);
        }
        if (!base.isSupported()) {
            return new MutantResult(mutant, Status.INVALID,
                    "unsupported in the new design: " + base.unsupportedReason, null);
        }

        Path sandbox = workDir.resolve("mutants").resolve(mutant.getId());
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

        LibraryRegistry.Entry variant = LibraryRegistry.variantOf(base, sandboxSources,
                testsPath != null ? testsPath : base.testsPath, sandbox.resolve("out").toString());
        GenatcMain.LibraryOutcome outcome = GenatcMain.runOne(variant);

        switch (outcome.status) {
            case "COMPILE_ERROR":
                return new MutantResult(mutant, Status.INVALID,
                        "the mutated library does not compile: " + firstLine(outcome.detail), outcome);
            case "INFEASIBLE":
            case "CUT_OFF_DEPTH":
            case "CUT_OFF_TIMEOUT":
                return new MutantResult(mutant, Status.KILLED,
                        "killed: the sequence became infeasible under the mutant (" + outcome.status + ")",
                        outcome);
            case "GENERATED":
                if (!outcome.junitCompiled) {
                    return new MutantResult(mutant, Status.INVALID,
                            "the generated JUnit class does not compile against the mutated library",
                            outcome);
                }
                if (!outcome.junitFailures.isEmpty()) {
                    return new MutantResult(mutant, Status.KILLED,
                            "killed by: " + outcome.junitFailures.get(0), outcome);
                }
                return new MutantResult(mutant, Status.SURVIVED,
                        "every generated assertion still holds", outcome);
            default:
                return new MutantResult(mutant, Status.INVALID,
                        "unexpected outcome status " + outcome.status + ": " + outcome.detail, outcome);
        }
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

        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("BASELINE — the unmutated libraries must pass before anything else");
        System.out.println("════════════════════════════════════════════════════════════════");
        for (LibraryRegistry.Entry entry : LibraryRegistry.ALL) {
            if (!entry.isSupported()) continue;
            if (!libraryKeys.isEmpty() && !libraryKeys.contains(entry.key)) continue;
            GenatcMain.LibraryOutcome outcome = runner.baseline(entry);
            boolean pass = "GENERATED".equals(outcome.status) && outcome.junitCompiled
                    && outcome.junitFailures.isEmpty();
            System.out.println(String.format("  %-10s %s  (%d test(s))",
                    entry.key, pass ? "PASS" : "FAIL", outcome.testsGenerated));
            if (!pass) {
                System.err.println("baseline failed for " + entry.key
                        + "; mutation scores would be meaningless: " + outcome.status + " " + outcome.detail);
                System.exit(1);
            }
        }

        System.out.println();
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("MUTATION RUN — whole pipeline re-run per mutant");
        System.out.println("════════════════════════════════════════════════════════════════");
        Report report = runner.run(libraryKeys, true);

        System.out.println();
        printSummary(report, System.out);
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
        out.println(String.format("%-34s %-5s %8s %8s %9s %s",
                "operator", "abbr", "mutants", "killed", "survived", "score"));
        for (Map.Entry<String, List<MutantResult>> entry : report.byOperator().entrySet()) {
            Report perOperator = new Report(entry.getValue());
            out.println(String.format("%-34s %-5s %8d %8d %9d %5.1f%%",
                    entry.getKey(), MutationOperator.abbreviationOf(entry.getKey()),
                    perOperator.total(), perOperator.killed(), perOperator.survived(),
                    perOperator.mutationScore()));
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

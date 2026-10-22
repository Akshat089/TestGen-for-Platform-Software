package in.ac.iiitb.plproject.atc;

import in.ac.iiitb.plproject.check.GenatcInputException;
import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.IrBuilder;
import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.parser.SpecToAstConverter;
import in.ac.iiitb.plproject.parser.TestStringParser;
import in.ac.iiitb.plproject.parser.ast.JmlSpecAst;
import in.ac.iiitb.plproject.parser.ast.TestStringAst;
import in.ac.iiitb.plproject.spf.InputVector;
import in.ac.iiitb.plproject.spf.SpfConfig;
import in.ac.iiitb.plproject.spf.SpfRunResult;
import in.ac.iiitb.plproject.spf.SpfRunner;
import in.ac.iiitb.plproject.symbolic.JUnitPrinter;
import in.ac.iiitb.plproject.symbolic.SymbolicDriverPrinter;
import in.ac.iiitb.plproject.verify.GeneratedSuiteHarness;
import in.ac.iiitb.plproject.verify.JunitJars;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The entry point: contract (.spec) + sequence(s) (.tests) -&gt; checked -&gt; IR
 * -&gt; contract-only symbolic driver -&gt; real SPF -&gt; one solved input vector
 * per sequence -&gt; one JUnit class with one {@code @Test} per sequence,
 * verified against the unmodified library.
 *
 * <p>Generation never reads, compiles or runs the library: {@link #generate}
 * depends only on the spec and the sequence file. Only {@link #runOne}'s
 * verification step puts the library next to the generated test.
 *
 * <pre>
 *   java -cp target/classes in.ac.iiitb.plproject.atc.GenatcMain            # every registered library
 *   java -cp target/classes in.ac.iiitb.plproject.atc.GenatcMain stack queue
 * </pre>
 */
public class GenatcMain {

    /** What happened to one sequence of a library's .tests file. */
    public static class SequenceOutcome {
        public String name;
        public String calls;
        /** SOLVED, INCORRECT_SEQUENCE, UNSAT_POSTCONDITION, SOLVER_ERROR, TIMEOUT, COMPILE_ERROR. */
        public String status;
        public String detail = "";
        public long spfMillis;
        public int pathsCompleted;
        public List<String> checkOnly = new ArrayList<String>();
        public TestPlan plan;
        public InputVector vector;
    }

    public static class LibraryOutcome {
        public final LibraryRegistry.Entry entry;
        /** SKIPPED / INPUT_ERROR / GENERATED / or, when no sequence gave a test, the first sequence's status. */
        public String status;
        public String detail = "";
        public List<SequenceOutcome> sequences = new ArrayList<SequenceOutcome>();
        public int testsGenerated;
        public long spfMillis;
        /** The generated JUnit class, or null when no test was generated. */
        public String junitSource;
        public String junitClassName = TestPlan.JUNIT_CLASS;
        public boolean junitCompiled;
        public int junitRunCount;
        public List<String> junitFailures = new ArrayList<String>();

        LibraryOutcome(LibraryRegistry.Entry entry) { this.entry = entry; }
    }

    public static void main(String[] args) throws Exception {
        List<LibraryRegistry.Entry> selected = new ArrayList<LibraryRegistry.Entry>();
        if (args.length == 0) {
            selected.addAll(LibraryRegistry.ALL);
        } else {
            for (String a : args) {
                LibraryRegistry.Entry e = LibraryRegistry.byKey(a);
                if (e == null) {
                    System.err.println("Unknown library: " + a + " (expected one of " + LibraryRegistry.keys() + ")");
                    System.exit(1);
                }
                selected.add(e);
            }
        }

        List<LibraryOutcome> outcomes = new ArrayList<LibraryOutcome>();
        for (LibraryRegistry.Entry entry : selected) {
            outcomes.add(runOne(entry));
        }
        printSummary(outcomes);
    }

    /** Generates the test for {@code entry} and runs it against {@code entry}'s library. */
    public static LibraryOutcome runOne(LibraryRegistry.Entry entry) throws Exception {
        LibraryOutcome outcome = generate(entry, new File(entry.outputDir), TestPlan.JUNIT_CLASS, true);
        if (outcome.junitSource == null) return outcome;

        File genDir = generatedDir(new File(entry.outputDir));
        for (String librarySourcePath : entry.librarySourcePaths) {
            Path src = Paths.get(librarySourcePath);
            Files.copy(src, new File(genDir, src.getFileName().toString()).toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        GeneratedSuiteHarness harness = new GeneratedSuiteHarness();
        GeneratedSuiteHarness.VerifyResult verify = harness.compileAndRun(genDir, JunitJars.resolve(),
                TestPlan.GENERATED_PACKAGE + "." + TestPlan.JUNIT_CLASS);
        outcome.junitCompiled = verify.compiled;
        outcome.junitRunCount = verify.runCount;
        outcome.junitFailures = verify.failures;

        if (!verify.compiled) {
            System.out.println("GENERATED JUNIT FAILED TO COMPILE:\n" + verify.compileError);
        } else if (verify.allPassed()) {
            System.out.println("JUnit against the unmodified library: " + verify.runCount
                    + "/" + verify.runCount + " passed");
        } else {
            System.out.println("JUnit against the unmodified library: "
                    + (verify.runCount - verify.failures.size()) + "/" + verify.runCount + " passed");
            for (String f : verify.failures) System.out.println("  FAIL " + f);
        }
        return outcome;
    }

    /**
     * Contract + sequences -&gt; JUnit source, without ever touching the library:
     * nothing from {@code entry.librarySourcePaths} is read here. Writes the
     * drivers and the JUnit class under {@code outputDir}.
     */
    public static LibraryOutcome generate(LibraryRegistry.Entry entry, File outputDir, String junitClassName,
                                          boolean verbose) throws Exception {
        LibraryOutcome outcome = new LibraryOutcome(entry);
        outcome.junitClassName = junitClassName;
        if (verbose) {
            System.out.println("\n================================================================");
            System.out.println(entry.key + " — " + entry.title);
            System.out.println("================================================================");
        }

        if (!entry.isSupported()) {
            outcome.status = "SKIPPED";
            outcome.detail = entry.unsupportedReason;
            if (verbose) System.out.println("SKIPPED (unsupported): " + entry.unsupportedReason);
            return outcome;
        }

        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(read(entry.specPath), entry.specPath);
        List<TestStringAst> tests = TestStringParser.parseAll(read(entry.testsPath), entry.testsPath);

        List<TestPlan> plans = new ArrayList<TestPlan>();
        try {
            for (TestStringAst test : tests) plans.add(IrBuilder.build(spec, test, entry.specPath, entry.testsPath));
        } catch (GenatcInputException e) {
            outcome.status = "INPUT_ERROR";
            outcome.detail = e.getMessage();
            if (verbose) System.out.println("INPUT CHECKS FAILED:\n" + e.getMessage());
            return outcome;
        }

        File genDir = generatedDir(outputDir);
        Files.createDirectories(genDir.toPath());
        deleteJava(genDir);

        List<JUnitPrinter.Sequence> solved = new ArrayList<JUnitPrinter.Sequence>();
        SpfRunner runner = new SpfRunner();
        for (int k = 0; k < plans.size(); k++) {
            TestPlan plan = plans.get(k);
            SequenceOutcome seq = new SequenceOutcome();
            seq.name = plan.getLibraryKey() != null ? plan.getLibraryKey() : "sequence" + (k + 1);
            seq.calls = callsOf(plan);
            seq.plan = plan;
            seq.checkOnly.addAll(plan.checkOnlyClauses());
            outcome.sequences.add(seq);

            File work = new File(outputDir, plans.size() == 1 ? "spf-work" : "spf-work/" + (k + 1));
            SpfRunResult result = runner.run(plan, work, SpfConfig.DEFAULT);
            seq.spfMillis = result.getElapsedMillis();
            seq.pathsCompleted = result.pathsCompleted();
            seq.status = result.getStatus().name();
            outcome.spfMillis += result.getElapsedMillis();

            if (k == 0) {
                Files.write(new File(genDir, TestPlan.SYMBOLIC_CLASS + ".java").toPath(),
                        SymbolicDriverPrinter.print(plan).getBytes(StandardCharsets.UTF_8));
            }

            switch (result.getStatus()) {
                case SOLVED:
                    seq.vector = result.getVector();
                    solved.add(new JUnitPrinter.Sequence(plan, result.getVector()));
                    break;
                case INCORRECT_SEQUENCE: {
                    CallBlock b = plan.getCallBlocks().get(result.getBlockingCall());
                    seq.detail = "INCORRECT SEQUENCE: call " + b.getIndex() + " (" + b.getOperationName()
                            + "): " + ("declare".equals(result.getDetail())
                                       ? "its parameters cannot be chosen" : result.getBlockingClause());
                    break;
                }
                case UNSAT_POSTCONDITION: {
                    CallBlock b = plan.getCallBlocks().get(result.getBlockingCall());
                    seq.detail = "UNSATISFIABLE POSTCONDITION: call " + b.getIndex() + " ("
                            + b.getOperationName() + "): " + result.getBlockingClause();
                    break;
                }
                case SOLVER_ERROR:
                    seq.detail = "SOLVER ERROR: " + firstLine(result.getDetail());
                    break;
                case TIMEOUT:
                    seq.detail = "TIMEOUT: " + result.getDetail();
                    break;
                default:
                    seq.detail = "DRIVER DID NOT COMPILE: " + result.getDetail();
                    break;
            }
            if (verbose) {
                System.out.println("sequence " + seq.name + ": " + seq.calls);
                System.out.println("  SPF: " + seq.status + " in " + seq.spfMillis + "ms"
                        + (seq.detail.isEmpty() ? "" : " — " + seq.detail));
                for (String c : seq.checkOnly) System.out.println("  CHECK-ONLY " + c);
                if (seq.vector != null && !seq.vector.getUnconstrainedSlots().isEmpty()) {
                    System.out.println("  left free by the solver: " + seq.vector.getUnconstrainedSlots());
                }
            }
        }

        outcome.testsGenerated = solved.size();
        if (solved.isEmpty()) {
            SequenceOutcome first = outcome.sequences.get(0);
            outcome.status = first.status;
            outcome.detail = first.detail;
            return outcome;
        }
        outcome.status = "GENERATED";
        outcome.junitSource = JUnitPrinter.print(junitClassName, solved);
        Files.write(new File(genDir, junitClassName + ".java").toPath(),
                outcome.junitSource.getBytes(StandardCharsets.UTF_8));
        return outcome;
    }

    public static File generatedDir(File outputDir) {
        return new File(outputDir, TestPlan.GENERATED_PACKAGE.replace('.', '/'));
    }

    private static void deleteJava(File dir) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) if (f.getName().endsWith(".java")) f.delete();
    }

    private static String callsOf(TestPlan plan) {
        StringBuilder sb = new StringBuilder();
        for (CallBlock b : plan.getCallBlocks()) {
            if (sb.length() > 0) sb.append(" -> ");
            sb.append(b.getOperationName());
        }
        return sb.toString();
    }

    private static void printSummary(List<LibraryOutcome> outcomes) {
        System.out.println("\n================================================================");
        System.out.println("SUMMARY");
        System.out.println("================================================================");
        for (LibraryOutcome o : outcomes) {
            System.out.println(String.format("%-14s %-20s tests=%-3d spf=%6dms %s",
                    o.entry.key, o.status, o.testsGenerated, o.spfMillis,
                    o.junitFailures.isEmpty() ? (o.status.equals("GENERATED") ? "" : o.detail)
                                              : (o.junitFailures.size() + " JUnit failure(s)")));
        }
    }

    private static String firstLine(String text) {
        if (text == null) return "";
        String t = text.trim();
        int newline = t.indexOf('\n');
        return newline < 0 ? t : t.substring(0, newline);
    }

    private static String read(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
}

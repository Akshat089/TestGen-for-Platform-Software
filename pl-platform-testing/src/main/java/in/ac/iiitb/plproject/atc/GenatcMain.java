package in.ac.iiitb.plproject.atc;

import in.ac.iiitb.plproject.check.GenatcInputException;
import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.IrBuilder;
import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.parser.SpecToAstConverter;
import in.ac.iiitb.plproject.parser.TestStringParser;
import in.ac.iiitb.plproject.parser.ast.JmlSpecAst;
import in.ac.iiitb.plproject.parser.ast.TestStringAst;
import in.ac.iiitb.plproject.spf.SpfConfig;
import in.ac.iiitb.plproject.spf.SpfSymexEngine;
import in.ac.iiitb.plproject.symbolic.JUnitPrinter;
import in.ac.iiitb.plproject.symbolic.Stmt;
import in.ac.iiitb.plproject.symbolic.TestGen;
import in.ac.iiitb.plproject.verify.GeneratedSuiteHarness;
import in.ac.iiitb.plproject.verify.JunitJars;

import java.io.File;
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
 * The entry point: contract (.spec) + sequence(s) (.tests) -&gt; checked -&gt; IR
 * -&gt; abstract test case -&gt; genCTC (each round: rewriteATC, then symex on
 * SPF with every ready call running on the real library) -&gt; the concrete
 * test case -&gt; one JUnit class with one {@code @Test} per sequence,
 * verified against the unmodified library.
 *
 * <p>The library is never read: it is compiled next to each round's SPF
 * driver and called, with concrete arguments only.
 *
 * <pre>
 *   java -cp target/classes in.ac.iiitb.plproject.atc.GenatcMain            # every registered library
 *   java -cp target/classes in.ac.iiitb.plproject.atc.GenatcMain stack queue
 * </pre>
 */
public class GenatcMain {

    /** Name of the generation trace written next to the outputs: ATC, every round, concrete test. */
    public static final String TRACE_FILE = "ATC.txt";

    /** What happened to one sequence of a library's .tests file. */
    public static class SequenceOutcome {
        public String name;
        public String calls;
        /** GENERATED, or genCTC's status: INCORRECT_SEQUENCE, SOLVER_ERROR, TIMEOUT, LIBRARY_EXCEPTION, ... */
        public String status;
        public String detail = "";
        public long spfMillis;
        /** genCTC rounds that turned inputs concrete. */
        public int rounds;
        /** symex calls, i.e. SPF runs. */
        public int spfRuns;
        /** The value each input command got, in order: {@code c0_elem -> "s1"}. */
        public Map<String, String> inputs = new LinkedHashMap<String, String>();
        public TestPlan plan;
        public List<Stmt> atc;
        public TestGen.Result result;
        public String trace;
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
     * Contract + sequences -&gt; JUnit source. Each sequence's ATC is
     * concretised by genCTC on {@code entry}'s library (the correct one when
     * the mutation runner caches a test). Writes each round's SPF driver under
     * {@code outputDir/spf-work}, the trace to {@code outputDir/ATC.txt} and the
     * JUnit class under {@code outputDir}.
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

        List<JUnitPrinter.Sequence> concrete = new ArrayList<JUnitPrinter.Sequence>();
        StringBuilder traces = new StringBuilder();
        for (int k = 0; k < plans.size(); k++) {
            TestPlan plan = plans.get(k);
            SequenceOutcome seq = new SequenceOutcome();
            seq.name = plan.getLibraryKey() != null ? plan.getLibraryKey() : "sequence" + (k + 1);
            seq.calls = callsOf(plan);
            seq.plan = plan;
            outcome.sequences.add(seq);

            File work = new File(outputDir, plans.size() == 1 ? "spf-work" : "spf-work/" + (k + 1));
            deleteRecursively(work);
            seq.atc = TestGen.getATC(plan);
            SpfSymexEngine engine = new SpfSymexEngine(plan, entry.librarySourcePaths, work, SpfConfig.DEFAULT);
            seq.result = TestGen.genCTC(seq.atc, Collections.<String>emptyList(), TestGen.typeEnv(plan), engine);
            seq.status = seq.result.status.name();
            seq.detail = seq.result.detail;
            seq.rounds = seq.result.concretisingRounds();
            seq.spfRuns = seq.result.symexRuns();
            seq.spfMillis = seq.result.engineMillis();
            outcome.spfMillis += seq.spfMillis;
            seq.trace = TestGen.trace(plan, seq.atc, seq.result);
            if (traces.length() > 0) traces.append("\n\n");
            traces.append(seq.trace);

            if (seq.result.concrete != null) {
                for (Stmt s : seq.result.concrete) {
                    if (s instanceof Stmt.Assign && ((Stmt.Assign) s).fromInput != null) {
                        seq.inputs.put(((Stmt.Assign) s).var, ((Stmt.Assign) s).expr.toString());
                    }
                }
                concrete.add(new JUnitPrinter.Sequence(plan, seq.result.concrete));
            }
            if (verbose) {
                System.out.println("sequence " + seq.name + ": " + seq.calls);
                System.out.println("  genCTC: " + seq.status + " after " + seq.rounds + " round(s), "
                        + seq.spfRuns + " SPF run(s), " + seq.spfMillis + "ms"
                        + (seq.detail.isEmpty() ? "" : " — " + seq.detail));
                if (!seq.inputs.isEmpty()) System.out.println("  inputs: " + seq.inputs);
            }
        }
        Files.write(new File(outputDir, TRACE_FILE).toPath(), traces.toString().getBytes(StandardCharsets.UTF_8));

        outcome.testsGenerated = concrete.size();
        if (concrete.isEmpty()) {
            SequenceOutcome first = outcome.sequences.get(0);
            outcome.status = first.status;
            outcome.detail = first.detail;
            return outcome;
        }
        outcome.status = "GENERATED";
        outcome.junitSource = JUnitPrinter.print(junitClassName, concrete);
        Files.write(new File(genDir, junitClassName + ".java").toPath(),
                outcome.junitSource.getBytes(StandardCharsets.UTF_8));
        return outcome;
    }

    private static void deleteRecursively(File dir) throws IOException {
        if (!dir.exists()) return;
        try (java.util.stream.Stream<Path> files = Files.walk(dir.toPath())) {
            files.sorted(java.util.Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        }
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
            int rounds = 0;
            for (SequenceOutcome seq : o.sequences) rounds += seq.rounds;
            System.out.println(String.format("%-14s %-20s tests=%-3d rounds=%-2d spf=%6dms %s",
                    o.entry.key, o.status, o.testsGenerated, rounds, o.spfMillis,
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

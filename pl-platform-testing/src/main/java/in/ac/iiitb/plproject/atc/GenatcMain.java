package in.ac.iiitb.plproject.atc;

import in.ac.iiitb.plproject.check.GenatcInputException;
import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.Clause;
import in.ac.iiitb.plproject.ir.IrBuilder;
import in.ac.iiitb.plproject.ir.ParamSlot;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The entry point: contract (.spec) + sequence (.tests) -&gt; checked -&gt; IR -&gt;
 * symbolic driver -&gt; real SPF -&gt; solved input vectors -&gt; JUnit class,
 * verified against the unmodified library. Replaces the old
 * {@code LibraryDryRunExamples}/{@code Main}.
 *
 * <pre>
 *   java -cp target/classes in.ac.iiitb.plproject.atc.GenatcMain            # every registered library
 *   java -cp target/classes in.ac.iiitb.plproject.atc.GenatcMain stack queue
 * </pre>
 */
public class GenatcMain {

    public static class LibraryOutcome {
        public final LibraryRegistry.Entry entry;
        public String status; // SKIPPED / INPUT_ERROR / INFEASIBLE / CUT_OFF / GENERATED
        public String detail = "";
        public int pathsExplored;
        public int testsGenerated;
        public long spfMillis;
        public boolean junitCompiled;
        public List<String> junitFailures = new ArrayList<String>();
        public List<String> unsolvedParams = new ArrayList<String>();

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

    public static LibraryOutcome runOne(LibraryRegistry.Entry entry) throws Exception {
        LibraryOutcome outcome = new LibraryOutcome(entry);
        System.out.println("\n================================================================");
        System.out.println(entry.key + " — " + entry.title);
        System.out.println("================================================================");

        if (!entry.isSupported()) {
            outcome.status = "SKIPPED";
            outcome.detail = entry.unsupportedReason;
            System.out.println("SKIPPED (unsupported): " + entry.unsupportedReason);
            return outcome;
        }

        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(read(entry.specPath), entry.specPath);
        TestStringAst test = TestStringParser.parse(read(entry.testsPath), entry.testsPath);

        TestPlan plan;
        try {
            plan = IrBuilder.build(spec, test, entry.specPath, entry.testsPath);
        } catch (GenatcInputException e) {
            outcome.status = "INPUT_ERROR";
            outcome.detail = e.getMessage();
            System.out.println("INPUT CHECKS FAILED:\n" + e.getMessage());
            return outcome;
        }

        Set<String> unsolved = new LinkedHashSet<String>();
        for (ParamSlot p : plan.pooledParams()) {
            unsolved.add(p.getSlotName() + " (" + p.getType() + "): " + p.getPoolReason());
        }
        outcome.unsolvedParams.addAll(unsolved);

        File outputDir = new File(entry.outputDir);
        File genDir = new File(outputDir, TestPlan.GENERATED_PACKAGE.replace('.', '/'));
        Files.createDirectories(genDir.toPath());

        SpfRunner runner = new SpfRunner();
        SpfRunResult result = runner.run(plan, new File(outputDir, "spf-work"), entry.librarySourcePaths,
                SpfConfig.DEFAULT);
        outcome.spfMillis = result.getElapsedMillis();
        outcome.pathsExplored = result.pathsExplored();

        if (result.isCompileFailed()) {
            outcome.status = "COMPILE_ERROR";
            outcome.detail = result.getCompileError();
            System.out.println("SYMBOLIC DRIVER FAILED TO COMPILE:\n" + result.getCompileError());
            return outcome;
        }

        if (!result.hasUsablePaths()) {
            if (result.getInfeasibleAtCallIndex() != null && result.getExecutionErrorDetail() != null) {
                outcome.status = "EXECUTION_ERROR";
                CallBlock failing = plan.getCallBlocks().get(result.getInfeasibleAtCallIndex());
                outcome.detail = "SPF could not execute call " + failing.getIndex() + " ("
                        + failing.getOperationName() + ") at all — every explored end state was a"
                        + " property violation rather than a normal completion: "
                        + result.getExecutionErrorDetail()
                        + ". This is a tool limitation (see IMPLEMENTATION_NOTES.md), not an"
                        + " unsatisfiable precondition.";
                System.out.println("EXECUTION ERROR: " + outcome.detail);
            } else if (result.getInfeasibleAtCallIndex() != null) {
                outcome.status = "INFEASIBLE";
                CallBlock failing = plan.getCallBlocks().get(result.getInfeasibleAtCallIndex());
                StringBuilder sb = new StringBuilder();
                sb.append("no path reaches the end of the sequence — call ").append(failing.getIndex())
                  .append(" (").append(failing.getOperationName())
                  .append(") has no precondition-satisfying predecessor path. Its precondition:\n");
                for (Clause c : failing.getPreconditions()) {
                    sb.append("    requires: ").append(c.getOriginalText()).append('\n');
                }
                outcome.detail = sb.toString();
                System.out.println("INFEASIBLE SEQUENCE: " + outcome.detail);
            } else if (result.isDepthLimitHit()) {
                outcome.status = "CUT_OFF_DEPTH";
                outcome.detail = "search depth bound (" + SpfConfig.DEFAULT.getDepthLimit()
                        + ") reached before any feasible path completed";
                System.out.println(outcome.detail);
            } else if (result.isTimedOut()) {
                outcome.status = "CUT_OFF_TIMEOUT";
                outcome.detail = "SPF timed out after " + SpfConfig.DEFAULT.getTimeoutSeconds()
                        + "s before any feasible path completed";
                System.out.println(outcome.detail);
            } else {
                outcome.status = "INFEASIBLE";
                outcome.detail = "no feasible path found, and no call index could be isolated";
                System.out.println(outcome.detail);
            }
            return outcome;
        }

        if (result.isDepthLimitHit()) {
            System.out.println("NOTE: search depth bound was reached — exploration may be incomplete.");
        }
        if (result.isTimedOut()) {
            System.out.println("NOTE: SPF timed out — exploration may be incomplete.");
        }
        if (result.errorStatesFound() > 0) {
            System.out.println("NOTE: SPF found " + result.errorStatesFound()
                    + " end state(s) that are a property violation (uncaught exception) along some"
                    + " execution; excluded from the delivered suite, not presented as a passing path.");
        }

        List<InputVector> vectors = result.getInputVectors();
        System.out.println("paths explored: " + outcome.pathsExplored + ", feasible: " + vectors.size()
                + ", SPF time: " + outcome.spfMillis + "ms");
        if (!unsolved.isEmpty()) {
            System.out.println("NOT solved for (pooled instead): " + unsolved);
        }
        // Delivering every feasible path as a @Test method is impractical once a
        // sequence has several pooled parameters (StringSet/Graph/LruCache's pool
        // combinations multiply out quickly) — capped here, not in SPF's own
        // exploration, so the report can still say how many paths SPF actually
        // found versus how many became tests.
        int maxTests = Integer.getInteger("genatc.maxTestsPerClass", 60);
        if (vectors.size() > maxTests) {
            System.out.println("NOTE: capping generated tests at " + maxTests + " of " + vectors.size()
                    + " feasible paths (set -Dgenatc.maxTestsPerClass to change).");
            vectors = vectors.subList(0, maxTests);
        }
        outcome.testsGenerated = vectors.size();

        String driverSource = SymbolicDriverPrinter.print(plan);
        Files.write(new File(genDir, TestPlan.SYMBOLIC_CLASS + ".java").toPath(),
                driverSource.getBytes(StandardCharsets.UTF_8));
        String junitSource = JUnitPrinter.print(plan, vectors);
        Files.write(new File(genDir, TestPlan.JUNIT_CLASS + ".java").toPath(),
                junitSource.getBytes(StandardCharsets.UTF_8));
        for (String librarySourcePath : entry.librarySourcePaths) {
            Path src = Paths.get(librarySourcePath);
            Files.copy(src, new File(genDir, src.getFileName().toString()).toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        GeneratedSuiteHarness harness = new GeneratedSuiteHarness();
        GeneratedSuiteHarness.VerifyResult verify = harness.compileAndRun(genDir, JunitJars.resolve(),
                TestPlan.GENERATED_PACKAGE + "." + TestPlan.JUNIT_CLASS);
        outcome.junitCompiled = verify.compiled;
        outcome.junitFailures = verify.failures;
        outcome.status = "GENERATED";

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

    private static void printSummary(List<LibraryOutcome> outcomes) {
        System.out.println("\n================================================================");
        System.out.println("SUMMARY");
        System.out.println("================================================================");
        for (LibraryOutcome o : outcomes) {
            System.out.println(String.format("%-14s %-14s paths=%-4d tests=%-4d spf=%6dms %s",
                    o.entry.key, o.status, o.pathsExplored, o.testsGenerated, o.spfMillis,
                    o.junitFailures.isEmpty() ? "" : (o.junitFailures.size() + " JUnit failure(s)")));
        }
    }

    private static String read(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
}

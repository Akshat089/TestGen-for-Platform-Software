package in.ac.iiitb.plproject;

import in.ac.iiitb.plproject.atc.GenatcMain;
import in.ac.iiitb.plproject.atc.LibraryRegistry;
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
import in.ac.iiitb.plproject.symbolic.JUnitPrinter;
import in.ac.iiitb.plproject.symbolic.SymbolicDriverPrinter;
import in.ac.iiitb.plproject.symbolic.TypeSupport;
import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the new (no-propagation, real-SPF) pipeline itself — the IR, the two
 * printers, and the input checks — WITHOUT running real SPF: every test here
 * builds a {@link TestPlan} from an actual {@code .spec}/{@code .tests} pair
 * via {@link IrBuilder} (the real Step 1/2), then drives the printers with a
 * hand-built {@link InputVector} rather than one SPF actually solved. That is
 * enough to check everything these tests care about — clause text, parameter
 * naming, snapshot types, boxing, no-SPF-tokens — since none of it depends on
 * which concrete values were chosen, only on the IR's structure. Whether SPF
 * itself solves a sequence correctly is checked separately, by actually
 * running it (see IMPLEMENTATION_NOTES.md for those real numbers) and by
 * {@code verify-generated.sh}.
 *
 * <p>The one exception is {@link #infeasibleSequenceProducesNoClassAndAReport()}:
 * infeasibility is a dynamic property of the symbolic search, so that one
 * test does run real SPF, and skips itself (rather than failing) if
 * {@code scripts/setup-spf.sh} has not been run on this machine.
 */
public class GeneratorPipelineTest {

    private static final Pattern ASSERT_MESSAGE = Pattern.compile(
            "assertTrue\\(\"((?:PRE|POST) [^\"]*)\"");

    private static TestPlan buildPlan(String specPath, String testsPath) throws IOException {
        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(read(specPath), specPath);
        TestStringAst test = TestStringParser.parse(read(testsPath), testsPath);
        return IrBuilder.build(spec, test, specPath, testsPath);
    }

    private static String read(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }

    /**
     * One literal per parameter slot, with NO dependence on SPF: a pooled slot
     * takes its pool's first entry, a symbolic slot takes
     * {@link TypeSupport#unconstrainedLiteral}. Good enough to drive both
     * printers for every structural check in this file.
     */
    private static InputVector syntheticVector(TestPlan plan) {
        Map<String, String> literals = new LinkedHashMap<String, String>();
        int ordinal = 0;
        for (CallBlock block : plan.getCallBlocks()) {
            for (ParamSlot p : block.getParams()) {
                if (p.isPooled()) {
                    literals.put(p.getSlotName(), TypeSupport.literal(p.getType(), p.getPool().get(0)));
                } else {
                    literals.put(p.getSlotName(), TypeSupport.unconstrainedLiteral(p.getType(), ordinal++));
                }
            }
        }
        return new InputVector(1, literals, java.util.Collections.<String>emptySet(),
                java.util.Collections.<String>emptySet());
    }

    // ── (1) every assertion traces to a contract clause, none invented ──────

    @Test
    public void everyGeneratedAssertionTracesToAContractClause() throws IOException {
        TestPlan plan = buildPlan("../specs/Stack.spec", "../specs/Stack.tests");
        String specText = read("../specs/Stack.spec");
        String junit = JUnitPrinter.print(plan, java.util.Collections.singletonList(syntheticVector(plan)));

        Matcher m = ASSERT_MESSAGE.matcher(junit);
        int checked = 0;
        while (m.find()) {
            // The message is read from the .java SOURCE text, where a literal
            // backslash (from \old/\result) is written doubled to be valid Java
            // source — undo that before comparing against the spec's own text.
            String message = m.group(1).replace("\\\\", "\\");
            String clauseText = message.substring(message.indexOf(':') + 1).trim();
            assertTrue("generated assertion message '" + message
                            + "' must trace to text actually in Stack.spec, found none for: " + clauseText,
                    specText.contains(clauseText));
            checked++;
        }
        assertTrue("expected to find generated assertions to check", checked > 0);
    }

    // ── (2) symbolic driver and JUnit share call order, params, precondition text ──

    @Test
    public void symbolicDriverAndJUnitShareCallOrderParamsAndPreconditionText() throws IOException {
        TestPlan plan = buildPlan("../specs/Stack.spec", "../specs/Stack.tests");
        String driver = SymbolicDriverPrinter.print(plan);
        String junit = JUnitPrinter.print(plan, java.util.Collections.singletonList(syntheticVector(plan)));

        for (CallBlock block : plan.getCallBlocks()) {
            String callComment = "// call " + block.getIndex() + ": " + block.getOperationName();
            assertTrue("driver missing " + callComment, driver.contains(callComment));
            assertTrue("junit missing " + callComment, junit.contains(callComment));

            for (ParamSlot p : block.getParams()) {
                assertTrue("driver missing declaration of " + p.getSlotName(),
                        driver.contains(p.getSlotName()));
                assertTrue("junit missing declaration of " + p.getSlotName(),
                        junit.contains(p.getSlotName()));
            }

            for (Clause pre : block.getPreconditions()) {
                String preMessage = "PRE " + block.getOperationName() + ": " + pre.getOriginalText();
                assertTrue("driver's own precondition handling must use the identical clause text"
                                + " (checked via the shared javaExpr, present verbatim): " + pre.getJavaExpr(),
                        driver.contains(pre.getJavaExpr()));
                assertTrue("junit missing precondition check '" + preMessage + "'",
                        junit.contains(preMessage));
            }
        }
    }

    // ── (3) generated JUnit contains no SPF tokens ───────────────────────────

    @Test
    public void generatedJUnitContainsNoSpfTokens() throws IOException {
        TestPlan plan = buildPlan("../specs/Stack.spec", "../specs/Stack.tests");
        String junit = JUnitPrinter.print(plan, java.util.Collections.singletonList(syntheticVector(plan)));

        assertFalse("generated JUnit must not import gov.nasa.jpf", junit.contains("gov.nasa.jpf"));
        assertFalse("generated JUnit must not reference Debug.*", junit.contains("Debug."));
        assertFalse("generated JUnit must not reference Verify.*", junit.contains("Verify."));
    }

    // ── (4) snapshots are plain locals of the declared state type ───────────

    @Test
    public void snapshotsArePlainLocalsOfTheDeclaredStateType() throws IOException {
        TestPlan plan = buildPlan("../specs/Stack.spec", "../specs/Stack.tests");
        String junit = JUnitPrinter.print(plan, java.util.Collections.singletonList(syntheticVector(plan)));

        // Stack.spec declares `int size;` — every \old(size) snapshot must be a
        // plain `int`, never Object/boxed/a symbolic wrapper.
        assertTrue("expected an `int old<i>_size = Helper.size;` snapshot somewhere",
                Pattern.compile("int old\\d+_size = Helper\\.size;").matcher(junit).find());
        assertFalse("a size snapshot must never be declared Object",
                Pattern.compile("Object old\\d+_size").matcher(junit).find());
    }

    // ── (5) boxed types stay boxed ───────────────────────────────────────────

    @Test
    public void boxedServerOutputStaysBoxed() throws IOException {
        TestPlan plan = buildPlan("../specs/HashMapLib.spec", "../specs/HashMapLib.tests");
        String junit = JUnitPrinter.print(plan, java.util.Collections.singletonList(syntheticVector(plan)));
        String driver = SymbolicDriverPrinter.print(plan);

        // put's declared return type is Integer (nullable oldVal) — the result
        // local must stay boxed in both outputs, never narrowed to primitive int.
        assertTrue("JUnit's put result must stay boxed Integer",
                Pattern.compile("Integer c\\d+_result = Helper\\.put\\(").matcher(junit).find());
        assertFalse("JUnit must never narrow put's result to a primitive int",
                Pattern.compile("(?<!r)int c\\d+_result = Helper\\.put\\(").matcher(junit).find());
        assertTrue("the symbolic driver must declare put's result as boxed Integer too",
                Pattern.compile("Integer c\\d+_result = Helper\\.put\\(").matcher(driver).find());
    }

    // ── (6) an infeasible sequence produces no class and the right report ──

    @Test
    public void infeasibleSequenceProducesNoClassAndAReport() throws Exception {
        try {
            in.ac.iiitb.plproject.spf.SpfHome.resolveRunJpfJar();
        } catch (IllegalStateException notInstalled) {
            Assume.assumeNoException("scripts/setup-spf.sh has not been run on this machine",
                    notInstalled);
        }

        Path testsFile = Files.createTempFile("infeasible-stack", ".tests");
        Files.write(testsFile,
                "test InfeasibleStack { sequence: push -> pop -> pop; }".getBytes(StandardCharsets.UTF_8));
        Path outDir = Files.createTempDirectory("infeasible-stack-out");

        LibraryRegistry.Entry entry = LibraryRegistry.variantOf(LibraryRegistry.STACK,
                LibraryRegistry.STACK.librarySourcePaths, testsFile.toString(), outDir.toString());

        GenatcMain.LibraryOutcome outcome = GenatcMain.runOne(entry);

        assertEquals("push -> pop -> pop must be reported infeasible", "INFEASIBLE", outcome.status);
        assertTrue("the report must name the failing call and its precondition",
                outcome.detail.contains("pop") && outcome.detail.contains("size > 0"));

        File junitFile = new File(outDir.toString()
                + "/" + TestPlan.GENERATED_PACKAGE.replace('.', '/') + "/" + TestPlan.JUNIT_CLASS + ".java");
        assertFalse("no JUnit class must be written for an infeasible sequence", junitFile.exists());
    }

    // ── (7) a stateless sequence produces no reset ──────────────────────────

    @Test
    public void statelessSequenceProducesNoReset() throws IOException {
        // A minimal spec with no `state { ... }` block at all, built directly
        // (not from a checked-in file: every registered library happens to
        // declare state, so this is the one case this suite has to construct
        // itself) — one pure function, no state to reset.
        String specText = "spec square {\n"
                + "    signature: int square(int n);\n"
                + "    requires:  n >= 0;\n"
                + "    ensures:   \\result == answer && answer >= 0;\n"
                + "}\n";
        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(specText, "<stateless>");
        TestStringAst test = TestStringParser.parse("square");
        TestPlan plan = IrBuilder.build(spec, test, "<stateless>", "<stateless>");

        assertFalse("a spec with no state block must build a stateless plan", plan.isHasState());

        String driver = SymbolicDriverPrinter.print(plan);
        String junit = JUnitPrinter.print(plan, java.util.Collections.singletonList(syntheticVector(plan)));

        assertFalse("the symbolic driver must not call Helper.reset() for a stateless sequence",
                driver.contains("reset()"));
        assertFalse("the generated JUnit must not call Helper.reset() for a stateless sequence",
                junit.contains("reset()"));
    }

    // ── input checks (rule 13), exercised directly so a check regression fails fast ──

    @Test
    public void duplicateContractIsRejectedWithFileAndLine() {
        String specText = "spec push { signature: void push(int x); requires: x > 0; ensures: x > 0; }\n"
                         + "spec push { signature: void push(int x); requires: x > 0; ensures: x > 0; }\n";
        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(specText, "dup.spec");
        TestStringAst test = TestStringParser.parse("push");
        try {
            IrBuilder.build(spec, test, "dup.spec", "<seq>");
            fail("a duplicate contract must be rejected");
        } catch (GenatcInputException e) {
            assertTrue(e.getMessage().contains("dup.spec:2"));
            assertTrue(e.getMessage().contains("duplicate contract"));
        }
    }

    @Test
    public void unknownOperationInSequenceIsRejected() throws IOException {
        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(read("../specs/Stack.spec"), "Stack.spec");
        TestStringAst test = TestStringParser.parse("push -> frobnicate -> pop", "x.tests");
        try {
            IrBuilder.build(spec, test, "Stack.spec", "x.tests");
            fail("an unknown operation must be rejected");
        } catch (GenatcInputException e) {
            assertTrue(e.getMessage().contains("frobnicate"));
        }
    }

    @Test
    public void oldOnANonStateNameIsRejected() {
        String specText = "spec f { signature: int f(int x); requires: x > 0;"
                         + " ensures: \\result == r && r == \\old(x); }\n";
        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(specText, "bad.spec");
        TestStringAst test = TestStringParser.parse("f");
        try {
            IrBuilder.build(spec, test, "bad.spec", "<seq>");
            fail("\\old on a parameter (not a state variable) must be rejected");
        } catch (GenatcInputException e) {
            assertTrue(e.getMessage().contains("not a declared state variable"));
        }
    }

    @Test
    public void resultInAPreconditionIsRejected() {
        String specText = "spec f { signature: int f(int x); requires: \\result > 0; ensures: \\result == x; }\n";
        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(specText, "bad2.spec");
        TestStringAst test = TestStringParser.parse("f");
        try {
            IrBuilder.build(spec, test, "bad2.spec", "<seq>");
            fail("\\result in a precondition must be rejected");
        } catch (GenatcInputException e) {
            assertTrue(e.getMessage().contains("\\result is not allowed in a precondition"));
        }
    }

    @Test
    public void anInputsBlockIsIgnoredNotFailed() throws IOException {
        // rule 4: parsed, ignored, one warning — never a hard failure.
        TestStringAst test = TestStringParser.parse(
                "test T { sequence: push -> pop; inputs { push[0].elem = \"x\"; } }", "legacy.tests");
        assertEquals(2, test.getCalls().size());
        assertTrue(test.getIgnoredInputsLineNumber() >= 0);
    }
}

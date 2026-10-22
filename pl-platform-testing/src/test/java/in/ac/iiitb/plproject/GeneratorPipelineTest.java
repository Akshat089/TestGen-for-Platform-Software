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
 * Tests the contract-only pipeline — the IR, the two printers, the input
 * checks — mostly WITHOUT running SPF: a {@link TestPlan} is built from an
 * actual {@code .spec}/{@code .tests} pair via {@link IrBuilder}, and the
 * printers are driven with a hand-built {@link InputVector}. The tests marked
 * "real SPF" run SPF itself and skip (rather than fail) when
 * {@code scripts/setup-spf.sh} has not been run on this machine.
 */
public class GeneratorPipelineTest {

    private static final Pattern ASSERT_MESSAGE = Pattern.compile(
            "assertTrue\\(\"((?:PRE|POST) [^\"]*)\"");
    private static final Pattern ASSERT_EXPR = Pattern.compile(
            "assertTrue\\(\"(?:[^\"\\\\]|\\\\.)*\", (.*)\\);");

    private static final String[][] SUPPORTED = {
        { "Stack", "stack" }, { "Queue", "queue" }, { "MathLib", "mathlib" }, { "IntArray", "arraylib" },
        { "LinkedList", "linkedlist" }, { "MinHeap", "heap" }, { "StringSet", "set" },
        { "HashMapLib", "hashmap" }, { "BinarySearchTree", "bst" }, { "Graph", "graph" },
        { "LruCache", "lrucache" },
    };

    private static TestPlan buildPlan(String specPath, String testsPath) throws IOException {
        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(read(specPath), specPath);
        TestStringAst test = TestStringParser.parse(read(testsPath), testsPath);
        return IrBuilder.build(spec, test, specPath, testsPath);
    }

    private static TestPlan buildPlan(String name) throws IOException {
        return buildPlan("../specs/" + name + ".spec", "../specs/" + name + ".tests");
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

    private static boolean spfInstalled() {
        try {
            in.ac.iiitb.plproject.spf.SpfHome.resolveRunJpfJar();
            return true;
        } catch (IllegalStateException notInstalled) {
            return false;
        }
    }

    // ── (1) every assertion traces to a contract clause, none invented ──────

    @Test
    public void everyGeneratedAssertionTracesToAContractClause() throws IOException {
        TestPlan plan = buildPlan("Stack");
        String specText = read("../specs/Stack.spec");
        String junit = JUnitPrinter.print(plan, syntheticVector(plan));

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

    // ── (2) the driver never calls into the library ──────────────────────────

    @Test
    public void driverContainsNoCallIntoTheLibrary() throws IOException {
        for (String[] lib : SUPPORTED) {
            String driver = SymbolicDriverPrinter.print(buildPlan(lib[0]));
            assertFalse(lib[1] + ": the driver must not mention Helper", driver.contains("Helper"));
            assertFalse(lib[1] + ": the driver must not call reset()", driver.contains("reset("));
            for (String libraryFile : new File("../libraries/" + lib[1]).list()) {
                String cls = libraryFile.replace(".java", "");
                assertFalse(lib[1] + ": the driver must not name library class " + cls,
                        Pattern.compile("\\b" + cls + "\\b").matcher(driver).find());
            }
            assertTrue(lib[1] + ": the driver imports only SPF's Debug/Verify",
                    driver.replaceAll("import gov\\.nasa\\.jpf\\.(symbc\\.Debug|vm\\.Verify);", "")
                          .indexOf("import ") < 0);
        }
    }

    // ── (3) driver and JUnit share call order, params and clause text ────────

    @Test
    public void symbolicDriverAndJUnitShareCallOrderParamsAndClauseText() throws IOException {
        TestPlan plan = buildPlan("Stack");
        String driver = SymbolicDriverPrinter.print(plan);
        String junit = JUnitPrinter.print(plan, syntheticVector(plan));

        for (CallBlock block : plan.getCallBlocks()) {
            String callComment = "// call " + block.getIndex() + ": " + block.getOperationName();
            assertTrue("driver missing " + callComment, driver.contains(callComment));
            assertTrue("junit missing " + callComment, junit.contains(callComment));

            for (ParamSlot p : block.getParams()) {
                assertTrue("driver missing declaration of " + p.getSlotName(),
                        driver.contains(" " + p.getSlotName() + " = "));
                assertTrue("junit missing declaration of " + p.getSlotName(),
                        junit.contains(" " + p.getSlotName() + " = "));
            }

            for (Clause pre : block.getPreconditions()) {
                assertTrue("driver must assume precondition '" + pre.getOriginalText() + "'",
                        driver.contains("// requires: " + pre.getOriginalText()));
                assertTrue("junit missing precondition check",
                        junit.contains("PRE " + block.getOperationName() + ": " + pre.getOriginalText()));
            }
        }
    }

    // ── (4) generated JUnit contains no SPF tokens ───────────────────────────

    @Test
    public void generatedJUnitContainsNoSpfTokens() throws IOException {
        TestPlan plan = buildPlan("Stack");
        String junit = JUnitPrinter.print(plan, syntheticVector(plan));

        assertFalse("generated JUnit must not import gov.nasa.jpf", junit.contains("gov.nasa.jpf"));
        assertFalse("generated JUnit must not reference Debug.*", junit.contains("Debug."));
        assertFalse("generated JUnit must not reference Verify.*", junit.contains("Verify."));
        assertTrue("every test has a time limit", junit.contains("@Test(timeout = 2000)"));
    }

    // ── (5) no tautological assertion ────────────────────────────────────────

    @Test
    public void noAssertionOnlyNamesTheResult() throws IOException {
        for (String[] lib : SUPPORTED) {
            TestPlan plan = buildPlan(lib[0]);
            String junit = JUnitPrinter.print(plan, syntheticVector(plan));
            Matcher m = ASSERT_EXPR.matcher(junit);
            while (m.find()) {
                String expr = m.group(1).trim();
                String[] sides = expr.split("==");
                assertFalse(lib[1] + ": tautological assertion " + expr,
                        sides.length == 2 && sides[0].trim().equals(sides[1].trim()));
            }
            assertFalse(lib[1] + ": \\result == alias must not be asserted",
                    Pattern.compile("c(\\d+)_result == c\\1_result\\)").matcher(junit).find());
        }
    }

    // ── (6) snapshots are plain locals of the declared state type ───────────

    @Test
    public void snapshotsArePlainLocalsOfTheDeclaredStateType() throws IOException {
        TestPlan plan = buildPlan("Stack");
        String junit = JUnitPrinter.print(plan, syntheticVector(plan));

        assertTrue("expected an `int old<i>_size = Helper.size;` snapshot somewhere",
                Pattern.compile("int old\\d+_size = Helper\\.size;").matcher(junit).find());
        assertFalse("a size snapshot must never be declared Object",
                Pattern.compile("Object old\\d+_size").matcher(junit).find());
    }

    // ── (7) boxed types stay boxed in the JUnit test ─────────────────────────

    @Test
    public void boxedServerOutputStaysBoxed() throws IOException {
        TestPlan plan = buildPlan("HashMapLib");
        String junit = JUnitPrinter.print(plan, syntheticVector(plan));

        assertTrue("JUnit's put result must stay boxed Integer",
                Pattern.compile("Integer c\\d+_result = Helper\\.put\\(").matcher(junit).find());
        assertFalse("JUnit must never narrow put's result to a primitive int",
                Pattern.compile("(?<!r)int c\\d+_result = Helper\\.put\\(").matcher(junit).find());
    }

    // ── (8) starting values come from the state block ────────────────────────

    @Test
    public void startingValuesComeFromTheStateBlock() throws IOException {
        String driver = SymbolicDriverPrinter.print(buildPlan("IntArray"));
        assertTrue(driver.contains("int m0_length = 4;"));
        assertTrue(driver.contains("int[] m0_A = {0, 0, 0, 0};"));
        assertTrue("a variable with no starting value starts from Java's default",
                driver.contains("int m0_writes = 0;"));
        assertTrue(SymbolicDriverPrinter.print(buildPlan("LruCache")).contains("int m0_capacity = 2;"));
    }

    // ── (9) real SPF: an incorrect sequence produces no class and the right report ──

    @Test
    public void incorrectSequenceProducesNoClassAndAReport() throws Exception {
        Assume.assumeTrue("scripts/setup-spf.sh has not been run on this machine", spfInstalled());

        Path testsFile = Files.createTempFile("incorrect-stack", ".tests");
        Files.write(testsFile,
                "test IncorrectStack { sequence: push -> pop -> pop; }".getBytes(StandardCharsets.UTF_8));
        Path outDir = Files.createTempDirectory("incorrect-stack-out");

        LibraryRegistry.Entry entry = LibraryRegistry.variantOf(LibraryRegistry.STACK,
                LibraryRegistry.STACK.librarySourcePaths, testsFile.toString(), outDir.toString());

        GenatcMain.LibraryOutcome outcome = GenatcMain.runOne(entry);

        assertEquals("INCORRECT_SEQUENCE", outcome.status);
        assertEquals("INCORRECT SEQUENCE: call 2 (pop): size > 0", outcome.detail);
        assertNull("no test for an incorrect sequence", outcome.junitSource);
        File junitFile = new File(GenatcMain.generatedDir(outDir.toFile()), TestPlan.JUNIT_CLASS + ".java");
        assertFalse("no JUnit class must be written for an incorrect sequence", junitFile.exists());
    }

    // ── (10) real SPF: one test method per sequence, library off SPF's classpath ──

    @Test
    public void oneTestMethodPerSequenceAndNoLibraryOnSpfsClasspath() throws Exception {
        Assume.assumeTrue("scripts/setup-spf.sh has not been run on this machine", spfInstalled());

        Path testsFile = Files.createTempFile("two-stack", ".tests");
        Files.write(testsFile, ("test First { sequence: push -> pop; }\n"
                + "test Second { sequence: push -> push -> peek; }\n").getBytes(StandardCharsets.UTF_8));
        Path outDir = Files.createTempDirectory("two-stack-out");
        LibraryRegistry.Entry entry = LibraryRegistry.variantOf(LibraryRegistry.STACK,
                LibraryRegistry.STACK.librarySourcePaths, testsFile.toString(), outDir.toString());

        GenatcMain.LibraryOutcome outcome = GenatcMain.generate(entry, outDir.toFile(), TestPlan.JUNIT_CLASS, false);
        assertEquals("GENERATED", outcome.status);
        assertEquals(2, outcome.testsGenerated);
        Matcher tests = Pattern.compile("@Test\\(").matcher(outcome.junitSource);
        int count = 0;
        while (tests.find()) count++;
        assertEquals("one @Test method per sequence", 2, count);

        for (String run : new String[] { "spf-work/1", "spf-work/2" }) {
            File runJpf = new File(outDir.toFile(), run + "/run.jpf");
            String cp = in.ac.iiitb.plproject.spf.SpfRunner.modeledClasspath(runJpf);
            for (String element : cp.split(",")) {
                assertFalse("SPF's classpath must not contain the library: " + element,
                        element.contains("libraries") || containsLibraryClass(new File(element)));
            }
            assertFalse("the SPF work directory must not contain the library source",
                    containsLibraryClass(new File(outDir.toFile(), run)));
        }
    }

    private static boolean containsLibraryClass(File dir) throws IOException {
        if (!dir.isDirectory()) return false;
        try (java.util.stream.Stream<Path> files = Files.walk(dir.toPath())) {
            return files.anyMatch(p -> p.getFileName().toString().startsWith("Helper."));
        }
    }

    // ── (11) real SPF: the same test for the correct and a mutated library ──

    @Test
    public void sameTestForCorrectAndMutatedLibrary() throws Exception {
        Assume.assumeTrue("scripts/setup-spf.sh has not been run on this machine", spfInstalled());

        Path mutantDir = Files.createTempDirectory("stack-mutant");
        String source = read(LibraryRegistry.STACK.librarySourcePath());
        String mutated = source.replace("size = S.size();", "size = S.size() + 1;");
        assertFalse("the mutation must change the library", mutated.equals(source));
        Path mutant = mutantDir.resolve("Helper.java");
        Files.write(mutant, mutated.getBytes(StandardCharsets.UTF_8));

        Path outA = Files.createTempDirectory("stack-correct");
        Path outB = Files.createTempDirectory("stack-mutated");
        LibraryRegistry.Entry correct = LibraryRegistry.variantOf(LibraryRegistry.STACK,
                LibraryRegistry.STACK.librarySourcePaths, LibraryRegistry.STACK.testsPath, outA.toString());
        LibraryRegistry.Entry mutatedEntry = LibraryRegistry.variantOf(LibraryRegistry.STACK,
                java.util.Collections.singletonList(mutant.toString()), LibraryRegistry.STACK.testsPath,
                outB.toString());

        String a = GenatcMain.generate(correct, outA.toFile(), TestPlan.JUNIT_CLASS, false).junitSource;
        String b = GenatcMain.generate(mutatedEntry, outB.toFile(), TestPlan.JUNIT_CLASS, false).junitSource;
        assertTrue(a != null);
        assertEquals("the generated test must not depend on the library", a, b);
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
        String junit = JUnitPrinter.print(plan, syntheticVector(plan));

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

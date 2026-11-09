package in.ac.iiitb.plproject;

import in.ac.iiitb.plproject.atc.GenatcMain;
import in.ac.iiitb.plproject.atc.LibraryRegistry;
import in.ac.iiitb.plproject.check.GenatcInputException;
import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.IrBuilder;
import in.ac.iiitb.plproject.ir.ParamSlot;
import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.parser.SpecToAstConverter;
import in.ac.iiitb.plproject.parser.TestStringParser;
import in.ac.iiitb.plproject.parser.ast.JmlSpecAst;
import in.ac.iiitb.plproject.parser.ast.TestStringAst;
import in.ac.iiitb.plproject.spf.SpfConfig;
import in.ac.iiitb.plproject.spf.SpfSymexEngine;
import in.ac.iiitb.plproject.symbolic.Expr;
import in.ac.iiitb.plproject.symbolic.IntegerModel;
import in.ac.iiitb.plproject.symbolic.JUnitPrinter;
import in.ac.iiitb.plproject.symbolic.Sigma;
import in.ac.iiitb.plproject.symbolic.Solution;
import in.ac.iiitb.plproject.symbolic.Stmt;
import in.ac.iiitb.plproject.symbolic.SymbolicDriverPrinter;
import in.ac.iiitb.plproject.symbolic.TestGen;
import in.ac.iiitb.plproject.symbolic.TypeSupport;
import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the TestGen pipeline: the ATC built from the IR, rewriteATC, isReady,
 * genCTC, the printers and the input checks. Most run WITHOUT SPF; the ones
 * marked "real SPF" run it and skip (rather than fail) when
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
        { "LruCache", "lrucache" }, { "TaskQueue", "taskqueue" }, { "TicketService", "ticketservice" },
        { "OrderService", "orderservice" },
    };

    private static TestPlan buildPlan(String specPath, String testsPath) throws IOException {
        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(read(specPath), specPath);
        TestStringAst test = TestStringParser.parse(read(testsPath), testsPath);
        return IrBuilder.build(spec, test, specPath, testsPath);
    }

    private static TestPlan buildPlan(String name) throws IOException {
        return buildPlan("../specs/" + name + ".spec", "../specs/" + name + ".tests");
    }

    private static TestPlan stackPlan(String sequence) {
        try {
            JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(read("../specs/Stack.spec"), "Stack.spec");
            return IrBuilder.build(spec, TestStringParser.parse(sequence), "Stack.spec", "<seq>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String read(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }

    /**
     * The ATC with every input command filled by rewriteATC, with NO SPF: each
     * input takes {@link TypeSupport#unconstrainedLiteral}. Enough to drive the
     * JUnit printer for every structural check in this file.
     */
    private static List<Stmt> syntheticConcrete(TestPlan plan) {
        List<Stmt> atc = TestGen.getATC(plan);
        List<String> values = new ArrayList<String>();
        for (Stmt s : atc) {
            if (!(s instanceof Stmt.Input)) continue;
            Stmt.Input in = (Stmt.Input) s;
            values.add(TypeSupport.unconstrainedLiteral(in.slot.getType(), in.ordinal));
        }
        return TestGen.rewriteATC(atc, values);
    }

    private static String junitOf(TestPlan plan) {
        return JUnitPrinter.print(plan, syntheticConcrete(plan));
    }

    private static boolean spfInstalled() {
        try {
            in.ac.iiitb.plproject.spf.SpfHome.resolveRunJpfJar();
            return true;
        } catch (IllegalStateException notInstalled) {
            return false;
        }
    }

    // ── the ATC: real calls, assume before and assert after each ─────────────

    @Test
    public void atcHasTheRealCallsWithAssumesBeforeAndAssertsAfter() throws IOException {
        for (String[] lib : SUPPORTED) {
            TestPlan plan = buildPlan(lib[0]);
            List<Stmt> atc = TestGen.getATC(plan);
            assertTrue(lib[1] + ": the ATC starts from the library's real state",
                    atc.get(0) instanceof Stmt.Reset);
            for (CallBlock block : plan.getCallBlocks()) {
                int call = -1;
                for (int i = 0; i < atc.size(); i++) {
                    Stmt s = atc.get(i);
                    if (s instanceof Stmt.Assign && ((Stmt.Assign) s).isApiCall() && ((Stmt.Assign) s).block == block) {
                        call = i;
                    }
                }
                assertTrue(lib[1] + ": the ATC must contain the real call " + block, call >= 0);
                Expr.Call c = (Expr.Call) ((Stmt.Assign) atc.get(call)).expr;
                assertEquals(block.getOperationName(), c.function);
                assertEquals(block.getParams().size(), c.args.size());
                for (int i = 0; i < atc.size(); i++) {
                    Stmt s = atc.get(i);
                    if (s instanceof Stmt.Assume && ((Stmt.Assume) s).block == block) {
                        assertTrue(lib[1] + ": every assume of " + block + " comes before the call", i < call);
                    }
                    if (s instanceof Stmt.Assert && ((Stmt.Assert) s).block == block) {
                        assertTrue(lib[1] + ": every assert of " + block + " comes after the call", i > call);
                    }
                    if (s instanceof Stmt.Input && ((Stmt.Input) s).block == block) {
                        assertTrue(lib[1] + ": every input of " + block + " comes before the call", i < call);
                    }
                }
                int assumes = 0;
                for (Stmt s : atc) if (s instanceof Stmt.Assume && ((Stmt.Assume) s).block == block) assumes++;
                assertEquals(lib[1] + ": one assume per precondition conjunct",
                        block.getPreconditions().size(), assumes);
            }
        }
    }

    @Test
    public void atcReadsTheLibrarysRealStateAndPrintsInTheAgreedShape() throws IOException {
        String atc = TestGen.print(TestGen.getATC(buildPlan("Stack")));
        assertTrue(atc.startsWith("// starting state: the library's real state after Helper.reset()\n"
                + "// call 0: push\n"
                + "old0_size := Helper.size\n"
                + "c0_elem := input<String>()\n"
                + "assume(c0_elem != null)\n"
                + "push(c0_elem)\n"
                + "assert(Helper.size == old0_size + 1)\n"
                + "assert(Helper.top == c0_elem)\n"));
        assertTrue(atc.contains("c2_result := pop()\n"));
        assertFalse("a clause that only names the result is not asserted", atc.contains("poppedElem"));
    }

    // ── rewriteATC ──────────────────────────────────────────────────────────

    @Test
    public void rewriteAtcOnTheSlideExample() {
        List<Stmt> t = Arrays.<Stmt>asList(input("x"), input("y"), input("z"));
        List<Stmt> out = TestGen.rewriteATC(t, Arrays.asList("1", "2"));
        assertEquals("x := 1\ny := 2\nz := input<int>()\n", TestGen.print(out));
        assertTrue(out.get(2) instanceof Stmt.Input);
        assertEquals("an empty L changes nothing", TestGen.print(t),
                TestGen.print(TestGen.rewriteATC(t, Collections.<String>emptyList())));
        try {
            TestGen.rewriteATC(Collections.<Stmt>emptyList(), Arrays.asList("1"));
            fail("|t| = 0 and |L| != 0 must raise an error");
        } catch (IllegalArgumentException expected) {
            // the slide's error case
        }
    }

    @Test
    public void rewriteAtcKeepsAssumesAndAsserts() {
        List<Stmt> atc = TestGen.getATC(stackPlan("push -> pop"));
        List<Stmt> out = TestGen.rewriteATC(atc, Arrays.asList("\"s1\""));
        assertEquals(atc.size(), out.size());
        assertFalse(TestGen.isAbstract(out));
        assertTrue(TestGen.print(out).contains("c0_elem := \"s1\"\nassume(c0_elem != null)\npush(c0_elem)\n"));
    }

    private static Stmt.Input input(String var) {
        ParamSlot slot = new ParamSlot(0, var, "int", in.ac.iiitb.plproject.ir.ParamKind.SYMBOLIC);
        return new Stmt.Input(var, "int", slot, null, 0);
    }

    // ── isReady / isSymbolic ─────────────────────────────────────────────────

    @Test
    public void isReadyIsFalseForAnApiCallWithASymbolicArgument() {
        Sigma sigma = new Sigma();
        sigma.declare("x", "int", Sigma.Value.SYMBOLIC);
        sigma.declare("y", "int", Sigma.Value.concrete("3"));
        Expr.Call symbolicCall = new Expr.Call("push", Arrays.<Expr>asList(new Expr.Var("y"), new Expr.Var("x")), true);
        Expr.Call concreteCall = new Expr.Call("push", Arrays.<Expr>asList(new Expr.Var("y"), new Expr.Num("4")), true);
        Expr.Call nonApi = new Expr.Call("abs", Arrays.<Expr>asList(new Expr.Var("x")), false);
        assertFalse(TestGen.isReady(symbolicCall, sigma));
        assertFalse(TestGen.isReady(new Stmt.Assign("r", "int", symbolicCall, null, null), sigma));
        assertTrue(TestGen.isReady(concreteCall, sigma));
        assertTrue("a call to a non-API function is ready", TestGen.isReady(nonApi, sigma));
        assertFalse(TestGen.isReady(new Expr.Var("x"), sigma));
        assertTrue(TestGen.isReady(new Expr.Num("7"), sigma));

        assertTrue(TestGen.isSymbolic(new Expr.Var("y")));
        assertFalse(TestGen.isSymbolic(new Expr.Num("7")));
        assertTrue(TestGen.isSymbolic(new Expr.Op("+", Arrays.<Expr>asList(new Expr.Num("1"), new Expr.Var("x")))));
        assertFalse(TestGen.isSymbolic(new Expr.Op("+", Arrays.<Expr>asList(new Expr.Num("1"), new Expr.Num("2")))));
    }

    @Test
    public void symexStopsAtTheFirstCallWithASymbolicArgument() {
        TestPlan plan = stackPlan("push -> pop -> push");
        final List<TestGen.Prefix> seen = new ArrayList<TestGen.Prefix>();
        TestGen.symex(TestGen.getATC(plan), TestGen.typeEnv(plan), prefix -> {
            seen.add(prefix);
            return Solution.values(Arrays.asList("\"s1\""), 0, null);
        });
        TestGen.Prefix p = seen.get(0);
        assertEquals("push(c0_elem)", p.firstNotReady.toString());
        assertEquals(1, p.inputs.size());
        assertEquals("c0_elem", p.inputs.get(0).var);
        assertEquals("(c0_elem != null)", p.pc.toString());
    }

    // ── genCTC with a scripted engine: NO_SOLUTION and the no-progress guard ──

    @Test
    public void noSolutionIsDistinctFromAnEmptyListAndNoProgressStops() {
        TestPlan plan = stackPlan("push -> pop");
        List<Stmt> atc = TestGen.getATC(plan);
        final Stmt.Assume[] blocking = new Stmt.Assume[1];
        for (Stmt s : atc) if (s instanceof Stmt.Assume && blocking[0] == null) blocking[0] = (Stmt.Assume) s;

        TestGen.Result rejected = TestGen.genCTC(atc, Collections.<String>emptyList(), TestGen.typeEnv(plan),
                prefix -> Solution.noSolution(blocking[0], 0, null));
        assertEquals(TestGen.Result.Status.INCORRECT_SEQUENCE, rejected.status);
        assertEquals("INCORRECT SEQUENCE: call 0 (push): elem != null", rejected.detail);
        assertNull(rejected.concrete);

        TestGen.Result stuck = TestGen.genCTC(atc, Collections.<String>emptyList(), TestGen.typeEnv(plan),
                prefix -> Solution.values(Collections.<String>emptyList(), 0, null));
        assertEquals(TestGen.Result.Status.NO_PROGRESS, stuck.status);
        assertEquals("one round, then stop", 1, stuck.symexRuns());
    }

    // ── the JUnit test is exactly the final t ───────────────────────────────

    @Test
    public void everyGeneratedAssertionTracesToAContractClause() throws IOException {
        for (String[] lib : SUPPORTED) {
            String specText = read("../specs/" + lib[0] + ".spec");
            String junit = junitOf(buildPlan(lib[0]));
            Matcher m = ASSERT_MESSAGE.matcher(junit);
            int checked = 0;
            while (m.find()) {
                // a literal backslash (from \old/\result) is doubled in the .java source
                String message = m.group(1).replace("\\\\", "\\");
                String clauseText = message.substring(message.indexOf(':') + 1).trim();
                assertTrue(lib[1] + ": '" + message + "' must trace to text in the spec", specText.contains(clauseText));
                checked++;
            }
            assertTrue(checked > 0);
        }
    }

    @Test
    public void junitIsExactlyTheConcreteTestCase() throws IOException {
        TestPlan plan = buildPlan("Stack");
        List<Stmt> concrete = syntheticConcrete(plan);
        String junit = JUnitPrinter.print(plan, concrete);
        int assumes = 0, asserts = 0, calls = 0;
        for (Stmt s : concrete) {
            if (s instanceof Stmt.Assume) assumes++;
            if (s instanceof Stmt.Assert) asserts++;
            if (s instanceof Stmt.Assign && ((Stmt.Assign) s).isApiCall()) calls++;
        }
        assertEquals(assumes, count(junit, "assertTrue(\"PRE "));
        assertEquals(asserts, count(junit, "assertTrue(\"POST "));
        assertEquals(calls, count(junit, "Helper.push(") + count(junit, "Helper.pop(") + count(junit, "Helper.peek("));
        assertTrue(junit.contains("Helper.reset();"));
        assertTrue(junit.contains("@Test(timeout = 2000)"));
        assertFalse("generated JUnit must not import gov.nasa.jpf", junit.contains("gov.nasa.jpf"));
        assertFalse(junit.contains("Debug."));
        assertFalse(junit.contains("Verify."));
        try {
            JUnitPrinter.print(plan, TestGen.getATC(plan));
            fail("an abstract test case must not be printed as a JUnit test");
        } catch (IllegalStateException expected) {
            // it still has input commands
        }
    }

    private static int count(String text, String needle) {
        int n = 0;
        for (int i = text.indexOf(needle); i >= 0; i = text.indexOf(needle, i + 1)) n++;
        return n;
    }

    @Test
    public void noAssertionOnlyNamesTheResult() throws IOException {
        for (String[] lib : SUPPORTED) {
            String junit = junitOf(buildPlan(lib[0]));
            Matcher m = ASSERT_EXPR.matcher(junit);
            while (m.find()) {
                String expr = m.group(1).trim();
                String[] sides = expr.split("==");
                assertFalse(lib[1] + ": tautological assertion " + expr,
                        sides.length == 2 && sides[0].trim().equals(sides[1].trim()));
            }
        }
    }

    @Test
    public void snapshotsArePlainLocalsOfTheDeclaredStateType() throws IOException {
        String junit = junitOf(buildPlan("Stack"));
        assertTrue(Pattern.compile("int old\\d+_size = Helper\\.size;").matcher(junit).find());
        assertFalse(Pattern.compile("Object old\\d+_size").matcher(junit).find());
    }

    @Test
    public void boxedServerOutputStaysBoxed() throws IOException {
        String junit = junitOf(buildPlan("HashMapLib"));
        assertTrue(Pattern.compile("Integer c\\d+_result = Helper\\.put\\(").matcher(junit).find());
        assertFalse(Pattern.compile("(?<!r)int c\\d+_result = Helper\\.put\\(").matcher(junit).find());
    }

    @Test
    public void statelessSequenceProducesNoReset() {
        String specText = "spec square {\n"
                + "    signature: int square(int n);\n"
                + "    requires:  n >= 0;\n"
                + "    ensures:   \\result == answer && answer >= 0;\n"
                + "}\n";
        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(specText, "<stateless>");
        TestPlan plan = IrBuilder.build(spec, TestStringParser.parse("square"), "<stateless>", "<stateless>");
        assertFalse(plan.isHasState());
        assertFalse(TestGen.getATC(plan).get(0) instanceof Stmt.Reset);
        assertFalse(junitOf(plan).contains("reset()"));
    }

    // ── the integer model: no pool, strings are ids, membership is equalities ──

    @Test
    public void noPoolCodeIsLeft() throws IOException {
        List<Path> sources = new ArrayList<Path>();
        try (java.util.stream.Stream<Path> files = Files.walk(Paths.get("src/main/java"))) {
            files.filter(f -> f.toString().endsWith(".java")).forEach(sources::add);
        }
        assertFalse(sources.isEmpty());
        Pattern pool = Pattern.compile("(?i)pool|\\bpick\\(|candidates");
        for (Path f : sources) {
            Matcher m = pool.matcher(read(f.toString()));
            if (m.find()) fail(f + " still mentions the pool: " + m.group());
        }
        for (String[] lib : SUPPORTED) {
            for (CallBlock block : buildPlan(lib[0]).getCallBlocks()) {
                for (ParamSlot p : block.getParams()) {
                    assertEquals(lib[1] + ": " + p, "String".equals(p.getType().trim()), p.isStringId());
                    assertEquals(lib[1] + ": " + p, !"String".equals(p.getType().trim()), p.isSymbolic());
                }
            }
        }
    }

    private static final Map<String, String> STATE = new LinkedHashMap<String, String>();
    static {
        STATE.put("Tasks", "Map<Integer,String>");
        STATE.put("Holds", "Map<Integer,String>");
        STATE.put("E", "Set<String>");
        STATE.put("Adj", "Map<String,List<String>>");
    }

    private static Map<String, String> symbolic(String... nameType) {
        Map<String, String> m = new LinkedHashMap<String, String>();
        for (int i = 0; i < nameType.length; i += 2) m.put(nameType[i], nameType[i + 1]);
        return m;
    }

    @Test
    public void containsKeyOnASymbolicKeyIsAnEqualityDisjunctionOverTheConcreteKeys() throws Exception {
        assertEquals("ok = memberKey(c1_taskId, Helper.Tasks);", IntegerModel.assume(
                "Helper.Tasks.containsKey(c1_taskId)", symbolic("c1_taskId", "Integer"), STATE));
        assertEquals("ok = !memberKey(c1_taskId, Helper.Tasks);", IntegerModel.assume(
                "!Helper.Tasks.containsKey(c1_taskId)", symbolic("c1_taskId", "Integer"), STATE));
        assertEquals("a concrete key is evaluated normally", "ok = Helper.Tasks.containsKey(c1_taskId);",
                IntegerModel.assume("Helper.Tasks.containsKey(c1_taskId)", symbolic(), STATE));
        assertEquals("ok = memberStr(c2_elem$id, c2_elem$nonNull, Helper.E);", IntegerModel.assume(
                "Helper.E.contains(c2_elem)", symbolic("c2_elem", "String"), STATE));
        assertEquals("ok = false; scan: for (Object k$0 : elements(Helper.Holds)) if (isKey(c2_seatHoldId, k$0)"
                + " && isStr(c2_email$id, c2_email$nonNull, Helper.Holds.get(k$0))) { ok = true; break scan; }",
                IntegerModel.assume("c2_email == Helper.Holds.get(c2_seatHoldId)",
                        symbolic("c2_seatHoldId", "Integer", "c2_email", "String"), STATE));
        assertEquals("ok = false; scan: for (Object k$0 : elements(Helper.Adj)) if (isStr(a$id, a$nonNull, k$0)"
                + " && memberStr(b$id, b$nonNull, Helper.Adj.get(k$0))) { ok = true; break scan; }",
                IntegerModel.assume("Helper.Adj.get(a).contains(b)", symbolic("a", "String", "b", "String"),
                        STATE));

        // the driver's own members, run on a concrete collection: one == per key, k == 1 || k == 2 || k == 5
        Assume.assumeTrue("scripts/setup-spf.sh has not been run on this machine", spfInstalled());
        Path dir = Files.createTempDirectory("membership");
        Path lib = dir.resolve("Helper.java");
        Files.write(lib, ("package " + TestPlan.GENERATED_PACKAGE + ";\n"
                + "public class Helper {\n"
                + "    public static java.util.Map<Integer, String> Tasks = new java.util.HashMap<Integer, String>();\n"
                + "    static { Tasks.put(1, \"a\"); Tasks.put(2, \"b\"); Tasks.put(5, \"c\"); }\n"
                + "}\n").getBytes(StandardCharsets.UTF_8));
        String driver = "package " + TestPlan.GENERATED_PACKAGE + ";\n"
                + "import gov.nasa.jpf.symbc.Debug;\nimport gov.nasa.jpf.vm.Verify;\n"
                + "public class " + TestPlan.SYMBOLIC_CLASS + " {\n"
                + "    public static void main(String[] args) {\n"
                + "        boolean ok;\n"
                + "        Integer k = new Integer(Debug.makeSymbolicInteger(\"k\"));\n"
                + "        try { " + IntegerModel.assume("Helper.Tasks.containsKey(k)", symbolic("k", "Integer"),
                        STATE) + " } catch (RuntimeException e) { ok = false; } Verify.ignoreIf(!ok);\n"
                + "    }\n" + IntegerModel.RUNTIME + "}\n";
        SpfSymexEngine.RawRun run = SpfSymexEngine.runDriver(driver, Collections.singletonList(lib.toString()),
                Files.createTempDirectory("membership-run").toFile(), SpfConfig.DEFAULT);
        assertNull(run.compileError, run.compileError);
        assertNull(run.blackBox, run.blackBox);
        String solved = null;
        for (in.ac.iiitb.plproject.spf.PathVector p : run.events) {
            if (p.getEvent() == in.ac.iiitb.plproject.spf.PathVector.Event.PATH && p.isSat()) solved = p.getVars().get("k");
        }
        assertNotNull("SPF solved the membership", solved);
        assertTrue("k is one of the concrete keys, got " + solved, Arrays.asList("1", "2", "5").contains(solved));
    }

    @Test
    public void aStringInputIsSolvedAsAnIntegerId() throws Exception {
        TestPlan plan = stackPlan("push -> pop");
        List<Stmt> atc = TestGen.getATC(plan);
        Stmt.Input elem = null;
        for (Stmt s : atc) if (s instanceof Stmt.Input) elem = (Stmt.Input) s;
        assertTrue(elem.slot.isStringId());
        final String[] driver = new String[1];
        TestGen.symex(atc, TestGen.typeEnv(plan), prefix -> {
            driver[0] = SymbolicDriverPrinter.print(plan, prefix, 0).source;
            return Solution.values(Arrays.asList("\"s7\""), 0, null);
        });
        assertTrue(driver[0], driver[0].contains("int c0_elem$id = Debug.makeSymbolicInteger(\"c0_elem$id\");"
                + " boolean c0_elem$nonNull = Debug.makeSymbolicBoolean(\"c0_elem$nonNull\");"));
        assertTrue(driver[0].contains("try { ok = c0_elem$nonNull; }"));
        assertEquals("ok = c0_elem$nonNull;", IntegerModel.assume("c0_elem != null", symbolic("c0_elem", "String"),
                plan.getStateVars()));
        assertEquals("ok = sameStr(x$id, x$nonNull, y$id, y$nonNull);", IntegerModel.assume("x.equals(y)",
                symbolic("x", "String", "y", "String"), plan.getStateVars()));
        assertEquals("ok = !isStr(x$id, x$nonNull, Helper.top);", IntegerModel.assume("x != Helper.top",
                symbolic("x", "String"), plan.getStateVars()));

        Map<String, String> vars = new LinkedHashMap<String, String>();
        vars.put("c0_elem$id", "42");
        vars.put("c0_elem$nonNull", "1");
        assertEquals("solved id k is the literal \"s\" + k", "\"s42\"", valueOf(elem, vars));
        vars.put("c0_elem$id", "-3");
        assertEquals("\"s-3\"", valueOf(elem, vars));
        vars.put("c0_elem$nonNull", "0");
        assertEquals("null", valueOf(elem, vars));
        assertEquals("a free id is small, distinct and non-zero", "\"s7\"",
                valueOf(elem, new LinkedHashMap<String, String>()));
    }

    private static String valueOf(Stmt.Input input, Map<String, String> vars) throws Exception {
        java.lang.reflect.Method m = SpfSymexEngine.class.getDeclaredMethod("valueOf", Stmt.Input.class,
                in.ac.iiitb.plproject.spf.PathVector.class);
        m.setAccessible(true);
        return (String) m.invoke(null, input, in.ac.iiitb.plproject.spf.PathVector.path(1, true, vars));
    }

    @Test
    public void aStringOperationStopsGenerationWithAnUnsupportedError() {
        String specText = "state { int n; }\n"
                + "spec greet {\n"
                + "    signature: void greet(String name);\n"
                + "    requires:  name.length() > 0;\n"
                + "    ensures:   n == \\old(n) + 1;\n"
                + "}\n";
        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(specText, "greet.spec");
        TestPlan plan = IrBuilder.build(spec, TestStringParser.parse("greet"), "greet.spec", "<seq>");
        List<Stmt> atc = TestGen.getATC(plan);
        TestGen.Result result = TestGen.genCTC(atc, Collections.<String>emptyList(), TestGen.typeEnv(plan),
                prefix -> {
                    try {
                        SymbolicDriverPrinter.print(plan, prefix, 0);
                        return Solution.values(Collections.<String>emptyList(), 0, null);
                    } catch (IntegerModel.Unsupported e) {
                        return Solution.failure(Solution.Kind.UNSUPPORTED, e.getMessage(), 0, null);
                    }
                });
        assertEquals(TestGen.Result.Status.UNSUPPORTED, result.status);
        assertEquals("UNSUPPORTED: unsupported string operation length in c0_name.length() > 0;"
                + " needs a string solver", result.detail);
        for (String op : new String[] { "charAt(0) == 'a'", "substring(1) == null", "startsWith(\"a\")",
                                        "concat(\"a\") != null", "indexOf(\"a\") > 0" }) {
            try {
                IntegerModel.assume("x." + op, symbolic("x", "String"), plan.getStateVars());
                fail(op + " must be rejected");
            } catch (IntegerModel.Unsupported e) {
                String name = op.substring(0, op.indexOf('('));
                assertEquals("unsupported string operation " + name + " in x." + op + "; needs a string solver",
                        e.getMessage());
            }
        }
        try {
            IntegerModel.assume("Helper.L.indexOf(x) >= 0", symbolic("x", "int"), Collections.singletonMap("L", "List<Integer>"));
            fail("a collection call that is not membership cannot be expanded");
        } catch (IntegerModel.Unsupported e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith("unsupported membership clause Helper.L.indexOf(x)"));
        }
    }

    @Test
    public void roundDriverEndsAtTheFirstNotReadyCallAndPassesHelperOnlyConcreteValues() {
        TestPlan plan = stackPlan("push -> pop -> push");
        List<Stmt> t = TestGen.rewriteATC(TestGen.getATC(plan), Arrays.asList("\"s1\""));
        final String[] source = new String[1];
        TestGen.symex(t, TestGen.typeEnv(plan), prefix -> {
            source[0] = SymbolicDriverPrinter.print(plan, prefix, 1).source;
            return Solution.values(Arrays.asList("\"s2\""), 0, null);
        });
        String driver = source[0];
        assertTrue(driver.contains("String c0_elem = \"s1\";"));
        assertTrue(driver.contains("Helper.push(c0_elem);"));
        assertTrue(driver.contains("String c1_result = Helper.pop();"));
        assertTrue(driver.contains("int c2_elem$id = Debug.makeSymbolicInteger(\"c2_elem$id\");"));
        assertFalse("no String object exists for a symbolic string", driver.contains("String c2_elem"));
        assertFalse("the program ends before the not-ready call", driver.contains("Helper.push(c2_elem)"));
        assertTrue(driver.contains("Verify.ignoreIf(!ok);"));
    }

    // ── real SPF ────────────────────────────────────────────────────────────

    @Test
    public void blackBoxGuardFiresWhenASymbolicValueReachesHelper() throws Exception {
        Assume.assumeTrue("scripts/setup-spf.sh has not been run on this machine", spfInstalled());
        Path dir = Files.createTempDirectory("blackbox");
        Path lib = dir.resolve("Helper.java");
        Files.write(lib, ("package " + TestPlan.GENERATED_PACKAGE + ";\n"
                + "public class Helper {\n"
                + "    public static int last;\n"
                + "    public static Integer boxed;\n"
                + "    public static int twice(int x) { last = 2 * x; return last; }\n"
                + "    public static int box(Integer x) { return 1; }\n"
                + "    public static int read() { return last; }\n"
                + "}\n").getBytes(StandardCharsets.UTF_8));
        List<String> libs = Collections.singletonList(lib.toString());

        String[][] cases = {
            { "symbolic argument", "Helper.twice(Debug.makeSymbolicInteger(\"x\"));", "true" },
            { "symbolic field of a boxed argument", "Helper.box(new Integer(Debug.makeSymbolicInteger(\"x\")));", "true" },
            { "symbolic field read inside Helper", "Helper.last = Debug.makeSymbolicInteger(\"x\"); Helper.read();", "true" },
            { "concrete argument", "Helper.twice(3); Helper.read();", "false" },
        };
        for (String[] c : cases) {
            String driver = "package " + TestPlan.GENERATED_PACKAGE + ";\n"
                    + "import gov.nasa.jpf.symbc.Debug;\n"
                    + "public class " + TestPlan.SYMBOLIC_CLASS + " {\n"
                    + "    public static void main(String[] args) { " + c[1] + " }\n"
                    + "}\n";
            SpfSymexEngine.RawRun run = SpfSymexEngine.runDriver(driver, libs,
                    Files.createTempDirectory("blackbox-run").toFile(), SpfConfig.DEFAULT);
            assertNull(c[0] + ": " + run.compileError, run.compileError);
            if (Boolean.parseBoolean(c[2])) {
                assertNotNull(c[0] + ": the guard must fire", run.blackBox);
                assertTrue(run.blackBox, run.blackBox.startsWith("BLACK-BOX VIOLATION"));
            } else {
                assertNull(c[0] + ": the guard must not fire", run.blackBox);
            }
        }
    }

    @Test
    public void incorrectSequenceIsRejectedAtTheCallThatCannotRun() throws Exception {
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
        assertFalse(junitFile.exists());
    }

    @Test
    public void idLibrariesStillGenerateTestsThatPassOnTheCorrectLibrary() throws Exception {
        Assume.assumeTrue("scripts/setup-spf.sh has not been run on this machine", spfInstalled());
        for (LibraryRegistry.Entry base : Arrays.asList(LibraryRegistry.TASKQUEUE, LibraryRegistry.TICKETSERVICE,
                                                        LibraryRegistry.ORDERSERVICE)) {
            Path outDir = Files.createTempDirectory(base.key + "-out");
            LibraryRegistry.Entry entry = LibraryRegistry.variantOf(base, base.librarySourcePaths, base.testsPath,
                    outDir.toString());
            GenatcMain.LibraryOutcome outcome = GenatcMain.runOne(entry);
            assertEquals(base.key + ": " + outcome.detail, "GENERATED", outcome.status);
            assertEquals(base.key, 1, outcome.testsGenerated);
            assertTrue(base.key + ": " + outcome.junitFailures, outcome.junitFailures.isEmpty());
        }
    }

    @Test
    public void genCtcTakesOneRoundPerCallWithArguments() throws Exception {
        Assume.assumeTrue("scripts/setup-spf.sh has not been run on this machine", spfInstalled());
        TestPlan plan = stackPlan("push -> push -> pop");
        File work = Files.createTempDirectory("genctc").toFile();
        TestGen.Result result = TestGen.genCTC(TestGen.getATC(plan), Collections.<String>emptyList(),
                TestGen.typeEnv(plan), new SpfSymexEngine(plan, LibraryRegistry.STACK.librarySourcePaths, work,
                        SpfConfig.DEFAULT));

        assertEquals(TestGen.Result.Status.GENERATED, result.status);
        assertEquals("one round per call with arguments", 2, result.concretisingRounds());
        assertEquals("the first symex call, then one per round", 3, result.symexRuns());
        assertFalse("the concrete test case has no input command", TestGen.isAbstract(result.concrete));
        for (TestGen.Round r : result.rounds) assertEquals(Solution.Kind.VALUES, r.solution.kind);
        assertEquals("a free string id: small and non-zero", Arrays.asList("\"s7\""),
                result.rounds.get(0).solution.values);
        assertEquals("distinct from the first", Arrays.asList("\"s9\""), result.rounds.get(1).solution.values);
        assertEquals(Collections.<String>emptyList(), result.rounds.get(2).solution.values);
        assertTrue(JUnitPrinter.print(plan, result.concrete).contains("String c1_elem = \"s9\";"));
    }

    @Test
    public void oneTestMethodPerSequence() throws Exception {
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
        assertEquals("one @Test method per sequence", 2, count(outcome.junitSource, "@Test("));
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

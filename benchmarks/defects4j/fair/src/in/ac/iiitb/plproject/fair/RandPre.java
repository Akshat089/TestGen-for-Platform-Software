package in.ac.iiitb.plproject.fair;

import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.IrBuilder;
import in.ac.iiitb.plproject.ir.ParamSlot;
import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.parser.SpecToAstConverter;
import in.ac.iiitb.plproject.parser.TestStringParser;
import in.ac.iiitb.plproject.parser.ast.JmlSpecAst;
import in.ac.iiitb.plproject.parser.ast.TestStringAst;
import in.ac.iiitb.plproject.symbolic.JUnitPrinter;
import in.ac.iiitb.plproject.symbolic.Stmt;
import in.ac.iiitb.plproject.symbolic.TestGen;
import in.ac.iiitb.plproject.symbolic.TypeSupport;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * RandPre: a random baseline with exactly TestGen's information (fair/README.md, "RandPre").
 *
 * <p>Input: TestGen's own test strings ({@code .tests}), the {@code .spec}, the real adapter and the contract
 * monitor. For each string, its calls run in order on the real (monitored) adapter, from {@code Helper.reset()}.
 * For each call, argument values are drawn at random and the first draw whose precondition holds on the current
 * real state is kept; the precondition is evaluated with the monitor's own Java for it ({@code contracts.tsv},
 * which {@link MonitorGen} wrote: the same clauses, in order, with {@code Objects.equals} for objects). Then the
 * call is made through the monitored adapter, which must log no PRE failure for it (a check that the two agree).
 * If no draw within the budget satisfies the precondition, the string is rejected, as TestGen rejects an
 * INCORRECT_SEQUENCE; if the kept call throws, it is rejected as TestGen's LIBRARY_EXCEPTION.
 *
 * <p>Values, as TestGen's solver: int/Integer in [-1000, 1000]; String {@code "s"+k}, k in [-1000, 1000]
 * (interned, as the literal in the written test is); boolean true/false. With {@code pool=true}, each argument
 * is, with probability 1/2, taken instead from the values seen earlier in the same string (the arguments of the
 * calls already made and the non-null values they returned) of the same type, when there is one.
 *
 * <p>Budget: {@code draws=N} draws per call, or {@code timing=<timing.tsv>} with {@code scope=<bug>}: the string
 * gets TestGen's wall-clock generation time for it (column total_ms) instead, for all its calls together.
 *
 * <p>Output: the kept values are put into TestGen's own abstract test case ({@link TestGen#getATC},
 * {@link TestGen#rewriteATC}) and printed by TestGen's {@link JUnitPrinter}: the same JUnit form
 * (Helper.reset(), fixed values, PRE and POST assertTrue from the contract). Plus {@code randpre.tsv}, one row per
 * string.
 *
 * <pre>
 *   RandPre spec=.. tests=.. which=long|all seed=N pool=true|false (draws=N | timing=.. scope=..)
 *           contracts=monitor-src/contracts.tsv monitorHelper=monitor-src/.../Helper.java out=DIR
 * </pre>
 * The classpath must hold the monitored adapter (monitor-f) and the fixed library classes before anything else.
 */
public final class RandPre {

    static final String PKG = "in.ac.iiitb.plproject.atc.generated";
    static final int MIN = -1000, MAX = 1000;

    public static void main(String[] argv) throws Exception {
        Map<String, String> a = new HashMap<String, String>();
        for (String s : argv) {
            int i = s.indexOf('=');
            if (i < 0) throw new IllegalArgumentException("expected key=value: " + s);
            a.put(s.substring(0, i), s.substring(i + 1));
        }
        String specPath = req(a, "spec"), testsPath = req(a, "tests");
        boolean onlyLong = req(a, "which").equals("long");
        long seed = Long.parseLong(req(a, "seed"));
        boolean pool = Boolean.parseBoolean(req(a, "pool"));
        int draws = a.containsKey("draws") ? Integer.parseInt(a.get("draws")) : -1;
        Map<String, Long> timeBudget = a.containsKey("timing") ? readTiming(a.get("timing"), req(a, "scope")) : null;
        if ((draws < 0) == (timeBudget == null)) throw new IllegalArgumentException("give draws=N or timing=..");
        Path out = Paths.get(req(a, "out"));

        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(read(specPath), specPath);
        List<TestStringAst> tests = TestStringParser.parseAll(read(testsPath), testsPath);
        if (onlyLong) tests = tests.subList(0, 1);   // the blind long string: the first one (as fair.sh's testgen_long)

        Files.createDirectories(out);
        Class<?> helper = Class.forName(PKG + ".Helper");
        Class<?> checks = compileChecks(spec, specPath, Paths.get(req(a, "contracts")),
                Paths.get(req(a, "monitorHelper")), out.resolve("checks"));
        Method reset = helper.getMethod("reset");

        List<JUnitPrinter.Sequence> concrete = new ArrayList<JUnitPrinter.Sequence>();
        StringBuilder tsv = new StringBuilder("string\tcalls\tstatus\tat_call\tdraws_total\tdraws_per_call\tbudget\tms\tseed\tpool\n");
        for (TestStringAst test : tests) {
            TestPlan plan = IrBuilder.build(spec, test, specPath, testsPath);
            List<Stmt> atc = TestGen.getATC(plan);
            Random rnd = new Random(seed * 1_000_003L + test.getName().hashCode());
            long budgetMs = -1;
            if (timeBudget != null) {
                Long ms = timeBudget.get(test.getName());
                if (ms == null) throw new IllegalStateException("no TestGen time for " + test.getName() + " in the timing file");
                budgetMs = ms;
            }
            long t0 = System.nanoTime();
            long deadline = budgetMs < 0 ? Long.MAX_VALUE : t0 + budgetMs * 1_000_000L;
            Map<String, List<Object>> seen = new HashMap<String, List<Object>>();
            Map<String, Set<Object>> seenSet = new HashMap<String, Set<Object>>();
            List<String> literals = new ArrayList<String>();
            List<Long> perCall = new ArrayList<Long>();
            String status = "GENERATED", at = "";
            long total = 0;
            reset.invoke(null);
            fairmon.FairLog.drain();
            for (CallBlock b : plan.getCallBlocks()) {
                List<ParamSlot> ps = b.getParams();
                Method op = adapterMethod(helper, b.getOperationName(), ps.size());
                Method pre = checks.getMethod("pre_" + b.getOperationName(), Object[].class);
                Object[] args = new Object[ps.size()];
                long n = 0;
                boolean ok = false;
                while (draws < 0 ? System.nanoTime() < deadline : n < draws) {
                    n++;
                    for (int i = 0; i < ps.size(); i++) args[i] = draw(rnd, ps.get(i).getType(), pool, seen);
                    if (pre.invoke(null, (Object) args) == null) { ok = true; break; }
                }
                total += n;
                perCall.add(n);
                if (!ok) { status = "REJECTED"; at = b.getIndex() + ":" + b.getOperationName(); break; }
                fairmon.FairLog.drain();
                Object ret;
                try {
                    ret = op.invoke(null, args);
                } catch (InvocationTargetException e) {
                    status = "LIBRARY_EXCEPTION";
                    at = b.getIndex() + ":" + b.getOperationName() + " " + e.getCause();
                    break;
                }
                for (String ev : fairmon.FairLog.drain()) {
                    if (ev.startsWith("PRE\t") || ev.startsWith("PREX\t")) {
                        throw new IllegalStateException("RandPre's precondition check and the monitor disagree on "
                                + test.getName() + " call " + b.getIndex() + " " + b.getOperationName() + ": " + ev);
                    }
                }
                for (int i = 0; i < ps.size(); i++) {
                    literals.add(literal(ps.get(i).getType(), args[i]));
                    remember(seen, seenSet, args[i]);
                }
                remember(seen, seenSet, ret);
            }
            long ms = (System.nanoTime() - t0) / 1_000_000L;
            StringBuilder pc = new StringBuilder();
            for (Long x : perCall) pc.append(pc.length() == 0 ? "" : ",").append(x);
            tsv.append(test.getName()).append('\t').append(plan.getCallBlocks().size()).append('\t').append(status)
               .append('\t').append(at.replace('\t', ' ').replace('\n', ' ')).append('\t').append(total).append('\t')
               .append(pc).append('\t').append(draws < 0 ? budgetMs + "ms" : draws + "draws").append('\t').append(ms)
               .append('\t').append(seed).append('\t').append(pool).append('\n');
            if (status.equals("GENERATED")) {
                concrete.add(new JUnitPrinter.Sequence(plan, TestGen.rewriteATC(atc, literals)));
            }
        }
        reset.invoke(null);
        Files.write(out.resolve("randpre.tsv"), tsv.toString().getBytes(StandardCharsets.UTF_8));
        System.out.print(tsv);
        Path gen = out.resolve("raw").resolve(PKG.replace('.', '/'));
        Files.createDirectories(gen);
        if (!concrete.isEmpty()) {
            String src = JUnitPrinter.print(TestPlan.JUNIT_CLASS, concrete);
            Files.write(gen.resolve(TestPlan.JUNIT_CLASS + ".java"), src.getBytes(StandardCharsets.UTF_8));
        }
        System.exit(0);
    }

    // ── values ──

    static String kind(String type) {
        switch (type.trim()) {
            case "int": case "Integer": return "int";
            case "String": return "String";
            case "boolean": case "Boolean": return "boolean";
            default: throw new IllegalArgumentException("RandPre has no value domain for type " + type);
        }
    }

    static Object draw(Random rnd, String type, boolean pool, Map<String, List<Object>> seen) {
        String k = kind(type);
        if (pool) {
            boolean fromPool = rnd.nextBoolean();
            List<Object> p = seen.get(k);
            if (fromPool && p != null && !p.isEmpty()) return p.get(rnd.nextInt(p.size()));
        }
        switch (k) {
            case "int": return MIN + rnd.nextInt(MAX - MIN + 1);
            case "String": return ("s" + (MIN + rnd.nextInt(MAX - MIN + 1))).intern();
            default: return rnd.nextBoolean();
        }
    }

    /** Pool: the distinct non-null int/String/boolean values seen, in the order first seen. */
    static void remember(Map<String, List<Object>> seen, Map<String, Set<Object>> seenSet, Object v) {
        String k;
        if (v instanceof Integer) k = "int";
        else if (v instanceof Boolean) k = "boolean";
        else if (v instanceof String && printable((String) v)) { k = "String"; v = ((String) v).intern(); }
        else return;
        if (seenSet.computeIfAbsent(k, x -> new LinkedHashSet<Object>()).add(v)) {
            seen.computeIfAbsent(k, x -> new ArrayList<Object>()).add(v);
        }
    }

    /** A String the written test can hold as a plain literal (TypeSupport.literal escapes only \ and "). */
    static boolean printable(String s) {
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) < 0x20 || s.charAt(i) > 0x7e) return false;
        return true;
    }

    /** Java source of a kept value, formatted by TestGen's own TypeSupport.literal. */
    static String literal(String type, Object v) {
        return TypeSupport.literal(type, String.valueOf(v));
    }

    static Method adapterMethod(Class<?> helper, String name, int arity) {
        for (Method m : helper.getMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == arity && Modifier.isStatic(m.getModifiers())) return m;
        }
        throw new IllegalStateException("the adapter has no " + name + "/" + arity);
    }

    // ── the precondition, as the monitor checks it ──

    /**
     * One {@code pre_<op>(Object[])} per operation of the spec: the monitor's PRE conjuncts for it, from
     * contracts.tsv (the monitor's final Java), in order; returns null if all hold, else the first false
     * clause. A clause that throws makes the call invalid, as in the monitor.
     */
    static Class<?> compileChecks(JmlSpecAst spec, String specPath, Path contracts, Path monitorHelper, Path dir)
            throws Exception {
        Map<String, List<String[]>> pre = new LinkedHashMap<String, List<String[]>>();
        List<String> lines = Files.readAllLines(contracts, StandardCharsets.UTF_8);
        for (String l : lines.subList(1, lines.size())) {
            String[] f = l.split("\t", -1);
            if (f.length >= 4 && f[1].equals("PRE")) pre.computeIfAbsent(f[0], k -> new ArrayList<String[]>()).add(f);
        }
        StringBuilder sb = new StringBuilder("package " + PKG + ";\n\n");
        for (String l : Files.readAllLines(monitorHelper, StandardCharsets.UTF_8)) {
            if (l.startsWith("import ")) sb.append(l).append('\n');
        }
        sb.append("\n@SuppressWarnings({\"unchecked\", \"rawtypes\"})\npublic final class RandPreChecks {\n");
        Set<String> ops = new LinkedHashSet<String>();
        for (in.ac.iiitb.plproject.parser.ast.JmlFunctionSpec fn : spec.getSpecs()) ops.add(fn.getName());
        for (String op : ops) {
            TestPlan one = IrBuilder.build(spec, new TestStringAst("randpre_" + op, Collections.singletonList(op)),
                    specPath, "randpre");
            CallBlock b = one.getCallBlocks().get(0);
            sb.append("    public static String pre_").append(op).append("(Object[] a) {\n");
            for (int i = 0; i < b.getParams().size(); i++) {
                ParamSlot p = b.getParams().get(i);
                sb.append("        ").append(p.getType()).append(' ').append(p.getSlotName()).append(" = (")
                  .append(TypeSupport.boxed(p.getType())).append(") a[").append(i).append("];\n");
            }
            sb.append("        try {\n");
            for (String[] c : pre.getOrDefault(op, Collections.<String[]>emptyList())) {
                sb.append("            if (!((").append(c[3]).append("))) return ").append(javaString(c[2])).append(";\n");
            }
            sb.append("        } catch (Throwable t) {\n            return \"threw \" + t;\n        }\n        return null;\n    }\n");
        }
        sb.append("}\n");
        Path srcDir = dir.resolve("src").resolve(PKG.replace('.', '/'));
        Path classes = dir.resolve("classes");
        Files.createDirectories(srcDir);
        Files.createDirectories(classes);
        Path src = srcDir.resolve("RandPreChecks.java");
        Files.write(src, sb.toString().getBytes(StandardCharsets.UTF_8));
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int rc = javac.run(null, null, err, "-nowarn", "-proc:none", "-d", classes.toString(),
                "-cp", System.getProperty("java.class.path"), src.toString());
        if (rc != 0) throw new IllegalStateException("RandPreChecks does not compile:\n" + err);
        URLClassLoader cl = new URLClassLoader(new URL[] { classes.toUri().toURL() }, RandPre.class.getClassLoader());
        return cl.loadClass(PKG + ".RandPreChecks");
    }

    // ── helpers ──

    static Map<String, Long> readTiming(String file, String scope) throws Exception {
        Map<String, Long> m = new HashMap<String, Long>();
        List<String> lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
        String[] h = lines.get(0).split("\t");
        int cs = idx(h, "scope"), cn = idx(h, "string"), ct = idx(h, "total_ms"), cr = idx(h, "run");
        for (String l : lines.subList(1, lines.size())) {
            String[] f = l.split("\t", -1);
            if (f[cs].equals(scope) && (cr < 0 || f[cr].equals("1"))) m.put(f[cn], Long.parseLong(f[ct]));
        }
        return m;
    }

    static int idx(String[] h, String name) {
        for (int i = 0; i < h.length; i++) if (h[i].equals(name)) return i;
        return -1;
    }

    static String req(Map<String, String> a, String k) {
        String v = a.get(k);
        if (v == null) throw new IllegalArgumentException("missing " + k + "=");
        return v;
    }

    static String read(String p) throws Exception {
        return new String(Files.readAllBytes(Paths.get(p)), StandardCharsets.UTF_8);
    }

    static String javaString(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            if (c == '\\' || c == '"') sb.append('\\').append(c);
            else if (c == '\n') sb.append("\\n");
            else sb.append(c);
        }
        return sb.append('"').toString();
    }

    private RandPre() {}
}

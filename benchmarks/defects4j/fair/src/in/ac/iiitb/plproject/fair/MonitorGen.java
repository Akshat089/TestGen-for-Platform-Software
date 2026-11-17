package in.ac.iiitb.plproject.fair;

import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.Clause;
import in.ac.iiitb.plproject.ir.IrBuilder;
import in.ac.iiitb.plproject.ir.ParamSlot;
import in.ac.iiitb.plproject.ir.Snapshot;
import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.parser.SpecToAstConverter;
import in.ac.iiitb.plproject.parser.ast.JmlFunctionSpec;
import in.ac.iiitb.plproject.parser.ast.JmlSpecAst;
import in.ac.iiitb.plproject.parser.ast.TestStringAst;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builds the CONTRACT MONITOR for one adapter.
 *
 * <pre>
 *   MonitorGen &lt;spec&gt; &lt;adapter Helper.java&gt; &lt;classpath of the library&gt; &lt;outDir&gt;
 * </pre>
 *
 * Writes, under {@code outDir/<package dirs>/}:
 * <ul>
 *   <li>{@code HelperImpl.java}: the adapter itself, renamed (every {@code Helper}
 *       identifier becomes {@code HelperImpl}); nothing else changes;</li>
 *   <li>{@code Helper.java}: the monitored adapter. Same fully-qualified name, same public
 *       static methods and state fields. Each method logs the call, checks the
 *       precondition, saves {@code \old(v)}, forwards to HelperImpl, copies the state
 *       fields back, logs what the call returned, and checks the postcondition.</li>
 * </ul>
 * and {@code outDir/contracts.tsv}: every check, as written in the .spec and as Java.
 *
 * <p>The checks are not written here: TestGen's own parser ({@link SpecToAstConverter}) and
 * IR builder ({@link IrBuilder}, which uses {@code ClauseText} to split the clauses and to
 * rewrite state variables, {@code \old(v)} and {@code \result}) produce them, exactly as for
 * TestGen's JUnit tests. One plan is built per operation (a one-call sequence), so the
 * names are {@code c0_<param>}, {@code old0_<v>} and {@code c0_result}; the monitor declares
 * locals with those names.
 *
 * <p>One deliberate difference: an {@code ==} or {@code !=} whose two operands are objects
 * (javac's attribution decides, on the generated monitor) becomes {@code Objects.equals}.
 * Other tools pass equal strings that are not the same object.
 */
public final class MonitorGen {

    private static final String OBJ_EQ = "java.util.Objects.equals";

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("usage: MonitorGen <spec> <Helper.java> <library classpath> <outDir>");
            System.exit(64);
        }
        Path specFile = Paths.get(args[0]);
        Path adapter = Paths.get(args[1]);
        String libCp = args[2];
        Path outDir = Paths.get(args[3]);

        JmlSpecAst spec = SpecToAstConverter.convertSpecToAst(
                new String(Files.readAllBytes(specFile), StandardCharsets.UTF_8), specFile.toString());
        String adapterSrc = new String(Files.readAllBytes(adapter), StandardCharsets.UTF_8);
        Adapter a = parseAdapter(adapter, adapterSrc);

        Path pkgDir = outDir.resolve(a.pkg.replace('.', '/'));
        Files.createDirectories(pkgDir);
        String impl = adapterSrc.replaceAll("\\bHelper\\b", "HelperImpl");
        Files.write(pkgDir.resolve("HelperImpl.java"), impl.getBytes(StandardCharsets.UTF_8));

        List<int[]> spans = new ArrayList<int[]>();
        StringBuilder contracts = new StringBuilder("op\tkind\tclause\tjava\n");
        String monitor = printMonitor(spec, specFile.toString(), a, spans, contracts);

        // == between objects -> Objects.equals, decided by javac's types on the monitor itself
        Path monitorFile = pkgDir.resolve("Helper.java");
        Files.write(monitorFile, monitor.getBytes(StandardCharsets.UTF_8));
        String fixed = objectEquality(monitorFile, pkgDir.resolve("HelperImpl.java"), libCp, monitor, spans);
        Files.write(monitorFile, fixed.getBytes(StandardCharsets.UTF_8));

        // contracts.tsv: the final Java of every check
        StringBuilder finalContracts = new StringBuilder(contracts.toString().split("\n", 2)[0]).append('\n');
        Matcher m = Pattern.compile("/\\*CHECK (\\S+) (PRE|POST) (\\d+)\\*/\\(\\((.*?)\\)\\)/\\*END\\*/").matcher(fixed);
        String[] lines = contracts.toString().split("\n");
        int i = 1;
        while (m.find()) {
            String[] f = lines[i++].split("\t");
            finalContracts.append(f[0]).append('\t').append(f[1]).append('\t').append(f[2]).append('\t')
                          .append(m.group(4)).append('\n');
        }
        Files.write(outDir.resolve("contracts.tsv"), finalContracts.toString().getBytes(StandardCharsets.UTF_8));
        int rewritten = countOf(fixed, OBJ_EQ);
        System.out.println("monitor: " + a.methods.size() + " methods, " + (lines.length - 1)
                + " checks, " + rewritten + " object comparisons use Objects.equals -> " + monitorFile);
    }

    // ── the adapter ──

    static final class Field {
        String type, name;
        boolean isFinal, plain;   // plain: primitive or String, logged in observations
    }

    static final class Param {
        String type, name;
    }

    static final class Method {
        String name, returnType;
        final List<Param> params = new ArrayList<Param>();
        final List<String> throwsList = new ArrayList<String>();
    }

    static final class Adapter {
        String pkg;
        final List<String> imports = new ArrayList<String>();
        final List<Field> fields = new ArrayList<Field>();
        final List<Method> methods = new ArrayList<Method>();
        boolean hasReset;
    }

    static Adapter parseAdapter(Path file, String src) throws IOException {
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fm = javac.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        JavacTask task = (JavacTask) javac.getTask(null, fm, d -> { }, Arrays.asList("-proc:none"), null,
                fm.getJavaFileObjects(file.toFile()));
        CompilationUnitTree cu = task.parse().iterator().next();
        SourcePositions pos = Trees.instance(task).getSourcePositions();
        Adapter a = new Adapter();
        a.pkg = cu.getPackageName() == null ? "" : cu.getPackageName().toString();
        for (ImportTree it : cu.getImports()) a.imports.add(text(src, pos, cu, it).trim());
        for (Tree t : cu.getTypeDecls()) {
            if (!(t instanceof ClassTree) || !((ClassTree) t).getSimpleName().contentEquals("Helper")) continue;
            for (Tree member : ((ClassTree) t).getMembers()) {
                if (member instanceof VariableTree) {
                    VariableTree v = (VariableTree) member;
                    if (!v.getModifiers().getFlags().containsAll(Arrays.asList(Modifier.PUBLIC, Modifier.STATIC))) continue;
                    Field f = new Field();
                    f.type = text(src, pos, cu, v.getType());
                    f.name = v.getName().toString();
                    f.isFinal = v.getModifiers().getFlags().contains(Modifier.FINAL);
                    f.plain = f.type.matches("int|long|short|byte|char|boolean|double|float|String|java\\.lang\\.String");
                    a.fields.add(f);
                } else if (member instanceof MethodTree) {
                    MethodTree mt = (MethodTree) member;
                    if (!mt.getModifiers().getFlags().containsAll(Arrays.asList(Modifier.PUBLIC, Modifier.STATIC))) continue;
                    if (mt.getName().contentEquals("reset") && mt.getParameters().isEmpty()) { a.hasReset = true; continue; }
                    if (!mt.getTypeParameters().isEmpty()) throw new IllegalStateException("generic adapter method " + mt.getName());
                    Method m = new Method();
                    m.name = mt.getName().toString();
                    m.returnType = text(src, pos, cu, mt.getReturnType());
                    for (VariableTree p : mt.getParameters()) {
                        Param q = new Param();
                        q.type = text(src, pos, cu, p.getType());
                        q.name = p.getName().toString();
                        m.params.add(q);
                    }
                    for (ExpressionTree e : mt.getThrows()) m.throwsList.add(text(src, pos, cu, e));
                    a.methods.add(m);
                }
            }
        }
        if (!a.hasReset) throw new IllegalStateException(file + ": the adapter has no public static reset()");
        return a;
    }

    // ── the monitored adapter ──

    static String printMonitor(JmlSpecAst spec, String specFile, Adapter a, List<int[]> spans,
                               StringBuilder contracts) {
        StringBuilder sb = new StringBuilder();
        if (!a.pkg.isEmpty()) sb.append("package ").append(a.pkg).append(";\n\n");
        for (String imp : a.imports) sb.append(imp).append('\n');
        sb.append("\n/**\n * CONTRACT MONITOR, generated by benchmarks/defects4j/fair (MonitorGen) from ")
          .append(specFile).append(".\n")
          .append(" * Same name, public static methods and state fields as the adapter; every call is\n")
          .append(" * forwarded to HelperImpl (the adapter, renamed) and checked against the contract.\n */\n")
          .append("@SuppressWarnings({\"unchecked\", \"rawtypes\"})\npublic class Helper {\n\n");
        for (Field f : a.fields) {
            sb.append("    public static ").append(f.isFinal ? "final " : "").append(f.type).append(' ').append(f.name)
              .append(f.isFinal ? " = HelperImpl." + f.name : "").append(";\n");
        }
        sb.append("\n    static {\n        HelperImpl.reset();\n        pull();\n        fairmon.FairLog.reset();\n    }\n\n");
        sb.append("    /** Copies the adapter's state fields into this class (after every call). */\n");
        sb.append("    private static void pull() {\n");
        for (Field f : a.fields) if (!f.isFinal) sb.append("        ").append(f.name).append(" = HelperImpl.").append(f.name).append(";\n");
        sb.append("    }\n\n    /** Copies state fields the test wrote back into the adapter, as the real adapter would see them. */\n");
        sb.append("    private static void push() {\n");
        for (Field f : a.fields) if (!f.isFinal) sb.append("        HelperImpl.").append(f.name).append(" = ").append(f.name).append(";\n");
        sb.append("    }\n\n    /** The first state field the test wrote directly (not through an adapter call), or null. */\n");
        sb.append("    private static String tampered() {\n");
        for (Field f : a.fields) {
            if (!f.isFinal) sb.append("        if (").append(f.name).append(" != HelperImpl.").append(f.name)
                              .append(") return \"").append(f.name).append("\";\n");
        }
        sb.append("        return null;\n    }\n\n    /** The plain state fields, for the observation log. */\n");
        sb.append("    private static String state() {\n        return \"\"");
        for (Field f : a.fields) if (f.plain) sb.append(" + \"|").append(f.name).append("=\" + fairmon.FairLog.show(").append(f.name).append(")");
        sb.append(";\n    }\n\n");
        sb.append("    public static void reset() {\n        HelperImpl.reset();\n        pull();\n        fairmon.FairLog.reset();\n    }\n");

        for (Method m : a.methods) {
            JmlFunctionSpec fn = spec.findSpecFor(m.name);
            sb.append('\n');
            sb.append("    public static ").append(m.returnType).append(' ').append(m.name).append('(');
            for (int i = 0; i < m.params.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(m.params.get(i).type).append(' ').append(m.params.get(i).name);
            }
            sb.append(')');
            if (!m.throwsList.isEmpty()) sb.append(" throws ").append(String.join(", ", m.throwsList));
            sb.append(" {\n");
            String op = javaString(m.name);
            sb.append("        fairmon.FairLog.call(").append(op).append(");\n");
            sb.append("        String fairTampered = tampered();\n");
            sb.append("        if (fairTampered != null) {\n            fairmon.FairLog.tamper(").append(op)
              .append(", fairTampered);\n            push();\n        }\n");
            CallBlock b = null;
            if (fn == null) {
                sb.append("        fairmon.FairLog.pre(").append(op).append(", \"not an operation of the contract\");\n");
            } else {
                TestPlan plan = IrBuilder.build(spec, new TestStringAst("fair_" + m.name,
                        Collections.singletonList(m.name)), specFile, "fair-monitor");
                b = plan.getCallBlocks().get(0);
                if (b.getParams().size() != m.params.size()) {
                    throw new IllegalStateException(m.name + ": the spec has " + b.getParams().size()
                            + " parameters, the adapter " + m.params.size());
                }
                for (int i = 0; i < m.params.size(); i++) {
                    ParamSlot s = b.getParams().get(i);
                    sb.append("        ").append(m.params.get(i).type).append(' ').append(s.getSlotName())
                      .append(" = ").append(m.params.get(i).name).append(";\n");
                }
                checks(sb, m.name, "PRE", b.getPreconditions(), "pre", "preThrew", spans, contracts);
                for (Snapshot s : b.getSnapshots()) {
                    sb.append("        ").append(fieldType(a, s.getStateVarName(), s.getType())).append(' ')
                      .append(s.getLocalName()).append(" = Helper.").append(s.getStateVarName()).append(";\n");
                }
            }
            boolean isVoid = m.returnType.equals("void");
            if (!isVoid) sb.append("        ").append(m.returnType).append(" fairRet;\n");
            sb.append("        try {\n            ").append(isVoid ? "" : "fairRet = ").append("HelperImpl.").append(m.name).append('(');
            for (int i = 0; i < m.params.size(); i++) sb.append(i > 0 ? ", " : "").append(m.params.get(i).name);
            sb.append(");\n        } catch (Throwable fairT) {\n            pull();\n");
            sb.append("            fairmon.FairLog.thrown(").append(op).append(", fairT);\n");
            sb.append("            fairmon.FairLog.obs(").append(op).append(", \"throws \" + fairT.getClass().getName() + state());\n");
            sb.append("            throw fairT;\n        }\n        pull();\n");
            sb.append("        fairmon.FairLog.obs(").append(op).append(", ")
              .append(isVoid ? "\"void\"" : "\"returns \" + fairmon.FairLog.show(fairRet)").append(" + state());\n");
            if (b != null) {
                if (b.isStoresResult()) {
                    sb.append("        ").append(m.returnType).append(' ').append(b.getResultVarName()).append(" = fairRet;\n");
                }
                List<Clause> posts = new ArrayList<Clause>();
                for (Clause c : b.getPostconditions()) if (!c.isTautological()) posts.add(c);
                checks(sb, m.name, "POST", posts, "post", "postThrew", spans, contracts);
            }
            if (!isVoid) sb.append("        return fairRet;\n");
            sb.append("    }\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * The clauses in order; the first false one is logged and the rest are not evaluated,
     * exactly like TestGen's consecutive assertTrue statements.
     */
    private static void checks(StringBuilder sb, String op, String kind, List<Clause> clauses, String logFalse,
                               String logThrew, List<int[]> spans, StringBuilder contracts) {
        if (clauses.isEmpty()) return;
        sb.append("        try {\n");
        for (int i = 0; i < clauses.size(); i++) {
            Clause c = clauses.get(i);
            sb.append(i == 0 ? "            if (!" : "            else if (!");
            sb.append("/*CHECK ").append(op).append(' ').append(kind).append(' ').append(i).append("*/((");
            int start = sb.length();
            sb.append(c.getJavaExpr());
            spans.add(new int[] { start, sb.length() });
            sb.append("))/*END*/) fairmon.FairLog.").append(logFalse).append('(').append(javaString(op)).append(", ")
              .append(javaString(c.getOriginalText())).append(");\n");
            contracts.append(op).append('\t').append(kind).append('\t').append(c.getOriginalText()).append('\t')
                     .append(c.getJavaExpr()).append('\n');
        }
        sb.append("        } catch (Throwable fairT) {\n            fairmon.FairLog.").append(logThrew).append('(')
          .append(javaString(op)).append(", fairT);\n        }\n");
    }

    private static String fieldType(Adapter a, String name, String specType) {
        for (Field f : a.fields) if (f.name.equals(name)) return f.type;
        return specType;
    }

    // ── == between objects -> Objects.equals ──

    static String objectEquality(Path monitorFile, Path implFile, String libCp, String src, List<int[]> spans)
            throws IOException {
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fm = javac.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        String fairmonCp = FairmonLocation.get();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        JavacTask task = (JavacTask) javac.getTask(new OutputStreamWriter(err, StandardCharsets.UTF_8), fm, null,
                Arrays.asList("-proc:none", "-nowarn", "-cp", libCp + File.pathSeparator + fairmonCp), null,
                fm.getJavaFileObjects(monitorFile.toFile(), implFile.toFile()));
        CompilationUnitTree monitorCu = null;
        for (CompilationUnitTree cu : task.parse()) {
            if (cu.getSourceFile().toUri().equals(monitorFile.toUri())) monitorCu = cu;
        }
        task.analyze();
        final Trees trees = Trees.instance(task);
        final SourcePositions pos = trees.getSourcePositions();
        final CompilationUnitTree cu = monitorCu;
        final List<long[]> edits = new ArrayList<long[]>();   // start, end, opEnd of the operator, isNotEqual
        new TreePathScanner<Void, Void>() {
            @Override public Void visitBinary(BinaryTree node, Void v) {
                if (node.getKind() == Tree.Kind.EQUAL_TO || node.getKind() == Tree.Kind.NOT_EQUAL_TO) {
                    long s = pos.getStartPosition(cu, node), e = pos.getEndPosition(cu, node);
                    if (inside(spans, s, e)) {
                        TypeMirror l = trees.getTypeMirror(new TreePath(getCurrentPath(), node.getLeftOperand()));
                        TypeMirror r = trees.getTypeMirror(new TreePath(getCurrentPath(), node.getRightOperand()));
                        if (l == null || r == null || l.getKind() == TypeKind.ERROR || r.getKind() == TypeKind.ERROR) {
                            throw new IllegalStateException("cannot type " + node + ":\n" + err);
                        }
                        if (isObject(l) && isObject(r)) {
                            edits.add(new long[] { s, e,
                                    pos.getStartPosition(cu, node.getLeftOperand()), pos.getEndPosition(cu, node.getLeftOperand()),
                                    pos.getStartPosition(cu, node.getRightOperand()), pos.getEndPosition(cu, node.getRightOperand()),
                                    node.getKind() == Tree.Kind.NOT_EQUAL_TO ? 1 : 0 });
                        }
                    }
                }
                return super.visitBinary(node, v);
            }
        }.scan(new TreePath(cu), null);
        if (err.size() > 0 && err.toString("UTF-8").contains("error:")) {
            throw new IllegalStateException("the monitor does not compile:\n" + err.toString("UTF-8"));
        }
        return rewrite(src, 0, src.length(), edits);
    }

    private static boolean isObject(TypeMirror t) {
        TypeKind k = t.getKind();
        return k == TypeKind.DECLARED || k == TypeKind.ARRAY || k == TypeKind.TYPEVAR || k == TypeKind.INTERSECTION;
    }

    private static boolean inside(List<int[]> spans, long s, long e) {
        for (int[] sp : spans) if (s >= sp[0] && e <= sp[1]) return true;
        return false;
    }

    /** src[from, to) with every edit inside it applied (edits may nest: a == (b == c) on objects). */
    private static String rewrite(String src, long from, long to, List<long[]> edits) {
        StringBuilder out = new StringBuilder();
        long at = from;
        List<long[]> top = new ArrayList<long[]>();
        for (long[] e : edits) {
            if (e[0] < from || e[1] > to || (e[0] == from && e[1] == to)) continue;
            boolean nested = false;
            for (long[] o : edits) {
                if (o != e && o[0] <= e[0] && o[1] >= e[1] && !(o[0] == e[0] && o[1] == e[1])
                        && o[0] >= from && o[1] <= to && !(o[0] == from && o[1] == to)) { nested = true; break; }
            }
            if (!nested) top.add(e);
        }
        top.sort((x, y) -> Long.compare(x[0], y[0]));
        for (long[] e : top) {
            out.append(src, (int) at, (int) e[0]);
            out.append(e[6] == 1 ? "!" : "").append(OBJ_EQ).append('(')
               .append(rewrite(src, e[2], e[3], edits)).append(", ").append(rewrite(src, e[4], e[5], edits)).append(')');
            at = e[1];
        }
        out.append(src, (int) at, (int) to);
        return out.toString();
    }

    // ── helpers ──

    private static String text(String src, SourcePositions pos, CompilationUnitTree cu, Tree t) {
        return src.substring((int) pos.getStartPosition(cu, t), (int) pos.getEndPosition(cu, t));
    }

    private static String javaString(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            if (c == '\\' || c == '"') sb.append('\\').append(c);
            else if (c == '\n') sb.append("\\n");
            else sb.append(c);
        }
        return sb.append('"').toString();
    }

    private static int countOf(String s, String sub) {
        int n = 0;
        for (int i = s.indexOf(sub); i >= 0; i = s.indexOf(sub, i + 1)) n++;
        return n;
    }

    /** Where fairmon.FairLog was loaded from (the monitor is type-checked against it). */
    static final class FairmonLocation {
        static String get() {
            try {
                return new File(Class.forName("fairmon.FairLog").getProtectionDomain().getCodeSource()
                        .getLocation().toURI()).getPath();
            } catch (Exception e) {
                throw new IllegalStateException("fairmon.FairLog is not on the classpath", e);
            }
        }
    }

    private MonitorGen() {}
}

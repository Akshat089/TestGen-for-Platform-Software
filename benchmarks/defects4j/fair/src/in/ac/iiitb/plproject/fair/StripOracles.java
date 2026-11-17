package in.ac.iiitb.plproject.fair;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaCompiler;
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
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Rule 5 (same oracle) for Randoop and EvoSuite suites: keeps only the call sequences.
 *
 * <pre>
 *   StripOracles &lt;srcDir&gt; &lt;outDir&gt; &lt;classpath&gt; &lt;adapter FQN&gt;
 * </pre>
 *
 * In every {@code @Test} method:
 * <ul>
 *   <li>removes each statement that is a call to {@code assert*}, {@code fail} or
 *       {@code verifyException} (any qualifier), and Java {@code assert} statements. If an
 *       assertion's arguments call the adapter, those calls are kept as statements, so the
 *       call sequence is unchanged;</li>
 *   <li>keeps everything else, try/catch included (the monitor's log decides the outcome);</li>
 *   <li>rule 1 (same API): before a statement that calls a library method directly (any
 *       method that is not the adapter's, not java.lang's String/Object/boxes/Math, not
 *       JUnit's, EvoSuite's runtime or a PrintStream), inserts
 *       {@code fairmon.FairLog.offApi(...)}: the monitor treats the test as INVALID from its
 *       next adapter call. Randoop is already restricted to the adapter by its options;
 *       EvoSuite cannot be, because it calls methods of the objects it gets (e.g. the
 *       adapter's public state fields).</li>
 * </ul>
 * Scaffolding and other classes without {@code @Test} are copied unchanged. Prints one line:
 * {@code files tests assertions_removed calls_kept_from_assertions offapi_markers}.
 */
public final class StripOracles {

    private static final Pattern ORACLE = Pattern.compile("assert\\w*|fail|verifyException");

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("usage: StripOracles <srcDir> <outDir> <classpath> <adapter FQN>");
            System.exit(64);
        }
        Path srcDir = Paths.get(args[0]), outDir = Paths.get(args[1]);
        String cp = args[2], adapter = args[3];
        List<Path> files;
        try (Stream<Path> w = Files.walk(srcDir)) {
            files = w.filter(p -> p.toString().endsWith(".java")).sorted().collect(Collectors.toList());
        }
        if (files.isEmpty()) throw new IllegalStateException("no sources in " + srcDir);

        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fm = javac.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        JavacTask task = (JavacTask) javac.getTask(new OutputStreamWriter(err, StandardCharsets.UTF_8), fm, null,
                Arrays.asList("-proc:none", "-nowarn", "-encoding", "UTF-8", "-cp", cp), null,
                fm.getJavaFileObjects(files.stream().map(Path::toFile).toArray(File[]::new)));
        List<CompilationUnitTree> units = new ArrayList<CompilationUnitTree>();
        for (CompilationUnitTree cu : task.parse()) units.add(cu);
        task.analyze();
        String errors = err.toString("UTF-8");
        if (errors.contains("error:")) throw new IllegalStateException("the generated tests do not compile:\n" + errors);
        Trees trees = Trees.instance(task);

        int[] totals = new int[5];   // files, tests, removed, kept calls, offapi
        for (CompilationUnitTree cu : units) {
            Path in = Paths.get(cu.getSourceFile().toUri());
            String src = new String(Files.readAllBytes(in), StandardCharsets.UTF_8);
            Stripper s = new Stripper(cu, trees, src, adapter);
            s.scan(new TreePath(cu), null);
            Path out = outDir.resolve(srcDir.relativize(in));
            Files.createDirectories(out.getParent());
            Files.write(out, s.result().getBytes(StandardCharsets.UTF_8));
            totals[0]++;
            totals[1] += s.tests;
            totals[2] += s.removed;
            totals[3] += s.keptCalls;
            totals[4] += s.offApi;
        }
        System.out.println("files " + totals[0] + " tests " + totals[1] + " assertions_removed " + totals[2]
                + " calls_kept_from_assertions " + totals[3] + " offapi_markers " + totals[4]);
    }

    static final class Edit {
        final long start, end;
        final String text;
        Edit(long start, long end, String text) { this.start = start; this.end = end; this.text = text; }
    }

    static final class Stripper extends TreePathScanner<Void, Void> {
        final CompilationUnitTree cu;
        final Trees trees;
        final SourcePositions pos;
        final String src, adapter;
        final List<Edit> edits = new ArrayList<Edit>();
        int tests, removed, keptCalls, offApi;
        boolean inTest;

        Stripper(CompilationUnitTree cu, Trees trees, String src, String adapter) {
            this.cu = cu;
            this.trees = trees;
            this.pos = trees.getSourcePositions();
            this.src = src;
            this.adapter = adapter;
        }

        @Override public Void visitMethod(MethodTree m, Void v) {
            boolean test = false;
            for (AnnotationTree a : m.getModifiers().getAnnotations()) {
                String n = a.getAnnotationType().toString();
                if (n.equals("Test") || n.equals("org.junit.Test")) test = true;
            }
            if (!test || m.getBody() == null) return null;
            tests++;
            inTest = true;
            super.visitMethod(m, v);
            inTest = false;
            return null;
        }

        @Override public Void visitBlock(BlockTree b, Void v) {
            if (inTest) {
                for (StatementTree st : b.getStatements()) handle(st);
            }
            return super.visitBlock(b, v);
        }

        @Override public Void visitClass(ClassTree c, Void v) {
            return super.visitClass(c, v);
        }

        /** One statement directly inside a block of a test method. */
        private void handle(StatementTree st) {
            long s = pos.getStartPosition(cu, st), e = pos.getEndPosition(cu, st);
            TreePath path = TreePath.getPath(cu, st);
            if (st instanceof AssertTree) {
                edits.add(new Edit(s, e, ";"));
                removed++;
                return;
            }
            if (st instanceof ExpressionStatementTree
                    && ((ExpressionStatementTree) st).getExpression() instanceof MethodInvocationTree) {
                MethodInvocationTree mi = (MethodInvocationTree) ((ExpressionStatementTree) st).getExpression();
                if (ORACLE.matcher(simpleName(mi)).matches()) {
                    List<String> calls = adapterCalls(path);
                    keptCalls += calls.size();
                    StringBuilder repl = new StringBuilder();
                    for (String c : calls) repl.append(c).append("; ");
                    // calls that are kept still go through the monitor; other direct library calls are marked
                    String marker = offApiIn(path, true);
                    if (marker != null) { repl.insert(0, marker); offApi++; }
                    edits.add(new Edit(s, e, repl.length() == 0 ? ";" : "{ " + repl + "}"));
                    removed++;
                    return;
                }
            }
            String marker = offApiIn(path, false);
            if (marker != null) {
                edits.add(new Edit(s, s, marker));
                offApi++;
            }
        }

        /** Source text of the outermost adapter calls inside an assertion. */
        private List<String> adapterCalls(TreePath stmt) {
            final List<String> out = new ArrayList<String>();
            new TreePathScanner<Void, Void>() {
                @Override public Void visitMethodInvocation(MethodInvocationTree mi, Void v) {
                    if (isAdapter(getCurrentPath())) {
                        out.add(src.substring((int) pos.getStartPosition(cu, mi), (int) pos.getEndPosition(cu, mi)));
                        return null;
                    }
                    return super.visitMethodInvocation(mi, v);
                }
            }.scan(stmt, null);
            return out;
        }

        /**
         * A marker statement if this statement (not counting nested blocks, which are handled
         * on their own) calls a library method directly; else null. In an assertion, only
         * calls inside kept adapter calls' arguments matter.
         */
        private String offApiIn(TreePath stmt, final boolean assertion) {
            final String[] found = new String[1];
            new TreePathScanner<Void, Void>() {
                @Override public Void visitBlock(BlockTree b, Void v) { return null; }
                @Override public Void visitLambdaExpression(LambdaExpressionTree l, Void v) { return null; }
                @Override public Void visitClass(ClassTree c, Void v) { return null; }
                @Override public Void visitMethodInvocation(MethodInvocationTree mi, Void v) {
                    if (assertion && ORACLE.matcher(simpleName(mi)).matches()) return super.visitMethodInvocation(mi, v);
                    if (found[0] == null && !allowed(getCurrentPath())) found[0] = describe(getCurrentPath());
                    return super.visitMethodInvocation(mi, v);
                }
            }.scan(stmt, null);
            if (found[0] == null) return null;
            return "fairmon.FairLog.offApi(\"" + found[0].replace("\\", "\\\\").replace("\"", "\\\"") + "\"); ";
        }

        private boolean isAdapter(TreePath p) {
            TypeElement owner = owner(p);
            return owner != null && owner.getQualifiedName().contentEquals(adapter);
        }

        private boolean allowed(TreePath p) {
            TypeElement owner = owner(p);
            if (owner == null) return true;   // could not resolve: never happens for compiling code
            String n = owner.getQualifiedName().toString();
            if (n.equals(adapter)) return true;
            if (n.startsWith("org.junit.") || n.startsWith("junit.") || n.startsWith("org.evosuite.")
                    || n.startsWith("org.hamcrest.") || n.equals("java.io.PrintStream") || n.startsWith("fairmon.")) {
                return true;
            }
            return n.equals("java.lang.String") || n.equals("java.lang.Object") || n.equals("java.lang.Math")
                    || n.equals("java.lang.Integer") || n.equals("java.lang.Long") || n.equals("java.lang.Short")
                    || n.equals("java.lang.Byte") || n.equals("java.lang.Character") || n.equals("java.lang.Boolean")
                    || n.equals("java.lang.Double") || n.equals("java.lang.Float") || n.equals("java.lang.Number");
        }

        private TypeElement owner(TreePath p) {
            Element el = trees.getElement(p);
            if (!(el instanceof ExecutableElement)) return null;
            Element o = el.getEnclosingElement();
            return o instanceof TypeElement ? (TypeElement) o : null;
        }

        private String describe(TreePath p) {
            TypeElement o = owner(p);
            Element el = trees.getElement(p);
            return (o == null ? "?" : o.getSimpleName()) + "." + (el == null ? "?" : el.getSimpleName());
        }

        String result() {
            edits.sort((a, b) -> a.start != b.start ? Long.compare(a.start, b.start) : Long.compare(a.end, b.end));
            StringBuilder out = new StringBuilder();
            long at = 0;
            for (Edit e : edits) {
                if (e.start < at) continue;   // inside a statement already replaced
                out.append(src, (int) at, (int) e.start).append(e.text);
                at = e.end;
            }
            out.append(src.substring((int) at));
            return out.toString();
        }
    }

    static String simpleName(MethodInvocationTree mi) {
        Tree sel = mi.getMethodSelect();
        if (sel instanceof MemberSelectTree) return ((MemberSelectTree) sel).getIdentifier().toString();
        return sel.toString();
    }

    private StripOracles() {}
}

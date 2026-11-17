package in.ac.iiitb.plproject.mutation;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generates the Mothra mutants for ONE real Defects4J class, for the mutation
 * comparison of TestGen, Randoop and EvoSuite.
 *
 * <p>It reuses {@link OperatorMutantGenerator} unchanged for the mutants
 * themselves, and adds the two things that generator cannot do on real Commons
 * code:
 * <ol>
 *   <li><b>The exact line of each mutant.</b> The generator emits mutants in line
 *       order but does not record the line, and {@link Mutant#applyTo} finds a
 *       method by name (first match, and only on headers with an access or static
 *       modifier), which on {@code StrBuilder}'s many {@code append} overloads
 *       would plant a mutant in the wrong overload or not at all. The line is
 *       recovered by replaying the generator's own line filter (called through
 *       reflection, so it is exactly the generator's rule).</li>
 *   <li><b>Which method each line really belongs to, and whether the adapter can
 *       reach it.</b> The library is parsed and attributed with the JDK's own
 *       compiler (javac Tree API), so method extents are exact (overloads,
 *       {@code throws}, {@code synchronized}, multi-line headers, inner and
 *       anonymous classes). Reachability is a rapid-type-analysis call graph
 *       from every method of the adapter, over all library sources.</li>
 * </ol>
 *
 * <pre>
 *   D4jMutantGen &lt;key&gt; &lt;target.java&gt; &lt;sources.txt&gt; &lt;outDir&gt;
 * </pre>
 * {@code sources.txt} is run.sh's {@code fixed-sources.txt}: the adapter first, then
 * every library file it needs. Writes {@code <outDir>/<key>.mutants} (the ordinary
 * format; {@code in:} is the exact method), {@code <key>.mutants.tsv} (one row per
 * in-scope mutant, with the line it is planted at) and {@code <key>.scope.txt}.
 */
public final class D4jMutantGen {

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.err.println("usage: D4jMutantGen <key> <target.java> <sources.txt> <outDir>");
            System.exit(64);
        }
        String key = args[0];
        Path target = Paths.get(args[1]).toAbsolutePath().normalize();
        List<String> sources = new ArrayList<String>();
        for (String l : Files.readAllLines(Paths.get(args[2]), StandardCharsets.UTF_8)) {
            if (!l.trim().isEmpty()) sources.add(l.trim());
        }
        Path outDir = Paths.get(args[3]);
        Files.createDirectories(outDir);

        // 1. All mutants of the file, from the unchanged generator.
        List<Mutant> all = OperatorMutantGenerator.generate(key, Collections.singletonList(target.toString()),
                Integer.MAX_VALUE);
        List<String> lines = Arrays.asList(new String(Files.readAllBytes(target), StandardCharsets.UTF_8)
                .split("\n", -1));
        int[] lineOf = locate(all, lines);

        // 2. Exact methods and reachability, from javac.
        CallGraph graph = new CallGraph(sources, target, sources.get(0));
        graph.build();

        // 3. Keep mutants inside a reachable method or initializer of the target file.
        List<Row> kept = new ArrayList<Row>();
        Map<String, int[]> perOp = new TreeMap<String, int[]>();
        int outsideCode = 0, unreachable = 0;
        Set<String> unreachableMethods = new TreeSet<String>();
        for (int i = 0; i < all.size(); i++) {
            Mutant m = all.get(i);
            String op = MutationOperator.abbreviationOf(m.getOperator());
            int[] c = perOp.computeIfAbsent(op, k -> new int[3]);
            c[0]++;
            CallGraph.Unit unit = graph.unitAt(lineOf[i] + 1);
            if (unit == null || m.getFind().contains("serialVersionUID")) { outsideCode++; continue; }
            if (!graph.reachable.contains(unit)) {
                unreachable++;
                unreachableMethods.add(unit.label);
                continue;
            }
            c[1]++;
            kept.add(new Row(m, lineOf[i] + 1, unit, graph.direct.contains(unit)));
        }
        int inScope = kept.size();

        // 4. Write the three files.
        StringBuilder tsv = new StringBuilder("id\tline\toperator\tabbr\tmethod\tdirect\tfind\treplace\n");
        List<Mutant> rendered = new ArrayList<Mutant>();
        for (Row r : kept) {
            Mutant m = r.mutant;
            tsv.append(m.getId()).append('\t').append(r.line).append('\t').append(m.getOperator()).append('\t')
               .append(MutationOperator.abbreviationOf(m.getOperator())).append('\t').append(r.unit.label)
               .append('\t').append(r.direct ? "direct" : "indirect").append('\t').append(tab(m.getFind()))
               .append('\t').append(tab(m.getReplacement())).append('\n');
            String description = m.getDescription().replaceFirst(", in [^,]*$", "")
                    + ", in " + r.unit.label + " at line " + r.line;
            rendered.add(new Mutant(m.getId(), key, description, m.getOperator(), r.unit.methodName,
                    m.getFile(), m.getFind(), m.getReplacement(), true, "", "generated"));
        }
        Files.write(outDir.resolve(key + ".mutants.tsv"), tsv.toString().getBytes(StandardCharsets.UTF_8));
        String header = OperatorMutantGenerator.render(key, Collections.singletonList(target.toString()), rendered)
                .replace("DO NOT EDIT. Regenerate with ./run-operator-suites.sh " + key,
                         "DO NOT EDIT. Regenerate with benchmarks/defects4j/mutation.sh --regen " + key
                         + "\n * Each mutant is planted at the line in " + key + ".mutants.tsv, not by `in:`.");
        Files.write(outDir.resolve(key + ".mutants"), header.getBytes(StandardCharsets.UTF_8));

        StringBuilder scope = new StringBuilder();
        scope.append("class: ").append(target).append('\n');
        scope.append("generated by OperatorMutantGenerator (no cap): ").append(all.size()).append('\n');
        scope.append("  outside any method/initializer body, or serialVersionUID: ").append(outsideCode).append('\n');
        scope.append("  in code the adapter cannot reach:       ").append(unreachable).append('\n');
        scope.append("  in reachable code (in scope):           ").append(inScope).append('\n');
        int direct = 0;
        for (Row r : kept) if (r.direct) direct++;
        scope.append("    of which in a method the adapter calls directly: ").append(direct).append('\n');
        scope.append("(all in-scope mutants are written; mutation.sh compiles them and samples its cap from the valid ones)\n");
        scope.append("\nper operator: generated / in scope\n");
        for (Map.Entry<String, int[]> e : perOp.entrySet()) {
            scope.append(String.format("  %-5s %5d %5d%n", e.getKey(), e.getValue()[0], e.getValue()[1]));
        }
        scope.append("\nreachable methods of the class (").append(graph.reachableInTarget().size()).append("), * = called directly by the adapter:\n");
        for (String s : graph.reachableInTarget()) scope.append("  ").append(s).append('\n');
        scope.append("\nunreachable methods with mutants (left out):\n");
        for (String s : unreachableMethods) scope.append("  ").append(s).append('\n');
        Files.write(outDir.resolve(key + ".scope.txt"), scope.toString().getBytes(StandardCharsets.UTF_8));
        System.out.print(scope);
    }

    private static String tab(String s) { return s.replace('\t', ' '); }

    private static final class Row {
        final Mutant mutant; final int line; final CallGraph.Unit unit; final boolean direct;
        Row(Mutant mutant, int line, CallGraph.Unit unit, boolean direct) {
            this.mutant = mutant; this.line = line; this.unit = unit; this.direct = direct;
        }
    }

    /**
     * The 0-based line each mutant was generated from. The generator walks lines in
     * order and emits each line's mutants together, so a forward scan for the first
     * line that matches {@code find} AND passes the generator's own line filter finds
     * exactly the line it used.
     */
    @SuppressWarnings("unchecked")
    static int[] locate(List<Mutant> mutants, List<String> lines) throws Exception {
        Class<?> g = OperatorMutantGenerator.class;
        Field headerField = g.getDeclaredField("METHOD_HEADER");
        headerField.setAccessible(true);
        Pattern header = (Pattern) headerField.get(null);
        Method isMutableCode = g.getDeclaredMethod("isMutableCode", String.class);
        Method isHarness = g.getDeclaredMethod("isHarnessMethod", String.class);
        Method countIn = g.getDeclaredMethod("countOccurrencesInMethod", List.class, int.class, String.class);
        isMutableCode.setAccessible(true);
        isHarness.setAccessible(true);
        countIn.setAccessible(true);

        // the generator's idea of "the method this line is in", for its harness rule
        String[] genMethod = new String[lines.size()];
        boolean[] isHeader = new boolean[lines.size()];
        String current = null;
        for (int i = 0; i < lines.size(); i++) {
            Matcher h = header.matcher(lines.get(i));
            if (h.matches()) { current = h.group(2); isHeader[i] = true; }
            genMethod[i] = current;
        }

        int[] out = new int[mutants.size()];
        int p = 0;
        for (int k = 0; k < mutants.size(); k++) {
            String find = mutants.get(k).getFind();
            while (p < lines.size()) {
                String line = lines.get(p);
                if (!isHeader[p] && line.trim().equals(find)
                        && (Boolean) isMutableCode.invoke(null, line)
                        && !(genMethod[p] != null && (Boolean) isHarness.invoke(null, genMethod[p]))
                        && (Integer) countIn.invoke(null, lines, p, line.trim()) == 1) {
                    break;
                }
                p++;
            }
            if (p >= lines.size()) {
                throw new IllegalStateException("cannot locate mutant " + mutants.get(k).getId() + ": " + find);
            }
            out[k] = p;
        }
        return out;
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * A rapid-type-analysis call graph over the adapter and the library sources.
     *
     * <p>Nodes ("units") are methods and constructors with a body, plus one
     * pseudo-unit per class for its instance initializers and one for its static
     * initializers. Roots: every method of the adapter (the tests may call any of
     * them). A virtual call reaches, in every class instantiated so far that is a
     * subtype of the call's owner, the most specific implementation in the sources.
     *
     * <p>When a virtual call on an instantiated library class lands in a JDK
     * superclass (e.g. {@code TreeList.add(E)} is {@code AbstractList.add}), the
     * JDK code may call back any of the class's overrides of JDK methods
     * ({@code add(int, E)} here), so all of those become reachable too. This and
     * name-free dispatch make the graph an over-approximation; the
     * {@code direct} flag (a method the adapter itself dispatches to) is the
     * strict alternative.
     */
    static final class CallGraph {
        static final class Unit {
            final String label;          // e.g. SetUniqueList.add(int,Object)
            final String methodName;     // e.g. add, <init>, <clinit>
            final TypeElement owner;
            final ExecutableElement element;   // null for initializer pseudo-units
            final Path file;
            final int startLine, endLine;     // body extent, 1-based inclusive (0 = no source)
            final List<Call> calls = new ArrayList<Call>();
            final List<TypeElement> news = new ArrayList<TypeElement>();
            final List<ExecutableElement> ctorCalls = new ArrayList<ExecutableElement>();
            boolean explicitCtorCall;
            Unit(String label, String methodName, TypeElement owner, ExecutableElement element, Path file,
                 int startLine, int endLine) {
                this.label = label; this.methodName = methodName; this.owner = owner; this.element = element;
                this.file = file; this.startLine = startLine; this.endLine = endLine;
            }
        }

        static final class Call {
            final ExecutableElement callee; final boolean virtual;
            Call(ExecutableElement callee, boolean virtual) { this.callee = callee; this.virtual = virtual; }
        }

        private final List<String> sources;
        private final Path target;
        private final Path adapter;
        private Trees trees;
        private Elements elements;
        private Types types;

        final Map<ExecutableElement, Unit> byMethod = new HashMap<ExecutableElement, Unit>();
        final Map<TypeElement, Unit> instanceInit = new HashMap<TypeElement, Unit>();
        final Map<TypeElement, Unit> staticInit = new HashMap<TypeElement, Unit>();
        final Set<TypeElement> sourceTypes = new LinkedHashSet<TypeElement>();
        final List<Unit> targetUnits = new ArrayList<Unit>();
        final Set<Unit> reachable = new LinkedHashSet<Unit>();
        final Set<Unit> direct = new HashSet<Unit>();

        CallGraph(List<String> sources, Path target, String adapter) {
            this.sources = sources;
            this.target = target;
            this.adapter = Paths.get(adapter).toAbsolutePath().normalize();
        }

        void build() throws Exception {
            JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diags = new DiagnosticCollector<JavaFileObject>();
            StandardJavaFileManager fm = javac.getStandardFileManager(diags, null, StandardCharsets.UTF_8);
            List<File> files = new ArrayList<File>();
            for (String s : sources) files.add(new File(s));
            JavacTask task = (JavacTask) javac.getTask(null, fm, diags,
                    Arrays.asList("-proc:none", "-nowarn", "-Xlint:none", "-implicit:none"), null,
                    fm.getJavaFileObjectsFromFiles(files));
            Iterable<? extends CompilationUnitTree> units = task.parse();
            task.analyze();
            trees = Trees.instance(task);
            elements = task.getElements();
            types = task.getTypes();
            for (CompilationUnitTree cu : units) collect(cu);
            solve();
        }

        /** The innermost unit whose body contains this 1-based line of the target file. */
        Unit unitAt(int line) {
            Unit best = null;
            for (Unit u : targetUnits) {
                if (line >= u.startLine && line <= u.endLine
                        && (best == null || u.endLine - u.startLine < best.endLine - best.startLine)) {
                    best = u;
                }
            }
            return best;
        }

        List<String> reachableInTarget() {
            List<String> out = new ArrayList<String>();
            Set<String> seen = new HashSet<String>();
            for (Unit u : targetUnits) {
                if (reachable.contains(u) && seen.add(u.label)) out.add((direct.contains(u) ? "* " : "  ") + u.label);
            }
            return out;
        }

        // ── collection ──

        private void collect(CompilationUnitTree cu) {
            Path file = Paths.get(cu.getSourceFile().toUri()).toAbsolutePath().normalize();
            boolean isTarget = file.equals(target);
            SourcePositions pos = trees.getSourcePositions();
            LineMap lm = cu.getLineMap();

            new TreePathScanner<Void, Deque<Unit>>() {
                @Override
                public Void visitClass(ClassTree node, Deque<Unit> stack) {
                    final TreePath classPath = getCurrentPath();
                    TypeElement type = (TypeElement) trees.getElement(classPath);
                    if (type == null) return null;
                    sourceTypes.add(type);
                    // pseudo-units for initializers; their extent is each initializer itself
                    for (Tree member : node.getMembers()) {
                        boolean isStatic;
                        if (member instanceof VariableTree && ((VariableTree) member).getInitializer() != null) {
                            isStatic = ((VariableTree) member).getModifiers().getFlags().contains(Modifier.STATIC);
                        } else if (member instanceof BlockTree) {
                            isStatic = ((BlockTree) member).isStatic();
                        } else {
                            continue;
                        }
                        Map<TypeElement, Unit> map = isStatic ? staticInit : instanceInit;
                        Unit u = map.get(type);
                        if (u == null) {
                            u = new Unit(simpleLabel(type) + (isStatic ? ".<clinit>" : ".<init-fields>"),
                                    isStatic ? "<clinit>" : "<init>", type, null, file, 0, 0);
                            map.put(type, u);
                        }
                        // each initializer gets its own extent unit sharing the same edges (registered for lines)
                        long s = pos.getStartPosition(cu, member), e = pos.getEndPosition(cu, member);
                        Unit extent = u;
                        if (isTarget) {
                            extent = new Unit(u.label, u.methodName, type, null, file,
                                    (int) lm.getLineNumber(s), (int) lm.getLineNumber(e));
                            targetUnits.add(extent);
                            aliases.put(extent, u);
                        }
                        Deque<Unit> inner = new ArrayDeque<Unit>();
                        inner.push(u);
                        scan(member, inner);
                    }
                    // methods and nested classes
                    for (Tree member : node.getMembers()) {
                        if (member instanceof MethodTree || member instanceof ClassTree) {
                            scan(member, stack);
                        }
                    }
                    return null;
                }

                @Override
                public Void visitMethod(MethodTree node, Deque<Unit> stack) {
                    ExecutableElement m = (ExecutableElement) trees.getElement(getCurrentPath());
                    if (m == null || node.getBody() == null) return null;
                    TypeElement owner = (TypeElement) m.getEnclosingElement();
                    long s = pos.getStartPosition(cu, node), e = pos.getEndPosition(cu, node);
                    Unit u = new Unit(label(m), m.getKind() == ElementKind.CONSTRUCTOR ? "<init>"
                            : m.getSimpleName().toString(), owner, m, file,
                            (int) lm.getLineNumber(s), (int) lm.getLineNumber(e));
                    byMethod.put(m, u);
                    if (isTarget) targetUnits.add(u);
                    final TreePath methodPath = getCurrentPath();
                    stack.push(u);
                    scan(node.getBody(), stack);
                    stack.pop();
                    return null;
                }

                @Override
                public Void visitMethodInvocation(MethodInvocationTree node, Deque<Unit> stack) {
                    Element el = trees.getElement(getCurrentPath());
                    if (el instanceof ExecutableElement && !stack.isEmpty()) {
                        ExecutableElement callee = (ExecutableElement) el;
                        Unit u = stack.peek();
                        ExpressionTree sel = node.getMethodSelect();
                        String name = sel instanceof IdentifierTree ? ((IdentifierTree) sel).getName().toString()
                                : sel instanceof MemberSelectTree ? ((MemberSelectTree) sel).getIdentifier().toString() : "";
                        boolean viaSuper = sel instanceof MemberSelectTree
                                && ((MemberSelectTree) sel).getExpression().toString().equals("super");
                        if (callee.getKind() == ElementKind.CONSTRUCTOR) {
                            u.ctorCalls.add(callee);           // this(...) / super(...)
                            if (name.equals("super") || name.equals("this")) u.explicitCtorCall = true;
                        } else {
                            boolean isVirtual = !viaSuper && !callee.getModifiers().contains(Modifier.STATIC)
                                    && !callee.getModifiers().contains(Modifier.PRIVATE)
                                    && !callee.getModifiers().contains(Modifier.FINAL);
                            u.calls.add(new Call(callee, isVirtual));
                        }
                    }
                    return super.visitMethodInvocation(node, stack);
                }

                @Override
                public Void visitNewClass(NewClassTree node, Deque<Unit> stack) {
                    final TreePath newPath = getCurrentPath();
                    if (!stack.isEmpty()) {
                        Unit u = stack.peek();
                        Element ctor = trees.getElement(newPath);
                        if (node.getClassBody() != null) {
                            TypeElement anon = (TypeElement) trees.getElement(
                                    new TreePath(newPath, node.getClassBody()));
                            if (anon != null) u.news.add(anon);
                        } else if (ctor instanceof ExecutableElement) {
                            u.news.add((TypeElement) ctor.getEnclosingElement());
                            u.ctorCalls.add((ExecutableElement) ctor);
                        }
                    }
                    // anonymous class bodies: scan args here, the body as its own class
                    for (ExpressionTree a : node.getArguments()) scan(a, stack);
                    if (node.getEnclosingExpression() != null) scan(node.getEnclosingExpression(), stack);
                    if (node.getClassBody() != null) {
                        scan(node.getClassBody(), stack);
                    }
                    return null;
                }

                @Override
                public Void visitEnhancedForLoop(EnhancedForLoopTree node, Deque<Unit> stack) {
                    // for (x : expr) calls expr.iterator(), hasNext(), next()
                    TypeMirror t = trees.getTypeMirror(new TreePath(getCurrentPath(), node.getExpression()));
                    if (t != null && t.getKind() == TypeKind.DECLARED && !stack.isEmpty()) {
                        Unit u = stack.peek();
                        TypeElement iterable = elements.getTypeElement("java.lang.Iterable");
                        TypeElement iterator = elements.getTypeElement("java.util.Iterator");
                        for (ExecutableElement m : ElementFilter.methodsIn(iterable.getEnclosedElements())) {
                            if (m.getSimpleName().contentEquals("iterator")) u.calls.add(new Call(m, true));
                        }
                        for (ExecutableElement m : ElementFilter.methodsIn(iterator.getEnclosedElements())) {
                            String n = m.getSimpleName().toString();
                            if (n.equals("hasNext") || n.equals("next")) u.calls.add(new Call(m, true));
                        }
                    }
                    return super.visitEnhancedForLoop(node, stack);
                }
            }.scan(new TreePath(cu), new ArrayDeque<Unit>());
        }

        /** extent units of initializers -> the shared per-class initializer unit */
        final Map<Unit, Unit> aliases = new HashMap<Unit, Unit>();

        // ── solving ──

        private final Set<TypeElement> instantiated = new LinkedHashSet<TypeElement>();
        private final List<Call> virtualCalls = new ArrayList<Call>();
        private final Deque<Unit> work = new ArrayDeque<Unit>();
        private final Set<TypeElement> callbackDone = new HashSet<TypeElement>();

        private void solve() {
            List<Unit> roots = new ArrayList<Unit>();
            for (Unit u : byMethod.values()) if (u.file.equals(adapter)) roots.add(u);
            for (Map.Entry<TypeElement, Unit> e : staticInit.entrySet()) {
                if (pathOf(e.getKey()).equals(adapter)) roots.add(e.getValue());
            }
            for (Unit r : roots) reach(r);
            boolean changed = true;
            while (changed) {
                changed = false;
                while (!work.isEmpty()) {
                    Unit u = work.pop();
                    for (TypeElement t : u.news) instantiate(t);
                    for (ExecutableElement c : u.ctorCalls) {
                        Unit cu = byMethod.get(c);
                        if (cu != null) reach(cu); else implicitCtor((TypeElement) c.getEnclosingElement());
                    }
                    if (u.element != null && u.element.getKind() == ElementKind.CONSTRUCTOR) {
                        Unit init = instanceInit.get(u.owner);
                        if (init != null) reach(init);
                        if (!u.explicitCtorCall) superNoArg(u.owner);
                    }
                    Unit clinit = staticInit.get(u.owner);
                    if (clinit != null) reach(clinit);
                    for (Call c : u.calls) {
                        if (c.virtual) virtualCalls.add(c);
                        for (Unit t : dispatch(c)) reach(t);
                    }
                }
                // re-dispatch every virtual call seen so far against the classes instantiated so far
                for (Call c : new ArrayList<Call>(virtualCalls)) {
                    for (Unit t : dispatch(c)) if (reach(t)) changed = true;
                }
                if (!work.isEmpty()) changed = true;
            }
            // direct: what the adapter's own calls dispatch to, once every class is known
            for (Unit r : roots) {
                for (Call c : r.calls) for (Unit t : dispatch(c)) direct.add(t);
                for (ExecutableElement c : r.ctorCalls) { Unit t = byMethod.get(c); if (t != null) direct.add(t); }
            }
            // the initializer extents share the reachability of their class's initializer unit
            for (Map.Entry<Unit, Unit> e : aliases.entrySet()) {
                if (reachable.contains(e.getValue())) reachable.add(e.getKey());
            }
        }

        private boolean reach(Unit u) {
            if (u == null || !reachable.add(u)) return false;
            work.push(u);
            return true;
        }

        private void instantiate(TypeElement t) {
            if (!instantiated.add(t)) return;
            implicitCtor(t);
        }

        /** a class whose constructor is not in the sources (default ctor, anonymous): its initializers and super() */
        private void implicitCtor(TypeElement t) {
            if (t == null) return;
            Unit init = instanceInit.get(t);
            if (init != null) reach(init);
            Unit clinit = staticInit.get(t);
            if (clinit != null) reach(clinit);
            boolean hasSourceCtor = false;
            for (ExecutableElement c : ElementFilter.constructorsIn(t.getEnclosedElements())) {
                if (byMethod.containsKey(c)) hasSourceCtor = true;
            }
            if (!hasSourceCtor) {
                // anonymous classes pass their arguments to the super constructor; take every super ctor
                TypeElement sup = superOf(t);
                if (sup != null) {
                    boolean anonymous = t.getNestingKind() == javax.lang.model.element.NestingKind.ANONYMOUS;
                    for (ExecutableElement c : ElementFilter.constructorsIn(sup.getEnclosedElements())) {
                        if (anonymous || c.getParameters().isEmpty()) {
                            Unit cu = byMethod.get(c);
                            if (cu != null) reach(cu); else if (c.getParameters().isEmpty()) implicitCtor(sup);
                        }
                    }
                }
            }
        }

        private void superNoArg(TypeElement t) {
            TypeElement sup = superOf(t);
            if (sup == null) return;
            for (ExecutableElement c : ElementFilter.constructorsIn(sup.getEnclosedElements())) {
                if (c.getParameters().isEmpty()) {
                    Unit cu = byMethod.get(c);
                    if (cu != null) reach(cu); else implicitCtor(sup);
                }
            }
        }

        private TypeElement superOf(TypeElement t) {
            TypeMirror s = t.getSuperclass();
            if (s.getKind() != TypeKind.DECLARED) return null;
            return (TypeElement) ((DeclaredType) s).asElement();
        }

        private List<Unit> dispatch(Call c) {
            List<Unit> out = new ArrayList<Unit>();
            if (!c.virtual) {
                Unit u = byMethod.get(c.callee);
                if (u != null) out.add(u);
                return out;
            }
            TypeElement owner = (TypeElement) c.callee.getEnclosingElement();
            for (TypeElement cls : new ArrayList<TypeElement>(instantiated)) {
                if (!types.isSubtype(types.erasure(cls.asType()), types.erasure(owner.asType()))) continue;
                boolean found = false;
                for (TypeElement s = cls; s != null; s = superOf(s)) {
                    if (!sourceTypes.contains(s)) {
                        // the implementation is in the JDK: it may call back the class's overrides
                        if (!s.getQualifiedName().contentEquals("java.lang.Object")) callbacks(cls, out);
                        break;
                    }
                    for (ExecutableElement m : ElementFilter.methodsIn(s.getEnclosedElements())) {
                        if (m.getSimpleName().equals(c.callee.getSimpleName())
                                && (m.equals(c.callee) || elements.overrides(m, c.callee, cls))) {
                            Unit u = byMethod.get(m);
                            if (u != null) out.add(u);
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
            }
            // a call on an interface or abstract type with no instantiated implementer still names the method
            Unit self = byMethod.get(c.callee);
            if (self != null && out.isEmpty()) out.add(self);
            return out;
        }

        /** every method of cls's source chain that overrides a method of a JDK supertype */
        private void callbacks(TypeElement cls, List<Unit> out) {
            if (!callbackDone.add(cls)) return;
            for (TypeElement s = cls; s != null && sourceTypes.contains(s); s = superOf(s)) {
                for (ExecutableElement m : ElementFilter.methodsIn(s.getEnclosedElements())) {
                    if (overridesJdk(m, cls)) {
                        Unit u = byMethod.get(m);
                        if (u != null) out.add(u);
                    }
                }
            }
        }

        private boolean overridesJdk(ExecutableElement m, TypeElement cls) {
            Deque<TypeMirror> todo = new ArrayDeque<TypeMirror>(types.directSupertypes(cls.asType()));
            Set<String> seen = new HashSet<String>();
            while (!todo.isEmpty()) {
                TypeMirror t = todo.pop();
                if (t.getKind() != TypeKind.DECLARED) continue;
                TypeElement te = (TypeElement) ((DeclaredType) t).asElement();
                if (!seen.add(te.getQualifiedName().toString())) continue;
                if (!sourceTypes.contains(te) && !te.getQualifiedName().contentEquals("java.lang.Object")) {
                    for (ExecutableElement jm : ElementFilter.methodsIn(te.getEnclosedElements())) {
                        if (jm.getSimpleName().equals(m.getSimpleName()) && elements.overrides(m, jm, cls)) return true;
                    }
                }
                todo.addAll(types.directSupertypes(t));
            }
            return false;
        }

        private Path pathOf(TypeElement t) {
            TreePath p = trees.getPath(t);
            return p == null ? Paths.get("") : Paths.get(p.getCompilationUnit().getSourceFile().toUri())
                    .toAbsolutePath().normalize();
        }

        private String simpleLabel(TypeElement t) {
            if (t.getNestingKind() == javax.lang.model.element.NestingKind.ANONYMOUS) {
                Element enc = t.getEnclosingElement();
                String where = enc instanceof ExecutableElement ? label((ExecutableElement) enc)
                        : enc instanceof TypeElement ? simpleLabel((TypeElement) enc) : "?";
                return where + ".<anonymous " + types.erasure(superOrInterface(t)) + ">";
            }
            Element enc = t.getEnclosingElement();
            String name = t.getSimpleName().toString();
            return enc instanceof TypeElement ? simpleLabel((TypeElement) enc) + "." + name : name;
        }

        private TypeMirror superOrInterface(TypeElement t) {
            if (!t.getInterfaces().isEmpty()) return t.getInterfaces().get(0);
            return t.getSuperclass();
        }

        private String label(ExecutableElement m) {
            StringBuilder sb = new StringBuilder(simpleLabel((TypeElement) m.getEnclosingElement())).append('.');
            sb.append(m.getKind() == ElementKind.CONSTRUCTOR ? "<init>" : m.getSimpleName().toString()).append('(');
            for (int i = 0; i < m.getParameters().size(); i++) {
                if (i > 0) sb.append(',');
                String t = types.erasure(m.getParameters().get(i).asType()).toString();
                sb.append(t.substring(t.lastIndexOf('.') + 1));
            }
            return sb.append(')').toString();
        }
    }

    private D4jMutantGen() {}
}

package in.ac.iiitb.plproject.spf;

import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.symbolic.Solution;
import in.ac.iiitb.plproject.symbolic.Stmt;
import in.ac.iiitb.plproject.symbolic.SymbolicDriverPrinter;
import in.ac.iiitb.plproject.symbolic.SymexEngine;
import in.ac.iiitb.plproject.symbolic.TestGen;
import in.ac.iiitb.plproject.symbolic.TypeSupport;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * symex's engine: Symbolic PathFinder, run as a separate process once per
 * genCTC round. The round's t' is printed as a Java program up to its first
 * not-ready call ({@link SymbolicDriverPrinter}), compiled together with the
 * library — which is on SPF's classpath because ready calls run it — and
 * executed by SPF. Its listener solves the path condition of the first
 * completed path with z3; the {@link in.ac.iiitb.plproject.spf.listener.BlackBoxGuard}
 * fails the run if a symbolic value ever reaches the library.
 */
public class SpfSymexEngine implements SymexEngine {

    private final TestPlan plan;
    private final List<String> librarySources;
    private final File workDir;
    private final SpfConfig config;
    private int round = 0;

    public SpfSymexEngine(TestPlan plan, List<String> librarySources, File workDir, SpfConfig config) {
        this.plan = plan;
        this.librarySources = librarySources;
        this.workDir = workDir;
        this.config = config;
    }

    @Override
    public Solution solve(TestGen.Prefix prefix) {
        SymbolicDriverPrinter.Printed printed = SymbolicDriverPrinter.print(plan, prefix, round);
        File dir = new File(workDir, "round" + round);
        round++;
        RawRun run;
        try {
            run = runDriver(printed.source, librarySources, dir, config);
        } catch (IOException | InterruptedException e) {
            return Solution.failure(Solution.Kind.SOLVER_ERROR, "could not run SPF: " + e, 0, printed.source);
        }
        return interpret(prefix, printed, run);
    }

    /** One SPF run of one driver, as raw events. */
    public static final class RawRun {
        public String compileError;
        public List<PathVector> events = new ArrayList<PathVector>();
        public boolean finished;
        /** The black-box guard's report, or null when it did not fire. */
        public String blackBox;
        public String console = "";
        public long millis;
    }

    private Solution interpret(TestGen.Prefix prefix, SymbolicDriverPrinter.Printed printed, RawRun run) {
        String program = printed.source;
        if (run.compileError != null) {
            return Solution.failure(Solution.Kind.COMPILE_ERROR, firstLine(run.compileError), run.millis, program);
        }
        if (run.blackBox != null) {
            return Solution.failure(Solution.Kind.BLACK_BOX_VIOLATION, run.blackBox, run.millis, program);
        }
        for (PathVector p : run.events) {
            if (p.getEvent() == PathVector.Event.PATH && p.isSat()) {
                List<String> values = new ArrayList<String>();
                for (Stmt.Input in : prefix.inputs) {
                    String value = valueOf(in, p);
                    if (value == null) {
                        return Solution.failure(Solution.Kind.SOLVER_ERROR,
                                "the solved path has no pick for " + in.var, run.millis, program);
                    }
                    values.add(value);
                }
                return Solution.values(values, run.millis, program);
            }
        }

        PathVector finishedEvent = null;
        boolean timeLimit = !run.finished;
        PathVector firstError = null;
        for (PathVector p : run.events) {
            if (p.getEvent() == PathVector.Event.SEARCH_FINISHED) finishedEvent = p;
            if (p.getEvent() == PathVector.Event.SEARCH_CONSTRAINT_HIT
                    && p.getDetail() != null && p.getDetail().contains("time")) timeLimit = true;
            if (p.getEvent() == PathVector.Event.ERROR && firstError == null) firstError = p;
        }
        if (timeLimit) {
            return Solution.failure(Solution.Kind.TIMEOUT, "SPF time limit (" + config.getTimeoutSeconds()
                    + "s) reached before any path completed", run.millis, program);
        }
        if (firstError != null) {
            Stmt at = printed.at(firstError.getLine());
            String where = at instanceof Stmt.Assign && ((Stmt.Assign) at).isApiCall()
                    ? "call " + ((Stmt.Assign) at).block.getIndex() + " ("
                      + ((Stmt.Assign) at).block.getOperationName() + "): "
                    : "driver line " + firstError.getLine() + ": ";
            return Solution.failure(Solution.Kind.LIBRARY_EXCEPTION, where + exceptionOf(firstError.getDetail()),
                    run.millis, program);
        }
        if (finishedEvent == null || solverFailed(run.console)) {
            return Solution.failure(Solution.Kind.SOLVER_ERROR, "SPF did not finish its search normally: "
                    + run.console.trim(), run.millis, program);
        }
        Stmt at = printed.at(finishedEvent.getDeepestPruneLine());
        if (!(at instanceof Stmt.Assume)) {
            return Solution.failure(Solution.Kind.SOLVER_ERROR, "no path completed and the pruning point (line "
                    + finishedEvent.getDeepestPruneLine() + ") is not an assume", run.millis, program);
        }
        return Solution.noSolution((Stmt.Assume) at, run.millis, program);
    }

    /**
     * The value of one input on the solved path: a pooled input's pick, or the
     * solver's value for a symbolic one. An input the path condition never
     * mentions gets a small, distinct, non-zero literal.
     */
    private static String valueOf(Stmt.Input in, PathVector path) {
        if (in.slot.isPooled()) {
            String raw = path.getPicks().get(in.var);
            return raw == null ? null : TypeSupport.literal(in.type, raw);
        }
        String raw = path.getVars().get(in.var);
        if (raw == null) return TypeSupport.unconstrainedLiteral(in.type, in.ordinal);
        return TypeSupport.solverLiteral(in.type, raw);
    }

    /** {@code java.lang.NullPointerException: ...} from a JPF error description. */
    private static String exceptionOf(String detail) {
        if (detail == null) return "";
        for (String line : detail.split("\n")) {
            String t = line.trim();
            if (t.matches("[A-Za-z_$][\\w$.]*(Exception|Error)\\b.*")) return t;
        }
        return firstLine(detail);
    }

    /** z3/SPF failures show up only on the console; these are the markers seen in practice. */
    private static boolean solverFailed(String console) {
        return console.contains("Error Z3") || console.contains("Z3Exception")
                || console.contains("java.lang.UnsatisfiedLinkError")
                || console.contains("gov.nasa.jpf.JPFException");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // running one driver
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Compiles {@code driverSource} together with the library sources and runs
     * SPF on it with the path listener and the black-box guard.
     */
    public static RawRun runDriver(String driverSource, List<String> librarySources, File dir, SpfConfig config)
            throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        RawRun run = new RawRun();
        File src = new File(dir, "src/" + TestPlan.GENERATED_PACKAGE.replace('.', '/'));
        Files.createDirectories(src.toPath());
        File driver = new File(src, TestPlan.SYMBOLIC_CLASS + ".java");
        Files.write(driver.toPath(), driverSource.getBytes(StandardCharsets.UTF_8));

        File classesDir = new File(dir, "classes");
        Files.createDirectories(classesDir.toPath());
        List<String> sources = new ArrayList<String>();
        sources.add(driver.getAbsolutePath());
        List<String> libraryClasses = new ArrayList<String>();
        for (String s : librarySources) {
            sources.add(new File(s).getAbsolutePath());
            String file = Paths.get(s).getFileName().toString();
            libraryClasses.add(TestPlan.GENERATED_PACKAGE + "." + file.substring(0, file.length() - ".java".length()));
        }
        run.compileError = compile(sources, classesDir);
        if (run.compileError != null) {
            run.millis = System.currentTimeMillis() - start;
            return run;
        }

        File outputJsonl = new File(dir, "paths.jsonl");
        File blackBox = new File(dir, "blackbox.txt");
        Files.deleteIfExists(outputJsonl.toPath());
        Files.deleteIfExists(blackBox.toPath());
        File jpfConfig = new File(dir, "run.jpf");
        writeJpfConfig(jpfConfig, classesDir, outputJsonl, blackBox, libraryClasses, config);

        run.finished = runSpfProcess(jpfConfig, config.getTimeoutSeconds());
        run.events = PathVectorReader.read(outputJsonl.toPath());
        run.console = consoleTail(new File(dir, "spf-console.log"));
        if (blackBox.isFile()) {
            run.blackBox = new String(Files.readAllBytes(blackBox.toPath()), StandardCharsets.UTF_8).trim();
        }
        run.millis = System.currentTimeMillis() - start;
        return run;
    }

    private static String consoleTail(File log) {
        try {
            if (!log.isFile()) return "";
            List<String> lines = Files.readAllLines(log.toPath(), StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            for (int i = Math.max(0, lines.size() - 15); i < lines.size(); i++) {
                sb.append(lines.get(i)).append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private static String compile(List<String> sources, File classesDir) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> args = new ArrayList<String>();
        args.add("-nowarn");
        args.add("-cp"); args.add(SpfHome.resolveDriverCompileClasspath());
        args.add("-g");
        args.add("-d"); args.add(classesDir.getAbsolutePath());
        args.addAll(sources);

        java.io.ByteArrayOutputStream errOut = new java.io.ByteArrayOutputStream();
        int rc = compiler.run(null, null, errOut, args.toArray(new String[0]));
        return rc == 0 ? null : errOut.toString(StandardCharsets.UTF_8.name());
    }

    private static void writeJpfConfig(File jpfConfig, File classesDir, File outputJsonl, File blackBox,
                                       List<String> libraryClasses, SpfConfig config) throws IOException {
        File jpfCore = SpfHome.resolveJpfCoreCheckoutDir();
        File jpfSymbc = SpfHome.resolveJpfSymbcCheckoutDir();
        // the listeners ship in this same classes dir / jar; naming their classes here would load
        // gov.nasa.jpf.* into the generator JVM, which does not have it
        String listenerClasspath = classpathEntryOf(SpfSymexEngine.class);

        // jpf-core's own jpf.properties: native_classpath is the HOST-side engine
        // (build/jpf.jar + build/jpf-annotations.jar), not jpf-classes.jar, which is
        // the target-side jar the driver compiles against (see SpfHome).
        StringBuilder nativeCp = new StringBuilder();
        nativeCp.append(new File(jpfCore, "build/jpf.jar").getAbsolutePath());
        nativeCp.append(",").append(new File(jpfCore, "build/jpf-annotations.jar").getAbsolutePath());
        nativeCp.append(",").append(new File(jpfSymbc, "build/jpf-symbc.jar").getAbsolutePath());
        File libDir = new File(jpfSymbc, "lib");
        File[] libJars = libDir.listFiles((d, name) -> name.endsWith(".jar"));
        if (libJars != null) {
            java.util.Arrays.sort(libJars);
            for (File jar : libJars) nativeCp.append(",").append(jar.getAbsolutePath());
        }
        nativeCp.append(",").append(listenerClasspath);

        // The target-side classpath: the compiled driver and library, plus the
        // jpf-classes.jar/jpf-symbc-classes.jar the driver was compiled against.
        StringBuilder sb = new StringBuilder();
        sb.append("target = ").append(TestPlan.GENERATED_PACKAGE).append('.').append(TestPlan.SYMBOLIC_CLASS).append('\n');
        sb.append("classpath = ").append(classesDir.getAbsolutePath()).append(",")
          .append(SpfHome.resolveDriverCompileClasspath().replace(File.pathSeparator, ",")).append('\n');
        sb.append("native_classpath = ").append(nativeCp).append('\n');
        sb.append("vm.insn_factory.class = gov.nasa.jpf.symbc.SymbolicInstructionFactory\n");
        sb.append("symbolic.dp = ").append(config.getSolver()).append('\n');
        sb.append("symbolic.min_int = ").append(config.getMinInt()).append('\n');
        sb.append("symbolic.max_int = ").append(config.getMaxInt()).append('\n');
        sb.append("search.depth_limit = ").append(config.getDepthLimit()).append('\n');
        sb.append("search.time_limit = ").append(config.getTimeoutSeconds()).append('\n');
        sb.append("search.multiple_errors = true\n");
        sb.append("listener = in.ac.iiitb.plproject.spf.listener.PathVectorListener,")
          .append("in.ac.iiitb.plproject.spf.listener.BlackBoxGuard\n");
        sb.append("genatc.output = ").append(outputJsonl.getAbsolutePath()).append('\n');
        sb.append("genatc.blackbox_output = ").append(blackBox.getAbsolutePath()).append('\n');
        sb.append("genatc.library_classes = ").append(String.join(",", libraryClasses)).append('\n');
        Files.write(jpfConfig.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String classpathEntryOf(Class<?> cls) {
        try {
            return new File(cls.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();
        } catch (Exception e) {
            throw new IllegalStateException("cannot resolve classpath entry for " + cls, e);
        }
    }

    /** true if the process finished on its own before the timeout; false if it was killed. */
    private static boolean runSpfProcess(File jpfConfig, int timeoutSeconds) throws IOException, InterruptedException {
        File runJpfJar = SpfHome.resolveRunJpfJar();
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        ProcessBuilder pb = new ProcessBuilder(javaBin, "-Xmx" + SpfConfig.SPF_HEAP, "-XX:+UseSerialGC",
                "-XX:TieredStopAtLevel=1", "-jar", runJpfJar.getAbsolutePath(), jpfConfig.getAbsolutePath());
        pb.redirectErrorStream(true);
        pb.redirectOutput(new File(jpfConfig.getParentFile(), "spf-console.log"));
        Process process = pb.start();
        boolean finished = process.waitFor(timeoutSeconds + 30L, TimeUnit.SECONDS);
        if (!finished) process.destroyForcibly();
        return finished;
    }

    private static String firstLine(String text) {
        if (text == null) return "";
        String t = text.trim();
        int newline = t.indexOf('\n');
        return newline < 0 ? t : t.substring(0, newline);
    }
}

package in.ac.iiitb.plproject.spf;

import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.symbolic.SymbolicDriverPrinter;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Step 4: runs Symbolic PathFinder, in a SEPARATE process from the generator
 * (rule 6), on the driver {@link SymbolicDriverPrinter} printed from the
 * contract. The library is not compiled, copied or put on any classpath here:
 * SPF sees the contract and nothing else, so its answer depends only on
 * (spec, sequence).
 *
 * <p>One run per sequence. The listener stops at the first solved path; when
 * no path completes it reports the highest driver line it pruned at, which
 * the printer's line map turns into the call and clause that cannot hold.
 */
public class SpfRunner {

    public SpfRunResult run(TestPlan plan, File workDir, SpfConfig config)
            throws IOException, InterruptedException {
        long start = System.currentTimeMillis();

        File genDir = new File(workDir, TestPlan.GENERATED_PACKAGE.replace('.', '/'));
        Files.createDirectories(genDir.toPath());
        SymbolicDriverPrinter.Printed printed = SymbolicDriverPrinter.printWithLines(plan);
        Files.write(new File(genDir, TestPlan.SYMBOLIC_CLASS + ".java").toPath(),
                printed.source.getBytes(StandardCharsets.UTF_8));

        File classesDir = new File(workDir, "classes");
        Files.createDirectories(classesDir.toPath());
        String compileError = compile(new File(genDir, TestPlan.SYMBOLIC_CLASS + ".java"), classesDir);
        if (compileError != null) {
            return new SpfRunResult(SpfRunResult.Status.COMPILE_ERROR, null, null, -1, null, compileError,
                    System.currentTimeMillis() - start);
        }

        File outputJsonl = new File(workDir, "paths.jsonl");
        Files.deleteIfExists(outputJsonl.toPath());
        File jpfConfig = new File(workDir, "run.jpf");
        writeJpfConfig(jpfConfig, classesDir, outputJsonl, config);

        boolean finished = runSpfProcess(jpfConfig, config.getTimeoutSeconds());
        List<PathVector> paths = PathVectorReader.read(outputJsonl.toPath());
        long elapsed = System.currentTimeMillis() - start;
        String console = consoleTail(new File(workDir, "spf-console.log"));

        for (PathVector p : paths) {
            if (p.getEvent() == PathVector.Event.PATH && p.isSat()) {
                return new SpfRunResult(SpfRunResult.Status.SOLVED, ValueExtractor.extract(plan, p), paths,
                        -1, null, null, elapsed);
            }
        }

        PathVector finishedEvent = null;
        boolean timeLimit = !finished;
        String firstError = null;
        for (PathVector p : paths) {
            if (p.getEvent() == PathVector.Event.SEARCH_FINISHED) finishedEvent = p;
            if (p.getEvent() == PathVector.Event.SEARCH_CONSTRAINT_HIT
                    && p.getDetail() != null && p.getDetail().contains("time")) timeLimit = true;
            if (p.getEvent() == PathVector.Event.ERROR && firstError == null) firstError = p.getDetail();
        }
        if (timeLimit) {
            return new SpfRunResult(SpfRunResult.Status.TIMEOUT, null, paths, -1, null,
                    "SPF time limit (" + config.getTimeoutSeconds() + "s) reached before any path completed",
                    elapsed);
        }
        if (finishedEvent == null || solverFailed(console)) {
            return new SpfRunResult(SpfRunResult.Status.SOLVER_ERROR, null, paths, -1, null,
                    "SPF did not finish its search normally: " + console, elapsed);
        }
        if (firstError != null) {
            return new SpfRunResult(SpfRunResult.Status.SOLVER_ERROR, null, paths, -1, null,
                    "SPF reported a property violation in the driver: " + firstError, elapsed);
        }

        SymbolicDriverPrinter.LineMap.Entry at = printed.lines.at(finishedEvent.getDeepestPruneLine());
        if (at == null) {
            return new SpfRunResult(SpfRunResult.Status.SOLVER_ERROR, null, paths, -1, null,
                    "no path completed and the pruning point (line " + finishedEvent.getDeepestPruneLine()
                    + ") is not an assumption of the driver", elapsed);
        }
        SpfRunResult.Status status = "ensures".equals(at.kind) ? SpfRunResult.Status.UNSAT_POSTCONDITION
                                                               : SpfRunResult.Status.INCORRECT_SEQUENCE;
        return new SpfRunResult(status, null, paths, at.callIndex, at.clauseText, at.kind, elapsed);
    }

    /** z3/SPF failures show up only on the console; these are the markers seen in practice. */
    private static boolean solverFailed(String console) {
        return console.contains("Error Z3") || console.contains("Z3Exception")
                || console.contains("java.lang.UnsatisfiedLinkError")
                || console.contains("gov.nasa.jpf.JPFException");
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

    private String compile(File driver, File classesDir) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> args = new ArrayList<String>();
        args.add("-cp"); args.add(SpfHome.resolveDriverCompileClasspath());
        args.add("-g");
        args.add("-d"); args.add(classesDir.getAbsolutePath());
        args.add(driver.getAbsolutePath());

        java.io.ByteArrayOutputStream errOut = new java.io.ByteArrayOutputStream();
        int rc = compiler.run(null, null, errOut, args.toArray(new String[0]));
        return rc == 0 ? null : errOut.toString(StandardCharsets.UTF_8.name());
    }

    private void writeJpfConfig(File jpfConfig, File classesDir, File outputJsonl, SpfConfig config)
            throws IOException {
        File jpfCore = SpfHome.resolveJpfCoreCheckoutDir();
        File jpfSymbc = SpfHome.resolveJpfSymbcCheckoutDir();
        // the listener ships in this same classes dir / jar; naming its class here would load
        // gov.nasa.jpf.* into the generator JVM, which does not have it
        String listenerClasspath = classpathEntryOf(SpfRunner.class);

        // Mirrors jpf-core's own jpf.properties: jpf-core.native_classpath is
        // build/jpf.jar + build/jpf-annotations.jar (the HOST-side engine), not
        // jpf-classes.jar (that's the target/SUT-side jar the DRIVER compiles
        // against instead — see SpfHome's class comment).
        StringBuilder nativeCp = new StringBuilder();
        nativeCp.append(new File(jpfCore, "build/jpf.jar").getAbsolutePath());
        nativeCp.append(",").append(new File(jpfCore, "build/jpf-annotations.jar").getAbsolutePath());
        nativeCp.append(",").append(new File(jpfSymbc, "build/jpf-symbc.jar").getAbsolutePath());
        File libDir = new File(jpfSymbc, "lib");
        File[] libJars = libDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if (libJars != null) {
            java.util.Arrays.sort(libJars);
            for (File jar : libJars) nativeCp.append(",").append(jar.getAbsolutePath());
        }
        nativeCp.append(",").append(listenerClasspath);

        // The modeled/target-side classpath: the compiled driver ONLY, plus the
        // jpf-classes.jar/jpf-symbc-classes.jar it was compiled against. No library.
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
        sb.append("listener = in.ac.iiitb.plproject.spf.listener.PathVectorListener\n");
        sb.append("genatc.output = ").append(outputJsonl.getAbsolutePath()).append('\n');
        Files.write(jpfConfig.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    /** The classpath SPF runs the driver with, for tests that check the library is not on it. */
    public static String modeledClasspath(File runJpf) throws IOException {
        for (String line : Files.readAllLines(runJpf.toPath(), StandardCharsets.UTF_8)) {
            if (line.startsWith("classpath = ")) return line.substring("classpath = ".length());
        }
        return "";
    }

    private static String classpathEntryOf(Class<?> cls) {
        try {
            return new File(cls.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .getAbsolutePath();
        } catch (Exception e) {
            throw new IllegalStateException("cannot resolve classpath entry for " + cls, e);
        }
    }

    /** true if the process finished on its own before the timeout; false if it was killed. */
    private boolean runSpfProcess(File jpfConfig, int timeoutSeconds) throws IOException, InterruptedException {
        File runJpfJar = SpfHome.resolveRunJpfJar();
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        ProcessBuilder pb = new ProcessBuilder(javaBin, "-Xmx" + SpfConfig.SPF_HEAP, "-XX:+UseSerialGC",
                "-jar", runJpfJar.getAbsolutePath(), jpfConfig.getAbsolutePath());
        pb.redirectErrorStream(true);
        pb.redirectOutput(new File(jpfConfig.getParentFile(), "spf-console.log"));
        Process process = pb.start();
        boolean finished = process.waitFor(timeoutSeconds + 30L, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
        }
        return finished;
    }
}

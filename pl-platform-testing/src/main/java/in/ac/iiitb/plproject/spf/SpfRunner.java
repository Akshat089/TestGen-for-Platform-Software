package in.ac.iiitb.plproject.spf;

import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.spf.listener.PathVectorListener;
import in.ac.iiitb.plproject.symbolic.SymbolicDriverPrinter;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Step 4: runs Symbolic PathFinder, in a SEPARATE process from the generator
 * (rule 6), against the driver {@link SymbolicDriverPrinter} printed.
 *
 * <p>On a sequence SPF finds no feasible path through, re-runs against
 * successively shorter prefixes to find the first call whose precondition
 * could not be met (rule 10) — the listener itself only sees completed-path
 * end states, so distinguishing "nothing after call 2" from "nothing after
 * call 0" needs this kind of search, not a single run.
 */
public class SpfRunner {

    public SpfRunResult run(TestPlan plan, File workDir, List<String> librarySourcePaths,
                             SpfConfig config) throws IOException, InterruptedException {
        SingleRun full = runOnce(plan, workDir, librarySourcePaths, config, "full");
        if (full.compileFailed) {
            return new SpfRunResult(plan, full.paths, java.util.Collections.<InputVector>emptyList(),
                    full.depthLimitHit, full.timedOut, true, full.compileError, null, full.elapsedMillis);
        }

        List<InputVector> vectors = ValueExtractor.extractAll(plan, full.paths);
        if (!vectors.isEmpty() || full.depthLimitHit || full.timedOut) {
            return new SpfRunResult(plan, full.paths, vectors, full.depthLimitHit, full.timedOut,
                    false, null, null, full.elapsedMillis);
        }

        // Zero feasible paths, no cutoff: either genuinely infeasible (no call's
        // precondition can be met) or SPF cannot execute some call at all (every
        // explored end state is a property violation — see executionErrorDetail's
        // doc). Re-run shrinking prefixes to find the first call where either
        // happens; the two are reported distinctly (rule 10).
        Integer failingCallIndex = null;
        String executionErrorDetail = null;
        long extraMillis = 0;
        for (int k = 1; k <= plan.getCallBlocks().size(); k++) {
            TestPlan prefix = new TestPlan(plan.getLibraryKey(), plan.getSpecFile(), plan.getTestsFile(),
                    plan.isHasState(), plan.getStateVars(), plan.getCallBlocks().subList(0, k));
            SingleRun r = runOnce(prefix, new File(workDir, "prefix" + k), librarySourcePaths, config,
                    "prefix" + k);
            extraMillis += r.elapsedMillis;
            if (r.compileFailed) break;
            boolean feasible = !ValueExtractor.extractAll(prefix, r.paths).isEmpty();
            if (feasible) continue;
            if (r.depthLimitHit || r.timedOut) break;
            String firstError = firstErrorDetail(r.paths);
            if (firstError != null) {
                failingCallIndex = k - 1;
                executionErrorDetail = firstError;
            } else {
                failingCallIndex = k - 1;
            }
            break;
        }

        return new SpfRunResult(plan, full.paths, vectors, full.depthLimitHit, full.timedOut,
                false, null, failingCallIndex, executionErrorDetail, full.elapsedMillis + extraMillis);
    }

    private static String firstErrorDetail(List<PathVector> paths) {
        for (PathVector p : paths) {
            if (p.getEvent() == PathVector.Event.ERROR) return p.getDetail();
        }
        return null;
    }

    private static class SingleRun {
        List<PathVector> paths = new ArrayList<PathVector>();
        boolean depthLimitHit;
        boolean timedOut;
        boolean compileFailed;
        String compileError;
        long elapsedMillis;
    }

    private SingleRun runOnce(TestPlan plan, File workDir, List<String> librarySourcePaths,
                               SpfConfig config, String label) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        SingleRun run = new SingleRun();

        File genDir = new File(workDir, packagePath());
        Files.createDirectories(genDir.toPath());

        String driverSource = SymbolicDriverPrinter.print(plan);
        Files.write(new File(genDir, TestPlan.SYMBOLIC_CLASS + ".java").toPath(),
                driverSource.getBytes(StandardCharsets.UTF_8));

        for (String librarySourcePath : librarySourcePaths) {
            Path src = Paths.get(librarySourcePath);
            Files.copy(src, new File(genDir, src.getFileName().toString()).toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        File classesDir = new File(workDir, "classes");
        Files.createDirectories(classesDir.toPath());
        String compileError = compile(genDir, classesDir);
        if (compileError != null) {
            run.compileFailed = true;
            run.compileError = compileError;
            run.elapsedMillis = System.currentTimeMillis() - start;
            return run;
        }

        File outputJsonl = new File(workDir, "paths.jsonl");
        File jpfConfig = new File(workDir, "run.jpf");
        writeJpfConfig(jpfConfig, classesDir, outputJsonl, config);

        boolean timedOut = !runSpfProcess(jpfConfig, config.getTimeoutSeconds());
        run.timedOut = timedOut;
        run.paths = PathVectorReader.read(outputJsonl.toPath());
        for (PathVector v : run.paths) {
            if (v.getEvent() == PathVector.Event.SEARCH_CONSTRAINT_HIT && v.isDepthLimit()) {
                run.depthLimitHit = true;
            }
        }
        run.elapsedMillis = System.currentTimeMillis() - start;
        return run;
    }

    private String compile(File genDir, File classesDir) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> sources = new ArrayList<String>();
        for (File f : genDir.listFiles()) {
            if (f.getName().endsWith(".java")) sources.add(f.getAbsolutePath());
        }
        String cp = SpfHome.resolveDriverCompileClasspath();
        List<String> args = new ArrayList<String>();
        args.add("-cp"); args.add(cp);
        args.add("-d"); args.add(classesDir.getAbsolutePath());
        args.addAll(sources);

        java.io.ByteArrayOutputStream errOut = new java.io.ByteArrayOutputStream();
        int rc = compiler.run(null, null, errOut, args.toArray(new String[0]));
        return rc == 0 ? null : errOut.toString(StandardCharsets.UTF_8.name());
    }

    private void writeJpfConfig(File jpfConfig, File classesDir, File outputJsonl, SpfConfig config)
            throws IOException {
        File jpfCore = SpfHome.resolveJpfCoreCheckoutDir();
        File jpfSymbc = SpfHome.resolveJpfSymbcCheckoutDir();
        String listenerClasspath = classpathEntryOf(PathVectorListener.class);

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
            for (File jar : libJars) nativeCp.append(",").append(jar.getAbsolutePath());
        }
        nativeCp.append(",").append(listenerClasspath);

        // The modeled/target-side classpath: the compiled driver + Helper, plus the
        // same jpf-classes.jar/jpf-symbc-classes.jar they were compiled against, so
        // the modeled JVM can resolve Verify/Debug as ordinary classes.
        StringBuilder sb = new StringBuilder();
        sb.append("target = ").append(TestPlan.GENERATED_PACKAGE).append('.').append(TestPlan.SYMBOLIC_CLASS).append('\n');
        sb.append("classpath = ").append(classesDir.getAbsolutePath()).append(",")
          .append(SpfHome.resolveDriverCompileClasspath()).append('\n');
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
        ProcessBuilder pb = new ProcessBuilder(javaBin, "-jar", runJpfJar.getAbsolutePath(),
                jpfConfig.getAbsolutePath());
        pb.redirectErrorStream(true);
        pb.redirectOutput(new File(jpfConfig.getParentFile(), "spf-console.log"));
        Process process = pb.start();
        boolean finished = process.waitFor(timeoutSeconds + 30L, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
        }
        return finished;
    }

    private static String packagePath() {
        return TestPlan.GENERATED_PACKAGE.replace('.', '/');
    }
}

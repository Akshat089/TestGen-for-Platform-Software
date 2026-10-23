package in.ac.iiitb.plproject.mutation;

import in.ac.iiitb.plproject.atc.GenatcMain;
import in.ac.iiitb.plproject.atc.LibraryRegistry;
import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.verify.JunitJars;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * One library's generated tests, each generated ONCE and compiled ONCE.
 *
 * <p>genCTC runs the ready calls on the library, so a test is generated here
 * on the CORRECT library, compiled against it into {@link #testClasses()},
 * and that same compiled class is then run, unchanged, against the correct
 * library and every mutant. Nothing is regenerated per mutant and SPF never
 * runs per mutant.
 *
 * <p>Every cached test gets its own class name in the generated package, so
 * all of a library's tests sit in one directory and one child JVM can run
 * any number of them against one library build ({@link #runTests}).
 */
public class TestCache {

    /** One generated test, or why there is none. */
    public static class CachedTest {
        public final String name;
        public final String testsPath;
        public final String sequence;
        /** GENERATED, or the generator's status when no test was produced. */
        public final String status;
        public final String detail;
        /** Fully-qualified class name, or null when no test was produced. */
        public final String className;
        public final long spfMillis;
        /** genCTC rounds that turned inputs concrete. */
        public final int rounds;
        /** The value each input command got: {@code c0_elem -> "s1"}. */
        public final Map<String, String> inputs;

        CachedTest(String name, String testsPath, String sequence, String status, String detail,
                   String className, long spfMillis, int rounds, Map<String, String> inputs) {
            this.name = name;
            this.testsPath = testsPath;
            this.sequence = sequence;
            this.status = status;
            this.detail = detail;
            this.className = className;
            this.spfMillis = spfMillis;
            this.rounds = rounds;
            this.inputs = inputs;
        }

        public boolean hasTest() { return className != null; }
    }

    /** What one cached test did in one run. */
    public static class TestRun {
        public final String className;
        public final boolean passed;
        /** PRE, POST, EXCEPTION or TIMEOUT when it failed. */
        public final String kind;
        public final String message;

        TestRun(String className, boolean passed, String kind, String message) {
            this.className = className;
            this.passed = passed;
            this.kind = kind;
            this.message = message;
        }
    }

    private final LibraryRegistry.Entry entry;
    private final Path root;

    public TestCache(LibraryRegistry.Entry entry, Path root) throws IOException {
        this.entry = entry;
        this.root = root;
        MutationRunner.deleteRecursively(root);
        Files.createDirectories(testClasses());
        String error = compileLibrary(entry.librarySourcePaths, libraryClasses());
        if (error != null) throw new IllegalStateException("the correct library does not compile: " + error);
    }

    public Path libraryClasses() { return root.resolve("library-classes"); }
    public Path testClasses() { return root.resolve("test-classes"); }

    /** Generates the test for one sequence file and compiles it against the correct library. */
    public CachedTest generate(String name, String testsPath) throws Exception {
        String simpleName = "GeneratedATCsTest_" + name.replaceAll("[^A-Za-z0-9_]", "_");
        LibraryRegistry.Entry variant = LibraryRegistry.variantOf(entry, entry.librarySourcePaths, testsPath,
                root.resolve("gen").resolve(name).toString());
        GenatcMain.LibraryOutcome outcome = GenatcMain.generate(variant,
                new File(variant.outputDir), simpleName, false);
        String sequence = outcome.sequences.isEmpty() ? "?" : outcome.sequences.get(0).calls;
        int rounds = 0;
        Map<String, String> inputs = new LinkedHashMap<String, String>();
        for (GenatcMain.SequenceOutcome s : outcome.sequences) {
            rounds += s.rounds;
            inputs.putAll(s.inputs);
        }
        if (outcome.junitSource == null) {
            return new CachedTest(name, testsPath, sequence, outcome.status, outcome.detail, null,
                    outcome.spfMillis, rounds, inputs);
        }
        File source = new File(GenatcMain.generatedDir(new File(variant.outputDir)), simpleName + ".java");
        String error = javac(testClasses(), libraryClasses().toString() + File.pathSeparator + junitClasspath(),
                java.util.Collections.singletonList(source.getAbsolutePath()));
        if (error != null) {
            return new CachedTest(name, testsPath, sequence, "TEST_COMPILE_ERROR", error, null,
                    outcome.spfMillis, rounds, inputs);
        }
        return new CachedTest(name, testsPath, sequence, "GENERATED", "",
                TestPlan.GENERATED_PACKAGE + "." + simpleName, outcome.spfMillis, rounds, inputs);
    }

    /** Runs cached tests against the correct library, in one JVM. */
    public Map<String, TestRun> runOnCorrectLibrary(List<String> classNames) throws Exception {
        return runTests(libraryClasses(), classNames, false);
    }

    /**
     * Runs {@code classNames} in order, in ONE child JVM, against the library
     * build in {@code libraryClasses}. With {@code untilFail}, stops at the
     * first failing test. A run the watchdog has to kill counts as a TIMEOUT
     * for the first test that did not report.
     */
    public Map<String, TestRun> runTests(Path libraryClasses, List<String> classNames, boolean untilFail)
            throws Exception {
        Map<String, TestRun> out = new LinkedHashMap<String, TestRun>();
        if (classNames.isEmpty()) return out;
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        List<String> cmd = new ArrayList<String>();
        cmd.add(javaBin);
        cmd.add("-Xmx128m");
        cmd.add("-XX:+UseSerialGC");
        cmd.add("-XX:TieredStopAtLevel=1");
        cmd.add("-cp");
        cmd.add(libraryClasses + File.pathSeparator + testClasses() + File.pathSeparator + junitClasspath()
                + File.pathSeparator + ownClasspath());
        cmd.add(MutantTestMain.class.getName());
        if (untilFail) cmd.add("--until-fail");
        cmd.addAll(classNames);

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        List<String> lines = new ArrayList<String>();
        Thread reader = new Thread(() -> {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream(),
                    StandardCharsets.UTF_8))) {
                String line;
                while ((line = r.readLine()) != null) {
                    synchronized (lines) { lines.add(line); }
                }
            } catch (IOException ignored) {
                // the process was killed
            }
        });
        reader.start();
        long budget = 20L + 3L * classNames.size();
        boolean finished = process.waitFor(budget, TimeUnit.SECONDS);
        if (!finished) process.destroyForcibly();
        reader.join(5000);

        synchronized (lines) {
            for (String line : lines) {
                if (!line.startsWith("RESULT\t")) continue;
                String[] f = line.split("\t", 5);
                boolean pass = f.length >= 3 && f[2].equals("PASS");
                out.put(f[1], new TestRun(f[1], pass, pass ? null : (f.length > 3 ? f[3] : "EXCEPTION"),
                        pass ? null : (f.length > 4 ? f[4] : "")));
            }
        }
        for (String cls : classNames) {
            if (out.containsKey(cls)) continue;
            boolean stoppedEarly = untilFail && hasFailure(out);
            if (stoppedEarly) break;
            out.put(cls, new TestRun(cls, false, "TIMEOUT",
                    finished ? "the test JVM ended without reporting this test" : "the test JVM was killed after "
                            + budget + "s"));
            break;
        }
        return out;
    }

    private static boolean hasFailure(Map<String, TestRun> runs) {
        for (TestRun r : runs.values()) if (!r.passed) return true;
        return false;
    }

    /** Compiles one library build (correct or mutated) into {@code out}; null on success, else the error. */
    public static String compileLibrary(List<String> sources, Path out) throws IOException {
        Files.createDirectories(out);
        return javac(out, junitClasspath(), sources);
    }

    private static String javac(Path out, String classpath, List<String> sources) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> args = new ArrayList<String>();
        args.add("-nowarn");
        args.add("-cp"); args.add(classpath);
        args.add("-d"); args.add(out.toString());
        args.addAll(sources);
        java.io.ByteArrayOutputStream err = new java.io.ByteArrayOutputStream();
        int rc = compiler.run(null, null, err, args.toArray(new String[0]));
        return rc == 0 ? null : err.toString(StandardCharsets.UTF_8.name());
    }

    private static String junitClasspath() {
        StringBuilder sb = new StringBuilder();
        for (File jar : JunitJars.resolve()) {
            if (sb.length() > 0) sb.append(File.pathSeparator);
            sb.append(jar.getAbsolutePath());
        }
        return sb.toString();
    }

    private static String ownClasspath() {
        try {
            return Paths.get(MutantTestMain.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

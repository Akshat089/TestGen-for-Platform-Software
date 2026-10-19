package in.ac.iiitb.plproject.verify;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Compiles a generated {@code GeneratedATCsTest.java} together with the
 * UNMODIFIED library it names, against ONLY real JUnit 4 — no SPF, nothing
 * from {@code gov.nasa.jpf} anywhere on this classpath — and runs it (rule
 * 11's "delivered JUnit class must run with no SPF on the classpath",
 * verified here rather than only asserted).
 *
 * <p>Every library's generated classes share one fully-qualified name
 * ({@code in.ac.iiitb.plproject.atc.generated.Helper}, etc.), so running more
 * than one library's suite in the same JVM uses a fresh {@link URLClassLoader}
 * per directory rather than the system classloader, which would silently keep
 * serving the FIRST library's already-loaded {@code Helper} class forever.
 */
public class GeneratedSuiteHarness {

    public static class VerifyResult {
        public final boolean compiled;
        public final String compileError;
        public final int runCount;
        public final List<String> failures;

        VerifyResult(boolean compiled, String compileError, int runCount, List<String> failures) {
            this.compiled = compiled;
            this.compileError = compileError;
            this.runCount = runCount;
            this.failures = failures;
        }

        public boolean allPassed() { return compiled && failures.isEmpty(); }
    }

    /**
     * @param generatedDir directory containing {@code GeneratedATCsTest.java} and the
     *                     library's own sources, all in package
     *                     {@code in.ac.iiitb.plproject.atc.generated}
     * @param junitJar     real JUnit 4 jar (and transitively hamcrest-core) to compile/run against
     */
    public VerifyResult compileAndRun(File generatedDir, List<File> junitJars, String testClassName)
            throws Exception {
        File classesDir = new File(generatedDir, "verify-classes");
        Files.createDirectories(classesDir.toPath());

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> sources = new ArrayList<String>();
        // GeneratedATCs.java (the symbolic driver) is kept alongside the JUnit class
        // for transparency — it shows exactly what SPF ran — but it imports
        // gov.nasa.jpf.* and must never be compiled onto this classpath (rule 11).
        String symbolicDriverFile = in.ac.iiitb.plproject.ir.TestPlan.SYMBOLIC_CLASS + ".java";
        for (File f : generatedDir.listFiles()) {
            if (f.getName().endsWith(".java") && !f.getName().equals(symbolicDriverFile)) {
                sources.add(f.getAbsolutePath());
            }
        }
        StringBuilder cp = new StringBuilder();
        for (File jar : junitJars) {
            if (cp.length() > 0) cp.append(File.pathSeparator);
            cp.append(jar.getAbsolutePath());
        }
        List<String> args = new ArrayList<String>();
        args.add("-cp"); args.add(cp.toString());
        args.add("-d"); args.add(classesDir.getAbsolutePath());
        args.addAll(sources);

        java.io.ByteArrayOutputStream errOut = new java.io.ByteArrayOutputStream();
        int rc = compiler.run(null, null, errOut, args.toArray(new String[0]));
        if (rc != 0) {
            return new VerifyResult(false, errOut.toString(), 0, new ArrayList<String>());
        }

        List<URL> urls = new ArrayList<URL>();
        urls.add(classesDir.toURI().toURL());
        try (URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[0]),
                GeneratedSuiteHarness.class.getClassLoader())) {
            Class<?> testClass = Class.forName(testClassName, true, loader);
            JUnitCore core = new JUnitCore();
            Result result = core.run(Request.aClass(testClass));
            List<String> failures = new ArrayList<String>();
            for (Failure f : result.getFailures()) {
                failures.add(f.getTestHeader() + ": " + f.getMessage());
            }
            return new VerifyResult(true, null, result.getRunCount(), failures);
        }
    }
}

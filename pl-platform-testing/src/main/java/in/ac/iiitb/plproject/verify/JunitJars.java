package in.ac.iiitb.plproject.verify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Locates the real JUnit 4 jar (and hamcrest-core, which it needs) used both
 * to compile/run the generated {@code GeneratedATCsTest} and as this
 * project's OWN test dependency (rule 11: one JUnit version everywhere).
 */
public final class JunitJars {

    private JunitJars() {}

    public static final String JUNIT_VERSION = "4.13.1";
    public static final String HAMCREST_VERSION = "1.3";

    public static List<File> resolve() {
        List<File> jars = new ArrayList<File>();
        File junit = fromClasspath("junit-" + JUNIT_VERSION);
        if (junit == null) junit = fromM2("junit", "junit", JUNIT_VERSION);
        File hamcrest = fromClasspath("hamcrest-core-" + HAMCREST_VERSION);
        if (hamcrest == null) hamcrest = fromM2("org/hamcrest", "hamcrest-core", HAMCREST_VERSION);
        if (junit == null || hamcrest == null) {
            throw new IllegalStateException("Cannot find junit-" + JUNIT_VERSION + ".jar / hamcrest-core-"
                    + HAMCREST_VERSION + ".jar on the classpath or in ~/.m2. Run `mvn -o test-compile` once"
                    + " first so Maven resolves the test dependency.");
        }
        jars.add(junit);
        jars.add(hamcrest);
        return jars;
    }

    private static File fromClasspath(String filePrefix) {
        String cp = System.getProperty("java.class.path", "");
        for (String entry : cp.split(File.pathSeparator)) {
            String name = new File(entry).getName();
            if (name.startsWith(filePrefix) && name.endsWith(".jar")) {
                File f = new File(entry);
                if (f.isFile()) return f;
            }
        }
        return null;
    }

    private static File fromM2(String group, String artifact, String version) {
        String home = System.getProperty("user.home");
        File jar = new File(home, ".m2/repository/" + group + "/" + artifact + "/" + version
                + "/" + artifact + "-" + version + ".jar");
        return jar.isFile() ? jar : null;
    }
}

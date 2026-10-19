package in.ac.iiitb.plproject.spf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Locates the jpf-core / jpf-symbc checkouts {@code scripts/setup-spf.sh}
 * built: {@code RunJPF.jar} to run SPF as a separate process (rule 6), and
 * the checkout directories needed to assemble the driver's compile classpath
 * and the SPF process's runtime {@code native_classpath}.
 *
 * <p>Both jpf-core and jpf-symbc build TWO jars with disjoint class sets: a
 * small one for the MODELED/target-side program ({@code jpf-classes.jar} /
 * {@code jpf-symbc-classes.jar} — holds {@code Verify}/{@code Debug}, which
 * the GENERATED DRIVER imports), and the full HOST-side engine
 * ({@code jpf.jar} / {@code jpf-symbc.jar} — holds {@code Config}/{@code JPF}/
 * {@code PropertyListenerAdapter}/{@code Search}/{@code PathCondition}, which
 * the generator's OWN JPF listener imports). pl-platform-testing's
 * {@code provided}-scope Maven dependency resolves the second pair, for
 * {@code mvn compile} to build the listener; everything in this class
 * resolves the checkout directory itself, used both to find the first pair
 * (for {@link #resolveDriverCompileClasspath()}) and to assemble the runtime
 * {@code native_classpath} ({@link SpfRunner}).
 */
public final class SpfHome {

    private SpfHome() {}

    public static File resolveRunJpfJar() {
        File fromEnvHome = tryEnvHome();
        if (fromEnvHome != null) return fromEnvHome;

        File fromSiteProps = trySiteProperties();
        if (fromSiteProps != null) return fromSiteProps;

        throw new IllegalStateException(
                "Cannot find RunJPF.jar. Run scripts/setup-spf.sh first (it builds jpf-core"
              + " and jpf-symbc), or set GENATC_SPF_HOME to a directory containing jpf-core/"
              + "build/RunJPF.jar.");
    }

    private static File tryEnvHome() {
        String home = System.getenv("GENATC_SPF_HOME");
        if (home == null) home = System.getProperty("user.home") + "/.genatc-spf";
        File jar = new File(home, "jpf-core/build/RunJPF.jar");
        return jar.isFile() ? jar : null;
    }

    private static File trySiteProperties() {
        File props = new File(System.getProperty("user.home"), ".jpf/site.properties");
        if (!props.isFile()) return null;
        try (FileInputStream in = new FileInputStream(props)) {
            Properties p = new Properties();
            p.load(in);
            String core = p.getProperty("jpf-core");
            if (core == null) return null;
            File jar = new File(core, "build/RunJPF.jar");
            return jar.isFile() ? jar : null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * The jars that compile the GENERATED DRIVER (a modeled/target-side program):
     * {@code jpf-classes.jar} for {@code gov.nasa.jpf.vm.Verify}, {@code
     * jpf-symbc-classes.jar} for {@code gov.nasa.jpf.symbc.Debug}. Deliberately
     * NOT the same jars Maven's {@code provided} dependency resolves (those are
     * the HOST-side engine jars — {@code jpf.jar}/{@code jpf-symbc.jar} — which
     * is what the generator's own JPF listener needs instead; the two jar pairs
     * have disjoint class sets, confirmed by inspection while wiring this up:
     * {@code Debug.class} is only in {@code jpf-symbc-classes.jar}, never in
     * {@code jpf-symbc.jar}).
     */
    public static String resolveDriverCompileClasspath() {
        File core = new File(resolveJpfCoreCheckoutDir(), "build/jpf-classes.jar");
        File symbc = new File(resolveJpfSymbcCheckoutDir(), "build/jpf-symbc-classes.jar");
        return core.getAbsolutePath() + File.pathSeparator + symbc.getAbsolutePath();
    }

    /** jpf-core checkout directory (not just the jar) — used to assemble the runtime classpath. */
    public static File resolveJpfCoreCheckoutDir() {
        return resolveCheckoutDir("jpf-core");
    }

    /** jpf-symbc checkout directory, so its {@code lib/*.jar} solver libraries can be found too. */
    public static File resolveJpfSymbcCheckoutDir() {
        return resolveCheckoutDir("jpf-symbc");
    }

    private static File resolveCheckoutDir(String repoDirName) {
        String home = System.getenv("GENATC_SPF_HOME");
        if (home == null) home = System.getProperty("user.home") + "/.genatc-spf";
        File dir = new File(home, repoDirName);
        if (new File(dir, "build").isDirectory()) return dir;

        File props = new File(System.getProperty("user.home"), ".jpf/site.properties");
        if (props.isFile()) {
            try (FileInputStream in = new FileInputStream(props)) {
                Properties p = new Properties();
                p.load(in);
                String configured = p.getProperty(repoDirName);
                if (configured != null && new File(configured, "build").isDirectory()) {
                    return new File(configured);
                }
            } catch (IOException ignored) {
                // fall through to the error below
            }
        }
        throw new IllegalStateException("Cannot find a built " + repoDirName
                + " checkout. Run scripts/setup-spf.sh first, or set GENATC_SPF_HOME.");
    }
}

package in.ac.iiitb.plproject.spf;

/**
 * Settable SPF search bounds (rule 6): a search depth bound and a timeout.
 * Both default to a value generous enough for a 3-6 call sequence over a
 * small int range, and can be overridden by whoever drives the registry
 * (also readable from system properties, so {@code verify-generated.sh} and
 * the CLI can set them without touching code).
 */
public class SpfConfig {

    public static final SpfConfig DEFAULT = fromSystemProperties();

    /** Heap for each SPF process; the drivers are small and many run in parallel. */
    public static final String SPF_HEAP = System.getProperty("genatc.spf.heap", "384m");

    private final int depthLimit;
    private final int timeoutSeconds;
    private final String solver;
    private final int minInt;
    private final int maxInt;

    public SpfConfig(int depthLimit, int timeoutSeconds, String solver, int minInt, int maxInt) {
        this.depthLimit = depthLimit;
        this.timeoutSeconds = timeoutSeconds;
        this.solver = solver;
        this.minInt = minInt;
        this.maxInt = maxInt;
    }

    public static SpfConfig fromSystemProperties() {
        return new SpfConfig(
                Integer.getInteger("genatc.spf.depthLimit", 300),
                Integer.getInteger("genatc.spf.timeoutSeconds", 60),
                System.getProperty("genatc.spf.solver", "z3"),
                Integer.getInteger("genatc.spf.minInt", -1000),
                Integer.getInteger("genatc.spf.maxInt", 1000));
    }

    public int getDepthLimit() { return depthLimit; }
    public int getTimeoutSeconds() { return timeoutSeconds; }
    public String getSolver() { return solver; }
    public int getMinInt() { return minInt; }
    public int getMaxInt() { return maxInt; }

    public SpfConfig withSolver(String solver) {
        return new SpfConfig(depthLimit, timeoutSeconds, solver, minInt, maxInt);
    }
}

package in.ac.iiitb.plproject.ir;

/**
 * How a call parameter's value is obtained.
 *
 * <p>{@link #SYMBOLIC} means SPF genuinely solves it: the symbolic driver
 * declares it with {@code Debug.makeSymbolic*} and the solved value comes back
 * through the path listener. {@link #POOLED} means the configured solver
 * cannot handle this value directly (rule 9) — a {@code String}, or a value
 * that would otherwise feed a hash-based collection lookup (Set/Map
 * {@code contains}/{@code containsKey}/{@code get}) with a fully symbolic key,
 * which blows up both configured decision procedures — so the driver instead
 * picks a small, distinct, deterministic literal from a pool via a symbolic
 * POOL INDEX, which SPF still solves. Either way the value in the final JUnit
 * test traces back to something SPF actually explored; {@code POOLED} only
 * means the literal itself, not the choice of which one, is fixed ahead of
 * time. The report must say so (rule 9) — it is never silently presented as
 * fully solved.
 */
public enum ParamKind {
    SYMBOLIC,
    POOLED
}

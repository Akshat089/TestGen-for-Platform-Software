package in.ac.iiitb.plproject.ir;

/**
 * One conjunct of a precondition or one clause of a postcondition, kept with
 * its original text alongside the Java it rewrites to (rule 5e).
 */
public class Clause {

    private final String originalText;
    private final String javaExpr;
    /** True when this clause references at least one SYMBOLIC parameter of its own call. */
    private final boolean mentionsSymbolicParam;

    public Clause(String originalText, String javaExpr, boolean mentionsSymbolicParam) {
        this.originalText = originalText;
        this.javaExpr = javaExpr;
        this.mentionsSymbolicParam = mentionsSymbolicParam;
    }

    /** The clause exactly as written in the {@code .spec} file. */
    public String getOriginalText() { return originalText; }

    /** The same clause, rewritten: state vars qualified, {@code \old}/{@code \result} substituted. */
    public String getJavaExpr() { return javaExpr; }

    /**
     * True when a symbolic (non-pooled) parameter of this call appears in the
     * clause — decides, for a PRECONDITION conjunct, whether the symbolic
     * driver prints {@code Debug.assume(...)} (there is something for the
     * solver to constrain) or {@code Verify.ignoreIf(!(...))} (the clause is
     * already fully concrete by the time this call runs — state plus
     * pool-resolved literals — and {@code Debug.assume} on a concrete boolean
     * is a silent no-op, confirmed by direct experiment while building this).
     */
    public boolean mentionsSymbolicParam() { return mentionsSymbolicParam; }

    @Override
    public String toString() {
        return originalText + "  =>  " + javaExpr;
    }
}

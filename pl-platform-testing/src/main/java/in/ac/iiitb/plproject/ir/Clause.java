package in.ac.iiitb.plproject.ir;

/**
 * One conjunct of a precondition or one clause of a postcondition, kept with
 * its original text alongside the two forms it is printed in: the JUnit
 * assertion over the real library ({@link #getJavaExpr()}) and the condition
 * over the symbolic driver's model ({@link #getModelExpr()}).
 */
public class Clause {

    private final String originalText;
    private final String javaExpr;
    /** Condition over the model, or null when the clause is check-only. */
    private final String modelExpr;
    /** Why the model cannot express this clause; null when it can. */
    private final String checkOnlyReason;
    /** True for {@code \result == alias}: it only names the result and asserts nothing. */
    private final boolean tautological;

    public Clause(String originalText, String javaExpr, String modelExpr, String checkOnlyReason,
                  boolean tautological) {
        this.originalText = originalText;
        this.javaExpr = javaExpr;
        this.modelExpr = modelExpr;
        this.checkOnlyReason = checkOnlyReason;
        this.tautological = tautological;
    }

    /** The clause exactly as written in the {@code .spec} file. */
    public String getOriginalText() { return originalText; }

    /** The JUnit form: state vars qualified as {@code Helper.<name>}, {@code \old}/{@code \result} substituted. */
    public String getJavaExpr() { return javaExpr; }

    /** The driver form, over model variables; null when {@link #isCheckOnly()}. */
    public String getModelExpr() { return modelExpr; }

    /** True when the model cannot express this clause: it is asserted in JUnit only. */
    public boolean isCheckOnly() { return modelExpr == null && !tautological; }

    public String getCheckOnlyReason() { return checkOnlyReason; }

    /** True for a clause that only names the result ({@code \result == poppedElem}); never asserted. */
    public boolean isTautological() { return tautological; }

    @Override
    public String toString() {
        return originalText + "  =>  " + javaExpr;
    }
}

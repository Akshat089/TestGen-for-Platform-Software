package in.ac.iiitb.plproject.ir;

/**
 * One conjunct of a precondition or one clause of a postcondition, kept with
 * its original text alongside its Java form over the real library
 * ({@link #getJavaExpr()}): state variables are {@code Helper.<name>},
 * {@code \old(v)} is the snapshot local, {@code \result} the stored result.
 * The ATC's {@code assume}/{@code assert} statements, the SPF driver's
 * {@code Verify.ignoreIf} and the JUnit assertions all print this one form.
 */
public class Clause {

    private final String originalText;
    private final String javaExpr;
    /** True for {@code \result == alias}: it only names the result and asserts nothing. */
    private final boolean tautological;

    public Clause(String originalText, String javaExpr, boolean tautological) {
        this.originalText = originalText;
        this.javaExpr = javaExpr;
        this.tautological = tautological;
    }

    /** The clause exactly as written in the {@code .spec} file. */
    public String getOriginalText() { return originalText; }

    /** The Java form: state vars qualified as {@code Helper.<name>}, {@code \old}/{@code \result} substituted. */
    public String getJavaExpr() { return javaExpr; }

    /** True for a clause that only names the result ({@code \result == poppedElem}); never asserted. */
    public boolean isTautological() { return tautological; }

    @Override
    public String toString() {
        return originalText + "  =>  " + javaExpr;
    }
}

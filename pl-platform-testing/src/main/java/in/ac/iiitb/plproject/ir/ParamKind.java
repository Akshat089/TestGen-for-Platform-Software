package in.ac.iiitb.plproject.ir;

/**
 * How SPF represents a call parameter's input command while it is still
 * symbolic.
 *
 * <p>{@link #SYMBOLIC}: a {@code Debug.makeSymbolic*} value; the solver gives
 * its value. {@link #POOLED}: a String, or a number the contract uses as a
 * Set/Map key — a fully symbolic one reaching {@code hashCode()} puts a
 * bit-shift into the path condition that neither z3 nor choco accepts. The
 * driver then picks one of a few concrete candidates with a symbolic INDEX:
 * the 3-value pool ({@code "s1","s2","s3"} / {@code 101,103,105}) plus, for a
 * parameter whose precondition tests membership in a state collection, the
 * members that real collection holds at that point. SPF explores the index;
 * every value that reaches the library is concrete.
 */
public enum ParamKind {
    SYMBOLIC,
    POOLED
}

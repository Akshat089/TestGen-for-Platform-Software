package in.ac.iiitb.plproject.ir;

/**
 * How SPF represents a call parameter's input command while it is still
 * symbolic.
 *
 * <p>{@link #SYMBOLIC}: a {@code Debug.makeSymbolic*} number or boolean; the
 * solver gives its value. A number the contract uses as a Set/Map key is
 * symbolic too: a membership test on it is expanded into equalities over the
 * collection's concrete contents, so the collection's own code never sees it.
 * {@link #STRING_ID}: a String, represented as a symbolic integer id plus a
 * null flag; a solved id {@code k} becomes the literal {@code "s" + k}.
 */
public enum ParamKind {
    SYMBOLIC,
    STRING_ID
}

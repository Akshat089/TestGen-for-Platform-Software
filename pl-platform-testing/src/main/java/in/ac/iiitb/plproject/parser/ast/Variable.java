package in.ac.iiitb.plproject.parser.ast;

/**
 * A named, typed value: a function parameter or a declared state variable.
 *
 * <p>Earlier revisions of this class also classified a variable as
 * CLIENT_INPUT or SERVER_OUTPUT so a later call could receive an earlier
 * call's return value as an argument. The new design has no such threading —
 * every argument of every call is chosen by SPF (or, for a type SPF's solver
 * cannot handle, drawn from a small concrete pool) — so that classification is
 * gone. A {@code Variable} is now just a name, a declared type, and whether
 * {@code null} is a legal value.
 */
public class Variable {

    private final String name;
    private final String typeName;

    /**
     * Whether this variable's value may legitimately be null at runtime.
     *
     * Example: HashMapLib's {@code put}'s previous value is null when the key
     * was absent, so it must be carried in a boxed type (Integer, not int) and
     * must never attract a synthesised non-null assertion — only what the
     * postcondition actually constrains is asserted.
     */
    private boolean nullable = false;

    public Variable(String name, String typeName) {
        this.name = name;
        this.typeName = typeName;
    }

    public Variable(String name, Object type) {
        this.name = name;
        this.typeName = type.toString();
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    /** True when null is a legal value for this variable. */
    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public String toString() {
        return name + ": " + typeName + (nullable ? " [nullable]" : "");
    }
}

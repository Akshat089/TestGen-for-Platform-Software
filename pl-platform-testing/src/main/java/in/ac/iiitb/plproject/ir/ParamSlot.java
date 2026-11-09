package in.ac.iiitb.plproject.ir;

/**
 * One call's parameter, named {@code c<i>_<param>} where {@code i} is the
 * position of the call in the sequence.
 */
public class ParamSlot {

    private final int callIndex;
    private final String paramName;
    private final String slotName;
    private final String type;
    private final ParamKind kind;

    public ParamSlot(int callIndex, String paramName, String type, ParamKind kind) {
        this.callIndex = callIndex;
        this.paramName = paramName;
        this.slotName = "c" + callIndex + "_" + paramName;
        this.type = type;
        this.kind = kind;
    }

    public int getCallIndex() { return callIndex; }
    public String getParamName() { return paramName; }
    /** Printable Java identifier: {@code c<i>_<param>}. */
    public String getSlotName() { return slotName; }
    public String getType() { return type; }
    public ParamKind getKind() { return kind; }
    public boolean isSymbolic() { return kind == ParamKind.SYMBOLIC; }
    public boolean isStringId() { return kind == ParamKind.STRING_ID; }

    @Override
    public String toString() {
        return slotName + ": " + type + " [" + kind + "]";
    }
}

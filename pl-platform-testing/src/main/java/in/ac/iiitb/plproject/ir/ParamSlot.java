package in.ac.iiitb.plproject.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    /** Why this parameter is {@link ParamKind#POOLED}, for the report. */
    private final String poolReason;
    /** The shared literal pool it draws from, when pooled; empty when symbolic. */
    private final List<String> pool;
    /**
     * State collections the call's precondition tests this parameter's
     * membership in ({@code Tasks.containsKey(taskId)} gives {@code Tasks}):
     * their real members at that point are candidates too.
     */
    private final List<String> membership;

    public ParamSlot(int callIndex, String paramName, String type, ParamKind kind, String poolReason,
                      List<String> pool, List<String> membership) {
        this.callIndex = callIndex;
        this.paramName = paramName;
        this.slotName = "c" + callIndex + "_" + paramName;
        this.type = type;
        this.kind = kind;
        this.poolReason = poolReason;
        this.pool = pool != null ? Collections.unmodifiableList(pool) : Collections.<String>emptyList();
        this.membership = membership != null ? Collections.unmodifiableList(new ArrayList<String>(membership))
                                             : Collections.<String>emptyList();
    }

    public int getCallIndex() { return callIndex; }
    public String getParamName() { return paramName; }
    /** Printable Java identifier: {@code c<i>_<param>}. */
    public String getSlotName() { return slotName; }
    public String getType() { return type; }
    public ParamKind getKind() { return kind; }
    public boolean isSymbolic() { return kind == ParamKind.SYMBOLIC; }
    public boolean isPooled() { return kind == ParamKind.POOLED; }
    public String getPoolReason() { return poolReason; }
    public List<String> getPool() { return pool; }
    public List<String> getMembership() { return membership; }

    @Override
    public String toString() {
        return slotName + ": " + type + " [" + kind + (poolReason != null ? ", " + poolReason : "") + "]";
    }
}

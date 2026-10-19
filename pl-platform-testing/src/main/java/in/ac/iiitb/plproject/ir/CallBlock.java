package in.ac.iiitb.plproject.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One position in the sequence: rule 5's call block.
 *
 * <p>Holds everything both printers need, already rewritten once: the
 * operation and its parameters (a), the precondition as a list of conjuncts
 * (b), the state variables the postcondition snapshots via {@code \old} (c),
 * the call and whether its result is stored (d), and the postcondition as a
 * list of clauses (e).
 */
public class CallBlock {

    private final int index;
    private final String operationName;
    private final String returnType; // "void" when the call returns nothing
    private final List<ParamSlot> params;
    private final List<Clause> preconditions;
    private final List<Snapshot> snapshots;
    private final boolean storesResult;
    private final String resultVarName; // e.g. "c2_result"; null when storesResult is false
    private final String resultAlias;   // the \result == name binding's name, or null
    private final List<Clause> postconditions;

    public CallBlock(int index, String operationName, String returnType,
                      List<ParamSlot> params, List<Clause> preconditions,
                      List<Snapshot> snapshots, boolean storesResult, String resultVarName,
                      String resultAlias, List<Clause> postconditions) {
        this.index = index;
        this.operationName = operationName;
        this.returnType = returnType;
        this.params = Collections.unmodifiableList(new ArrayList<ParamSlot>(params));
        this.preconditions = Collections.unmodifiableList(new ArrayList<Clause>(preconditions));
        this.snapshots = Collections.unmodifiableList(new ArrayList<Snapshot>(snapshots));
        this.storesResult = storesResult;
        this.resultVarName = resultVarName;
        this.resultAlias = resultAlias;
        this.postconditions = Collections.unmodifiableList(new ArrayList<Clause>(postconditions));
    }

    public int getIndex() { return index; }
    public String getOperationName() { return operationName; }
    public String getReturnType() { return returnType; }
    public List<ParamSlot> getParams() { return params; }
    public List<Clause> getPreconditions() { return preconditions; }
    public List<Snapshot> getSnapshots() { return snapshots; }
    public boolean isStoresResult() { return storesResult; }
    public String getResultVarName() { return resultVarName; }
    public String getResultAlias() { return resultAlias; }
    public List<Clause> getPostconditions() { return postconditions; }

    /** The call expression's argument list, in declaration order: {@code c0_elem, c0_x, ...}. */
    public String argumentList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(params.get(i).getSlotName());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "call[" + index + "] " + operationName + "(" + argumentList() + ")";
    }
}

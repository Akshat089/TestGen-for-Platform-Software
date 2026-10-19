package in.ac.iiitb.plproject.ir;

/**
 * One {@code \old(v)} snapshot a call's postcondition reads: a plain local of
 * the state variable's declared type, taken right before the call (rule 5c
 * and the "snapshots are plain locals of the declared state type" test).
 */
public class Snapshot {

    private final String stateVarName;
    private final String type;
    private final String localName;

    public Snapshot(String stateVarName, String type, String localName) {
        this.stateVarName = stateVarName;
        this.type = type;
        this.localName = localName;
    }

    public String getStateVarName() { return stateVarName; }
    public String getType() { return type; }
    /** e.g. {@code old0_size} for call index 0's snapshot of state variable {@code size}. */
    public String getLocalName() { return localName; }

    @Override
    public String toString() {
        return localName + " = Helper." + stateVarName + " (" + type + ")";
    }
}

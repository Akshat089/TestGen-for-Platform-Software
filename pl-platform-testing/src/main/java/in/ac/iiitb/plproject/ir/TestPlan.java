package in.ac.iiitb.plproject.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The single shared representation Step 2 builds and both printers (Step 3
 * and Step 6) read: an optional reset, then one {@link CallBlock} per
 * position in the sequence (rule 5).
 */
public class TestPlan {

    /** Package every generated driver/test class lives in. */
    public static final String GENERATED_PACKAGE = "in.ac.iiitb.plproject.atc.generated";
    /** Class name of the symbolic driver (the SPF target). */
    public static final String SYMBOLIC_CLASS = "GeneratedATCs";
    /** Class name of the JUnit test class the JUnit printer emits. */
    public static final String JUNIT_CLASS = "GeneratedATCsTest";

    private final String libraryKey;
    private final String specFile;
    private final String testsFile;
    /** True when the spec declares a {@code state { ... }} block (rule 2). */
    private final boolean hasState;
    private final Map<String, String> stateVars;
    private final List<CallBlock> callBlocks;

    public TestPlan(String libraryKey, String specFile, String testsFile, boolean hasState,
                     Map<String, String> stateVars, List<CallBlock> callBlocks) {
        this.libraryKey = libraryKey;
        this.specFile = specFile;
        this.testsFile = testsFile;
        this.hasState = hasState;
        this.stateVars = Collections.unmodifiableMap(new LinkedHashMap<String, String>(stateVars));
        this.callBlocks = Collections.unmodifiableList(new ArrayList<CallBlock>(callBlocks));
    }

    public String getLibraryKey() { return libraryKey; }
    public String getSpecFile() { return specFile; }
    public String getTestsFile() { return testsFile; }
    public boolean isHasState() { return hasState; }
    public Map<String, String> getStateVars() { return stateVars; }
    public List<CallBlock> getCallBlocks() { return callBlocks; }

    /** Every pooled parameter across the whole plan, for the "not solved for" report (rule 9). */
    public List<ParamSlot> pooledParams() {
        List<ParamSlot> out = new ArrayList<ParamSlot>();
        for (CallBlock block : callBlocks) {
            for (ParamSlot p : block.getParams()) {
                if (p.isPooled()) out.add(p);
            }
        }
        return out;
    }
}

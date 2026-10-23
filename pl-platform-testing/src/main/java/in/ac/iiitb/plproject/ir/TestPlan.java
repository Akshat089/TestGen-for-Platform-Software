package in.ac.iiitb.plproject.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The single shared representation built from the contract and one sequence:
 * one {@link CallBlock} per position in the sequence. The abstract test case
 * ({@link in.ac.iiitb.plproject.symbolic.TestGen#getATC}), every SPF driver
 * and the final JUnit test are all printed from it.
 */
public class TestPlan {

    /** Package every generated driver/test class lives in (the library's own package). */
    public static final String GENERATED_PACKAGE = "in.ac.iiitb.plproject.atc.generated";
    /** Class name of the SPF driver printed each genCTC round. */
    public static final String SYMBOLIC_CLASS = "GeneratedATCs";
    /** Class name of the JUnit test class the JUnit printer emits. */
    public static final String JUNIT_CLASS = "GeneratedATCsTest";

    private final String libraryKey;
    private final String specFile;
    private final String testsFile;
    /** True when the spec declares a {@code state { ... }} block. */
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

    /** Every pooled parameter across the whole plan. */
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

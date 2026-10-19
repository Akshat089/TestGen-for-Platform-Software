package in.ac.iiitb.plproject.spf;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * One solved input vector for the whole sequence: slot name -&gt; Java literal
 * (rule 7), plus which slots the solver left unconstrained (rule 8) and which
 * were pooled rather than genuinely solved (rule 9) — both surfaced so the
 * JUnit printer and the report can say so honestly.
 */
public class InputVector {

    private final int pathId;
    private final Map<String, String> literalsBySlot;
    private final Set<String> unconstrainedSlots;
    private final Set<String> pooledSlots;

    public InputVector(int pathId, Map<String, String> literalsBySlot,
                        Set<String> unconstrainedSlots, Set<String> pooledSlots) {
        this.pathId = pathId;
        this.literalsBySlot = Collections.unmodifiableMap(new LinkedHashMap<String, String>(literalsBySlot));
        this.unconstrainedSlots = Collections.unmodifiableSet(unconstrainedSlots);
        this.pooledSlots = Collections.unmodifiableSet(pooledSlots);
    }

    public int getPathId() { return pathId; }
    public Map<String, String> getLiteralsBySlot() { return literalsBySlot; }
    public String literalFor(String slotName) { return literalsBySlot.get(slotName); }
    public Set<String> getUnconstrainedSlots() { return unconstrainedSlots; }
    public Set<String> getPooledSlots() { return pooledSlots; }
}

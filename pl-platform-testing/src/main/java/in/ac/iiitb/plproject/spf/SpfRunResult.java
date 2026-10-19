package in.ac.iiitb.plproject.spf;

import in.ac.iiitb.plproject.ir.TestPlan;

import java.util.Collections;
import java.util.List;

/** Everything one SPF run against one {@link TestPlan} found out (Step 4/5's outcome). */
public class SpfRunResult {

    private final TestPlan plan;
    private final List<PathVector> rawPaths;
    private final List<InputVector> inputVectors;
    private final boolean depthLimitHit;
    private final boolean timedOut;
    private final boolean compileFailed;
    private final String compileError;
    /** Set only when {@code inputVectors} is empty for a genuine (not cut-off) reason. */
    private final Integer infeasibleAtCallIndex;
    private final long elapsedMillis;

    public SpfRunResult(TestPlan plan, List<PathVector> rawPaths, List<InputVector> inputVectors,
                         boolean depthLimitHit, boolean timedOut, boolean compileFailed,
                         String compileError, Integer infeasibleAtCallIndex, long elapsedMillis) {
        this.plan = plan;
        this.rawPaths = Collections.unmodifiableList(rawPaths);
        this.inputVectors = Collections.unmodifiableList(inputVectors);
        this.depthLimitHit = depthLimitHit;
        this.timedOut = timedOut;
        this.compileFailed = compileFailed;
        this.compileError = compileError;
        this.infeasibleAtCallIndex = infeasibleAtCallIndex;
        this.elapsedMillis = elapsedMillis;
    }

    public TestPlan getPlan() { return plan; }
    public List<PathVector> getRawPaths() { return rawPaths; }
    public List<InputVector> getInputVectors() { return inputVectors; }
    public boolean isDepthLimitHit() { return depthLimitHit; }
    public boolean isTimedOut() { return timedOut; }
    public boolean isCompileFailed() { return compileFailed; }
    public String getCompileError() { return compileError; }
    public Integer getInfeasibleAtCallIndex() { return infeasibleAtCallIndex; }
    public long getElapsedMillis() { return elapsedMillis; }

    public boolean hasUsablePaths() { return !inputVectors.isEmpty(); }

    public int pathsExplored() {
        int n = 0;
        for (PathVector p : rawPaths) if (p.getEvent() == PathVector.Event.PATH) n++;
        return n;
    }
}

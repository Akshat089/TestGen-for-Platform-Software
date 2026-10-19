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
    /**
     * Set when EVERY explored end state was a property violation rather than a
     * normal completion — not "the precondition is unsatisfiable" but "SPF
     * cannot execute this call symbolically at all", e.g. the confirmed
     * jpf-symbc IREM/IDIV limitation found while building this (a fresh
     * symbolic int's underlying concrete shadow value is the JVM default 0,
     * and the bytecode handler's fast concrete-zero check fires on that
     * BEFORE ever consulting the path condition, so ANY symbolic value used
     * as a {@code %}/{@code /} divisor throws immediately regardless of what
     * was {@code Debug.assume}d about it). Reported distinctly (rule 10).
     */
    private final String executionErrorDetail;
    private final long elapsedMillis;

    public SpfRunResult(TestPlan plan, List<PathVector> rawPaths, List<InputVector> inputVectors,
                         boolean depthLimitHit, boolean timedOut, boolean compileFailed,
                         String compileError, Integer infeasibleAtCallIndex, long elapsedMillis) {
        this(plan, rawPaths, inputVectors, depthLimitHit, timedOut, compileFailed, compileError,
                infeasibleAtCallIndex, null, elapsedMillis);
    }

    public SpfRunResult(TestPlan plan, List<PathVector> rawPaths, List<InputVector> inputVectors,
                         boolean depthLimitHit, boolean timedOut, boolean compileFailed,
                         String compileError, Integer infeasibleAtCallIndex, String executionErrorDetail,
                         long elapsedMillis) {
        this.plan = plan;
        this.rawPaths = Collections.unmodifiableList(rawPaths);
        this.inputVectors = Collections.unmodifiableList(inputVectors);
        this.depthLimitHit = depthLimitHit;
        this.timedOut = timedOut;
        this.compileFailed = compileFailed;
        this.compileError = compileError;
        this.infeasibleAtCallIndex = infeasibleAtCallIndex;
        this.executionErrorDetail = executionErrorDetail;
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
    public String getExecutionErrorDetail() { return executionErrorDetail; }
    public long getElapsedMillis() { return elapsedMillis; }

    public boolean hasUsablePaths() { return !inputVectors.isEmpty(); }

    public int pathsExplored() {
        int n = 0;
        for (PathVector p : rawPaths) if (p.getEvent() == PathVector.Event.PATH) n++;
        return n;
    }

    /**
     * How many end states SPF found to be a property violation (an uncaught
     * exception, in this project — there are no printed assertions in the
     * symbolic driver for a real violation to trip). Not included among
     * {@link #getInputVectors()}; surfaced so the report can say a crash is
     * REACHABLE somewhere in the state space even though it is not part of
     * the delivered suite (rule 10's spirit: report distinctly, don't hide).
     */
    public int errorStatesFound() {
        int n = 0;
        for (PathVector p : rawPaths) if (p.getEvent() == PathVector.Event.ERROR) n++;
        return n;
    }
}

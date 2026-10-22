package in.ac.iiitb.plproject.spf;

import java.util.Collections;
import java.util.List;

/** What one SPF run over one sequence's contract-only driver found (Steps 4/5). */
public class SpfRunResult {

    public enum Status {
        /** SPF found a path through the whole sequence and the solver gave its inputs. */
        SOLVED,
        /** No path: a call's precondition cannot hold after the calls before it. */
        INCORRECT_SEQUENCE,
        /** No path: every precondition can hold but some postcondition cannot. */
        UNSAT_POSTCONDITION,
        /** SPF or the solver failed (an exception, a crashed run, no search result). */
        SOLVER_ERROR,
        /** The SPF time limit was reached before any path completed. */
        TIMEOUT,
        /** The generated driver did not compile — a generator bug. */
        COMPILE_ERROR
    }

    private final Status status;
    private final InputVector vector;
    private final List<PathVector> rawPaths;
    /** INCORRECT_SEQUENCE / UNSAT_POSTCONDITION: the call that blocks, else -1. */
    private final int blockingCall;
    /** The clause that blocks, as written in the contract. */
    private final String blockingClause;
    private final String detail;
    private final long elapsedMillis;

    public SpfRunResult(Status status, InputVector vector, List<PathVector> rawPaths, int blockingCall,
                        String blockingClause, String detail, long elapsedMillis) {
        this.status = status;
        this.vector = vector;
        this.rawPaths = rawPaths == null ? Collections.<PathVector>emptyList()
                                         : Collections.unmodifiableList(rawPaths);
        this.blockingCall = blockingCall;
        this.blockingClause = blockingClause;
        this.detail = detail;
        this.elapsedMillis = elapsedMillis;
    }

    public Status getStatus() { return status; }
    public boolean isSolved() { return status == Status.SOLVED; }
    /** The one input vector, when solved. */
    public InputVector getVector() { return vector; }
    public List<PathVector> getRawPaths() { return rawPaths; }
    public int getBlockingCall() { return blockingCall; }
    public String getBlockingClause() { return blockingClause; }
    public String getDetail() { return detail; }
    public long getElapsedMillis() { return elapsedMillis; }

    /** Completed paths SPF reported (the search stops at the first solved one). */
    public int pathsCompleted() {
        int n = 0;
        for (PathVector p : rawPaths) if (p.getEvent() == PathVector.Event.PATH) n++;
        return n;
    }
}

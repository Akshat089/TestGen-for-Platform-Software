package in.ac.iiitb.plproject.symbolic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * What symex returned: values for the inputs it met (possibly none), or
 * NO_SOLUTION — a distinct result, never an empty list — or a failure of the
 * engine itself, each reported on its own.
 */
public final class Solution {

    public enum Kind {
        /** One Java literal per input of the prefix, in order (empty when the prefix had none). */
        VALUES,
        /** No path satisfies the assumes: {@link #blocking} names the one no path got past. */
        NO_SOLUTION,
        SOLVER_ERROR,
        TIMEOUT,
        /** A ready call threw on the real library, on every path. */
        LIBRARY_EXCEPTION,
        /** A symbolic value reached the library (the black-box guard fired). */
        BLACK_BOX_VIOLATION,
        /** The printed driver did not compile — a generator bug or a state field the library lacks. */
        COMPILE_ERROR
    }

    public final Kind kind;
    public final List<String> values;
    public final Stmt.Assume blocking;
    public final String detail;
    public final long millis;
    /** The Java program the engine ran for this round, or null. */
    public final String program;
    /** Set by {@link TestGen#symex}: what it handed the engine. */
    TestGen.Prefix prefix;

    private Solution(Kind kind, List<String> values, Stmt.Assume blocking, String detail, long millis,
                     String program) {
        this.kind = kind;
        this.values = values == null ? null : Collections.unmodifiableList(new ArrayList<String>(values));
        this.blocking = blocking;
        this.detail = detail;
        this.millis = millis;
        this.program = program;
    }

    public static Solution values(List<String> values, long millis, String program) {
        return new Solution(Kind.VALUES, values, null, null, millis, program);
    }

    public static Solution noSolution(Stmt.Assume blocking, long millis, String program) {
        return new Solution(Kind.NO_SOLUTION, null, blocking, null, millis, program);
    }

    public static Solution failure(Kind kind, String detail, long millis, String program) {
        if (kind == Kind.VALUES || kind == Kind.NO_SOLUTION) throw new IllegalArgumentException(kind.name());
        return new Solution(kind, null, null, detail, millis, program);
    }

    public TestGen.Prefix getPrefix() { return prefix; }

    public String describe() {
        switch (kind) {
            case VALUES: return values.toString();
            case NO_SOLUTION:
                return "NO_SOLUTION" + (blocking == null ? "" : " at assume(" + blocking.clause.getJavaExpr()
                        + ") of call " + blocking.block.getIndex() + " (" + blocking.block.getOperationName() + ")");
            case SOLVER_ERROR: return "SOLVER ERROR: " + detail;
            case TIMEOUT: return "TIMEOUT: " + detail;
            case LIBRARY_EXCEPTION: return "LIBRARY EXCEPTION: " + detail;
            case BLACK_BOX_VIOLATION: return "BLACK-BOX VIOLATION: " + detail;
            default: return "DRIVER DID NOT COMPILE: " + detail;
        }
    }
}

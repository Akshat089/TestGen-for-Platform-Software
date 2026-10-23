package in.ac.iiitb.plproject.symbolic;

import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.Clause;
import in.ac.iiitb.plproject.ir.ParamSlot;

/**
 * One statement of an abstract (or concrete) test case. An ATC is an ordered
 * {@code List<Stmt>}; {@link TestGen#getATC} builds it from the IR and every
 * later form (each round's t', the final concrete test) is the same list with
 * input commands replaced by assignments.
 */
public abstract class Stmt {

    /** The library's real starting state: {@code Helper.reset()}. */
    public static final class Reset extends Stmt {
        @Override public String toString() {
            return "// starting state: the library's real state after Helper.reset()";
        }
    }

    public static final class Comment extends Stmt {
        public final String text;
        public Comment(String text) { this.text = text; }
        @Override public String toString() { return "// " + text; }
    }

    /** {@code x := input<T>()}: an argument whose value is not chosen yet. */
    public static final class Input extends Stmt {
        public final String var;
        public final String type;
        public final ParamSlot slot;
        public final CallBlock block;
        /** Position among all input commands of the ATC, from 0. */
        public final int ordinal;
        public Input(String var, String type, ParamSlot slot, CallBlock block, int ordinal) {
            this.var = var;
            this.type = type;
            this.slot = slot;
            this.block = block;
            this.ordinal = ordinal;
        }
        @Override public String toString() { return var + " := input<" + type + ">()"; }
    }

    /**
     * {@code x := e}. A call to an operation that returns nothing, or whose
     * result the contract never names, has no target ({@code var == null}).
     */
    public static final class Assign extends Stmt {
        public final String var;
        public final String type;
        public final Expr expr;
        /** The call this assignment belongs to. */
        public final CallBlock block;
        /** The input command rewriteATC replaced with this assignment, or null. */
        public final Input fromInput;
        public Assign(String var, String type, Expr expr, CallBlock block, Input fromInput) {
            this.var = var;
            this.type = type;
            this.expr = expr;
            this.block = block;
            this.fromInput = fromInput;
        }

        /** True for the real call to the library. */
        public boolean isApiCall() {
            return expr instanceof Expr.Call && ((Expr.Call) expr).api;
        }
        @Override public String toString() { return (var == null ? "" : var + " := ") + expr; }
    }

    /** {@code assume(c)}: one precondition conjunct. */
    public static final class Assume extends Stmt {
        public final Clause clause;
        public final CallBlock block;
        public Assume(Clause clause, CallBlock block) {
            this.clause = clause;
            this.block = block;
        }
        @Override public String toString() { return "assume(" + clause.getJavaExpr() + ")"; }
    }

    /** {@code assert(c)}: one postcondition clause. */
    public static final class Assert extends Stmt {
        public final Clause clause;
        public final CallBlock block;
        public Assert(Clause clause, CallBlock block) {
            this.clause = clause;
            this.block = block;
        }
        @Override public String toString() { return "assert(" + clause.getJavaExpr() + ")"; }
    }
}

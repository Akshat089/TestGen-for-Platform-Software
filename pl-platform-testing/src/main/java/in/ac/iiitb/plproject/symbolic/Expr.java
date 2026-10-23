package in.ac.iiitb.plproject.symbolic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The right-hand side of an ATC assignment: a variable, a number (any
 * literal), an operator over operands, or a call. A call to an API function —
 * an operation of the library — is the REAL call; a call to anything else is
 * not an API call.
 */
public abstract class Expr {

    public static final class Var extends Expr {
        public final String name;
        public Var(String name) { this.name = name; }
        @Override public String toString() { return name; }
    }

    /** A literal: {@code 3}, {@code "s1"}, {@code 7L}, {@code true}. */
    public static final class Num extends Expr {
        public final String literal;
        public Num(String literal) { this.literal = literal; }
        @Override public String toString() { return literal; }
    }

    public static final class Op extends Expr {
        public final String op;
        public final List<Expr> operands;
        public Op(String op, List<Expr> operands) {
            this.op = op;
            this.operands = Collections.unmodifiableList(new ArrayList<Expr>(operands));
        }
        @Override public String toString() {
            if (operands.size() == 1) return op + operands.get(0);
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < operands.size(); i++) {
                if (i > 0) sb.append(' ').append(op).append(' ');
                sb.append(operands.get(i));
            }
            return sb.append(')').toString();
        }
    }

    public static final class Call extends Expr {
        public final String function;
        public final List<Expr> args;
        /** True for an operation of the library under test. */
        public final boolean api;
        public Call(String function, List<Expr> args, boolean api) {
            this.function = function;
            this.args = Collections.unmodifiableList(new ArrayList<Expr>(args));
            this.api = api;
        }
        @Override public String toString() {
            StringBuilder sb = new StringBuilder(function).append('(');
            for (int i = 0; i < args.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(args.get(i));
            }
            return sb.append(')').toString();
        }
    }
}

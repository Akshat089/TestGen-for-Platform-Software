package in.ac.iiitb.plproject.symbolic;

import in.ac.iiitb.plproject.ir.CallBlock;
import in.ac.iiitb.plproject.ir.Clause;
import in.ac.iiitb.plproject.ir.ParamSlot;
import in.ac.iiitb.plproject.ir.Snapshot;
import in.ac.iiitb.plproject.ir.TestPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The TestGen algorithm ("Test the REST"): the abstract test case is
 * concretised one round at a time, running every call whose arguments are
 * already concrete on the REAL library and asking the solver only for the
 * inputs before the first call that is not ready yet.
 *
 * <pre>
 *   genCTC(t, L, sigma):
 *     if not isAbstract(t): return t
 *     t' = rewriteATC(t, L)
 *     L' = symex(t', sigma)
 *     if L' == NO_SOLUTION: reject t
 *     return genCTC(t', L', sigma)
 * </pre>
 *
 * The control of {@link #symex} — which statements are ready, what sigma
 * holds, which constraints are collected — runs here; executing the ready
 * prefix, building the path condition and solving it is the {@link SymexEngine}'s
 * job (SPF, in {@link in.ac.iiitb.plproject.spf.SpfSymexEngine}). The library
 * is never read: it is only called, by SPF and by the final JUnit test, and
 * only with concrete arguments.
 */
public final class TestGen {

    private TestGen() {}

    /** The variable a state variable {@code v} is read through in the ATC. */
    public static String stateVar(String name) {
        return "Helper." + name;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // the abstract test case
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * getATC: the IR as an ordered list of statements. Per call: a snapshot of
     * each state variable the postcondition reads through {@code \old}, one
     * input command per argument, one {@code assume} per precondition
     * conjunct, the real API call, one {@code assert} per postcondition clause
     * (except a clause that only names the result).
     */
    public static List<Stmt> getATC(TestPlan plan) {
        List<Stmt> t = new ArrayList<Stmt>();
        if (plan.isHasState()) t.add(new Stmt.Reset());
        int ordinal = 0;
        for (CallBlock block : plan.getCallBlocks()) {
            t.add(new Stmt.Comment("call " + block.getIndex() + ": " + block.getOperationName()));
            for (Snapshot s : block.getSnapshots()) {
                t.add(new Stmt.Assign(s.getLocalName(), s.getType(),
                        new Expr.Var(stateVar(s.getStateVarName())), block, null));
            }
            List<Expr> args = new ArrayList<Expr>();
            for (ParamSlot p : block.getParams()) {
                t.add(new Stmt.Input(p.getSlotName(), p.getType(), p, block, ordinal++));
                args.add(new Expr.Var(p.getSlotName()));
            }
            for (Clause c : block.getPreconditions()) t.add(new Stmt.Assume(c, block));
            Expr call = new Expr.Call(block.getOperationName(), args, true);
            t.add(block.isStoresResult()
                    ? new Stmt.Assign(block.getResultVarName(), block.getReturnType(), call, block, null)
                    : new Stmt.Assign(null, block.getReturnType(), call, block, null));
            for (Clause c : block.getPostconditions()) {
                if (!c.isTautological()) t.add(new Stmt.Assert(c, block));
            }
        }
        return t;
    }

    /**
     * sigma at the start: the declared type of every variable of the test
     * case. The state variables are concrete (the library's real state after
     * {@code reset()}); everything else is not defined yet.
     */
    public static Sigma typeEnv(TestPlan plan) {
        Sigma sigma = new Sigma();
        for (Map.Entry<String, String> e : plan.getStateVars().entrySet()) {
            sigma.declare(stateVar(e.getKey()), e.getValue().trim(), Sigma.Value.concrete(null));
        }
        for (CallBlock block : plan.getCallBlocks()) {
            for (Snapshot s : block.getSnapshots()) {
                sigma.declare(s.getLocalName(), s.getType(), Sigma.Value.UNDEFINED);
            }
            for (ParamSlot p : block.getParams()) {
                sigma.declare(p.getSlotName(), p.getType(), Sigma.Value.UNDEFINED);
            }
            if (block.isStoresResult()) {
                sigma.declare(block.getResultVarName(), block.getReturnType(), Sigma.Value.UNDEFINED);
            }
        }
        return sigma;
    }

    /** True if {@code t} has at least one input command. */
    public static boolean isAbstract(List<Stmt> t) {
        for (Stmt s : t) if (s instanceof Stmt.Input) return true;
        return false;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // rewriteATC
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Replaces the first {@code |L|} input commands of {@code t}, in order, by
     * assignments of the values in {@code L}; every other statement is kept.
     */
    public static List<Stmt> rewriteATC(List<Stmt> t, List<String> L) {
        if (t.isEmpty() && !L.isEmpty()) {
            throw new IllegalArgumentException("rewriteATC: more values than input commands, left over: " + L);
        }
        List<Stmt> out = new ArrayList<Stmt>();
        if (t.isEmpty()) return out;
        Stmt s1 = t.get(0);
        List<Stmt> rest = t.subList(1, t.size());
        if (s1 instanceof Stmt.Input && !L.isEmpty()) {
            Stmt.Input in = (Stmt.Input) s1;
            out.add(new Stmt.Assign(in.var, in.type, new Expr.Num(L.get(0)), in.block, in));
            out.addAll(rewriteATC(rest, L.subList(1, L.size())));
        } else {
            out.add(s1);
            out.addAll(rewriteATC(rest, L));
        }
        return out;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // symex
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Ready statements: an assignment is ready if its expression is; every
     * other statement (input, assume, assert, a comment) is ready.
     */
    public static boolean isReady(Stmt s, Sigma sigma) {
        if (s instanceof Stmt.Assign) return isReady(((Stmt.Assign) s).expr, sigma);
        return true;
    }

    /**
     * {@code Var(x)} is ready iff sigma[x] is not symbolic; a number is ready;
     * an operator is ready iff its operands are; a call to an API function is
     * ready iff ALL its arguments are; a call to any other function is ready.
     */
    public static boolean isReady(Expr e, Sigma sigma) {
        if (e instanceof Expr.Var) return !sigma.get(((Expr.Var) e).name).isSymbolic();
        if (e instanceof Expr.Num) return true;
        if (e instanceof Expr.Op) {
            for (Expr o : ((Expr.Op) e).operands) if (!isReady(o, sigma)) return false;
            return true;
        }
        Expr.Call call = (Expr.Call) e;
        if (!call.api) return true;
        for (Expr a : call.args) if (!isReady(a, sigma)) return false;
        return true;
    }

    /** {@code Var} -&gt; true; {@code Num} -&gt; false; an operator (or call) is symbolic if any operand is. */
    public static boolean isSymbolic(Expr e) {
        if (e instanceof Expr.Var) return true;
        if (e instanceof Expr.Num) return false;
        List<Expr> operands = e instanceof Expr.Op ? ((Expr.Op) e).operands : ((Expr.Call) e).args;
        for (Expr o : operands) if (isSymbolic(o)) return true;
        return false;
    }

    /**
     * The value of {@code e} under sigma. A ready API call runs on the real
     * library with concrete arguments, so its result is concrete.
     */
    public static Sigma.Value symeval(Expr e, Sigma sigma) {
        if (!isSymbolic(e)) {
            return Sigma.Value.concrete(e instanceof Expr.Num ? ((Expr.Num) e).literal : null);
        }
        if (e instanceof Expr.Var) return sigma.get(((Expr.Var) e).name);
        if (e instanceof Expr.Call && ((Expr.Call) e).api) return Sigma.Value.concrete(null);
        List<Expr> operands = e instanceof Expr.Op ? ((Expr.Op) e).operands : ((Expr.Call) e).args;
        for (Expr o : operands) if (symeval(o, sigma).isSymbolic()) return Sigma.Value.SYMBOLIC;
        return Sigma.Value.concrete(null);
    }

    private static final Pattern IDENT = Pattern.compile("(?<![A-Za-z0-9_.\"])[A-Za-z_][A-Za-z0-9_]*");

    /** symeval of a condition: every variable sigma knows a literal for is replaced by it. */
    public static String symeval(Clause c, Sigma sigma) {
        String text = c.getJavaExpr();
        Matcher m = IDENT.matcher(text);
        StringBuffer out = new StringBuffer();
        while (m.find()) {
            Sigma.Value v = sigma.get(m.group());
            String replacement = v.state == Sigma.State.CONCRETE && v.literal != null ? v.literal : m.group();
            m.appendReplacement(out, Matcher.quoteReplacement(replacement));
        }
        m.appendTail(out);
        return out.toString();
    }

    /** One collected constraint: the assume it came from and its symbolic evaluation. */
    public static final class Constraint {
        public final Stmt.Assume assume;
        public final String condition;
        Constraint(Stmt.Assume assume, String condition) {
            this.assume = assume;
            this.condition = condition;
        }
    }

    /**
     * Input: sigma[x] becomes a fresh symbolic value. Assign: sigma[x] =
     * symeval(e) — for a ready API call, the call runs and its result is
     * concrete. Assume: its condition is appended to C. Assert is not a
     * constraint and is skipped.
     */
    public static void symexInstr(Stmt s, Sigma sigma, List<Constraint> C) {
        if (s instanceof Stmt.Input) {
            sigma.set(((Stmt.Input) s).var, Sigma.Value.SYMBOLIC);
        } else if (s instanceof Stmt.Assign) {
            Stmt.Assign a = (Stmt.Assign) s;
            Sigma.Value v = symeval(a.expr, sigma);
            if (a.var != null) sigma.set(a.var, v);
        } else if (s instanceof Stmt.Assume) {
            Stmt.Assume a = (Stmt.Assume) s;
            C.add(new Constraint(a, symeval(a.clause, sigma)));
        }
    }

    /** {@code c1 && c2 && ... && cn}. */
    public static final class PathConstraint {
        public final List<Constraint> conjuncts;
        PathConstraint(List<Constraint> conjuncts) {
            this.conjuncts = Collections.unmodifiableList(new ArrayList<Constraint>(conjuncts));
        }
        @Override public String toString() {
            if (conjuncts.isEmpty()) return "true";
            StringBuilder sb = new StringBuilder();
            for (Constraint c : conjuncts) {
                if (sb.length() > 0) sb.append(" && ");
                sb.append('(').append(c.condition).append(')');
            }
            return sb.toString();
        }
    }

    public static PathConstraint computePathConstraint(List<Constraint> C) {
        return new PathConstraint(C);
    }

    /** What symex handed the engine: the ready prefix of t and its symbolic inputs. */
    public static final class Prefix {
        public final List<Stmt> statements;
        /** The input commands in the prefix, in order: solve() returns one value for each. */
        public final List<Stmt.Input> inputs;
        /** The first statement that is not ready, or null when all of t is ready. */
        public final Stmt firstNotReady;
        public final PathConstraint pc;
        Prefix(List<Stmt> statements, List<Stmt.Input> inputs, Stmt firstNotReady, PathConstraint pc) {
            this.statements = Collections.unmodifiableList(new ArrayList<Stmt>(statements));
            this.inputs = Collections.unmodifiableList(new ArrayList<Stmt.Input>(inputs));
            this.firstNotReady = firstNotReady;
            this.pc = pc;
        }
    }

    /**
     * Runs t' up to its first not-ready statement and solves the path
     * constraint for the inputs met on the way.
     */
    public static Solution symex(List<Stmt> t, Sigma sigma, SymexEngine engine) {
        Sigma s = sigma.copy();
        List<Constraint> C = new ArrayList<Constraint>();
        List<Stmt> ready = new ArrayList<Stmt>();
        List<Stmt.Input> inputs = new ArrayList<Stmt.Input>();
        Stmt firstNotReady = null;
        for (Stmt si : t) {
            if (!isReady(si, s)) {
                firstNotReady = si;
                break;
            }
            symexInstr(si, s, C);
            ready.add(si);
            if (si instanceof Stmt.Input) inputs.add((Stmt.Input) si);
        }
        PathConstraint pc = computePathConstraint(C);
        Prefix prefix = new Prefix(ready, inputs, firstNotReady, pc);
        Solution solution = engine.solve(prefix);
        solution.prefix = prefix;
        return solution;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // genCTC
    // ─────────────────────────────────────────────────────────────────────────

    /** One step of genCTC: L, t' = rewriteATC(t, L), and L' = symex(t'). */
    public static final class Round {
        public final List<String> values;
        public final List<Stmt> rewritten;
        public final Solution solution;
        Round(List<String> values, List<Stmt> rewritten, Solution solution) {
            this.values = values;
            this.rewritten = rewritten;
            this.solution = solution;
        }
    }

    public static final class Result {
        public enum Status {
            GENERATED, INCORRECT_SEQUENCE, SOLVER_ERROR, TIMEOUT, LIBRARY_EXCEPTION, BLACK_BOX_VIOLATION,
            NO_PROGRESS, COMPILE_ERROR
        }

        public final Status status;
        /** The concrete test case, when generated. */
        public final List<Stmt> concrete;
        public final List<Round> rounds;
        public final String detail;

        Result(Status status, List<Stmt> concrete, List<Round> rounds, String detail) {
            this.status = status;
            this.concrete = concrete;
            this.rounds = Collections.unmodifiableList(new ArrayList<Round>(rounds));
            this.detail = detail;
        }

        /** genCTC rounds that turned inputs concrete: one per call with arguments. */
        public int concretisingRounds() {
            int n = 0;
            for (Round r : rounds) if (!r.values.isEmpty()) n++;
            return n;
        }

        /** symex calls, i.e. SPF runs. */
        public int symexRuns() { return rounds.size(); }

        public long engineMillis() {
            long ms = 0;
            for (Round r : rounds) ms += r.solution.millis;
            return ms;
        }
    }

    public static Result genCTC(List<Stmt> t, List<String> L, Sigma sigma, SymexEngine engine) {
        return genCTC(t, L, sigma, engine, new ArrayList<Round>());
    }

    private static Result genCTC(List<Stmt> t, List<String> L, Sigma sigma, SymexEngine engine,
                                 List<Round> rounds) {
        if (!isAbstract(t)) return new Result(Result.Status.GENERATED, t, rounds, "");
        List<Stmt> t1 = rewriteATC(t, L);
        Solution L1 = symex(t1, sigma, engine);
        rounds.add(new Round(L, t1, L1));
        switch (L1.kind) {
            case NO_SOLUTION: {
                Stmt.Assume a = L1.blocking;
                String detail = a == null ? "INCORRECT SEQUENCE: no path satisfies the preconditions"
                        : "INCORRECT SEQUENCE: call " + a.block.getIndex() + " (" + a.block.getOperationName()
                          + "): " + a.clause.getOriginalText();
                return new Result(Result.Status.INCORRECT_SEQUENCE, null, rounds, detail);
            }
            case VALUES:
                if (L1.values.isEmpty() && isAbstract(t1)) {
                    return new Result(Result.Status.NO_PROGRESS, null, rounds,
                            "NO PROGRESS: symex turned no input concrete, but t' still has input commands");
                }
                return genCTC(t1, L1.values, sigma, engine, rounds);
            default:
                return new Result(Result.Status.valueOf(L1.kind.name()), null, rounds, L1.describe());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // printing
    // ─────────────────────────────────────────────────────────────────────────

    /** The test case, one statement per line. */
    public static String print(List<Stmt> t) {
        StringBuilder sb = new StringBuilder();
        for (Stmt s : t) sb.append(s).append('\n');
        return sb.toString();
    }

    /** The ATC, every round's t' and L', and the concrete test: the generation trace. */
    public static String trace(TestPlan plan, List<Stmt> atc, Result result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(plan.getSpecFile()).append(" + ").append(plan.getTestsFile())
          .append(" (").append(plan.getLibraryKey()).append(")\n\n");
        sb.append("== ATC t ==\n").append(print(atc));
        int k = 0;
        for (Round r : result.rounds) {
            sb.append("\n== round ").append(k++).append(": t' = rewriteATC(t, ").append(r.values)
              .append(") ==\n").append(print(r.rewritten));
            Prefix p = r.solution.prefix;
            sb.append("-- symex: ").append(p.statements.size()).append(" ready statement(s), ")
              .append(p.firstNotReady == null ? "all of t' ran"
                      : "stopped at " + p.firstNotReady).append('\n');
            sb.append("-- pc = ").append(p.pc).append('\n');
            sb.append("-- L' = ").append(r.solution.describe()).append('\n');
        }
        if (result.concrete != null) {
            sb.append("\n== concrete test case ==\n").append(print(result.concrete));
        } else {
            sb.append("\n== no test: ").append(result.detail).append(" ==\n");
        }
        return sb.toString();
    }
}

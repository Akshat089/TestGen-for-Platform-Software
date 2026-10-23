package in.ac.iiitb.plproject.ir;

import in.ac.iiitb.plproject.check.InputChecker;
import in.ac.iiitb.plproject.parser.ast.FunctionSignature;
import in.ac.iiitb.plproject.parser.ast.JmlFunctionSpec;
import in.ac.iiitb.plproject.parser.ast.JmlSpecAst;
import in.ac.iiitb.plproject.parser.ast.TestStringAst;
import in.ac.iiitb.plproject.parser.ast.Variable;
import in.ac.iiitb.plproject.symbolic.TypeSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contract + sequence -&gt; {@link TestPlan}, the one shared IR the ATC, every
 * SPF driver and the JUnit test are printed from.
 *
 * <p>Runs the input checks first; a {@link in.ac.iiitb.plproject.check.GenatcInputException}
 * aborts before anything is built. Every decision here (clause text, snapshots,
 * which parameters are pooled) comes from the {@code .spec} text alone; the
 * library is never read.
 */
public final class IrBuilder {

    private IrBuilder() {}

    /** A state variable a key can be looked up in: a {@code Set<...>} or {@code Map<...>}. */
    private static final Pattern KEYED_COLLECTION = Pattern.compile("^\\s*(Set|Map)\\s*<");

    /** A pooled number is {@code NUMERIC_POOL_BASE + NUMERIC_POOL_STEP * index}: 101, 103, 105. */
    public static final int NUMERIC_POOL_BASE = 101;
    public static final int NUMERIC_POOL_STEP = 2;

    public static TestPlan build(JmlSpecAst spec, TestStringAst test, String specFile, String testsFile) {
        InputChecker.check(spec, test, specFile, testsFile);

        // One shared literal pool per pooled TYPE, so e.g. a `String` added by one
        // call in the sequence can be the same literal a later call's precondition
        // requires to already be present (StringSet's add -> contains -> remove).
        Map<String, List<String>> poolsByType = new HashMap<String, List<String>>();

        List<CallBlock> blocks = new ArrayList<CallBlock>();
        List<String> calls = test.getCalls();
        for (int i = 0; i < calls.size(); i++) {
            JmlFunctionSpec fn = spec.findSpecFor(calls.get(i));
            blocks.add(buildBlock(i, fn, spec.getStateVars(), poolsByType));
        }

        return new TestPlan(test.getName(), specFile, testsFile,
                !spec.getStateVars().isEmpty(), spec.getStateVars(), blocks);
    }

    private static CallBlock buildBlock(int i, JmlFunctionSpec fn, Map<String, String> stateVars,
                                         Map<String, List<String>> poolsByType) {
        Set<String> stateVarNames = stateVars.keySet();
        FunctionSignature sig = fn.getSignature();
        List<Variable> declaredParams = (sig != null && sig.getParameters() != null)
                ? sig.getParameters() : new ArrayList<Variable>();

        List<ParamSlot> slots = new ArrayList<ParamSlot>();
        Map<String, String> paramSlotNames = new LinkedHashMap<String, String>();
        for (Variable p : declaredParams) {
            ParamSlot slot = buildSlot(i, p, fn, stateVars, poolsByType);
            slots.add(slot);
            paramSlotNames.put(p.getName(), slot.getSlotName());
        }

        String returnType = fn.getReturnTypeName();
        boolean hasReturn = returnType != null && !returnType.trim().isEmpty()
                && !"void".equals(returnType.trim());
        String resultAlias = fn.getResultBinding();
        String ensuresText = fn.getEnsuresText();
        boolean storesResult = hasReturn
                && (resultAlias != null || ClauseText.containsResultToken(ensuresText));
        String resultVarName = storesResult ? "c" + i + "_result" : null;

        Set<String> oldVarNames = ClauseText.oldNames(ensuresText);
        final Map<String, String> snapshotNames = new LinkedHashMap<String, String>();
        for (String v : oldVarNames) snapshotNames.put(v, "old" + i + "_" + v);
        Function<String, String> snapshotNameOf = new Function<String, String>() {
            @Override public String apply(String v) { return snapshotNames.get(v); }
        };

        List<Snapshot> snapshots = new ArrayList<Snapshot>();
        for (Map.Entry<String, String> e : snapshotNames.entrySet()) {
            snapshots.add(new Snapshot(e.getKey(), stateVars.get(e.getKey()), e.getValue()));
        }

        List<Clause> preconditions = new ArrayList<Clause>();
        for (String conjunct : ClauseText.splitTopLevelAnd(fn.getRequiresText())) {
            ClauseText.Rewritten r = ClauseText.rewrite(conjunct, paramSlotNames, stateVarNames,
                    null, null, snapshotNameOf);
            preconditions.add(new Clause(conjunct, r.java, false));
        }

        List<Clause> postconditions = new ArrayList<Clause>();
        for (String clauseText : ClauseText.splitTopLevelAnd(ensuresText)) {
            ClauseText.Rewritten r = ClauseText.rewrite(clauseText, paramSlotNames, stateVarNames,
                    resultAlias, resultVarName, snapshotNameOf);
            postconditions.add(new Clause(clauseText, r.java, namesOnlyTheResult(clauseText, resultAlias)));
        }

        return new CallBlock(i, fn.getName(), hasReturn ? returnType.trim() : "void",
                slots, preconditions, snapshots, storesResult, resultVarName, resultAlias,
                postconditions);
    }

    /** {@code \result == poppedElem}: both sides are the result, so it asserts nothing. */
    static boolean namesOnlyTheResult(String clauseText, String resultAlias) {
        String side = "(?:\\\\result" + (resultAlias != null ? "|" + Pattern.quote(resultAlias) : "") + ")";
        return clauseText.trim().matches(side + "\\s*==\\s*" + side);
    }

    /**
     * A parameter is pooled when it is a {@code String}, or when the call's own
     * contract uses it as the key/element of a {@code Set}/{@code Map} state
     * variable ({@code Keys.contains(key)}, {@code M.get(key)}, ...): a fully
     * symbolic number reaching {@code java.util.HashMap}'s bit-spreading
     * {@code hashCode()} puts a shift into the path condition that neither z3
     * nor choco accepts (confirmed by experiment: "Choco does not support
     * bitwise SHIFT" / "Error Z3"). Any other number is symbolic.
     */
    private static ParamSlot buildSlot(int callIndex, Variable param, JmlFunctionSpec fn,
                                        Map<String, String> stateVars, Map<String, List<String>> poolsByType) {
        String type = param.getTypeName();
        List<String> keyedIn = new ArrayList<String>();
        List<String> membership = new ArrayList<String>();
        for (Map.Entry<String, String> e : stateVars.entrySet()) {
            if (!KEYED_COLLECTION.matcher(e.getValue()).find()) continue;
            if (usedAsKey(fn.getRequiresText(), e.getKey(), param.getName())) {
                keyedIn.add(e.getKey());
                membership.add(e.getKey());
            } else if (usedAsKey(fn.getEnsuresText(), e.getKey(), param.getName())) {
                keyedIn.add(e.getKey());
            }
        }

        String poolReason = null;
        ParamKind kind;
        if (TypeSupport.isPooledType(type)) {
            kind = ParamKind.POOLED;
            poolReason = "SPF's string support is weak; pooled as a concrete literal";
        } else if (!keyedIn.isEmpty() && TypeSupport.isSymbolicallySolvable(type)) {
            kind = ParamKind.POOLED;
            poolReason = "used as a key of " + keyedIn + " — a fully symbolic " + type
                    + " breaks both configured solvers' hashCode() handling";
        } else {
            kind = ParamKind.SYMBOLIC;
        }

        List<String> pool = java.util.Collections.emptyList();
        if (kind == ParamKind.POOLED) {
            pool = poolsByType.computeIfAbsent(type.trim(), t -> freshPool(t));
        } else {
            membership.clear();
        }
        return new ParamSlot(callIndex, param.getName(), type, kind, poolReason, pool, membership);
    }

    /** True when {@code text} contains {@code collection.<lookup>(param)}. */
    static boolean usedAsKey(String text, String collection, String param) {
        if (text == null || text.isEmpty()) return false;
        return Pattern.compile("(?<![A-Za-z0-9_.])" + Pattern.quote(collection)
                + "\\s*\\.\\s*(contains|containsKey|get|put|add|remove)\\s*\\(\\s*"
                + Pattern.quote(param) + "\\s*[,)]").matcher(text).find();
    }

    private static List<String> freshPool(String type) {
        List<String> values = new ArrayList<String>();
        for (int k = 0; k < TypeSupport.POOL_SIZE; k++) {
            if ("String".equals(type)) {
                values.add("s" + (k + 1));
            } else {
                values.add(String.valueOf(NUMERIC_POOL_BASE + k * NUMERIC_POOL_STEP)); // 101, 103, 105
            }
        }
        return values;
    }
}

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
 * Step 2: contract + sequence -&gt; {@link TestPlan}, the one shared IR both
 * printers read.
 *
 * <p>Runs the input checks (rule 13) first; a {@link in.ac.iiitb.plproject.check.GenatcInputException}
 * aborts before anything is built. What is built has NO propagation (rule 1):
 * a call's return value is never threaded into a later call's argument — every
 * parameter is either genuinely symbolic or drawn from a small concrete pool
 * (rule 9), decided purely from the {@code .spec} text, never from the library
 * (rule 12).
 */
public final class IrBuilder {

    private IrBuilder() {}

    private static final Pattern SET_TYPE = Pattern.compile("Set<\\s*([^,<>]+)\\s*>");
    private static final Pattern MAP_TYPE = Pattern.compile("Map<\\s*([^,<>]+)\\s*,\\s*([^,<>]+)\\s*>");
    private static final Pattern HASH_LOOKUP = Pattern.compile(
            "\\.(contains|containsKey|get|put|add|remove)\\s*\\(");

    public static TestPlan build(JmlSpecAst spec, TestStringAst test, String specFile, String testsFile) {
        InputChecker.check(spec, test, specFile, testsFile);

        Set<String> stateVarNames = spec.getStateVars().keySet();
        Set<String> hashKeyTypes = collectHashKeyTypes(spec);

        // One shared literal pool per pooled TYPE, so e.g. a `String` added by one
        // call in the sequence can be the same literal a later call's precondition
        // requires to already be present (StringSet's add -> contains -> remove).
        Map<String, List<String>> poolsByType = new HashMap<String, List<String>>();

        List<CallBlock> blocks = new ArrayList<CallBlock>();
        List<String> calls = test.getCalls();
        for (int i = 0; i < calls.size(); i++) {
            JmlFunctionSpec fn = spec.findSpecFor(calls.get(i));
            blocks.add(buildBlock(i, fn, spec.getStateVars(), hashKeyTypes, poolsByType));
        }

        return new TestPlan(test.getName(), specFile, testsFile,
                !spec.getStateVars().isEmpty(), spec.getStateVars(), blocks);
    }

    private static CallBlock buildBlock(int i, JmlFunctionSpec fn, Map<String, String> stateVars,
                                         Set<String> hashKeyTypes, Map<String, List<String>> poolsByType) {
        Set<String> stateVarNames = stateVars.keySet();
        FunctionSignature sig = fn.getSignature();
        List<Variable> declaredParams = (sig != null && sig.getParameters() != null)
                ? sig.getParameters() : new ArrayList<Variable>();

        List<ParamSlot> slots = new ArrayList<ParamSlot>();
        Map<String, String> paramSlotNames = new LinkedHashMap<String, String>();
        for (Variable p : declaredParams) {
            ParamSlot slot = buildSlot(i, p, hashKeyTypes, poolsByType);
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
            boolean symbolic = mentionsSymbolic(r.referencedParams, slots);
            preconditions.add(new Clause(conjunct, r.java, symbolic));
        }

        List<Clause> postconditions = new ArrayList<Clause>();
        for (String clauseText : ClauseText.splitTopLevelAnd(ensuresText)) {
            ClauseText.Rewritten r = ClauseText.rewrite(clauseText, paramSlotNames, stateVarNames,
                    resultAlias, resultVarName, snapshotNameOf);
            boolean symbolic = mentionsSymbolic(r.referencedParams, slots);
            postconditions.add(new Clause(clauseText, r.java, symbolic));
        }

        return new CallBlock(i, fn.getName(), hasReturn ? returnType.trim() : "void",
                slots, preconditions, snapshots, storesResult, resultVarName, resultAlias,
                postconditions);
    }

    private static boolean mentionsSymbolic(Set<String> referencedParamNames, List<ParamSlot> slots) {
        for (ParamSlot slot : slots) {
            if (referencedParamNames.contains(slot.getParamName()) && slot.isSymbolic()) return true;
        }
        return false;
    }

    /**
     * Every TYPE a spec uses as the element of a {@code Set&lt;T&gt;} or the key of
     * a {@code Map&lt;K,V&gt;} state variable that some function's requires/ensures
     * text actually looks up by key ({@code .contains(}, {@code .containsKey(},
     * {@code .get(}, {@code .put(}, {@code .add(}, {@code .remove(}). A parameter
     * of exactly this type is pooled rather than solved (rule 9): a fully symbolic
     * value reaching {@code java.util.HashMap}/{@code HashSet}'s bit-spread
     * {@code hashCode()} breaks both configured solvers, confirmed by direct
     * experiment while building this (bst's {@code int key} into
     * {@code Set<Integer> Keys} is the one library this augments beyond the
     * String-typed cases rule 9 names explicitly).
     */
    static Set<String> collectHashKeyTypes(JmlSpecAst spec) {
        Map<String, String> declared = spec.getStateVars();
        Set<String> result = new LinkedHashSet<String>();
        for (Map.Entry<String, String> e : declared.entrySet()) {
            String varName = e.getKey();
            String type = e.getValue();
            String keyType = null;
            Matcher setM = SET_TYPE.matcher(type);
            if (setM.find()) keyType = setM.group(1).trim();
            Matcher mapM = MAP_TYPE.matcher(type);
            if (mapM.find()) keyType = mapM.group(1).trim();
            if (keyType == null) continue;

            boolean lookedUp = false;
            for (JmlFunctionSpec fn : spec.getSpecs()) {
                if (usesHashLookup(fn.getRequiresText(), varName) || usesHashLookup(fn.getEnsuresText(), varName)) {
                    lookedUp = true;
                    break;
                }
            }
            if (lookedUp) result.add(keyType);
        }
        return result;
    }

    private static boolean usesHashLookup(String text, String varName) {
        if (text == null || text.isEmpty()) return false;
        int idx = text.indexOf(varName + ".");
        while (idx >= 0) {
            Matcher m = HASH_LOOKUP.matcher(text);
            m.region(idx + varName.length(), text.length());
            if (m.lookingAt()) return true;
            idx = text.indexOf(varName + ".", idx + 1);
        }
        return false;
    }

    private static ParamSlot buildSlot(int callIndex, Variable param, Set<String> hashKeyTypes,
                                        Map<String, List<String>> poolsByType) {
        String type = param.getTypeName();
        String poolReason = null;
        ParamKind kind;
        boolean isHashKeyType = !java.util.Collections.disjoint(hashKeyTypes, TypeSupport.withBoxedEquivalent(type));
        if (isHashKeyType && TypeSupport.isSymbolicallySolvable(type)) {
            kind = ParamKind.POOLED;
            poolReason = "used as a Set/Map key — a fully symbolic " + type
                    + " breaks both configured solvers' hashCode() handling";
        } else if (TypeSupport.isPooledType(type)) {
            kind = ParamKind.POOLED;
            poolReason = "SPF's string support is weak; pooled as a concrete literal";
        } else {
            kind = ParamKind.SYMBOLIC;
        }

        List<String> pool = java.util.Collections.emptyList();
        if (kind == ParamKind.POOLED) {
            pool = poolsByType.computeIfAbsent(type.trim(), t -> freshPool(t));
        }
        return new ParamSlot(callIndex, param.getName(), type, kind, poolReason, pool);
    }

    private static List<String> freshPool(String type) {
        List<String> values = new ArrayList<String>();
        for (int k = 0; k < TypeSupport.POOL_SIZE; k++) {
            if ("String".equals(type)) {
                values.add("s" + (k + 1));
            } else {
                values.add(String.valueOf(101 + k * 2)); // 101, 103, 105 — distinct, non-zero
            }
        }
        return values;
    }
}

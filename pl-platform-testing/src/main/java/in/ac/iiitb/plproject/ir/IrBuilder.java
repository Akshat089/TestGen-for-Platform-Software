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

    /** A pooled number is {@code NUMERIC_POOL_BASE + NUMERIC_POOL_STEP * index}: 101, 103, 105. */
    public static final int NUMERIC_POOL_BASE = 101;
    public static final int NUMERIC_POOL_STEP = 2;

    private static final Pattern OLD_CALL = Pattern.compile("\\\\old\\s*\\(\\s*[A-Za-z_][A-Za-z0-9_]*\\s*\\)");

    public static TestPlan build(JmlSpecAst spec, TestStringAst test, String specFile, String testsFile) {
        InputChecker.check(spec, test, specFile, testsFile);

        Set<String> hashKeyTypes = collectHashKeyTypes(spec);

        // One shared literal pool per pooled TYPE, so e.g. a `String` added by one
        // call in the sequence can be the same literal a later call's precondition
        // requires to already be present (StringSet's add -> contains -> remove).
        Map<String, List<String>> poolsByType = new HashMap<String, List<String>>();
        List<String> stringPool = poolsByType.computeIfAbsent("String", t -> freshPool(t));

        Map<String, ModelTranslator.StateModel> model = ModelTranslator.modelState(
                spec.getStateVars(), spec.getStateInitializers(), hashKeyTypes);
        List<String> startDecls = new ArrayList<String>();
        for (ModelTranslator.StateModel s : model.values()) {
            startDecls.addAll(ModelTranslator.startDecls(s, stringPool));
        }

        // state name -> the model version a bare name currently reads (0 = starting value)
        Map<String, Integer> current = new LinkedHashMap<String, Integer>();
        for (String name : model.keySet()) current.put(name, 0);

        List<CallBlock> blocks = new ArrayList<CallBlock>();
        List<String> calls = test.getCalls();
        for (int i = 0; i < calls.size(); i++) {
            JmlFunctionSpec fn = spec.findSpecFor(calls.get(i));
            blocks.add(buildBlock(i, fn, spec.getStateVars(), model, current, hashKeyTypes, poolsByType,
                    stringPool));
        }

        return new TestPlan(test.getName(), specFile, testsFile,
                !spec.getStateVars().isEmpty(), spec.getStateVars(), startDecls, blocks);
    }

    private static CallBlock buildBlock(int i, JmlFunctionSpec fn, Map<String, String> stateVars,
                                         Map<String, ModelTranslator.StateModel> model,
                                         Map<String, Integer> current, Set<String> hashKeyTypes,
                                         Map<String, List<String>> poolsByType, List<String> stringPool) {
        Set<String> stateVarNames = stateVars.keySet();
        FunctionSignature sig = fn.getSignature();
        List<Variable> declaredParams = (sig != null && sig.getParameters() != null)
                ? sig.getParameters() : new ArrayList<Variable>();

        List<ParamSlot> slots = new ArrayList<ParamSlot>();
        Map<String, String> paramSlotNames = new LinkedHashMap<String, String>();
        Map<String, ParamSlot> paramsByName = new LinkedHashMap<String, ParamSlot>();
        List<String> paramDecls = new ArrayList<String>();
        for (Variable p : declaredParams) {
            ParamSlot slot = buildSlot(i, p, hashKeyTypes, poolsByType);
            slots.add(slot);
            paramSlotNames.put(p.getName(), slot.getSlotName());
            paramsByName.put(p.getName(), slot);
            paramDecls.add(paramDecl(slot));
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

        // (b) the precondition reads the model as it stands before the call
        ModelTranslator.Scope preScope = new ModelTranslator.Scope(model, current, null, paramsByName,
                null, null, stringPool);
        List<Clause> preconditions = new ArrayList<Clause>();
        for (String conjunct : ClauseText.splitTopLevelAnd(fn.getRequiresText())) {
            ClauseText.Rewritten r = ClauseText.rewrite(conjunct, paramSlotNames, stateVarNames,
                    null, null, snapshotNameOf);
            preconditions.add(clause(conjunct, r.java, preScope, false));
        }

        // (c) fresh values: the result, and every state variable the postcondition
        // names outside \old(...). Anything it does not name keeps its value (frame rule).
        List<String> freshDecls = new ArrayList<String>();
        ModelTranslator.Term resultTerm = null;
        if (hasReturn) {
            String rv = "c" + i + "_result";
            freshDecls.addAll(resultDecl(returnType.trim(), rv));
            resultTerm = ModelTranslator.Term.resultOf(returnType, rv);
        }
        Map<String, Integer> before = new LinkedHashMap<String, Integer>(current);
        Set<String> changed = ClauseText.freeIdentifiers(OLD_CALL.matcher(ensuresText == null ? "" : ensuresText)
                .replaceAll(" "));
        for (ModelTranslator.StateModel s : model.values()) {
            if (!changed.contains(s.name) || s.shape == ModelTranslator.Shape.UNSUPPORTED) continue;
            current.put(s.name, i + 1);
        }
        ModelTranslator.Scope postScope = new ModelTranslator.Scope(model,
                new LinkedHashMap<String, Integer>(current), before, paramsByName, resultAlias, resultTerm,
                stringPool);
        List<String> ensuresClauses = new ArrayList<String>();
        for (String c : ClauseText.splitTopLevelAnd(ensuresText)) {
            ensuresClauses.add(OLD_CALL.matcher(c).replaceAll(" 0 "));
        }
        for (ModelTranslator.StateModel s : model.values()) {
            if (!changed.contains(s.name) || s.shape == ModelTranslator.Shape.UNSUPPORTED) continue;
            freshDecls.addAll(ModelTranslator.freshCollection(s, i + 1, ensuresClauses, postScope));
        }

        // (d) the postcondition relates the fresh values to the old ones
        List<Clause> postconditions = new ArrayList<Clause>();
        for (String clauseText : ClauseText.splitTopLevelAnd(ensuresText)) {
            ClauseText.Rewritten r = ClauseText.rewrite(clauseText, paramSlotNames, stateVarNames,
                    resultAlias, resultVarName, snapshotNameOf);
            boolean tautological = namesOnlyTheResult(clauseText, resultAlias);
            postconditions.add(tautological ? new Clause(clauseText, r.java, null, null, true)
                                            : clause(clauseText, r.java, postScope, false));
        }

        return new CallBlock(i, fn.getName(), hasReturn ? returnType.trim() : "void",
                slots, preconditions, snapshots, storesResult, resultVarName, resultAlias,
                postconditions, paramDecls, freshDecls);
    }

    private static Clause clause(String text, String java, ModelTranslator.Scope scope, boolean tautological) {
        try {
            return new Clause(text, java, ModelTranslator.translate(text, scope), null, tautological);
        } catch (ModelTranslator.Unmodellable e) {
            return new Clause(text, java, null, e.getMessage(), tautological);
        }
    }

    /** {@code \result == poppedElem}: both sides are the result, so it asserts nothing. */
    static boolean namesOnlyTheResult(String clauseText, String resultAlias) {
        String side = "(?:\\\\result" + (resultAlias != null ? "|" + Pattern.quote(resultAlias) : "") + ")";
        return clauseText.trim().matches(side + "\\s*==\\s*" + side);
    }

    /** The driver line declaring one parameter (step a). */
    private static String paramDecl(ParamSlot slot) {
        String v = slot.getSlotName();
        if (slot.isPooled()) {
            // SPF chooses the pool index; the extractor maps it back to the literal.
            return "int " + v + " = index(\"" + v + "\");";
        }
        String t = TypeSupport.unboxed(slot.getType());
        return t + " " + v + " = " + TypeSupport.symbolicFactory(t) + "(\"" + v + "\");";
    }

    /** The driver lines declaring a call's fresh result. */
    private static List<String> resultDecl(String type, String var) {
        if (TypeSupport.isPrimitive(type)) {
            List<String> out = new ArrayList<String>();
            out.add(type + " " + var + " = " + TypeSupport.symbolicFactory(type) + "(\"" + var + "\");");
            return out;
        }
        return ModelTranslator.freshRef(var, "String".equals(type) ? "int" : TypeSupport.unboxed(type),
                "String".equals(type));
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
                values.add(String.valueOf(NUMERIC_POOL_BASE + k * NUMERIC_POOL_STEP)); // 101, 103, 105
            }
        }
        return values;
    }
}

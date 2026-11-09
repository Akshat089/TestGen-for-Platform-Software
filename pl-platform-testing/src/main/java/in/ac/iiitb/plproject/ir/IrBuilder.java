package in.ac.iiitb.plproject.ir;

import in.ac.iiitb.plproject.check.InputChecker;
import in.ac.iiitb.plproject.parser.ast.FunctionSignature;
import in.ac.iiitb.plproject.parser.ast.JmlFunctionSpec;
import in.ac.iiitb.plproject.parser.ast.JmlSpecAst;
import in.ac.iiitb.plproject.parser.ast.TestStringAst;
import in.ac.iiitb.plproject.parser.ast.Variable;
import in.ac.iiitb.plproject.symbolic.IntegerModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Contract + sequence -&gt; {@link TestPlan}, the one shared IR the ATC, every
 * SPF driver and the JUnit test are printed from.
 *
 * <p>Runs the input checks first; a {@link in.ac.iiitb.plproject.check.GenatcInputException}
 * aborts before anything is built. Every decision here (clause text, snapshots,
 * which parameters are strings) comes from the {@code .spec} text alone; the
 * library is never read.
 */
public final class IrBuilder {

    private IrBuilder() {}

    public static TestPlan build(JmlSpecAst spec, TestStringAst test, String specFile, String testsFile) {
        InputChecker.check(spec, test, specFile, testsFile);

        List<CallBlock> blocks = new ArrayList<CallBlock>();
        List<String> calls = test.getCalls();
        for (int i = 0; i < calls.size(); i++) {
            JmlFunctionSpec fn = spec.findSpecFor(calls.get(i));
            blocks.add(buildBlock(i, fn, spec.getStateVars()));
        }

        return new TestPlan(test.getName(), specFile, testsFile,
                !spec.getStateVars().isEmpty(), spec.getStateVars(), blocks);
    }

    private static CallBlock buildBlock(int i, JmlFunctionSpec fn, Map<String, String> stateVars) {
        Set<String> stateVarNames = stateVars.keySet();
        FunctionSignature sig = fn.getSignature();
        List<Variable> declaredParams = (sig != null && sig.getParameters() != null)
                ? sig.getParameters() : new ArrayList<Variable>();

        List<ParamSlot> slots = new ArrayList<ParamSlot>();
        Map<String, String> paramSlotNames = new LinkedHashMap<String, String>();
        for (Variable p : declaredParams) {
            ParamSlot slot = buildSlot(i, p);
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
     * A String parameter is an integer id plus a null flag; any other
     * parameter is a symbolic number or boolean. Neither depends on how the
     * contract uses it: a membership test on a symbolic key is expanded over
     * the collection's concrete contents when the driver is printed.
     */
    private static ParamSlot buildSlot(int callIndex, Variable param) {
        String type = param.getTypeName();
        ParamKind kind = IntegerModel.isString(type) ? ParamKind.STRING_ID : ParamKind.SYMBOLIC;
        return new ParamSlot(callIndex, param.getName(), type, kind);
    }
}

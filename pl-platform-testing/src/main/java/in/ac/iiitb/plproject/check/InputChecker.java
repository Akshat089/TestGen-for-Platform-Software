package in.ac.iiitb.plproject.check;

import in.ac.iiitb.plproject.ir.ClauseText;
import in.ac.iiitb.plproject.parser.ast.FunctionSignature;
import in.ac.iiitb.plproject.parser.ast.JmlFunctionSpec;
import in.ac.iiitb.plproject.parser.ast.JmlSpecAst;
import in.ac.iiitb.plproject.parser.ast.TestStringAst;
import in.ac.iiitb.plproject.parser.ast.Variable;
import in.ac.iiitb.plproject.symbolic.TypeSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Rule 13: reject, with the input file and line, before anything is
 * generated:
 *
 * <ul>
 *   <li>unknown operation in the sequence;</li>
 *   <li>duplicate contract (two {@code spec} blocks with the same name);</li>
 *   <li>a name in a condition that is not a parameter, a state variable, or
 *       {@code \result} (or its alias);</li>
 *   <li>{@code \result} or {@code \old} in a precondition;</li>
 *   <li>{@code \old} on a name that is not a state variable;</li>
 *   <li>a type with no symbolic or concrete handling.</li>
 * </ul>
 */
public final class InputChecker {

    private InputChecker() {}

    /** Checks one {@code .spec} file's AST. Returns every problem found, empty if none. */
    public static List<String> checkSpec(JmlSpecAst spec, String specFile) {
        List<String> problems = new ArrayList<String>();
        Set<String> stateVars = spec.getStateVars().keySet();
        Set<String> seenNames = new HashSet<String>();

        for (JmlFunctionSpec fn : spec.getSpecs()) {
            String where = (specFile != null ? specFile : "<spec>") + ":" + fn.getLineNumber();

            if (!seenNames.add(fn.getName())) {
                problems.add(where + ": duplicate contract '" + fn.getName()
                        + "' — a spec block with this name already appeared earlier in the file");
            }

            FunctionSignature sig = fn.getSignature();
            Set<String> paramNames = new LinkedHashSet<String>();
            if (sig != null && sig.getParameters() != null) {
                for (Variable p : sig.getParameters()) {
                    paramNames.add(p.getName());
                    if (TypeSupport.isUnsupportedType(p.getTypeName())) {
                        problems.add(where + ": " + fn.getName() + "(" + p.getName() + "): type '"
                                + p.getTypeName() + "' has neither a symbolic nor a concrete handling");
                    }
                }
                if (sig.getReturnTypeName() != null && !"void".equals(sig.getReturnTypeName().trim())
                        && TypeSupport.isUnsupportedType(sig.getReturnTypeName())) {
                    problems.add(where + ": " + fn.getName() + "(): return type '"
                            + sig.getReturnTypeName() + "' has neither a symbolic nor a concrete handling");
                }
            }

            String requires = fn.getRequiresText();
            String ensures = fn.getEnsuresText();
            String resultAlias = fn.getResultBinding();

            // \result or \old in a precondition.
            if (ClauseText.containsResultToken(requires)) {
                problems.add(where + ": " + fn.getName()
                        + ": \\result is not allowed in a precondition — requires: " + requires);
            }
            if (ClauseText.containsOld(requires)) {
                problems.add(where + ": " + fn.getName()
                        + ": \\old is not allowed in a precondition — requires: " + requires);
            }

            // \old on a non-state name, in the postcondition (the only place \old may appear).
            for (String oldName : ClauseText.oldNames(ensures)) {
                if (!stateVars.contains(oldName)) {
                    problems.add(where + ": " + fn.getName() + ": \\old(" + oldName
                            + ") — '" + oldName + "' is not a declared state variable");
                }
            }

            // Every free identifier in requires/ensures must be a parameter, a state
            // variable, or \result's alias (bare "result" is also accepted as a
            // courtesy for specs that write it without the backslash).
            Set<String> allowed = new HashSet<String>(paramNames);
            allowed.addAll(stateVars);
            allowed.add("result");
            if (resultAlias != null) allowed.add(resultAlias);

            for (String conjunct : ClauseText.splitTopLevelAnd(requires)) {
                checkFreeIdentifiers(conjunct, allowed, where, fn.getName(), "requires", problems);
            }
            for (String clause : ClauseText.splitTopLevelAnd(stripOld(ensures))) {
                checkFreeIdentifiers(clause, allowed, where, fn.getName(), "ensures", problems);
            }
        }
        return problems;
    }

    /** Checks a {@code .tests} sequence against the spec it will run against. */
    public static List<String> checkSequence(JmlSpecAst spec, TestStringAst test, String testsFile) {
        List<String> problems = new ArrayList<String>();
        String file = testsFile != null ? testsFile : "<tests>";
        int line = test.getSequenceLineNumber();
        String where = file + (line >= 0 ? ":" + line : "");

        List<String> calls = test.getCalls();
        if (calls.isEmpty()) {
            problems.add(where + ": the sequence is empty");
            return problems;
        }
        for (String call : calls) {
            if (spec.findSpecFor(call) == null) {
                problems.add(where + ": unknown operation '" + call
                        + "' in sequence — no spec declares a function with this name");
            }
        }
        return problems;
    }

    /** Runs both checks and throws if either found a problem. */
    public static void check(JmlSpecAst spec, TestStringAst test, String specFile, String testsFile) {
        List<String> problems = new ArrayList<String>();
        problems.addAll(checkSpec(spec, specFile));
        problems.addAll(checkSequence(spec, test, testsFile));
        if (!problems.isEmpty()) throw new GenatcInputException(problems);
    }

    private static void checkFreeIdentifiers(String text, Set<String> allowed, String where,
                                              String fnName, String clauseKind,
                                              List<String> problems) {
        for (String id : ClauseText.freeIdentifiers(text)) {
            if (id.equals("true") || id.equals("false") || id.equals("null")) continue;
            if (!allowed.contains(id)) {
                problems.add(where + ": " + fnName + " " + clauseKind + ": '" + id
                        + "' is not a parameter, a state variable, or \\result (or its alias) — "
                        + clauseKind + ": " + text);
            }
        }
    }

    /** Strips \old(name) down to just name, so the free-identifier scan sees the name underneath. */
    private static String stripOld(String text) {
        return text.replaceAll("\\\\old\\s*\\(\\s*([A-Za-z_][A-Za-z0-9_]*)\\s*\\)", "$1");
    }
}

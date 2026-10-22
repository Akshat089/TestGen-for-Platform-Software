package in.ac.iiitb.plproject.spf.listener;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Step 5's data source: at the end of every COMPLETED path, solves the path
 * condition and writes one JSON line naming every symbolic variable the
 * solver assigned a value — {@code {"event":"path","path":N,"sat":true,
 * "vars":{"c0_elem$idx":"0", ...}}}. This is the whole of rule 7: no console
 * text is parsed anywhere downstream of this file, just this one JSON-lines
 * file, written directly from jpf-symbc's own {@link PathCondition} API
 * ({@code solve()} then {@code header.getVarVals(...)}) rather than its
 * human-readable report.
 *
 * <p>Also records when a search constraint (the configured depth bound) cuts
 * a branch short, via JPF's own {@link Search#getSearchConstraint()} — again
 * a structured getter, not scraped text — so a depth cutoff can be reported
 * distinctly from "no path reaches the end" (rule 10).
 *
 * <p>Deliberately generic: it has no idea what a "call block" or a "parameter
 * slot" is. {@code in.ac.iiitb.plproject.spf.ValueExtractor} maps the raw
 * {@code c<i>_<param>[$idx]} variable names in its output back to the IR.
 */
public class PathVectorListener extends PropertyListenerAdapter {

    private final PrintWriter out;
    private int pathId = 0;
    /** Highest line of the driver's main() at which SPF pruned a path, or -1. */
    private int deepestPruneLine = -1;
    /** Stop at the first completed, solved path: one input vector per sequence. */
    private final boolean stopAtFirstPath;

    public PathVectorListener(Config conf, JPF jpf) {
        this(conf);
    }

    public PathVectorListener(Config conf) {
        String path = conf.getString("genatc.output", "genatc-paths.jsonl");
        this.stopAtFirstPath = conf.getBoolean("genatc.stop_at_first_path", true);
        try {
            this.out = new PrintWriter(new FileWriter(path, false));
        } catch (IOException e) {
            throw new RuntimeException("PathVectorListener: cannot open " + path, e);
        }
    }

    @Override
    public void searchConstraintHit(Search search) {
        out.println("{\"event\":\"searchConstraintHit\",\"detail\":"
                + jsonString(search.getSearchConstraint()) + "}");
        out.flush();
    }

    /**
     * Remembers how far into the driver the search got before a path was
     * pruned. The driver is straight-line with one assumption per line, so
     * when NO path completes, the highest pruned line is the first clause
     * that cannot hold after everything before it: every earlier clause was
     * passed by some path, and no path got past this one.
     */
    @Override
    public void instructionExecuted(VM vm, ThreadInfo ti, Instruction next, Instruction executed) {
        if (!vm.getSystemState().isIgnored()) return;
        int line = -1;
        if (isDriverMain(executed.getMethodInfo())) {
            line = executed.getLineNumber();
        } else {
            for (StackFrame f = ti.getTopFrame(); f != null; f = f.getPrevious()) {
                if (isDriverMain(f.getMethodInfo())) {
                    line = f.getLine();
                    break;
                }
            }
        }
        if (line > deepestPruneLine) deepestPruneLine = line;
    }

    private static boolean isDriverMain(MethodInfo mi) {
        return mi != null && "main".equals(mi.getName()) && mi.getClassInfo() != null
                && mi.getClassInfo().getName().endsWith(".GeneratedATCs");
    }

    @Override
    public void stateAdvanced(Search search) {
        if (!search.isEndState()) return;

        // An end state that is a property violation (an uncaught exception, here)
        // is not a completed run of the sequence — solving its path condition can
        // still succeed (the PC up to the violation is whatever it is), but
        // printing it as a feasible path would offer a @Test that calls the
        // library with inputs SPF itself flagged as crashing it along some
        // execution, which is a different kind of finding than "this path
        // completes and its postconditions hold". Recorded separately so the
        // generator can at least count how many such states were seen; not
        // reported as a usable path.
        if (search.getCurrentError() != null) {
            out.println("{\"event\":\"error\",\"detail\":" + jsonString(search.getCurrentError().getDescription()) + "}");
            out.flush();
            return;
        }

        pathId++;
        VM vm = search.getVM();
        ChoiceGenerator<?> cg = vm.getChoiceGenerator();
        if (cg != null && !(cg instanceof PCChoiceGenerator)) {
            cg = cg.getPreviousChoiceGeneratorOfType(PCChoiceGenerator.class);
        }
        if (!(cg instanceof PCChoiceGenerator)) {
            // no symbolic decision on this path at all: nothing is constrained,
            // every value is free
            out.println("{\"event\":\"path\",\"path\":" + pathId + ",\"sat\":true,\"vars\":{}}");
            out.flush();
            if (stopAtFirstPath) search.terminate();
            return;
        }

        PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
        boolean sat = pc != null && pc.solve();
        Map<String, Object> vals = new LinkedHashMap<String, Object>();
        if (sat && pc.header != null) pc.header.getVarVals(vals);

        StringBuilder sb = new StringBuilder();
        sb.append("{\"event\":\"path\",\"path\":").append(pathId).append(",\"sat\":").append(sat)
          .append(",\"vars\":{");
        boolean first = true;
        for (Map.Entry<String, Object> e : vals.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append(jsonString(e.getKey())).append(":").append(jsonString(String.valueOf(e.getValue())));
        }
        sb.append("}}");
        out.println(sb);
        out.flush();
        if (sat && stopAtFirstPath) search.terminate();
    }

    @Override
    public void searchFinished(Search search) {
        out.println("{\"event\":\"searchFinished\",\"paths\":" + pathId
                + ",\"deepestPruneLine\":" + deepestPruneLine + "}");
        out.flush();
        out.close();
    }

    private static String jsonString(String s) {
        if (s == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\\') sb.append('\\').append(c);
            else if (c == '\n') sb.append("\\n");
            else sb.append(c);
        }
        return sb.append('"').toString();
    }
}

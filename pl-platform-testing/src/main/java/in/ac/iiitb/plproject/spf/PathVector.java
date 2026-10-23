package in.ac.iiitb.plproject.spf;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** One line of {@link in.ac.iiitb.plproject.spf.listener.PathVectorListener}'s output. */
public class PathVector {

    public enum Event { PATH, SEARCH_CONSTRAINT_HIT, SEARCH_FINISHED, ERROR }

    private final Event event;
    private final int pathId;
    private final boolean sat;
    private final Map<String, String> vars;
    private final Map<String, String> picks;
    private final String detail;
    private final int line;

    private PathVector(Event event, int pathId, boolean sat, Map<String, String> vars, Map<String, String> picks,
                       String detail, int line) {
        this.event = event;
        this.pathId = pathId;
        this.sat = sat;
        this.vars = Collections.unmodifiableMap(vars);
        this.picks = Collections.unmodifiableMap(picks);
        this.detail = detail;
        this.line = line;
    }

    public static PathVector path(int pathId, boolean sat, Map<String, String> vars, Map<String, String> picks) {
        return new PathVector(Event.PATH, pathId, sat, vars, picks, null, -1);
    }

    public static PathVector constraintHit(String detail) {
        return new PathVector(Event.SEARCH_CONSTRAINT_HIT, -1, false, new LinkedHashMap<String, String>(),
                new LinkedHashMap<String, String>(), detail, -1);
    }

    /** A property violation (an uncaught exception) at {@code line} of the driver, or -1. */
    public static PathVector error(String detail, int line) {
        return new PathVector(Event.ERROR, -1, false, new LinkedHashMap<String, String>(),
                new LinkedHashMap<String, String>(), detail, line);
    }

    public static PathVector finished(int totalPaths, int deepestPruneLine) {
        return new PathVector(Event.SEARCH_FINISHED, totalPaths, false, new LinkedHashMap<String, String>(),
                new LinkedHashMap<String, String>(), null, deepestPruneLine);
    }

    /** SEARCH_FINISHED only: the highest driver line a path was pruned at, or -1. */
    public int getDeepestPruneLine() { return event == Event.SEARCH_FINISHED ? line : -1; }

    /** ERROR only: the driver line that was executing, or -1. */
    public int getLine() { return line; }

    public Event getEvent() { return event; }
    public int getPathId() { return pathId; }
    public boolean isSat() { return sat; }
    /** The solver's value for each symbolic variable of the path condition. */
    public Map<String, String> getVars() { return vars; }
    /** The candidate each pooled input picked on this path: input name -&gt; value. */
    public Map<String, String> getPicks() { return picks; }
    public String getDetail() { return detail; }
}

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
    private final String detail;

    private PathVector(Event event, int pathId, boolean sat, Map<String, String> vars, String detail) {
        this.event = event;
        this.pathId = pathId;
        this.sat = sat;
        this.vars = Collections.unmodifiableMap(vars);
        this.detail = detail;
    }

    public static PathVector path(int pathId, boolean sat, Map<String, String> vars) {
        return new PathVector(Event.PATH, pathId, sat, vars, null);
    }

    public static PathVector constraintHit(String detail) {
        return new PathVector(Event.SEARCH_CONSTRAINT_HIT, -1, false,
                new LinkedHashMap<String, String>(), detail);
    }

    public static PathVector error(String detail) {
        return new PathVector(Event.ERROR, -1, false, new LinkedHashMap<String, String>(), detail);
    }

    public static PathVector finished(int totalPaths) {
        return new PathVector(Event.SEARCH_FINISHED, totalPaths, false,
                new LinkedHashMap<String, String>(), null);
    }

    public Event getEvent() { return event; }
    public int getPathId() { return pathId; }
    public boolean isSat() { return sat; }
    public Map<String, String> getVars() { return vars; }
    public String getDetail() { return detail; }

    /** True when the depth bound (rather than the memory bound) was the constraint hit. */
    public boolean isDepthLimit() {
        return detail != null && detail.contains("depth limit");
    }
}

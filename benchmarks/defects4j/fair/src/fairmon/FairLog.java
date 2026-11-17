package fairmon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The contract monitor's event log, shared by the monitored adapter (which writes it)
 * and {@link FairRunner}'s listener (which reads it after every test).
 *
 * <p>EvoSuite runs its tests in a separate, instrumenting class loader, so this
 * class may be loaded twice. The log itself is therefore kept in a JDK object
 * stored in the system properties, which every class loader sees. EvoSuite's
 * scaffolding restores the system properties from a shallow clone, so the same
 * list object survives that.
 *
 * <p>Events, one string each (tab-separated):
 * <pre>
 *   RESET                       the adapter was reset (reset() or its static initializer)
 *   CALL    op                  an adapter call starts
 *   PRE     op  clause          a precondition conjunct is false: the call is INVALID
 *   TAMPER  op  field           a state field was written by the test, not by the adapter
 *   OFFAPI  what                the test called a library method directly (marker added
 *                               by StripOracles); the next adapter call is INVALID
 *   PREX    op  exception       evaluating a precondition threw
 *   THROW   op  exception       the library threw
 *   POST    op  clause          a postcondition clause is false
 *   POSTX   op  exception       evaluating a postcondition threw
 *   OBS     op  digest          what the call returned / threw and the plain state after it
 * </pre>
 */
public final class FairLog {

    private FairLog() {}

    private static final String KEY = "fairmon.log";
    private static List<String> local;   // only if the system properties cannot be used

    @SuppressWarnings("unchecked")
    public static List<String> events() {
        try {
            Object o = System.getProperties().get(KEY);
            if (o instanceof List) return (List<String>) o;
            List<String> l = Collections.synchronizedList(new ArrayList<String>());
            System.getProperties().put(KEY, l);
            return l;
        } catch (Throwable t) {
            synchronized (FairLog.class) {
                if (local == null) local = Collections.synchronizedList(new ArrayList<String>());
                local.add("LOGERR\t" + t);
                return local;
            }
        }
    }

    /** Takes every event logged so far, in order, and clears the log. */
    public static List<String> drain() {
        List<String> l = events();
        synchronized (l) {
            List<String> out = new ArrayList<String>(l);
            l.clear();
            return out;
        }
    }

    private static void add(String e) {
        events().add(e);
    }

    public static void reset() { add("RESET"); }
    public static void call(String op) { add("CALL\t" + op); }
    public static void pre(String op, String clause) { add("PRE\t" + op + "\t" + clause); }
    public static void tamper(String op, String field) { add("TAMPER\t" + op + "\t" + field); }
    public static void offApi(String what) { add("OFFAPI\t" + what); }
    public static void preThrew(String op, Throwable t) { add("PREX\t" + op + "\t" + clip(t)); }
    public static void thrown(String op, Throwable t) { add("THROW\t" + op + "\t" + clip(t)); }
    public static void post(String op, String clause) { add("POST\t" + op + "\t" + clause); }
    public static void postThrew(String op, Throwable t) { add("POSTX\t" + op + "\t" + clip(t)); }
    public static void obs(String op, String digest) { add("OBS\t" + op + "\t" + digest); }

    /** A short, safe text of a value the adapter returns (String, number, boolean) or of a plain state field. */
    public static String show(Object v) {
        if (v == null) return "null";
        if (v instanceof String || v instanceof Number || v instanceof Boolean || v instanceof Character) {
            String s = String.valueOf(v);
            return s.length() > 40 ? s.substring(0, 40) + "~" + s.hashCode() : s;
        }
        return v.getClass().getName();   // never call toString() of a library object (it may loop)
    }

    private static String clip(Throwable t) {
        String s = String.valueOf(t).replace('\t', ' ').replace('\n', ' ');
        return s.length() > 200 ? s.substring(0, 200) : s;
    }
}

package in.ac.iiitb.plproject.atc.generated;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.Flat3Map;

/**
 * DEFECTS4J — Flat3Map (Apache Commons Collections 3.x), bugs 1 and 6.
 *
 * An ADAPTER over the real org.apache.commons.collections.map.Flat3Map from the
 * Defects4J checkout. Flat3Map keeps up to 3 entries in plain fields
 * (key1/value1 .. key3/value3) and switches to a HashMap on the 4th, so a test
 * string that crosses 3 entries runs both code paths.
 *
 * Global state (mirrors specs/Flat3Map.spec):
 * <pre>
 *   M    : Map&lt;String,String&gt;  the real Flat3Map
 *   size : int                 M.size()
 * </pre>
 *
 * Two methods take a parameter the adapter never uses. They are there for the
 * contract, which cannot write \old(M.get(key)) (\old works only on plain
 * state variables):
 *   remove(key, stored)                       the precondition pins stored == M.get(key),
 *                                             so the postcondition can say "remove
 *                                             returned the stored value" (bug 6);
 *   setValue(key, value, other, otherValue)   the precondition pins otherValue ==
 *                                             M.get(other), so the postcondition can
 *                                             say "the other entry was not touched" (bug 1).
 * The adapter only forwards the call; the comparison is in the contract.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Helper {

    public static Map<String, String> M = new Flat3Map();
    public static int size = 0;

    /** map.put(key, value) for a key not in the map. */
    public static String put(String key, String value) {
        String oldVal = M.put(key, value);
        size = M.size();
        return oldVal;
    }

    /** map.put(key, value) for a key already in the map. */
    public static String putExisting(String key, String value) {
        String oldVal = M.put(key, value);
        size = M.size();
        return oldVal;
    }

    /** map.get(key). */
    public static String get(String key) {
        return M.get(key);
    }

    /** map.containsKey(key). */
    public static boolean containsKey(String key) {
        return M.containsKey(key);
    }

    /** map.remove(key). {@code stored} is for the contract only (see the class comment). */
    public static String remove(String key, String stored) {
        String removedVal = M.remove(key);
        size = M.size();
        return removedVal;
    }

    /**
     * Entry.setValue(value) on the entry for {@code key}, reached through the
     * map's entry-set iterator (bug 1 is in that iterator's setValue).
     * {@code other} and {@code otherValue} are for the contract only.
     */
    public static void setValue(String key, String value, String other, String otherValue) {
        for (Iterator it = M.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            if (key.equals(entry.getKey())) {
                entry.setValue(value);
                break;
            }
        }
        size = M.size();
    }

    /** map.size(). Named count because size is the state variable. */
    public static int count() {
        return M.size();
    }

    public static void reset() {
        M = new Flat3Map();
        size = 0;
    }
}

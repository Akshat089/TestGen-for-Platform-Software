package in.ac.iiitb.plproject.atc.generated;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.map.ListOrderedMap;

/**
 * DEFECTS4J — ListOrderedMap (Apache Commons Collections 4), bug Collections-22.
 *
 * An ADAPTER over the real org.apache.commons.collections4.map.ListOrderedMap
 * from the Defects4J checkout: a map that remembers the order of its keys and
 * lets you insert at a position. Null values are allowed.
 *
 * Global state (mirrors specs/ListOrderedMap4.spec):
 * <pre>
 *   M    : ListOrderedMap&lt;String,String&gt;  the real map
 *   size : int                             M.size()
 * </pre>
 *
 * putAllAt / putAllExistingAt are the int/String form of putAll(index, map):
 * the adapter builds a two-entry map, in order (LinkedHashMap), and passes it.
 * putNull stores a null value, which the contract cannot ask the solver for
 * (it picks non-null strings unless a precondition forces null).
 */
public class Helper {

    public static ListOrderedMap<String, String> M = new ListOrderedMap<String, String>();
    public static int size = 0;

    /** M.put(key, value) for a new key. */
    public static String put(String key, String value) {
        String old = M.put(key, value);
        size = M.size();
        return old;
    }

    /** M.put(key, null) for a new key. */
    public static void putNull(String key) {
        M.put(key, null);
        size = M.size();
    }

    /** M.put(index, key, value): insert a new key at a position. */
    public static void putAt(int index, String key, String value) {
        M.put(index, key, value);
        size = M.size();
    }

    /** M.putAll(index, {k1: v1, k2: v2}) with two new keys. */
    public static void putAllAt(int index, String k1, String v1, String k2, String v2) {
        M.putAll(index, pair(k1, v1, k2, v2));
        size = M.size();
    }

    /** M.putAll(index, {k1: v1, k2: v2}) with two keys already in the map. */
    public static void putAllExistingAt(int index, String k1, String v1, String k2, String v2) {
        M.putAll(index, pair(k1, v1, k2, v2));
        size = M.size();
    }

    /** M.get(key). */
    public static String get(String key) {
        return M.get(key);
    }

    /** M.indexOf(key). */
    public static int indexOf(String key) {
        return M.indexOf(key);
    }

    /** M.remove(key). */
    public static String remove(String key) {
        String old = M.remove(key);
        size = M.size();
        return old;
    }

    /** M.size(). Named count because size is the state variable. */
    public static int count() {
        return M.size();
    }

    private static Map<String, String> pair(String k1, String v1, String k2, String v2) {
        Map<String, String> m = new LinkedHashMap<String, String>();
        m.put(k1, v1);
        m.put(k2, v2);
        return m;
    }

    public static void reset() {
        M = new ListOrderedMap<String, String>();
        size = 0;
    }
}

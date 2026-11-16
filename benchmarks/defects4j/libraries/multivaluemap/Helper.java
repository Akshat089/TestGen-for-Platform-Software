package in.ac.iiitb.plproject.atc.generated;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.collections.map.MultiValueMap;

/**
 * DEFECTS4J — MultiValueMap (Apache Commons Collections 3.x), bug Collections-4.
 *
 * An ADAPTER over the real org.apache.commons.collections.map.MultiValueMap
 * from the Defects4J checkout: a map from each key to a collection of values
 * (an ArrayList per key, the class's default). put(key, value) adds value to
 * key's collection.
 *
 * Global state (mirrors specs/MultiValueMap.spec):
 * <pre>
 *   M         : MultiValueMap  the real map (keys are Strings)
 *   keyCount  : int            M.size(), the number of keys
 *   totalSize : int            M.totalSize(), the number of values over all keys
 * </pre>
 *
 * putAllPair is the int/String form of putAll(key, collection): the adapter
 * passes the two values as a list.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Helper {

    public static MultiValueMap M = MultiValueMap.decorate(new HashMap());
    public static int keyCount = 0;
    public static int totalSize = 0;

    /** M.put(key, value): adds value to key's collection; returns value if the map changed, else null. */
    public static String put(String key, String value) {
        Object r = M.put(key, value);
        sync();
        return (String) r;
    }

    /** M.putAll(key, [a, b]): true if the map changed. */
    public static boolean putAllPair(String key, String a, String b) {
        boolean changed = M.putAll(key, Arrays.asList(a, b));
        sync();
        return changed;
    }

    /** M.size(key): the number of values for key. */
    public static int sizeOf(String key) {
        return M.size(key);
    }

    /** M.containsValue(key, value). */
    public static boolean containsValue(String key, String value) {
        return M.containsValue(key, value);
    }

    /** M.remove(key): drops key and all its values. */
    public static void removeKey(String key) {
        M.remove(key);
        sync();
    }

    /** M.totalSize(). */
    public static int total() {
        return M.totalSize();
    }

    private static void sync() {
        keyCount = M.size();
        totalSize = M.totalSize();
    }

    public static void reset() {
        M = MultiValueMap.decorate(new HashMap());
        keyCount = 0;
        totalSize = 0;
    }
}

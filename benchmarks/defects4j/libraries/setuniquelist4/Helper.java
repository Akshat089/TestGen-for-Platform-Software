package in.ac.iiitb.plproject.atc.generated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections4.list.SetUniqueList;

/**
 * DEFECTS4J — SetUniqueList (Apache Commons Collections 4), bug 21.
 *
 * The same adapter as libraries/setuniquelist/Helper.java, compiled against the
 * collections4 package. The one contract difference is subList: from 4.0 the
 * Javadoc says it returns an UNMODIFIABLE view, because changes through the view
 * can corrupt the parent's uniqueness set. Bug 21 is a version that returned a
 * modifiable view.
 *
 * subRemoveAt turns "the view refused the change" (UnsupportedOperationException)
 * into the boolean it returns. That is reporting what happened, not a check:
 * the contract (specs/SetUniqueList4.spec) is what says the change must be refused.
 *
 * Global state (mirrors specs/SetUniqueList4.spec):
 * <pre>
 *   L           : List&lt;String&gt;  the real SetUniqueList
 *   size        : int           L.size()
 *   trackedSize : int           L.asSet().size(), the internal uniqueness set
 *   Sub         : List&lt;String&gt;  the last view subList() handed out
 *   subSize     : int           Sub.size()
 * </pre>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Helper {

    public static List<String> L = fresh();
    public static int size = 0;
    public static int trackedSize = 0;
    public static List<String> Sub = null;
    public static int subSize = 0;

    /** SetUniqueList over an empty ArrayList and HashSet, as setUniqueList() builds it. */
    static final class Fresh extends SetUniqueList {
        Fresh() {
            super(new ArrayList(), new HashSet());
        }
    }

    private static List<String> fresh() {
        return new Fresh();
    }

    public static boolean add(String e) {
        boolean added = L.add(e);
        sync();
        return added;
    }

    public static void addAt(int index, String e) {
        L.add(index, e);
        sync();
    }

    public static boolean addAllAt(int index, String a, String b) {
        boolean changed = L.addAll(index, Arrays.asList(a, b));
        sync();
        return changed;
    }

    public static String set(int index, String e) {
        String replaced = L.set(index, e);
        sync();
        return replaced;
    }

    public static String setExisting(int index, String e) {
        String replaced = L.set(index, e);
        sync();
        return replaced;
    }

    public static String removeAt(int index) {
        String removed = L.remove(index);
        sync();
        return removed;
    }

    public static boolean removeElement(String e) {
        boolean wasRemoved = L.remove(e);
        sync();
        return wasRemoved;
    }

    public static String get(int index) {
        return L.get(index);
    }

    public static int indexOf(String e) {
        return L.indexOf(e);
    }

    public static boolean contains(String e) {
        return L.contains(e);
    }

    public static int count() {
        return L.size();
    }

    /** list.subList(from, to). */
    public static void subList(int from, int to) {
        Sub = L.subList(from, to);
        subSize = Sub.size();
    }

    /** Sub.remove(i): true if the view let the element be removed, false if it refused. */
    public static boolean subRemoveAt(int i) {
        boolean changed;
        try {
            Sub.remove(i);
            changed = true;
        } catch (UnsupportedOperationException refused) {
            changed = false;
        }
        subSize = Sub.size();
        sync();
        return changed;
    }

    private static void sync() {
        size = L.size();
        trackedSize = ((SetUniqueList) L).asSet().size();
    }

    public static void reset() {
        L = fresh();
        size = 0;
        trackedSize = 0;
        Sub = null;
        subSize = 0;
    }
}

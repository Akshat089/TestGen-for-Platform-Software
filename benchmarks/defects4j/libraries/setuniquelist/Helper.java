package in.ac.iiitb.plproject.atc.generated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.list.SetUniqueList;

/**
 * DEFECTS4J — SetUniqueList (Apache Commons Collections 3.x), bugs 5, 15, 16, 19.
 *
 * Unlike the libraries under libraries/, this Helper is an ADAPTER: the code
 * under test is the real org.apache.commons.collections.list.SetUniqueList from
 * the Defects4J checkout, compiled next to this file. Every method forwards one
 * call to it with the int/String arguments TestGen chose, then copies the state
 * the contract reads into static fields. It adds no checks of its own; every
 * check lives in specs/SetUniqueList.spec.
 *
 * Global state (mirrors specs/SetUniqueList.spec):
 * <pre>
 *   L              : List&lt;String&gt;  the real SetUniqueList
 *   size           : int           L.size()
 *   trackedSize    : int           L.asSet().size(), the internal uniqueness set
 *   subSize        : int           size of the last view subList() handed out
 *   subTrackedSize : int           size of that view's own uniqueness set
 * </pre>
 *
 * trackedSize is the point of the adapter: a SetUniqueList is a list plus a set
 * that must hold the same elements, and bugs 15 and 19 break exactly that
 * agreement. Comparing the two sizes needs no quantifier.
 *
 * The list is built through the (protected) constructor every version has,
 * {@code SetUniqueList(List, Set)}, via an empty subclass: the 3.x versions
 * only offer the factory decorate(), the later generic ones only
 * setUniqueList(), and both do exactly this for an empty list. The subclass
 * overrides nothing, so every call still runs the real class's code.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Helper {

    public static List<String> L = fresh();
    public static int size = 0;
    public static int trackedSize = 0;
    public static int subSize = 0;
    public static int subTrackedSize = 0;

    /** SetUniqueList over an empty ArrayList and HashSet, as decorate()/setUniqueList() build it. */
    static final class Fresh extends SetUniqueList {
        Fresh() {
            super(new ArrayList(), new HashSet());
        }
    }

    private static List<String> fresh() {
        return new Fresh();
    }

    /** list.add(e): append if not already present. */
    public static boolean add(String e) {
        boolean added = L.add(e);
        sync();
        return added;
    }

    /** list.add(index, e). */
    public static void addAt(int index, String e) {
        L.add(index, e);
        sync();
    }

    /** list.addAll(index, [a, b]): the int/String form of addAll(index, collection) (bug 5). */
    public static boolean addAllAt(int index, String a, String b) {
        boolean changed = L.addAll(index, Arrays.asList(a, b));
        sync();
        return changed;
    }

    /** list.set(index, e) with an element not in the list (bug 15). */
    public static String set(int index, String e) {
        String replaced = L.set(index, e);
        sync();
        return replaced;
    }

    /** list.set(index, e) with an element already in the list (bug 19 when it sits at index). */
    public static String setExisting(int index, String e) {
        String replaced = L.set(index, e);
        sync();
        return replaced;
    }

    /** list.remove(index). */
    public static String removeAt(int index) {
        String removed = L.remove(index);
        sync();
        return removed;
    }

    /** list.remove(e). */
    public static boolean removeElement(String e) {
        boolean wasRemoved = L.remove(e);
        sync();
        return wasRemoved;
    }

    /** list.get(index). */
    public static String get(int index) {
        return L.get(index);
    }

    /** list.indexOf(e). */
    public static int indexOf(String e) {
        return L.indexOf(e);
    }

    /** list.contains(e). */
    public static boolean contains(String e) {
        return L.contains(e);
    }

    /** list.size(). Named count because size is the state variable. */
    public static int count() {
        return L.size();
    }

    /** list.subList(from, to), recording the view's size and its own uniqueness set's size (bug 16). */
    public static void subList(int from, int to) {
        List<String> view = L.subList(from, to);
        subSize = view.size();
        subTrackedSize = ((SetUniqueList) view).asSet().size();
    }

    private static void sync() {
        size = L.size();
        trackedSize = ((SetUniqueList) L).asSet().size();
    }

    public static void reset() {
        L = fresh();
        size = 0;
        trackedSize = 0;
        subSize = 0;
        subTrackedSize = 0;
    }
}

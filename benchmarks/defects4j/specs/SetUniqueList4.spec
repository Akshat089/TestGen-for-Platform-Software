/*
 * DEFECTS4J — SetUniqueList (Apache Commons Collections 4)
 * Real bug: Collections-21.  Adapter: libraries/setuniquelist4/Helper.java
 *
 * The same contract as SetUniqueList.spec for the list itself. subList differs:
 * from 4.0 the Javadoc promises an UNMODIFIABLE view, because a change made
 * through the view can corrupt the parent's uniqueness set.
 *
 *   bug 21  subList returns a modifiable view
 *           -> subRemoveAt removes the element: changed == false and
 *              size == \old(size) fail
 *
 * subList asks for from < to so the view is not empty and subRemoveAt has an
 * index to try.
 */

state {
    List<String> L;
    int size;
    int trackedSize;
    List<String> Sub;
    int subSize;
}

spec add {
    signature: boolean add(String e);
    requires:  e != null && !L.contains(e);
    ensures:   \result == added && added == true && L.contains(e) && size == \old(size) + 1 && trackedSize == size;
}

spec addAt {
    signature: void addAt(int index, String e);
    requires:  index >= 0 && index <= size && e != null && !L.contains(e);
    ensures:   L.get(index) == e && size == \old(size) + 1 && trackedSize == size;
}

spec addAllAt {
    signature: boolean addAllAt(int index, String a, String b);
    requires:  index >= 0 && index < size && a != null && b != null && a != b && !L.contains(a) && !L.contains(b);
    ensures:   \result == changed && L.get(index) == a && L.get(index + 1) == b && size == \old(size) + 2 && trackedSize == size;
}

spec set {
    signature: String set(int index, String e);
    requires:  index >= 0 && index < size && e != null && !L.contains(e);
    ensures:   \result == replaced && L.get(index) == e && L.contains(e) && !L.contains(replaced) && size == \old(size) && trackedSize == size;
}

spec setExisting {
    signature: String setExisting(int index, String e);
    requires:  index >= 0 && index < size && L.contains(e);
    ensures:   \result == replaced && L.contains(e) && size <= \old(size) && size >= \old(size) - 1 && trackedSize == size;
}

spec removeAt {
    signature: String removeAt(int index);
    requires:  index >= 0 && index < size;
    ensures:   \result == removed && !L.contains(removed) && size == \old(size) - 1 && trackedSize == size;
}

spec removeElement {
    signature: boolean removeElement(String e);
    requires:  L.contains(e);
    ensures:   \result == wasRemoved && wasRemoved == true && !L.contains(e) && size == \old(size) - 1 && trackedSize == size;
}

spec get {
    signature: String get(int index);
    requires:  index >= 0 && index < size;
    ensures:   \result == elem && L.contains(elem) && size == \old(size);
}

spec indexOf {
    signature: int indexOf(String e);
    requires:  L.contains(e);
    ensures:   \result == position && position >= 0 && position < size && L.get(position) == e;
}

spec contains {
    signature: boolean contains(String e);
    requires:  e != null;
    ensures:   \result == present && size == \old(size);
}

spec count {
    signature: int count();
    requires:  size >= 0;
    ensures:   \result == n && n == size && n == trackedSize;
}

spec subList {
    signature: void subList(int from, int to);
    requires:  from >= 0 && from < to && to <= size;
    ensures:   subSize == to - from && size == \old(size);
}

spec subRemoveAt {
    signature: boolean subRemoveAt(int i);
    requires:  i >= 0 && i < subSize;
    ensures:   \result == changed && changed == false && size == \old(size) && subSize == \old(subSize) && trackedSize == size;
}

/*
 * DEFECTS4J — SetUniqueList (Apache Commons Collections 3.x)
 * Real bugs: Collections-5, -15, -16, -19.  Adapter: libraries/setuniquelist/Helper.java
 *
 * A SetUniqueList is a List that refuses duplicates. It keeps a List and a Set
 * that must always hold the same elements. trackedSize (the Set's size) ==
 * size (the List's size) is that agreement, written without a quantifier, and
 * it is asserted after every call that changes the list.
 *
 *   bug 15  set(i, e) with a new e leaves the replaced element in the Set
 *           -> trackedSize == size fails after set
 *   bug 19  set(i, e) with e already at i drops e from the Set
 *           -> L.contains(e) and trackedSize == size fail after setExisting
 *   bug 5   addAll(i, [a, b]) appends instead of inserting at i
 *           -> L.get(index) == a fails after addAllAt (when index < size)
 *   bug 16  subList(from, to) gives the view the parent's whole Set
 *           -> subTrackedSize == subSize fails after subList
 *
 * set and setExisting are the same library call, split because a precondition
 * cannot say "e is new OR e is already at index": one requires !L.contains(e),
 * the other L.contains(e). Whether setExisting hits bug 19 depends on the
 * solver choosing the element that already sits at index.
 *
 * subList asks for to < size, a proper part of the list: when the view is the
 * whole list, the parent's Set is also the right Set and bug 16 is invisible.
 */

state {
    List<String> L;
    int size;
    int trackedSize;
    int subSize;
    int subTrackedSize;
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
    requires:  from >= 0 && from <= to && to < size;
    ensures:   subSize == to - from && subTrackedSize == subSize && size == \old(size);
}

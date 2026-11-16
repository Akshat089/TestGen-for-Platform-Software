/*
 * DEFECTS4J — TreeList (Apache Commons Collections)
 * Real bug: Collections-20.  Adapter: libraries/treelist/Helper.java
 *
 * A list iterator sits between two elements: next() returns the element at
 * nextIndex and moves right, previous() returns the one before it and moves
 * left. The contract checks every returned element against the list itself,
 * L.get(position), so an iterator that returns the wrong element is caught.
 *
 *   Collections-20  after previous() then remove(), the iterator's cached
 *                   "next" node still points past the removed element, so the
 *                   following previous() returns the wrong element
 *                   -> elem == L.get(itNextIndex) fails after iterPrevious
 *
 * The iterator calls must not be mixed with add after iterStart (the list
 * would throw ConcurrentModificationException); the test strings respect that.
 */

state {
    List<String> L;
    int size;
    int itNextIndex;
    int lastMove;
}

spec add {
    signature: void add(String e);
    requires:  e != null && !L.contains(e);
    ensures:   L.get(size - 1) == e && size == \old(size) + 1;
}

spec get {
    signature: String get(int index);
    requires:  index >= 0 && index < size;
    ensures:   \result == elem && L.contains(elem) && size == \old(size);
}

spec iterStart {
    signature: void iterStart(int index);
    requires:  index >= 0 && index <= size;
    ensures:   itNextIndex == index && lastMove == 0;
}

spec iterNext {
    signature: String iterNext();
    requires:  itNextIndex < size;
    ensures:   \result == elem && elem == L.get(\old(itNextIndex)) && itNextIndex == \old(itNextIndex) + 1 && size == \old(size);
}

spec iterPrevious {
    signature: String iterPrevious();
    requires:  itNextIndex > 0;
    ensures:   \result == elem && elem == L.get(itNextIndex) && itNextIndex == \old(itNextIndex) - 1 && size == \old(size);
}

spec iterRemove {
    signature: void iterRemove();
    requires:  lastMove != 0;
    ensures:   size == \old(size) - 1 && itNextIndex <= \old(itNextIndex) && itNextIndex >= \old(itNextIndex) - 1;
}

spec count {
    signature: int count();
    requires:  size >= 0;
    ensures:   \result == n && n == size;
}

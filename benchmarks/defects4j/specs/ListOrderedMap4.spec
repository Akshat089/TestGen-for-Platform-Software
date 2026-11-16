/*
 * DEFECTS4J — ListOrderedMap (Apache Commons Collections 4)
 * Real bug: Collections-22.  Adapter: libraries/listorderedmap4/Helper.java
 *
 * A map that keeps its keys in order and can insert at a position.
 * putAll(index, map) puts each entry at index and moves on: after a new key to
 * index + 1, after an existing key to just behind that key.
 *
 *   Collections-22  putAll(index, map) decides "new key" from put's return
 *                   value, which is also null for an existing key whose value
 *                   is null; it then moves on to the wrong position (and can
 *                   throw IndexOutOfBoundsException)
 *                   -> putAllExistingAt's postconditions, or the call itself, fail
 *
 * put / putNull / putAt and putAllAt / putAllExistingAt are split for the
 * same reason as set/setExisting in SetUniqueList.spec: a precondition cannot
 * say "new key OR existing key", and the two cases change size differently.
 */

state {
    Map<String,String> M;
    int size;
}

spec put {
    signature: String put(String key, String value);
    requires:  key != null && value != null && !M.containsKey(key);
    ensures:   \result == prev && prev == null && M.get(key) == value && M.indexOf(key) == size - 1 && size == \old(size) + 1;
}

spec putNull {
    signature: void putNull(String key);
    requires:  key != null && !M.containsKey(key);
    ensures:   M.containsKey(key) && M.get(key) == null && size == \old(size) + 1;
}

spec putAt {
    signature: void putAt(int index, String key, String value);
    requires:  index >= 0 && index <= size && key != null && !M.containsKey(key);
    ensures:   M.indexOf(key) == index && M.get(key) == value && size == \old(size) + 1;
}

spec putAllAt {
    signature: void putAllAt(int index, String k1, String v1, String k2, String v2);
    requires:  index >= 0 && index <= size && k1 != null && k2 != null && k1 != k2 && !M.containsKey(k1) && !M.containsKey(k2);
    ensures:   M.indexOf(k1) == index && M.indexOf(k2) == index + 1 && M.get(k1) == v1 && M.get(k2) == v2 && size == \old(size) + 2;
}

spec putAllExistingAt {
    signature: void putAllExistingAt(int index, String k1, String v1, String k2, String v2);
    requires:  index >= 0 && index <= size && k1 != k2 && M.containsKey(k1) && M.containsKey(k2);
    ensures:   M.indexOf(k2) == M.indexOf(k1) + 1 && M.get(k1) == v1 && M.get(k2) == v2 && size == \old(size);
}

spec get {
    signature: String get(String key);
    requires:  M.containsKey(key);
    ensures:   \result == val && val == M.get(key) && size == \old(size);
}

spec indexOf {
    signature: int indexOf(String key);
    requires:  M.containsKey(key);
    ensures:   \result == pos && pos >= 0 && pos < size;
}

spec remove {
    signature: String remove(String key);
    requires:  M.containsKey(key);
    ensures:   \result == prev && !M.containsKey(key) && M.indexOf(key) == -1 && size == \old(size) - 1;
}

spec count {
    signature: int count();
    requires:  size >= 0;
    ensures:   \result == n && n == size;
}

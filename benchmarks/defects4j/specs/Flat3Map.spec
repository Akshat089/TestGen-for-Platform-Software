/*
 * DEFECTS4J — Flat3Map (Apache Commons Collections 3.x)
 * Real bugs: Collections-1, -6.  Adapter: libraries/flat3map/Helper.java
 *
 * Flat3Map stores up to 3 entries in plain fields and switches to a HashMap on
 * the 4th, so the bugs below live in the "flat" (<= 3 entries) code.
 *
 *   bug 6  remove(key) in flat mode returns another entry's value
 *          -> removedVal == stored fails
 *   bug 1  Entry.setValue in flat mode is missing its breaks, so setting the
 *          1st or 2nd entry also overwrites the entries after it
 *          -> M.get(other) == otherValue fails
 *
 * The contract cannot write \old(M.get(key)) (\old works only on plain state
 * variables). Instead the value is NAMED BEFORE THE CALL as an extra
 * parameter that the precondition pins down: remove(key, stored) requires
 * M.get(key) == stored, and the postcondition compares the result with stored.
 * setValue's other/otherValue pair works the same way. The adapter ignores
 * these parameters.
 *
 * put and putExisting are the same library call, split because the size
 * changes differently for a new key and an existing one.
 */

state {
    Map<String,String> M;
    int size;
}

spec put {
    signature: String put(String key, String value);
    requires:  key != null && value != null && !M.containsKey(key);
    ensures:   \result == oldVal && oldVal == null && M.get(key) == value && size == \old(size) + 1;
}

spec putExisting {
    signature: String putExisting(String key, String value);
    requires:  value != null && M.containsKey(key);
    ensures:   \result == oldVal && M.get(key) == value && size == \old(size);
}

spec get {
    signature: String get(String key);
    requires:  M.containsKey(key);
    ensures:   \result == curVal && curVal == M.get(key) && size == \old(size);
}

spec containsKey {
    signature: boolean containsKey(String key);
    requires:  key != null;
    ensures:   \result == present && size == \old(size);
}

spec remove {
    signature: String remove(String key, String stored);
    requires:  M.containsKey(key) && M.get(key) == stored;
    ensures:   \result == removedVal && removedVal == stored && !M.containsKey(key) && size == \old(size) - 1;
}

spec setValue {
    signature: void setValue(String key, String value, String other, String otherValue);
    requires:  M.containsKey(key) && M.containsKey(other) && key != other && M.get(other) == otherValue && value != null && value != otherValue;
    ensures:   M.get(key) == value && M.get(other) == otherValue && size == \old(size);
}

spec count {
    signature: int count();
    requires:  size >= 0;
    ensures:   \result == n && n == size;
}

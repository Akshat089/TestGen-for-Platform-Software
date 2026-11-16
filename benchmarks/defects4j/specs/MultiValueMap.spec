/*
 * DEFECTS4J — MultiValueMap (Apache Commons Collections 3.x)
 * Real bug: Collections-4.  Adapter: libraries/multivaluemap/Helper.java
 *
 * Each key maps to a list of values. The Javadoc of put says it returns the
 * value added if the map changed, null otherwise; putAll(key, coll) returns
 * true if the map changed. With a list per key, every add changes the map.
 *
 *   Collections-4  put and putAll report "no change" (null / false) the first
 *                  time a key is used, although the value was added
 *                  -> r == value fails after put, changed == true after putAllPair
 *
 * putAllPair asks for two different values so the list grows by exactly 2.
 */

state {
    Map<String,String> M;
    int keyCount;
    int totalSize;
}

spec put {
    signature: String put(String key, String value);
    requires:  key != null && value != null;
    ensures:   \result == r && r == value && M.containsKey(key) && M.containsValue(key, value) && totalSize == \old(totalSize) + 1;
}

spec putAllPair {
    signature: boolean putAllPair(String key, String a, String b);
    requires:  key != null && a != null && b != null && a != b;
    ensures:   \result == changed && changed == true && M.containsKey(key) && totalSize == \old(totalSize) + 2;
}

spec sizeOf {
    signature: int sizeOf(String key);
    requires:  M.containsKey(key);
    ensures:   \result == n && n >= 1 && n <= totalSize && totalSize == \old(totalSize);
}

spec containsValue {
    signature: boolean containsValue(String key, String value);
    requires:  M.containsKey(key) && value != null;
    ensures:   \result == present && totalSize == \old(totalSize);
}

spec removeKey {
    signature: void removeKey(String key);
    requires:  M.containsKey(key);
    ensures:   !M.containsKey(key) && keyCount == \old(keyCount) - 1 && totalSize < \old(totalSize);
}

spec total {
    signature: int total();
    requires:  totalSize >= 0;
    ensures:   \result == n && n == totalSize && n >= keyCount;
}

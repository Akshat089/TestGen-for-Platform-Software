package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/LruCache.spec + ../specs/LruCache.tests.
 * One @Test method per path SPF found through the sequence;
 * every assertion traces to one postcondition clause of the contract.
 * No SPF on this class's classpath (rule 11).
 */
public class GeneratedATCsTest {

    // SPF path #1
    @Test
    public void path1() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #2
    @Test
    public void path2() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #3
    @Test
    public void path3() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #4
    @Test
    public void path4() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #5
    @Test
    public void path5() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #6
    @Test
    public void path6() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #7
    @Test
    public void path7() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #8
    @Test
    public void path8() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #9
    @Test
    public void path9() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #10
    @Test
    public void path10() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #11
    @Test
    public void path11() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #12
    @Test
    public void path12() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #13
    @Test
    public void path13() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #14
    @Test
    public void path14() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #15
    @Test
    public void path15() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #16
    @Test
    public void path16() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #17
    @Test
    public void path17() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #18
    @Test
    public void path18() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #19
    @Test
    public void path19() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #20
    @Test
    public void path20() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #21
    @Test
    public void path21() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #22
    @Test
    public void path22() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s2";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #23
    @Test
    public void path23() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s2";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #24
    @Test
    public void path24() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s2";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #25
    @Test
    public void path25() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #26
    @Test
    public void path26() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #27
    @Test
    public void path27() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #28
    @Test
    public void path28() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s2";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #29
    @Test
    public void path29() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s2";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #30
    @Test
    public void path30() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s2";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #31
    @Test
    public void path31() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #32
    @Test
    public void path32() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #33
    @Test
    public void path33() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #34
    @Test
    public void path34() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s2";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #35
    @Test
    public void path35() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s2";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #36
    @Test
    public void path36() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s2";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s2";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #37
    @Test
    public void path37() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #38
    @Test
    public void path38() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #39
    @Test
    public void path39() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #40
    @Test
    public void path40() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s3";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #41
    @Test
    public void path41() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s3";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #42
    @Test
    public void path42() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s3";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #43
    @Test
    public void path43() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #44
    @Test
    public void path44() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #45
    @Test
    public void path45() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #46
    @Test
    public void path46() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s3";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #47
    @Test
    public void path47() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s3";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #48
    @Test
    public void path48() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s2";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s3";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #49
    @Test
    public void path49() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #50
    @Test
    public void path50() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #51
    @Test
    public void path51() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #52
    @Test
    public void path52() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s3";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #53
    @Test
    public void path53() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s3";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #54
    @Test
    public void path54() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s1";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s3";
        String c2_value = "s3";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s3";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #55
    @Test
    public void path55() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s2";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #56
    @Test
    public void path56() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s2";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #57
    @Test
    public void path57() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s2";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s2";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #58
    @Test
    public void path58() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s2";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s1";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #59
    @Test
    public void path59() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s2";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s2";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }

    // SPF path #60
    @Test
    public void path60() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        String c0_value = "s1";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == evictedKey", c0_result == c0_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s1";
        String c1_value = "s2";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == evictedKey", c1_result == c1_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s1";
        String c2_value = "s1";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: \\result == evictedKey", c2_result == c2_result);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        String c3_key = "s1";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: \\result == cachedValue", c3_result == c3_result);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s3";
        String c4_value = "s3";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }
}

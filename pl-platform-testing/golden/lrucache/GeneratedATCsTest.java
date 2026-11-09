package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/LruCache.spec + ../specs/LruCache.tests.
 * One @Test method per sequence: the concrete test case genCTC returned,
 * statement for statement; every assertion is one clause of the contract.
 */
public class GeneratedATCsTest {

    // sequence: put -> put -> put -> get -> restore
    @Test(timeout = 2000)
    public void lruCacheDryRun() {
        Helper.reset();

        // call 0: put
        String c0_key = "s7";
        String c0_value = "s9";
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c0_key) == c0_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c0_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 1: put
        String c1_key = "s11";
        String c1_value = "s13";
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c1_key) == c1_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c1_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 2: put
        String c2_key = "s15";
        String c2_value = "s17";
        assertTrue("PRE put: key != null", c2_key != null);
        assertTrue("PRE put: value != null", c2_value != null);
        assertTrue("PRE put: capacity > 0", Helper.capacity > 0);
        String c2_result = Helper.put(c2_key, c2_value);
        assertTrue("POST put: C.get(key) == value", Helper.C.get(c2_key) == c2_value);
        assertTrue("POST put: mostRecent == key", Helper.mostRecent == c2_key);
        assertTrue("POST put: size <= capacity", Helper.size <= Helper.capacity);

        // call 3: get
        int old3_size = Helper.size;
        String c3_key = "s11";
        assertTrue("PRE get: C.containsKey(key)", Helper.C.containsKey(c3_key));
        String c3_result = Helper.get(c3_key);
        assertTrue("POST get: cachedValue == C.get(key)", c3_result == Helper.C.get(c3_key));
        assertTrue("POST get: mostRecent == key", Helper.mostRecent == c3_key);
        assertTrue("POST get: size == \\old(size)", Helper.size == old3_size);

        // call 4: restore
        String c4_evictedKey = "s0";
        String c4_value = "s23";
        assertTrue("PRE restore: evictedKey != null", c4_evictedKey != null);
        assertTrue("PRE restore: !C.containsKey(evictedKey)", !Helper.C.containsKey(c4_evictedKey));
        Helper.restore(c4_evictedKey, c4_value);
        assertTrue("POST restore: C.containsKey(evictedKey)", Helper.C.containsKey(c4_evictedKey));
        assertTrue("POST restore: mostRecent == evictedKey", Helper.mostRecent == c4_evictedKey);
        assertTrue("POST restore: size <= capacity", Helper.size <= Helper.capacity);
    }
}

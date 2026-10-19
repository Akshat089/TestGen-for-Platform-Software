package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/HashMapLib.spec + ../specs/HashMapLib.tests.
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
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s1";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s1";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s1";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #2
    @Test
    public void path2() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s2";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s1";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s1";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #3
    @Test
    public void path3() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s2";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s1";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s2";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #4
    @Test
    public void path4() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s2";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s2";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s1";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #5
    @Test
    public void path5() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s2";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s2";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s2";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #6
    @Test
    public void path6() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s3";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s1";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s1";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #7
    @Test
    public void path7() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s3";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s1";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s3";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #8
    @Test
    public void path8() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s3";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s3";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s1";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #9
    @Test
    public void path9() {
        Helper.reset();

        // call 0: put
        String c0_key = "s1";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s3";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s3";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s3";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #10
    @Test
    public void path10() {
        Helper.reset();

        // call 0: put
        String c0_key = "s2";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s1";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s1";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s1";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #11
    @Test
    public void path11() {
        Helper.reset();

        // call 0: put
        String c0_key = "s2";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s1";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s1";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s2";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #12
    @Test
    public void path12() {
        Helper.reset();

        // call 0: put
        String c0_key = "s2";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s1";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s2";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s1";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #13
    @Test
    public void path13() {
        Helper.reset();

        // call 0: put
        String c0_key = "s2";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s1";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s2";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s2";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #14
    @Test
    public void path14() {
        Helper.reset();

        // call 0: put
        String c0_key = "s2";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s2";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s2";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s2";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #15
    @Test
    public void path15() {
        Helper.reset();

        // call 0: put
        String c0_key = "s2";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s3";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s2";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s2";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #16
    @Test
    public void path16() {
        Helper.reset();

        // call 0: put
        String c0_key = "s2";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s3";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s2";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s3";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #17
    @Test
    public void path17() {
        Helper.reset();

        // call 0: put
        String c0_key = "s2";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s3";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s3";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s2";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #18
    @Test
    public void path18() {
        Helper.reset();

        // call 0: put
        String c0_key = "s2";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s3";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s3";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s3";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #19
    @Test
    public void path19() {
        Helper.reset();

        // call 0: put
        String c0_key = "s3";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s1";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s1";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s1";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #20
    @Test
    public void path20() {
        Helper.reset();

        // call 0: put
        String c0_key = "s3";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s1";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s1";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s3";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #21
    @Test
    public void path21() {
        Helper.reset();

        // call 0: put
        String c0_key = "s3";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s1";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s3";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s1";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #22
    @Test
    public void path22() {
        Helper.reset();

        // call 0: put
        String c0_key = "s3";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s1";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s3";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s3";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #23
    @Test
    public void path23() {
        Helper.reset();

        // call 0: put
        String c0_key = "s3";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s2";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s2";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s2";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #24
    @Test
    public void path24() {
        Helper.reset();

        // call 0: put
        String c0_key = "s3";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s2";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s2";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s3";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #25
    @Test
    public void path25() {
        Helper.reset();

        // call 0: put
        String c0_key = "s3";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s2";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s3";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s2";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #26
    @Test
    public void path26() {
        Helper.reset();

        // call 0: put
        String c0_key = "s3";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s2";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s3";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s3";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #27
    @Test
    public void path27() {
        Helper.reset();

        // call 0: put
        String c0_key = "s3";
        Integer c0_value = 7;
        assertTrue("PRE put: key != null", c0_key != null);
        assertTrue("PRE put: value != null", c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);
        assertTrue("POST put: \\result == oldVal", c0_result == c0_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c0_key) == c0_value);

        // call 1: put
        String c1_key = "s3";
        Integer c1_value = 9;
        assertTrue("PRE put: key != null", c1_key != null);
        assertTrue("PRE put: value != null", c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);
        assertTrue("POST put: \\result == oldVal", c1_result == c1_result);
        assertTrue("POST put: M.get(key) == value", Helper.M.get(c1_key) == c1_value);

        // call 2: getOldValue
        String c2_key = "s3";
        assertTrue("PRE getOldValue: M.containsKey(key)", Helper.M.containsKey(c2_key));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);
        assertTrue("POST getOldValue: \\result == curVal", c2_result == c2_result);
        assertTrue("POST getOldValue: curVal == M.get(key)", c2_result == Helper.M.get(c2_key));
        assertTrue("POST getOldValue: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_key = "s3";
        assertTrue("PRE remove: M.containsKey(key)", Helper.M.containsKey(c3_key));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
        assertTrue("POST remove: !M.containsKey(key)", !Helper.M.containsKey(c3_key));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }
}

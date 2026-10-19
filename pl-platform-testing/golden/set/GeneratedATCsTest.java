package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/StringSet.spec + ../specs/StringSet.tests.
 * One @Test method per path SPF found through the sequence;
 * every assertion traces to one postcondition clause of the contract.
 * No SPF on this class's classpath (rule 11).
 */
public class GeneratedATCsTest {

    // SPF path #1
    @Test
    public void path1() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #2
    @Test
    public void path2() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #3
    @Test
    public void path3() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #4
    @Test
    public void path4() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #5
    @Test
    public void path5() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #6
    @Test
    public void path6() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #7
    @Test
    public void path7() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #8
    @Test
    public void path8() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #9
    @Test
    public void path9() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #10
    @Test
    public void path10() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #11
    @Test
    public void path11() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #12
    @Test
    public void path12() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #13
    @Test
    public void path13() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #14
    @Test
    public void path14() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #15
    @Test
    public void path15() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s1";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #16
    @Test
    public void path16() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #17
    @Test
    public void path17() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #18
    @Test
    public void path18() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #19
    @Test
    public void path19() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #20
    @Test
    public void path20() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #21
    @Test
    public void path21() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #22
    @Test
    public void path22() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #23
    @Test
    public void path23() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #24
    @Test
    public void path24() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #25
    @Test
    public void path25() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #26
    @Test
    public void path26() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #27
    @Test
    public void path27() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #28
    @Test
    public void path28() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #29
    @Test
    public void path29() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #30
    @Test
    public void path30() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s2";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #31
    @Test
    public void path31() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #32
    @Test
    public void path32() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #33
    @Test
    public void path33() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #34
    @Test
    public void path34() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #35
    @Test
    public void path35() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s1";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #36
    @Test
    public void path36() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s1";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #37
    @Test
    public void path37() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #38
    @Test
    public void path38() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #39
    @Test
    public void path39() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #40
    @Test
    public void path40() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #41
    @Test
    public void path41() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s2";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #42
    @Test
    public void path42() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s2";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #43
    @Test
    public void path43() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s1";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #44
    @Test
    public void path44() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s2";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }

    // SPF path #45
    @Test
    public void path45() {
        Helper.reset();

        // call 0: add
        String c0_elem = "s3";
        assertTrue("PRE add: elem != null", c0_elem != null);
        boolean c0_result = Helper.add(c0_elem);
        assertTrue("POST add: \\result == added", c0_result == c0_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c0_elem));

        // call 1: add
        String c1_elem = "s3";
        assertTrue("PRE add: elem != null", c1_elem != null);
        boolean c1_result = Helper.add(c1_elem);
        assertTrue("POST add: \\result == added", c1_result == c1_result);
        assertTrue("POST add: E.contains(elem)", Helper.E.contains(c1_elem));

        // call 2: contains
        String c2_elem = "s3";
        assertTrue("PRE contains: elem != null", c2_elem != null);
        int old2_size = Helper.size;
        boolean c2_result = Helper.contains(c2_elem);
        assertTrue("POST contains: \\result == present", c2_result == c2_result);
        assertTrue("POST contains: present == E.contains(elem)", c2_result == Helper.E.contains(c2_elem));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old2_size);

        // call 3: remove
        String c3_elem = "s3";
        assertTrue("PRE remove: E.contains(elem)", Helper.E.contains(c3_elem));
        int old3_size = Helper.size;
        Helper.remove(c3_elem);
        assertTrue("POST remove: !E.contains(elem)", !Helper.E.contains(c3_elem));
        assertTrue("POST remove: size == \\old(size) - 1", Helper.size == old3_size - 1);
    }
}

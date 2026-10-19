package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/BinarySearchTree.spec + ../specs/BinarySearchTree.tests.
 * One @Test method per path SPF found through the sequence;
 * every assertion traces to one postcondition clause of the contract.
 * No SPF on this class's classpath (rule 11).
 */
public class GeneratedATCsTest {

    // SPF path #1
    @Test
    public void path1() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #2
    @Test
    public void path2() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #3
    @Test
    public void path3() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #4
    @Test
    public void path4() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #5
    @Test
    public void path5() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #6
    @Test
    public void path6() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #7
    @Test
    public void path7() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #8
    @Test
    public void path8() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #9
    @Test
    public void path9() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #10
    @Test
    public void path10() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #11
    @Test
    public void path11() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #12
    @Test
    public void path12() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #13
    @Test
    public void path13() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #14
    @Test
    public void path14() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #15
    @Test
    public void path15() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #16
    @Test
    public void path16() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #17
    @Test
    public void path17() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #18
    @Test
    public void path18() {
        Helper.reset();

        // call 0: insert
        int c0_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #19
    @Test
    public void path19() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #20
    @Test
    public void path20() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #21
    @Test
    public void path21() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #22
    @Test
    public void path22() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #23
    @Test
    public void path23() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #24
    @Test
    public void path24() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #25
    @Test
    public void path25() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #26
    @Test
    public void path26() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #27
    @Test
    public void path27() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #28
    @Test
    public void path28() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #29
    @Test
    public void path29() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #30
    @Test
    public void path30() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #31
    @Test
    public void path31() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #32
    @Test
    public void path32() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #33
    @Test
    public void path33() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #34
    @Test
    public void path34() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #35
    @Test
    public void path35() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #36
    @Test
    public void path36() {
        Helper.reset();

        // call 0: insert
        int c0_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #37
    @Test
    public void path37() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #38
    @Test
    public void path38() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #39
    @Test
    public void path39() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #40
    @Test
    public void path40() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #41
    @Test
    public void path41() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #42
    @Test
    public void path42() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #43
    @Test
    public void path43() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #44
    @Test
    public void path44() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #45
    @Test
    public void path45() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #46
    @Test
    public void path46() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #47
    @Test
    public void path47() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #48
    @Test
    public void path48() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 101;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #49
    @Test
    public void path49() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #50
    @Test
    public void path50() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #51
    @Test
    public void path51() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 103;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #52
    @Test
    public void path52() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 101;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #53
    @Test
    public void path53() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 103;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }

    // SPF path #54
    @Test
    public void path54() {
        Helper.reset();

        // call 0: insert
        int c0_key = 105;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c0_key));
        int old0_size = Helper.size;
        Helper.insert(c0_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c0_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old0_size + 1);

        // call 1: insert
        int c1_key = 103;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c1_key));
        int old1_size = Helper.size;
        Helper.insert(c1_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c1_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old1_size + 1);

        // call 2: insert
        int c2_key = 101;
        assertTrue("PRE insert: !Keys.contains(key)", !Helper.Keys.contains(c2_key));
        int old2_size = Helper.size;
        Helper.insert(c2_key);
        assertTrue("POST insert: Keys.contains(key)", Helper.Keys.contains(c2_key));
        assertTrue("POST insert: size == \\old(size) + 1", Helper.size == old2_size + 1);

        // call 3: contains
        int c3_key = 105;
        assertTrue("PRE contains: size > 0", Helper.size > 0);
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);
        assertTrue("POST contains: \\result == found", c3_result == c3_result);
        assertTrue("POST contains: found == Keys.contains(key)", c3_result == Helper.Keys.contains(c3_key));
        assertTrue("POST contains: size == \\old(size)", Helper.size == old3_size);

        // call 4: min
        assertTrue("PRE min: size > 0", Helper.size > 0);
        int old4_size = Helper.size;
        int c4_result = Helper.min();
        assertTrue("POST min: \\result == smallest", c4_result == c4_result);
        assertTrue("POST min: smallest == minKey", c4_result == Helper.minKey);
        assertTrue("POST min: size == \\old(size)", Helper.size == old4_size);

        // call 5: delete
        int c5_key = 105;
        assertTrue("PRE delete: Keys.contains(key)", Helper.Keys.contains(c5_key));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
        assertTrue("POST delete: !Keys.contains(key)", !Helper.Keys.contains(c5_key));
        assertTrue("POST delete: size == \\old(size) - 1", Helper.size == old5_size - 1);
    }
}

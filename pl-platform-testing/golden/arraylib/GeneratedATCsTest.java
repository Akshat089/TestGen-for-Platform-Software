package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/IntArray.spec + ../specs/IntArray.tests.
 * One @Test method per path SPF found through the sequence;
 * every assertion traces to one postcondition clause of the contract.
 * No SPF on this class's classpath (rule 11).
 */
public class GeneratedATCsTest {

    // SPF path #1
    @Test
    public void path1() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = 0;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #2
    @Test
    public void path2() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #3
    @Test
    public void path3() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #4
    @Test
    public void path4() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = 0;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #5
    @Test
    public void path5() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #6
    @Test
    public void path6() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #7
    @Test
    public void path7() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = 0;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #8
    @Test
    public void path8() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #9
    @Test
    public void path9() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #10
    @Test
    public void path10() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = 0;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #11
    @Test
    public void path11() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #12
    @Test
    public void path12() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 7;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 0;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #13
    @Test
    public void path13() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #14
    @Test
    public void path14() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #15
    @Test
    public void path15() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #16
    @Test
    public void path16() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #17
    @Test
    public void path17() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #18
    @Test
    public void path18() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #19
    @Test
    public void path19() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #20
    @Test
    public void path20() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #21
    @Test
    public void path21() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #22
    @Test
    public void path22() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = 0;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #23
    @Test
    public void path23() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #24
    @Test
    public void path24() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #25
    @Test
    public void path25() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #26
    @Test
    public void path26() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = 0;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #27
    @Test
    public void path27() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #28
    @Test
    public void path28() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 1;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #29
    @Test
    public void path29() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #30
    @Test
    public void path30() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #31
    @Test
    public void path31() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #32
    @Test
    public void path32() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #33
    @Test
    public void path33() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #34
    @Test
    public void path34() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #35
    @Test
    public void path35() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #36
    @Test
    public void path36() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #37
    @Test
    public void path37() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #38
    @Test
    public void path38() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #39
    @Test
    public void path39() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #40
    @Test
    public void path40() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #41
    @Test
    public void path41() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #42
    @Test
    public void path42() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #43
    @Test
    public void path43() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #44
    @Test
    public void path44() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 2;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #45
    @Test
    public void path45() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #46
    @Test
    public void path46() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #47
    @Test
    public void path47() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #48
    @Test
    public void path48() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 0;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #49
    @Test
    public void path49() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #50
    @Test
    public void path50() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #51
    @Test
    public void path51() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #52
    @Test
    public void path52() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 1;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #53
    @Test
    public void path53() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #54
    @Test
    public void path54() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #55
    @Test
    public void path55() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #56
    @Test
    public void path56() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 2;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #57
    @Test
    public void path57() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #58
    @Test
    public void path58() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = -1;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = 7;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = 0;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #59
    @Test
    public void path59() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = -1;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }

    // SPF path #60
    @Test
    public void path60() {
        Helper.reset();

        // call 0: set
        int c0_index = 0;
        int c0_value = 0;
        assertTrue("PRE set: index >= 0", c0_index >= 0);
        assertTrue("PRE set: index < length", c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);
        assertTrue("POST set: A[index] == value", Helper.A[c0_index] == c0_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old0_writes + 1);

        // call 1: set
        int c1_index = 3;
        int c1_value = -2;
        assertTrue("PRE set: index >= 0", c1_index >= 0);
        assertTrue("PRE set: index < length", c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);
        assertTrue("POST set: A[index] == value", Helper.A[c1_index] == c1_value);
        assertTrue("POST set: writes == \\old(writes) + 1", Helper.writes == old1_writes + 1);

        // call 2: get
        int c2_index = 3;
        assertTrue("PRE get: index >= 0", c2_index >= 0);
        assertTrue("PRE get: index < length", c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);
        assertTrue("POST get: \\result == elem", c2_result == c2_result);
        assertTrue("POST get: elem == A[index]", c2_result == Helper.A[c2_index]);
        assertTrue("POST get: writes == \\old(writes)", Helper.writes == old2_writes);

        // call 3: sum
        assertTrue("PRE sum: length > 0", Helper.length > 0);
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();
        assertTrue("POST sum: \\result == total", c3_result == c3_result);
        assertTrue("POST sum: total >= 0", c3_result >= 0);
        assertTrue("POST sum: writes == \\old(writes)", Helper.writes == old3_writes);

        // call 4: indexOf
        int c4_value = -1;
        assertTrue("PRE indexOf: length > 0", Helper.length > 0);
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
        assertTrue("POST indexOf: \\result == position", c4_result == c4_result);
        assertTrue("POST indexOf: position >= -1", c4_result >= -1);
        assertTrue("POST indexOf: position < length", c4_result < Helper.length);
        assertTrue("POST indexOf: writes == \\old(writes)", Helper.writes == old4_writes);
    }
}

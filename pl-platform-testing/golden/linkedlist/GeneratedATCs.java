package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/**
 * Symbolic driver for SPF, built from ../specs/LinkedList.spec only.
 * It models the library state itself and never calls the library.
 */
public class GeneratedATCs {

    public static void main(String[] args) {
        // starting state
        int m0_size = 0;
        int m0_head = 0;
        int m0_head$null = 1;
        int m0_tail = 0;
        int m0_tail$null = 1;

        // call 0: addFirst
        int c0_value = Debug.makeSymbolicInteger("c0_value");
        // requires: value != null
        Verify.ignoreIf(!(!(0 == 1)));
        int m1_size = Debug.makeSymbolicInteger("m1_size");
        int m1_head = Debug.makeSymbolicInteger("m1_head");
        int m1_head$null = flag("m1_head$null");
        // ensures: head == value
        Verify.ignoreIf(!((m1_head$null == 0 && m1_head == c0_value)));
        // CHECK-ONLY ensures: L.contains(value)  (state 'L' is not modelled: its elements (Integer) are not drawn from a pool)
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m1_size == (m0_size + 1))));

        // call 1: addLast
        int c1_value = Debug.makeSymbolicInteger("c1_value");
        // requires: value != null
        Verify.ignoreIf(!(!(0 == 1)));
        int m2_size = Debug.makeSymbolicInteger("m2_size");
        int m2_tail = Debug.makeSymbolicInteger("m2_tail");
        int m2_tail$null = flag("m2_tail$null");
        // ensures: tail == value
        Verify.ignoreIf(!((m2_tail$null == 0 && m2_tail == c1_value)));
        // CHECK-ONLY ensures: L.contains(value)  (state 'L' is not modelled: its elements (Integer) are not drawn from a pool)
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m2_size == (m1_size + 1))));

        // call 2: removeFirst
        // requires: size > 0
        Verify.ignoreIf(!((m2_size > 0)));
        int c2_result = Debug.makeSymbolicInteger("c2_result");
        int c2_result$null = flag("c2_result$null");
        int m3_size = Debug.makeSymbolicInteger("m3_size");
        // ensures: \result == removedValue  (only names the result)
        // ensures: removedValue == \old(head)
        Verify.ignoreIf(!(((c2_result$null == 1 && m1_head$null == 1) || (c2_result$null == 0 && m1_head$null == 0 && c2_result == m1_head))));
        // ensures: size == \old(size) - 1
        Verify.ignoreIf(!((m3_size == (m2_size - 1))));

        // call 3: indexOf
        int c3_value = Debug.makeSymbolicInteger("c3_value");
        // requires: size > 0
        Verify.ignoreIf(!((m3_size > 0)));
        int c3_result = Debug.makeSymbolicInteger("c3_result");
        int m4_size = Debug.makeSymbolicInteger("m4_size");
        // ensures: \result == position  (only names the result)
        // ensures: position >= -1
        Verify.ignoreIf(!((c3_result >= (-1))));
        // ensures: position < size
        Verify.ignoreIf(!((c3_result < m4_size)));
    }

    /** A symbolic 0/1 flag. */
    private static int flag(String name) {
        int v = Debug.makeSymbolicInteger(name);
        Verify.ignoreIf(v < 0 || v > 1);
        return v;
    }

    /** A symbolic index into the 3-value pool. */
    private static int index(String name) {
        int v = Debug.makeSymbolicInteger(name);
        Verify.ignoreIf(v < 0 || v > 2);
        return v;
    }
}

package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/**
 * Symbolic driver for SPF, built from ../specs/MinHeap.spec only.
 * It models the library state itself and never calls the library.
 */
public class GeneratedATCs {

    public static void main(String[] args) {
        // starting state
        int m0_size = 0;
        int m0_minValue = 0;
        int m0_minValue$null = 1;

        // call 0: insert
        int c0_value = Debug.makeSymbolicInteger("c0_value");
        // requires: value != null
        Verify.ignoreIf(!(!(0 == 1)));
        int m1_size = Debug.makeSymbolicInteger("m1_size");
        // CHECK-ONLY ensures: H.contains(value)  (state 'H' is not modelled: its elements (Integer) are not drawn from a pool)
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m1_size == (m0_size + 1))));

        // call 1: insert
        int c1_value = Debug.makeSymbolicInteger("c1_value");
        // requires: value != null
        Verify.ignoreIf(!(!(0 == 1)));
        int m2_size = Debug.makeSymbolicInteger("m2_size");
        // CHECK-ONLY ensures: H.contains(value)  (state 'H' is not modelled: its elements (Integer) are not drawn from a pool)
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m2_size == (m1_size + 1))));

        // call 2: insert
        int c2_value = Debug.makeSymbolicInteger("c2_value");
        // requires: value != null
        Verify.ignoreIf(!(!(0 == 1)));
        int m3_size = Debug.makeSymbolicInteger("m3_size");
        // CHECK-ONLY ensures: H.contains(value)  (state 'H' is not modelled: its elements (Integer) are not drawn from a pool)
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m3_size == (m2_size + 1))));

        // call 3: peekMin
        // requires: size > 0
        Verify.ignoreIf(!((m3_size > 0)));
        int c3_result = Debug.makeSymbolicInteger("c3_result");
        int c3_result$null = flag("c3_result$null");
        int m4_size = Debug.makeSymbolicInteger("m4_size");
        int m4_minValue = Debug.makeSymbolicInteger("m4_minValue");
        int m4_minValue$null = flag("m4_minValue$null");
        // ensures: \result == smallest  (only names the result)
        // ensures: smallest == minValue
        Verify.ignoreIf(!(((c3_result$null == 1 && m4_minValue$null == 1) || (c3_result$null == 0 && m4_minValue$null == 0 && c3_result == m4_minValue))));
        // ensures: size == \old(size)
        Verify.ignoreIf(!((m4_size == m3_size)));

        // call 4: extractMin
        // requires: size > 0
        Verify.ignoreIf(!((m4_size > 0)));
        int c4_result = Debug.makeSymbolicInteger("c4_result");
        int c4_result$null = flag("c4_result$null");
        int m5_size = Debug.makeSymbolicInteger("m5_size");
        // ensures: \result == removedMin  (only names the result)
        // ensures: removedMin == \old(minValue)
        Verify.ignoreIf(!(((c4_result$null == 1 && m4_minValue$null == 1) || (c4_result$null == 0 && m4_minValue$null == 0 && c4_result == m4_minValue))));
        // ensures: size == \old(size) - 1
        Verify.ignoreIf(!((m5_size == (m4_size - 1))));
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

package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/**
 * Symbolic driver for SPF, built from ../specs/Stack.spec only.
 * It models the library state itself and never calls the library.
 */
public class GeneratedATCs {

    public static void main(String[] args) {
        // starting state
        int[] m0_S = new int[3];
        int m0_size = 0;
        int m0_top = 0;
        int m0_top$null = 1;

        // call 0: push
        int c0_elem = index("c0_elem");
        // requires: elem != null
        Verify.ignoreIf(!(!(0 == 1)));
        int m1_size = Debug.makeSymbolicInteger("m1_size");
        int m1_top = index("m1_top");
        int m1_top$null = flag("m1_top$null");
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m1_size == (m0_size + 1))));
        // ensures: top == elem
        Verify.ignoreIf(!((m1_top$null == 0 && m1_top == c0_elem)));

        // call 1: push
        int c1_elem = index("c1_elem");
        // requires: elem != null
        Verify.ignoreIf(!(!(0 == 1)));
        int m2_size = Debug.makeSymbolicInteger("m2_size");
        int m2_top = index("m2_top");
        int m2_top$null = flag("m2_top$null");
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m2_size == (m1_size + 1))));
        // ensures: top == elem
        Verify.ignoreIf(!((m2_top$null == 0 && m2_top == c1_elem)));

        // call 2: pop
        // requires: size > 0
        Verify.ignoreIf(!((m2_size > 0)));
        int c2_result = index("c2_result");
        int c2_result$null = flag("c2_result$null");
        int m3_size = Debug.makeSymbolicInteger("m3_size");
        // ensures: \result == poppedElem  (only names the result)
        // ensures: size == \old(size) - 1
        Verify.ignoreIf(!((m3_size == (m2_size - 1))));

        // call 3: peek
        // requires: size > 0
        Verify.ignoreIf(!((m3_size > 0)));
        int c3_result = index("c3_result");
        int c3_result$null = flag("c3_result$null");
        int m4_size = Debug.makeSymbolicInteger("m4_size");
        int m4_top = index("m4_top");
        int m4_top$null = flag("m4_top$null");
        // ensures: \result == peekedElem  (only names the result)
        // ensures: peekedElem == top
        Verify.ignoreIf(!(((c3_result$null == 1 && m4_top$null == 1) || (c3_result$null == 0 && m4_top$null == 0 && c3_result == m4_top))));
        // ensures: size == \old(size)
        Verify.ignoreIf(!((m4_size == m3_size)));
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

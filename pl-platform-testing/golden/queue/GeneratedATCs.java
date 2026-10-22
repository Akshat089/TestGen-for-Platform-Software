package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/**
 * Symbolic driver for SPF, built from ../specs/Queue.spec only.
 * It models the library state itself and never calls the library.
 */
public class GeneratedATCs {

    public static void main(String[] args) {
        // starting state
        int[] m0_Q = new int[3];
        int m0_size = 0;
        int m0_head = 0;
        int m0_head$null = 1;

        // call 0: enqueue
        int c0_elem = index("c0_elem");
        // requires: elem != null
        Verify.ignoreIf(!(!(0 == 1)));
        int[] m1_Q = { flag("m1_Q_0"), flag("m1_Q_1"), flag("m1_Q_2") };
        Verify.ignoreIf(!(c0_elem == 0 || m1_Q[0] == m0_Q[0]));
        Verify.ignoreIf(!(c0_elem == 1 || m1_Q[1] == m0_Q[1]));
        Verify.ignoreIf(!(c0_elem == 2 || m1_Q[2] == m0_Q[2]));
        int m1_size = Debug.makeSymbolicInteger("m1_size");
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m1_size == (m0_size + 1))));
        // ensures: Q.contains(elem)
        Verify.ignoreIf(!((m1_Q[c0_elem] == 1)));

        // call 1: enqueue
        int c1_elem = index("c1_elem");
        // requires: elem != null
        Verify.ignoreIf(!(!(0 == 1)));
        int[] m2_Q = { flag("m2_Q_0"), flag("m2_Q_1"), flag("m2_Q_2") };
        Verify.ignoreIf(!(c1_elem == 0 || m2_Q[0] == m1_Q[0]));
        Verify.ignoreIf(!(c1_elem == 1 || m2_Q[1] == m1_Q[1]));
        Verify.ignoreIf(!(c1_elem == 2 || m2_Q[2] == m1_Q[2]));
        int m2_size = Debug.makeSymbolicInteger("m2_size");
        // ensures: size == \old(size) + 1
        Verify.ignoreIf(!((m2_size == (m1_size + 1))));
        // ensures: Q.contains(elem)
        Verify.ignoreIf(!((m2_Q[c1_elem] == 1)));

        // call 2: dequeue
        // requires: size > 0
        Verify.ignoreIf(!((m2_size > 0)));
        int c2_result = index("c2_result");
        int c2_result$null = flag("c2_result$null");
        int m3_size = Debug.makeSymbolicInteger("m3_size");
        // ensures: \result == dequeuedElem  (only names the result)
        // ensures: dequeuedElem == \old(head)
        Verify.ignoreIf(!(((c2_result$null == 1 && m0_head$null == 1) || (c2_result$null == 0 && m0_head$null == 0 && c2_result == m0_head))));
        // ensures: size == \old(size) - 1
        Verify.ignoreIf(!((m3_size == (m2_size - 1))));

        // call 3: front
        // requires: size > 0
        Verify.ignoreIf(!((m3_size > 0)));
        int c3_result = index("c3_result");
        int c3_result$null = flag("c3_result$null");
        int m4_size = Debug.makeSymbolicInteger("m4_size");
        int m4_head = index("m4_head");
        int m4_head$null = flag("m4_head$null");
        // ensures: \result == frontElem  (only names the result)
        // ensures: frontElem == head
        Verify.ignoreIf(!(((c3_result$null == 1 && m4_head$null == 1) || (c3_result$null == 0 && m4_head$null == 0 && c3_result == m4_head))));
        // ensures: size == \old(size)
        Verify.ignoreIf(!((m4_size == m3_size)));

        // call 4: isEmpty
        // requires: size >= 0
        Verify.ignoreIf(!((m4_size >= 0)));
        boolean c4_result = Debug.makeSymbolicBoolean("c4_result");
        int m5_size = Debug.makeSymbolicInteger("m5_size");
        // ensures: \result == emptyFlag  (only names the result)
        // ensures: emptyFlag == false
        Verify.ignoreIf(!((c4_result == false)));
        // ensures: size == \old(size)
        Verify.ignoreIf(!((m5_size == m4_size)));
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

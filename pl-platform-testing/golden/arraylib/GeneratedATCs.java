package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/**
 * Symbolic driver for SPF, built from ../specs/IntArray.spec only.
 * It models the library state itself and never calls the library.
 */
public class GeneratedATCs {

    public static void main(String[] args) {
        // starting state
        int[] m0_A = {0, 0, 0, 0};
        int m0_length = 4;
        int m0_writes = 0;

        // call 0: set
        int c0_index = Debug.makeSymbolicInteger("c0_index");
        int c0_value = Debug.makeSymbolicInteger("c0_value");
        // requires: index >= 0
        Verify.ignoreIf(!((c0_index >= 0)));
        // requires: index < length
        Verify.ignoreIf(!((c0_index < m0_length)));
        int[] m1_A = { Debug.makeSymbolicInteger("m1_A_0"), Debug.makeSymbolicInteger("m1_A_1"), Debug.makeSymbolicInteger("m1_A_2"), Debug.makeSymbolicInteger("m1_A_3") };
        Verify.ignoreIf(!(c0_index == 0 || m1_A[0] == m0_A[0]));
        Verify.ignoreIf(!(c0_index == 1 || m1_A[1] == m0_A[1]));
        Verify.ignoreIf(!(c0_index == 2 || m1_A[2] == m0_A[2]));
        Verify.ignoreIf(!(c0_index == 3 || m1_A[3] == m0_A[3]));
        int m1_writes = Debug.makeSymbolicInteger("m1_writes");
        // ensures: A[index] == value
        Verify.ignoreIf(!((m1_A[c0_index] == c0_value)));
        // ensures: writes == \old(writes) + 1
        Verify.ignoreIf(!((m1_writes == (m0_writes + 1))));

        // call 1: set
        int c1_index = Debug.makeSymbolicInteger("c1_index");
        int c1_value = Debug.makeSymbolicInteger("c1_value");
        // requires: index >= 0
        Verify.ignoreIf(!((c1_index >= 0)));
        // requires: index < length
        Verify.ignoreIf(!((c1_index < m0_length)));
        int[] m2_A = { Debug.makeSymbolicInteger("m2_A_0"), Debug.makeSymbolicInteger("m2_A_1"), Debug.makeSymbolicInteger("m2_A_2"), Debug.makeSymbolicInteger("m2_A_3") };
        Verify.ignoreIf(!(c1_index == 0 || m2_A[0] == m1_A[0]));
        Verify.ignoreIf(!(c1_index == 1 || m2_A[1] == m1_A[1]));
        Verify.ignoreIf(!(c1_index == 2 || m2_A[2] == m1_A[2]));
        Verify.ignoreIf(!(c1_index == 3 || m2_A[3] == m1_A[3]));
        int m2_writes = Debug.makeSymbolicInteger("m2_writes");
        // ensures: A[index] == value
        Verify.ignoreIf(!((m2_A[c1_index] == c1_value)));
        // ensures: writes == \old(writes) + 1
        Verify.ignoreIf(!((m2_writes == (m1_writes + 1))));

        // call 2: get
        int c2_index = Debug.makeSymbolicInteger("c2_index");
        // requires: index >= 0
        Verify.ignoreIf(!((c2_index >= 0)));
        // requires: index < length
        Verify.ignoreIf(!((c2_index < m0_length)));
        int c2_result = Debug.makeSymbolicInteger("c2_result");
        int[] m3_A = { Debug.makeSymbolicInteger("m3_A_0"), Debug.makeSymbolicInteger("m3_A_1"), Debug.makeSymbolicInteger("m3_A_2"), Debug.makeSymbolicInteger("m3_A_3") };
        Verify.ignoreIf(!(c2_index == 0 || m3_A[0] == m2_A[0]));
        Verify.ignoreIf(!(c2_index == 1 || m3_A[1] == m2_A[1]));
        Verify.ignoreIf(!(c2_index == 2 || m3_A[2] == m2_A[2]));
        Verify.ignoreIf(!(c2_index == 3 || m3_A[3] == m2_A[3]));
        int m3_writes = Debug.makeSymbolicInteger("m3_writes");
        // ensures: \result == elem  (only names the result)
        // ensures: elem == A[index]
        Verify.ignoreIf(!((c2_result == m3_A[c2_index])));
        // ensures: writes == \old(writes)
        Verify.ignoreIf(!((m3_writes == m2_writes)));

        // call 3: sum
        // requires: length > 0
        Verify.ignoreIf(!((m0_length > 0)));
        int c3_result = Debug.makeSymbolicInteger("c3_result");
        int m4_writes = Debug.makeSymbolicInteger("m4_writes");
        // ensures: \result == total  (only names the result)
        // ensures: total >= 0
        Verify.ignoreIf(!((c3_result >= 0)));
        // ensures: writes == \old(writes)
        Verify.ignoreIf(!((m4_writes == m3_writes)));

        // call 4: indexOf
        int c4_value = Debug.makeSymbolicInteger("c4_value");
        // requires: length > 0
        Verify.ignoreIf(!((m0_length > 0)));
        int c4_result = Debug.makeSymbolicInteger("c4_result");
        int m5_length = Debug.makeSymbolicInteger("m5_length");
        int m5_writes = Debug.makeSymbolicInteger("m5_writes");
        // ensures: \result == position  (only names the result)
        // ensures: position >= -1
        Verify.ignoreIf(!((c4_result >= (-1))));
        // ensures: position < length
        Verify.ignoreIf(!((c4_result < m5_length)));
        // ensures: writes == \old(writes)
        Verify.ignoreIf(!((m5_writes == m4_writes)));
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

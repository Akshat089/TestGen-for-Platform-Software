package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/** Symbolic driver for SPF. Not shipped with the JUnit test class (rule 11). */
public class GeneratedATCs {

    private static final int[] POOL_int = {101, 103, 105};

    public static void main(String[] args) {
        Helper.reset();

        // call 0: insert
        int c0_key$idx = Debug.makeSymbolicInteger("c0_key$idx");
        Verify.ignoreIf(!(c0_key$idx >= 0 && c0_key$idx < 3));
        int c0_key = POOL_int[c0_key$idx];
        Verify.ignoreIf(!(!Helper.Keys.contains(c0_key)));
        int old0_size = Helper.size;
        Helper.insert(c0_key);

        // call 1: insert
        int c1_key$idx = Debug.makeSymbolicInteger("c1_key$idx");
        Verify.ignoreIf(!(c1_key$idx >= 0 && c1_key$idx < 3));
        int c1_key = POOL_int[c1_key$idx];
        Verify.ignoreIf(!(!Helper.Keys.contains(c1_key)));
        int old1_size = Helper.size;
        Helper.insert(c1_key);

        // call 2: insert
        int c2_key$idx = Debug.makeSymbolicInteger("c2_key$idx");
        Verify.ignoreIf(!(c2_key$idx >= 0 && c2_key$idx < 3));
        int c2_key = POOL_int[c2_key$idx];
        Verify.ignoreIf(!(!Helper.Keys.contains(c2_key)));
        int old2_size = Helper.size;
        Helper.insert(c2_key);

        // call 3: contains
        int c3_key$idx = Debug.makeSymbolicInteger("c3_key$idx");
        Verify.ignoreIf(!(c3_key$idx >= 0 && c3_key$idx < 3));
        int c3_key = POOL_int[c3_key$idx];
        Verify.ignoreIf(!(Helper.size > 0));
        int old3_size = Helper.size;
        boolean c3_result = Helper.contains(c3_key);

        // call 4: min
        Verify.ignoreIf(!(Helper.size > 0));
        int old4_size = Helper.size;
        int c4_result = Helper.min();

        // call 5: delete
        int c5_key$idx = Debug.makeSymbolicInteger("c5_key$idx");
        Verify.ignoreIf(!(c5_key$idx >= 0 && c5_key$idx < 3));
        int c5_key = POOL_int[c5_key$idx];
        Verify.ignoreIf(!(Helper.Keys.contains(c5_key)));
        int old5_size = Helper.size;
        Helper.delete(c5_key);
    }
}

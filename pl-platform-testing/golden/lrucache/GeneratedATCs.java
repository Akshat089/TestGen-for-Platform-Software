package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/** Symbolic driver for SPF. Not shipped with the JUnit test class (rule 11). */
public class GeneratedATCs {

    private static final String[] POOL_String = {"s1", "s2", "s3"};

    public static void main(String[] args) {
        Helper.reset();

        // call 0: put
        int c0_key$idx = Debug.makeSymbolicInteger("c0_key$idx");
        Verify.ignoreIf(!(c0_key$idx >= 0 && c0_key$idx < 3));
        String c0_key = POOL_String[c0_key$idx];
        int c0_value$idx = Debug.makeSymbolicInteger("c0_value$idx");
        Verify.ignoreIf(!(c0_value$idx >= 0 && c0_value$idx < 3));
        String c0_value = POOL_String[c0_value$idx];
        Verify.ignoreIf(!(c0_key != null));
        Verify.ignoreIf(!(c0_value != null));
        Verify.ignoreIf(!(Helper.capacity > 0));
        String c0_result = Helper.put(c0_key, c0_value);

        // call 1: put
        int c1_key$idx = Debug.makeSymbolicInteger("c1_key$idx");
        Verify.ignoreIf(!(c1_key$idx >= 0 && c1_key$idx < 3));
        String c1_key = POOL_String[c1_key$idx];
        int c1_value$idx = Debug.makeSymbolicInteger("c1_value$idx");
        Verify.ignoreIf(!(c1_value$idx >= 0 && c1_value$idx < 3));
        String c1_value = POOL_String[c1_value$idx];
        Verify.ignoreIf(!(c1_key != null));
        Verify.ignoreIf(!(c1_value != null));
        Verify.ignoreIf(!(Helper.capacity > 0));
        String c1_result = Helper.put(c1_key, c1_value);

        // call 2: put
        int c2_key$idx = Debug.makeSymbolicInteger("c2_key$idx");
        Verify.ignoreIf(!(c2_key$idx >= 0 && c2_key$idx < 3));
        String c2_key = POOL_String[c2_key$idx];
        int c2_value$idx = Debug.makeSymbolicInteger("c2_value$idx");
        Verify.ignoreIf(!(c2_value$idx >= 0 && c2_value$idx < 3));
        String c2_value = POOL_String[c2_value$idx];
        Verify.ignoreIf(!(c2_key != null));
        Verify.ignoreIf(!(c2_value != null));
        Verify.ignoreIf(!(Helper.capacity > 0));
        String c2_result = Helper.put(c2_key, c2_value);

        // call 3: get
        int c3_key$idx = Debug.makeSymbolicInteger("c3_key$idx");
        Verify.ignoreIf(!(c3_key$idx >= 0 && c3_key$idx < 3));
        String c3_key = POOL_String[c3_key$idx];
        Verify.ignoreIf(!(Helper.C.containsKey(c3_key)));
        int old3_size = Helper.size;
        String c3_result = Helper.get(c3_key);

        // call 4: restore
        int c4_evictedKey$idx = Debug.makeSymbolicInteger("c4_evictedKey$idx");
        Verify.ignoreIf(!(c4_evictedKey$idx >= 0 && c4_evictedKey$idx < 3));
        String c4_evictedKey = POOL_String[c4_evictedKey$idx];
        int c4_value$idx = Debug.makeSymbolicInteger("c4_value$idx");
        Verify.ignoreIf(!(c4_value$idx >= 0 && c4_value$idx < 3));
        String c4_value = POOL_String[c4_value$idx];
        Verify.ignoreIf(!(c4_evictedKey != null));
        Verify.ignoreIf(!(!Helper.C.containsKey(c4_evictedKey)));
        Helper.restore(c4_evictedKey, c4_value);
    }
}

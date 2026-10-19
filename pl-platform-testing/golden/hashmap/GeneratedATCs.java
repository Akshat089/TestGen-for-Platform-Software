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
        Integer c0_value = Debug.makeSymbolicInteger("c0_value");
        Verify.ignoreIf(!(c0_key != null));
        Debug.assume(c0_value != null);
        Integer c0_result = Helper.put(c0_key, c0_value);

        // call 1: put
        int c1_key$idx = Debug.makeSymbolicInteger("c1_key$idx");
        Verify.ignoreIf(!(c1_key$idx >= 0 && c1_key$idx < 3));
        String c1_key = POOL_String[c1_key$idx];
        Integer c1_value = Debug.makeSymbolicInteger("c1_value");
        Verify.ignoreIf(!(c1_key != null));
        Debug.assume(c1_value != null);
        Integer c1_result = Helper.put(c1_key, c1_value);

        // call 2: getOldValue
        int c2_key$idx = Debug.makeSymbolicInteger("c2_key$idx");
        Verify.ignoreIf(!(c2_key$idx >= 0 && c2_key$idx < 3));
        String c2_key = POOL_String[c2_key$idx];
        Verify.ignoreIf(!(Helper.M.containsKey(c2_key)));
        int old2_size = Helper.size;
        Integer c2_result = Helper.getOldValue(c2_key);

        // call 3: remove
        int c3_key$idx = Debug.makeSymbolicInteger("c3_key$idx");
        Verify.ignoreIf(!(c3_key$idx >= 0 && c3_key$idx < 3));
        String c3_key = POOL_String[c3_key$idx];
        Verify.ignoreIf(!(Helper.M.containsKey(c3_key)));
        int old3_size = Helper.size;
        Helper.remove(c3_key);
    }
}

package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/** Symbolic driver for SPF. Not shipped with the JUnit test class (rule 11). */
public class GeneratedATCs {

    public static void main(String[] args) {
        Helper.reset();

        // call 0: insert
        Integer c0_value = Debug.makeSymbolicInteger("c0_value");
        Debug.assume(c0_value != null);
        int old0_size = Helper.size;
        Helper.insert(c0_value);

        // call 1: insert
        Integer c1_value = Debug.makeSymbolicInteger("c1_value");
        Debug.assume(c1_value != null);
        int old1_size = Helper.size;
        Helper.insert(c1_value);

        // call 2: insert
        Integer c2_value = Debug.makeSymbolicInteger("c2_value");
        Debug.assume(c2_value != null);
        int old2_size = Helper.size;
        Helper.insert(c2_value);

        // call 3: peekMin
        Verify.ignoreIf(!(Helper.size > 0));
        int old3_size = Helper.size;
        Integer c3_result = Helper.peekMin();

        // call 4: extractMin
        Verify.ignoreIf(!(Helper.size > 0));
        Integer old4_minValue = Helper.minValue;
        int old4_size = Helper.size;
        Integer c4_result = Helper.extractMin();
    }
}

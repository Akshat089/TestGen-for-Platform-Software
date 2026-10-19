package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/** Symbolic driver for SPF. Not shipped with the JUnit test class (rule 11). */
public class GeneratedATCs {

    public static void main(String[] args) {
        Helper.reset();

        // call 0: set
        int c0_index = Debug.makeSymbolicInteger("c0_index");
        int c0_value = Debug.makeSymbolicInteger("c0_value");
        Debug.assume(c0_index >= 0);
        Debug.assume(c0_index < Helper.length);
        int old0_writes = Helper.writes;
        Helper.set(c0_index, c0_value);

        // call 1: set
        int c1_index = Debug.makeSymbolicInteger("c1_index");
        int c1_value = Debug.makeSymbolicInteger("c1_value");
        Debug.assume(c1_index >= 0);
        Debug.assume(c1_index < Helper.length);
        int old1_writes = Helper.writes;
        Helper.set(c1_index, c1_value);

        // call 2: get
        int c2_index = Debug.makeSymbolicInteger("c2_index");
        Debug.assume(c2_index >= 0);
        Debug.assume(c2_index < Helper.length);
        int old2_writes = Helper.writes;
        int c2_result = Helper.get(c2_index);

        // call 3: sum
        Verify.ignoreIf(!(Helper.length > 0));
        int old3_writes = Helper.writes;
        int c3_result = Helper.sum();

        // call 4: indexOf
        int c4_value = Debug.makeSymbolicInteger("c4_value");
        Verify.ignoreIf(!(Helper.length > 0));
        int old4_writes = Helper.writes;
        int c4_result = Helper.indexOf(c4_value);
    }
}

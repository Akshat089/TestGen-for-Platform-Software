package in.ac.iiitb.plproject.atc.generated;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

/**
 * Symbolic driver for SPF, built from ../specs/Graph.spec only.
 * It models the library state itself and never calls the library.
 */
public class GeneratedATCs {

    public static void main(String[] args) {
        // starting state
        int[] m0_V = new int[3];
        int[] m0_Adj$has = new int[3];
        int[] m0_Adj$in = new int[9];
        int m0_edgeCount = 0;

        // call 0: addVertex
        int c0_name = index("c0_name");
        // requires: name != null
        Verify.ignoreIf(!(!(0 == 1)));
        int[] m1_V = { flag("m1_V_0"), flag("m1_V_1"), flag("m1_V_2") };
        Verify.ignoreIf(!(c0_name == 0 || m1_V[0] == m0_V[0]));
        Verify.ignoreIf(!(c0_name == 1 || m1_V[1] == m0_V[1]));
        Verify.ignoreIf(!(c0_name == 2 || m1_V[2] == m0_V[2]));
        int m1_edgeCount = Debug.makeSymbolicInteger("m1_edgeCount");
        // ensures: V.contains(name)
        Verify.ignoreIf(!((m1_V[c0_name] == 1)));
        // ensures: edgeCount == \old(edgeCount)
        Verify.ignoreIf(!((m1_edgeCount == m0_edgeCount)));

        // call 1: addVertex
        int c1_name = index("c1_name");
        // requires: name != null
        Verify.ignoreIf(!(!(0 == 1)));
        int[] m2_V = { flag("m2_V_0"), flag("m2_V_1"), flag("m2_V_2") };
        Verify.ignoreIf(!(c1_name == 0 || m2_V[0] == m1_V[0]));
        Verify.ignoreIf(!(c1_name == 1 || m2_V[1] == m1_V[1]));
        Verify.ignoreIf(!(c1_name == 2 || m2_V[2] == m1_V[2]));
        int m2_edgeCount = Debug.makeSymbolicInteger("m2_edgeCount");
        // ensures: V.contains(name)
        Verify.ignoreIf(!((m2_V[c1_name] == 1)));
        // ensures: edgeCount == \old(edgeCount)
        Verify.ignoreIf(!((m2_edgeCount == m1_edgeCount)));

        // call 2: addEdge
        int c2_from = index("c2_from");
        int c2_to = index("c2_to");
        // requires: V.contains(from)
        Verify.ignoreIf(!((m2_V[c2_from] == 1)));
        // requires: V.contains(to)
        Verify.ignoreIf(!((m2_V[c2_to] == 1)));
        int[] m3_Adj$has = { flag("m3_Adj$has_0"), flag("m3_Adj$has_1"), flag("m3_Adj$has_2") };
        int[] m3_Adj$in = { flag("m3_Adj$in_0"), flag("m3_Adj$in_1"), flag("m3_Adj$in_2"), flag("m3_Adj$in_3"), flag("m3_Adj$in_4"), flag("m3_Adj$in_5"), flag("m3_Adj$in_6"), flag("m3_Adj$in_7"), flag("m3_Adj$in_8") };
        Verify.ignoreIf(!(c2_from == 0 || c2_to == 0 || m3_Adj$has[0] == m0_Adj$has[0]));
        Verify.ignoreIf(!(c2_from == 1 || c2_to == 1 || m3_Adj$has[1] == m0_Adj$has[1]));
        Verify.ignoreIf(!(c2_from == 2 || c2_to == 2 || m3_Adj$has[2] == m0_Adj$has[2]));
        Verify.ignoreIf(!((c2_from * 3 + c2_to) == 0 || (c2_to * 3 + c2_from) == 0 || m3_Adj$in[0] == m0_Adj$in[0]));
        Verify.ignoreIf(!((c2_from * 3 + c2_to) == 1 || (c2_to * 3 + c2_from) == 1 || m3_Adj$in[1] == m0_Adj$in[1]));
        Verify.ignoreIf(!((c2_from * 3 + c2_to) == 2 || (c2_to * 3 + c2_from) == 2 || m3_Adj$in[2] == m0_Adj$in[2]));
        Verify.ignoreIf(!((c2_from * 3 + c2_to) == 3 || (c2_to * 3 + c2_from) == 3 || m3_Adj$in[3] == m0_Adj$in[3]));
        Verify.ignoreIf(!((c2_from * 3 + c2_to) == 4 || (c2_to * 3 + c2_from) == 4 || m3_Adj$in[4] == m0_Adj$in[4]));
        Verify.ignoreIf(!((c2_from * 3 + c2_to) == 5 || (c2_to * 3 + c2_from) == 5 || m3_Adj$in[5] == m0_Adj$in[5]));
        Verify.ignoreIf(!((c2_from * 3 + c2_to) == 6 || (c2_to * 3 + c2_from) == 6 || m3_Adj$in[6] == m0_Adj$in[6]));
        Verify.ignoreIf(!((c2_from * 3 + c2_to) == 7 || (c2_to * 3 + c2_from) == 7 || m3_Adj$in[7] == m0_Adj$in[7]));
        Verify.ignoreIf(!((c2_from * 3 + c2_to) == 8 || (c2_to * 3 + c2_from) == 8 || m3_Adj$in[8] == m0_Adj$in[8]));
        int m3_edgeCount = Debug.makeSymbolicInteger("m3_edgeCount");
        // ensures: Adj.get(from).contains(to)
        Verify.ignoreIf(!((m3_Adj$has[c2_from] == 1) && (m3_Adj$in[c2_from * 3 + c2_to] == 1)));
        // ensures: Adj.get(to).contains(from)
        Verify.ignoreIf(!((m3_Adj$has[c2_to] == 1) && (m3_Adj$in[c2_to * 3 + c2_from] == 1)));
        // ensures: edgeCount == \old(edgeCount) + 1
        Verify.ignoreIf(!((m3_edgeCount == (m2_edgeCount + 1))));

        // call 3: degree
        int c3_name = index("c3_name");
        // requires: V.contains(name)
        Verify.ignoreIf(!((m2_V[c3_name] == 1)));
        int c3_result = Debug.makeSymbolicInteger("c3_result");
        int[] m4_Adj$has = { flag("m4_Adj$has_0"), flag("m4_Adj$has_1"), flag("m4_Adj$has_2") };
        int[] m4_Adj$in = { flag("m4_Adj$in_0"), flag("m4_Adj$in_1"), flag("m4_Adj$in_2"), flag("m4_Adj$in_3"), flag("m4_Adj$in_4"), flag("m4_Adj$in_5"), flag("m4_Adj$in_6"), flag("m4_Adj$in_7"), flag("m4_Adj$in_8") };
        int m4_edgeCount = Debug.makeSymbolicInteger("m4_edgeCount");
        // ensures: \result == vertexDegree  (only names the result)
        // CHECK-ONLY ensures: vertexDegree == Adj.get(name).size()  (.size(...) has no model (a List may hold duplicates))
        // ensures: vertexDegree >= 0
        Verify.ignoreIf(!((c3_result >= 0)));
        // ensures: edgeCount == \old(edgeCount)
        Verify.ignoreIf(!((m4_edgeCount == m3_edgeCount)));

        // call 4: hasEdge
        int c4_from = index("c4_from");
        int c4_to = index("c4_to");
        // requires: V.contains(from)
        Verify.ignoreIf(!((m2_V[c4_from] == 1)));
        // requires: V.contains(to)
        Verify.ignoreIf(!((m2_V[c4_to] == 1)));
        boolean c4_result = Debug.makeSymbolicBoolean("c4_result");
        int[] m5_Adj$has = { flag("m5_Adj$has_0"), flag("m5_Adj$has_1"), flag("m5_Adj$has_2") };
        int[] m5_Adj$in = { flag("m5_Adj$in_0"), flag("m5_Adj$in_1"), flag("m5_Adj$in_2"), flag("m5_Adj$in_3"), flag("m5_Adj$in_4"), flag("m5_Adj$in_5"), flag("m5_Adj$in_6"), flag("m5_Adj$in_7"), flag("m5_Adj$in_8") };
        Verify.ignoreIf(!(c4_from == 0 || m5_Adj$has[0] == m4_Adj$has[0]));
        Verify.ignoreIf(!(c4_from == 1 || m5_Adj$has[1] == m4_Adj$has[1]));
        Verify.ignoreIf(!(c4_from == 2 || m5_Adj$has[2] == m4_Adj$has[2]));
        Verify.ignoreIf(!((c4_from * 3 + c4_to) == 0 || m5_Adj$in[0] == m4_Adj$in[0]));
        Verify.ignoreIf(!((c4_from * 3 + c4_to) == 1 || m5_Adj$in[1] == m4_Adj$in[1]));
        Verify.ignoreIf(!((c4_from * 3 + c4_to) == 2 || m5_Adj$in[2] == m4_Adj$in[2]));
        Verify.ignoreIf(!((c4_from * 3 + c4_to) == 3 || m5_Adj$in[3] == m4_Adj$in[3]));
        Verify.ignoreIf(!((c4_from * 3 + c4_to) == 4 || m5_Adj$in[4] == m4_Adj$in[4]));
        Verify.ignoreIf(!((c4_from * 3 + c4_to) == 5 || m5_Adj$in[5] == m4_Adj$in[5]));
        Verify.ignoreIf(!((c4_from * 3 + c4_to) == 6 || m5_Adj$in[6] == m4_Adj$in[6]));
        Verify.ignoreIf(!((c4_from * 3 + c4_to) == 7 || m5_Adj$in[7] == m4_Adj$in[7]));
        Verify.ignoreIf(!((c4_from * 3 + c4_to) == 8 || m5_Adj$in[8] == m4_Adj$in[8]));
        int m5_edgeCount = Debug.makeSymbolicInteger("m5_edgeCount");
        // ensures: \result == connected  (only names the result)
        // ensures: connected == Adj.get(from).contains(to)
        Verify.ignoreIf(!((m5_Adj$has[c4_from] == 1) && (c4_result == (m5_Adj$in[c4_from * 3 + c4_to] == 1))));
        // ensures: edgeCount == \old(edgeCount)
        Verify.ignoreIf(!((m5_edgeCount == m4_edgeCount)));
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

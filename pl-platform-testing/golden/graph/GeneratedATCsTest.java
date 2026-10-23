package in.ac.iiitb.plproject.atc.generated;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Generated from ../specs/Graph.spec + ../specs/Graph.tests.
 * One @Test method per sequence: the concrete test case genCTC returned,
 * statement for statement; every assertion is one clause of the contract.
 */
public class GeneratedATCsTest {

    // sequence: addVertex -> addVertex -> addEdge -> degree -> hasEdge
    @Test(timeout = 2000)
    public void graphDryRun() {
        Helper.reset();

        // call 0: addVertex
        int old0_edgeCount = Helper.edgeCount;
        String c0_name = "s1";
        assertTrue("PRE addVertex: name != null", c0_name != null);
        Helper.addVertex(c0_name);
        assertTrue("POST addVertex: V.contains(name)", Helper.V.contains(c0_name));
        assertTrue("POST addVertex: edgeCount == \\old(edgeCount)", Helper.edgeCount == old0_edgeCount);

        // call 1: addVertex
        int old1_edgeCount = Helper.edgeCount;
        String c1_name = "s2";
        assertTrue("PRE addVertex: name != null", c1_name != null);
        Helper.addVertex(c1_name);
        assertTrue("POST addVertex: V.contains(name)", Helper.V.contains(c1_name));
        assertTrue("POST addVertex: edgeCount == \\old(edgeCount)", Helper.edgeCount == old1_edgeCount);

        // call 2: addEdge
        int old2_edgeCount = Helper.edgeCount;
        String c2_from = "s1";
        String c2_to = "s1";
        assertTrue("PRE addEdge: V.contains(from)", Helper.V.contains(c2_from));
        assertTrue("PRE addEdge: V.contains(to)", Helper.V.contains(c2_to));
        Helper.addEdge(c2_from, c2_to);
        assertTrue("POST addEdge: Adj.get(from).contains(to)", Helper.Adj.get(c2_from).contains(c2_to));
        assertTrue("POST addEdge: Adj.get(to).contains(from)", Helper.Adj.get(c2_to).contains(c2_from));
        assertTrue("POST addEdge: edgeCount == \\old(edgeCount) + 1", Helper.edgeCount == old2_edgeCount + 1);

        // call 3: degree
        int old3_edgeCount = Helper.edgeCount;
        String c3_name = "s2";
        assertTrue("PRE degree: V.contains(name)", Helper.V.contains(c3_name));
        int c3_result = Helper.degree(c3_name);
        assertTrue("POST degree: vertexDegree == Adj.get(name).size()", c3_result == Helper.Adj.get(c3_name).size());
        assertTrue("POST degree: vertexDegree >= 0", c3_result >= 0);
        assertTrue("POST degree: edgeCount == \\old(edgeCount)", Helper.edgeCount == old3_edgeCount);

        // call 4: hasEdge
        int old4_edgeCount = Helper.edgeCount;
        String c4_from = "s1";
        String c4_to = "s1";
        assertTrue("PRE hasEdge: V.contains(from)", Helper.V.contains(c4_from));
        assertTrue("PRE hasEdge: V.contains(to)", Helper.V.contains(c4_to));
        boolean c4_result = Helper.hasEdge(c4_from, c4_to);
        assertTrue("POST hasEdge: connected == Adj.get(from).contains(to)", c4_result == Helper.Adj.get(c4_from).contains(c4_to));
        assertTrue("POST hasEdge: edgeCount == \\old(edgeCount)", Helper.edgeCount == old4_edgeCount);
    }
}

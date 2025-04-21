package cs3110.hw4;

import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Alex Myska
 */
public class WeightedAdjacencyListTests<T> {

    /**
     *    Notes:
     *
     *    These tests only cover the WeightedAdjacencyList class, and there are no tests
     *    for CharacterSeparator. This is because CharacterSeparator is only for images,
     *    and I don't know what images you have on your computer.
     *
     *    The WeightedAdjacencyList class NEEDS to work with generic types. I went to
     *    office hours and was told this directly, so ignoring it and making your program
     *    work with only images is wrong.
     *
     *    There is more than one way to implement the adjacency list, so this class will
     *    assume you have all your methods done and simply call the methods given in
     *    the skeleton code.
     *
     *    By default, every test is annotated with @Test and @Ignore. As you implement your
     *    class, remove the @Ignore tags that correspond to the tests you want to run. I've
     *    Javadoc-ed every test so you have an idea of what might be useful to you. They're
     *    also loosely ordered on the order I tested my own code in.
     *
     *    Some tests will track the amount of time your algorithm takes to run. Mine takes
     *    around 5ms give or take for each of them, and my implementation is pretty simple
     *    and unoptimized, so you can use that as a baseline. Either way, these aren't great
     *    indicators of how fast your program actually is, since in reality most graphs
     *    (including the images we can run through this thing) are going to contain hundreds
     *    to thousands of nodes and edges, far outpacing what I can reasonably write by hand.
     *
     *    IF YOUR CODE DOES NOT PASS THESE TESTS IT IS NOT NECESSARILY WRONG!!!
     *    There is more than one way to do this assignment, and you should modify the tests
     *    as needed to suit your own code.
     *
     *    Good luck!
     */

    @Test
    //@Ignore
    /**
     * Test 1 - getVertexCount(), hasVertex(), getVertices()
     *
     * Graph used for this test:
     *
     *    [A]     [B]
     *
     *
     *    [C]     [D]
     */
    public void test1() {
        List<T> vertices = List.of((T[]) new String[]{"A", "B", "C", "D"});
        WeightedAdjacencyList<T> W = new WeightedAdjacencyList<>(vertices);

        assertEquals("W should have 4 vertices.", 4, W.getVertexCount());
        assertTrue("W should contain 'A'.", W.hasVertex((T) "A"));
    }

    @Test
    //@Ignore
    /**
     * Test 2 - addEdge(), getEdgeCount(), hasEdge(), getNeighbors(), areNeighbors()
     *
     * Graph used for this test: (all weights 0)
     *
     *    [A] --> [B]
     *     |
     *     V
     *    [C] --> [D]
     */
    public void test2() {
        List<T> vertices = List.of((T[]) new String[]{"A", "B", "C", "D"});
        WeightedAdjacencyList<T> W = new WeightedAdjacencyList<>(vertices);

        assertTrue("addEdge() should return 'true'.", W.addEdge((T) "A", (T) "B", 0));
        assertFalse("addEdge() should return 'false' because the edge A-B already exists.", W.addEdge((T) "A", (T) "B", 0));
        W.addEdge((T) "C", (T) "D", 0);
        W.addEdge((T) "A", (T) "C", 0);

        assertFalse("addEdge() should return 'false' because the graph does not contain 'E'.", W.addEdge((T) "A", (T) "E", 0));
        assertEquals("W should have 3 edges.", 3, W.getEdgeCount());
        assertTrue("The edge A-B should be in W.", W.hasEdge((T) "A", (T) "B"));
        assertFalse("The edge A-D should not be in W.", W.hasEdge((T) "A", (T) "D"));
        ArrayList<T> neighbors = (ArrayList) W.getNeighbors((T) "A");
        assertEquals("A should have 2 neighbors.", 2, neighbors.size());
        assertTrue("C should be a neighbor of D.", W.areNeighbors((T) "C", (T) "D"));
        assertFalse("B should not be a neighbor of D.", W.areNeighbors((T) "B", (T) "D"));
    }

    @Test
    //@Ignore
    /**
     * Test 3 - getShortestPaths(). Also tests time taken.
     *
     * Graph used for this test:
     *
     *    [A] --> [B]
     *     |       |
     *     V       V
     *    [C] --> [D]
     *
     *    A-B = 2
     *    A-C = 1
     *    B-D = 3
     *    C-D = 0
     */
    public void test3() {
        long startTime = System.currentTimeMillis();
        List<T> vertices = List.of((T[]) new String[]{"A", "B", "C", "D"});
        WeightedAdjacencyList<T> W = new WeightedAdjacencyList<>(vertices);

        W.addEdge((T) "A", (T) "B", 2);
        W.addEdge((T) "A", (T) "C", 1);
        W.addEdge((T) "B", (T) "D", 3);
        W.addEdge((T) "C", (T) "D", 0);

        Map<T, Long> shortestPaths = W.getShortestPaths((T) "A");
        Long aa = 0L;
        Long ab = 2L;
        Long ac = 1L;
        Long ad = 1L;
        assertEquals("A -> A should be 0.", aa, shortestPaths.get((T) "A"));
        assertEquals("A -> B should be 2.", ab, shortestPaths.get((T) "B"));
        assertEquals("A -> C should be 1.", ac, shortestPaths.get((T) "C"));
        assertEquals("A -> D should be 1.", ad, shortestPaths.get((T) "D"));

        long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time for test 3: " + (endTime - startTime) + "ms");
    }

    @Test
    //@Ignore
    /**
     * Test 4 - getShortestPaths(). Also tests time taken.
     *
     * Graph used for this test:
     *
     *    [A] --> [B]
     *     |       |
     *     V       V
     *    [C] --> [D]
     *     |       |
     *     V       V
     *    [E] --> [F]
     *
     *    A-B = 1
     *    A-C = 5
     *    B-D = 1
     *    C-D = 3
     *    C-E = 10
     *    D-F = 1
     *    E-F = 2
     */
    public void test4() {
        long startTime = System.currentTimeMillis();
        List<T> vertices = List.of((T[]) new String[]{"A", "B", "C", "D", "E", "F"});
        WeightedAdjacencyList<T> W = new WeightedAdjacencyList<>(vertices);

        W.addEdge((T) "A", (T) "B", 1);
        W.addEdge((T) "A", (T) "C", 5);
        W.addEdge((T) "B", (T) "D", 1);
        W.addEdge((T) "C", (T) "D", 3);
        W.addEdge((T) "C", (T) "E", 10);
        W.addEdge((T) "D", (T) "F", 1);
        W.addEdge((T) "E", (T) "F", 2);

        Map<T, Long> shortestPaths = W.getShortestPaths((T) "A");
        assertEquals("A -> A should be 0.", Long.valueOf(0), shortestPaths.get((T) "A"));
        assertEquals("A -> B should be 1.", Long.valueOf(1), shortestPaths.get((T) "B"));
        assertEquals("A -> C should be 5.", Long.valueOf(5), shortestPaths.get((T) "C"));
        assertEquals("A -> D should be 2.", Long.valueOf(2), shortestPaths.get((T) "D"));
        assertEquals("A -> E should be 15.", Long.valueOf(15), shortestPaths.get((T) "E"));
        assertEquals("A -> F should be 2.", Long.valueOf(2), shortestPaths.get((T) "F"));

        shortestPaths = W.getShortestPaths((T) "C");
        assertEquals("C -> A should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "A"));
        assertEquals("C -> B should be infinnity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "B"));
        assertEquals("C -> C should be 0.", Long.valueOf(0), shortestPaths.get((T) "C"));
        assertEquals("C -> D should be 3.", Long.valueOf(3), shortestPaths.get((T) "D"));
        assertEquals("C -> E should be 10.", Long.valueOf(10), shortestPaths.get((T) "E"));
        assertEquals("C -> F should be 4.", Long.valueOf(4), shortestPaths.get((T) "F"));

        long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time for test 4: " + (endTime - startTime) + "ms");
    }

    /**
     * Makes the following graph:
     *
     *    [A] --- [B] --> [C] --- [D]
     *     |       |       |       |
     *     V       V       V       V
     *    [E]     [F] <-- [G]     [H]
     *     ^       |               |
     *     |       V               V
     *    [I] --> [J] --> [K]     [L]
     *             |       |       |
     *             V       |       V
     *    [M] <-- [N]     [O] --> [P]
     *
     *    A-B & B-A = 2
     *    A-E       = 10
     *    B-C       = 0
     *    B-F       = 7
     *    C-D & D-C = 7
     *    C-G       = 6
     *    D-H       = 1
     *    I-E       = 3
     *    G-F       = 11
     *    F-J       = 9
     *    H-L       = 2
     *    I-J       = 1
     *    J-K       = 5
     *    J-N       = 0
     *    K-O & O-K = 12
     *    L-P       = 2
     *    N-M       = 4
     *    O-P       = 0
     */
    public WeightedAdjacencyList<T> makeBigOlGraph() {
        List<T> vertices = List.of((T[]) new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"});
        WeightedAdjacencyList<T> W = new WeightedAdjacencyList<>(vertices);

        W.addEdge((T) "A", (T) "B", 2);
        W.addEdge((T) "B", (T) "A", 2);
        W.addEdge((T) "A", (T) "E", 10);
        W.addEdge((T) "B", (T) "C", 0);
        W.addEdge((T) "B", (T) "F", 7);
        W.addEdge((T) "C", (T) "D", 7);
        W.addEdge((T) "D", (T) "C", 7);
        W.addEdge((T) "C", (T) "G", 6);
        W.addEdge((T) "D", (T) "H", 1);
        W.addEdge((T) "I", (T) "E", 3);
        W.addEdge((T) "G", (T) "F", 11);
        W.addEdge((T) "F", (T) "J", 9);
        W.addEdge((T) "H", (T) "L", 2);
        W.addEdge((T) "I", (T) "J", 1);
        W.addEdge((T) "J", (T) "K", 5);
        W.addEdge((T) "J", (T) "N", 0);
        W.addEdge((T) "K", (T) "O", 12);
        W.addEdge((T) "O", (T) "K", 12);
        W.addEdge((T) "L", (T) "P", 2);
        W.addEdge((T) "N", (T) "M", 4);
        W.addEdge((T) "O", (T) "P", 0);

        return W;
    }

    @Test
    //@Ignore
    /**
     * Test 5 - getShortestPaths(). Also tests time taken.
     *
     * Graph used for this test:
     *    Big ol' Graph
     */
    public void test5() {
        long startTime = System.currentTimeMillis();
        WeightedAdjacencyList<T> W = makeBigOlGraph();
        Map<T, Long> shortestPaths = W.getShortestPaths((T) "A");
        assertEquals("A -> A should be 0.", Long.valueOf(0), shortestPaths.get((T) "A"));
        assertEquals("A -> B should be 2.", Long.valueOf(2), shortestPaths.get((T) "B"));
        assertEquals("A -> C should be 2.", Long.valueOf(2), shortestPaths.get((T) "C"));
        assertEquals("A -> D should be 9.", Long.valueOf(9), shortestPaths.get((T) "D"));
        assertEquals("A -> E should be 10.", Long.valueOf(10), shortestPaths.get((T) "E"));
        assertEquals("A -> F should be 9.", Long.valueOf(9), shortestPaths.get((T) "F"));
        assertEquals("A -> G should be 8.", Long.valueOf(8), shortestPaths.get((T) "G"));
        assertEquals("A -> H should be 10.", Long.valueOf(10), shortestPaths.get((T) "H"));
        assertEquals("A -> I should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "I"));
        assertEquals("A -> J should be 18.", Long.valueOf(18), shortestPaths.get((T) "J"));
        assertEquals("A -> K should be 23.", Long.valueOf(23), shortestPaths.get((T) "K"));
        assertEquals("A -> L should be 12.", Long.valueOf(12), shortestPaths.get((T) "L"));
        assertEquals("A -> M should be 22.", Long.valueOf(22), shortestPaths.get((T) "M"));
        assertEquals("A -> N should be 18.", Long.valueOf(18), shortestPaths.get((T) "N"));
        assertEquals("A -> O should be 35.", Long.valueOf(35), shortestPaths.get((T) "O"));
        assertEquals("A -> P should be 14.", Long.valueOf(14), shortestPaths.get((T) "P"));
        long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time for test 5: " + (endTime - startTime) + "ms");
    }

    @Test
    //@Ignore
    /**
     * Test 6 - getShortestPaths(). Also tests time taken.
     *
     * Graph used for this test:
     *    Big ol' Graph
     */
    public void test6() {
        long startTime = System.currentTimeMillis();
        WeightedAdjacencyList<T> W = makeBigOlGraph();
        Map<T, Long> shortestPaths = W.getShortestPaths((T) "F");
        assertEquals("F -> A should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "A"));
        assertEquals("F -> B should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "B"));
        assertEquals("F -> C should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "C"));
        assertEquals("F -> D should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "D"));
        assertEquals("F -> E should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "E"));
        assertEquals("F -> F should be 0.", Long.valueOf(0), shortestPaths.get((T) "F"));
        assertEquals("F -> G should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "G"));
        assertEquals("F -> H should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "H"));
        assertEquals("F -> I should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "I"));
        assertEquals("F -> J should be 9.", Long.valueOf(9), shortestPaths.get((T) "J"));
        assertEquals("F -> K should be 14.", Long.valueOf(14), shortestPaths.get((T) "K"));
        assertEquals("F -> L should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "L"));
        assertEquals("F -> M should be 13.", Long.valueOf(13), shortestPaths.get((T) "M"));
        assertEquals("F -> N should be 9.", Long.valueOf(9), shortestPaths.get((T) "N"));
        assertEquals("F -> O should be 26.", Long.valueOf(26), shortestPaths.get((T) "O"));
        assertEquals("F -> P should be 26.", Long.valueOf(26), shortestPaths.get((T) "P"));
        shortestPaths = W.getShortestPaths((T) "K");
        assertEquals("K -> A should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "A"));
        assertEquals("K -> B should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "B"));
        assertEquals("K -> C should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "C"));
        assertEquals("K -> D should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "D"));
        assertEquals("K -> E should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "E"));
        assertEquals("K -> F should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "F"));
        assertEquals("K -> G should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "G"));
        assertEquals("K -> H should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "H"));
        assertEquals("K -> I should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "I"));
        assertEquals("K -> J should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "J"));
        assertEquals("K -> K should be 0.", Long.valueOf(0), shortestPaths.get((T) "K"));
        assertEquals("K -> L should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "L"));
        assertEquals("K -> M should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "M"));
        assertEquals("K -> N should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "N"));
        assertEquals("K -> O should be 12.", Long.valueOf(12), shortestPaths.get((T) "O"));
        assertEquals("K -> P should be 12.", Long.valueOf(12), shortestPaths.get((T) "P"));
        shortestPaths = W.getShortestPaths((T) "C");
        assertEquals("C -> A should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "A"));
        assertEquals("C -> B should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "B"));
        assertEquals("C -> C should be 0.", Long.valueOf(0), shortestPaths.get((T) "C"));
        assertEquals("C -> D should be 7.", Long.valueOf(7), shortestPaths.get((T) "D"));
        assertEquals("C -> E should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "E"));
        assertEquals("C -> F should be 17.", Long.valueOf(17), shortestPaths.get((T) "F"));
        assertEquals("C -> G should be 6.", Long.valueOf(6), shortestPaths.get((T) "G"));
        assertEquals("C -> H should be 8.", Long.valueOf(8), shortestPaths.get((T) "H"));
        assertEquals("C -> I should be infinity.", Long.valueOf(Long.MAX_VALUE), shortestPaths.get((T) "I"));
        assertEquals("C -> J should be 26.", Long.valueOf(26), shortestPaths.get((T) "J"));
        assertEquals("C -> K should be 31.", Long.valueOf(31), shortestPaths.get((T) "K"));
        assertEquals("C -> L should be 10.", Long.valueOf(10), shortestPaths.get((T) "L"));
        assertEquals("C -> M should be 30.", Long.valueOf(30), shortestPaths.get((T) "M"));
        assertEquals("C -> N should be 26.", Long.valueOf(26), shortestPaths.get((T) "N"));
        assertEquals("C -> O should be 43.", Long.valueOf(43), shortestPaths.get((T) "O"));
        assertEquals("C -> P should be 12.", Long.valueOf(12), shortestPaths.get((T) "P"));
        long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time for test 5: " + (endTime - startTime) + "ms");
    }
}

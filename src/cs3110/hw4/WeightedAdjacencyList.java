package cs3110.hw4;

import java.util.*;

public class WeightedAdjacencyList<T> implements WeightedGraph<T> {
    
    private final Map<T, List<Pair<T, Long>>> adjacencyList;

    /**
     * Constructor for the WeightedAdjacencyList class. Initializes the graph with empty adjacency lists.
     * @param vertices A list of vertices to initialize the graph with.
     */
    public WeightedAdjacencyList(List<T> vertices) {
        adjacencyList = new HashMap<>();
        for (T vertex : vertices) {
            adjacencyList.put(vertex, new ArrayList<>());
        }
    }

    /**
     * Adds the directed edge (u,v) to the graph. If the edge is already present, it should not be modified.
     * @param u The source vertex.
     * @param v The target vertex.
     * @param weight The weight of the edge (u,v).
     * @return True if the edge was added to the graph, false if 1) either u or v are not in the graph 2) the edge was already present.
     */
    @Override
    public boolean addEdge(T u, T v, int weight) {
        List<Pair<T, Long>> neighbors = adjacencyList.get(u);
        if (neighbors == null || adjacencyList.get(v) == null || weight < 0) {
            return false; // u or v not in graph or negative weight
        }

        for (Pair<T, Long> neighbor : neighbors) {
            if (neighbor.getFirst().equals(v)) {
                return false; // Edge already present
            }
        }

        neighbors.add(new Pair<>(v, (long) weight));
        return true; // Edge added successfully
    }

    /**
     * Adds a vertex to the graph. If the vertex is already present, it should not be modified.
     * @param vertex A vertex to add to the graph.
     * @return False vertex was already in the graph, true otherwise.
     */
    @Override
    public boolean addVertex(T vertex) {
        if (adjacencyList.containsKey(vertex)) { // Vertex already in the graph
            return false;
        }
        else {
            adjacencyList.put(vertex, new ArrayList<Pair<T, Long>>());
            return true;
        }
    }

    /**
     * Returns the number of vertices in the graph.
     * @return |V|
     */
    @Override
    public int getVertexCount() {
        return adjacencyList.size();
    }

    /**
     * Checks if a vertex is in the graph.
     * @param v The name of a vertex.
     * @return True if v is in the graph, false otherwise.
     */
    @Override
    public boolean hasVertex(T v) {
        return adjacencyList.containsKey(v);
    }

    /**
     * Returns an iterable of all vertices in the graph.
     * @return An Iterable of V.
     */
    @Override
    public Iterable<T> getVertices() {
        return adjacencyList.keySet();
    }

    /**
     * Returns the number of edges in the graph.
     * @return |E|
     */
    @Override
    public int getEdgeCount() {
        int edgeCount = 0;
        // Works becauset the graph is directed, so we 
        // can just add all neighbors of each vertex
        for (List<Pair<T, Long>> neighbors : adjacencyList.values()) {
            edgeCount += neighbors.size();
        }

        return edgeCount;
    }

    /**
     * Checks if an edge is in the graph.
     * @param u The source of the edge.
     * @param v The target of the edge.
     * @return True if (u,v) is in the graph, false otherwise.
     */
    @Override
    public boolean hasEdge(T u, T v) { 
        List<Pair<T, Long>> neighbors = adjacencyList.get(u);
        if (neighbors == null) {
            return false; // u not in graph
        }

        for (Pair<T, Long> neighbor : neighbors) {
            if (neighbor.getFirst().equals(v)) {
                return true; // Edge found
            }
        }

        return false; // Edge not found after search
    }

    /**
     * Returns all neighbors of vertex u.
     * @param u A vertex.
     * @return The neighbors of u in the weighted graph.
     */
    @Override
    public Iterable<T> getNeighbors(T u) {
        List<Pair<T, Long>> neighbors = adjacencyList.get(u);
        if (neighbors == null) {
            return Collections.emptyList(); // u not in graph
        }
        
        List<T> neighborVertices = new ArrayList<>();
        for (Pair<T, Long> neighbor : neighbors) {
            neighborVertices.add(neighbor.getFirst());
        }
        
        return neighborVertices; // Return the list of neighbors
    }

    /**
     * Checks if two vertices are neighbors in the graph (uses hasEdge).
     * @param u
     * @param v
     * @return
     */
    @Override
    public boolean areNeighbors(T u, T v) {
        return hasEdge(u,v);
    }

    /**
     * Uses Dijkstra's algorithm to find the (length of the) shortest path from s to all other reachable vertices in the graph.
     * If the graph contains negative edge weights, the algorithm should terminate, but the return value is undefined.
     * @param s The source vertex.
     * @return A Mapping from all reachable vertices to their distance from s. Unreachable vertices should NOT be included in the Map.
     */
    @Override
    public Map<T, Long> getShortestPaths(T s) {
        // Distance map to return
        Map<T, Long> distances = new HashMap<>();

        // Priority queue for Dijkstra's algorithm
        PriorityQueue<Pair<T, Long>> queue = new PriorityQueue<>(Comparator.comparingLong(Pair::getSecond));

        // Initialize distances
        for (T vertex: adjacencyList.keySet()) {
            distances.put(vertex, Long.MAX_VALUE); // Set all distances to infinity
        }
        distances.put(s, 0L); // Distance to source is 0
        queue.add(new Pair<>(s, 0L)); // Add source to the queue

        // Dijkstra's algorithm
        while (!queue.isEmpty()) {
            Pair<T, Long> current = queue.poll();
            T u = current.getFirst();
            long currentDistance = current.getSecond();

            // If current distance is greater than one we already found, skip
            if (currentDistance > distances.get(u)) {
                continue;
            }

            // Explore neighbors
            for (Pair<T, Long> neighbor : adjacencyList.get(u)) {
                T v = neighbor.getFirst();
                long weight = neighbor.getSecond();
                long newDistance = currentDistance + weight;

                // Relax the edge
                if (newDistance < distances.get(v)) {
                    distances.put(v, newDistance);
                    queue.add(new Pair<>(v, newDistance));
                }
            }
        }

        return distances;
    }
}

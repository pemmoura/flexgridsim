/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexgridsim.util;

import java.util.ArrayList;

/**
 * Dijkstra's routing algorithm.
 * 
 * For a given source node, the algorithm finds the path with lowest cost to
 * every other vertex. The algorithm steps are the following:
 * 
 * 1) Assign to every node a distance value. Set it to zero for the initial node
 * and to infinity for all other nodes.
 * 2) Mark all nodes as unvisited. Set initial node as current.
 * 3) For current node, consider all its unvisited neighbors and calculate their
 * tentative distance (from the initial node). If this distance is less than the 
 * previously recorded distance (infinity in the beginning, zero for the initial node),
 * overwrite the distance.
 * 4) After all neighbors of the current node have been considered, mark it as visited.
 * A visited node will not be checked ever again; its distance recorded now is final
 * and minimal.
 * 5) If all nodes have been visited, finish. Otherwise, set the unvisited node with
 * the smallest distance (from the initial node) as the next "current node" and
 * continue from step 3.
 * 
 * @author andred
 */
public class Dijkstra {
    // Dijkstra's algorithm to find shortest path from s to all other nodes
    /**
     * Dijkstra.
     *
     * @param G the weighted graph of the network
     * @param s the source node
     * @return the a array of ints representing the path
     */
    public static int[] dijkstra(WeightedGraph G, int s) {
        final double[] dist = new double[G.size()];  // shortest known distance from "s"
        final int[] pred = new int[G.size()];  // preceding node in path
        final boolean[] visited = new boolean[G.size()]; // all false initially

        for (int i = 0; i < dist.length; i++) {
            pred[i] = -1;
            dist[i] = Integer.MAX_VALUE;
        }
        dist[s] = 0;

        for (int i = 0; i < dist.length; i++) {
            final int next = minVertex(dist, visited);
//            if (next < 0) {
//                return pred;
//            }
            if (next >= 0) {
                visited[next] = true;

                // The shortest path to next is dist[next] and via pred[next].

                final int[] n = G.neighbors(next);
                for (int j = 0; j < n.length; j++) {
                    final int v = n[j];
                    final double d = dist[next] + G.getWeight(next, v);
                    if (dist[v] > d && !G.isEdgeRemoved(next, v)) {
                        dist[v] = d;
                        pred[v] = next;
                    }
                }
            }
        }
        return pred;  // (ignore pred[s]==0!)
    }
    
    /**
     * Finds, from the list of unvisited vertexes, the one with the lowest
     * distance from the initial node.
     * 
     * @param dist vector with shortest known distance from the initial node
     * @param v vector indicating the visited nodes
     * @return vertex with minimum distance from initial node,
     * 			or -1 if the graph is unconnected or if no vertexes were visited yet
     */
    public static int minVertex(double[] dist, boolean[] v) {
        double x = Double.MAX_VALUE;
        int y = -1;   // graph not connected, or no unvisited vertices
        for (int i = 0; i < dist.length; i++) {
            if (!v[i] && dist[i] < x) {
                y = i;
                x = dist[i];
            }
        }
        return y;
    }
    
    /**
     * Retrieves the shortest path between a source and a destination node,
     * within a weighted graph.
     * 
     * @param G the weighted graph in which the shortest path will be found
     * @param src the source node
     * @param dst the destination node
     * @return the shortest path, as a vector of integers that represent node coordinates
     */
    public static int[] getShortestPath(WeightedGraph G, int src, int dst) {
        int x;
        int[] sp;
        ArrayList<Integer> path = new ArrayList<Integer>();

        final int[] pred = dijkstra(G, src);

        x = dst;

        while (x != src) {
            path.add(0, x);
            x = pred[x];
            // No path
            if (x == -1) {
                return new int[0];
            }
        }
        path.add(0, src);
        sp = new int[path.size()];
        for (int i = 0; i < path.size(); i++) {
            sp[i] = path.get(i);
        }
        return sp;
    }
    
}

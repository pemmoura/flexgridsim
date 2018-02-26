/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexgridsim.util;

/**
 * A weighted graph associates a label (weight) with every edge in the graph. If
 * a pair of nodes has weight equal to zero, it means the edge between them
 * doesn't exist.
 * 
 * @author andred, pedrom
 */
public class WeightedGraph {
	protected int numNodes;
	protected double[][] edges; // adjacency matrix
	protected boolean[] visited;
	protected boolean[][] edgeRemoved;

	/**
	 * Creates a new WeightedGraph object with no edges,
	 * 
	 * @param n
	 *            number of nodes the new graph will have
	 */
	public WeightedGraph(int n) {
		edges = new double[n][n];
		edgeRemoved = new boolean[n][n];
		visited = new boolean[n];
		numNodes = n;
	}

	/**
	 * Creates a new WeightedGraph object, based on an already existing weighted
	 * graph.
	 * 
	 * @param g
	 *            the graph that will be copied into the new one
	 */
	public WeightedGraph(WeightedGraph g) {
		numNodes = g.numNodes;
		edges = new double[numNodes][numNodes];
		for (int i = 0; i < numNodes; i++) {
			visited[i] = false;
		}
		for (int i = 0; i < numNodes; i++) {
			for (int j = 0; j < numNodes; j++) {
				edges[i][j] = g.getWeight(i, j);
			}
		}
		for (int i = 0; i < numNodes; i++) {
			for (int j = 0; j < numNodes; j++) {
				edgeRemoved[i][j] = false;
			}
		}
	}

	/**
	 * Retrieves the size of the graph, i.e., the amount of vertexes it has.
	 * 
	 * @return integer with the quantity of nodes in the graph
	 */
	public int size() {
		return numNodes;
	}

	/**
	 * Gets the num nodes.
	 *
	 * @return the num nodes
	 */
	public int getNumNodes() {
		return numNodes;
	}

	/**
	 * Gets the num edges.
	 *
	 * @return the num edges
	 */
	public int getNumEdges() {
		return edges.length;
	}
	/**
	 * Creates a new edge within the graph, which requires its two vertexes and
	 * its weight.
	 * 
	 * @param source
	 *            the edge's source node
	 * @param target
	 *            the edge's destination node
	 * @param w
	 *            the value of the edge's weight
	 */
	public void addEdge(int source, int target, double w) {
		edges[source][target] = w;
	}



	/**
	 * Retrieves the weight of a given edge on the graph.
	 * 
	 * @param source
	 *            the edge's source node
	 * @param target
	 *            the edge's destination node
	 * @return the value of the edge's weight
	 */
	public double getWeight(int source, int target) {
		return edges[source][target];
	}


	/**
	 * Retrieves the neighbors of a given vertex.
	 * 
	 * @param vertex
	 *            index of the vertex within the matrix of edges
	 * @return list with indexes of the vertex's neighbors
	 */
	public int[] neighbors(int vertex) {
		int count = 0;
		for (int i = 0; i < edges[vertex].length; i++) {
			if (edges[vertex][i] > 0) {
				count++;
			}
		}
		final int[] answer = new int[count];
		count = 0;
		for (int i = 0; i < edges[vertex].length; i++) {
			if (edges[vertex][i] > 0) {
				answer[count++] = i;
			}
		}
		return answer;
	}

	/**
	 * Mark as visited.
	 *
	 * @param node the node
	 */
	public void markAsVisited(int node){
		this.visited[node] = true;
	}
	
	
	/**
	 * Mark as visited.
	 *
	 * @param node the src
	 * @return true, if is visited
	 */
	public boolean isVisited(int node){
		return visited[node];
	}
	
	/**
	 * Clean visited.
	 */
	public void cleanVisited(){
		for (int i = 0; i < numNodes; i++) {
			visited[i] = false;
		}
	}
	
	/**
	 * Checks if is edge removed.
	 *
	 * @param src the src
	 * @param dst the dst
	 * @return true, if is edge removed
	 */
	public boolean isEdgeRemoved(int src, int dst){
		return edgeRemoved[src][dst];
	}
	/**
	 * Prints all information related to the weighted graph. For each vertex,
	 * shows the vertexes is is adjacent to and the weight of each edge.
	 * 
	 * @return string containing the edges of each vertex
	 */
	@Override
	public String toString() {
		String s = "";
		for (int j = 0; j < edges.length; j++) {
			s += Integer.toString(j) + ": ";
			for (int i = 0; i < edges[j].length; i++) {
				if (edges[j][i] > 0) {
					s += Integer.toString(i) + ":"
							+ Double.toString(edges[j][i]) + " ";
				}
			}
			s += "\n";
		}
		return s;
	}
	
}

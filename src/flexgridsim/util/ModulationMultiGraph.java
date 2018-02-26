/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexgridsim.util;

import java.util.ArrayList;

import flexgridsim.PhysicalTopology;
import flexgridsim.TrafficGenerator;

/**
 * A multigraph that has an edge for each free slot between two node.
 * 
 * @author pedrom
 */
public class ModulationMultiGraph {
	protected int numNodes;
	protected double[][][] edges;
	protected boolean[][][] edgesRemoved;
	/**
	 * Instantiate a spectrum graph from a weighted graph the edges from the weighted graph will be
	 * transformed in N edges, where N is the number of slots in the Physical Topology pt.
	 *
	 * @param G the weighted graph
	 * @param pt the pt
	 * @param traffic the traffic
	 * @param numberOfModulations the number of modulations
	 */
	public ModulationMultiGraph(WeightedGraph G, PhysicalTopology pt, TrafficGenerator traffic, int numberOfModulations) {
		this.numNodes = G.getNumNodes();
		this.edges = new double[this.numNodes][this.numNodes][numberOfModulations];
		this.edgesRemoved = new boolean[numNodes][numNodes][numberOfModulations];
		for (int i = 0; i < G.size(); i++) {
			for (int j = 0; j < G.size(); j++) {
				for (int k = 0; k < pt.getNumSlots(); k++) {
					this.edgesRemoved[i][j][k] = true;
				}
			}
		}
		for (int i = 0; i < G.size(); i++) {
			int[] neighbors = G.neighbors(i);
			for (int j = 0; j < neighbors.length; j++) {
				this.restoreRemovedEdges(i, neighbors[j], 0, pt.getNumSlots()-1);
			}
		}
	}
	/**
	 * Retrieves the size of the graph, i.e., the amount of vertexes it has.
	 * 
	 * @return integer with the quantity of nodes in the graph
	 */
	public int size() {
		return this.numNodes;
	}

	/**
	 * Retrieves the weight of a given edge on the graph.
	 * 
	 * @param source
	 *            the edge's source node
	 * @param target
	 *            the edge's destination node
	 * @param edge
	 *            the number of the edge
	 * @return the value of the edge's weight
	 */
	public double getWeight(int source, int target, int edge) {
		return this.edges[source][target][edge];
	}

	/**
	 * Sets a determined weight to a given edge on the graph.
	 * 
	 * @param source
	 *            the edge's source node
	 * @param target
	 *            the edge's destination node
	 * @param edge
	 *            the number of the edge
	 * @param w
	 *            the value of the weight
	 */
	public void setWeight(int source, int target, int edge, double w) {
		this.edges[source][target][edge] = w;
	}

	/**
	 * Retrieves the neighbors of a given vertex.
	 * 
	 * @param vertex
	 *            index of the vertex within the matrix of edges
	 * @return list with indexes of the vertex's neighbors
	 */
	public int[] neighbors(int vertex) {
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		for (int i = 0; i < this.edgesRemoved[vertex].length; i++) {
			for (int j = 0; j < this.edgesRemoved[vertex][i].length; j++) {
				if (!this.edgesRemoved[vertex][i][j]) {
					neighbors.add(i);
					break;
				}
			}
		}
		int neighborsArray[] = new int[neighbors.size()];
		for (int i = 0; i < neighbors.size(); i++) {
			neighborsArray[i] = neighbors.get(i).intValue();
		}
		return neighborsArray;
	}

	/**
	 * Verify if a set of contiguous edges are available
	 * 
	 * @param src
	 *            source node
	 * @param dst
	 *            destination node
	 * @param first
	 *            first edge index to be verified
	 * @param last
	 *            last edge index to be verified
	 * @return true if a set of contiguous edges are available, false otherwise
	 */
	public boolean hasSetOfEdges(int src, int dst, int first, int last) {
		for (int i = first; i <= last; i++) {
			if (this.edgesRemoved[src][dst][i]) {// && edges[src][dst][i] > 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Mark the edge as removed
	 * 
	 * @param src
	 *            source node
	 * @param dst
	 *            destination node
	 * @param edge
	 *            index of edge to be removed
	 * @throws RuntimeException
	 *             if the edge is already removed
	 */             
	public void markEdgeRemoved(int src, int dst, int edge)
			throws RuntimeException {
		if (edgesRemoved[src][dst][edge] == true) {
			throw new RuntimeException();
		} else {
			this.edgesRemoved[src][dst][edge] = true;
		}
	}

	/**
	 * Mark a set of contiguous edges as removed
	 * 
	 * @param src
	 *            source node
	 * @param dst
	 *            destination node
	 * @param firstEdge
	 *            index of the first edge to be removed
	 * @param lastEdge
	 * 			  index of the last edge to be removed
	 * @throws RuntimeException
	 * 			  if any edge is already removed
	 */
	public void markEdgesRemoved(int src, int dst, int firstEdge, int lastEdge)
			throws RuntimeException {
		for (int i = firstEdge; i <= lastEdge; i++) {
			if (edgesRemoved[src][dst][i] == true) {
				throw new RuntimeException();
			} else {
				this.edgesRemoved[src][dst][i] = true;
			}
		}
	}

	/**
	 * Mark the edge as not removed
	 * 
	 * @param src
	 *            source node
	 * @param dst
	 *            destination node
	 * @param edge
	 *            index of edge to be restored
	 * @throws RuntimeException
	 * 			   if the edge is already in the graph
	 */
	public void restoreRemovedEdge(int src, int dst, int edge)
			throws RuntimeException {
		if (edgesRemoved[src][dst][edge] == false) {
			throw new RuntimeException();
		} else {
			this.edgesRemoved[src][dst][edge] = false;
		}
	}

	/**
	 * Mark a set of contiguous edges as not removed
	 * 
	 * @param src
	 *            source node
	 * @param dst
	 *            destination node
	 * @param firstEdge
	 *            index of the first edge to be restored
	 * @param lastEdge
	 * 			  index of the last edge to be restored
	 * @throws RuntimeException 
	 * 			  if any edge is already in the graph
	 */
	public void restoreRemovedEdges(int src, int dst, int firstEdge,
			int lastEdge) throws RuntimeException {
		for (int i = firstEdge; i <= lastEdge; i++) {
			if (edgesRemoved[src][dst][i] == false) {
				throw new RuntimeException();
			} else {
				this.edgesRemoved[src][dst][i] = false;
			}
		}
	}
	/**
	 * Verify if an edge exists 
	 * 
	 * @param src source of the edge
	 * @param dst destination of the edge
	 * @param edge number of the edge in the multigraph
	 * @return true if the graph has the edge
	 */
	public boolean hasEdge(int src, int dst, int edge) {
		return !this.edgesRemoved[src][dst][edge];
	}
}
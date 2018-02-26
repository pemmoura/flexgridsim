/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexgridsim.util;

import java.util.ArrayList;

import flexgridsim.PhysicalTopology;
import flexgridsim.TrafficGenerator;
import flexgridsim.TrafficInfo;

/**
 * A multigraph that has an edge for each free slot between two node.
 * 
 * @author pedrom
 */
public class MultiGraph {

	protected int numNodes;
	protected double[][][] edges; // adjacency matrix
	protected int numEdges;
	protected boolean[][][] edgesRemoved;
	protected int[][] numberOfFreeSlots;
	protected TrafficGenerator traffic;
	
	/**
	 * Instantiate a spectrum graph from a weighted graph the edges from the weighted graph will be
	 * transformed in N edges, where N is the number of slots in the Physical Topology pt.
	 *
	 * @param G the weighted graph
	 * @param pt the pt
	 * @param traffic the traffic
	 */
	public MultiGraph(WeightedGraph G, PhysicalTopology pt, TrafficGenerator traffic) {
		this.numNodes = G.getNumNodes();
		this.numEdges = pt.getNumSlots();
		this.edges = new double[this.numNodes][this.numNodes][this.numEdges];
		this.edgesRemoved = new boolean[numNodes][numNodes][numEdges];
		this.numberOfFreeSlots = new int[numNodes][numNodes];
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
		this.traffic = traffic;
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
			for (int j = 0; j < this.numEdges; j++) {
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
			this.numberOfFreeSlots[src][dst]--;
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
//				throw new RuntimeException();
			} else {
				this.edgesRemoved[src][dst][i] = true;
			}
		}
		this.numberOfFreeSlots[src][dst] = this.numberOfFreeSlots[src][dst]
				- (lastEdge - firstEdge + 1);
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
			this.numberOfFreeSlots[src][dst]++;
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
		this.numberOfFreeSlots[src][dst] = this.numberOfFreeSlots[src][dst]
				+ (lastEdge - firstEdge + 1);
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
	/**
	 * Return the highest number of contiguous edges between two nodes
	 * 
	 * @param src source of the link
	 * @param dst destination of the link
	 * @return  the highest number of contiguous edges between two nodes
	 */
	public int maxNumberOfContiguousEdges(int src, int dst) {
		int maxValue = 0;
		int localCount = 0;
		for (int i = 0; i < this.edgesRemoved[src][dst].length; i++) {
			if (!this.edgesRemoved[src][dst][i]) {
				localCount++;
			} else {
				if (maxValue < localCount) {
					maxValue = localCount;
				}
				localCount = 0;
			}
		}
		if (maxValue < localCount) {
			maxValue = localCount;
		}
		return maxValue;
	}
	
	/**
	 * Return the number of fragments, i.e. number of sets of contiguos slots
	 * between two nodes
	 * 
	 * @param src source node
	 * @param dst destination node
	 * @return number of fragments
	 */
	public int numberOfFragments(int src, int dst) {
		int counter = 0;
		boolean currentValue = true;
		for (int i = 0; i < this.edgesRemoved[src][dst].length; i++) {
			if (this.edgesRemoved[src][dst][i] != currentValue) {
				if (currentValue == true)
					counter++;
				currentValue = !currentValue;
			}
		}
		return counter;
	}
	/**
	 * 
	 * @param src source node
	 * @param dst destination node
	 * @return the higher cardinality, in or out the node
	 */
	public double maxFlow(int src, int dst) {
		int outCounter = 0;
		for (int i = 0; i < this.edgesRemoved[src][dst].length; i++) {
			if (this.edgesRemoved[src][dst][i] == false) {
				outCounter++;
			}
		}
		int inCounter = 0;
		for (int i = 0; i < this.edgesRemoved[src][dst].length; i++) {
			if (this.edgesRemoved[dst][src][i] == false) {
				inCounter++;
			}
		}
		return Math.min(outCounter, inCounter);
	}
	
	/**
	 * Gets the traffic.
	 *
	 * @return the traffic
	 */
	public TrafficInfo[] getTrafficInfo() {
		return traffic.getCallsTypeInfo();
	}

	/**
	 * @param src source node
	 * @param dst destination node
	 * @return number of free slots between two nodes
	 */
	public int getNumberOfFreeSlots(int src, int dst) {
		return numberOfFreeSlots[src][dst];
	}
	
	/**
	 * Gets the fragmentation ratio, a metric that states the potential of each free contiguous set of slots
	 * by telling the number of traffic calls it could fit in. then calculating the mean of that
	 *
	 * @param src the src
	 * @param dst the dst
	 * @param slotCapacity the slot capacity
	 * @return the fragmentation ratio
	 */
	public double getFragmentationRatio(int src, int dst, double slotCapacity){
		ArrayList<Double> fragmentsPotential = new ArrayList<Double>();
		for (int i = 0; i < this.edgesRemoved.length-1; i++) {
			if (this.edgesRemoved[src][dst][i] == false){
				i++;
				int fragmentSize = 1;
				while (this.edgesRemoved[src][dst][i] == false && i < edgesRemoved.length-2 ){
					fragmentSize++;
					i++;
				}
				double counter = 0;
				for (TrafficInfo call : this.traffic.getCallsTypeInfo()) {
					if (call.getRate()/slotCapacity >= fragmentSize){
						counter++;
					}
				}
				fragmentsPotential.add(counter/this.traffic.getCallsTypeInfo().length) ;
			}
		}
		double sum = 0;
		for (Double potential : fragmentsPotential) {
			sum += potential.doubleValue();
		}
		return sum/fragmentsPotential.size();
	}
}
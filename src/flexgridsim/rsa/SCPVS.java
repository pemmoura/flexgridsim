package flexgridsim.rsa;

import java.util.ArrayList;

import org.w3c.dom.Element;

import flexgridsim.FlexGridLink;
import flexgridsim.Flow;
import flexgridsim.PhysicalTopology;
import flexgridsim.Slot;
import flexgridsim.TrafficGenerator;
import flexgridsim.VirtualTopology;
import flexgridsim.util.ScpvsObject;
import flexgridsim.util.Tree;
import flexgridsim.util.WeightedGraph;


/**
 * The Class SCPVS.
 * 
 * @author pedrom
 */
public class SCPVS implements RSA {
	protected PhysicalTopology pt;
	protected VirtualTopology vt;
	protected ControlPlaneForRSA cp;
	protected WeightedGraph graph;
	
	/**
	 * Instantiates a new scpvs.
	 */
	public SCPVS() {
	}

	public void simulationInterface(Element xml, PhysicalTopology pt, VirtualTopology vt,
			ControlPlaneForRSA cp, TrafficGenerator traffic) {
		this.pt = pt;
		this.vt = vt;
		this.cp = cp;
		this.graph = pt.getWeightedGraph();
	}

	public void flowArrival(Flow flow) {
		ArrayList<FlexGridLink> path;
		int firstSlot;
		int lastSlot;
		long id;
		int demandInSlots = (int) Math.ceil(flow.getRate() / (double) pt.getSlotCapacity()) + 2;
		// Shortest-Path routing
		path = getShortestPath(graph, flow.getSource(), flow.getDestination(),
				demandInSlots);
		// If no possible path found, block the call
		if (path == null) {
			cp.blockFlow(flow.getID());
			return;
		} else if (path.size() == 0) {
			return;
		}
		int[] links = new int[path.size()];
		// Create the links vector
		for (int i = 0; i < path.size(); i++) {
			links[i] = path.get(i).getID();
		}
		// First-Fit slot assignment
		for (int i = 0; i < pt.getNumSlots()-demandInSlots; i++) {
			// Create the slots arrays
			firstSlot = i;
			lastSlot = i + demandInSlots - 1;
			// If can establish the lightpath, accept the call
			ArrayList<Slot> slotList = new ArrayList<Slot>();
			for (int j = firstSlot; j <= lastSlot; j++) {
				slotList.add(new Slot(0, j));
			}
			id = vt.createLightpath(links, slotList, 0);
			if (id >= 0) {
				// Single-hop routing (end-to-end lightpath)
				flow.setLinks(links);
				cp.acceptFlow(flow.getID(), vt.getLightpath(id));
				return;
			}
		}
		// Block the call
		cp.blockFlow(flow.getID());
	}

	public void flowDeparture(Flow flow) {
		
	}
	
	/**
	 * Gets the shortest path in a form of a lightpath.
	 *
	 * @param G the weighted graph from the pt
	 * @param src the source node
	 * @param dst the the destination node
	 * @param demand the demand of the flow
	 * @return the shortest path between the nodes
	 */
	public ArrayList<FlexGridLink> getShortestPath(WeightedGraph G, int src, int dst, int demand) {
		G.cleanVisited();
		ArrayList<FlexGridLink> resultPath = new ArrayList<FlexGridLink>();
		double CR = Double.MAX_VALUE;
		Tree<Integer> tree = new Tree<Integer>(new Integer(src));
		ScpvsObject data =  new ScpvsObject();
		int[] neighbors = G.neighbors(src);
		G.markAsVisited(src);
		for (int i = 0; i < neighbors.length; i++) {
			int v = neighbors[i];
			FlexGridLink currentLink = pt.getLink(src, neighbors[i]);
			if (contiguousSlotsAvailable(currentLink.getSpectrumCore(0), demand)){
				G.markAsVisited(v);
				tree.addLeaf(v);
				data.setAvailableSpectrum(1, v, currentLink.getSpectrumCore(0));
				data.setRoutingCost(1, v, currentLink.getWeight());
				data.addLinkToPath(1, v, currentLink);
				data.setPrevious(1, v, currentLink.getSource());
			}
		}
		int L = 1;
		int treeHeight = tree.height();
		while (L <= treeHeight){
			ArrayList<Integer> leaves = tree.getLeavesOnLevel(L);
			for (int i = 0; i < leaves.size(); i++) {
				int u = leaves.get(i);
				double uCost = data.getRoutingCost(L, u);
				if (u == dst && uCost < CR) {
					CR = uCost;
					resultPath = data.getPath(L, u);
				}
				neighbors = G.neighbors(u);
				for (int j = 0; j < neighbors.length; j++) {
					int v = neighbors[j];
					double cost = data.getRoutingCost(L, u) + pt.getLink(u,v).getWeight();
					if ( contiguousSlotsAvailable(pt.getLink(u, v).getSpectrumCore(0), demand) && cost < CR && !G.isVisited(v)){
						 tree.addLeaf(u, v);
						 G.markAsVisited(v);
						 boolean[] s1 = data.getAvailableSpectruim(L, u);
						 boolean[] s2 = pt.getLink(u,v).getSpectrumCore(0);
						 data.setRoutingCost(L+1, v, cost);
						 data.setAvailableSpectrum(L+1, v, arrayAnd(s1, s2));
						 data.clearPath(L+1, v);
						 for (FlexGridLink link : data.getPath(L,u)) {
							data.addLinkToPath(L+1, v, link);
						 }
						 data.addLinkToPath(L+1, v, pt.getLink(u,v));
						 data.setPrevious(L+1, v, u);
					}
				}
			}
			treeHeight = tree.height();
			L++;
		}
		return resultPath;
	}
	/**
	 * Verify if the array of booleans has n contiguous true values
	 * 
	 * @param array
	 *            array to be verified
	 * @param n
	 *            number of contiguous slots
	 * @return return true in case it has n contiguous slots and false in case
	 *         it doesnt
	 */
	public static boolean contiguousSlotsAvailable(boolean[] array, int n) {
		int j;
		for (int i = 0; i < array.length; i++) {
			if (array[i]) {
				for (j = i; j < i + n && j < array.length; j++) {
					if (!array[j]) {
						i = j;
						break;
					}
				}
				if (j == i + n)
					return true;
			}
		}
		return false;
	}

	/**
	 * Array and between to arrays.
	 *
	 * @param array1 the array1
	 * @param array2 the array2
	 * @return the boolean[]
	 */
	public static boolean[] arrayAnd(boolean[] array1, boolean[] array2) {
		if (array1.length != array2.length) {
			throw (new IllegalArgumentException());
		}
		boolean[] result = new boolean[array1.length];
		for (int i = 0; i < array1.length; i++) {
			result[i] = array1[i] & array2[i];
		}
		return result;
	}

}


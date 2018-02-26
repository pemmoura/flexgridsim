package flexgridsim.util;

/**
 * @author pedrom
 *
 */
public class ModulationGraph extends WeightedGraph{

	/**
	 * Instantiates a new modulation graph.
	 *
	 * @param n the n
	 */
	public ModulationGraph(int n) {
		super(n);
	}
	
	/**
	 * Instantiates a new modulation graph.
	 *
	 * @param g the g
	 */
	public ModulationGraph(WeightedGraph g) {
		super(g);
	}
	
	public double getWeight(int source, int target) {
		return edges[source][target]; 
	}


}

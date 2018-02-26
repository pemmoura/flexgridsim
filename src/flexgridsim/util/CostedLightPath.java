package flexgridsim.util;

import java.util.ArrayList;

import flexgridsim.LightPath;
import flexgridsim.Slot;

/**
 * The Class CostedLightPath.
 */
public class CostedLightPath extends LightPath {
	private double cost;

	/**
	 * Instantiates a new costed light path.
	 *
	 * @param id the id
	 * @param src the src
	 * @param dst the dst
	 * @param links the links
	 * @param slotList list of slots
	 * @param modulationLevel the modulation level
	 * @param cost the cost
	 */
	public CostedLightPath(long id, int src, int dst, int[] links,
			ArrayList<Slot> slotList, int modulationLevel, double cost) {
		super(id, src, dst, links, slotList, modulationLevel);
		this.cost = cost;
	}

	/**
	 * Gets the cost.
	 *
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Sets the cost.
	 *
	 * @param cost the new cost
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}
	
}

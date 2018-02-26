package flexgridsim;

import java.util.ArrayList;

/**
 * @author pedrom
 *
 */
public class FlexGridLink12MCF extends FlexGridLink{

	/**
	 * @param id
	 * @param src
	 * @param dst
	 * @param cores
	 * @param delay
	 * @param slots
	 * @param weight
	 * @param distance
	 */
	public FlexGridLink12MCF(int id, int src, int dst, int cores, double delay, int slots, double weight,
			int distance) {
		super(id, src, dst, cores, delay, slots, weight, distance);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param i index of spectrum (core)
	 * @param j index of spectrum (slot)
	 * @return a list of neighbor slots
	 */
	public ArrayList<Slot> getCoupledFibersInUse(int i, int j){
		ArrayList<Slot> coupledFibers = new ArrayList<Slot>();
		if (i%2==0) {
			if (reservedSlots[i+1][j]){
				coupledFibers.add(new Slot(i+1, j));
			}
		} else {
			if (reservedSlots[i-1][j]){
				coupledFibers.add(new Slot(i-1, j));
			}
		}
		return coupledFibers;
	}

}

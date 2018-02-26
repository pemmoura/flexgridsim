package flexgridsim;

import java.util.ArrayList;

/**
 * @author pedrom
 *
 */
public class FlexGridLink7MCF extends FlexGridLink {

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
	public FlexGridLink7MCF(int id, int src, int dst, int cores, double delay, int slots, double weight, int distance) {
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
		if (i==0){
			if (reservedSlots[this.cores-1][j]){
				coupledFibers.add(new Slot(this.cores-1, j));
			}
			if (reservedSlots[1][j]){
				coupledFibers.add(new Slot(1, j));
			}
		} else if (i==cores-1){
			if (reservedSlots[0][j] ){
				coupledFibers.add(new Slot(0, j));
			}
			if (reservedSlots[cores-2][j]){
				coupledFibers.add(new Slot(this.cores-2, j));
			}
		} else {
			if (reservedSlots[i+1][j]){
				coupledFibers.add(new Slot(i+1, j));
			}
			if (reservedSlots[i-1][j]){
				coupledFibers.add(new Slot(i-1, j));
			}
		}
		return coupledFibers;
	}
}

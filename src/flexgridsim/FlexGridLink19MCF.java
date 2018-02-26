package flexgridsim;

import java.util.ArrayList;

/**
 * @author pedrom
 *
 */
public class FlexGridLink19MCF extends FlexGridLink{
	private boolean coupling[][];
	/**
	 * @param id
	 * @param src
	 * @param dst
	 * @param cores
	 * @param delay
	 * @param slots
	 * @param weight
	 * @param distance
	 * 
	 */
	public FlexGridLink19MCF(int id, int src, int dst, int cores, double delay, int slots, double weight,
			int distance) {
		super(id, src, dst, cores, delay, slots, weight, distance);
		coupling = new boolean[cores][cores];
		for (int i = 0; i < coupling.length; i++) {
			for (int j = 0; j < coupling[i].length; j++) {
				coupling[i][j]=false;
			}
		}
		for (int k = 1; k < 6; k++) {
			coupleCores(0, k);
		}
		coupleCores(1,7);
		coupleCores(1,2);
		coupleCores(2,3);
		coupleCores(3,4);
		coupleCores(4,5);
		coupleCores(5,6);
		coupleCores(1,18);
		coupleCores(1,7);
		coupleCores(2,8);
		coupleCores(2,9);
		
		coupleCores(3,10);
		coupleCores(3,11);
		coupleCores(4,12);
		coupleCores(4,13);
		coupleCores(5,14);
		coupleCores(5,15);
		coupleCores(6,16);
		coupleCores(6,17);
		
		coupleCores(7,8);
		coupleCores(8,9);
		coupleCores(9,10);
		coupleCores(10,11);
		coupleCores(11,12);
		coupleCores(12,13);
		coupleCores(13,14);
		coupleCores(14,15);
		coupleCores(15,16);
		coupleCores(16,17);
		coupleCores(17,18);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param i index of spectrum (core)
	 * @param j index of spectrum (slot)
	 * @return a list of neighbor slots
	 */
	public ArrayList<Slot> getCoupledFibersInUse(int i, int j){
		ArrayList<Slot> coupledFibers = new ArrayList<Slot>();
		for (int k = 0; k < coupling[i].length; k++) {
			if (coupling[i][k]) {
				coupledFibers.add(new Slot(i, k));
			}
		}
		
		return coupledFibers;
	}
	
	/**
	 * @param i
	 * @param j
	 */
	public void coupleCores(int i, int j) {
		coupling[i][j] = true;
		coupling[j][i] = true;
	}

}

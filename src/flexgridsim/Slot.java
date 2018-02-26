package flexgridsim;

/**
 * @author pedrom
 *
 */
public class Slot {
	/**
	 * 
	 */
	public int c;
	/**
	 * 
	 */
	public int s;
	
	/**
	 * 
	 */
	/**
	 * @param x
	 * @param y
	 */
	public Slot(int x, int y) {
		super();
		this.c = x;
		this.s = y;
	}
	
	
	@Override
	public String toString(){
		return "("+c+","+s+")";
	}
}

package flexgridsim.util;

/**
 * @author pedrom
 *
 */
public class Decibel {
	
	/**
	 * @param v1
	 * @param v2
	 * @return sum of two values in dB
	 */
	public static double add(double v1, double v2) {
		return 10* Math.log10(Math.pow(10, (v1/10))+Math.pow(10, (v2/10)));
	}
	
	/**
	 * @param v1
	 * @param v2
	 * @return subtraction of two values in dB
	 */
	public static double subtract(double v1, double v2) {
		return 10* Math.log10(Math.pow(10, (v1/10))-Math.pow(10, (v2/10)));
	}
}

package flexgridsim;

/**
 * The Class Modulations for use withotut SDM, deprecated
 */
public class Modulations {
	
	/** The Constant distance. */
	public static final int distance[] = {10000000,5000,3000,1750,750,250};
	
	
	/**
	 * Number of modulations.
	 *
	 * @return the int
	 */
	public static int numberOfModulations(){
		return 6;
	}
	/**
	 * Gets the bandwidth.
	 *
	 * @param modulationLevel the modulation level
	 * @return the bandwidth which modulation level can transmit
	 */
	public static double getBandwidth(int modulationLevel){
		switch (modulationLevel){
		case 0:
			return 12.5;
		case 1: 
			return 25.0;
		case 2: 
			return 37.5;
		case 3: 
			return 50.0;
		case 4: 
			return 62.5;
		case 5: 
			return 75.0;
		default:
			return 0;
		}
	}
	
	/**
	 * Gets the modulation level.
	 *
	 * @param bandwidth the bandwidth
	 * @return the modulation level
	 */
	public static int getModulationLevel(double bandwidth){
		if (bandwidth <= 12.5){
			return 0;
		} else if (bandwidth > 12.5 && bandwidth <= 25){
			return 1;
		} else if (bandwidth > 25 && bandwidth <= 37.5){
			return 2;
		} else if (bandwidth > 37.5 && bandwidth <= 50){
			return 3;
		} else if (bandwidth > 50 && bandwidth <= 62.5){
			return 4;
		} else if (bandwidth > 62.5){
			return 5;
		} else {
			return 0;
		}
	}
	
	/**
	 * Gets the power consumption.
	 *
	 * @param modulationLevel the modulation level
	 * @return the power consumption of the modulation level
	 */
	public static double getPowerConsumption(int modulationLevel){
		switch (modulationLevel){
		case 0:
			return 47.13;
		case 1: 
			return 62.75;
		case 2: 
			return 78.38;
		case 3: 
			return 94.0;
		case 4: 
			return 109.63;
		case 5: 
			return 125.23;
		default:
			return 47.3;
		}
	}
	
	/**
	 * Gets the max distance.
	 *
	 * @param modulationLevel the modulation level
	 * @return the max distance
	 */
	public static int getMaxDistance(int modulationLevel){
		if (modulationLevel >= 0 && modulationLevel <= 5){
			return distance[modulationLevel];
		} else {
			return distance[0];
		}
	}
	
	
	/**
	 * Gets the modulation by distance.
	 *
	 * @param givendistance the distance
	 * @return the modulation by distance
	 */
	public static int getModulationByDistance(int givendistance){
		if (givendistance <= distance[5]){
			return 5;
		} else if (givendistance >  distance[5] && givendistance <=  distance[4]){
			return 4;
		} else if (givendistance >  distance[4] && givendistance <=  distance[3]){
			return 3;
		} else if (givendistance >  distance[3] && givendistance <=  distance[2]){
			return 2;
		} else if (givendistance >  distance[2] && givendistance <=  distance[1]){
			return 1;
		} else if (givendistance >  distance[1]){
			return 0;
		} else {
			return 0;
		}
	}
}

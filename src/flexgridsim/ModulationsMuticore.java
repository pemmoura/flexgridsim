package flexgridsim;

/**
 * @author pedrom
 *
 */
public class ModulationsMuticore {
		/**
		 * The Class Modulations.
		 */
		public static final int numberOfModulations = 4;
		
		/**
		 * 
		 */
		public static final int numberOfSymbols[] = {1,2,4,6};
		/** The Constant distance. */
		public static final int maxDistance[] = {13851,13851,5937,2289};
		/**
		 * SNR threshold for the correct 
		 */
		public static final double SNR_THRESHOLD[] = {4.2,7.2,13.9,19.8};
		
		
		/**
		 * 
		 */
		public static final double inBandXT[] = {-7,-9,-13,-15};
		/**
		 * Number of modulations.
		 *
		 * @return the int
		 */
		public static int numberOfModulations(){
			return numberOfModulations;
		}
		/**
		 * Gets the bandwidth.
		 *
		 * @param modulationLevel the modulation level
		 * @param slotCapacity 
		 * @return the bandwidth which modulation level can transmit
		 */
		public static double getBandwidth(int modulationLevel){
			return numberOfSymbols[modulationLevel];
		}
		
		
		/**
		 * Gets the max distance.
		 *
		 * @param modulationLevel the modulation level
		 * @return the max distance
		 */
		public static int getMaxDistance(int modulationLevel){
			if (modulationLevel >= 0 && modulationLevel <= 5){
				return maxDistance[modulationLevel];
			} else {
				return maxDistance[0];
			}
		}
		
		/**
		 * Gets the max distance.
		 *
		 * @param modulationLevel the modulation level
		 * @return the max distance
		 */
		public static double getSNRThreshold(int modulationLevel){
			if (modulationLevel >= 0 && modulationLevel <= 3){
				return SNR_THRESHOLD[modulationLevel];
			} else {
				return SNR_THRESHOLD[0];
			}
		}
		
		/**
		 * Gets the modulation by distance.
		 *
		 * @param givendistance the distance
		 * @return the modulation by distance
		 */
		public static int getModulationByDistance(int givendistance){
			int i = numberOfModulations-1;
			while (givendistance <= maxDistance[i] && i >= 0){
				i--;
			}
			return i;
		}
		
		/**
		 * @param cores
		 * @return worst aggregate inter core crosstalk crosstalk 
		 */
		public static double interCoreXT(int cores) {
			if (cores <= 7) {
				return -45;
			} else if (cores > 7 && cores <=12) {
				return -31;
			} else if (cores >12) {
				return -20;
			} else {
				return 1000;
			}
		}
		
}

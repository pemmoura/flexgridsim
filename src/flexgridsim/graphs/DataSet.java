package flexgridsim.graphs;

import java.util.ArrayList;

/**
 * Class of a data set, and arraylist that has a group of data to be written on output files
 * 
 * @author pedrom
 * 
 */
public class DataSet {
	
	private ArrayList<double[]> dots;
	private int dimension;

	/**
	 * Instantiates a new data set.
	 *
	 * @param dimension the of the data set
	 */
	public DataSet(int dimension) {
		this.dots = new ArrayList<double[]>();
		this.dimension = dimension;
	}

	/**
	 * Gets the number of dots.
	 *
	 * @return the number of dots
	 */
	public int getNumberOfDots() {
		return dots.size();
	}

	/**
	 * Gets the dot value.
	 *
	 * @param dotIndex dot index
	 * @param valueIndex value index
	 * @return the dot value
	 */
	public double getDotValue(int dotIndex, int valueIndex){
		double[] dot = dots.get(dotIndex);
		return dot[valueIndex];
	}
	/**
	 * Convert a dot to a string to be written in a file
	 *
	 * @return the string of the dot
	 */
	public String dotToString(){
		double[] mean = dotsMean();
		double[] confidenceInterval = dotsConfidenceInterval();
		String dotString = getDotValue(0, 0) + "";
		for (int i = 1; i < mean.length; i++) {
			dotString += "\t" + mean[i] + "\t" + confidenceInterval[i];
		}
		return dotString;
	}

	/**
	 * Adds one dot.
	 *
	 * @param values the values
	 */
	public void addDot(double...values) {
		if (values.length != dimension) {
			throw new IllegalArgumentException();
		} else {
			dots.add(values);
		}
	}
	
	/**
	 * Dots sum.
	 *
	 * @return the double[]
	 */
	public double[] dotsSum(){
		double[] sum = new double[dimension];
		for (double[] dot: dots){
			for (int i=0; i<dimension; i++){
				if (dot[i]!=Double.NaN)
					sum[i] += dot[i];
			}
		}
		return sum;
	}
	
	/**
	 * Dots square sum.
	 *
	 * @return the double[]
	 */
	public double[] dotsSquareSum(){
		double[] sum2 = new double[dimension];
		for (double[] dot: dots){
			for (int i=0; i<dimension; i++){
				if (dot[i]!=Double.NaN)
					sum2[i] += dot[i]*dot[i];
			}
		}
		return sum2;
	}
	/**
	 * Calculate the mean for each field.
	 *
	 * @return an array with a mean for each field
	 */
	public double[] dotsMean(){
		double[] mean = dotsSum();
		for (int i = 0; i < mean.length; i++) {
			mean[i] = mean[i] / dots.size();
		}
		return mean;
	}
	
	/**
	 * Dots standard variation.
	 *
	 * @return the double[]
	 */
	public double[] dotsStandardVariation(){
		int N = dots.size();
		double[] mean = dotsMean();
		double[] sum2 = dotsSquareSum();
		double stdVar[] = new double[dimension];
		for (int i = 0; i < stdVar.length; i++) {
			stdVar[i] = Math.sqrt((sum2[i] - N * (mean[i] * mean[i]))/(N - 1));
		}
		return stdVar;
	}
	/**
	 * Calculate the confidence interval for each field.
	 *
	 * @return an array with a confidence interval for each field
	 */
	public double[] dotsConfidenceInterval(){
		//1.96*(desvioMBBR/sqrt(N-1));
		int N = dots.size();
		double stdVar[] = dotsStandardVariation();
		double confidenceInterval[] = new double[dimension];
		for (int i = 0; i < confidenceInterval.length; i++) {
			confidenceInterval[i] = 1.96 * (stdVar[i] / Math.sqrt(N-1));
		}
		return confidenceInterval;
	}
	
}

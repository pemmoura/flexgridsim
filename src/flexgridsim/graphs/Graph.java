package flexgridsim.graphs;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Define the methods all graphs must have.
 *
 * @author pedrom
 */
public class Graph {

	private String name;
	private String dotsFileName;
	private DataSet dataSet;


	/**
	 * Instantiates a new graph.
	 *
	 * @param name the name of the graph
	 * @param dotsFileName the output dots file name
	 * @param dataSetDimension the data set dimension
	 */
	public Graph(String name, String dotsFileName,
			int dataSetDimension) {
		super();
		this.name = name;
		this.dotsFileName = dotsFileName;
		this.dataSet = new DataSet(dataSetDimension);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Write dots to file.
	 */
	public void writeDotsToFile() {
		FileWriter fStream;
		try {
			fStream = new FileWriter(dotsFileName, true);
			fStream.append(dataSet.dotToString()+System.getProperty("line.separator"));
			fStream.close();
		} catch (IOException e) {
			System.out.println("Error writing the graph file");
		} catch (IndexOutOfBoundsException e){
			System.out.println("No dots to write");
		}
	}
	

	/**
	 * Gets the data set.
	 *
	 * @return the data set
	 */
	public DataSet getDataSet() {
		return dataSet;
	}
}

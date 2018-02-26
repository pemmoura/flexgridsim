/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexgridsim;

/**
 * The Main class takes care of the execution of the simulator, which includes
 * dealing with the arguments called (or not) on the command line.
 * 
 * @author andred
 */
public class Main {

	/**
	 * Instantiates a Simulator object and takes the arguments from the command
	 * line. Based on the number of arguments, can detect if there are too many
	 * or too few, which prints a message teaching how to run FlexGridSim. If the
	 * number is correct, detects which arguments were applied and makes sure
	 * they have the expected effect.
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		Simulator flexgridsim;
		String usage = "Usage: flexgridsim simulation_file seed [-trace] [-verbose] [minload maxload step]";
		String simConfigFile;
		boolean verbose = false;
		boolean trace = false;
		int seed = 1;
		double minload = 0, maxload = 0, step = 1;

		if (args.length < 2 || args.length > 7) {
			System.out.println(usage);
			System.exit(0);
		} else {
			if (args.length == 3 || args.length == 6) {
				if (args[2].equals("-verbose")) {
					verbose = true;
				} else {
					if (args[2].equals("-trace")) {
						trace = true;
					} else {
						System.out.println(usage);
						System.exit(0);
					}
				}
			}
			if (args.length == 4 || args.length == 7) {
				if ((args[2].equals("-trace") && args[3].equals("-verbose"))
						|| (args[3].equals("-trace") && args[2]
								.equals("-verbose"))) {
					trace = true;
					verbose = true;
				} else {
					System.out.println(usage);
					System.exit(0);
				}
			}
			if (args.length == 5 || args.length == 6 || args.length == 7) {
				minload = Double.parseDouble(args[args.length - 3]);
				maxload = Double.parseDouble(args[args.length - 2]);
				step = Double.parseDouble(args[args.length - 1]);
			}
		}

		simConfigFile = args[0];
		seed = Integer.parseInt(args[1]);

		for (double load = minload; load <= maxload; load += step) {
			flexgridsim = new Simulator();
			flexgridsim.Execute(simConfigFile, trace, verbose, load, seed);
		}
	}
}

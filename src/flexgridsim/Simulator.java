/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexgridsim;

import java.io.File;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Centralizes the simulation execution. Defines what the command line
 * arguments do, and extracts the simulation information from the XML file.
 * 
 * @author andred
 */
public class Simulator {

    private static final String simName = new String("flexgridsim");
    private static final Float simVersion = new Float(2.0);
    
    /** Verbose flag. */
    public static boolean verbose = false;
    
    /** Trace flag. */
    public static boolean trace = false;
    
    /**
     * Executes simulation based on the given XML file and the used command line arguments.
     * 
     * @param simConfigFile name of the XML file that contains all information about the simulation
     * @param trace activates the Tracer class functionalities
     * @param verbose activates the printing of information about the simulation, on runtime, for debugging purposes
     * @param forcedLoad range of loads for which several simulations are automated; if not specified, load is taken from the XML file
     * @param numberOfSimulations a number in the interval [1,25] that defines up to 25 different random simulations
     */
    public void Execute(String simConfigFile, boolean trace, boolean verbose, double forcedLoad, int numberOfSimulations) {

        Simulator.verbose = verbose;
        Simulator.trace = trace;

        if (Simulator.verbose) {
            System.out.println("#################################");
            System.out.println("# Simulator " + simName + " version " + simVersion.toString() + "  #");
            System.out.println("#################################\n");
        }
        if (Simulator.verbose) {
            System.out.println("(0) Accessing simulation file " + simConfigFile + "...");
        }
        try {
	        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse(new File(simConfigFile));
	
	        // normalize text representation
	        doc.getDocumentElement().normalize();
	
	        // check the root TAG name and version
	        if (!doc.getDocumentElement().getNodeName().equals(simName)) {
	            System.out.println("Root element of the simulation file is " + doc.getDocumentElement().getNodeName() + ", " + simName + " is expected!");
	            System.exit(0);
	        }
	        if (!doc.getDocumentElement().hasAttribute("version")) {
	            System.out.println("Cannot find version attribute!");
	            System.exit(0);
	        }
	        if (Float.compare(new Float(doc.getDocumentElement().getAttribute("version")), simVersion) > 0) {
	            System.out.println("Simulation config file requires a newer version of the simulator!");
	            System.exit(0);
	        }
	        
	        OutputManager gp = new OutputManager((Element) doc.getElementsByTagName("graphs").item(0));
	        
	        for (int seed = 1; seed <= numberOfSimulations; seed++) {
	        
	            long begin = System.currentTimeMillis();
	
	            
	            if (Simulator.verbose) {
	                System.out.println("(0) Done. (" + Float.toString((float) ((float) (System.currentTimeMillis() - begin) / (float) 1000)) + " sec)\n");
	            }
	
	            /*
	             * Extract physical topology part
	             */
	            begin = System.currentTimeMillis();
	            if (Simulator.verbose) {
	                System.out.println("(1) Loading physical topology information...");
	            }
	
	            PhysicalTopology pt = new PhysicalTopology((Element) doc.getElementsByTagName("physical-topology").item(0));
	            if (Simulator.verbose) {
	                System.out.println(pt);
	            }
	
	            if (Simulator.verbose) {
	                System.out.println("(1) Done. (" + Float.toString((float) ((float) (System.currentTimeMillis() - begin) / (float) 1000)) + " sec)\n");
	            }
	
	            /*
	             * Extract virtual topology part
	             */
	            begin = System.currentTimeMillis();
	            if (Simulator.verbose) {
	                System.out.println("(2) Loading virtual topology information...");
	            }
	
	            VirtualTopology vt = new VirtualTopology((Element) doc.getElementsByTagName("virtual-topology").item(0), pt);
	            if (Simulator.verbose) {
	                System.out.println(vt);
	            }
	
	            if (Simulator.verbose) {
	                System.out.println("(2) Done. (" + Float.toString((float) ((float) (System.currentTimeMillis() - begin) / (float) 1000)) + " sec)\n");
	            }
	
	            /*
	             * Extract simulation traffic part
	             */
	            begin = System.currentTimeMillis();
	            if (Simulator.verbose) {
	                System.out.println("(3) Loading traffic information...");
	            }
	
	            EventScheduler events = new EventScheduler();
	            TrafficGenerator traffic = new TrafficGenerator((Element) doc.getElementsByTagName("traffic").item(0), forcedLoad);
	            traffic.generateTraffic(pt, events, seed);
	
	            if (Simulator.verbose) {
	                System.out.println("(3) Done. (" + Float.toString((float) ((float) (System.currentTimeMillis() - begin) / (float) 1000)) + " sec)\n");
	            }
	
	            /*
	             * Load graph configuration
	             */
	            begin = System.currentTimeMillis();
	            if (Simulator.verbose) {
	                System.out.println("(4) Loading graphs configuration...");
	            }
	            
	            
	            /*
	             * Extract simulation setup part
	             */
	            begin = System.currentTimeMillis();
	            if (Simulator.verbose) {
	                System.out.println("(4) Loading simulation setup information...");
	            }
	
	            MyStatistics st = MyStatistics.getMyStatisticsObject();
	            st.statisticsSetup(gp, pt, traffic, pt.getNumNodes(), 3, 0, forcedLoad);
	
	            Tracer tr = Tracer.getTracerObject();
	            if (Simulator.trace == true)
	            {
	            	if (forcedLoad == 0) {
	                	tr.setTraceFile(simConfigFile.substring(4, simConfigFile.length() - 4) + ".trace");
	            	} else {
	                	tr.setTraceFile(simConfigFile.substring(4, simConfigFile.length() - 4) + "_Load_" + Double.toString(forcedLoad) + ".trace");
	            	}
	            }
	            tr.toogleTraceWriting(Simulator.trace);
	            
	            String rsaModule = "flexgridsim.rsa." + ((Element) doc.getElementsByTagName("rsa").item(0)).getAttribute("module");
	            if (Simulator.verbose) {
	                System.out.println("RSA module: " + rsaModule);
	            }
	            ControlPlane cp = new ControlPlane(((Element) doc.getElementsByTagName("rsa").item(0)), events, rsaModule, pt, vt, traffic);
	
	            if (Simulator.verbose) {
	                System.out.println("(4) Done. (" + Float.toString((float) ((float) (System.currentTimeMillis() - begin) / (float) 1000)) + " sec)\n");
	            }
	            
	            
	            /*
	             * Run the simulation
	             */
	            begin = System.currentTimeMillis();
	            if (Simulator.verbose) {
	                System.out.println("(5) Running the simulation...");
	            }
	            System.out.println(simConfigFile+ " -> Load "+ forcedLoad +": Running the simulation number " + seed);
	            new SimulationRunner(cp, events);
	
	            if (Simulator.verbose) {
	                System.out.println("(5) Done. (" + Float.toString((float) ((float) (System.currentTimeMillis() - begin) / (float) 1000)) + " sec)\n");
	            }
	
	            if (Simulator.verbose) {
	                if (forcedLoad == 0) {
	                    System.out.println("Statistics (" + simConfigFile + "):\n");
	                } else {
	                    System.out.println("Statistics for " + Double.toString(forcedLoad) + " erlangs (" + simConfigFile + "):\n");
	                }
	                System.out.println(st.fancyStatistics());
	            } else {
	                st.calculateLastStatistics();
	            }
	            
	            // Terminate MyStatistics singleton
	            st.finish();
	            if (Simulator.trace == true)
	            	tr.finish();
	        }
	        gp.writeAllToFiles();
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());

        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();

        } catch (Throwable t) {
            t.printStackTrace();
        }
        
    }
}
  

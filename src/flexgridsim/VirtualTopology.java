
package flexgridsim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import org.w3c.dom.*;

/**
 * The virtual topology is created based on a given Physical Topology and
 * on the lightpaths specified on the XML file.
 * 
 * @author andred
 */
public class VirtualTopology {

    private long nextLightpathID = 0;
    private TreeSet<LightPath>[][] adjMatrix;
    private int adjMatrixSize;
    private Map<Long, LightPath> lightPaths;
    private PhysicalTopology pt;
    private Tracer tr = Tracer.getTracerObject();
//    private MyStatistics st = MyStatistics.getMyStatisticsObject();
    
    private static class LightPathSort implements Comparator<LightPath> {

        public int compare(LightPath lp1, LightPath lp2) {
            if (lp1.getID() < lp2.getID()) {
                return -1;
            }
            if (lp1.getID() > lp2.getID()) {
                return 1;
            }
            return 0;
        }
    }
    
    /**
     * Creates a new VirtualTopology object.
     * 
     * @param xml file that contains all simulation information
     * @param pt Physical Topology of the network
     */
    @SuppressWarnings("unchecked")
    public VirtualTopology(Element xml, PhysicalTopology pt) {
        int nodes, lightpaths;

        lightPaths = new HashMap<Long, LightPath>();

        try {
            this.pt = pt;
            if (Simulator.verbose) {
                System.out.println(xml.getAttribute("name"));
            }

            adjMatrixSize = nodes = pt.getNumNodes();

            // Process lightpaths
            adjMatrix = new TreeSet[nodes][nodes];
            for (int i = 0; i < nodes; i++) {
                for (int j = 0; j < nodes; j++) {
                    if (i != j) {
                        adjMatrix[i][j] = new TreeSet<LightPath>(new LightPathSort());
                    }
                }
            }
            NodeList lightpathlist = xml.getElementsByTagName("lightpath");
            lightpaths = lightpathlist.getLength();
            if (Simulator.verbose) {
                System.out.println(Integer.toString(lightpaths) + " lightpath(s)");
            }
            if (lightpaths > 0) {
                //TODO
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    /**
     * First, creates a lightpath in the Physical Topology through the createLightpathInPT
     * function. Then, gets the lightpath's source and destination nodes, so a new
     * LightPath object can finally be created and added to the lightPaths HashMap
     * and to the adjMatrix TreeSet.
     *
     * @param links list of integers that represent the links that form the lightpath
     * @param slotList list of slots
     * @param modulationLevel the modulation level
     * @return -1 if LightPath object cannot be created, or its unique identifier otherwise
     */
    public long createLightpath(int[] links, ArrayList<Slot> slotList, int modulationLevel) {
        LightPath lp;
        int src, dst;
        long id;
        if (links.length < 1) {
            throw (new IllegalArgumentException());
        } else {
            if (!canCreateLightpath(links, slotList, modulationLevel)) {
                return -1;
            }
            createLightpathInPT(links, slotList);
            src = pt.getLink(links[0]).getSource();
            dst = pt.getLink(links[links.length - 1]).getDestination();
            id = this.nextLightpathID;
            lp = new LightPath(id, src, dst, links, slotList, modulationLevel);
            adjMatrix[src][dst].add(lp);
            lightPaths.put(nextLightpathID, lp);
            tr.createLightpath(lp);
            this.nextLightpathID++;

            return id;
        }
    }
    
    /**
     * First, removes a given lightpath in the Physical Topology through the removeLightpathInPT
     * function. Then, gets the lightpath's source and destination nodes, to remove it 
     * from the lightPaths HashMap and the adjMatrix TreeSet.
     * 
     * @param id the unique identifier of the lightpath to be removed
     * @return true if operation was successful, or false otherwise
     */
    public boolean removeLightPath(long id) {
        int src, dst;
        LightPath lp;

        if (id < 0) {
            throw (new IllegalArgumentException());
        } else {
            if (!lightPaths.containsKey(id)) {
                return false;
            }
            lp = lightPaths.get(id);
            removeLightpathFromPT(lp.getLinks(), lp.getSlotList());
            src = lp.getSource();
            dst = lp.getDestination();

            lightPaths.remove(id);
            adjMatrix[src][dst].remove(lp);
            tr.removeLightpath(lp);

            return true;
        }
    }
    
    
    /**
     * Retrieves a determined LightPath object from the Virtual Topology.
     * 
     * @param id the lightpath's unique identifier
     * @return the required lightpath
     */
    public LightPath getLightpath(long id) {
        if (id < 0) {
            throw (new IllegalArgumentException());
        } else {
            if (lightPaths.containsKey(id)) {
                return lightPaths.get(id);
            } else {
                return null;
            }
        }
    }
    
    /**
     * Retrieves the TreeSet with all LightPath objects that
     * belong to the Virtual Topology.
     * 
     * @param src the lightpath's source node
     * @param dst the lightpath's destination node
     * @return the TreeSet with all of the lightpaths
     */
    public TreeSet<LightPath> getLightpaths(int src, int dst) {
        return new TreeSet<LightPath>(adjMatrix[src][dst]);
    }
    
    /**
     * Retrieves the adjacency matrix of the Virtual Topology.
     * 
     * @return the VirtualTopology object's adjMatrix
     */
    public TreeSet<LightPath>[][] getAdjMatrix() {
        return adjMatrix;
    }
    
    
    /**
     * Says whether or not a lightpath can be created, based only on its
     * links and slots.
     *
     * @param links list of integers that represent the links that form the lightpath
     * @param slotList list of slots
     * @param modulation modulation level
     * @return true if the lightpath can be created, or false otherwise
     */
    public boolean canCreateLightpath(int[] links, ArrayList<Slot> slotList, int modulation) {
        try {
	        for (int i = 0; i < links.length; i++) {
	            if (!pt.getLink(links[i]).areSlotsAvailable(slotList, modulation)) {
	                return false;
	            }
	        }
	        for (int i = 0; i < links.length; i++) {
	        	for (Slot slot : slotList) {
	        		if (pt.getLink(links[i]).getNoise(slot)==0) {
	        			//TODO
			        }
				}
		        
	        }
        } catch (IllegalArgumentException e){
			System.out.println("Illegal argument for areSlotsAvailable");
			return false;
		}
       
       
        return true;
    }
    
    /**
     * Reserves, in the physical topology, the resources a given lightpath needs:
     * links, wavelengths and wavelength converters (if necessary).
     * 
     * @param links list of integers that represent the links that form the lightpath 
     * @param firstSlot list of wavelength values used in the lightpath links
     */
    private void createLightpathInPT(int[] links, ArrayList<Slot> slotList) {
        for (int i = 0; i < links.length; i++) {
            pt.getLink(links[i]).reserveSlots(slotList);
        }
    }
    
    /**
     * Releases, in the physical topology, the resources a given lightpath was using:
     * links, wavelengths and wavelength converters (if necessary).
     * 
     * @param links list of integers that represent the links that form the lightpath
     * @param firstSlot list of wavelength values used in the lightpath links
     */
    private void removeLightpathFromPT(int[] links, ArrayList<Slot> slotList) {
        for (int i = 0; i < links.length; i++) {
            pt.getLink(links[i]).releaseSlots(slotList);
        }
    }
    

    
    /**
     * Prints all lightpaths belonging to the Virtual Topology.
     * 
     * @return string containing all the elements of the adjMatrix TreeSet
     */
    @Override
    public String toString() {
        String vtopo = "";
        for (int i = 0; i < adjMatrixSize; i++) {
            for (int j = 0; j < adjMatrixSize; j++) {
                if (adjMatrix[i][j] != null) {
                    if (!adjMatrix[i][j].isEmpty()) {
                        vtopo += adjMatrix[i][j].toString() + "\n\n";
                    }
                }
            }
        }
        return vtopo;
    }
}

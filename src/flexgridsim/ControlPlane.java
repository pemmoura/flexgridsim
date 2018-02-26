/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexgridsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import flexgridsim.rsa.ControlPlaneForRSA;
import flexgridsim.rsa.RSA;

/**
 * The Control Plane is responsible for managing resources and
 * connection within the network.
 */
public class ControlPlane implements ControlPlaneForRSA {

    private RSA rsa;
    private PhysicalTopology pt;
    private VirtualTopology vt;
    private Map<Flow, LightPath> mappedFlows; // Flows that have been accepted into the network
    private Map<Long, Flow> activeFlows; // Flows that have been accepted or that are waiting for a decision 
    private Tracer tr = Tracer.getTracerObject();
    private MyStatistics st = MyStatistics.getMyStatisticsObject();

	/**
	 * Creates a new ControlPlane object.
	 *
	 * @param xml the xml
	 * @param eventScheduler the event scheduler
	 * @param rsaModule the name of the RWA class
	 * @param pt the network's physical topology
	 * @param vt the network's virtual topology
	 * @param traffic the traffic
	 */
    public ControlPlane(Element xml, EventScheduler eventScheduler, String rsaModule, PhysicalTopology pt, VirtualTopology vt, TrafficGenerator traffic) {
        @SuppressWarnings("rawtypes")
		Class RSAClass;
        mappedFlows = new HashMap<Flow, LightPath>();
        activeFlows = new HashMap<Long, Flow>();
        this.pt = pt;
        this.vt = vt;

        try {
            RSAClass = Class.forName(rsaModule);
            rsa = (RSA) RSAClass.newInstance();
            rsa.simulationInterface(xml, pt, vt, this, traffic);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    /**
     * Deals with an Event from the event queue.
     * If it is of the FlowArrivalEvent kind, adds it to the list of active flows.
     * If it is from the FlowDepartureEvent, removes it from the list.
     * 
     * @param event the Event object taken from the queue 
     */
    public void newEvent(Event event) {
        if (event instanceof FlowArrivalEvent) {
            newFlow(((FlowArrivalEvent) event).getFlow());
            rsa.flowArrival(((FlowArrivalEvent) event).getFlow());
        } else if (event instanceof FlowDepartureEvent) {
            Flow removedFlow = removeFlow(((FlowDepartureEvent) event).getID());
            rsa.flowDeparture(removedFlow);
        } 
    }

    /**
     * Retrieves a Flow object from the list of active flows.
     * 
     * @param id the unique identifier of the Flow object
     * @return the required Flow object
     */
    public Flow getFlow(long id) {
        return activeFlows.get(id);
    }
    
    /**
     * Adds a given active Flow object to a determined Physical Topology.
     * 
     * @param id unique identifier of the Flow object
     * @param lightpath the Path, or list of LighPath objects
     * @return true if operation was successful, or false if a problem occurred
     */
    public boolean acceptFlow(long id, LightPath lightpath) {
        Flow flow;

        if (id < 0) {
            throw (new IllegalArgumentException());
        } else {
            if (!activeFlows.containsKey(id)) {
            	throw (new IllegalArgumentException());
            }
            flow = activeFlows.get(id);
            if (!canAddFlowToPT(flow, lightpath)) {
                return false;
            } 
            addFlowToPT(flow, lightpath);
            mappedFlows.put(flow, lightpath);
            tr.acceptFlow(flow, lightpath);
            st.acceptFlow(flow, lightpath);
            flow.setAccepeted(true);
            return true;
        }
    }

    /**
     * Removes a given Flow object from the list of active flows.
     * 
     * @param id unique identifier of the Flow object
     * @return true if operation was successful, or false if a problem occurred
     */
    public boolean blockFlow(long id) {
        Flow flow;

        if (id < 0) {
            throw (new IllegalArgumentException());
        } else {
            if (!activeFlows.containsKey(id)) {
                return false;
            }
            flow = activeFlows.get(id);
            if (mappedFlows.containsKey(flow)) {
                return false;
            }
            activeFlows.remove(id);
            tr.blockFlow(flow);
            st.blockFlow(flow);
            return true;
        }
    }
    
    /**
     * Removes a given Flow object from the Physical Topology and then
     * puts it back, but with a new route (set of LightPath objects). 
     * 
     * @param id unique identifier of the Flow object
     * @param lightpath list of LightPath objects, which form a Path
     * @return true if operation was successful, or false if a problem occurred
     */
    public boolean rerouteFlow(long id, LightPath lightpath) {
        Flow flow;
        LightPath oldPath;
        if (id < 0) {
            throw (new IllegalArgumentException());
        } else {
            if (!activeFlows.containsKey(id)) {
                return false;
            }
            flow = activeFlows.get(id);
            if (!mappedFlows.containsKey(flow)) {
                return false;
            }
            oldPath = mappedFlows.get(flow);
            removeFlowFromPT(flow, lightpath);
            if (!canAddFlowToPT(flow, lightpath)) {
                addFlowToPT(flow, oldPath);
                return false;
            }
            addFlowToPT(flow, lightpath);
            mappedFlows.put(flow, lightpath);
            //tr.flowRequest(id, true);
            return true;
        }
    }
    
    /**
     * Adds a given Flow object to the HashMap of active flows.
     * The HashMap also stores the object's unique identifier (ID). 
     * 
     * @param flow Flow object to be added
     */
    private void newFlow(Flow flow) {
        activeFlows.put(flow.getID(), flow);
    }
    
    /**
     * Removes a given Flow object from the list of active flows.
     * 
     * @param id the unique identifier of the Flow to be removed
     * 
     * @return the flow object
     */
    private Flow removeFlow(long id) {
        Flow flow;
        LightPath lightpaths;

        if (activeFlows.containsKey(id)) {
            flow = activeFlows.get(id);
            if (mappedFlows.containsKey(flow)) {
                lightpaths = mappedFlows.get(flow);
                removeFlowFromPT(flow, lightpaths);
                mappedFlows.remove(flow);
            }
            activeFlows.remove(id);
            return flow;
        }
        return null;
    }
    
    /**
     * Removes a given Flow object from a Physical Topology. 
     * 
     * @param flow the Flow object that will be removed from the PT
     * @param lightpaths a list of LighPath objects
     */
    private void removeFlowFromPT(Flow flow, LightPath lightpath) {
        int[] links;
        links = lightpath.getLinks();
        for (int j = 0; j < links.length; j++) {
            pt.getLink(links[j]).releaseSlots(lightpath.getSlotList());
            pt.getLink(links[j]).updateNoise(lightpath.getSlotList(), flow.getModulationLevel());
        }
        vt.removeLightPath(lightpath.getID());
    }
    
    /**
     * Says whether or not a given Flow object can be added to a 
     * determined Physical Topology, based on the amount of bandwidth the
     * flow requires opposed to the available bandwidth.
     * 
     * @param flow the Flow object to be added 
     * @param lightpaths list of LightPath objects the flow uses
     * @return true if Flow object can be added to the PT, or false if it can't
     */
    private boolean canAddFlowToPT(Flow flow, LightPath lightpath) {
        int[] links;
        // Test the availability of resources
        
        links = lightpath.getLinks();
        for (int j = 0; j < links.length; j++) {
            if (pt.getLink(links[j]).areSlotsAvailable(lightpath.getSlotList(), flow.getModulationLevel())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Adds a Flow object to a Physical Topology.
     * This means adding the flow to the network's traffic,
     * which simply decreases the available bandwidth.
     * 
     * @param flow the Flow object to be added 
     * @param lightpaths list of LightPath objects the flow uses
     */
    private void addFlowToPT(Flow flow, LightPath lightpath) {
        int[] links = lightpath.getLinks();
        // Implements it
        for (int j = 0; j < links.length; j++) {
            pt.getLink(links[j]).reserveSlots(lightpath.getSlotList());
            pt.getLink(links[j]).updateNoise(lightpath.getSlotList(), flow.getModulationLevel());
        }
        
    }
    
    /**
     * Retrieves a Path object, based on a given Flow object.
     * That's possible thanks to the HashMap mappedFlows, which
     * maps a Flow to a Path.
     * 
     * @param flow Flow object that will be used to find the Path object
     * @return Path object mapped to the given flow 
     */
    public LightPath getPath(Flow flow) {
        return mappedFlows.get(flow);
    }
    
    /**
     * Retrieves the complete set of Flow/Path pairs listed on the
     * mappedFlows HashMap.
     * 
     * @return the mappedFlows HashMap
     */
    public Map<Flow, LightPath> getMappedFlows() {
        return mappedFlows;
    }
    
    public boolean canGroom(Flow flow){
    	for (Map.Entry<Flow, LightPath> entry : mappedFlows.entrySet()){
    		LightPath lp = entry.getValue();
    		if (flow.getSource()==lp.getSource() && flow.getDestination() == lp.getDestination()){
    			int demandInSlots = (int) Math.ceil(flow.getRate() / (double) Modulations.getBandwidth(lp.getModulationLevel()));
    			int slotCount = lp.getSlotList().get(lp.getSlotList().size()-1).s;
    			ArrayList<Slot> slotList = new ArrayList<Slot>();
    			for (int i = slotCount; i < slotCount+demandInSlots; i++) {
    				Slot p = new Slot(0,i);
    				slotList.add(p);
				}
    			boolean contiguity = true;
    			for (int j = 0; j < lp.getLinks().length; j++) {
    				if (pt.getLink(lp.getLink(j)).areSlotsAvailable(slotList, flow.getModulationLevel())){
    					contiguity = false;
    					break;
    				}
				}
    			if (contiguity == true){
    				for (int linkID : lp.getLinks()) {
    		            pt.getLink(linkID).reserveSlots(lp.getSlotList());
    		        }
    				flow.setSlotList(slotList);
    				st.groomedFlow(flow);
    				return true;
    			}
    		}
    	    
    	}
    	return false;
    }
    
    /**
     * Groom flow.
     * @param flow 
     *
     * @param lp the lp
     * @param demandInSlots 
     */
    public void groomFlow(Flow flow, LightPath lp){
		for (int linkID : lp.getLinks()) {
            pt.getLink(linkID).reserveSlots(lp.getSlotList());
        }
		st.groomedFlow(flow);
    }
 

}

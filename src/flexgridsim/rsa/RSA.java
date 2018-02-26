/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package flexgridsim.rsa;

import org.w3c.dom.Element;

import flexgridsim.*;

/**
 * This is the interface that provides some methods for the RWA class.
 * These methods basically deal with the simulation interface and with
 * the arriving and departing flows.
 * 
 * The Routing and Spectrum Allocation (RSA) is a optical networking problem
 * that has the goal of maximizing the number of optical connections.
 * 
 * @author andred, pedrom
 */
public interface RSA {
    
    /**
     * Simulation interface.
     *
     * @param xml the xml
     * @param pt the Physical Topology object
     * @param vt the Virtual Topology object
     * @param cp the Control Plane object
     * @param traffic the traffic
     */
    public void simulationInterface(Element xml, PhysicalTopology pt, VirtualTopology vt, ControlPlaneForRSA cp, TrafficGenerator traffic);

    /**
     * Flow arrival.
     *
     * @param flow the flow that has arrived
     */
    public void flowArrival(Flow flow);
    
    /**
     * Flow departure.
     *
     * @param flow the flow has departed
     */
    public void flowDeparture(Flow flow);

}

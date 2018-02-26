/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexgridsim;

/**
 * Simply runs the simulation, as long as there are events
 * scheduled to happen.
 * 
 * @author andred
 */
public class SimulationRunner {

    /**
     * Creates a new SimulationRunner object.
     * 
     * @param cp the the simulation's control plane
     * @param events the simulation's event scheduler
     */
	public SimulationRunner(ControlPlane cp, EventScheduler events) {
        Event event;
        Tracer tr = Tracer.getTracerObject();
        MyStatistics st = MyStatistics.getMyStatisticsObject();
        
        while ((event = events.popEvent()) != null) {
            tr.add(event);
            st.addEvent(event);
            cp.newEvent(event);
        }
    }
}

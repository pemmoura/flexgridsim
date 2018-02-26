/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package flexgridsim;

/**
 * Methods to treat the outgoing of a Flow object.
 * 
 * @author andred, pedrom
 */
public class FlowDepartureEvent extends Event{

    private long id;
    Flow flow;
    
    /**
     * Creates a new FlowDepartureEvent object.
     *
     * @param time the time
     * @param id unique identifier of the outgoing flow
     * @param flow the flow
     */
    public FlowDepartureEvent(double time, long id, Flow flow) {
    	super(time);
        this.id = id;
        this.flow = flow;
    }
    
    /**
     * Retrieves the identifier of the FlowDepartureEvent object.
     * 
     * @return the FlowDepartureEvent's id attribute
     */
    public long getID() {
        return this.id;
    }
    
    /**
     * Prints all information related to the outgoing flow.
     * 
     * @return string containing all the values of the flow's parameters
     */
    public String toString() {
        return "Departure: "+Long.toString(id);
    }

	/**
	 * Gets the flow.
	 *
	 * @return the flow
	 */
	public Flow getFlow() {
		return flow;
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package flexgridsim;

/**
 * Methods to treat the incoming of a Flow object.
 * 
 * @author andred, pedrom
 */
public class FlowArrivalEvent extends Event {
	
    private Flow flow;
    
    /**
     * Creates a new FlowArrivalEvent object.
     *
     * @param time the time
     * @param flow the arriving flow
     */
    public FlowArrivalEvent(double time, Flow flow) {
    	super(time);
        this.flow = flow;
    }
    
    /**
     * Retrives the flow attribute of the FlowArrivalEvent object.
     * 
     * @return the FlowArrivalEvent's flow attribute
     */
    public Flow getFlow() {
        return this.flow;
    }
    
    /**
     * Prints all information related to the arriving flow.
     * 
     * @return string containing all the values of the flow's parameters
     */
    public String toString() {
        return "Arrival: "+flow.toString();
    }
}

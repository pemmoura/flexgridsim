/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package flexgridsim;

/**
 * Returns details about the network traffic: holding time, rate,
 * class of service and weight.
 * 
 * @author andred
 */
public class TrafficInfo {
    
    private double holdingTime;
    private int rate;
    private int cos;
    private int weight;
    
    /**
     * Creates a new TrafficInfo object.
     * 
     * @param holdingTime seconds by which a call will be delayed
     * @param rate transfer rate, measured in Mbps
     * @param cos class of service, to prioritize packets based on application type
     * @param weight cost of the network link
     */
    public TrafficInfo(double holdingTime, int rate, int cos, int weight) {
        this.holdingTime = holdingTime;
        this.rate = rate;
        this.cos = cos;
        this.weight = weight;
    }
    
    /**
     * Retrieves the time, in seconds, by which a call will be delayed.
     * 
     * @return the holdingTime attribute of the TrafficInfo object
     */
    public double getHoldingTime() {
        return holdingTime;
    }
    
    /**
     * Retrieves the transfer rate, in Mbps, of a call.
     * 
     * @return the rate attribute of the TrafficInfo object
     */
    public int getRate() {
        return rate;
    }
    
    /**
     * Retrieves the class of service of the TrafficInfo object.
     * This information is useful for prioritizing traffic based on
     * application type.
     * 
     * @return the cos attribute of the TrafficInfo object
     */
    public int getCOS() {
        return cos;
    }
    
    /**
     * Retrieves the cost of a link on the network.
     * 
     * @return the weight attribute of the TrafficInfo object
     */
    public int getWeight() {
        return weight;
    }

}

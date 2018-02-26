/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexgridsim;

/**
 * The Optical Cross-Connects (OXCs) can switch the optical signal coming
 * in on a wavelenght of an input fiber link to the same wavelength in an
 * output fiber link. The OXC may also switch the optical signal on an
 * incoming wavelength of an input fiber link to some other wavelength on
 * an output fiber link.
 * 
 * The OXC object has grooming input and output ports.
 * Traffic grooming is the process of grouping many small data flows
 * into larger units, so they can be processed as single units.
 * Grooming in OXCs has the objective of minimizing the cost of the network.
 * 
 * @author andred
 */
public class OXC {
	private static int idCount;
    private int id;
    
    /**
     * Creates a new OXC object. 
     * 
     */
    public OXC() {
        this.id = idCount;
        idCount++;
    }
    
    /**
     * Retrieves the OXC's unique identifier.
     * 
     * @return the OXC's id attribute
     */
    public int getID() {
        return id;
    }
    
}

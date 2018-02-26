package flexgridsim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Generates a trace file that contains information about what happened during
 * the simulation, such as arriving and departing flows, created lightpaths,
 * and accepted or blocked flows.
 * 
 * @author andred
 * 
 * Trace Format: [event] [time] [info]
 * 
 * Events:
 * a = flow arrival
 * d = flow departure
 * n = flow accepted into the network
 * b = flow blocked
 * 
 */
public class Tracer {

    private PrintWriter trace;
    private static Tracer singletonObject;
    private boolean writeTrace;

    /**
     * A private Constructor prevents any other class from instantiating.
     */
    private Tracer() {
    	
    	writeTrace = true;
    }
    
    /**
     * Creates a new Tracer object, in case it doesn't exist yet.
     * 
     * @return the Tracer's singletonObject attribute
     */
    public static synchronized Tracer getTracerObject() {
        if (singletonObject == null) {
            singletonObject = new Tracer();
        }
        return singletonObject;
    }
    
    /**
     * Throws an exception to stop a cloned Tracer object from
     * being created.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
    
    /**
     * Prints formatted representations of a Tracer object to a text-output stream.
     * 
     * @param filename name of the file where the output will go
     * @throws IOException exception thrown in case something goes wrong
     */
    public void setTraceFile(String filename) throws IOException {
        trace = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
    }
    
    /**
     * Toggles the writeTrace boolean between true or false.
     * 
     * @param write boolean that will be attributed to writeTrace
     */
    public void toogleTraceWriting(boolean write) {
        writeTrace = write;
    }
    
    /**
     * Adds an object to the trace file. If it is an Event, the addEvent
     * method will deal with it. Otherwise, the object is simply transformed
     * into a String and printed into the trace file.
     * 
     * @param o object to be added
     */
    public void add(Object o) {
        try {
            if (o instanceof String) {
                if (writeTrace) {
                    trace.println((String) o);
                }
            } else if (o instanceof Event) {
                addEvent((Event) o);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * The event of an accepted flow object is added to the trace file.
     * 
     * @param flow the accepted flow
     * @param lightpaths the lightpaths that belong to the flow
     */
    public void acceptFlow(Flow flow, LightPath lightpaths) {
        
    	String str;

        str = "flow-accepted " + "- " + flow.toTrace();
        str += " " + Long.toString(lightpaths.getID());
        
        if (writeTrace) {
            trace.println(str);
        }
    }
    
    /**
     * The event of a blocked flow is added to the trace file.
     * 
     * @param flow the blocked flow
     */
    public void blockFlow(Flow flow) {
        if (writeTrace) {
            trace.println("flow-blocked " + "- " + flow.toTrace());
        }
    }
    
    /**
     * Registers, in the tracer file, that a lightpath was created.
     * 
     * @param lp the LightPath object that was created
     */
    public void createLightpath(LightPath lp) {
        if (writeTrace) {
            trace.println("lightpath-created " + lp.toTrace());
        }
    }
    
    /**
     * Registers, in the tracer file, that a lightpath was removed.
     * 
     * @param lp    LightPath object that was removed
     */
    public void removeLightpath(LightPath lp) {
        if (writeTrace) {
            trace.println("lightpath-removed " + lp.toTrace());
        }
    }
    
    /**
     * Adds an event to the tracer file. To do so, first verifies if it is
     * either FlowArrivalEvent or FlowDepartureEvent.
     * 
     * @param event the Event object to be added
     */
    private void addEvent(Event event)
    {
        try
        {
        	if (event instanceof FlowArrivalEvent)
        	{
                if (writeTrace)
                {
                    trace.println("flow-arrived " + Double.toString(event.getTime()) + " " + ((FlowArrivalEvent) event).getFlow().toTrace());
                }
            }
        	else if (event instanceof FlowDepartureEvent)
        	{
                if (writeTrace)
                {
                    trace.println("flow-departed " + Double.toString(event.getTime()) + " " + Long.toString(((FlowDepartureEvent) event).getID()) + " - - - - -");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Finalizes the tracing actions by attributing the singletonObject to null
     * and closing/flushing the object that generates the output.
     */
    public void finish()
    {
        trace.flush();
        trace.close();
        singletonObject = null;
    }
}

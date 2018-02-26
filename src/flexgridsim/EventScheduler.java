/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexgridsim;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * The simulator's timeline is not defined by time itself, but by
 * events. Therefore, if there are no events, there is nothing to
 * simulate. The EventScheduler takes the given events and sorts
 * them on a queue, based on their "time" attribute. When the simulation
 * begins, the events are pulled out of the queue one by one.
 */
public class EventScheduler {

    private PriorityQueue<Event> eventQueue;
    
    /**
     * This class allows sorting within the EventScheduler.
     * It is the core of the the scheduling process.
     */
    private static class EventSort implements Comparator<Event> {

    	/**
    	 * Compares two events, by their times, to allow the sorting.
    	 * Returns -1 if the first event is sooner, 1 if it is later
    	 * and zero if they are equal.
    	 */
        public int compare(Event o1, Event o2) {
            if (o1.getTime() < o2.getTime()) {
                return -1;
            }
            if (o1.getTime() > o2.getTime()) {
                return 1;
            }
            return 0;
        }
    }
    
    /**
     * Creates a new eventQueue with the initial capacity of 100 elements
     * and uses a new EventSort as the comparator for sorting. 
     */
    public EventScheduler() {
        EventSort eventSort = new EventSort();
        eventQueue = new PriorityQueue<Event>(100, eventSort);
    }
    
    /**
     * Adds a given event to the eventQueue.
     * 
     * @param event will be added to the eventQueue
     * @return boolean true if the eventQueue changed after calling this method;
     * 				   false if duplicates are not permitted and the event is
     * 						 already in the queue
     */
    public boolean addEvent(Event event) {
        return eventQueue.add(event);
    }
    
    /**
     * Retrieves and removes the first event from the eventQueue.
     * 
     * @return the first event of the queue, or null if it has no events
     */
    public Event popEvent() {
        return eventQueue.poll();
    }
}

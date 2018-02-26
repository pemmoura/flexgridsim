package flexgridsim.util;

import java.util.ArrayList;
import java.util.HashMap;

import flexgridsim.FlexGridLink;

/**
 * The Class ScpvsObject.
 * 
 * @author pedrom
 */
public class ScpvsObject {
	HashMap<Integer, HashMap<Integer, ArrayList<FlexGridLink>>> path;
	HashMap<Integer,HashMap<Integer, boolean[]>> availableSpectrum;
	HashMap<Integer,HashMap<Integer, Double>> routingCost;
	HashMap<Integer,HashMap<Integer, Integer>> previous;

	/**
	 * Instantiates a new scpvs object.
	 */
	public ScpvsObject() {
		path = new HashMap<Integer, HashMap<Integer, ArrayList<FlexGridLink>>>();
		availableSpectrum = new HashMap<Integer, HashMap<Integer, boolean[]>>();
		routingCost = new HashMap<Integer, HashMap<Integer, Double>>();
		previous = new HashMap<Integer, HashMap<Integer, Integer>>();
	}

	/**
	 * Sets the available spectrum.
	 * 
	 * @param treeLevel
	 *            the tree level
	 * @param j
	 *            the j
	 * @param spectrum
	 *            the spectrum
	 * @return the hash map
	 */
	public HashMap<Integer, boolean[]> setAvailableSpectrum(int treeLevel,
			int j, boolean[] spectrum) {
		HashMap<Integer, boolean[]> map;
		map = availableSpectrum.get(treeLevel);
		if (map == null) {
			map = new HashMap<Integer, boolean[]>();
			availableSpectrum.put(treeLevel, map);
		}
		map.put(j, spectrum);
		return map;
	}

	/**
	 * Sets the routing cost.
	 * 
	 * @param treeLevel
	 *            the tree level
	 * @param j
	 *            the j
	 * @param cost
	 *            the cost
	 * @return the hash map
	 */
	public HashMap<Integer, Double> setRoutingCost(int treeLevel, int j,
			double cost) {
		HashMap<Integer, Double> map;
		map = routingCost.get(treeLevel);
		if (map == null) {
			map = new HashMap<Integer, Double>();
			routingCost.put(treeLevel, map);
		}
		map.put(j, cost);
		return map;
	}

	/**
	 * Sets the link.
	 * 
	 * @param treeLevel
	 *            the tree level
	 * @param j
	 *            the j
	 * @param link
	 *            the link
	 * @return the hash map
	 */
	public HashMap<Integer, ArrayList<FlexGridLink>> addLinkToPath(int treeLevel, int j,
			FlexGridLink link) {
		HashMap<Integer, ArrayList<FlexGridLink>> map;
		map = path.get(treeLevel);
		if (map == null) {
			map = new HashMap<Integer, ArrayList<FlexGridLink>>();
			path.put(treeLevel, map);
		}
		try{
			map.get(j).add(link);
		} catch(NullPointerException e){
			map.put(j, new ArrayList<FlexGridLink>());
			map.get(j).add(link);
		}
		return map;
	}

	/**
	 * Sets the previous.
	 * 
	 * @param treeLevel
	 *            the tree level
	 * @param j
	 *            the j
	 * @param previous
	 *            the previous
	 * @return the hash map
	 */
	public HashMap<Integer, Integer> setPrevious(int treeLevel, int j,
			int previous) {
		HashMap<Integer, Integer> map;
		map = this.previous.get(treeLevel);
		if (map == null) {
			map = new HashMap<Integer, Integer>();
			this.previous.put(treeLevel, map);
		}
		map.put(j, previous);
		return map;
	}
	
	/**
	 * Gets the routing cost.
	 *
	 * @param treeLevel the tree level
	 * @param j the j
	 * @return the routing cost
	 */
	public double getRoutingCost(int treeLevel, int j){
		if (routingCost.get(treeLevel) == null){
			throw new IllegalArgumentException();
		} else if (routingCost.get(treeLevel).get(j) == null) {
			throw new IllegalArgumentException();
		} else {
			return routingCost.get(treeLevel).get(j);
		}
	}
	
	/**
	 * Gets the path.
	 *
	 * @param treeLevel the tree level
	 * @param j the j
	 * @return the path
	 */
	public ArrayList<FlexGridLink> getPath(int treeLevel, int j){
		if (path.get(treeLevel) == null){
			throw new IllegalArgumentException();
		} else {
			return path.get(treeLevel).get(j);
		}
	}
	
	/**
	 * Gets the path.
	 *
	 * @param treeLevel the tree level
	 * @param j the j
	 */
	public void clearPath(int treeLevel, int j){
		if (path.get(treeLevel) != null && path.get(treeLevel).get(j) != null){
			path.get(treeLevel).get(j).clear();
		}
	}
	
	
	/**
	 * Gets the available spectruim.
	 *
	 * @param treeLevel the tree level
	 * @param j the j
	 * @return the available spectruim
	 */
	public boolean[] getAvailableSpectruim(int treeLevel, int j){
		if (routingCost.get(treeLevel) == null){
			throw new IllegalArgumentException();
		} else {
			return availableSpectrum.get(treeLevel).get(j);
		}
	}
	
	
}

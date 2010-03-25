package buri.ddmsence.samples.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Simple data class representing a distribution of keys.
 * 
 * <p>
 * This class is used in the Escape application to keep track of how often
 * keywords and mimeTypes are used.
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class Distribution {

	private Map<String, Integer> _map = new HashMap<String, Integer>();
	
	/**
	 * Empty constructor
	 */
	public Distribution() {}
	
	/**
	 * Increments the count for a particular key.
	 * 
	 * @param key the key
	 */
	public void incrementCount(String key) {
		Integer count = getCount(key);
		getMap().put(key, new Integer(count + 1));
	}
	
	/**
	 * Accessor for the keys
	 */
	public Set<String> getKeys() {
		return (getMap().keySet());
	}
	
	/**
	 * Accessor for a count
	 */
	public Integer getCount(String key) {
		Integer count = getMap().get(key);
		if (count == null)
			count = new Integer(0);
		return (count);
	}
	
	/**
	 * Accessor for the map
	 */
	private Map<String, Integer> getMap() {
		return (_map);
	}
}

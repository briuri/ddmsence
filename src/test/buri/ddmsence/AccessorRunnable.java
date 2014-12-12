package buri.ddmsence;

/**
 * Runnable implementation which allows multithreaded testing of get and set operations on a variable.
 * 
 * @author Brian Uri!
 * @since 2.5.0
 */
public abstract class AccessorRunnable implements Runnable {
	private String _threadName;
	private boolean _match;
	
	public static final int NUM_THREADS = 1000;
	
	/**
	 * Constructor stores the unique thread name for identification.
	 */
	public AccessorRunnable(String threadName) {
		_threadName = threadName;
	}
	
	/**
	 * Injection point for the expected value
	 */
	public abstract String getExpectedValue();
	
	/**
	 * Injection point for the setter
	 */
	public abstract void callSet();
	
	/**
	 * Injection point for the getter
	 */
	public abstract String callGet();
	
	@Override
	public void run() {
		callSet();
		Thread.yield();
		String storedValue = callGet();
		setMatch(getExpectedValue().equals(storedValue));
	}
	
	/**
	 * Accessor for the thread name
	 */
	public String getThreadName() {
		return (_threadName);
	}
	
	/**
	 * Accessor for whether the value matched its set value after the run.
	 */
	public boolean getMatch() {
		return _match;
	}

	/**
	 * Accessor for whether the value matched its set value after the run.
	 */
	protected void setMatch(boolean match) {
		_match = match;
	}

}

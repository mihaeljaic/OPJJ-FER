package hr.fer.zemris.java.webserver;

/**
 * Dispatcher that gets path analyzes it and determines how to process it.
 * 
 * @author Mihael Jaić
 *
 */

public interface IDispatcher {
	/**
	 * Gets path and
	 * 
	 * @param urlPath
	 *            Path.
	 * @throws Exception
	 *             If something went wrong during dispatching.
	 */
	void dispatchRequest(String urlPath) throws Exception;
}

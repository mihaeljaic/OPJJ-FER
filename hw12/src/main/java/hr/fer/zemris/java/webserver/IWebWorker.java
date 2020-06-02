package hr.fer.zemris.java.webserver;

/**
 * Web worker that processes request.
 * 
 * @author Mihael Jaić
 *
 */

public interface IWebWorker {
	/**
	 * Processes client's request.
	 * 
	 * @param context
	 *            Context
	 * @throws Exception
	 *             If something went wrong during processing client's request.
	 */
	public void processRequest(RequestContext context) throws Exception;
}

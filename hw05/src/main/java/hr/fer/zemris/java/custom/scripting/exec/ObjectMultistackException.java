package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Exception that is thrown if stack in {@link ObjectMultistack} object is
 * empty.
 * 
 * @author Mihael Jaić
 *
 */

public class ObjectMultistackException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Does nothing.
	 */
	
	public ObjectMultistackException() {
		super();
	}
	
	/**
	 * Gets message what went wrong.
	 * 
	 * @param message Message.
	 */
	
	public ObjectMultistackException(String message) {
		super(message);
	}
}

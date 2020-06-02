package hr.fer.zemris.java.hw06.shell;

/**
 * Runtime exception that is thrown if something went wrong while shell was
 * communicating with user.
 * 
 * @author Mihael Jaić
 *
 */

public class ShellIOException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Defaul constructor that does nothing.
	 */

	public ShellIOException() {
		super();
	}

	/**
	 * Gets message about what went wrong.
	 * 
	 * @param message
	 *            Message.
	 */

	public ShellIOException(String message) {
		super(message);
	}
}

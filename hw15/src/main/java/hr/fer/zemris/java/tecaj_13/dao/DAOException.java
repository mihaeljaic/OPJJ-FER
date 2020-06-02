package hr.fer.zemris.java.tecaj_13.dao;

/**
 * Runtime exception that is thrown if error occurred while communicating with
 * database.
 * 
 * @author Mihael Jaić
 *
 */

public class DAOException extends RuntimeException {
	/** Default serial id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that gets message and caught exception.
	 * 
	 * @param message
	 *            Message.
	 * @param cause
	 *            Caught exception.
	 */

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor that gets message.
	 * 
	 * @param message
	 *            Message.
	 */

	public DAOException(String message) {
		super(message);
	}
}
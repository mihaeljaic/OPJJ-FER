package hr.fer.zemris.bf.parser;

/**
 * Runtime exception that is thrown if something went wrong during parsing.
 * 
 * @author Mihael Jaić
 *
 */

public class ParserException extends RuntimeException {

	/**
	 * Default serial version number.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that does nothing.
	 */

	public ParserException() {
		super();
	}

	/**
	 * Gets message about what went wrong during parsing.
	 * 
	 * @param message
	 *            Message.
	 */

	public ParserException(String message) {
		super(message);
	}
}

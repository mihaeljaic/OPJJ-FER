package hr.fer.zemris.java.hw04.db;

/**
 * Exception that is thrown if query parser couldn't parse query string.
 * 
 * @author Mihael Jaić
 *
 */

public class QueryParserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that does nothing.
	 */

	public QueryParserException() {
		super();
	}

	/**
	 * Constructor that sets message.
	 *
	 * @param message
	 *            Message informing about what went wrong during parsing.
	 */

	public QueryParserException(String message) {
		super(message);
	}
}

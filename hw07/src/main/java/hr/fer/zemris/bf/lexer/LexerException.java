package hr.fer.zemris.bf.lexer;

/**
 * Runtime exception that is thrown if something went wrong during lexical
 * analysis by {@link Lexer}.
 * 
 * @author Mihael Jaić
 *
 */

public class LexerException extends RuntimeException {

	/**
	 * Default serial version number.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that does nothing.
	 */

	public LexerException() {
		super();
	}

	/**
	 * Gets message about what went wrong.
	 * 
	 * @param message
	 *            Message.
	 */

	public LexerException(String message) {
		super(message);
	}
}

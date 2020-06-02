package hr.fer.zemris.java.hw03.prob1;

/**
 * Run time exception that is thrown if lexer can't generate new token.
 * 
 * @author Mihael Jaić
 *
 */

public class LexerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that does nothing.
	 */

	public LexerException() {
		super();
	}

	/**
	 * Message which tells what went wrong in creating token.
	 * 
	 * @param message
	 *            Message.
	 */

	public LexerException(String message) {
		super(message);
	}
}

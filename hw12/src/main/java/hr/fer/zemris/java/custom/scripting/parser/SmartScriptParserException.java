package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Runtime exception that is thrown if something went wrong during parsing.
 * 
 * @author Mihael Jaić
 *
 */

public class SmartScriptParserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Message which describes what went wrong during parsing.
	 * 
	 * @param message	Message.
	 */

	public SmartScriptParserException(String message) {
		super(message);
	}
}

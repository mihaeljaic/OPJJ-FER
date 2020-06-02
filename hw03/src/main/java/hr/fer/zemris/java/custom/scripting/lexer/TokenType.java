package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enum that describes what type of information is in token.
 * 
 * @author Mihael Jaić
 *
 */

public enum TokenType {
	/**
	 * Beginning of tag.
	 */
	TAG,
	/**
	 * End of tag.
	 */
	TERMINATE_TAG,
	/**
	 * Variable name.
	 */
	VARIABLE,
	/**
	 * Constant integer value.
	 */
	INTEGER,
	/**
	 * Constant double value.
	 */
	DOUBLE,
	/**
	 * String.
	 */
	STRING,
	/**
	 * Function name.
	 */
	FUNCTION,
	/**
	 * Symbol of operator.
	 */
	OPERATOR,
	/**
	 * End of file.
	 */
	EOF;
}

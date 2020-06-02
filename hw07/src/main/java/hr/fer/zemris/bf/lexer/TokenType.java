package hr.fer.zemris.bf.lexer;

/**
 * Token types which are used in {@link Lexer} class.
 * 
 * @author Mihael Jaić
 *
 */

public enum TokenType {
	/**
	 * End of file.
	 */
	EOF,
	/**
	 * Variable.
	 */
	VARIABLE,
	/**
	 * Constant.
	 */
	CONSTANT,
	/**
	 * Operator.
	 */
	OPERATOR,
	/**
	 * Open bracket.
	 */
	OPEN_BRACKET,
	/**
	 * Closed bracket.
	 */
	CLOSED_BRACKET
}

package hr.fer.zemris.java.hw03.prob1;

/**
 * Lexer states in which lexer works. In basic state word, number and symbol
 * tokens are generated as explained in {@link Lexer} class.In extended state
 * lexer treats everything as text until he comes to '#' character. Which means
 * lexer should change state. Escape symbols are not allowed in this state.
 * 
 * @author Mihael Jaić
 *
 */

public enum LexerState {
	/**
	 * Basic state of lexer
	 */
	BASIC,
	/**
	 * Extended state in lexer.
	 */
	EXTENDED;
}

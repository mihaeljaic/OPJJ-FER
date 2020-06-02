package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enum that represents states of lexer. When lexer is in basic state it
 * generates tokens which are outside of tag and inside_tag state is when lexer
 * is generating token inside of tag. For detailed information about rules for
 * each state see {@link SmartScriptLexer}.
 * 
 * @author Mihael Jaić
 *
 */

public enum LexerState {
	/**
	 * Basic state of lexer. Generates tokens which are outside of tags.
	 */
	BASIC,
	/**
	 * State in which lexer generates tokens which are inside of tags.
	 */
	INSIDE_TAG
}

package hr.fer.zemris.java.custom.scripting.lexer;

import hr.fer.zemris.java.custom.scripting.elems.*;

/**
 * Class that does lexical analysis of given input string by creating tokens
 * which have different meaning to parser. Token types are defined in
 * {@link TokenType} enum. Lexer works in basic and inside_tag state. In basic
 * state everything is interpreted as string outside tags. Tags begin with "{$".
 * Possible escape sequences are only "\{" and "\\". Inside tags first has to be
 * tag name which has to start with letter or "=". Strings have to be in double
 * quotes. Possible escapes for strings are "\"", "\\", "\n, "\r" and "\t" which
 * have standard meaning. Function names need to have "@" before their name.
 * Variable names have to start with letter and contain only letter, digits or
 * underscore.
 * 
 * @author Mihael Jaić
 *
 */

public class SmartScriptLexer {
	/**
	 * Input data that needs to be parsed.
	 */
	private char[] data;
	/**
	 * Last token that was processed.
	 */
	private Token token;
	/**
	 * Current index in input data.
	 */
	private int currentIndex;
	/**
	 * State of lexer.
	 */
	private LexerState state;

	/**
	 * Constructor that gets input text that will be processed.
	 * 
	 * @param text
	 *            Input.
	 * @throws IllegalArgumentException
	 *             If text is null.
	 */

	public SmartScriptLexer(String text) throws IllegalArgumentException {
		if (text == null) {
			throw new IllegalArgumentException("Input can't be null.");
		}

		data = text.toCharArray();
		state = LexerState.BASIC;
	}

	/**
	 * Generates and returns new token.
	 * 
	 * @return Next token in input string.
	 * @throws LexerException
	 *             If end of file is reached. And EOF token was already
	 *             generated.
	 */

	public Token nextToken() throws LexerException {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("Reached end of file.");
		}

		if (state == LexerState.BASIC) {
			generateNewTokenBasicState();
		} else {
			generateNewTokenInsideTagState();
		}

		return token;
	}

	/**
	 * Gets last token that was generated.
	 * 
	 * @return Last token that was generated.
	 * @throws LexerException
	 *             If no tokens were generated yet.
	 */

	public Token getToken() throws LexerException {
		if (token == null) {
			throw new LexerException("No tokens were generated yet.");
		}

		return token;
	}

	/**
	 * Sets lexer's state.
	 * 
	 * @param state
	 *            State of lexer.
	 * @throws IllegalArgumentException
	 *             If state is null;
	 */

	public void setState(LexerState state) throws IllegalArgumentException {
		if (state == null) {
			throw new IllegalArgumentException("State cannot be null.");
		}

		this.state = state;
	}

	/**
	 * Gets current index in data.
	 * 
	 * @return Current index in data.
	 */

	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * Generates new token. In this state only tag and string tokens can be
	 * generated. Tag tokens
	 */

	private void generateNewTokenBasicState() {
		if (currentIndex == data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}

		// Checks if there is new tag ahead.
		if (isTag()) {
			token = new Token(TokenType.TAG, new ElementString(generateTag()));

		} else {
			token = new Token(TokenType.STRING, new ElementString(generateTextOutsideTag()));
		}

	}

	/**
	 * Generates new token inside tag.
	 */

	private void generateNewTokenInsideTagState() {
		skipSpaces();
		if (currentIndex == data.length) {
			throw new LexerException("Reached end of file while inside tag.");
		}

		if (isTagTerminate()) {
			currentIndex += 2;
			token = new Token(TokenType.TERMINATE_TAG, null);
			return;
		}

		if (isValidVariableName()) {
			token = new Token(TokenType.VARIABLE, new ElementVariable(generateName()));

		} else if (isFunction()) {
			currentIndex++;
			token = new Token(TokenType.FUNCTION, new ElementFunction(generateName()));

		} else if (isOperator()) {
			token = new Token(TokenType.OPERATOR, new ElementOperator(Character.toString(data[currentIndex])));
			currentIndex++;

		} else if (isDouble()) {
			token = new Token(TokenType.DOUBLE, new ElementConstantDouble(generateDouble()));

		} else if (isInteger()) {
			token = new Token(TokenType.INTEGER, new ElementConstantInteger(generateInteger()));

		} else {
			// This will try to generate string if it fails exception is thrown.
			token = new Token(TokenType.STRING, new ElementString(generateString()));

		}
	}

	/**
	 * Checks if next 2 characters are tag terminate "$}".
	 * 
	 * @return True if 2 characters match tag terminate, false otherwise.
	 */

	private boolean isTagTerminate() {
		return currentIndex + 1 < data.length && data[currentIndex] == '$' && data[currentIndex + 1] == '}';
	}

	/**
	 * Skips all spaces, tabs and new line symbols.
	 */

	private void skipSpaces() {
		while (currentIndex < data.length && (data[currentIndex] == ' ' || data[currentIndex] == '\n'
				|| data[currentIndex] == '\t' || data[currentIndex] == '\r')) {
			currentIndex++;
		}
	}

	/**
	 * Generates tag name.
	 * 
	 * @return Tag name.
	 */

	private String generateTag() {
		// Ignores start of Tag "{$"
		currentIndex += 2;
		skipSpaces();

		if (currentIndex < data.length && data[currentIndex] == '=') {
			currentIndex++;
			return "=";
		}

		if (!isValidVariableName()) {
			throw new LexerException("Could not generate Tag.");
		}

		// Tags aren't case sensitive.
		String temp = generateName().toUpperCase();
		if (temp.equals("END")) {
			// Removes spaces and end of tag "$}"
			skipSpaces();
			currentIndex += 2;
		}

		return temp;
	}

	/**
	 * Checks if following character is letter so it can represent variable
	 * name.
	 * 
	 * @return True if character is valid variable name. False otherwise.
	 */

	private boolean isValidVariableName() {
		return currentIndex < data.length && Character.isLetter(data[currentIndex]);
	}

	/**
	 * Generates name of variable or tag. It starts with letter and consits of
	 * only letters, digits or underscores.
	 * 
	 * @return Name
	 */

	private String generateName() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < data.length && isLetterDigitOrUnderscore()) {
			sb.append(data[currentIndex]);
			currentIndex++;
		}

		return sb.toString();
	}

	/**
	 * Checks if next character is letter, digit or underscore.
	 * 
	 * @return True if next character is letter, digit or underscore. False
	 *         otherwise.
	 */

	private boolean isLetterDigitOrUnderscore() {
		return Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex])
				|| data[currentIndex] == '_';
	}

	/**
	 * Generates string outside of tag. Until it reaches EOF or tag.
	 * 
	 * @return Text outside of tag.
	 */

	private String generateTextOutsideTag() {
		StringBuilder sb = new StringBuilder();
		boolean isEscaped = false;

		while (currentIndex < data.length && (isEscaped || (!isTag()))) {
			if (data[currentIndex] == '\\' && (!isEscaped)) {
				isEscaped = true;
				currentIndex++;
				continue;

			} else if (isEscaped && !isValidEscapeSymbolOutsideTag()) {
				throw new LexerException("Invalid escape sequence " + Character.toString(data[currentIndex]) + ".");
			}

			isEscaped = false;
			sb.append(data[currentIndex]);
			currentIndex++;
		}

		return sb.toString();
	}

	/**
	 * Checks if characater is valid escape symbol inside tag.
	 * 
	 * @return True if is valid escape symbol inside tag. False otherwise.
	 */

	private boolean isValidEscapeSymbolInsideTag() {
		return data[currentIndex] == '\\' || data[currentIndex] == '"' || data[currentIndex] == 'n'
				|| data[currentIndex] == 't' || data[currentIndex] == 'r';
	}

	/**
	 * Checks if character is valid escape symbol outside of tag.
	 * 
	 * @return True if is character is valid escape symbol. False otherwise.
	 */

	private boolean isValidEscapeSymbolOutsideTag() {
		return data[currentIndex] == '\\' || data[currentIndex] == '{';
	}

	/**
	 * Checks if next 2 characters are "{$" which means start of tag.
	 * 
	 * @return True if tag is ahead, false otherwise.
	 */

	private boolean isTag() {
		return currentIndex + 1 < data.length && data[currentIndex] == '{' && data[currentIndex + 1] == '$';
	}

	/**
	 * Checks if next character is operator. In case next character is '-' it
	 * must be checked if it can be interpreted as number.
	 * 
	 * @return True if next character is operator. False otherwise.
	 */

	private boolean isOperator() {
		return currentIndex < data.length && data[currentIndex] == '+' || data[currentIndex] == '*'
				|| data[currentIndex] == '/' || data[currentIndex] == '^' || (data[currentIndex] == '-'
						&& (currentIndex + 1 >= data.length || (!Character.isDigit(data[currentIndex + 1]))));
	}

	/**
	 * Checks if next token is function. Function starts with "@name" where name
	 * has to start with letter and consist of only letters, digits or
	 * underscores.
	 * 
	 * @return True if next token is function. False otherwise.
	 */

	private boolean isFunction() {
		return currentIndex + 1 < data.length && data[currentIndex] == '@'
				&& Character.isLetter(data[currentIndex + 1]);
	}

	/**
	 * Checks if next token is integer. Integer can start with '-' sign but
	 * can't have decimal point.
	 * 
	 * @return If next token is integer. False otherwise.
	 */

	private boolean isInteger() {
		return Character.isDigit(data[currentIndex]) || (currentIndex + 1 < data.length && data[currentIndex] == '-'
				&& Character.isDigit(data[currentIndex + 1]));
	}

	/**
	 * Checks if next token is double. Double can start with '-' and needs to
	 * have decimal point.
	 * 
	 * @return If next token is double. False otherwise.
	 */

	private boolean isDouble() {
		if (!isInteger()) {
			return false;
		}

		int tempIndex = currentIndex;
		if (data[tempIndex] == '-') {
			tempIndex++;
		}

		while (tempIndex < data.length && (Character.isDigit(data[tempIndex]) || data[tempIndex] == '.')) {
			if (data[tempIndex] == '.') {
				return true;
			}
			tempIndex++;
		}

		return false;
	}

	/**
	 * Generates integer number. If number is too big exception is thrown.
	 * 
	 * @return Integer value of next token.
	 */

	private Integer generateInteger() {
		StringBuilder sb = new StringBuilder();

		if (data[currentIndex] == '-') {
			sb.append('-');
			currentIndex++;
		}

		while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
			sb.append(data[currentIndex]);
			currentIndex++;
		}

		try {
			return Integer.parseInt(sb.toString());

		} catch (NumberFormatException ex) {
			throw new LexerException("Could not convert " + sb.toString() + " to integer.");
		}
	}

	/**
	 * Generates double value of token.
	 * 
	 * @return Double value of token.
	 */

	private Double generateDouble() {
		StringBuilder sb = new StringBuilder();

		if (data[currentIndex] == '-') {
			sb.append('-');
			currentIndex++;
		}

		boolean decimalPoint = false;
		// Except digits there can only appear one dot.
		while (currentIndex < data.length
				&& (Character.isDigit(data[currentIndex]) || (!decimalPoint && data[currentIndex] == '.'))) {
			if (data[currentIndex] == '.') {
				decimalPoint = true;
			}
			sb.append(data[currentIndex]);
			currentIndex++;
		}

		try {
			return Double.parseDouble(sb.toString());

		} catch (NumberFormatException ex) {
			throw new LexerException("Could not convert " + sb.toString() + " to double.");
		}
	}

	/**
	 * Generates string inside tag.
	 * 
	 * @return String inside tag.
	 */

	private String generateString() {
		if (data[currentIndex] != '"') {
			throw new LexerException("Invalid character inside tag. String has to start with \". At " + currentIndex);
		}

		currentIndex++;
		StringBuilder sb = new StringBuilder();

		boolean isEscaped = false;
		while (currentIndex < data.length && (data[currentIndex] != '"' || isEscaped)) {
			if (data[currentIndex] == '\\' && (!isEscaped)) {
				isEscaped = true;
				currentIndex++;
				continue;

			} else if (isEscaped && (!isValidEscapeSymbolInsideTag())) {
				throw new LexerException("Invalid escape sequence " + data[currentIndex] + " inside tag.");

			} else if (isEscaped && isNRT()) {
				sb.append(convertToEscape(data[currentIndex]));
				isEscaped = false;
				currentIndex++;
				continue;

			}

			isEscaped = false;
			sb.append(data[currentIndex]);
			currentIndex++;
		}

		if (sb.length() == 0 || currentIndex >= data.length || data[currentIndex] != '"') {
			throw new LexerException("Invalid character inside tag.");
		}

		currentIndex++;

		return sb.toString();
	}

	/**
	 * Checks if next character is 'n', 'r' or 't'.
	 * 
	 * @return If next character is 'n', 'r' or 't'. False otherwise.
	 */

	private boolean isNRT() {
		return data[currentIndex] == 'n' || data[currentIndex] == 'r' || data[currentIndex] == 't';
	}

	/**
	 * Converts character into escape character. Only 'n', 'r' or 't' characters
	 * are allowed.
	 * 
	 * @param c
	 *            Character that will be transformed into escape.
	 * @return Escape character.
	 */

	private char convertToEscape(char c) {
		if (c == 'n') {
			return '\n';
		} else if (c == 'r') {
			return '\r';
		} else {
			return '\t';
		}
	}

}

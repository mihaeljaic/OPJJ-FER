package hr.fer.zemris.java.hw03.prob1;

/**
 * Lexer that tokenizes input string using following rules. Text contains of set
 * of words, numbers and symbols. Word is every set that has one or more
 * characters that are letters. Number is every set of one or more digits that
 * can be stored in type long. If number doesn't fit in type long exception is
 * thrown. Symbol is every other character that isn't letter, number or space.
 * Lexer works in 2 states see {@link LexerState}. Lexer starts in basic state.
 * 
 * @author Mihael Jaić
 *
 */

public class Lexer {
	/**
	 * Input data.
	 */
	private char[] data;
	/**
	 * Last generated token.
	 */
	private Token token;
	/**
	 * Current index in data array.
	 */
	private int currentIndex;
	/**
	 * Current lexer state.
	 */
	private LexerState state;

	/**
	 * Constructor that gets input text which will be tokenized.
	 * 
	 * @param text
	 *            Input text.
	 * @throws IllegalArgumentException
	 *             If text is null.
	 */

	public Lexer(String text) throws IllegalArgumentException {
		if (text == null) {
			throw new IllegalArgumentException("Text can't be null.");
		}

		data = text.toCharArray();
		state = LexerState.BASIC;
	}

	/**
	 * Generates and returns next token.
	 * 
	 * @return Next token.
	 * @throws LexerException
	 *             If end of file is reached.
	 */

	public Token nextToken() throws LexerException {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("Reached end of file. Can't generate next token.");
		}

		if (state == LexerState.BASIC) {
			generateNewTokenBasicState();
		} else {
			generateNewTokenExtendedState();
		}
		return token;
	}

	/**
	 * Returns last generated token. Calling it multiple times will return same
	 * token. Doesn't generate next token.
	 * 
	 * @return Last generated token.
	 * @throws LexerException
	 *             If no token was generated yet.
	 */

	public Token getToken() throws LexerException {
		if (token == null) {
			throw new LexerException("No token was generated yet.");
		}

		return token;
	}

	/**
	 * Sets lexer state.
	 * 
	 * @param state
	 *            New lexer state.
	 * @throws IllegalArgumentException
	 *             If state is null.
	 */

	public void setState(LexerState state) throws IllegalArgumentException {
		if (state == null) {
			throw new IllegalArgumentException("Lexer state can't be null.");
		}

		this.state = state;
	}

	/**
	 * Generates new token in basic state.
	 */

	private void generateNewTokenBasicState() {
		removeSpaces();

		if (currentIndex == data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}

		if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
			String word = generateWord();
			token = new Token(TokenType.WORD, word);

		} else if (Character.isDigit(data[currentIndex])) {
			try {
				token = new Token(TokenType.NUMBER, Long.parseLong(generateNumber()));
			} catch (NumberFormatException ex) {
				throw new LexerException("Too big number in expression.");
			}

		} else {
			token = new Token(TokenType.SYMBOL, data[currentIndex]);
			currentIndex++;
		}
	}

	/**
	 * Removes spaces until character that isn't space.
	 */

	private void removeSpaces() {
		while (currentIndex < data.length && nextIsSpace()) {
			currentIndex++;
		}
	}

	/**
	 * Checks if next character is empty.
	 * 
	 * @return True if next character is space. False otherwise.
	 */

	private boolean nextIsSpace() {
		return data[currentIndex] == '\n' || data[currentIndex] == '\r' || data[currentIndex] == '\t'
				|| data[currentIndex] == ' ';
	}

	/**
	 * Generates word.
	 * 
	 * @return Word.
	 */

	private String generateWord() {
		StringBuilder sb = new StringBuilder();
		boolean escaped = false;

		while (currentIndex < data.length && (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\'
				|| (escaped && Character.isDigit(data[currentIndex])))) {
			if (escaped && data[currentIndex] != '\\' && (!(Character.isDigit(data[currentIndex])))) {
				throw new LexerException();

			} else if (data[currentIndex] == '\\' && !escaped) {
				escaped = true;
				currentIndex++;
				continue;
			}

			escaped = false;

			sb.append(data[currentIndex]);
			currentIndex++;
		}

		if (sb.length() == 0) {
			throw new LexerException();
		}

		return sb.toString();
	}

	/**
	 * Generates number in string form.
	 * 
	 * @return Number in string format.
	 */

	private String generateNumber() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
			sb.append(data[currentIndex]);
			currentIndex++;
		}

		return sb.toString();
	}

	/**
	 * Generates new token in extended state.
	 */

	private void generateNewTokenExtendedState() {
		removeSpaces();

		if (currentIndex == data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}

		if (data[currentIndex] == '#') {
			token = new Token(TokenType.SYMBOL, '#');
			currentIndex++;
			return;
		}

		StringBuilder sb = new StringBuilder();

		while (currentIndex < data.length && data[currentIndex] != '#' && (!nextIsSpace())) {
			sb.append(data[currentIndex]);
			currentIndex++;
		}

		token = new Token(TokenType.WORD, sb.toString());
	}

}

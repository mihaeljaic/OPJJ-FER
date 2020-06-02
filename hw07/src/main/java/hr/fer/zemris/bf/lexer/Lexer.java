package hr.fer.zemris.bf.lexer;

import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.utils.Constants;

/**
 * Lexer that does lexical analysis of boolean expression. Generates tokens that
 * are used in {@link Parser} for building tree of expression. Boolean
 * expression consists of variables, operators, constants and brackets.
 * Constants can be given by string("true" or "false") or by numbers(1-true or
 * 0-false). Variable names have to start with letter after which there can be
 * zero or more letters, digits or underscores. Operators can be given by string
 * ("or", "xor", "and", "not") or by symbols(+ or operator, * and operator, !
 * not, :+: xor operator). Highest priority in expression evaluation have
 * brackets, after that and operator, xor and finaly or operator with least
 * priority. Variable names, constants that are given in string form and
 * operators that are given in string form have to be separated with space.
 * 
 * @author Mihael Jaić
 *
 */

public class Lexer {
	/**
	 * Last generated token.
	 */
	private Token token;
	/**
	 * Expression.
	 */
	private char[] expression;
	/**
	 * Current index in expression.
	 */
	private int currentIndex;

	/**
	 * String of boolean true constant.
	 */
	private static final String trueString = "true";
	/**
	 * String of boolean false constant.
	 */
	private static final String falseString = "false";

	/**
	 * Gets expression.
	 * 
	 * @param expression
	 *            Expression.
	 * @throws LexerException
	 *             If expression is null.
	 */

	public Lexer(String expression) throws LexerException {
		if (expression == null) {
			throw new LexerException("Expression can't be null.");
		}

		this.expression = expression.toCharArray();
	}

	/**
	 * Generates next token.
	 * 
	 * @return Next token in expression.
	 * @throws LexerException
	 *             If lexer couldn't generate next token.
	 */

	public Token nextToken() throws LexerException {
		if (token != null && token.getTokenType() == TokenType.EOF) {
			throw new LexerException("No more tokens can be generated.");
		}

		generateNextToken();

		return token;
	}

	/**
	 * Generates next token.
	 */

	private void generateNextToken() {
		skipSpaces();

		if (currentIndex == expression.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}

		if (isIdentificatorAhead()) {
			processIdentificator();

		} else if (isNumberAhead()) {
			processNumber();

		} else if (isBracketAhead()) {
			processBracket();

		} else if (isSymbolOperatorAhead()) {
			processOperator();

		} else {
			throw new LexerException("Invalid expression.");
		}
	}

	/**
	 * Generates new operator token.
	 */

	private void processOperator() {
		if (expression[currentIndex] == '*') {
			token = new Token(TokenType.OPERATOR, Constants.AND_STRING);
		} else if (expression[currentIndex] == '+') {
			token = new Token(TokenType.OPERATOR, Constants.OR_STRING);
		} else if (expression[currentIndex] == '!') {
			token = new Token(TokenType.OPERATOR, Constants.NOT_STRING);
		} else {
			token = new Token(TokenType.OPERATOR, Constants.XOR_STRING);
			// xor operator has 2 characters more than any other operator.
			currentIndex += 2;
		}

		currentIndex++;
	}

	/**
	 * Generates new open or closed bracket token.
	 */

	private void processBracket() {
		if (expression[currentIndex] == '(') {
			token = new Token(TokenType.OPEN_BRACKET, expression[currentIndex]);
		} else {
			token = new Token(TokenType.CLOSED_BRACKET, expression[currentIndex]);
		}

		currentIndex++;
	}

	/**
	 * Generates new constant token.
	 */

	private void processNumber() {
		String number = extractNumber();

		if ((!number.equals("1")) && (!number.equals("0"))) {
			throw new LexerException(String.format("Unexpected number: %s.", number));
		}

		token = new Token(TokenType.CONSTANT, number.equals("1"));
	}

	/**
	 * Extracts number from expression
	 * 
	 * @return Number.
	 */

	private String extractNumber() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < expression.length && Character.isDigit(expression[currentIndex])) {
			sb.append(expression[currentIndex]);
			currentIndex++;
		}

		return sb.toString();
	}

	/**
	 * Generates new token based on identificator.
	 */

	private void processIdentificator() {
		String identificator = generateString();

		if (identificator.equals(Constants.AND_STRING)) {
			token = new Token(TokenType.OPERATOR, Constants.AND_STRING);

		} else if (identificator.equals(Constants.XOR_STRING)) {
			token = new Token(TokenType.OPERATOR, Constants.XOR_STRING);

		} else if (identificator.equals(Constants.OR_STRING)) {
			token = new Token(TokenType.OPERATOR, Constants.OR_STRING);

		} else if (identificator.equals(Constants.NOT_STRING)) {
			token = new Token(TokenType.OPERATOR, Constants.NOT_STRING);

		} else if (identificator.equals(trueString)) {
			token = new Token(TokenType.CONSTANT, true);

		} else if (identificator.equals(falseString)) {
			token = new Token(TokenType.CONSTANT, false);

		} else {
			token = new Token(TokenType.VARIABLE, identificator.toUpperCase());
		}
	}

	/**
	 * Extracts identificator string from expression.
	 * 
	 * @return String.
	 */

	private String generateString() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < expression.length
				&& (Character.isLetterOrDigit(expression[currentIndex]) || expression[currentIndex] == '_')) {
			sb.append(expression[currentIndex]);
			currentIndex++;
		}

		return sb.toString().toLowerCase();
	}

	/**
	 * Skips all spaces and tabs.
	 */

	private void skipSpaces() {
		while (currentIndex < expression.length && (expression[currentIndex] == ' ' || expression[currentIndex] == '\t'
				|| expression[currentIndex] == '\n' || expression[currentIndex] == '\r')) {
			currentIndex++;
		}
	}

	/**
	 * Checks if identificator is next in expression.
	 * 
	 * @return True if identificator is next, false otherwise.
	 */

	private boolean isIdentificatorAhead() {
		return Character.isLetter(expression[currentIndex]);
	}

	/**
	 * Checks if numerical sequence if next in expression.
	 * 
	 * @return True if numerical sequence is next in expression, false
	 *         otherwise.
	 */

	private boolean isNumberAhead() {
		return Character.isDigit(expression[currentIndex]);
	}

	/**
	 * Checks if open or closed bracket is next in expression.
	 * 
	 * @return True if open or closed bracket is next, false otherwise.
	 */

	private boolean isBracketAhead() {
		return expression[currentIndex] == '(' || expression[currentIndex] == ')';
	}

	/**
	 * Checks if operator in form of symbol is next in expression.
	 * 
	 * @return True if operator in form of symbol is next in expression.
	 */

	private boolean isSymbolOperatorAhead() {
		return expression[currentIndex] == '*' || expression[currentIndex] == '+' || expression[currentIndex] == '!'
				|| (currentIndex + 2 < expression.length && expression[currentIndex] == ':'
						&& expression[currentIndex + 1] == '+' && expression[currentIndex + 2] == ':');
	}
}

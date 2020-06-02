package hr.fer.zemris.bf.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import hr.fer.zemris.bf.lexer.Lexer;
import hr.fer.zemris.bf.lexer.LexerException;
import hr.fer.zemris.bf.lexer.Token;
import hr.fer.zemris.bf.lexer.TokenType;
import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;
import hr.fer.zemris.bf.utils.Constants;

/**
 * Parser that creates tree of {@link Node}s from given boolean expression. It
 * follows following grammar: <br>
 * S -> E1 <br>
 * E1 -> E2 (OR E2)*<br>
 * E2 -> E3 (XOR E3)* <br>
 * E3 -> E4 (AND E4)* <br>
 * E4 -> NOT E4 | E5 <br>
 * E5 -> VAR | KONST | '(' E1 ')'<br>
 * OR, XOR, AND, NOT all represent each operator. VAR is variable name. KONST is
 * constant value. * is Kleene operator see <a href=
 * "https://en.wikipedia.org/wiki/Kleene_star">https://en.wikipedia.org/wiki/Kleene_star</a>.
 * 
 * @author Mihael Jaić
 *
 */

public class Parser {
	/**
	 * Root node.
	 */
	private Node rootNode;
	/**
	 * Lexer.
	 */
	private Lexer lexer;
	/**
	 * Current token.
	 */
	private Token currentToken;

	/**
	 * Xor operator strategy.
	 */
	private static final BinaryOperator<Boolean> xorOperator;
	/**
	 * And operator strategy.
	 */
	private static final BinaryOperator<Boolean> andOperator;
	/**
	 * Or operator strategy.
	 */
	private static final BinaryOperator<Boolean> orOperator;
	/**
	 * Not operator strategy.
	 */
	private static final UnaryOperator<Boolean> notOperator;

	static {
		xorOperator = (a, b) -> a != b;
		andOperator = (a, b) -> a && b;
		orOperator = (a, b) -> a || b;
		notOperator = a -> !a;
	}

	/**
	 * Parses given expression.
	 * 
	 * @param expression
	 *            Expression.
	 * @throws ParserException
	 *             If something went wrong during parsing.
	 */

	public Parser(String expression) throws ParserException {
		if (expression == null) {
			throw new ParserException("Expression can't be null.");
		}

		lexer = new Lexer(expression);

		parse();
	}

	/**
	 * Gets expression's root node.
	 * 
	 * @return Root node that represents expression.
	 */

	public Node getExpression() {
		return rootNode;
	}

	/**
	 * S production in grammar.
	 */

	private void parse() {
		// Gets E1 nonterminal sign.
		rootNode = parseOr();

		if (!checkType(currentToken, TokenType.EOF)) {
			throw new ParserException("Invalid bracket close.");
		}
	}

	/**
	 * E1 production in grammar.
	 * 
	 * @return Node
	 */

	private Node parseOr() {
		// Gets E2 nonterminal sign.
		Node temp = parseXor();

		if (checkType(currentToken, TokenType.EOF) || (checkType(currentToken, TokenType.CLOSED_BRACKET))) {
			// Returns E2.
			return temp;
		}
		
		if (!(checkType(currentToken, TokenType.OPERATOR) && checkStringValue(currentToken, Constants.OR_STRING))) {
			throw new ParserException(String.format("Unexpected token: %s.", currentToken.toString()));
		}

		List<Node> children = new ArrayList<>();
		children.add(temp);

		while (true) {
			if (checkType(currentToken, TokenType.EOF) || checkType(currentToken, TokenType.CLOSED_BRACKET)) {
				break;
			}

			if (!(checkType(currentToken, TokenType.OPERATOR) && checkStringValue(currentToken, Constants.OR_STRING))) {
				throw new ParserException(String.format("Unexpected token: %s.", currentToken.toString()));
			}

			children.add(parseXor());
		}

		return new BinaryOperatorNode(Constants.OR_STRING, children, orOperator);
	}

	/**
	 * E2 production in grammar.
	 * 
	 * @return Node
	 */

	private Node parseXor() {
		Node temp = parseAnd();

		if (!(checkType(currentToken, TokenType.OPERATOR) && checkStringValue(currentToken, Constants.XOR_STRING))) {
			return temp;
		}

		List<Node> children = new ArrayList<>();
		children.add(temp);

		while (checkType(currentToken, TokenType.OPERATOR) && checkStringValue(currentToken, Constants.XOR_STRING)) {
			children.add(parseAnd());
		}

		return new BinaryOperatorNode(Constants.XOR_STRING, children, xorOperator);
	}

	/**
	 * E3 production in grammar.
	 * 
	 * @return Node
	 */

	private Node parseAnd() {
		Node temp = parseNot();

		try {
			currentToken = lexer.nextToken();
		} catch (LexerException ex) {
			throw new ParserException(String.format("Lexer has thrown exception: %s", ex.getMessage()));
		}

		if (!(checkType(currentToken, TokenType.OPERATOR) && checkStringValue(currentToken, Constants.AND_STRING))) {
			return temp;
		}

		List<Node> children = new ArrayList<>();
		children.add(temp);

		while (checkType(currentToken, TokenType.OPERATOR) && checkStringValue(currentToken, Constants.AND_STRING)) {
			children.add(parseNot());

			try {
				currentToken = lexer.nextToken();
			} catch (LexerException ex) {
				throw new ParserException(String.format("Lexer has thrown exception: %s", ex.getMessage()));
			}
		}

		return new BinaryOperatorNode(Constants.AND_STRING, children, andOperator);
	}

	/**
	 * E4 production in grammar.
	 * 
	 * @return Node
	 */

	private Node parseNot() {
		try {
			currentToken = lexer.nextToken();
		} catch (LexerException ex) {
			throw new ParserException(String.format("Lexer has thrown exception: %s", ex.getMessage()));
		}

		if (checkType(currentToken, TokenType.OPERATOR) && checkStringValue(currentToken, Constants.NOT_STRING)) {
			return new UnaryOperatorNode(Constants.NOT_STRING, parseNot(), notOperator);
		}

		return parseOperand();
	}

	/**
	 * E5 production in grammar.
	 * 
	 * @return Node
	 */

	private Node parseOperand() {
		if (checkType(currentToken, TokenType.VARIABLE)) {
			return new VariableNode((String) currentToken.getTokenValue());
		}

		if (checkType(currentToken, TokenType.CONSTANT)) {
			return new ConstantNode((boolean) currentToken.getTokenValue());
		}

		if (checkType(currentToken, TokenType.OPEN_BRACKET)) {
			Node temp = parseOr();

			if (!checkType(currentToken, TokenType.CLOSED_BRACKET)) {
				throw new ParserException(String.format("Expected ')' but found %s.", currentToken.toString()));
			}

			return temp;
		}

		throw new ParserException(String.format("Unexpected token: %s.", currentToken.toString()));
	}

	/**
	 * Compares types of two tokens.
	 * 
	 * @param token
	 *            Token.
	 * @param type
	 *            Compared token type.
	 * @return True if token type matches, false otherwise.
	 */

	private boolean checkType(Token token, TokenType type) {
		return token.getTokenType() == type;
	}

	/**
	 * Compares string value of token with given string.
	 * 
	 * @param token
	 *            Token.
	 * @param value
	 *            String for comparison.
	 * @return True if string value matches token's value, false otherwise.
	 */

	private boolean checkStringValue(Token token, String value) {
		if (!(token.getTokenValue() instanceof String)) {
			return false;
		}

		return ((String) token.getTokenValue()).equals(value);
	}

}

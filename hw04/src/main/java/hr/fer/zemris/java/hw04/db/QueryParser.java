package hr.fer.zemris.java.hw04.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.lexer.*;

/**
 * Parser that generates list of expressions from query string. Parser perfoms
 * parsing only when method parse() is called. For new query commmand you can
 * set query string and call method parse() again which will generate new list
 * of expressions. Each expression consists of getter of attribute, operator and
 * string literal. String queries have to be in form of attribute name followed
 * by operator which is followed by string literal.
 * 
 * @author Mihael Jaić
 *
 */

public class QueryParser {
	/**
	 * Flag if query is of type directQuery.
	 */
	private boolean isDirectQuery;
	/**
	 * List of conditional expressions.
	 */
	private List<ConditionalExpression> query;
	/**
	 * Lexer
	 */
	private QueryLexer lexer;
	/**
	 * Flag if query string was already parsed.
	 */
	private boolean parsed;

	/**
	 * Gets query string that is delegated to lexer.
	 * 
	 * @param queryString
	 *            Query string.
	 * @throws QueryParserException
	 *             If query string is null.
	 */

	public QueryParser(String queryString) throws QueryParserException {
		super();
		if (queryString == null) {
			throw new QueryParserException("Query command can't be null.");
		}

		query = new ArrayList<>();
		lexer = new QueryLexer(queryString);
	}

	/**
	 * Constructor that sets empty query string.
	 */

	public QueryParser() {
		this("");
	}

	/**
	 * Checks if query had only one expression that contained of attribute name
	 * "jmbag" with operator "=".
	 * 
	 * @return True if query is direct query, false otherwise.
	 */

	public boolean isDirectQuery() {
		return isDirectQuery;
	}

	/**
	 * If query was direct gets string literal of query. If it wasn't direct
	 * throws exception.
	 * 
	 * @return String literal.
	 * @throws IllegalStateException
	 *             If query wasn't direct.
	 */

	public String getQueriedJMBAG() throws IllegalStateException {
		if (!isDirectQuery) {
			throw new IllegalStateException("Query is not direct.");
		}

		return query.get(0).getStringLiteral();
	}

	/**
	 * Gets list of conditional expressions which represent query.
	 * 
	 * @return List of conditional expressions.
	 */

	public List<ConditionalExpression> getQuery() {
		return query;
	}

	/**
	 * Sets new query string that gets delegated to lexer.
	 * 
	 * @param queryString
	 *            New query string.
	 */

	public void setQueryString(String queryString) {
		if (queryString == null) {
			throw new IllegalArgumentException("Query command can't be null.");
		}

		lexer.setData(queryString);
		parsed = false;
		isDirectQuery = false;
		query.clear();
	}

	/**
	 * Parses query string into list of expressions.
	 * 
	 * @throws QueryParserException
	 *             If this query string was already parse. If query string
	 *             doesn't satisfy grammar.
	 */

	public void parse() throws QueryParserException {
		if (parsed) {
			throw new QueryParserException("Input string has already being parsed.");
		}

		QueryTokenType lastType = null;
		IFieldValueGetter lastGetter = null;
		IComparisonOperator lastOperator = null;
		boolean firstComparison = true;

		QueryToken token = lexer.nextToken();

		while (token.getType() != QueryTokenType.EOF) {
			if (!checkValidOrder(token.getType(), lastType)) {
				throw new QueryParserException(
						"Illegal order in query. " + token.getValue() + " can't come after " + lastType + ".");
			}

			if (token.getType() == QueryTokenType.ATTRIBUTE) {
				lastGetter = processAttribute(token);

			} else if (token.getType() == QueryTokenType.OPERATOR) {
				lastOperator = processOperator(token);

			} else if (token.getType() == QueryTokenType.STRING_LITERAL) {
				String stringLiteral = token.getValue();
				query.add(new ConditionalExpression(lastGetter, stringLiteral, lastOperator));

				if (firstComparison && lastGetter == FieldValueGetters.JMBAG
						&& lastOperator == ComparisonOperators.EQUALS) {
					isDirectQuery = true;

				} else {
					isDirectQuery = false;
				}
				firstComparison = false;

			}
			// It is already assumed that we are using only AND logical operator
			// so in case of AND operator token nothing is done.

			lastType = token.getType();
			token = lexer.nextToken();
		}

		if (lastType != QueryTokenType.STRING_LITERAL) {
			throw new QueryParserException("Query must end with string literal.");
		}

		parsed = true;
	}

	/**
	 * Checks if tokens are in valid order.
	 * 
	 * @param current
	 *            Current type.
	 * @param last
	 *            Last token type.
	 * @return True if order of tokens is valid, false otherwise.
	 */

	private boolean checkValidOrder(QueryTokenType current, QueryTokenType last) {
		if (last == null || last == QueryTokenType.AND) {
			return current == QueryTokenType.ATTRIBUTE;
		}
		if (last == QueryTokenType.ATTRIBUTE) {
			return current == QueryTokenType.OPERATOR;
		}
		if (last == QueryTokenType.OPERATOR) {
			return current == QueryTokenType.STRING_LITERAL;
		}
		// After string literal only AND operator can come.
		return current == QueryTokenType.AND;
	}

	/**
	 * Gets field value getter from attribute's name.
	 * 
	 * @param token
	 *            Token.
	 * @return Field value getter.
	 */

	private IFieldValueGetter processAttribute(QueryToken token) {
		if (token.getValue().equals("jmbag")) {
			return FieldValueGetters.JMBAG;

		} else if (token.getValue().equals("firstName")) {
			return FieldValueGetters.FIRST_NAME;

		} else if (token.getValue().equals("lastName")) {
			return FieldValueGetters.LAST_NAME;

		} else {
			throw new QueryParserException("Invalid attribute name " + token.getValue() + ".");
		}
	}

	/**
	 * Gets comparison operator from token's value.
	 * 
	 * @param token
	 *            Token.
	 * @return Comparison operator.
	 */

	private IComparisonOperator processOperator(QueryToken token) {
		switch (token.getValue()) {
		case "<":
			return ComparisonOperators.LESS;
		case "<=":
			return ComparisonOperators.LESS_OR_EQUALS;
		case ">":
			return ComparisonOperators.GREATER;
		case ">=":
			return ComparisonOperators.GREATER_OR_EQUALS;
		case "=":
			return ComparisonOperators.EQUALS;
		case "!=":
			return ComparisonOperators.NOT_EQUALS;
		default:
			return ComparisonOperators.LIKE;
		}

	}

}

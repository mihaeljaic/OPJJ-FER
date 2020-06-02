package hr.fer.zemris.java.hw04.db;

/**
 * Class that represents conditional expression of query in database. It
 * consists of attribute name, operator and string literal.
 * 
 * @author Mihael Jaić
 *
 */

public class ConditionalExpression {
	/**
	 * Getter to attribute name in conditional expression.
	 */
	private IFieldValueGetter fieldGetter;
	/**
	 * String literal.
	 */
	private String stringLiteral;
	/**
	 * Comparison operator.
	 */
	private IComparisonOperator comparisonOperator;

	/**
	 * Constructor that sets all attributes to given values.
	 * 
	 * @param fieldGetter
	 *            Getter of attribute name.
	 * @param stringLiteral
	 *            String literal.
	 * @param comparisonOperator
	 *            Comparison operator.
	 */

	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
			IComparisonOperator comparisonOperator) {
		super();
		if (fieldGetter == null || stringLiteral == null || comparisonOperator == null) {
			throw new IllegalArgumentException("null values are not allowed.");
		}

		this.fieldGetter = fieldGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}

	/**
	 * Gets fieldGetter.
	 * 
	 * @return Field getter.
	 */

	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Gets string literal.
	 * 
	 * @return String literal.
	 */

	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Gets comparison operator.
	 * 
	 * @return Comparison operator.
	 */

	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}

}

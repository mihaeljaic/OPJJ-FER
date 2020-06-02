package hr.fer.zemris.java.hw04.db;

import java.util.List;

/**
 * Implementation of {@link IFilter} strategy. Checks if student satisfies all
 * given expressions.
 * 
 * @author Mihael Jaić
 *
 */

public class QueryFilter implements IFilter {
	/**
	 * List of conditional expressions.
	 */
	private List<ConditionalExpression> conditionalExpressions;

	/**
	 * Constructor that gets list of conditional expressions.
	 * 
	 * @param conditionalExpressions
	 *            Conditional expressions.
	 * @throws IllegalArgumentException
	 *             If conditionalExpressions is null
	 */

	public QueryFilter(List<ConditionalExpression> conditionalExpressions) throws IllegalArgumentException {
		super();
		if (conditionalExpressions == null) {
			throw new IllegalArgumentException("Argument with value null is not allowed.");
		}

		this.conditionalExpressions = conditionalExpressions;
	}

	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression expr : conditionalExpressions) {
			if (!expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record), expr.getStringLiteral())) {
				return false;
			}
		}

		return true;
	}

}

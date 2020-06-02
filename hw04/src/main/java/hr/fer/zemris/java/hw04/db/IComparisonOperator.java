package hr.fer.zemris.java.hw04.db;

/**
 * Strategy that represents operators for 2 strings. Checks if 2 strings satisfy
 * operation.
 * 
 * @author Mihael Jaić
 *
 */

public interface IComparisonOperator {
	/**
	 * Checks if relation between values of 2 strings satisfies some operation.
	 * 
	 * @param value1
	 *            First string.
	 * @param value2
	 *            Second string.
	 * @return True if relation is satisfied, false otherwise.
	 */
	public boolean satisfied(String value1, String value2);
}

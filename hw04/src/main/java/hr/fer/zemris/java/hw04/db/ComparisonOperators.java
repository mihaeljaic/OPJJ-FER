package hr.fer.zemris.java.hw04.db;

/**
 * Class that contains strategy implementations of {@link IComparisonOperator}.
 * 
 * @author Mihael Jaić
 *
 */

public class ComparisonOperators {
	/**
	 * Operator that returns true if first string has lower value than second,
	 * false otherwise.
	 */
	public static final IComparisonOperator LESS;
	/**
	 * Operator that returns true if first string has lower or equal value than
	 * second, false otherwise.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS;
	/**
	 * Operator that returns true if first string has greater value than second,
	 * false otherwise.
	 */
	public static final IComparisonOperator GREATER;
	/**
	 * Operator that returns true if first string has greater or equal value
	 * than second, false otherwise.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS;
	/**
	 * Operator that returns true if strings are equal, false otherwise.
	 */
	public static final IComparisonOperator EQUALS;
	/**
	 * Operator that returns true if strings are different, false otherwise.
	 */
	public static final IComparisonOperator NOT_EQUALS;
	/**
	 * Operator that checks if there is pattern in given string. Pattern can
	 * contain single or none '*' characters. Character '*' means that any
	 * characters can be in that place including case when no characters come in
	 * that place. Example pattern "AA*AA" means that string has to start with
	 * "AA" than can come any amount of characters but string has to end with
	 * "AA" after that.
	 */
	public static final IComparisonOperator LIKE;

	static {
		LESS = (value1, value2) -> value1.compareTo(value2) < 0;
		LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;
		GREATER = (value1, value2) -> value1.compareTo(value2) > 0;
		GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;
		EQUALS = (value1, value2) -> value1.equals(value2);
		NOT_EQUALS = (value1, value2) -> !value1.equals(value2);

		LIKE = (value, pattern) -> {
			// If there are more '*' this will throw exception.
			int index = indexOfStar(pattern);
			// No '*' in LIKE operator.
			if (index == -1) {
				return value.equals(pattern);

				// '*' is only character so all strings satisfy.
			} else if (pattern.length() == 1) {
				return true;

			} else if (index == 0) {
				return starAtFirstPlace(value, pattern);

			} else if (index == pattern.length() - 1) {
				return starAtLastPlace(value, pattern);

			} else {
				return starAtMiddle(value, pattern, index);
			}
		};
	}

	/**
	 * If '*' is at first place in pattern checks if given string satisfies
	 * pattern.
	 * 
	 * @param value
	 *            String that is checked if it satisfies pattern.
	 * @param pattern
	 *            Pattern.
	 * @return True if string satisfies pattern, false otherwise.
	 */

	private static boolean starAtFirstPlace(String value, String pattern) {
		String subString = pattern.substring(1);

		return value.endsWith(subString);
	}

	/**
	 * Checks if given string satisfies pattern if '*' is at last place.
	 * 
	 * @param value
	 *            String that is checked if it satisfies pattern.
	 * @param pattern
	 *            Pattern.
	 * @return True if string satisfies pattern, false otherwise.
	 */

	private static boolean starAtLastPlace(String value, String pattern) {
		String subString = pattern.substring(0, pattern.length() - 1);

		return value.startsWith(subString);
	}

	/**
	 * Checks if given string satisfies pattern if '*' is in middle of pattern.
	 * 
	 * @param value
	 *            String that is checked if it satisfies pattern.
	 * @param pattern
	 *            Pattern.
	 * @param index
	 *            Index of '*' in pattern.
	 * @return True if string satisfies pattern, false otherwise.
	 */

	private static boolean starAtMiddle(String value, String pattern, int index) {
		String subString1 = pattern.substring(0, index);
		String subString2 = pattern.substring(index + 1);

		if (value.length() >= subString1.length() + subString2.length()) {
			return value.startsWith(subString1) && value.endsWith(subString2);
		}

		return false;
	}

	/**
	 * Returns index of '*' character in pattern. If there is no '*' in pattern
	 * -1 is returned.
	 * 
	 * @param s
	 *            Pattern
	 * @return Index of '*' character.
	 * @throws IllegalArgumentException
	 *             If there are more '*' characters in pattern.
	 */

	private static int indexOfStar(String s) throws IllegalArgumentException {
		int index = s.indexOf('*');
		if (index != -1 && index + 1 < s.length() && s.indexOf('*', index + 1) != -1) {
			throw new IllegalArgumentException("There are 2 or more stars in LIKE operator.");

		}

		return index;
	}
}

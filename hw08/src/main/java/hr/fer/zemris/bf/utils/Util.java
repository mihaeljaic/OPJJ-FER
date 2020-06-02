package hr.fer.zemris.bf.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import hr.fer.zemris.bf.model.Node;

/**
 * Util class that offers some methods for boolean expressions.
 * 
 * @author Mihael Jaić
 *
 */

public class Util {
	/**
	 * Comparator for boolean array. Works only for arrays of same size!
	 */
	private static final Comparator<boolean[]> booleanArrayComparator;

	static {
		booleanArrayComparator = (values1, values2) -> {
			for (int i = 0; i < values1.length; i++) {
				if (values1[i] != values2[i]) {
					// If values1 is true means it has higher value than values2
					// so 1 is returned, otherwise -1 is returned.
					return values1[i] ? 1 : -1;
				}
			}

			return 0;
		};
	}

	/**
	 * Number of bits in integer type.
	 */
	private static final int intBitSize = 32;

	/**
	 * Consumes each combination of truth table that is defined by amount of
	 * variables given. If there is n variables there will be 2^n combinations
	 * in truth table. Combinations are unique and sorted.
	 * 
	 * @param variables
	 *            Variables.
	 * @param consumer
	 *            Consumer.
	 */

	public static void forEach(List<String> variables, Consumer<boolean[]> consumer) {
		int n = variables.size();
		boolean[] values = new boolean[n];

		for (int i = 1; i <= (1 << n); i++) {
			consumer.accept(values);
			for (int j = 1; j <= n; j++) {
				// Checks if j-th variable(from left to right) needs to change
				// its value.
				if ((i & ((1 << (n - j)) - 1)) == 0) {
					// Changes value of j-th variable.
					values[j - 1] ^= true;
				}
			}
		}
	}

	/**
	 * Builds set of boolean values of variables in truth table that give
	 * expressionValue from given expression.
	 * 
	 * @param variables
	 *            Variables.
	 * @param expression
	 *            Expression.
	 * @param expressionValue
	 *            Expression value.
	 * @return Set of boolean values of variables in truth table that give
	 *         expressionValue from given expression.
	 */

	public static Set<boolean[]> filterAssignments(List<String> variables, Node expression, boolean expressionValue) {
		ExpressionEvaluator evaluator = new ExpressionEvaluator(variables);

		Set<boolean[]> satisfiedValues = new TreeSet<>(booleanArrayComparator);

		Util.forEach(variables, values -> {
			evaluator.setValues(values);
			expression.accept(evaluator);

			if (expressionValue == evaluator.getResult()) {
				boolean[] satisfied = new boolean[values.length];
				for (int i = 0; i < satisfied.length; i++) {
					satisfied[i] = values[i];
				}
				satisfiedValues.add(satisfied);
			}
		});

		return satisfiedValues;
	}

	/**
	 * Calculates int value of boolean array. Value is calculated by summing all
	 * values that are true. If some i-th value is true then it's int value is
	 * 2^(n-i) where n is size of array if, value is false then it's int value
	 * is 0.
	 * 
	 * @param values
	 *            Values.
	 * @return Integer value of boolean array.
	 */

	public static int booleanArrayToInt(boolean[] values) {
		int result = 0;

		for (int i = 1; i <= values.length; i++) {
			if (values[i - 1]) {
				result += (1 << (values.length - i));
			}
		}

		return result;
	}

	/**
	 * Returns set of integers that represent sum of minterms of given
	 * expression and list of variables.
	 * 
	 * @param variables
	 *            List of variables.
	 * @param expression
	 *            Expression.
	 * @return Sum of minterms of given expression and list of variables.
	 */

	public static Set<Integer> toSumOfMinterms(List<String> variables, Node expression) {
		return generateMinOrMaxTerm(variables, expression, true);
	}

	/**
	 * Returns set of integers that represent product of maxterms of given
	 * expression and list of variables.
	 * 
	 * @param variables
	 *            List of variables.
	 * @param expression
	 *            Expression.
	 * @return Set of integers that represent product of maxterms of given
	 *         expression and list of variables.
	 */

	public static Set<Integer> toProductOfMaxterms(List<String> variables, Node expression) {
		return generateMinOrMaxTerm(variables, expression, false);
	}

	/**
	 * Returns set of integers that represent either product of maxterms or sum
	 * of minterms of given expression and list of variables. Determined by
	 * expressionValue variable.
	 * 
	 * @param variables
	 *            List of variables.
	 * @param expression
	 *            Expression.
	 * @param expressionValue
	 *            True for sum of minterms false for product of maxterms.
	 * @return Set of integers that represent either product of maxterms or sum
	 *         of minterms of given expression and list of variables. Determined
	 *         by expressionValue variable.
	 */

	private static Set<Integer> generateMinOrMaxTerm(List<String> variables, Node expression, boolean expressionValue) {
		Set<Integer> result = new TreeSet<>();

		for (boolean[] value : filterAssignments(variables, expression, expressionValue)) {
			result.add(booleanArrayToInt(value));
		}

		return result;
	}

	/**
	 * Converts given number into bit mask of size n in form of byte array.
	 * Where value 1 in array represents that bit is set to 1 at that position,
	 * value 0 means it's set to 0.
	 * 
	 * @param x
	 *            Number.
	 * @param n
	 *            Size of bit mask.
	 * @return Bit mask of number in form of byte array.
	 * @throws IllegalArgumentException
	 *             If size of bit mask is not in interval of[1, 32].
	 */

	public static byte[] indexToByteArray(int x, int n) throws IllegalArgumentException {
		// More than 32 bits cannot be combined with int value.
		if (n < 1 || n > intBitSize) {
			throw new IllegalArgumentException("n has to be in range of [1, 32].");
		}

		byte[] result = new byte[n];

		for (int i = 1; i <= n; i++) {
			// If bit in x at position i is 1, then 1 is returned, otherwise 0.
			result[i - 1] = (byte) ((x & (1 << n - i)) != 0 ? 1 : 0);
		}

		return result;
	}

}

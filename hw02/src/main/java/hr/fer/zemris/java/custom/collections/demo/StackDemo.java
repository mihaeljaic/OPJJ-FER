package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Program that demonstrates functionality of ObjectStack class. It calculates
 * the given expression in postfix notation. Receives one argument from command
 * line. Argument has to be in postfix notation and has to be enclosed in
 * quotation marks.
 * 
 * Example: "8 -2 / -1 *". Supported operations are: +, -, *, / and %.
 * 
 * @author Mihael Jaić
 *
 */

public class StackDemo {

	/**
	 * Method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		if (args.length != 1) {
			error("Invalid number of arguments.");
		}

		String[] expression = args[0].split("\\s+");
		if (expression == null || expression.length == 0) {
			error("Invalid expression.");
		}

		ObjectStack stack = new ObjectStack();

		System.out.printf("%s = %d%n", args[0], calculateExpression(expression, stack));

	}

	/**
	 * Calculates given expression. Expression has to be in postfix notation. If
	 * expression is invalid or there is division by zero program is terminated
	 * with appropriate message. Otherwise returns the result.
	 * 
	 * @param expression
	 *            Postfix expression that needs to be calculated
	 * @param stack
	 *            Reference to stack.
	 * @return Result of given expression.
	 */

	private static int calculateExpression(String[] expression, ObjectStack stack) {
		for (String element : expression) {
			if (isInteger(element)) {
				stack.push(Integer.parseInt(element));
				continue;
			}
			try {
				int secondOperand = (Integer) stack.pop();
				int firstOperand = (Integer) stack.pop();

				if ((element.equals("/") || element.equals("%")) && secondOperand == 0) {
					error("Division by zero.");
				}

				int tempResult = 0;
				switch (element) {
				case "+":
					tempResult = firstOperand + secondOperand;
					break;
				case "-":
					tempResult = firstOperand - secondOperand;
					break;
				case "*":
					tempResult = firstOperand * secondOperand;
					break;
				case "/":
					tempResult = firstOperand / secondOperand;
					break;
				case "%":
					tempResult = firstOperand % secondOperand;
					break;
				default:
					error("Invalid expression");
				}

				stack.push(tempResult);

			} catch (EmptyStackException ex) {
				error("Invalid expression.");
			}

		}
		if (stack.size() != 1) {
			error("Invalid expression.");
		}

		return (Integer) stack.pop();
	}

	/**
	 * Method that checks if given string can be interpreted as integer.
	 * 
	 * @param string
	 *            Expression.
	 * @return True if string represents integer, false otherwise.
	 */

	private static boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;

		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * Method which prints out the error message and ends the program.
	 * 
	 * @param message
	 *            Message to be displayed on standard output.
	 */

	private static void error(String message) {
		System.err.printf("%s%n", message);
		System.exit(1);
	}

}

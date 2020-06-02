package demo;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.parser.ParserException;
import hr.fer.zemris.bf.qmc.Minimizer;
import hr.fer.zemris.bf.utils.Util;
import hr.fer.zemris.bf.utils.VariablesGetter;

/**
 * Program that accepts boolean expressions and writes out their minimal form
 * using Quine-McCkluskey algorithm. Input has to be in following form: First
 * comes function name with variable names inside brackets after what comes '='
 * sign and than follows expression for minterms. After that optionally can come
 * expression for don't cares but it has to be separated with '|'. Minterms or
 * don't cares can be given in 2 ways: by regular boolean expression or writing
 * indexes inside '[' brackets. For example f(A,B,C,D) = NOT A AND NOT B AND
 * (NOT C OR D) OR A AND C | [4,6]. To see logs run program with following VM
 * arguments: "-Djava.util.logging.config.file=./logging.properties". Type
 * "quit" to exit program.
 * 
 * @author Mihael Jaić
 *
 */

public class QMC {

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.print("> ");
			String line = sc.nextLine();
			if (line.trim().toLowerCase().equals("quit")) {
				break;
			}

			if (!validFunction(line)) {
				System.out.println("Error: invalid function expression.");
				continue;
			}

			String[] equals = line.split("=");
			String[] forParsing = equals[1].split("\\|");

			List<String> variables = getVariables(equals[0]);
			if (duplicatedVariableNames(variables)) {
				System.out.println("Error: duplicate variable names.");
				continue;
			}

			Set<Integer> minterms = null;
			try {
				minterms = parseExpression(forParsing[0], variables);
			} catch (ParserException ex) {
				System.out.println("Error: invalid function expression.");
				continue;
			} catch (IllegalArgumentException ex) {
				System.out.println(String.format("Error: %s", ex.getMessage()));
				continue;
			}

			Set<Integer> dontCares = new TreeSet<>();

			if (forParsing.length == 2) {
				try {
					dontCares = parseExpression(forParsing[1], variables);
				} catch (ParserException ex) {
					System.out.println("Error: invalid function expression.");
					continue;
				} catch (IllegalArgumentException ex) {
					System.out.println(String.format("Error: %s", ex.getMessage()));
					continue;
				}
			}

			Minimizer minimizer = null;
			try {
				minimizer = new Minimizer(minterms, dontCares, variables);
			} catch (IllegalArgumentException ex) {
				System.out.println(String.format("Error: %s", ex.getMessage()));
				continue;
			}

			List<String> minimalForms = minimizer.getMinimalFormsAsString();

			for (int i = 0; i < minimalForms.size(); i++) {
				System.out.println(String.format("%d. %s", i + 1, minimalForms.get(i)));
			}
		}

		sc.close();
	}

	/**
	 * Checks if there are duplicates in variable names list.
	 * 
	 * @param variables
	 *            Variables.
	 * @return True if there are duplicates in variable names list, false
	 *         otherwise.
	 */

	private static boolean duplicatedVariableNames(List<String> variables) {
		for (int i = 0; i < variables.size(); i++) {
			for (int j = i + 1; j < variables.size(); j++) {
				if (variables.get(i).equals(variables.get(j))) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Gets sum of minterms from given expression and variables.
	 * 
	 * @param expression
	 *            Expression.
	 * @param variables
	 *            Variables.
	 * @return Sum of minterms.
	 */

	private static Set<Integer> parseExpression(String expression, List<String> variables) {
		if (expression.trim().startsWith("[")) {
			return parseWithoutParser(expression, variables);
		}

		return parseWithParser(expression, variables);
	}

	/**
	 * Gets sum of minterms by using parser.
	 * 
	 * @param expression
	 *            Expression.
	 * @param variables
	 *            Variables.
	 * @return Sum of minterms.
	 */

	private static Set<Integer> parseWithParser(String expression, List<String> variables) {
		Parser parser = new Parser(expression);
		Node rootNode = parser.getExpression();

		VariablesGetter varGetter = new VariablesGetter();
		rootNode.accept(varGetter);
		List<String> realVariables = varGetter.getVariables();

		for (String name : realVariables) {
			if (!variables.contains(name)) {
				throw new IllegalArgumentException("variables name mismatch.");
			}
		}

		return Util.toSumOfMinterms(variables, rootNode);
	}

	/**
	 * Gets sum of minterms from given expression inside '['and ']' and
	 * variables.
	 * 
	 * @param expression
	 *            Expression.
	 * @param variables
	 *            Variables.
	 * @return Sum of minterms.
	 */

	private static Set<Integer> parseWithoutParser(String expression, List<String> variables) {
		Set<Integer> indexes = new TreeSet<>();

		StringBuilder number = new StringBuilder();
		for (int i = 0; i < expression.length(); i++) {
			if (Character.isDigit(expression.charAt(i))) {
				number.setLength(0);
				number.append(expression.charAt(i++));
				while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
					number.append(expression.charAt(i));
					i++;
				}
				indexes.add(Integer.parseInt(number.toString()));
			}
		}

		return indexes;
	}

	/**
	 * Gets variables from function parameters.
	 * 
	 * @param string
	 *            Function parameters
	 * @return Variables from function parameters.
	 */

	private static List<String> getVariables(String string) {
		string = string.substring(string.indexOf("(") + 1, string.indexOf(")")).toUpperCase().trim();

		return Arrays.asList(string.split("\\s*,\\s*"));
	}

	/**
	 * Checks if function is in right form.
	 * 
	 * @param expression
	 *            Input.
	 * @return True if function is in right form, false otherwise.
	 */

	private static boolean validFunction(String expression) {
		String[] temp = expression.split("=");
		if (temp.length != 2) {
			return false;
		}

		// Used for checking if function name and variable names are valid.
		Pattern pattern = Pattern.compile(
				"^(\\s*[A-Z][A-Z0-9]*\\s*\\(\\s*[A-Z][A-Z0-9]*\\s*(\\s*,\\s*[A-Z][A-Z0-9]*\\s*)*\\)\\s*)$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(temp[0]);
		if (!matcher.matches()) {
			return false;
		}

		String[] secondPart = temp[1].split("\\|");
		if (secondPart.length > 2) {
			return false;
		}

		// Used for checking if expression as indexes is valid.
		Pattern numberPattern = Pattern.compile("^(\\s*\\[\\s*[0-9]+(\\s*,\\s*[0-9]+\\s*)*\\s*\\]\\s*)$");

		for (String s : secondPart) {
			if (s.trim().startsWith("[")) {
				matcher = numberPattern.matcher(s);
				if (!matcher.matches()) {
					return false;
				}
			}
		}

		return true;
	}

}

package hr.fer.zemris.bf.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Evaluates boolean expression by visiting all nodes in parser tree that were
 * parsed from some expression.
 * 
 * @author Mihael Jaić
 *
 */

public class ExpressionEvaluator implements NodeVisitor {
	/**
	 * Array of values for variables.
	 */
	private boolean[] values;
	/**
	 * Contains mapped variable names with their position in "truth" table.
	 */
	private Map<String, Integer> positions;
	/**
	 * Stack used for calculating of expression.
	 */
	private Stack<Boolean> stack = new Stack<>();

	/**
	 * Gets variables that will be used in calculating expression.
	 * 
	 * @param variables
	 *            List of variables.
	 */

	public ExpressionEvaluator(List<String> variables) {
		positions = new HashMap<>();
		values = new boolean[variables.size()];

		for (int i = 0; i < variables.size(); i++) {
			positions.put(variables.get(i), i);
		}
	}

	/**
	 * Sets values for each variable.
	 * 
	 * @param values
	 *            Array of values that represent values for each variable.
	 */

	public void setValues(boolean[] values) {
		if (values.length != this.values.length) {
			throw new IllegalArgumentException(
					String.format("Array size mismatch. Expected array of size %d.", this.values.length));
		}

		for (int i = 0; i < values.length; i++) {
			this.values[i] = values[i];
		}
		start();
	}

	@Override
	public void visit(ConstantNode node) {
		stack.push(node.getValue());
	}

	@Override
	public void visit(VariableNode node) {
		Integer variableIndex = positions.get(node.getName());
		if (variableIndex == null) {
			throw new IllegalStateException(String.format("Unknown variable name %s.", node.getName()));
		}

		stack.push(values[variableIndex]);
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		node.getChild().accept(this);

		stack.push(node.getOperator().apply(stack.pop()));
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		for (Node child : node.getChildren()) {
			child.accept(this);
		}

		boolean tempResult = stack.pop();
		for (int i = 0; i < node.getChildren().size() - 1; i++) {
			tempResult = node.getOperator().apply(tempResult, stack.pop());
		}
		stack.push(tempResult);
	}

	/**
	 * Clears stack.
	 */

	public void start() {
		stack.clear();
	}

	/**
	 * Returns final result of expression.
	 * 
	 * @return Result of expression.
	 * @throws IllegalStateException
	 *             If something went wrong while calculating expression.
	 */

	public boolean getResult() throws IllegalStateException {
		if (stack.size() != 1) {
			throw new IllegalStateException("Didn't calculate result. Something went wrong.");
		}

		return stack.peek();
	}

}

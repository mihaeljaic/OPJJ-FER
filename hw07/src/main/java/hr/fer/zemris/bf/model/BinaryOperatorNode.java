package hr.fer.zemris.bf.model;

import java.util.List;
import java.util.function.BinaryOperator;

/**
 * Node that represents binary operation between two or more nodes.
 * 
 * @author Mihael Jaić
 *
 */

public class BinaryOperatorNode implements Node {
	/**
	 * Operator name.
	 */
	private String name;
	/**
	 * List of children of this node.
	 */
	private List<Node> children;
	/**
	 * Operator strategy.
	 */
	private BinaryOperator<Boolean> operator;

	/**
	 * Sets all attributes of node.
	 * 
	 * @param name
	 *            Operator name.
	 * @param children
	 *            List of children of this node.
	 * @param operator
	 *            Operator strategy.
	 * @throws IllegalArgumentException
	 *             If any of arguments is null or number of children is less
	 *             than two.
	 */

	public BinaryOperatorNode(String name, List<Node> children, BinaryOperator<Boolean> operator)
			throws IllegalArgumentException {
		if (name == null || children == null || operator == null) {
			throw new IllegalArgumentException("null values are not allowed.");
		}

		if (children.size() < 2) {
			throw new IllegalArgumentException("Binary operator needs to have at least 2 children.");
		}

		this.name = name;
		this.children = children;
		this.operator = operator;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Gets operator name.
	 * 
	 * @return Operator name.
	 */

	public String getName() {
		return name;
	}

	/**
	 * Gets list of children of this node.
	 * 
	 * @return Children.
	 */

	public List<Node> getChildren() {
		return children;
	}

	/**
	 * Gets operator strategy.
	 * 
	 * @return Operator strategy.
	 */

	public BinaryOperator<Boolean> getOperator() {
		return operator;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BinaryOperatorNode)) {
			return false;
		}

		BinaryOperatorNode other = (BinaryOperatorNode) obj;

		if (!(name.equals(other.getName()) && children.size() == other.getChildren().size())) {
			return false;
		}

		for (int i = 0; i < children.size(); i++) {
			if (!children.get(i).equals(other.getChildren().get(i))) {
				return false;
			}
		}

		return true;
	}
}

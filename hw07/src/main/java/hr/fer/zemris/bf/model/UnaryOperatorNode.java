package hr.fer.zemris.bf.model;

import java.util.function.UnaryOperator;

/**
 * Node that represents unary operation on one node.
 * 
 * @author Mihael Jaić
 *
 */

public class UnaryOperatorNode implements Node {
	/**
	 * Unary operator name.
	 */
	private String name;
	/**
	 * Reference to child.
	 */
	private Node child;
	/**
	 * Strategy of unary operator.
	 */
	private UnaryOperator<Boolean> operator;

	/**
	 * Sets all attributes of node.
	 * 
	 * @param name
	 *            Unary operator name.
	 * @param child
	 *            Reference to child.
	 * @param operator
	 *            Strategy of unary operator.
	 * @throws IllegalArgumentException
	 *             If any of arguments is null.
	 */

	public UnaryOperatorNode(String name, Node child, UnaryOperator<Boolean> operator) throws IllegalArgumentException {
		if (name == null || child == null || operator == null) {
			throw new IllegalArgumentException("null values are not allowed.");
		}

		this.name = name;
		this.child = child;
		this.operator = operator;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Gets name.
	 * 
	 * @return Name.
	 */

	public String getName() {
		return name;
	}

	/**
	 * Gets child.
	 * 
	 * @return Child.
	 */

	public Node getChild() {
		return child;
	}

	/**
	 * Gets operator strategy.
	 * 
	 * @return Operator strategy.
	 */

	public UnaryOperator<Boolean> getOperator() {
		return operator;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UnaryOperatorNode)) {
			return false;
		}

		UnaryOperatorNode other = (UnaryOperatorNode) obj;

		return name.equals(other.getName()) && child.equals(other.getChild());
	}
}

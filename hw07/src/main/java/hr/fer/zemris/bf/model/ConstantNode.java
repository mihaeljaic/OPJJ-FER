package hr.fer.zemris.bf.model;

/**
 * Node that represents boolean value.
 * 
 * @author Mihael Jaić
 *
 */

public class ConstantNode implements Node {
	/**
	 * Boolean value of node.
	 */
	private boolean value;

	/**
	 * Sets node value.
	 * 
	 * @param value
	 *            Value.
	 */

	public ConstantNode(boolean value) {
		this.value = value;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Gets value of node.
	 * 
	 * @return Node value.
	 */

	public boolean getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ConstantNode)) {
			return false;
		}

		ConstantNode other = (ConstantNode) obj;

		return value == other.getValue();
	}
}

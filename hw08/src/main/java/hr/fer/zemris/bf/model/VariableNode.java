package hr.fer.zemris.bf.model;

/**
 * Node that represents variable name.
 * 
 * @author Mihael Jaić
 *
 */

public class VariableNode implements Node {
	/**
	 * Variable name.
	 */
	private String name;

	/**
	 * Sets variable name.
	 * 
	 * @param name
	 *            Variable name.
	 * @throws IllegalArgumentException
	 *             If name is null.
	 */

	public VariableNode(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Name can't be null.");
		}

		this.name = name;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Gets variable name.
	 * 
	 * @return Variable name.
	 */

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VariableNode)) {
			return false;
		}

		VariableNode other = (VariableNode) obj;
		return name.equals(other.getName());
	}
}

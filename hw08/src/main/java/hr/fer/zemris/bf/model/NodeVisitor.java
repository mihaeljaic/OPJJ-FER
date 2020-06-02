package hr.fer.zemris.bf.model;

/**
 * Interface that defines visit methods for each {@link Node}.
 * 
 * @author Mihael Jaić
 *
 */

public interface NodeVisitor {
	/**
	 * Visits {@link ConstantNode}.
	 * 
	 * @param node
	 *            Constant node.
	 */
	void visit(ConstantNode node);

	/**
	 * Visits {@link VariableNode}.
	 * 
	 * @param node
	 *            Variable node.
	 */
	void visit(VariableNode node);

	/**
	 * Visits {@link UnaryOperatorNode}.
	 * 
	 * @param node
	 *            Unary operator node.
	 */
	void visit(UnaryOperatorNode node);

	/**
	 * Visits {@link BinaryOperatorNode}.
	 * 
	 * @param node
	 *            Binary operator node.
	 */
	void visit(BinaryOperatorNode node);
}

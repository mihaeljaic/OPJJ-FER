package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Visits all nodes in parser tree and prints them with indentation.
 * 
 * @author Mihael Jaić
 *
 */

public class ExpressionTreePrinter implements NodeVisitor {
	/**
	 * Used for printing indentation of each node.
	 */
	private int indentation;

	@Override
	public void visit(ConstantNode node) {
		// "1" means true value, "0" is false.
		System.out.printf("%s%s%n", makeSpaces(), node.getValue() ? "1" : "0");
	}

	@Override
	public void visit(VariableNode node) {
		System.out.printf("%s%s%n", makeSpaces(), node.getName());
	}

	// 2 is amount of spaces for each indentation of node.
	@Override
	public void visit(UnaryOperatorNode node) {
		System.out.printf("%s%s%n", makeSpaces(), node.getName());

		indentation += 2;
		node.getChild().accept(this);
		indentation -= 2;
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		System.out.printf("%s%s%n", makeSpaces(), node.getName());

		indentation += 2;
		for (Node child : node.getChildren()) {
			child.accept(this);
		}
		indentation -= 2;
	}

	/**
	 * Used for printing indentation of each node.
	 * 
	 * @return Space that represent indentation of node.
	 */

	private String makeSpaces() {
		StringBuilder sb = new StringBuilder(indentation);

		for (int i = 0; i < indentation; i++) {
			sb.append(" ");
		}

		return sb.toString();
	}

}

package hr.fer.zemris.bf.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Node visitor that extracts all variable names from expression into sorted
 * list with unique nodes.
 * 
 * @author Mihael Jaić
 *
 */

public class VariablesGetter implements NodeVisitor {
	/**
	 * Variables.
	 */
	private List<String> variables;
	/**
	 * Sorted variables.
	 */
	private Set<String> sortedVariables;

	/**
	 * Constructor that prepares for visiting nodes.
	 */

	public VariablesGetter() {
		sortedVariables = new TreeSet<>();
	}

	/**
	 * Gets list of unique sorted variables.
	 * 
	 * @return List of unique sorted variables.
	 */

	public List<String> getVariables() {
		if (variables == null) {
			variables = new ArrayList<>(sortedVariables);
			sortedVariables = null;
		}

		return variables;
	}

	@Override
	public void visit(ConstantNode node) {

	}

	@Override
	public void visit(VariableNode node) {
		sortedVariables.add(node.getName());
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		node.getChild().accept(this);
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		for (Node child : node.getChildren()) {
			child.accept(this);
		}
	}

}

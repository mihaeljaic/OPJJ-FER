package hr.fer.zemris.java.custom.scripting.nodes;

import java.io.IOException;

import hr.fer.zemris.java.custom.scripting.elems.*;

/**
 * Node in parser tree that represents for loop. It can have 3 or 4 arguments.
 * First element has to be variable name and others can be string, variable name
 * or number.
 * 
 * @author Mihael Jaić
 *
 */

public class ForLoopNode extends Node {
	/**
	 * Variable name.
	 */
	private final ElementVariable variable;
	/**
	 * Start expression in for loop.
	 */
	private final Element startExpression;
	/**
	 * End expression in for loop.
	 */
	private final Element endExpression;
	/**
	 * Step expression in for loop.
	 */
	private final Element stepExpression;

	/**
	 * Constructor that takes 4 arguments. Arguments have to be specific type
	 * see {@link ForLoopNode}.
	 * 
	 * @param variable
	 *            Variable name.
	 * @param startExpression
	 *            Start expression.
	 * @param endExpression
	 *            End expression.
	 * @param stepExpression
	 *            Step expression.
	 * @throws IllegalArgumentException
	 *             If arguments are bad.
	 */

	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression)
			throws IllegalArgumentException {
		if (!(validateArguments(variable, startExpression, endExpression, stepExpression))) {
			throw new IllegalArgumentException("Invalid for loop arguments.");
		}

		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * Constructor that takes 3 arguments. Arguments have to be specific type
	 * see {@link ForLoopNode}.
	 * 
	 * @param variable
	 * @param startExpression
	 * @param endExpression
	 * @throws IllegalArgumentException
	 */

	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression)
			throws IllegalArgumentException {
		this(variable, startExpression, endExpression, null);
	}

	/**
	 * Gets end expression.
	 * 
	 * @return End expression.
	 */

	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Gets start expression.
	 * 
	 * @return Start expression.
	 */

	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Gets step expression.
	 * 
	 * @return Step expression.
	 */

	public Element getStepExpression() {
		return stepExpression;
	}

	/**
	 * Gets variable.
	 * 
	 * @return Variable.
	 */

	public ElementVariable getVariable() {
		return variable;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ForLoopNode)) {
			return false;
		}

		ForLoopNode other = (ForLoopNode) obj;

		if (stepExpression == null && other.getStepExpression() != null) {
			return false;
		}

		if (!allElementsEqual(other)) {
			return false;
		}

		if (numberOfChildren() != other.numberOfChildren()) {
			return false;
		}

		for (int i = 0; i < numberOfChildren(); i++) {
			if (!(getChild(i).equals(other.getChild(i)))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Compares all elements from 2 for loops.
	 * 
	 * @param other
	 *            Reference to other ForLoopNode.
	 * @return True if all elements are equal. False otherwise.
	 */

	private boolean allElementsEqual(ForLoopNode other) {
		return startExpression.equals(other.getStartExpression()) && variable.equals(other.getVariable())
				&& endExpression.equals(other.getEndExpression())
				&& (stepExpression == null || stepExpression.equals(other.getStepExpression()));
	}

	/**
	 * Reconstruction of original for loop before parsing.
	 * 
	 * @return Reconstruction of original for loop.
	 */

	@Override
	public String toString() {
		String string = new String(
				variable.asText() + " " + rebuildString(startExpression) + " " + rebuildString(endExpression));
		if (stepExpression != null) {
			string += " " + rebuildString(stepExpression);
		}
		return string;

	}

	/**
	 * Adds escape sequences to string elements and @ for function elements.
	 * 
	 * @param e
	 *            Reference to element.
	 * @return String representation of element.
	 */

	private String rebuildString(Element e) {
		if (e instanceof ElementString) {
			return "\"" + addEscape(e.asText()) + "\"";

		} else if (e instanceof ElementFunction) {
			return "@" + e.asText();
		}

		return e.asText();
	}

	/**
	 * Checks if constructor arguments match for loop arguments.
	 * 
	 * @param variable
	 *            Variable name.
	 * @param startExpression
	 *            Start expression.
	 * @param endExpression
	 *            End expression.
	 * @param stepExpression
	 *            Step expression.
	 * @return True if arguments are valid, false otherwise.
	 */

	private boolean validateArguments(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {

		boolean temp = variable instanceof ElementVariable && isVariableNumberOrString(startExpression)
				&& isVariableNumberOrString(endExpression);
		return temp && (stepExpression == null || isVariableNumberOrString(stepExpression));
	}

	/**
	 * Checks if element is variable, number or string.
	 * 
	 * @param e
	 *            Reference to element.
	 * @return True if element is variable, number or string. False otherwise.
	 */

	private boolean isVariableNumberOrString(Element e) {
		return e instanceof ElementVariable || e instanceof ElementConstantInteger
				|| e instanceof ElementString || e instanceof ElementConstantDouble;
	}

	/**
	 * Adds escape symbol that was removed before parsing.
	 * 
	 * @param text
	 *            Value of string element.
	 * @return String element with added escape symbols.
	 */

	private String addEscape(String text) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '\\' || text.charAt(i) == '\"') {
				sb.append('\\');

			} else if (text.charAt(i) == '\n') {
				sb.append("\\n");
				continue;

			} else if (text.charAt(i) == '\r') {
				sb.append("\\r");
				continue;

			} else if (text.charAt(i) == 't') {
				sb.append("\\t");
				continue;
			}

			sb.append(text.charAt(i));
		}

		return sb.toString();
	}

	@Override
	public void accept(INodeVisitor visitor) throws IOException {
		visitor.visitForLoopNode(this);
	}

}

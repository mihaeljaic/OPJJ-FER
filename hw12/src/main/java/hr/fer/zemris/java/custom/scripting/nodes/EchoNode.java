package hr.fer.zemris.java.custom.scripting.nodes;

import java.io.IOException;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;

/**
 * Node in parser tree that represents echo tag. It can have zero or more
 * elements of class Element.
 * 
 * @author Mihael Jaić
 *
 */

public class EchoNode extends Node {
	/**
	 * Elements in echo node.
	 */
	private final Element[] elements;

	/**
	 * Constructor that sets reference to elements.
	 * 
	 * @param elements
	 *            Reference to elements.
	 * @throws IllegalArgumentException
	 *             If argument is null.
	 */

	public EchoNode(Element[] elements) throws IllegalArgumentException {
		if (elements == null) {
			throw new IllegalArgumentException("Array of elements can't be null.");
		}

		this.elements = elements;
	}

	/**
	 * Gets elements in echo node.
	 * 
	 * @return Array of elements in echo node.
	 */

	public Element[] getElements() {
		return elements;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EchoNode)) {
			return false;
		}

		EchoNode other = (EchoNode) obj;

		if (elements.length != other.getElements().length) {
			return false;
		}

		for (int i = 0; i < elements.length; i++) {
			if (!(elements[i].equals(other.getElements()[i]))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Reconstruction of original echo tag before parsing.
	 * 
	 * @return Reconstruction of original for loop.
	 */

	@Override
	public String toString() {
		if (elements.length == 0 || (elements.length == 1 && elements[0].asText().isEmpty())) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (Element element : elements) {
			sb.append(" " + rebuildString(element));
		}

		if (sb.length() == 0 || sb.toString().equals("")) {
			return "";
		}

		return sb.toString();
	}

	/**
	 * Adds escape sequences to string elements and @ for function elements that
	 * were lost while parsing.
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

			} else if (text.charAt(i) == '\t') {
				sb.append("\\t");
				continue;
			}

			sb.append(text.charAt(i));
		}

		return sb.toString();
	}

	@Override
	public void accept(INodeVisitor visitor) throws IOException {
		visitor.visitEchoNode(this);
	}
}

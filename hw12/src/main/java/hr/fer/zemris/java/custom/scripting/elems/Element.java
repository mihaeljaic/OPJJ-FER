package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Base abstract class for other element classes which are used in creating
 * Nodes in parser tree.
 * 
 * @author Mihael Jaić
 *
 */

public abstract class Element {

	/**
	 * Returns element's value as text.
	 * 
	 * @return Element's value.
	 */

	public String asText() {
		return "";
	}

}

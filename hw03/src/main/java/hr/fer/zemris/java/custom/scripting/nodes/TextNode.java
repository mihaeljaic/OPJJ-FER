package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node that has document text that is outside of tags.
 * 
 * @author Mihael Jaić
 *
 */

public class TextNode extends Node {
	/**
	 * Text.
	 */
	private final String text;

	/**
	 * Sets text.
	 * 
	 * @param text
	 * @throws IllegalArgumentException
	 *             If text is null.
	 */

	public TextNode(String text) throws IllegalArgumentException {
		if (text == null) {
			throw new IllegalArgumentException("Text can't be null.");
		}
		this.text = text;
	}

	/**
	 * Gets text.
	 * 
	 * @return Text from node.
	 */

	public String getText() {
		return text;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TextNode)) {
			return false;
		}

		TextNode other = (TextNode) obj;

		return text.equals(other.getText());
	}

	/**
	 * Backslashes that were removed while parsing are retrieved. Used for
	 * rebuilding original document.
	 * 
	 * @return Reconstruction of original text.
	 */

	@Override
	public String toString() {

		String temp = text.replaceAll("\\\\", "\\\\\\\\");
		temp = temp.replaceAll("\\{", "\\\\{");
		return temp;
	}

}

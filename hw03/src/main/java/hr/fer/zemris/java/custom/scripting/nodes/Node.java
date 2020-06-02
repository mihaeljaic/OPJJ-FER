package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Base class for other node classes. Represents node in parser tree.
 * 
 * @author Mihael Jaić
 *
 */

public abstract class Node {
	/**
	 * Collection of child nodes.
	 */
	private ArrayIndexedCollection childNodes;

	/**
	 * Adds child to this node.
	 * 
	 * @param child
	 *            Reference to child node.
	 * @throws IllegalArgumentException
	 *             If child isn't instance of Node class.
	 */

	public void addChildNode(Node child) throws IllegalArgumentException {
		if (!(child instanceof Node)) {
			throw new IllegalArgumentException("Child has to be instance of Node class.");
		}

		if (childNodes == null) {
			childNodes = new ArrayIndexedCollection();
		}

		childNodes.add(child);
	}

	/**
	 * Number of childern.
	 * 
	 * @return Number of children.
	 */

	public int numberOfChildren() {
		return childNodes == null ? 0 : childNodes.size();
	}

	/**
	 * Gets child at index.
	 * 
	 * @param index
	 *            Index of child.
	 * @return Reference to child at index.
	 * @throws IndexOutOfBoundsException
	 *             If index is out of array.
	 */

	public Node getChild(int index) throws IndexOutOfBoundsException {
		return (Node) childNodes.get(index);
	}

}

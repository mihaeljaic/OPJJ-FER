package hr.fer.zemris.bf.model;

import hr.fer.zemris.bf.parser.Parser;

/**
 * Interface represents nodes in {@link Parser} tree.
 * 
 * @author Mihael Jaić
 *
 */

public interface Node {
	/**
	 * Accepts visitor.
	 * 
	 * @param visitor Visitor.
	 */
	void accept(NodeVisitor visitor);
}

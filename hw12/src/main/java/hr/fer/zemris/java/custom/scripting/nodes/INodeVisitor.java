package hr.fer.zemris.java.custom.scripting.nodes;

import java.io.IOException;

/**
 * Interface that visits all node in document tree.
 * 
 * @author Mihael Jaić
 *
 */

public interface INodeVisitor {
	/**
	 * Visits text node.
	 * 
	 * @param node
	 *            Text node
	 * @throws IOException
	 *             If there were errors while visiting text node.
	 */
	public void visitTextNode(TextNode node) throws IOException;

	/**
	 * Visits for loop node.
	 * 
	 * @param node
	 *            For loop node.
	 * @throws IOException
	 *             If there were errors while visiting for loop node.
	 */
	public void visitForLoopNode(ForLoopNode node) throws IOException;

	/**
	 * Visits echo node.
	 * 
	 * @param node
	 *            Echo node.
	 * @throws IOException
	 *             If there were errors while visiting echo node.
	 */
	public void visitEchoNode(EchoNode node) throws IOException;

	/**
	 * Visits document node.
	 * 
	 * @param node
	 *            Document node.
	 * @throws IOException
	 *             If there were errors while visiting document node.
	 */
	public void visitDocumentNode(DocumentNode node) throws IOException;

}

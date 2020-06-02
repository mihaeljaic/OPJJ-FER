package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.util.Util;

/**
 * Program gets one argument that is path to script file. Parses script and then
 * tries to recreate original script file, which is printed to standard output.
 * 
 * @author Mihael Jaić
 *
 */

public class TreeWriter {

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 * @throws IOException
	 *             If there were errors while visiting nodes.
	 */

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Expected file name.");
			return;
		}

		String docBody = Util.readFile(args[0]);
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException ex) {
			System.out.println("Could not parse given document.");
			return;
		}

		WriterVisitor visitor = new WriterVisitor();
		parser.getDocumentNode().accept(visitor);
	}

	/**
	 * Visits all nodes in parser tree and tries to recreate original script
	 * document.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.toString());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) throws IOException {
			System.out.printf("{$FOR %s $}", node.toString());

			for (int i = 0, numberOfChildren = node.numberOfChildren(); i < numberOfChildren; i++) {
				node.getChild(i).accept(this);
			}

			System.out.print("{$END$}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.printf("{$=%s$}", node.toString());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) throws IOException {
			for (int i = 0, numberOfChildren = node.numberOfChildren(); i < numberOfChildren; i++) {
				node.getChild(i).accept(this);
			}
		}

	}

}

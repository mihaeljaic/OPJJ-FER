package hr.fer.zemris.java.hw03;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Program in which is demonstrated functionality of {@link SmartScriptParser}.
 * Program accepts one command line argument which is path to text file which
 * will be parsed. Example: "examples/doc1.txt". In examples doc there are 6
 * files which can be tested. After parsing prints reconstruction of original
 * input string from parser syntax tree. Reconstructed string won't necessarily
 * be identical to original. Reconstructed string is printed on standard output
 * and then put into another parser which again does the same thing like first
 * parser. If both outputs are equal then parser is working fine. Also program
 * will print exact structure of parser syntax tree.
 * 
 * @author Mihael Jaić
 *
 */

public class SmartScriptTester {

	/**
	 * Method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 * @throws IOException
	 */

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println(
					"Program gets 1 argument as path to text file which will be parsed. Example\nexamples/doc1.txt");
			System.exit(1);
		}
		String docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);

		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);

		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}

		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);

		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();

		System.out.println("Document body 1:");
		System.out.println(createOriginalDocumentBody(document));
		System.out.println();
		System.out.println("Document body 2:");
		System.out.println(createOriginalDocumentBody(document));

		System.out.println();

		System.out.println("Tree structure document1:");
		System.out.println(printOutDocumentTree(document));
		System.out.println();
		System.out.println("Tree structure document2:");
		System.out.println(printOutDocumentTree(document2));
	}

	/**
	 * Tries to create string from parser syntax tree that was sent to parser.
	 * Uses rebuildOriginal method to create string.
	 * 
	 * @param document
	 *            Reference to document.
	 * @return Rebuilt string.
	 * @throws IllegalArgumentException
	 *             If document is null.
	 */

	private static String createOriginalDocumentBody(DocumentNode document) throws IllegalArgumentException {
		if (document == null) {
			throw new IllegalArgumentException("Document can't be null.");
		}

		StringBuilder sb = new StringBuilder();
		rebuildOriginal(document, sb);

		return sb.toString();
	}

	/**
	 * Visits all nodes of syntax tree and collects their values stored as
	 * string.
	 * 
	 * @param node
	 *            Reference to current node in tree.
	 * @param sb
	 *            String builder which appends values of nodes.
	 */

	private static void rebuildOriginal(Node node, StringBuilder sb) {
		if (!(node instanceof DocumentNode)) {
			if (node instanceof ForLoopNode) {
				sb.append("{$FOR ");
				// Node to string method will add any escape sequences that
				// could be lost during parsing.
				sb.append(node.toString() + "$}");

				for (int i = 0; i < node.numberOfChildren(); i++) {
					rebuildOriginal(node.getChild(i), sb);
				}
				sb.append("{$END$}");

			} else if (node instanceof EchoNode) {
				sb.append("{$=");
				sb.append(node.toString() + "$}");

			} else {
				sb.append(node.toString());
			}
			return;
		}

		for (int i = 0; i < node.numberOfChildren(); i++) {
			rebuildOriginal(node.getChild(i), sb);
		}
	}

	/**
	 * Prints out parser syntax tree.
	 * 
	 * @param document
	 *            Reference to root of document.
	 * @return String representation of tree.
	 */

	private static String printOutDocumentTree(DocumentNode document) {
		StringBuilder sb = new StringBuilder();

		walkTree(document, sb, 0);

		return sb.toString();
	}

	/**
	 * Visits all nodes in tree and collects their value as string.
	 * 
	 * @param node
	 *            Current node in tree.
	 * @param sb
	 *            String builder that collects values of nodes.
	 * @param deviation
	 *            Depth of node in tree. Used for creating tab spaces.
	 */

	private static void walkTree(Node node, StringBuilder sb, int deviation) {
		if (!(node instanceof ForLoopNode || node instanceof DocumentNode)) {
			for (int i = 0; i < deviation; i++) {
				sb.append("\t");
			}

			sb.append(nodeToString(node));
			return;
		}

		for (int i = 0; i < deviation; i++) {
			sb.append("\t");
		}

		deviation++;
		sb.append(nodeToString(node));

		for (int i = 0; i < node.numberOfChildren(); i++) {
			walkTree(node.getChild(i), sb, deviation);
		}

	}

	/**
	 * Generates string representation of node in which is added node class
	 * name.
	 * 
	 * @param node
	 *            Node
	 * @return String representation of given node.
	 */

	private static String nodeToString(Node node) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.getClass().getSimpleName() + " ");

		if (node instanceof TextNode) {
			TextNode textNode = (TextNode) node;
			sb.append(toPrintForm(textNode.toString()));

		} else {
			sb.append(node.toString());
		}

		sb.append("\n");
		return sb.toString();
	}

	/**
	 * Substitutes tabs and new line escape characters with letters so output
	 * would be more readable.
	 * 
	 * @param s
	 *            String in which will be processed.
	 * @return String which has substituted escape characters with letters.
	 */

	private static String toPrintForm(String s) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\n') {
				sb.append("\\n");

			} else if (s.charAt(i) == '\r') {
				sb.append("\\r");

			} else if (s.charAt(i) == '\t') {
				sb.append("\\t");

			} else {
				sb.append(s.charAt(i));
			}
		}

		return sb.toString();
	}

}

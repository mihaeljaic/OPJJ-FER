package hr.fer.zemris.java.custom.scripting.parser;

import java.util.Arrays;
import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;

/**
 * Parser that gets input string and parses it into nodes that form document
 * tree. Input string can contain tags that start with "{$" and are terminated
 * with "$}" at end of tag or document text that is outside of tags. Everything
 * outside tags is treated as text while inside tags there can be variables,
 * numbers, etc. For more details see {@link SmartScriptParser}. Possible tags
 * for this parser are "FOR", "END" and "=". "FOR" tag represents for loop and
 * "END" tag terminates for loop. Each for loop needs to have its end tag. For
 * loop has to start with variable name, followed by 2 or 3 elements that are
 * either variable name, string or number. "=" tag is echo tag. There are no
 * specific rules for number or type of elements inside echo tag.
 * 
 * @author Mihael Jaić
 *
 */

public class SmartScriptParser {
	/**
	 * Lexer.
	 */
	private SmartScriptLexer lexer;
	/**
	 * Stack.
	 */
	private ObjectStack stack;
	/**
	 * Document node.
	 */
	private DocumentNode documentNode;

	/**
	 * Constructor which gets input string and parses it.
	 * 
	 * @param input
	 *            Input string.
	 * @throws SmartScriptParserException
	 *             If something went wrong during parsing.
	 */

	public SmartScriptParser(String input) throws SmartScriptParserException {
		if (input == null || input.equals("")) {
			throw new SmartScriptParserException("Input can't be null or empty!");
		}

		lexer = new SmartScriptLexer(input);
		documentNode = new DocumentNode();
		stack = new ObjectStack();

		try {
			processInput();
		} catch (RuntimeException ex) {
			throw new SmartScriptParserException(ex.getMessage());
		}
	}

	/**
	 * Gets document node.
	 * 
	 * @return Document node.
	 */

	public DocumentNode getDocumentNode() {
		return documentNode;
	}

	/**
	 * Parses given input string.
	 */

	private void processInput() {
		stack.push(documentNode);

		while (true) {
			Token token = lexer.nextToken();

			if (token.getType() == TokenType.EOF && stack.size() > 1) {
				throw new SmartScriptParserException("Too few END tags in document.");
			}

			if (token.getType() == TokenType.EOF) {
				break;
			}

			if (token.getType() == TokenType.STRING) {
				Node baseNode = (Node) stack.peek();
				ElementString temp = (ElementString) token.getValue();

				baseNode.addChildNode(new TextNode((String) temp.asText()));
				continue;
			}

			if (token.getType() == TokenType.TAG) {
				ElementString temp = (ElementString) token.getValue();
				String tagName = (String) temp.asText();

				lexer.setState(LexerState.INSIDE_TAG);

				if (tagName.equals("FOR")) {
					createForLoop();

				} else if (tagName.equals("=")) {
					createEcho();

				} else if (tagName.equals("END")) {
					stack.pop();
					if (stack.isEmpty()) {
						throw new SmartScriptParserException("END tag is in invalid spot. Isn't closing any for loop.");
					}

				} else {
					// Only FOR, = and END tags are accepted by this parser.
					throw new SmartScriptParserException("Invalid tag name.");
				}

				lexer.setState(LexerState.BASIC);
			}
		}
	}

	/**
	 * Creates for loop node.
	 */

	private void createForLoop() {
		Token token = lexer.nextToken();

		ArrayIndexedCollection elements = generateTagElements();
		if (elements.size() < 2 || elements.size() > 3) {
			throw new SmartScriptParserException("Invalid number of arguments in for loop.");
		}

		ForLoopNode forLoop = null;

		if (elements.size() == 2) {
			forLoop = new ForLoopNode((ElementVariable) token.getValue(), (Element) elements.get(0),
					(Element) elements.get(1));

		} else if (elements.size() == 3) {
			forLoop = new ForLoopNode((ElementVariable) token.getValue(), (Element) elements.get(0),
					(Element) elements.get(1), (Element) elements.get(2));
		}

		Node baseNode = (Node) stack.peek();
		baseNode.addChildNode(forLoop);
		stack.push(forLoop);
	}

	/**
	 * Creates echo node.
	 */

	private void createEcho() {
		ArrayIndexedCollection temp = generateTagElements();

		EchoNode echoNode = null;
		if (temp.size() == 0) {
			// If echo tag is empty, empty string is generated to avoid errors.
			Element[] elements = new Element[] { new ElementString("") };
			echoNode = new EchoNode(elements);

		} else {
			Element[] elements = Arrays.copyOf(temp.toArray(), temp.size(), Element[].class);
			echoNode = new EchoNode(elements);
		}

		Node baseNode = (Node) stack.peek();
		baseNode.addChildNode(echoNode);
	}

	/**
	 * Generates tokens inside tag using lexer.
	 * 
	 * @return Collection of elements generated by lexer.
	 */

	private ArrayIndexedCollection generateTagElements() {
		ArrayIndexedCollection elements = new ArrayIndexedCollection();

		while (true) {
			Token token = lexer.nextToken();
			if (token.getType() == TokenType.TERMINATE_TAG) {
				break;
			}
			elements.add((Element) token.getValue());
		}

		return elements;
	}
}

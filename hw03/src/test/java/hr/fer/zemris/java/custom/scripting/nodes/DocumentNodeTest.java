package hr.fer.zemris.java.custom.scripting.nodes;

import static org.junit.Assert.*;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import org.junit.Test;

/**
 * Tests method equals in {@link DocumentNode} class.
 * 
 * @author Mihael Jaić
 *
 */

public class DocumentNodeTest {

	@Test
	public void testNull() {
		DocumentNode doc = new DocumentNode();

		assertFalse(doc.equals(null));
	}

	@Test
	public void testEmptyDocs() {
		DocumentNode doc1 = new DocumentNode();
		DocumentNode doc2 = new DocumentNode();

		assertTrue(doc1.equals(doc2));
	}

	@Test
	public void testSameReference() {
		DocumentNode doc1 = new DocumentNode();
		doc1.addChildNode(new TextNode("asd"));

		DocumentNode doc2 = doc1;

		assertTrue(doc1.equals(doc2));
	}

	@Test
	public void testDifferent() {
		DocumentNode doc1 = new DocumentNode();
		DocumentNode doc2 = new DocumentNode();

		doc1.addChildNode(new TextNode("sss"));
		doc2.addChildNode(new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(0),
				new ElementConstantDouble(10.0)));

		assertFalse(doc1.equals(doc2));
	}

	@Test
	public void testSomeDocuments() {
		DocumentNode doc1 = new DocumentNode();
		DocumentNode doc2 = new DocumentNode();

		doc1.addChildNode(new TextNode("randomtext"));

		ForLoopNode forLoop1 = new ForLoopNode(new ElementVariable("i"), new ElementString("a"),
				new ElementString("b"));
		forLoop1.addChildNode(new EchoNode(new Element[] { new ElementString("random"), new ElementVariable("i"),
				new ElementConstantDouble(1.5) }));

		doc1.addChildNode(forLoop1);

		doc2.addChildNode(new TextNode("randomtext"));

		ForLoopNode forLoop2 = new ForLoopNode(new ElementVariable("i"), new ElementString("a"),
				new ElementString("b"));
		forLoop2.addChildNode(new EchoNode(new Element[] { new ElementString("random"), new ElementVariable("i"),
				new ElementConstantDouble(1.5) }));

		doc2.addChildNode(forLoop2);

		// Documents are entire the same.
		assertTrue(doc1.equals(doc2));
	}

	@Test
	public void testSomeDocuments2() {
		DocumentNode doc1 = new DocumentNode();
		DocumentNode doc2 = new DocumentNode();

		doc1.addChildNode(new TextNode("randomtext"));

		ForLoopNode forLoop1 = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(0),
				new ElementConstantInteger(10));
		forLoop1.addChildNode(new TextNode("aaabbb"));

		doc1.addChildNode(forLoop1);

		doc2.addChildNode(new TextNode("randomtext"));

		// In document2 for loop element variable is "j" and in document1 its
		// "i"
		ForLoopNode forLoop2 = new ForLoopNode(new ElementVariable("j"), new ElementConstantInteger(0),
				new ElementConstantInteger(10));
		forLoop1.addChildNode(new TextNode("aaabbb"));

		doc2.addChildNode(forLoop2);

		// Documents differ in for loop.
		assertFalse(doc1.equals(doc2));
	}

	@Test
	public void testSameNodesButDifferentOrder() {
		DocumentNode doc1 = new DocumentNode();
		DocumentNode doc2 = new DocumentNode();

		doc1.addChildNode(new TextNode("randomtext"));
		doc1.addChildNode(new EchoNode(new Element[] { new ElementVariable("i"), new ElementFunction("sin") }));

		doc2.addChildNode(new EchoNode(new Element[] { new ElementVariable("i"), new ElementFunction("sin") }));
		doc2.addChildNode(new TextNode("randomtext"));

		// Echo node and text node are same but in different order.
		assertFalse(doc1.equals(doc2));
	}

	@Test
	public void testSomeDocuments3() {
		DocumentNode doc1 = new DocumentNode();
		DocumentNode doc2 = new DocumentNode();

		doc1.addChildNode(new TextNode("A tag follows "));
		doc1.addChildNode(new EchoNode(new Element[] { new ElementString("Joe \"Long\" Smith") }));
		doc1.addChildNode(new TextNode("."));

		doc2.addChildNode(new TextNode("A tag follows "));
		doc2.addChildNode(new EchoNode(new Element[] { new ElementString("Joe \"Long\" Smith") }));
		doc2.addChildNode(new TextNode("."));

		assertTrue(doc1.equals(doc2));
	}

	@Test
	public void testSomeDocuments4() {
		DocumentNode doc1 = new DocumentNode();
		DocumentNode doc2 = new DocumentNode();

		doc1.addChildNode(new TextNode("A tag follows "));
		doc1.addChildNode(new EchoNode(new Element[] { new ElementString("Joe \"Long\" Smith") }));
		doc1.addChildNode(new TextNode("."));

		doc2.addChildNode(new TextNode("A tag follows "));
		// String element in echo nodes is different than in document1.
		doc2.addChildNode(new EchoNode(new Element[] { new ElementString("Joe \"Long\"") }));
		doc2.addChildNode(new TextNode("."));

		assertFalse(doc1.equals(doc2));
	}

	@Test
	public void testSomeDocuments5() {
		DocumentNode doc1 = new DocumentNode();
		DocumentNode doc2 = new DocumentNode();

		// Document 1 init.

		ForLoopNode forLoopNode1 = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(1),
				new ElementConstantInteger(10), new ElementConstantInteger(1));

		forLoopNode1.addChildNode(new TextNode("\r\n This is "));
		forLoopNode1.addChildNode(new EchoNode(new Element[] { new ElementVariable("i") }));
		forLoopNode1.addChildNode(new TextNode("-th time this message is generated.\r\n"));

		doc1.addChildNode(forLoopNode1);
		doc1.addChildNode(new TextNode("\r\n"));

		ForLoopNode forLoopNode2 = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(0),
				new ElementConstantInteger(10), new ElementConstantInteger(2));

		forLoopNode2.addChildNode(new TextNode("\r\n sin("));
		forLoopNode2.addChildNode(new EchoNode(new Element[] { new ElementVariable("i") }));
		forLoopNode2.addChildNode(new TextNode("^2) = "));
		forLoopNode2.addChildNode(new EchoNode(
				new Element[] { new ElementVariable("i"), new ElementVariable("i"), new ElementOperator("*"),
						new ElementFunction("sin"), new ElementString("0.000"), new ElementFunction("decfmt") }));
		forLoopNode2.addChildNode(new TextNode("\r\n"));

		doc1.addChildNode(forLoopNode2);

		// Document 2 init.

		ForLoopNode forLoopNode3 = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(1),
				new ElementConstantInteger(10), new ElementConstantInteger(1));

		forLoopNode3.addChildNode(new TextNode("\r\n This is "));
		forLoopNode3.addChildNode(new EchoNode(new Element[] { new ElementVariable("i") }));
		forLoopNode3.addChildNode(new TextNode("-th time this message is generated.\r\n"));

		doc2.addChildNode(forLoopNode3);
		doc2.addChildNode(new TextNode("\r\n"));

		ForLoopNode forLoopNode4 = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(0),
				new ElementConstantInteger(10), new ElementConstantInteger(2));

		forLoopNode4.addChildNode(new TextNode("\r\n sin("));
		forLoopNode4.addChildNode(new EchoNode(new Element[] { new ElementVariable("i") }));
		forLoopNode4.addChildNode(new TextNode("^2) = "));
		forLoopNode4.addChildNode(new EchoNode(
				new Element[] { new ElementVariable("i"), new ElementVariable("i"), new ElementOperator("*"),
						new ElementFunction("sin"), new ElementString("0.000"), new ElementFunction("decfmt") }));
		forLoopNode4.addChildNode(new TextNode("\r\n"));

		doc2.addChildNode(forLoopNode4);

		assertTrue(doc1.equals(doc2));
	}

	@Test
	public void testSomeDocuments6() {
		DocumentNode doc1 = new DocumentNode();
		DocumentNode doc2 = new DocumentNode();

		// Document 1 init.

		ForLoopNode forLoopNode1 = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(1),
				new ElementConstantInteger(10), new ElementConstantInteger(1));

		forLoopNode1.addChildNode(new TextNode("\r\n This is "));
		forLoopNode1.addChildNode(new EchoNode(new Element[] { new ElementVariable("i") }));
		forLoopNode1.addChildNode(new TextNode("-th time this message is generated.\r\n"));

		doc1.addChildNode(forLoopNode1);
		doc1.addChildNode(new TextNode("\r\n"));

		ForLoopNode forLoopNode2 = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(0),
				new ElementConstantInteger(10), new ElementConstantInteger(2));

		forLoopNode2.addChildNode(new TextNode("\r\n sin("));
		forLoopNode2.addChildNode(new EchoNode(new Element[] { new ElementVariable("i") }));
		forLoopNode2.addChildNode(new TextNode("^2) = "));
		forLoopNode2.addChildNode(new EchoNode(
				new Element[] { new ElementVariable("i"), new ElementVariable("i"), new ElementOperator("*"),
						new ElementFunction("sin"), new ElementString("0.000"), new ElementFunction("decfmt") }));
		forLoopNode2.addChildNode(new TextNode("\r\n"));

		doc1.addChildNode(forLoopNode2);

		// Document 2 init.

		ForLoopNode forLoopNode3 = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(1),
				new ElementConstantInteger(10), new ElementConstantInteger(1));

		forLoopNode3.addChildNode(new TextNode("\r\n This is "));
		forLoopNode3.addChildNode(new EchoNode(new Element[] { new ElementVariable("i") }));
		forLoopNode3.addChildNode(new TextNode("-th time this message is generated.\r\n"));

		doc2.addChildNode(forLoopNode3);
		doc2.addChildNode(new TextNode("\r\n"));

		ForLoopNode forLoopNode4 = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(0),
				new ElementConstantInteger(10), new ElementConstantInteger(2));

		// " sin(" is removed in this text node compared to same text node in
		// document1.
		forLoopNode4.addChildNode(new TextNode("\r\n"));
		forLoopNode4.addChildNode(new EchoNode(new Element[] { new ElementVariable("i") }));
		forLoopNode4.addChildNode(new TextNode("^2) = "));
		forLoopNode4.addChildNode(new EchoNode(
				new Element[] { new ElementVariable("i"), new ElementVariable("i"), new ElementOperator("*"),
						new ElementFunction("sin"), new ElementString("0.000"), new ElementFunction("decfmt") }));
		forLoopNode4.addChildNode(new TextNode("\r\n"));

		doc2.addChildNode(forLoopNode4);

		assertFalse(doc1.equals(doc2));
	}

}

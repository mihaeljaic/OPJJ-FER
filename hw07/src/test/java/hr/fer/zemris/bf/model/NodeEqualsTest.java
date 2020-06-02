package hr.fer.zemris.bf.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class NodeEqualsTest {

	@Test
	public void testConstantNodeEquals() {
		ConstantNode trueNode = new ConstantNode(true);
		ConstantNode falseNode = new ConstantNode(false);

		ConstantNode trueNode2 = new ConstantNode(true);
		ConstantNode falseNode2 = new ConstantNode(false);

		assertTrue(trueNode.equals(trueNode2));
		assertTrue(falseNode.equals(falseNode2));

		assertFalse(trueNode.equals(falseNode));
	}

	@Test
	public void testVariableNodeEquals() {
		VariableNode a = new VariableNode("A");
		VariableNode b = new VariableNode("B");

		VariableNode a2 = new VariableNode("A");
		VariableNode b2 = new VariableNode("B");

		assertTrue(a.equals(a2));
		assertTrue(b.equals(b2));

		assertFalse(a.equals(b));
		assertFalse(b.equals(a2));
	}

	@Test
	public void testUnaryOperatorEquals() {
		VariableNode a = new VariableNode("A");
		UnaryOperatorNode not1 = new UnaryOperatorNode("not", a, value -> !value);

		VariableNode b = new VariableNode("B");
		UnaryOperatorNode not2 = new UnaryOperatorNode("not", b, value -> !value);

		UnaryOperatorNode not3 = new UnaryOperatorNode("not", a, value -> !value);

		UnaryOperatorNode not4 = new UnaryOperatorNode("not", new UnaryOperatorNode("not", b, value -> !value),
				value -> !value);
		UnaryOperatorNode not5 = new UnaryOperatorNode("not", not2, value -> !value);

		assertTrue(not1.equals(not3));
		assertFalse(not1.equals(not2));
		assertFalse(not2.equals(not3));

		assertTrue(not4.equals(not5));
		assertFalse(not4.equals(not3));
		assertFalse(not5.equals(not1));
	}

	@Test
	public void testBinaryOperatorEquals() {
		VariableNode a = new VariableNode("A");
		VariableNode b = new VariableNode("B");
		VariableNode c = new VariableNode("C");
		VariableNode d = new VariableNode("D");

		List<Node> children1 = new ArrayList<>();
		children1.add(a);
		children1.add(b);

		List<Node> children2 = new ArrayList<>();
		children2.add(c);
		children2.add(d);

		BinaryOperatorNode or = new BinaryOperatorNode("or", children1, (v1, v2) -> v1 || v2);
		BinaryOperatorNode or2 = new BinaryOperatorNode("or", children1, (v1, v2) -> v1 || v2);
		BinaryOperatorNode or3 = new BinaryOperatorNode("or", children2, (v1, v2) -> v1 || v2);

		assertTrue(or.equals(or2));
		assertFalse(or.equals(or3));
		assertFalse(or2.equals(or3));

		BinaryOperatorNode xor = new BinaryOperatorNode("xor", children1, (v1, v2) -> v1 != v2);

		assertFalse(xor.equals(or));

		List<Node> children3 = new ArrayList<>();
		children3.add(or);
		children3.add(xor);

		// this is duplicated so that we have different references.
		List<Node> children4 = new ArrayList<>();
		children4.add(or);
		children4.add(xor);

		BinaryOperatorNode and = new BinaryOperatorNode("xor", children3, (v1, v2) -> v1 && v2);
		BinaryOperatorNode and2 = new BinaryOperatorNode("xor", children4, (v1, v2) -> v1 && v2);
		
		assertTrue(and.equals(and2));
		assertFalse(and.equals(or3));
		assertFalse(and2.equals(xor));
		assertFalse(and.equals(or));
		assertFalse(and2.equals(or));
	}

}

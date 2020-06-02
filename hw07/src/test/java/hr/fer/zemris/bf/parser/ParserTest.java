package hr.fer.zemris.bf.parser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeEqualsTest;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Some test cases depend on equals method from Node elements. This method is
 * tested for each Node in {@link NodeEqualsTest}.
 * 
 * @author Mihael Jaić
 *
 */

public class ParserTest {

	@Test(expected = ParserException.class)
	public void testNull() {
		Parser parser = new Parser(null);
	}

	@Test(expected = ParserException.class)
	public void testEmpty() {
		Parser parser = new Parser("");
	}

	@Test(expected = ParserException.class)
	public void testTwoVariableNamesInRow() {
		Parser parser = new Parser("a b or c");
	}

	@Test(expected = ParserException.class)
	public void testTwoOperatorsInRow() {
		Parser parser = new Parser("a and or b");
	}

	@Test(expected = ParserException.class)
	public void testConstantAndVariableInRow() {
		Parser parser = new Parser("true a and b");
	}

	@Test(expected = ParserException.class)
	public void testInvalidBrackets1() {
		Parser parser = new Parser("(a and b");
	}

	@Test(expected = ParserException.class)
	public void testInvalidBrackets2() {
		Parser parser = new Parser("(a and b)) + c");
	}

	@Test(expected = ParserException.class)
	public void testInvalidBrackets3() {
		Parser parser = new Parser("(a and b))))");
	}

	@Test(expected = ParserException.class)
	public void testBracketAfterVariable() {
		Parser parser = new Parser("a (and c)");
	}

	@Test(expected = ParserException.class)
	public void testVariableAfterBracket() {
		Parser parser = new Parser("(c or d) mor not (a or b)");
	}

	@Test(expected = ParserException.class)
	public void testStartWithOperator() {
		Parser parser = new Parser("xor a");
	}

	@Test(expected = ParserException.class)
	public void testBinaryOperatorAfterNot() {
		Parser parser = new Parser("not and a");
	}

	@Test(expected = ParserException.class)
	public void testVariableBeforNotOperator() {
		Parser parser = new Parser("A not b and B");
	}

	@Test
	public void testMultipleNotOperators() {
		Parser parser = new Parser("not (not ( not (a)))");

		VariableNode variable = new VariableNode("A");
		UnaryOperatorNode thirdNot = new UnaryOperatorNode("not", variable, a -> !a);
		UnaryOperatorNode secondNot = new UnaryOperatorNode("not", thirdNot, a -> !a);
		UnaryOperatorNode rootNot = new UnaryOperatorNode("not", secondNot, a -> !a);

		assertEquals(rootNot, parser.getExpression());
	}

	@Test
	public void testSomeExpression1() {
		Parser parser = new Parser("(d or b) xor not (a or c)");

		List<Node> secondOrChildren = new ArrayList<>();
		secondOrChildren.add(new VariableNode("A"));
		secondOrChildren.add(new VariableNode("C"));
		BinaryOperatorNode secondOr = new BinaryOperatorNode("or", secondOrChildren, (a, b) -> a || b);

		List<Node> firstOrChildren = new ArrayList<>();
		firstOrChildren.add(new VariableNode("D"));
		firstOrChildren.add(new VariableNode("B"));
		BinaryOperatorNode firstOr = new BinaryOperatorNode("or", firstOrChildren, (a, b) -> a || b);

		UnaryOperatorNode not = new UnaryOperatorNode("not", secondOr, a -> !a);

		List<Node> xorChildren = new ArrayList<>();
		xorChildren.add(firstOr);
		xorChildren.add(not);
		BinaryOperatorNode xor = new BinaryOperatorNode("xor", xorChildren, (a, b) -> a != b);

		assertEquals(xor, parser.getExpression());
	}

	@Test
	public void testSomeExpression2() {
		Parser parser = new Parser("a xor b or c xor d");

		List<Node> secondxorChildren = new ArrayList<>();
		secondxorChildren.add(new VariableNode("C"));
		secondxorChildren.add(new VariableNode("D"));
		BinaryOperatorNode secondxor = new BinaryOperatorNode("xor", secondxorChildren, (a, b) -> a != b);

		List<Node> firstxorChildren = new ArrayList<>();
		firstxorChildren.add(new VariableNode("A"));
		firstxorChildren.add(new VariableNode("B"));
		BinaryOperatorNode firstxor = new BinaryOperatorNode("xor", firstxorChildren, (a, b) -> a != b);

		List<Node> orChildren = new ArrayList<>();
		orChildren.add(firstxor);
		orChildren.add(secondxor);
		BinaryOperatorNode or = new BinaryOperatorNode("or", orChildren, (a, b) -> a || b);

		assertEquals(or, parser.getExpression());
	}
}

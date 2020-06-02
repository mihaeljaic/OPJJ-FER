package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.*;

/**
 * Only lexer is tested in this class. There may be some invalid tags, for loops etc. so lexer won't throw exception in that case.
 * 
 * @author Mihael Jaić
 *
 */

public class SmartScriptLexerTest {

	@Test
	public void testNotNull() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		assertNotNull("Token was expected but null was returned.", lexer.nextToken());
	}

	@Test(expected = LexerException.class)
	public void testGetTokenWhenNoTokenWasGenerated() {
		SmartScriptLexer lexer = new SmartScriptLexer("asd");

		lexer.getToken();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullInput() {
		// must throw!
		new SmartScriptLexer(null);
	}

	@Test
	public void testEmpty() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		assertEquals("Empty input must generate only EOF token.", TokenType.EOF, lexer.nextToken().getType());
	}

	@Test
	public void testGetReturnsLastNext() {
		// Calling getToken once or several times after calling nextToken must
		// return each time what nextToken returned...
		SmartScriptLexer lexer = new SmartScriptLexer("");

		Token token = lexer.nextToken();
		assertEquals("getToken returned different token than nextToken.", token, lexer.getToken());
		assertEquals("getToken returned different token than nextToken.", token, lexer.getToken());
	}

	@Test(expected = LexerException.class)
	public void testNextTokenAfterEOF() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.nextToken();
		// Should throw
		lexer.nextToken();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetNullState() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		lexer.setState(null);
	}
	
	public void testTextOutsideTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("ast \\\\ \r\n\t \\{");
		
		Token t = lexer.nextToken();
		assertEquals(new Token(TokenType.STRING, new ElementString("ast \\ \r\n\t {")), t);
		
		assertEquals(new Token(TokenType.EOF, null), lexer.nextToken());
	}

	@Test(expected = LexerException.class)
	public void testEOFinsideTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$= asdfgh");

		Token t1 = lexer.nextToken();
		assertEquals(new Token(TokenType.TAG, new ElementString("=")), t1);

		lexer.setState(LexerState.INSIDE_TAG);

		Token t2 = lexer.nextToken();
		assertEquals(new Token(TokenType.VARIABLE, new ElementVariable("asdfgh")), t2);

		// Should throw exception because EOF was reached inside tag.
		lexer.nextToken();
	}

	@Test(expected = LexerException.class)
	public void testEmptyTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$  $}");

		lexer.nextToken();
	}

	@Test(expected = LexerException.class)
	public void testInvalidTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$-25$}");

		// Tag name can only be variable name or =
		lexer.nextToken();
	}
	
	@Test
	public void testTagWithSpaces() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$  a_3$}");
		
		Token t = lexer.nextToken();
		// Tags are generated to upper case.
		assertEquals(new Token(TokenType.TAG, new ElementString("A_3")), t);
	}

	@Test(expected = LexerException.class)
	public void testInvalidEscapeSequenceInsideTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$  FOR \" \\{ \"");
		
		// Reads tag
		lexer.nextToken();
		assertEquals(new Token(TokenType.TAG, new ElementString("FOR")), lexer.getToken());

		lexer.setState(LexerState.INSIDE_TAG);
		// Should throw because \{ isn't valid escape sequence.
		lexer.nextToken();
	}

	@Test
	public void testValidEscapeSequenceInsideTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$= \"asd\\\" \"$}");

		Token t1 = lexer.nextToken();
		assertEquals(new Token(TokenType.TAG, new ElementString("=")), t1);

		lexer.setState(LexerState.INSIDE_TAG);

		Token t2 = lexer.nextToken();
		assertEquals(new Token(TokenType.STRING, new ElementString("asd\" ")), t2);
	}

	@Test
	public void testEscapeTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("Example \\{$=1$}.");

		Token token = lexer.nextToken();
		// \\{ will escape tag and backslash will be removed. Tag won't be generated.
		assertEquals(new Token(TokenType.STRING, new ElementString("Example {$=1$}.")), token);
	}

	@Test(expected = LexerException.class)
	public void testInvalidEscapeSequenceOutsideTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("asd\\\"");

		// " is invalid escape sequence outside tag, exception should be thrown.
		lexer.nextToken();
	}

	@Test
	public void testMinusSignInterpretedAsNumber() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$= -25$}");

		Token t1 = lexer.nextToken();
		assertEquals(new Token(TokenType.TAG, new ElementString("=")), t1);

		lexer.setState(LexerState.INSIDE_TAG);

		Token t2 = lexer.nextToken();
		assertEquals(new Token(TokenType.INTEGER, new ElementConstantInteger(-25)), t2);
	}

	@Test
	public void testMinusSignInterpretedAsOperator() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ FoR - ij$}");

		Token t1 = lexer.nextToken();
		assertEquals(new Token(TokenType.TAG, new ElementString("FOR")), t1);

		lexer.setState(LexerState.INSIDE_TAG);

		Token t2 = lexer.nextToken();
		assertEquals(new Token(TokenType.OPERATOR, new ElementOperator("-")), t2);
	}

	@Test
	public void testNumberInterpretedAsDouble() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$= -3.6$}");

		Token t1 = lexer.nextToken();
		assertEquals(new Token(TokenType.TAG, new ElementString("=")), t1);

		lexer.setState(LexerState.INSIDE_TAG);

		Token t2 = lexer.nextToken();
		assertEquals("-3.6 must be interpreted as double not integer!",
				new Token(TokenType.DOUBLE, new ElementConstantDouble(-3.6)), t2);
	}

	@Test
	public void testFunction() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$= @sin $}");
		
		Token t1 = lexer.nextToken();
		assertEquals(new Token(TokenType.TAG, new ElementString("=")), t1);
		
		lexer.setState(LexerState.INSIDE_TAG);
		
		Token t2 = lexer.nextToken();
		assertEquals(new Token(TokenType.FUNCTION, new ElementFunction("sin")), t2);
	}
	
	@Test
	public void testVariableName() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$=  \n   a23_+++$}");
		
		Token t1 = lexer.nextToken();
		assertEquals(new Token(TokenType.TAG, new ElementString("=")), t1);
		
		lexer.setState(LexerState.INSIDE_TAG);
		
		Token t2 = lexer.nextToken();
		assertEquals(new Token(TokenType.VARIABLE, new ElementVariable("a23_")), t2);
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidTextInsideTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$= # $}");
		
		Token t1 = lexer.nextToken();
		assertEquals(new Token(TokenType.TAG, new ElementString("=")), t1);
		
		lexer.setState(LexerState.INSIDE_TAG);
		
		// # doesn't match anything. Exception should be thrown.
		lexer.nextToken();
	}
	
}

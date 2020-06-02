package hr.fer.zemris.bf.lexer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LexerTest {

	@Test(expected = LexerException.class)
	public void testNull() {
		Lexer lexer = new Lexer(null);
	}
	
	@Test
	public void testEmpty() {
		Lexer lexer = new Lexer("");
		
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test(expected = LexerException.class)
	public void testNextTokenAfterEOF() {
		Lexer lexer = new Lexer("");
		
		lexer.nextToken();
		// already reached EOF.
		lexer.nextToken();
	}
	
	@Test(expected = LexerException.class)
	public void testInvalidNumericSequence1() {
		Lexer lexer = new Lexer("10");
		
		lexer.nextToken();
	}
	
	@Test(expected = LexerException.class)
	public void testInvalidNumericSequence2() {
		Lexer lexer = new Lexer("325");
		
		lexer.nextToken();
	}
	
	@Test(expected = LexerException.class)
	public void testInvalidExpression1() {
		Lexer lexer = new Lexer(":+;");
		
		lexer.nextToken();
	}
	
	@Test(expected = LexerException.class)
	public void testInvalidExpression2() {
		Lexer lexer = new Lexer("-");
		
		lexer.nextToken();
	}
	
	@Test
	public void testVariableName() {
		Lexer lexer = new Lexer("  	  vAr_NaMe1 VaRnAmE_2 ");
		
		assertEquals(new Token(TokenType.VARIABLE, "VAR_NAME1"), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "VARNAME_2"), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testVariable() {
		Lexer lexer = new Lexer("a");
		
		assertEquals(new Token(TokenType.VARIABLE, "A"), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testAndOperator() {
		Lexer lexer = new Lexer("  AnD *  ");
		
		assertEquals(new Token(TokenType.OPERATOR, "and"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "and"), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testOrOperator() {
		Lexer lexer = new Lexer("  Or +  ");
		
		assertEquals(new Token(TokenType.OPERATOR, "or"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "or"), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testXorOperator() {
		Lexer lexer = new Lexer("  XoR :+:  ");
			
		assertEquals(new Token(TokenType.OPERATOR, "xor"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "xor"), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testNotOperator() {
		Lexer lexer = new Lexer("  NoT ! ");
		
		assertEquals(new Token(TokenType.OPERATOR, "not"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "not"), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testTrueConstant() {
		Lexer lexer = new Lexer("  TrUe 1  ");
			
		assertEquals(new Token(TokenType.CONSTANT, true), lexer.nextToken());
		assertEquals(new Token(TokenType.CONSTANT, true), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testFalseConstant() {
		Lexer lexer = new Lexer("  FaLsE  0  ");
		
		assertEquals(new Token(TokenType.CONSTANT, false), lexer.nextToken());
		assertEquals(new Token(TokenType.CONSTANT, false), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testBrackets() {
		Lexer lexer = new Lexer("  ( )  ");
		
		assertEquals(new Token(TokenType.OPEN_BRACKET, '('), lexer.nextToken());
		assertEquals(new Token(TokenType.CLOSED_BRACKET, ')'), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testVariableSeparationWithOperators() {
		Lexer lexer = new Lexer(" xor_1 xor astrue");
		
		assertEquals(new Token(TokenType.VARIABLE, "XOR_1"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "xor"), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "ASTRUE"), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testVariableSeparationWithOperators2() {
		Lexer lexer = new Lexer(" a:+:b  ");
		
		assertEquals(new Token(TokenType.VARIABLE, "A"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "xor"), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "B"), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testSomeExpression1() {
		Lexer lexer = new Lexer("(d or b) xor not (a or c)");
		
		assertEquals(new Token(TokenType.OPEN_BRACKET, '('), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "D"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "or"), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "B"), lexer.nextToken());
		assertEquals(new Token(TokenType.CLOSED_BRACKET, ')'), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "xor"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "not"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPEN_BRACKET, '('), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "A"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "or"), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "C"), lexer.nextToken());
		assertEquals(new Token(TokenType.CLOSED_BRACKET, ')'), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}
	
	@Test
	public void testSomeExpression2() {
		Lexer lexer = new Lexer("(c or d) mor not (a or b)");
		
		assertEquals(new Token(TokenType.OPEN_BRACKET, '('), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "C"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "or"), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "D"), lexer.nextToken());
		assertEquals(new Token(TokenType.CLOSED_BRACKET, ')'), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "MOR"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "not"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPEN_BRACKET, '('), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "A"), lexer.nextToken());
		assertEquals(new Token(TokenType.OPERATOR, "or"), lexer.nextToken());
		assertEquals(new Token(TokenType.VARIABLE, "B"), lexer.nextToken());
		assertEquals(new Token(TokenType.CLOSED_BRACKET, ')'), lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
	}

}

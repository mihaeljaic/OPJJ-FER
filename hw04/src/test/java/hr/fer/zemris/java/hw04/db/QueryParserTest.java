package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * In order to parse input text you need to call method parse() !!!!
 * 
 * @author Mihael Jaić
 *
 */

public class QueryParserTest {
	
	@Test(expected = QueryParserException.class)
	public void testNull() {
		QueryParser qp = new QueryParser(null);
	}
	
	@Test
	public void testDirectQuery() {
		QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
		qp1.parse();
		
		assertTrue(qp1.isDirectQuery());
		assertEquals("0123456789", qp1.getQueriedJMBAG());
		assertEquals(1, qp1.getQuery().size());
	}
	
	@Test
	public void testSomeQuery() {
		QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" aNd    lastName>\"J\"");
		qp2.parse();
		
		assertFalse(qp2.isDirectQuery());
		assertEquals(2, qp2.getQuery().size());
		
		assertEquals(FieldValueGetters.JMBAG, qp2.getQuery().get(0).getFieldGetter());
		assertEquals(ComparisonOperators.EQUALS, qp2.getQuery().get(0).getComparisonOperator());
		assertEquals("0123456789", qp2.getQuery().get(0).getStringLiteral());
		
		assertEquals(FieldValueGetters.LAST_NAME, qp2.getQuery().get(1).getFieldGetter());
		assertEquals(ComparisonOperators.GREATER, qp2.getQuery().get(1).getComparisonOperator());
		assertEquals("J", qp2.getQuery().get(1).getStringLiteral());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testNotDirectQueryCallingMethodGetQueriedJmbag() {
		QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
		qp2.parse();
		
		// This will throw.
		qp2.getQueriedJMBAG();
	}
	
	@Test(expected = QueryParserException.class)
	public void testQueryBeginsWithStringLiteral() {
		QueryParser qp = new QueryParser("\"asd\" = firstName");
		// This will throw
		qp.parse();
	}
	
	@Test(expected = QueryParserException.class)
	public void testQueryDoesntEndWithStringLiteral() {
		QueryParser qp = new QueryParser("firstName = \"asd\" AND lastName");

		qp.parse();
	}
	
	@Test(expected = QueryParserException.class)
	public void testQueryInvalidAttributeName() {
		QueryParser qp = new QueryParser("FirstNaME = \"asdf\"");
		
		qp.parse();
	}
	
	@Test(expected = QueryParserException.class)
	public void testQueryNoAndOperator() {
		QueryParser qp = new QueryParser("firstName = \"rand\" lastName != \"a\"");
		
		qp.parse();
	}
	
	@Test
	public void testQueryWithMultipleExpressions() {
		QueryParser qp = new QueryParser("jmbag = 		\"0000000003\" AND lastName LikE \"B*\"		");
		
		qp.parse();
		
		assertFalse(qp.isDirectQuery());
		assertEquals(2, qp.getQuery().size());
		
		assertEquals(FieldValueGetters.JMBAG, qp.getQuery().get(0).getFieldGetter());
		assertEquals(ComparisonOperators.EQUALS, qp.getQuery().get(0).getComparisonOperator());
		assertEquals("0000000003", qp.getQuery().get(0).getStringLiteral());
		
		assertEquals(FieldValueGetters.LAST_NAME, qp.getQuery().get(1).getFieldGetter());
		assertEquals(ComparisonOperators.LIKE, qp.getQuery().get(1).getComparisonOperator());
		assertEquals("B*", qp.getQuery().get(1).getStringLiteral());
	}

}

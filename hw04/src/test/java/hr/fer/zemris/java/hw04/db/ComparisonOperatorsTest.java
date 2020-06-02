package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComparisonOperatorsTest {
	
	@Test(expected = NullPointerException.class)
	public void testNull() {
		IComparisonOperator oper = ComparisonOperators.EQUALS;
		
		oper.satisfied(null, null);
	}
	
	@Test
	public void testLess() {
		IComparisonOperator lessOperator = ComparisonOperators.LESS;
		
		assertTrue(lessOperator.satisfied("Ana", "Jasna"));
		assertTrue(lessOperator.satisfied("A", "B"));
		assertFalse(lessOperator.satisfied("Jasna", "Ana"));
		assertFalse(lessOperator.satisfied("č", "A"));
	}
	
	@Test
	public void testLessOrEquals() {
		IComparisonOperator lessOrEquals = ComparisonOperators.LESS_OR_EQUALS;
		
		assertTrue(lessOrEquals.satisfied("Ana", "Jasna"));
		assertTrue(lessOrEquals.satisfied("Ana", "Ana"));
		assertFalse(lessOrEquals.satisfied("Jasna", "Ana"));
		assertFalse(lessOrEquals.satisfied("B", "A"));
	}
	
	@Test
	public void testGreater() {
		IComparisonOperator greater = ComparisonOperators.GREATER;
		
		assertTrue(greater.satisfied("Jasna", "Ana"));
		assertTrue(greater.satisfied("B", "A"));
		assertFalse(greater.satisfied("A", "a"));
		assertFalse(greater.satisfied("z", "č"));
	}
	
	@Test
	public void testGreaterOrEquals() {
		IComparisonOperator greaterOrEquals = ComparisonOperators.GREATER_OR_EQUALS;
		
		assertTrue(greaterOrEquals.satisfied("Jasna", "Ana"));
		assertTrue(greaterOrEquals.satisfied("Jasna", "Jasna"));
		assertFalse(greaterOrEquals.satisfied("a", "rand"));
		assertFalse(greaterOrEquals.satisfied("Ana", "Anaa"));
	}
	
	@Test
	public void testEquals() {
		IComparisonOperator equalsOperator = ComparisonOperators.EQUALS;
		
		assertTrue(equalsOperator.satisfied("Ana", "Ana"));
		assertTrue(equalsOperator.satisfied("Jasna", "Jasna"));
		assertFalse(equalsOperator.satisfied("Ana", "Jasna"));
		assertFalse(equalsOperator.satisfied("Jasna", "Ana"));
	}
	
	@Test
	public void testNotEquals() {
		IComparisonOperator notEquals = ComparisonOperators.NOT_EQUALS;
		
		assertTrue(notEquals.satisfied("Ana", "Jasna"));
		assertTrue(notEquals.satisfied("Jasna", "Ana"));
		assertFalse(notEquals.satisfied("Ana", "Ana"));
		assertFalse(notEquals.satisfied("Jasna", "Jasna"));
	}
	
	
	// LIKE operator tests.
	
	
	@Test(expected = NullPointerException.class)
	public void testLikeNull() {
		IComparisonOperator likeOper = ComparisonOperators.LIKE;
		
		likeOper.satisfied("asd", null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLikeMoreStars1() {
		IComparisonOperator likeOper = ComparisonOperators.LIKE;
		
		likeOper.satisfied("aa", "as*df*g");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLikeMoreStars2() {
		IComparisonOperator likeOper = ComparisonOperators.LIKE;
		
		likeOper.satisfied("dds", "asd**");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLikeMoreStars3() {
		IComparisonOperator likeOper = ComparisonOperators.LIKE;
		
		likeOper.satisfied("random", "**");
	}
	
	@Test
	public void testLikeOnlyStarInPattern() {
		IComparisonOperator likeOper = ComparisonOperators.LIKE;
		
		assertTrue(likeOper.satisfied("anything", "*"));
		assertTrue(likeOper.satisfied("", "*"));
	}
	
	@Test
	public void testLikeNoStars() {
		IComparisonOperator likeOper = ComparisonOperators.LIKE;
		
		assertTrue(likeOper.satisfied("ad", "ad"));
		assertTrue(likeOper.satisfied("", ""));
		assertFalse(likeOper.satisfied("ad", "da"));
		assertFalse(likeOper.satisfied("AD", "ad"));
	}
	
	@Test
	public void testLikeOneStarBegining() {
		IComparisonOperator likeOper = ComparisonOperators.LIKE;
		
		assertTrue(likeOper.satisfied("sda", "*da"));
		assertTrue(likeOper.satisfied("Zagreb", "*eb"));
		assertFalse(likeOper.satisfied("Zagreb", "*it"));
		assertFalse(likeOper.satisfied("random", "*randomness"));
		assertFalse(likeOper.satisfied("", "*A"));
	}
	
	@Test
	public void testLikeOneStarMiddle() {
		IComparisonOperator likeOper = ComparisonOperators.LIKE;
		
		assertTrue(likeOper.satisfied("AAAA", "AA*AA"));
		assertTrue(likeOper.satisfied("ABCssds", "ABC*"));
		assertFalse(likeOper.satisfied("AAA", "AA*AA"));
		assertFalse(likeOper.satisfied("asdsa", "asd*dsa"));
		assertFalse(likeOper.satisfied("", "a*a"));
	}
	
	@Test
	public void testLikeOneStarEnd() {
		IComparisonOperator likeOper = ComparisonOperators.LIKE;
		
		assertTrue(likeOper.satisfied("abc", "abc*"));
		assertTrue(likeOper.satisfied("dsfgh", "ds*"));
		assertFalse(likeOper.satisfied("Zagreb", "za*"));
		assertFalse(likeOper.satisfied("Zagreb", "Zagrebiensis*"));
		assertFalse(likeOper.satisfied("", "a*"));
	}
	
}

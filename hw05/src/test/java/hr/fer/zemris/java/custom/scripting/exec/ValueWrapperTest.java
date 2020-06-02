package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValueWrapperTest {
	
	@Test
	public void testAddBothNull() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		
		v1.add(v2.getValue());
		
		assertEquals(null, v2.getValue());
		assertEquals(0, v1.getValue());
	}
	
	@Test
	public void testAddDoubleWithInteger() {
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		
		v3.add(v4.getValue());
		
		assertEquals(13.0, v3.getValue());
		assertEquals(1, v4.getValue());
	}
	
	@Test
	public void testAddIntegers() {
		ValueWrapper v5 = new ValueWrapper("12");
		ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));
		
		v5.add(v6.getValue());
		
		assertEquals(13, v5.getValue());
		assertEquals(1, v6.getValue());
	}
	
	@Test(expected = RuntimeException.class)
	public void testInvalidString() {
		ValueWrapper v7 = new ValueWrapper("Ankica");
		ValueWrapper v8 = new ValueWrapper(Integer.valueOf(1));
		
		v7.add(v8.getValue());
	}
	
	@Test
	public void testObjectStayedTheSame() {
		String other = "13";
		ValueWrapper wrapper = new ValueWrapper(null);
		
		wrapper.add(other);
		wrapper.subtract(other);
		wrapper.multiply(other);
		wrapper.divide(other);
		
		assertTrue(wrapper.numCompare(other) < 0);
		assertEquals(0, wrapper.getValue());
		assertEquals("13", other);
	}
	
	@Test
	public void testAddDoubleToNull() {
		ValueWrapper wrapper = new ValueWrapper(null);
		
		wrapper.add(5.4);
		assertEquals(5.4, wrapper.getValue());
	}
	
	@Test
	public void testSubtractNulls() {
		ValueWrapper vw = new ValueWrapper(null);
		
		vw.subtract(null);
		assertEquals(0, vw.getValue());
	}
	
	@Test
	public void testSubtractDoubleWithString() {
		ValueWrapper v1 = new ValueWrapper(3.5);
		ValueWrapper v2 = new ValueWrapper("3.5e+0");
		
		v1.subtract(v2.getValue());
		
		assertEquals(0.0, v1.getValue());
		assertEquals("3.5e+0", v2.getValue());
	}
	
	@Test
	public void testSubtractStringWithNull() {
		ValueWrapper v1 = new ValueWrapper("5");
		ValueWrapper v2 = new ValueWrapper(null);
		
		v1.subtract(v2.getValue());
		
		assertEquals(5, v1.getValue());
		assertEquals(null, v2.getValue());
		assertTrue(v1.getValue() instanceof Integer);
	}
	
	@Test
	public void testMultiplyNulls() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		
		v1.multiply(v2.getValue());
		
		assertEquals(0, v1.getValue());
		assertEquals(null, v2.getValue());
	}
	
	@Test
	public void testMultiplyStringWithNull() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper("3.5E+1");
		
		v1.multiply(v2.getValue());
		
		assertEquals(0.0, v1.getValue());
		assertTrue(v1.getValue() instanceof Double);
		assertEquals("3.5E+1", v2.getValue());
	}
	
	@Test(expected = RuntimeException.class)
	public void testDivisionByDecimalZero() {
		ValueWrapper wrapper = new ValueWrapper(null);
		
		wrapper.divide(0.0);
	}
	
	@Test(expected = RuntimeException.class)
	public void testDivisionByIntegerZero() {
		ValueWrapper wrapper = new ValueWrapper(null);
		
		wrapper.divide(0);
	}
	
	@Test(expected = RuntimeException.class)
	public void testDivisionByStringZero() {
		ValueWrapper v1 = new ValueWrapper(2.5);
		ValueWrapper v2 = new ValueWrapper("0.0");
		
		v1.divide(v2.getValue());
	}
	
	@Test
	public void testDivideStringsIntInt() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper("2");
		
		v1.divide(v2.getValue());
		
		assertEquals(1, v1.getValue());
		assertEquals("2", v2.getValue());
	}
	
	@Test
	public void testDivideStringsIntDouble() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper("-1.5");
		
		v1.divide(v2.getValue());
		
		assertEquals(-2.0, v1.getValue());
		assertEquals("-1.5", v2.getValue());
	}
	
	@Test
	public void testCompareInts() {
		ValueWrapper v1 = new ValueWrapper(2);
		ValueWrapper v2 = new ValueWrapper(7);
		
		assertTrue(v1.numCompare(v2.getValue()) < 0);
		assertEquals(2, v1.getValue());
		assertEquals(7, v2.getValue());
	}
	
	@Test
	public void testCompareIntsEqual() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(3);
		
		assertTrue(v1.numCompare(v2.getValue()) == 0);
		assertEquals(3, v1.getValue());
		assertEquals(3, v2.getValue());
	}
	
	@Test
	public void testCompareDoubleAndInt() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(3.000001);
		
		assertTrue(v1.numCompare(v2.getValue()) < 0);
		assertEquals(3, v1.getValue());
		assertEquals(3.000001, v2.getValue());
	}
	
	@Test
	public void testCompareNulls() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		
		assertTrue(v1.numCompare(v2.getValue()) == 0);
		assertEquals(null, v1.getValue());
		assertEquals(null, v2.getValue());
	}
	
	@Test
	public void testCompareStrings() {
		ValueWrapper v1 = new ValueWrapper("3.5E+1");
		ValueWrapper v2 = new ValueWrapper("35");
		
		assertTrue(v1.numCompare(v2.getValue()) == 0);
		assertEquals("3.5E+1", v1.getValue());
		assertEquals("35", v2.getValue());
	}
	
	@Test
	public void testCompareStringsDoubleInt() {
		ValueWrapper v1 = new ValueWrapper("3.0000001");
		ValueWrapper v2 = new ValueWrapper("3");
		
		assertTrue(v1.numCompare(v2.getValue()) > 0);
		assertEquals("3.0000001", v1.getValue());
		assertEquals("3", v2.getValue());
	}
	
	@Test(expected = RuntimeException.class)
	public void testEmptyString() {
		ValueWrapper v1 = new ValueWrapper("");
		
		v1.add(3);
	}
	
	@Test(expected = RuntimeException.class)
	public void testInvalidType1() {
		ValueWrapper v1 = new ValueWrapper(new ValueWrapper(""));
		
		v1.add(3);
	}
	
	@Test(expected = RuntimeException.class)
	public void testInvalidType2() {
		ValueWrapper v1 = new ValueWrapper("23.4.2");
		
		v1.add(2);
	}
}

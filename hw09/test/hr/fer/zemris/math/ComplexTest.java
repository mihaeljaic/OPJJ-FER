package hr.fer.zemris.math;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class ComplexTest {
	private static final double epsilon = 1e-6;
	
	private Complex c1;
	private Complex c2;
	
	@Before
	public void init() {
		c1 = new Complex(2.5, -3);
		c2 = new Complex(2, 3);
	}
	
	@Test
	public void testModule() {
		assertEquals(3.9051248379533272, c1.module(), epsilon);
		assertEquals(3.6055512754639893, c2.module(), epsilon);
	}
	
	@Test
	public void testMultiply() {
		assertEquals(new Complex(14, 1.5), c1.multiply(c2));
	}
	
	@Test
	public void testDivide() {
		assertEquals(new Complex(-0.307692, -1.038462), c1.divide(c2));
	}
	
	@Test
	public void testAdd() {
		assertEquals(new Complex(4.5, 0), c1.add(c2));
	}
	
	@Test
	public void testSub() {
		assertEquals(new Complex(0.5, -6), c1.sub(c2));
	}
	
	@Test
	public void testNegate() {
		assertEquals(new Complex(-2.5, 3), c1.negate());
	}
	
	@Test
	public void testPower() {
		assertEquals(new Complex(-51.875, -29.25), c1.power(3));
	}
	
	@Test
	public void testRoot() {
		List<Complex> solutions = c1.root(3);

		List<Complex> expectedResult = new ArrayList<>();
		expectedResult.add(new Complex(-0.361428879, 1.532712301));
		expectedResult.add(new Complex(-1.1466533, -1.0793627));
		expectedResult.add(new Complex(1.5080822, -0.4533496));
		
		for (Complex solution : solutions) {
			assertTrue(expectedResult.contains(solution));
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidExpression() {
		String expression = "3 + 2";
		Complex c = new Complex(expression);
	}
	
	@Test
	public void testParse() {
		String expression = "  -i2.5  ";
		Complex c = new Complex(expression);
		
		assertEquals(2.5, c.module(), epsilon);
		System.out.println(c);
	}
}

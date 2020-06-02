package hr.fer.zemris.java.hw01;

import static org.junit.Assert.*;

import java.text.NumberFormat;
import java.text.ParseException;

import org.junit.Test;

/**
 * @author Mihael Jaić
 *
 */
public class RectangleTest extends Rectangle {
	/**
	 * Number used for checking if two real numbers are equal. If absolute value of two real numbers is less
	 * than epsilon they are considered equal.
	 */
	private static final double EPSILON = 1e-6;

	/**
	 * 
	 */
	@Test
	public void testcalculateArea1() {
		assertEquals(16, calculateArea(2, 8), EPSILON);
	}
	
	/**
	 * 
	 */
	@Test
	public void testcalculateArea2(){
		NumberFormat formatter = NumberFormat.getInstance();
		try {
			double width = formatter.parse("2.1").doubleValue();
			double height = formatter.parse("3.5").doubleValue();
			double result = formatter.parse("7.35").doubleValue();
			assertEquals(result, calculateArea(width, height), EPSILON);
		} catch (ParseException e) {
			//ignore
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testcalculatePerimeter1(){
		NumberFormat formatter = NumberFormat.getInstance();
		try {
			double width = formatter.parse("2.1").doubleValue();
			double height = formatter.parse("3.5").doubleValue();
			double result = formatter.parse("11.2").doubleValue();
			assertEquals(result, calculatePerimeter(width, height), EPSILON);
		} catch (ParseException e) {
			//ignore
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testcalculatePerimeter2(){
		assertEquals(20, calculatePerimeter(2, 8), EPSILON);
	}
	
}

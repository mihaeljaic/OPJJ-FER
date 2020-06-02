package hr.fer.zemris.java.hw01;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Mihael Jaić
 *
 */
public class FactorialTest extends Factorial {

	/**
	 * 
	 */
	@Test
	public void fourFactorial(){
		assertEquals(24, calculateFactorial(4));
	}
	
	/**
	 * 
	 */
	@Test
	public void oneFactorial(){
		assertEquals(1, calculateFactorial(1));
	}
	
	/**
	 * 
	 */
	@Test
	public void twentyFactorial(){
		assertEquals(2432902008176640000L, calculateFactorial(20));
	}
	
	/**
	 * 
	 */
	@Test
	public void fifteenFactorial(){
		assertEquals(1307674368000L, calculateFactorial(15));
	}

}

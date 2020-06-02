package hr.fer.zemris.java.hw05.demo2;

import static org.junit.Assert.*;

import java.text.NumberFormat;
import java.text.ParseException;

import org.junit.Test;

public class PrimesCollectionTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeInput() {
		PrimesCollection primes = new PrimesCollection(-2);
	}

	@Test
	public void testFirstTenPrimeNumbers() {
		PrimesCollection primesCollection = new PrimesCollection(10);

		Integer[] expected = new Integer[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29 };

		int i = 0;
		for (Integer prime : primesCollection) {
			assertEquals(expected[i++], prime);
		}
	}

	@Test
	public void testNestedForLoops() {
		// First 3 prime numbers will be generated.
		PrimesCollection primesCollection = new PrimesCollection(3);

		int i = 0;
		for (Integer prim1 : primesCollection) {
			int j = 0;
			for (Integer prim2 : primesCollection) {
				int k = 0;
				for (Integer prim3 : primesCollection) {
					assertTrue(testPrimes(prim1, prim2, prim3, i, j, k));
					k++;
				}
				j++;
			}
			i++;
		}
	}
	
	
	/**
	 * Specific method for testing if 3 prime numbers are correct by matching
	 * their index. Index 0 means it is first prime number so expected prime
	 * number is 2 and so on. Works only for indexes 0 to 2.
	 * 
	 * @param prim1
	 *            First prime number to be compared.
	 * @param prim2
	 *            Second prime number to be compared.
	 * @param prim3
	 *            Third prime number to be compared.
	 * @param i
	 *            Index of first prime number.
	 * @param j
	 *            Index of second prime number.
	 * @param k
	 *            Index of third prime number.
	 * @return True if all prime numbers match their indexes, false otherwise.
	 */

	private boolean testPrimes(int prim1, int prim2, int prim3, int i, int j, int k) {
		int exp1 = matchPrime(i);
		int exp2 = matchPrime(j);
		int exp3 = matchPrime(k);

		return prim1 == exp1 && prim2 == exp2 && exp3 == prim3;

	}

	private int matchPrime(int index) {
		if (index == 0) {
			return 2;
		}
		// Works only for indexes 0 to 2.
		return index == 1 ? 3 : 5;
	}

}

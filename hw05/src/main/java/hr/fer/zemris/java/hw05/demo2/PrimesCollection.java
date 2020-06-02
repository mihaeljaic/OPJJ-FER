package hr.fer.zemris.java.hw05.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Collection that generates first n prime numbers. Number n is specified in
 * constructor. This collection has to be used in foreach loop.
 * 
 * @author Mihael Jaić
 *
 */

public class PrimesCollection implements Iterable<Integer> {
	/**
	 * Number of prime numbers this collection will generate.
	 */
	private final int count;

	/**
	 * Gets number of prime numbers that this collection has to generate.
	 * 
	 * @param count
	 *            Number of prime numbers this collection will generate.
	 * @throws IllegalArgumentException
	 *             If count isn't positive.
	 */

	public PrimesCollection(int count) throws IllegalArgumentException {
		super();
		if (count < 1) {
			throw new IllegalArgumentException("Number of primes has to be positive.");
		}

		this.count = count;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new IteratorImpl(count);
	}

	/**
	 * Iterator for {@link PrimesCollection} class. Generates first n prime
	 * numbers.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class IteratorImpl implements Iterator<Integer> {
		/**
		 * Current prime number.
		 */
		private int currentPrime;
		/**
		 * Number of prime numbers to generate.
		 */
		private int remaining;

		// Modification count was not used because collection can't change
		// anything(count variable is final).

		/**
		 * Constructor that sets number of prime numbers to be generated.
		 * 
		 * @param count
		 *            Number of prime numbers to be generated.
		 */

		private IteratorImpl(int count) {
			super();

			currentPrime = 1;
			remaining = count;
		}

		@Override
		public boolean hasNext() {
			return remaining > 0;
		}

		@Override
		public Integer next() {
			if (remaining <= 0) {
				throw new NoSuchElementException("No more prime numbers to generate");
			}

			remaining--;

			calculateNextPrime();

			return currentPrime;
		}

		/**
		 * Finds next prime number.
		 */

		private void calculateNextPrime() {
			while (true) {
				currentPrime++;

				boolean isPrime = true;
				double squareRoot = Math.sqrt(currentPrime);
				for (int i = 2; i <= squareRoot; i++) {
					if (currentPrime % i == 0) {
						isPrime = false;
						break;
					}
				}

				if (isPrime) {
					return;
				}
			}
		}

	}

}

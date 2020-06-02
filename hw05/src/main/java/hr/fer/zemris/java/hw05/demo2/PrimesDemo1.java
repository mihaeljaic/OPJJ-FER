package hr.fer.zemris.java.hw05.demo2;

/**
 * Demonstrates functionality of {@link PrimesCollection} class. Prints out
 * first 5 prime numbers.
 * 
 * @author Mihael Jaić
 *
 */

public class PrimesDemo1 {

	/**
	 * Method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(5);

		for (Integer prime : primesCollection) {
			System.out.println("Got prime: " + prime);
		}
	}

}

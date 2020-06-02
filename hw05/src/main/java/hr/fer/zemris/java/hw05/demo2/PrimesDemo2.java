package hr.fer.zemris.java.hw05.demo2;

/**
 * Demonstrates functionality of {@link PrimesCollection} class in nested
 * foreach loops.
 * 
 * @author Mihael Jaić
 *
 */

public class PrimesDemo2 {

	/**
	 * Method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(2);

		for (Integer prime : primesCollection) {
			for (Integer prime2 : primesCollection) {
				System.out.println("Got prime pair: " + prime + ", " + prime2);
			}
		}
	}

}

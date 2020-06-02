package hr.fer.zemris.java.hw05.observer1;

/**
 * Writes to the standard output double value of value stored in integer storage
 * each time value changed in storage for the first n times since registration
 * to integer storage. After that storage deregisters this observer.
 * 
 * @author Mihael Jaić
 *
 */

public class DoubleValue implements IntegerStorageObserver {
	/**
	 * How many times it will print double the value of storage.
	 */
	private int n;

	/**
	 * Gets number of times this observer will do it's action when storage's
	 * value changes.
	 * 
	 * @param n
	 *            Number of times this observer will do it's action when
	 *            storage's value changes.
	 * @throws IllegalArgumentException
	 *             If n isn't positive.
	 */

	public DoubleValue(int n) throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException("n has to be positive.");
		}

		this.n = n;
	}

	/**
	 * Gets n current value.
	 * 
	 * @return n current value.
	 */

	public int getN() {
		return n;
	}

	@Override
	public void valueChanged(IntegerStorage istorage) {
		if (istorage == null) {
			throw new IllegalArgumentException("Storage can't be null.");
		}

		System.out.printf("Double value: %d%n", istorage.getValue() * 2);

		n--;
	}

}

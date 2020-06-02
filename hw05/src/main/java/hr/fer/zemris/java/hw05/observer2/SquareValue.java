package hr.fer.zemris.java.hw05.observer2;

/**
 * Writes a square of the integer stored in the IntegerStorage to the standard
 * output (but the stored integer itself is not modified!) whenever value
 * changes in integer storage.
 * 
 * @author Mihael Jaić
 *
 */

public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		if (istorage == null) {
			throw new IllegalArgumentException("Storage change can't be null.");
		}

		int value = istorage.getNewValue();
		System.out.printf("Provided new value: %d, square is %d%n", value, value * value);
	}

}

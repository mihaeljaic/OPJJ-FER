package hr.fer.zemris.java.hw05.observer1;

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
	public void valueChanged(IntegerStorage istorage) {
		if (istorage == null) {
			throw new IllegalArgumentException("Storage can't be null.");
		}

		int value = istorage.getValue();
		System.out.printf("Provided new value: %d, square is %d%n", value, value * value);
	}

}

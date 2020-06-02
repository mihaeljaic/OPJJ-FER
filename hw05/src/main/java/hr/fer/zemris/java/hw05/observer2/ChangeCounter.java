package hr.fer.zemris.java.hw05.observer2;

/**
 * Counts (and writes to the standard output) the number of times the value
 * stored has been changed since the registration to storage each time value
 * changed in storage.
 * 
 * @author Mihael Jaić
 *
 */

public class ChangeCounter implements IntegerStorageObserver {
	/**
	 * Counter that counts how many times value changed.
	 */
	private int counter;
	
	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		if (istorage == null) {
			throw new IllegalArgumentException("Storage change can't be null.");
		}
		
		System.out.printf("Number of values changes since tracking: %d%n", ++counter);
	}

}

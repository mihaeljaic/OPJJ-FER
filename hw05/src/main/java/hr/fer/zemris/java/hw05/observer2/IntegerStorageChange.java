package hr.fer.zemris.java.hw05.observer2;

/**
 * Class that stores information about storage when value in storage changes.
 * This information is used by observers.
 * 
 * @author Mihael Jaić
 *
 */

public class IntegerStorageChange {
	/**
	 * Storage.
	 */
	private final IntegerStorage storage;
	/**
	 * Value before change
	 */
	private final int valueBefore;
	/**
	 * New value of storage.
	 */
	private final int newValue;

	/**
	 * Sets all attributes.
	 * 
	 * @param storage
	 *            Storage.
	 * @param valueBefore
	 *            Value before.
	 * @param newValue
	 *            New value.
	 * @throws IllegalArgumentException
	 *             If storage is null.
	 */

	public IntegerStorageChange(IntegerStorage storage, int valueBefore, int newValue) throws IllegalArgumentException {
		super();
		if (storage == null) {
			throw new IllegalArgumentException("Storage can't be null.");
		}

		this.storage = storage;
		this.valueBefore = valueBefore;
		this.newValue = newValue;
	}

	/**
	 * Gets reference to storage.
	 * 
	 * @return Storage.
	 */

	public IntegerStorage getStorage() {
		return storage;
	}

	/**
	 * Gets value before change.
	 * 
	 * @return Value before change.
	 */

	public int getValueBefore() {
		return valueBefore;
	}

	/**
	 * Gets new value.
	 * 
	 * @return New value.
	 */

	public int getNewValue() {
		return newValue;
	}
}

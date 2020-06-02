package hr.fer.zemris.java.hw05.observer1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that stores integer value. If value changes all observers are informed
 * about value changed.
 * 
 * @author Mihael Jaić
 *
 */

public class IntegerStorage {
	/**
	 * Value.
	 */
	private int value;
	/**
	 * List of observers.
	 */
	private List<IntegerStorageObserver> observers;

	/**
	 * Sets initial value of storage.
	 * 
	 * @param initialValue
	 *            Initial value.
	 */

	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Adds observer to list of observers of this storage. If observer is
	 * already in list it is not added. null values are not allowed.
	 * 
	 * @param observer
	 *            Observer
	 * @throws IllegalArgumentException
	 *             If observer is null.
	 */

	public void addObserver(IntegerStorageObserver observer) throws IllegalArgumentException {
		if (observer == null) {
			throw new IllegalArgumentException("null values are not allowed.");
		}

		if (observers == null) {
			observers = new ArrayList<>();
		}

		if (observers.contains(observer)) {
			return;
		}

		observers.add(observer);
	}

	/**
	 * Removes observer from list of observers of storage. If observer is null
	 * or observer is not in list nothing is done.
	 * 
	 * @param observer
	 *            Observer.
	 */

	public void removeObserver(IntegerStorageObserver observer) {
		if (observer == null || observers == null || (!observers.contains(observer))) {
			return;
		}

		observers.remove(observer);
	}

	/**
	 * Removes all observers from storage.
	 */

	public void clearObservers() {
		if (observers != null) {
			observers.clear();
		}
	}

	/**
	 * Gets value.
	 * 
	 * @return Value.
	 */

	public int getValue() {
		return value;
	}

	/**
	 * Sets new value to storage. All observers are informed by change. Removes
	 * all DoubleValue observers who have reached n value changes.
	 * 
	 * @param value	New value.
	 */

	public void setValue(int value) {
		// Only if new value is different than the current value:
		if (this.value != value) {
			// Update current value
			this.value = value;
			// Notify all registered observers
			if (observers != null) {
				for (Iterator<IntegerStorageObserver> it = observers.iterator(); it.hasNext();) {
					IntegerStorageObserver obs = it.next();
					obs.valueChanged(this);

					if (obs instanceof DoubleValue && ((DoubleValue) obs).getN() == 0) {
						it.remove();
					}
				}

			}
		}
	}

}

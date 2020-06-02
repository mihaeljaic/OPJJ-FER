package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * List model for storing prime numbers. To generate next prime number in list
 * call method next.
 * 
 * @author Mihael Jaić
 *
 */

public class PrimListModel implements ListModel<Integer> {
	/**
	 * Listeners.
	 */
	private List<ListDataListener> listeners = new ArrayList<>();
	/**
	 * Prime numbers.
	 */
	private List<Integer> primNumbers;

	/**
	 * Adds one as first prime number.
	 */

	public PrimListModel() {
		primNumbers = new ArrayList<>();
		primNumbers.add(1);
	}

	@Override
	public int getSize() {
		return primNumbers.size();
	}

	@Override
	public Integer getElementAt(int index) {
		if (index < 0 || index > primNumbers.size() - 1) {
			throw new IllegalArgumentException();
		}

		return primNumbers.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		if (listeners.contains(l)) {
			return;
		}

		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	/**
	 * Calculates next prime number and informs all observers about the change.
	 */

	public void next() {
		int last = primNumbers.get(primNumbers.size() - 1);
		boolean isPrime = false;
		while (!isPrime) {
			last++;
			double sqrt = Math.sqrt(last);
			isPrime = true;
			for (int i = 2; i <= sqrt; i++) {
				if (last % i == 0) {
					isPrime = false;
					break;
				}
			}
		}

		primNumbers.add(last);

		int index = primNumbers.size() - 1;

		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index);
		for (ListDataListener listener : listeners) {
			listener.intervalAdded(event);
		}
	}
}

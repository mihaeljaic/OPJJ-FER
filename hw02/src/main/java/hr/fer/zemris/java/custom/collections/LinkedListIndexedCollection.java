package hr.fer.zemris.java.custom.collections;

/**
 * This class is implementation of linked list-backed collection of objects.
 * Duplicate elements are allowed. Storage of null references is not allowed.
 * 
 * @author Mihael Jaić
 *
 */

public class LinkedListIndexedCollection extends Collection {

	/**
	 * Structure of node in the linked list.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class ListNode {
		/**
		 * Reference to previous node in list.
		 */
		private ListNode previous;
		/**
		 * Reference to next node in ist.
		 */
		private ListNode next;
		/**
		 * Value of node.
		 */
		private Object value;

		/**
		 * Constructor that sets value of node.
		 * 
		 * @param value
		 *            Value of node.
		 */

		private ListNode(Object value) {
			this.value = value;
		}

	}

	/**
	 * Size of collection.
	 */
	private int size;
	/**
	 * Reference to first node in the list.
	 */
	private ListNode first;
	/**
	 * Reference to last node in the list.
	 */
	private ListNode last;

	/**
	 * Constructor that does nothing.
	 */

	public LinkedListIndexedCollection() {

	}

	/**
	 * Constructor that adds all elements from other collection to this
	 * collection.
	 * 
	 * @param other
	 *            Reference to other collection.
	 * @throws IllegalArgumentException
	 *             If other isn't instance of Collection.
	 */

	public LinkedListIndexedCollection(Collection other) throws IllegalArgumentException {
		if (!(other instanceof Collection)) {
			throw new IllegalArgumentException("other isn't instance of Collection class.");
		}

		addAll(other);
	}

	@Override
	public void add(Object value) throws IllegalArgumentException {
		if (value == null) {
			throw new IllegalArgumentException("Adding null values isn't allowed.");
		}

		ListNode newNode = new ListNode(value);

		if (first == null) {
			first = newNode;

		} else {
			last.next = newNode;
			newNode.previous = last;
		}

		last = newNode;
		size++;
	}

	/**
	 * Gets element at index in collection. Index has to be inside collection
	 * [0, size - 1].
	 * 
	 * @param index
	 *            Index of element in collection.
	 * @return Reference to element.
	 * @throws IndexOutOfBoundsException
	 *             If index is out of collection.
	 */

	public Object get(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index has to be in interval [0, size-1]");
		}

		return getNode(index).value;
	}

	/**
	 * Returns reference to node at given index.
	 * 
	 * @param index
	 *            Index of element in collection.
	 * @return Reference to node at index.
	 */

	private ListNode getNode(int index) {
		if (index + 1 > size / 2) {
			// Starts from the back of the list.
			ListNode temp = last;

			for (int i = size - 1; i > index; i--) {
				temp = temp.previous;
			}
			return temp;

		} else {
			// Starts from the first node in the list.
			ListNode temp = first;

			for (int i = 0; i < index; i++) {
				temp = temp.next;
			}
			return temp;
		}
	}

	@Override
	public void clear() {
		first = null;
		last = null;
		size = 0;
	}

	/**
	 * Inserts element in collection at given position. Element that was at
	 * given position and elements that are right of position are shifted one
	 * space to the right. Position has to be in interval [0, size].
	 * 
	 * @param value
	 *            Reference to element
	 * @param index
	 *            Position where new element will be inserted.
	 * @throws IndexOutOfBoundsException
	 *             If position isn't in interval[0, size - 1].
	 * @throws IllegalArgumentException
	 *             If value is reference to null.
	 */

	public void insert(Object value, int index) throws IllegalArgumentException, IndexOutOfBoundsException {
		if (value == null) {
			throw new IllegalArgumentException("Adding null values isn't allowed.");
		}

		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Index has to be in interval [0, size]");
		}

		if (index == size || size == 0) {
			// Inserts node at end of the list.
			add(value);
			return;

		} else if (index == 0) {
			// Inserts node at start of the list.
			ListNode newNode = new ListNode(value);
			newNode.next = first;
			first.previous = newNode;
			first = newNode;

		} else {
			// Inserts node in between nodes at position (index - 1) and
			// position (index).
			ListNode rightTemp = getNode(index);

			ListNode leftTemp = rightTemp.previous;

			ListNode newNode = new ListNode(value);

			leftTemp.next = newNode;
			newNode.previous = leftTemp;
			newNode.next = rightTemp;
			rightTemp.previous = newNode;
		}

		size++;

	}

	/**
	 * Returns index of first element that equals to value. If there is no
	 * searched object -1 is returned instead.
	 * 
	 * @param value
	 *            Reference to searched element
	 * @return Index of first appearance of element. -1 if there is no element
	 *         in collection.
	 */

	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}

		ListNode temp = first;
		int index = 0;

		while (temp != null) {
			if (temp.value.equals(value)) {
				return index;
			}

			index++;
			temp = temp.next;
		}

		return -1;
	}

	/**
	 * Removes element at given index. All elements that were right of index are
	 * moved one place to the left. Index has to be in interval [0, size - 1].
	 * 
	 * @param index
	 *            Index of element that will be removed.
	 * @throws IndexOutOfBoundsException
	 *             If index is out of array.
	 */

	public void remove(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index has to be in interval [0, size-1]");
		}

		// If there is only one element in the list.
		if (first == last) {
			first = null;
			last = null;

		} else if (index == 0) {
			first.next.previous = null;
			first = first.next;

		} else if (index == size - 1) {
			last.previous.next = null;
			last = last.previous;

		} else {
			ListNode temp = getNode(index);

			temp.previous.next = temp.next;
			temp.next.previous = temp.previous;
		}

		size--;
	}

	@Override
	public void forEach(Processor processor) throws IllegalArgumentException {
		if (processor == null) {
			throw new IllegalArgumentException("Argument isn't instance of processor.");
		}

		ListNode temp = first;

		while (temp != null) {
			processor.process(temp.value);
			temp = temp.next;
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}

	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);

		if (index == -1) {
			return false;
		}

		remove(index);
		return true;
	}

	@Override
	public Object[] toArray() throws UnsupportedOperationException {
		if (size == 0) {
			throw new UnsupportedOperationException("List is empty.");
		}

		Object[] returnArray = new Object[size];

		ListNode temp = first;

		for (int i = 0; i < size; i++) {
			returnArray[i] = temp.value;
			temp = temp.next;
		}

		return returnArray;
	}

}

package hr.fer.zemris.java.hw04.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Hash table structure that stores ordered pair key and value. Collisions in
 * entrys are solved by using linked list. It offers standard collection methods
 * of adding, removing, searching, etc. Average time complexity for adding,
 * removing or searching by key is O(1). Elements with null keys are not allowed
 * while values can be null.
 * 
 * @author Mihael Jaić
 *
 * @param <K>
 *            Key.
 * @param <V>
 *            Value.
 */

public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	/**
	 * Node in table entry list.
	 * 
	 * @author Mihael Jaić
	 *
	 * @param <K>
	 *            Key.
	 * @param <V>
	 *            Value.
	 */

	public static class TableEntry<K, V> {
		/**
		 * Key.
		 */
		private K key;
		/**
		 * Value.
		 */
		private V value;
		/**
		 * Reference to next element in list.
		 */
		private TableEntry<K, V> next;

		/**
		 * Sets attributes values.
		 * 
		 * @param key
		 *            Key.
		 * @param value
		 *            Value.
		 * @param next
		 *            Reference to next element in list.
		 * @throws IllegalArgumentException
		 *             If key is null.
		 */

		public TableEntry(K key, V value, TableEntry<K, V> next) throws IllegalArgumentException {
			if (key == null) {
				throw new IllegalArgumentException("Key can't be null.");
			}

			this.key = key;
			this.value = value;
			this.next = next;
		}

		/**
		 * Gets key of node.
		 * 
		 * @return Key.
		 */

		public K getKey() {
			return key;
		}

		/**
		 * Gets value of node.
		 * 
		 * @return Value.
		 */

		public V getValue() {
			return value;
		}

		/**
		 * Sets value of node.
		 * 
		 * @param value
		 *            Value.
		 */

		public void setValue(V value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return key.toString() + "=" + value.toString();
		}
	}

	/**
	 * Number of elements in table.
	 */
	private int size;
	/**
	 * Hash table.
	 */
	private TableEntry<K, V>[] table;
	/**
	 * Current size of table.
	 */
	private int capacity;
	/**
	 * Default table size.
	 */
	private static final int DEFAULT_CAPACITY = 16;
	/**
	 * When ratio between number of elements and table capacity is bigger or
	 * equal than this factor, table is resized.
	 */
	private static final double LOAD_FACTOR = 0.75;
	/**
	 * Number of modifications done in table since creating it.
	 */
	private int modificationCount;

	/**
	 * Constructor that makes table of default size;
	 */

	@SuppressWarnings("unchecked")
	public SimpleHashtable() {
		capacity = DEFAULT_CAPACITY;
		table = (TableEntry<K, V>[]) new TableEntry[capacity];
	}

	/**
	 * Constructor that makes table of size that is first number bigger or equal
	 * than argument that is power of 2. Capacity has to be positive.
	 * 
	 * @param capacity
	 *            Initial capacity.
	 * @throws IllegalArgumentException
	 *             If capacity is less than 1.
	 */

	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) throws IllegalArgumentException {
		if (capacity < 1) {
			throw new IllegalArgumentException("Capacity has to be positive.");
		}

		for (this.capacity = 1; this.capacity < capacity; this.capacity *= 2);

		table = (TableEntry<K, V>[]) new TableEntry[this.capacity];
	}

	/**
	 * Hash function that calculates index of element by its key.
	 * 
	 * @param key
	 *            Key
	 * @return Index in table.
	 */

	private int hashFunction(Object key) {
		return Math.abs(key.hashCode()) % capacity;
	}

	/**
	 * Adds element into table. Index in table is calculated using hash
	 * function. If element already exists than it is not added but value is
	 * changed to new value instead.
	 * 
	 * @param key
	 *            Key
	 * @param value
	 *            Value.
	 * @throws IllegalArgumentException
	 *             If key is null.
	 */

	public void put(K key, V value) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key can't be null.");
		}

		TableEntry<K, V> temp = getEntry(key);
		if (temp != null) {
			// Key already exists so value is only changed.
			temp.value = value;
			return;
		}

		addToTable(new TableEntry<K, V>(key, value, null), table);

		size++;
		modificationCount++;
		if ((double) size / capacity >= LOAD_FACTOR) {
			resizeTable();
		}
	}

	/**
	 * Adds element to table given in argument.
	 * 
	 * @param entry
	 *            Element
	 * @param table
	 *            Table.
	 */

	private void addToTable(TableEntry<K, V> entry, TableEntry<K, V>[] table) {
		int slot = hashFunction(entry.key);
		TableEntry<K, V> temp = table[slot];

		if (temp == null) {
			table[slot] = entry;
			return;
		}

		while (temp.next != null) {
			temp = temp.next;
		}

		temp.next = entry;
	}

	/**
	 * Doubles the size of table. All elements from previous table are rehashed
	 * to new table.
	 */

	@SuppressWarnings("unchecked")
	private void resizeTable() {
		capacity *= 2;
		TableEntry<K, V>[] newTable = (TableEntry<K, V>[]) new TableEntry[capacity];

		for (int i = 0; i < table.length; i++) {
			for (TableEntry<K, V> temp = table[i]; temp != null;) {
				TableEntry<K, V> next = temp.next;
				temp.next = null;

				addToTable(temp, newTable);

				temp = next;
			}
		}

		table = newTable;
		modificationCount++;
	}

	/**
	 * Removes all elements from table.
	 */

	public void clear() {
		for (int i = 0; i < capacity; i++) {
			table[i] = null;
		}

		size = 0;
		modificationCount++;
	}

	/**
	 * Checks if element with key is in table.
	 * 
	 * @param key
	 *            Key.
	 * @return True if element with key is in the table, false otherwise.
	 */

	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}

		return getEntry(key) != null;
	}

	/**
	 * Checks if there is element with given value in table.
	 * 
	 * @param value
	 *            Value.
	 * @return True if element with given value exists in table, false
	 *         otherwise.
	 */

	public boolean containsValue(Object value) {
		if (value == null) {
			return false;
		}

		for (int i = 0; i < capacity; i++) {
			TableEntry<K, V> temp = table[i];

			while (temp != null) {
				if (temp.value.equals(value)) {
					return true;
				}

				temp = temp.next;
			}
		}

		return false;
	}

	/**
	 * Gets reference to entry that has given key. If element with given key
	 * doesn't exist null is returned.
	 * 
	 * @param key
	 *            Key.
	 * @return Element in table that has given key.
	 */

	private TableEntry<K, V> getEntry(Object key) {
		int slot = hashFunction(key);
		TableEntry<K, V> temp = table[slot];

		while (temp != null) {
			if (temp.key.equals(key)) {
				return temp;
			}
			temp = temp.next;
		}

		return null;
	}

	/**
	 * Gets value of element with given key. If element doesn't exist in table
	 * null is returned.
	 * 
	 * @param key
	 *            Key.
	 * @return Value of element with given key.
	 */

	public V get(Object key) {
		if (key == null) {
			return null;
		}

		TableEntry<K, V> temp = getEntry(key);

		return temp == null ? null : temp.value;
	}

	/**
	 * Returns number of elements in table.
	 * 
	 * @return Number of elements in table.
	 */

	public int size() {
		return size;
	}

	/**
	 * Checks if there is no elements in table.
	 * 
	 * @return True if there are no elements in table, false otherwise.
	 */

	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Removes element with given key. If there is no such element nothing is
	 * done.
	 * 
	 * @param key
	 *            Key.
	 */

	public void remove(Object key) {
		if (key == null || (!containsKey(key))) {
			return;
		}

		int slot = hashFunction(key);

		// If element is at start of list.
		if (table[slot].key.equals(key)) {
			table[slot] = table[slot].next;

		} else {
			TableEntry<K, V> temp = table[slot];
			while (!temp.next.key.equals(key)) {
				temp = temp.next;
			}

			temp.next = temp.next.next;
		}

		size--;
		modificationCount++;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int noOfVisited = 0;

		sb.append("[");
		for (int i = 0; i < capacity && noOfVisited < size; i++) {
			TableEntry<K, V> temp = table[i];

			while (temp != null) {
				sb.append(temp);
				noOfVisited++;

				if (noOfVisited < size) {
					sb.append(", ");
				}

				temp = temp.next;
			}
		}
		sb.append("]");

		return sb.toString();
	}

	@Override
	public Iterator<SimpleHashtable.TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Iterator for hash table.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		/**
		 * Current element in table.
		 */
		private int position;
		/**
		 * Reference to last element.
		 */
		private TableEntry<K, V> last;
		/**
		 * Reference to element before last.
		 */
		private TableEntry<K, V> beforeLast;
		/**
		 * Number of modificiations of table that were when iterator was created
		 * plus eventual modifications while iterating.
		 */
		private int modCountWhileIterating;

		/**
		 * Constructor that gets current modification count of hash table.
		 */

		private IteratorImpl() {
			modCountWhileIterating = modificationCount;
		}

		@Override
		public boolean hasNext() throws ConcurrentModificationException {
			if (modCountWhileIterating != modificationCount) {
				throw new ConcurrentModificationException("Hash table has changed while iterating.");
			}

			return position < size;
		}

		@Override
		public SimpleHashtable.TableEntry<K, V> next() throws NoSuchElementException, ConcurrentModificationException {
			if (modCountWhileIterating != modificationCount) {
				throw new ConcurrentModificationException("Hash table has changed while iterating.");
			}

			if (position == size) {
				throw new NoSuchElementException("No more elements in collection.");
			}

			beforeLast = last;

			// If no elements were generated yet. First entry that has element
			// is searched.
			if (last == null) {
				int i = 0;
				for (; table[i] == null; i++);
				last = table[i];
			
			// If there is more elements in current entry list.
			} else if (last.next != null) {
				last = last.next;

			} else {
				int slot = hashFunction(last.key) + 1;
				while (table[slot] == null)
					slot++;
				last = table[slot];

			}

			position++;
			return last;
		}

		@Override
		public void remove() throws IllegalStateException, ConcurrentModificationException {
			if (modCountWhileIterating != modificationCount) {
				throw new ConcurrentModificationException("Hash table has changed while iterating.");
			}

			if (last == beforeLast) {
				throw new IllegalStateException("Remove was used twice in a row or no elements were iterated yet.");
			}

			SimpleHashtable.this.remove(last.key);

			last = beforeLast;
			position--;
			modCountWhileIterating++;
		}
	}

}

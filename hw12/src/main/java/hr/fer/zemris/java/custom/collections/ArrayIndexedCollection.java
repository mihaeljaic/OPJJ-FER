package hr.fer.zemris.java.custom.collections;


/**
 * Class is implementation of resizable array-backed collection of objects.
 * Duplicate elements are allowed. Storage of null references is not allowed. 
 * 
 * @author Mihael Jaić
 *
 */

public class ArrayIndexedCollection extends Collection {
	/**
	 * Default capacity of array of elements in collection.
	 */
	private static final int DEFAULT_CAPACITY = 16;
	/**
	 * Size of collection.
	 */
	private int size;
	/**
	 * Capacity of collection.
	 */
	private int capacity;
	/**
	 * Reference to array of elements.
	 */
	private Object[] elements;
	
	/**
	 * Constructor that allocates array of default capacity.
	 */

	public ArrayIndexedCollection() {
		capacity = DEFAULT_CAPACITY;
		elements = new Object[capacity];
	}
	
	/**
	 * Constructor that allocates array to initial capacity defined by user.
	 * Initial capacity has to be greater than 0.
	 * 
	 * @param initialCapacity	Initial capacity of collection.
	 * @throws IllegalArgumentException  If initial capacity is less than 1.
	 */

	public ArrayIndexedCollection(int initialCapacity) throws IllegalArgumentException {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("Initial capacity has to be greater than 1.");
		}

		capacity = initialCapacity;
		elements = new Object[capacity];
	}
	
	/**
	 * Constructor that sets capacity to default and adds all elements from other collection to this collection.
	 * 
	 * @param other		Reference to other collection
	 * @throws IllegalArgumentException	If other isn't reference to collection.
	 */

	public ArrayIndexedCollection(Collection other) throws IllegalArgumentException {
		this();
		
		if (!(other instanceof Collection)) {
			throw new IllegalArgumentException("other has to be reference to Collection.");
		}

		addAll(other);
	}
	
	/**
	 * Constructor that sets capacity to initial capacity and adds all elements from other collection
	 * to this collection. Initial capacity has to be greater than 0.
	 * 
	 * @param other		Reference to other collection.
	 * @param initialCapacity	Initial capacity of collection.
	 * @throws IllegalArgumentException	If other isn't reference to Collection or initial capacity is less than 1.
	 */

	public ArrayIndexedCollection(Collection other, int initialCapacity) throws IllegalArgumentException {
		this(initialCapacity);

		if (!(other instanceof Collection)) {
			throw new IllegalArgumentException("other has to be reference to Collection.");
		}

		addAll(other);
	}

	@Override
	public void add(Object value) throws IllegalArgumentException {
		if (value == null) {
			throw new IllegalArgumentException("Null values are not allowed.");
		}

		if (size == capacity) {
			resizeArray();
		}

		elements[size] = value;
		size++;
	}
	
	/**
	 * Gets element at index in collection. Index has to be inside collection
	 * [0, size - 1].
	 * 
	 * @param index	Index of element in collection.
	 * @return	Reference to element.
	 * @throws IndexOutOfBoundsException	If index is out of collection.
	 */

	public Object get(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index is not in specified interval.");
		}

		return elements[index];
	}

	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}

		size = 0;
	}
	
	/**
	 * Doubles the size of array.
	 */
	
	private void resizeArray() {
		Object[] temp = elements;
		capacity *= 2;
		elements = new Object[capacity];

		for (int i = 0; i < size; i++) {
			elements[i] = temp[i];
		}
	}
	 
	/**
	 * Inserts element in collection at given position. Element that was at given position and elements
	 * that are right of position are shifted one space to the right. Position has to be in
	 * interval [0, size - 1]. 
	 * 
	 * @param value		Reference to element
	 * @param position	Position where new element will be inserted.
	 * @throws IndexOutOfBoundsException	If position isn't in interval[0, size - 1].
	 * @throws IllegalArgumentException		If value is reference to null.
	 */

	public void insert(Object value, int position) throws IndexOutOfBoundsException, IllegalArgumentException {
		if (value == null) {
			throw new IllegalArgumentException("Null values are not allowed.");
		}

		if (position < 0 || position >= size) {
			throw new IndexOutOfBoundsException("Index is not in specified interval.");
		}

		if (size == capacity) {
			resizeArray();
		}

		// Moves all elements that are right to position or at position by 1
		// place to right.
		for (int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}
		elements[position] = value;
		size++;
	}
	
	/**
	 * Returns index of first element that equals to value. If there is no searched object
	 * -1 is returned instead.
	 * 
	 * @param value	Reference to searched element
	 * @return	Index of first appearance of element. -1 if there is no element in collection.
	 */

	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}
		
		for (int i = 0; i < size; i++) {
			if (value.equals(elements[i])) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Removes element at given index. All elements that were right of 
	 * index are moved one place to left. Index has to be in interval [0, size - 1].
	 * 
	 * @param index		Index of element that will be removed.
	 * @throws IndexOutOfBoundsException	If index is out of array.
	 */

	public void remove(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index is not in specified interval.");
		}
		
		// Moves all elements that are right of index one place to the left.
		for (int i = index; i + 1 < size; i++) {
			elements[i] = elements[i + 1];
		}
		size--;
	}
	
	
	@Override
	public void forEach(Processor processor) throws IllegalArgumentException {
		if (!(processor instanceof Processor)) {
			throw new IllegalArgumentException("processor isn't instance of Processor class.");
		}

		for (int i = 0; i < size; i++) {
			processor.process(elements[i]);
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
			throw new UnsupportedOperationException("Collection is empty.");
		}

		Object[] returnArray = new Object[size];

		for (int i = 0; i < size; i++) {
			returnArray[i] = elements[i];
		}
		return returnArray;
	}

}

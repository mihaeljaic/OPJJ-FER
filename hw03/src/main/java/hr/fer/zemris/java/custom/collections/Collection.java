package hr.fer.zemris.java.custom.collections;

/**
 * Class represents general collection of objects.
 * 
 * @author Mihael Jaić
 *
 */

public class Collection {

	/**
	 * Collection constructor.
	 */

	protected Collection() {

	}

	/**
	 * Checks if collection is empty.
	 * 
	 * @return True if collection is empty, false otherwise.
	 */

	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Gets size of collection. Method is expected to be overriden by extending
	 * class.
	 * 
	 * @return Size of collection.
	 */

	public int size() {
		return 0;
	}

	/**
	 * Adds element to collection. Method is expected to be overriden by
	 * extending class.
	 * 
	 * @param value
	 *            element that is added to collection
	 */

	public void add(Object value) {

	}

	/**
	 * Checks if element is in the collection. Method is expected to be
	 * overriden by extending class.
	 * 
	 * @param value
	 *            Element that is searched for.
	 * @return True if element is in the collection, false otherwise.
	 */

	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Removes element with value of Object from collection. If there are
	 * multiple Objects with same value only one will be removed. Method is
	 * expected to be overriden by extending class.
	 * 
	 * @param value
	 *            Value of element
	 * @return True if element was removed, false otherwise.
	 */

	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Generates new array of Object elements that represents collection. Method
	 * is expected to be overriden by extending class.
	 * 
	 * @return Array of Objects from collection
	 * @throws UnsupportedOperationException
	 *             If collection is empty.
	 */

	public Object[] toArray() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Walks through collection and processes each element. Method is expected
	 * to be overriden by extending class.
	 * 
	 * @param processor
	 *            Reference to processor.
	 * @throws IllegalArgumentException
	 *             If processor isn't instance of Processor class.
	 */

	public void forEach(Processor processor) throws IllegalArgumentException {

	}

	/**
	 * Adds all elements from other collection to this collection.
	 * 
	 * @param other
	 *            Reference to other collection
	 * @throws IllegalArgumentException
	 *             If other isn't instance of collection
	 */

	public void addAll(Collection other) throws IllegalArgumentException {
		if (!(other instanceof Collection)) {
			throw new IllegalArgumentException("other isn't reference to Collection class.");
		}

		class LocalProcessor extends Processor {
			@Override
			public void process(Object value) {
				add(value);
			}
		}

		other.forEach(new LocalProcessor());

	}

	/**
	 * Removes all elements from the collection. Method is expected to be
	 * overriden by extending class.
	 */

	public void clear() {

	}

}

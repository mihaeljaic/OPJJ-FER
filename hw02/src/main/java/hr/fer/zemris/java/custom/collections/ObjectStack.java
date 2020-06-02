package hr.fer.zemris.java.custom.collections;

/**
 * Class that respresents stack data structure. Offers standard stack
 * operations. Stack is implemented by ArrayIndexCollection.
 * 
 * @author Mihael Jaić
 *
 */

public class ObjectStack {

	/**
	 * Stack.
	 */
	private ArrayIndexedCollection stack;

	/**
	 * Constructor that creates stack.
	 */

	public ObjectStack() {
		stack = new ArrayIndexedCollection();
	}

	/**
	 * Checks if stack is empty.
	 * 
	 * @return True if stack is empty, false otherwise.
	 */

	public boolean isEmpty() {
		return stack.isEmpty();
	}

	/**
	 * Returns number of elements in stack.
	 * 
	 * @return Number of elements in stack.
	 */

	public int size() {
		return stack.size();
	}

	/**
	 * Adds element to top of the stack.
	 * Null values are not allowed.
	 * 
	 * @param value
	 *            Element that is added to stack.
	 * @throws IllegalArgumentException
	 *             If element with null value is tried to add.
	 */

	public void push(Object value) throws IllegalArgumentException {
		stack.add(value);
	}

	/**
	 * Gets element at the top of the stack. Element is removed from the stack.
	 * Stack must not be empty,
	 * 
	 * @return Element at the top of the stack.
	 * @throws EmptyStackException
	 */

	public Object pop() throws EmptyStackException {
		Object value = peek();
		stack.remove(stack.size() - 1);

		return value;
	}

	/**
	 * Returns element at the top of the stack.
	 * Stack must not be empty.
	 * 
	 * @return Element at the top of the stack.
	 * @throws EmptyStackException
	 *             If stack is empty.
	 */

	public Object peek() throws EmptyStackException {
		try {
			return stack.get(stack.size() - 1);

		} catch (IndexOutOfBoundsException ex) {
			throw new EmptyStackException();
		}
	}

	/**
	 * Removes all elements from the stack.
	 */

	public void clear() {
		stack.clear();
	}

}

package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * Map-like collection that uses Strings as keys. Each key has its own virtual
 * stack. Stack contains ValueWrapper objects as values.
 * 
 * @author Mihael Jaić
 *
 */

public class ObjectMultistack {

	/**
	 * Class represents node in stack list.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class MultistackEntry {
		/**
		 * Valuewrapper object.
		 */
		private ValueWrapper valueWrapper;
		/**
		 * Reference to next node in stack list.
		 */
		private MultistackEntry next;

		/**
		 * Sets node attributes.
		 * 
		 * @param valueWrapper
		 *            Value.
		 * @param next
		 *            Reference to next node in stack list.
		 */

		private MultistackEntry(ValueWrapper valueWrapper, MultistackEntry next) {
			this.valueWrapper = valueWrapper;
			this.next = next;
		}
	}

	/**
	 * Map used to store keys with stack lists.
	 */
	private Map<String, MultistackEntry> map;

	/**
	 * Creates map.
	 */

	public ObjectMultistack() {
		super();
		map = new HashMap<>();
	}

	/**
	 * Pushes new value to stack of given key. Key or value can't be null.
	 * 
	 * @param name
	 *            Key
	 * @param valueWrapper
	 *            Value
	 * @throws IllegalArgumentException
	 *             If key or value is null.
	 */

	public void push(String name, ValueWrapper valueWrapper) throws IllegalArgumentException {
		if (name == null || valueWrapper == null) {
			throw new IllegalArgumentException("null values are not allowed.");
		}

		map.put(name, new MultistackEntry(valueWrapper, map.get(name)));
	}

	/**
	 * Gets value from top of stack for given key. Value is removed from stack
	 * afterwards.
	 * 
	 * @param name
	 *            Key.
	 * @return Value from top of stack for given key.
	 * @throws ObjectMultistackException
	 *             If stack for given key is empty.
	 */

	public ValueWrapper pop(String name) throws ObjectMultistackException {
		if (isEmpty(name)) {
			throw new ObjectMultistackException(String.format("Stack for key %s is empty.", name));
		}

		MultistackEntry temp = map.get(name);

		if (temp.next == null) {
			map.remove(name);
		} else {
			map.put(name, temp.next);
		}

		return temp.valueWrapper;
	}

	/**
	 * Gets first value in stack from given key.
	 * 
	 * @param name
	 *            Key.
	 * @return Value at top of stack for given key.
	 * @throws ObjectMultistackException
	 *             If stack is empty.
	 */

	public ValueWrapper peek(String name) throws ObjectMultistackException {
		if (isEmpty(name)) {
			throw new ObjectMultistackException(String.format("Stack for key %s is empty.", name));
		}

		return map.get(name).valueWrapper;
	}

	/**
	 * Checks if stack for given key is empty.
	 * 
	 * @param name
	 *            Key.
	 * @return True if stack for given key is empty, false otherwise.
	 */

	public boolean isEmpty(String name) {
		return name == null || (!map.containsKey(name));
	}
}

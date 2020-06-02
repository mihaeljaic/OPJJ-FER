package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that represents name of function. Name is set in constructor and
 * cannot be changed.
 * 
 * @author Mihael Jaić
 *
 */

public class ElementFunction extends Element {
	/**
	 * Function name.
	 */
	private final String name;

	/**
	 * Constructor that sets name of the function.
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 *             Name can't be null.
	 */

	public ElementFunction(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Name can't be null.");
		}
		this.name = name;
	}

	/**
	 * Gets name of the function.
	 * 
	 * @return Name of function.
	 */

	public String getName() {
		return name;
	}

	@Override
	public String asText() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ElementFunction)) {
			return false;
		}

		ElementFunction other = (ElementFunction) obj;
		return this.name.equals(other.getName());
	}
}

package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that stores name of variable.
 * 
 * @author Mihael Jaić
 *
 */

public class ElementVariable extends Element {
	/**
	 * Name of variable.
	 */
	private final String name;

	/**
	 * Constructor that sets variable name.
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 *             Name can't be null.
	 */

	public ElementVariable(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Name can't be null.");
		}
		this.name = name;
	}

	/**
	 * Gets variable name.
	 * 
	 * @return Variable name.
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
		if (!(obj instanceof ElementVariable)) {
			return false;
		}

		ElementVariable other = (ElementVariable) obj;
		return this.name.equals(other.getName());
	}
}

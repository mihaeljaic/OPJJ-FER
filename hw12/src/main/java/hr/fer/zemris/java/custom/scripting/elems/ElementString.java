package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that holds value of constant string.
 * 
 * @author Mihael Jaić
 *
 */

public class ElementString extends Element {
	/**
	 * Constant string.
	 */
	private final String value;

	/**
	 * Constructor that sets string value.
	 * 
	 * @param value
	 * @throws IllegalArgumentException
	 *             Value can't be null.
	 */

	public ElementString(String value) throws IllegalArgumentException {
		if (value == null) {
			throw new IllegalArgumentException("Value can't be null.");
		}
		this.value = value;
	}

	/**
	 * Gets value of element.
	 * 
	 * @return Constant string.
	 */

	public String getValue() {
		return value;
	}

	@Override
	public String asText() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ElementString)) {
			return false;
		}

		ElementString other = (ElementString) obj;
		return this.value.equals(other.getValue());
	}

}

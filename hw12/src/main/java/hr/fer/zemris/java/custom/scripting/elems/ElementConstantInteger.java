package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that has read-only property of type integer as value.
 * 
 * @author Mihael Jaić
 *
 */

public class ElementConstantInteger extends Element {
	/**
	 * Value of element.
	 */
	private final int value;

	/**
	 * Constructor that sets value of element.
	 * 
	 * @param value
	 */

	public ElementConstantInteger(int value) {
		this.value = value;
	}

	/**
	 * Gets value of element.
	 * 
	 * @return Value of element.
	 */

	public int getValue() {
		return value;
	}

	@Override
	public String asText() {
		return Integer.toString(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ElementConstantInteger)) {
			return false;
		}

		ElementConstantInteger other = (ElementConstantInteger) obj;
		return this.value == other.getValue();
	}
}

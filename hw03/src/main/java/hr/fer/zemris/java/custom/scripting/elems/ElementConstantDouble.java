package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that has read-only property of type double as value.
 * 
 * @author Mihael Jaić
 *
 */

public class ElementConstantDouble extends Element {
	/**
	 * Value of element.
	 */
	private final double value;
	/**
	 * Used for comparison of real numbers.
	 */
	private static final double EPSILON = 1e-6;

	/**
	 * Constructor which sets value of element
	 * 
	 * @param value
	 *            Value of element.
	 */

	public ElementConstantDouble(double value) {
		this.value = value;
	}

	/**
	 * Getter for value.
	 * 
	 * @return Value of element.
	 */

	public double getValue() {
		return value;
	}

	@Override
	public String asText() {
		return Double.toString(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ElementConstantDouble)) {
			return false;
		}

		ElementConstantDouble other = (ElementConstantDouble) obj;
		return Math.abs(this.value - other.getValue()) < EPSILON;
	}
}

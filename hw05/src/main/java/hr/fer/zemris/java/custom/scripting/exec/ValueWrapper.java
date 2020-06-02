package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * Value wrapper that stores object of any kind. But in case arithmetic or
 * comparison methods are called value and other object have to be either null,
 * String, Double or Integer type. If any object is null it is treated as
 * integer of value 0. If any object is String it is converted into number.
 * 
 * @author Mihael Jaić
 *
 */

public class ValueWrapper {
	/**
	 * Wrapped value.
	 */
	private Object value;

	/**
	 * Sets value.
	 * 
	 * @param value
	 *            Value.
	 */

	public ValueWrapper(Object value) {
		super();

		this.value = value;
	}

	/**
	 * Gets value.
	 * 
	 * @return Value.
	 */

	public Object getValue() {
		return value;
	}

	/**
	 * Sets value.
	 * 
	 * @param value
	 *            New value.
	 */

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Adds value with another object. Both value or other object have to be
	 * valid types. Result is stored in value variable. If value was Double or
	 * other object was Double before operation value is now of type Double. If
	 * both were Integers value is now Integer. Other object remains unchanged.
	 * 
	 * @param incValue
	 *            Number that adds to this value.
	 * @throws RuntimeException
	 *             If any of operands is invalid type.
	 */

	public void add(Object incValue) throws RuntimeException {
		Object other = convert(incValue);
		value = convert(value);

		value = performOperation(value, other, (a, b) -> a + b);
	}

	/**
	 * Subtracts value with another object. Both value or other object have to
	 * be valid types. Result is stored in value variable. If value was Double
	 * or other object was Double before operation value is now of type Double.
	 * If both were Integers value is now Integer. Other object remains
	 * unchanged.
	 * 
	 * @param decValue
	 *            Number that subtracts this value.
	 * @throws RuntimeException
	 *             If any of operands is invalid type.
	 */

	public void subtract(Object decValue) throws RuntimeException {
		Object other = convert(decValue);
		value = convert(value);

		value = performOperation(value, other, (a, b) -> a - b);
	}

	/**
	 * Mulitplies value with another object. Both value or other object have to
	 * be valid types. Result is stored in value variable. If value was Double
	 * or other object was Double before operation value is now of type Double.
	 * If both were Integers value is now Integer. Other object remains
	 * unchanged.
	 * 
	 * @param mulValue
	 *            Number that multiplies this value.
	 * @throws RuntimeException
	 *             If any of operands is invalid type.
	 */

	public void multiply(Object mulValue) throws RuntimeException {
		Object other = convert(mulValue);
		value = convert(value);

		value = performOperation(value, other, (a, b) -> a * b);
	}

	/**
	 * Divides value with another object. Both value or other object have to be
	 * valid types. Result is stored in value variable. If value was Double or
	 * other object was Double before operation value is now of type Double. If
	 * both were Integers value is now Integer. Other object can't be zero.
	 * Other object remains unchanged.
	 * 
	 * @param divValue
	 *            Number that divides this value.
	 * @throws RuntimeException
	 *             If any of operands is invalid type. Or if object is zero.
	 */

	public void divide(Object divValue) throws RuntimeException {
		Object other = convert(divValue);
		
		if (other instanceof Double && ((Double) other).compareTo(0.0) == 0
				|| (other instanceof Integer && (Integer) other == 0)) {
			throw new RuntimeException("Division by zero.");
		}
		
		value = convert(value);

		value = performOperation(value, other, (a, b) -> a / b);
	}

	/**
	 * Compares value with another number. Both value and other number have to
	 * be valid types. If wrapper value has lower value negative integer is
	 * returned. If wrapper value has higher value positive integer is returned.
	 * If numbers are equal zero is returned. Both object remain unchanged.
	 * 
	 * @param withValue
	 *            Compared number.
	 * @return Negative integer if value is lower than other number. Positive
	 *         integer if value is higher than other number. Zero if both are
	 *         equal.
	 * @throws RuntimeException
	 *             If any of numbers is invalid type.
	 */

	public int numCompare(Object withValue) throws RuntimeException {
		Object other = convert(withValue);
		// Doesn't change current value.
		Object tempValue = convert(value);

		Object temp = performOperation(tempValue, other, (a, b) -> a - b);

		if (temp instanceof Double) {
			return Double.compare((Double) temp, 0.0);
		}

		return Integer.compare((Integer) temp, 0);
	}

	/**
	 * Converts given object into number of type Double or Integer. If object is
	 * invalid type exception is thrown.
	 * 
	 * @param object
	 *            Object.
	 * @return Object transformed into number.
	 */

	private Object convert(Object object) {
		if (!validType(object)) {
			throw new RuntimeException(String.format("Operand %s is invalid type.", object));
		}

		if (object == null) {
			return new Integer(0);
		}

		if (object instanceof String) {
			try {
				return convertStringToNumber((String) object);

			} catch (NumberFormatException ex) {
				throw new RuntimeException(String.format("Could not convert %s to number.", object));
			}
		}

		// Object is of type Integer or Double.
		return object;
	}

	/**
	 * Performs given BiFunction operation on value and other Object. Result of
	 * operation is Double if any of operands is Double. If both operands are
	 * Integers result is Integer.
	 * 
	 * @param tempValue
	 *            Temporary value stored in wrapper.
	 * 
	 * @param other
	 *            Other Object.
	 * @param operation
	 *            Operation.
	 * @return Result of operation.
	 */

	private Object performOperation(Object tempValue, Object other, BiFunction<Double, Double, Double> operation) {
		if (tempValue instanceof Double && other instanceof Double) {
			return operation.apply((Double) tempValue, (Double) other);
		}

		if (tempValue instanceof Double && other instanceof Integer) {
			return operation.apply((Double) tempValue, Double.valueOf((Integer) other));
		}

		if (tempValue instanceof Integer && other instanceof Double) {
			return operation.apply(Double.valueOf((Integer) tempValue), (Double) other);
		}

		return operation.apply(Double.valueOf((Integer) tempValue), Double.valueOf((Integer) other)).intValue();
	}

	/**
	 * Checks if objects is valid type for this wrapper.
	 * 
	 * @param other
	 *            Tested object.
	 * @return True if object is valid type, false otherwise.
	 */

	private boolean validType(Object other) {
		return other == null || other instanceof Integer || other instanceof Double || other instanceof String;
	}

	/**
	 * If string contains decimal point or exponent sign it is tried to convert
	 * into double, otherwise it tries to conver into integer. If conversion
	 * fails exception is thrown.
	 * 
	 * @param s
	 *            Given string.
	 * @return Number of type Double or Integer.
	 */

	private Object convertStringToNumber(String s) {
		if (isDecimal(s)) {
			return Double.parseDouble(s);
		}

		return Integer.parseInt(s);
	}

	/**
	 * Checks if given string contains decimal point or exponent sign.
	 * 
	 * @param s
	 *            String
	 * @return True if given string could be decimal value, false otherwise.
	 */

	private boolean isDecimal(String s) {
		for (Character c : s.toCharArray()) {
			if (c == '.' || c == 'e' || c == 'E') {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		if (value == null) {
			return "";
		}

		return value.toString();
	}
}

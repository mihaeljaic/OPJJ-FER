package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that stores symbol of operation.
 * 
 * @author Mihael Jaić
 *
 */

public class ElementOperator extends Element {
	/**
	 * Symbol of operation.
	 */
	private final String symbol;

	/**
	 * Constructor that sets symbol value.
	 * 
	 * @param symbol
	 * @throws IllegalArgumentException
	 *             Symbol can't be null.
	 */

	public ElementOperator(String symbol) throws IllegalArgumentException {
		if (symbol == null) {
			throw new IllegalArgumentException("Symbol can't be null.");
		}
		this.symbol = symbol;
	}

	/**
	 * Returns symbol of operation.
	 * 
	 * @return Symbol.
	 */

	public String getSymbol() {
		return symbol;
	}

	@Override
	public String asText() {
		return symbol;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ElementOperator)) {
			return false;
		}

		ElementOperator other = (ElementOperator) obj;
		return this.symbol.equals(other.getSymbol());
	}
}

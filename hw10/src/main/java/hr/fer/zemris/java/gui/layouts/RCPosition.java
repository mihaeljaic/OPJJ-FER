package hr.fer.zemris.java.gui.layouts;

/**
 * Model for row and column numbers.
 * 
 * @author Mihael Jaić
 *
 */

public class RCPosition {
	/**
	 * Row number.
	 */
	private final int row;
	/**
	 * Column number.
	 */
	private final int column;

	/**
	 * Constructor that sets row and column numbers.
	 * 
	 * @param row
	 *            Row number.
	 * @param column
	 *            Column number.
	 */

	public RCPosition(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	/**
	 * Gets row number.
	 * 
	 * @return Row number.
	 */

	public int getRow() {
		return row;
	}

	/**
	 * Gets column number
	 * 
	 * @return Column number.
	 */

	public int getColumn() {
		return column;
	}
}

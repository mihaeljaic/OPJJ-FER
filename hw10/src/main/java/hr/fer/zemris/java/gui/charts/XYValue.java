package hr.fer.zemris.java.gui.charts;

/**
 * Structure with x and y coordinate as integer values.
 * 
 * @author Mihael Jaić
 *
 */

public class XYValue {
	/**
	 * X value.
	 */
	private int x;
	/**
	 * Y value.
	 */
	private int y;

	/**
	 * Constructor that sets x and y.
	 * 
	 * @param x
	 *            X value.
	 * @param y
	 *            Y value.
	 */

	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets y value.
	 * 
	 * @return Y value.
	 */

	public int getY() {
		return y;
	}

	/**
	 * Gets x value.
	 * 
	 * @return X value.
	 */

	public int getX() {
		return x;
	}
}

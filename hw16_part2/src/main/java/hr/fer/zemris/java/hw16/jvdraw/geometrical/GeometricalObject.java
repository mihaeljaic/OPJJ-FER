package hr.fer.zemris.java.hw16.jvdraw.geometrical;

import java.awt.Graphics;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw.BoundingBox;

/**
 * Class that models abstract geometrical objects that are drawn on canvas.
 * 
 * @author Mihael Jaić
 *
 */

public abstract class GeometricalObject {
	/**
	 * Gets bounding box of geometrical object.
	 * 
	 * @return Bounding box of geometrical object.
	 */
	public abstract BoundingBox getBoundingBox();

	/**
	 * Shows detailed information about geometrical object and offers user to
	 * modify it.
	 * 
	 * @return True if user changed properties. False otherwise.
	 */
	public abstract boolean showProperties();

	/**
	 * Paints itself on given graphics. Also applies translation in x and y
	 * axis.
	 * 
	 * @param g
	 *            Graphics.
	 * @param translationX
	 *            X axis translation.
	 * @param translationY
	 *            Y axis translation.
	 */
	public abstract void paint(Graphics g, int translationX, int translationY);

}

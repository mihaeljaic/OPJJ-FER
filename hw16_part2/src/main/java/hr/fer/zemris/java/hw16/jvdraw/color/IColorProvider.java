package hr.fer.zemris.java.hw16.jvdraw.color;

import java.awt.Color;

/**
 * Color provider that acts as subject in observer pattern. Informs all
 * listeners when color changes.
 * 
 * @author Mihael Jaić
 *
 */

public interface IColorProvider {
	/**
	 * Gets current color from provider.
	 * 
	 * @return Current color.
	 */
	public Color getCurrentColor();

	/**
	 * Adds color change listener.
	 * 
	 * @param l
	 *            Color change listener.
	 */
	public void addColorChangeListener(ColorChangeListener l);

	/**
	 * Removes color change listener.
	 * 
	 * @param l
	 *            Color change listener.
	 */
	public void removeColorChangeListener(ColorChangeListener l);
}

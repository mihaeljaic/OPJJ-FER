package hr.fer.zemris.java.hw16.jvdraw.color;

import java.awt.Color;

/**
 * Color change listener that listens to {@link IColorProvider}.
 * 
 * @author Mihael Jaić
 *
 */

public interface ColorChangeListener {
	/**
	 * When color provider changes color listener acts depending on
	 * implementation.
	 * 
	 * @param source
	 *            Color provider.
	 * @param oldColor
	 *            Old color.
	 * @param newColor
	 *            New color.
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}

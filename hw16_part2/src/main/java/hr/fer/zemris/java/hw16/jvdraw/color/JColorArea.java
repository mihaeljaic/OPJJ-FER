package hr.fer.zemris.java.hw16.jvdraw.color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 * Color area that is painted in its current color. When user clicks on it it
 * offers change of its color. On color change informs all color change
 * listeners.
 * 
 * @author Mihael Jaić
 *
 */

public class JColorArea extends JComponent implements IColorProvider {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;
	/** Component's width and height. */
	private static final int dimension = 15;
	/** Current selected color. */
	private Color selectedColor;
	/** Previous color */
	private Color oldColor;
	/** List of color change listeners. */
	private List<ColorChangeListener> listeners = new ArrayList<>();

	/**
	 * Constructor that gets initial selected color.
	 * 
	 * @param selectedColor
	 *            Initial selected color.
	 */

	public JColorArea(Color selectedColor) {
		super();
		this.selectedColor = selectedColor;
	}

	/**
	 * Sets selected color for area. All listeners are informed after color
	 * changes.
	 * 
	 * @param selectedColor
	 */

	public void setSelectedColor(Color selectedColor) {
		oldColor = this.selectedColor;
		this.selectedColor = selectedColor;

		fire();
	}

	/**
	 * Informs all color change listeners about changed color in area.
	 */

	public void fire() {
		for (ColorChangeListener listener : listeners) {
			listener.newColorSelected(this, oldColor, selectedColor);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(dimension, dimension);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(selectedColor);
		Insets insets = getInsets();
		g.fillRect(insets.left, insets.top, getWidth() - insets.right, getHeight() - insets.bottom);
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		listeners.add(l);
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}

}

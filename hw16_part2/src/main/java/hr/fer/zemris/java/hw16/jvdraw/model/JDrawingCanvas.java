package hr.fer.zemris.java.hw16.jvdraw.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.color.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.color.IColorProvider;

/**
 * Canvas on which user can draw geometrical objects. Listens to drawing model
 * and draws all of geometrical objects when informed. Draws also geometrical
 * shapes when user clicks so he can get preview what it's final look will be.
 * 
 * @author Mihael Jaić
 *
 */

public class JDrawingCanvas extends JComponent implements DrawingModelListener, ColorChangeListener {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;
	/** Drawing model. */
	private DrawingModel drawingModel;
	/**
	 * Flag that indicates if user has started click to create new geometrical
	 * object.
	 */
	private boolean clicked;
	/** Start point where user first clicked. */
	private Point start;
	/** End point where user's mouse is currently. */
	private Point end;
	/** Foreground color provider. */
	private IColorProvider fgColorProv;
	/** Background color provider. */
	private IColorProvider bgColorProv;
	/** Foreground color. */
	private Color fgColor;
	/** Background color. */
	private Color bgColor;
	/** Selected geometrical object shape. */
	private String selectedShape;

	/**
	 * Constructor that sets drawing model.
	 * 
	 * @param drawingModel
	 *            Drawing model.
	 */

	public JDrawingCanvas(DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
	}

	/**
	 * Sets foreground color provider.
	 * 
	 * @param fgColorProv
	 *            Foreground color provider.
	 */

	public void setFgColorProv(IColorProvider fgColorProv) {
		this.fgColorProv = fgColorProv;
	}

	/**
	 * Sets background color provider.
	 * 
	 * @param bgColorProv
	 *            Background color provider.
	 */

	public void setBgColorProv(IColorProvider bgColorProv) {
		this.bgColorProv = bgColorProv;
	}

	/**
	 * Sets selected geometrical shape.
	 * 
	 * @param selectedShape
	 *            Selected geomtrical shape.
	 */

	public void setSelectedShape(String selectedShape) {
		this.selectedShape = selectedShape;
	}

	/**
	 * Sets start point where user first clicked.
	 * 
	 * @param start
	 *            Start point.
	 */

	public void setStart(Point start) {
		this.start = start;
	}

	/**
	 * Gets start point.
	 * 
	 * @return Start point.
	 */

	public Point getStart() {
		return start;
	}

	/**
	 * Sets clicked flag.
	 * 
	 * @param clicked
	 *            Clicked flag.
	 */

	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

	/**
	 * Gets clicked flag.
	 * 
	 * @return Clicked flag.
	 */

	public boolean getClicked() {
		return clicked;
	}

	/**
	 * Sets end point.
	 * 
	 * @param end
	 *            End point.
	 */

	public void setEnd(Point end) {
		this.end = end;
	}

	@Override
	public void paint(Graphics g) {
		Insets insets = getInsets();
		g.setColor(Color.WHITE);
		g.fillRect(insets.left, insets.top, getWidth() - insets.right, getHeight() - insets.bottom);

		for (int i = 0, size = drawingModel.getSize(); i < size; i++) {
			drawingModel.getObject(i).paint(g, 0, 0);
		}

		g.setColor(fgColor);
		if (start != null && end != null && clicked) {
			if (selectedShape.equals("Line")) {
				g.drawLine(start.x, start.y, end.x, end.y);
			} else {
				int radius = (int) Math.round(end.distance(start));
				g.drawOval(start.x - radius, start.y - radius, radius * 2, radius * 2);

				if (selectedShape.equals("Filled circle")) {
					g.setColor(bgColor);
					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.fillOval(start.x - radius, start.y - radius, radius * 2, radius * 2);
				}
			}
		}
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		fgColor = fgColorProv.getCurrentColor();
		bgColor = bgColorProv.getCurrentColor();
	}
}

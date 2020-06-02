package hr.fer.zemris.java.hw16.jvdraw.geometrical;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw.BoundingBox;

/**
 * Class that models geometrical object circle. It has all information about its
 * position and size so it can draw itself.
 * 
 * @author Mihael Jaić
 *
 */

public class Circle extends GeometricalObject {
	/** Center of circle. */
	private Point center;
	/** Radius of circle. */
	private int radius;
	/** Color of circle's edges. */
	private Color color;
	/** Circle's name. */
	private String name;
	/** Static counter that is used to name circle objects. */
	private static int count;
	/** Max number that user can select for center or radius. */
	private static final int maxPoint = 2000;

	/**
	 * Resets counter.
	 */

	public static void resetCount() {
		count = 0;
	}

	/**
	 * Gets center point.
	 * 
	 * @return Center point.
	 */

	public Point getCenter() {
		return center;
	}

	/**
	 * Gets radius.
	 * 
	 * @return Radius.
	 */

	public int getRadius() {
		return radius;
	}

	/**
	 * Gets circle's color.
	 * 
	 * @return Circle's color
	 */

	public Color getColor() {
		return color;
	}

	/**
	 * Gets circle's name.
	 * 
	 * @return Circle's name.
	 */

	public String getName() {
		return name;
	}

	/**
	 * Gets current count.
	 * 
	 * @return Current count.
	 */

	public static int getCount() {
		return count;
	}

	/**
	 * Constructor that sets center point radius and color.
	 * 
	 * @param center
	 *            Center point.
	 * @param radius
	 *            Radius.
	 * @param color
	 *            Color.
	 */

	public Circle(Point center, int radius, Color color) {
		super();
		this.center = center;
		this.radius = radius;
		this.color = color;
		name = String.format("Circle %d", ++count);
	}

	@Override
	public boolean showProperties() {
		JPanel dialogPanel = new JPanel(new BorderLayout());

		JPanel labels = new JPanel(new GridLayout(4, 0));
		labels.add(new JLabel("Center x"));
		labels.add(new JLabel("Center y"));
		labels.add(new JLabel("Radius"));
		labels.add(new JLabel("Color(red, green, blue)"));

		JPanel fields = new JPanel(new GridLayout(4, 0));
		JSpinner centerX = new JSpinner(new SpinnerNumberModel(center.x, 0, maxPoint, 1));
		fields.add(centerX);
		JSpinner centerY = new JSpinner(new SpinnerNumberModel(center.y, 0, maxPoint, 1));
		fields.add(centerY);
		JSpinner radius = new JSpinner(new SpinnerNumberModel(this.radius, 0, maxPoint, 1));
		fields.add(radius);

		JColorChooser colorChooser = new JColorChooser(color);
		dialogPanel.add(colorChooser, BorderLayout.SOUTH);
		dialogPanel.add(labels, BorderLayout.WEST);
		dialogPanel.add(fields, BorderLayout.CENTER);

		int selection = JOptionPane.showConfirmDialog(dialogPanel, dialogPanel,
				String.format("Edit %s properties", name), JOptionPane.OK_CANCEL_OPTION);

		if (selection == JOptionPane.OK_OPTION) {
			center = new Point((int) centerX.getModel().getValue(), (int) centerY.getModel().getValue());
			this.radius = (int) radius.getModel().getValue();
			color = colorChooser.getColor();

			return true;
		}

		return false;
	}

	@Override
	public void paint(Graphics g, int translationX, int translationY) {
		g.setColor(color);
		g.drawOval(center.x - radius - translationX, center.y - translationY - radius, radius * 2, radius * 2);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(Math.max(0, center.x - radius), Math.max(0, center.y - radius), center.x + radius,
				center.y + radius);
	}

}

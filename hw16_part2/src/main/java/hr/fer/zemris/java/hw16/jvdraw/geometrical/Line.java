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
 * Class that models geometrical object line. It has all information about its
 * position and size so it can draw itsel on canvas.
 * 
 * @author Mihael Jaić
 *
 */

public class Line extends GeometricalObject {
	/** Start point. */
	private Point start;
	/** End point. */
	private Point end;
	/** Line color. */
	private Color color;
	/** Static counter that is used to name filled circle objects. */
	private static int count;
	/** Name. */
	private String name;
	/** Max number that user can select for point coordinates. */
	private static final int maxPoint = 2000;

	/**
	 * Resets counter.
	 */

	public static void resetCount() {
		count = 0;
	}

	/**
	 * Constructor that sets start point, end point and color.
	 * 
	 * @param start
	 *            Start point.
	 * @param end
	 *            End point.
	 * @param color
	 *            Color.
	 */

	public Line(Point start, Point end, Color color) {
		super();
		this.start = start;
		this.end = end;
		this.color = color;
		name = String.format("Line %d", ++count);
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
	 * Gets end point.
	 * 
	 * @return End point.
	 */
	
	public Point getEnd() {
		return end;
	}
	
	/**
	 * Gets color.
	 * 
	 * @return Color.
	 */
	
	public Color getColor() {
		return color;
	}
	
	/**
	 * Gets counter value.
	 * 
	 * @return Counter value.
	 */
	
	public static int getCount() {
		return count;
	}
	
	/**
	 * Gets name.
	 * 
	 * @return Name.
	 */
	
	public String getName() {
		return name;
	}

	@Override
	public boolean showProperties() {
		JPanel dialogPanel = new JPanel(new BorderLayout());

		JPanel labels = new JPanel(new GridLayout(4, 0));
		labels.add(new JLabel("Start x "));
		labels.add(new JLabel("Start y "));
		labels.add(new JLabel("End x"));
		labels.add(new JLabel("End y"));

		JPanel fields = new JPanel(new GridLayout(4, 0, 0, 2));
		JSpinner startX = new JSpinner(new SpinnerNumberModel(start.x, 0, maxPoint, 1));
		fields.add(startX);
		JSpinner startY = new JSpinner(new SpinnerNumberModel(start.y, 0, maxPoint, 1));
		fields.add(startY);
		JSpinner endX = new JSpinner(new SpinnerNumberModel(end.x, 0, maxPoint, 1));
		fields.add(endX);
		JSpinner endY = new JSpinner(new SpinnerNumberModel(end.y, 0, maxPoint, 1));
		fields.add(endY);

		JColorChooser colorChooser = new JColorChooser(color);
		dialogPanel.add(colorChooser, BorderLayout.SOUTH);

		dialogPanel.add(labels, BorderLayout.WEST);
		dialogPanel.add(fields, BorderLayout.CENTER);
		int selection = JOptionPane.showConfirmDialog(dialogPanel, dialogPanel,
				String.format("Edit %s properties", name), JOptionPane.OK_CANCEL_OPTION);

		if (selection == JOptionPane.OK_OPTION) {
			start = new Point((int) startX.getModel().getValue(), (int) startY.getModel().getValue());
			end = new Point((int) endX.getModel().getValue(), (int) endY.getModel().getValue());
			color = colorChooser.getColor();

			return true;
		}

		return false;
	}

	@Override
	public void paint(Graphics g, int translationX, int translationY) {
		g.setColor(color);
		g.drawLine(start.x - translationX, start.y - translationY, end.x - translationX, end.y - translationY);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.max(start.x, end.x),
				Math.max(start.y, end.y));
	}

}

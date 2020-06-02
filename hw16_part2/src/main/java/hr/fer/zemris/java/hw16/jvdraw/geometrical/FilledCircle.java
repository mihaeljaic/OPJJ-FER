package hr.fer.zemris.java.hw16.jvdraw.geometrical;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw.BoundingBox;

/**
 * Class that models filled circle geometrical object. It has all information
 * about its position and size so it can draw itsel on canvas.
 * 
 * @author Mihael Jaić
 *
 */

public class FilledCircle extends GeometricalObject {
	/** Center point. */
	private Point center;
	/** Radius. */
	private int radius;
	/** Foreground color used for drawing circle edges. */
	private Color fgColor;
	/** Background color used for filling circle. */
	private Color bgColor;
	/** Static counter that is used to name filled circle objects. */
	private static int count;
	/** Filled circle's name. */
	private String name;
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
	 * Gets foreground color.
	 * 
	 * @return Foreground color.
	 */

	public Color getFgColor() {
		return fgColor;
	}

	/**
	 * Gets background color.
	 * 
	 * @return Background color.
	 */

	public Color getBgColor() {
		return bgColor;
	}

	/**
	 * Gets count.
	 * 
	 * @return Count current value.
	 */

	public static int getCount() {
		return count;
	}

	/**
	 * Gets object's name.
	 * 
	 * @return Object's name.
	 */

	public String getName() {
		return name;
	}

	/**
	 * Constructor that sets center point radius and foreground and background
	 * colors.
	 * 
	 * @param center
	 *            Center point.
	 * @param radius
	 *            Radius.
	 * @param fgColor
	 *            Foreground color.
	 * @param bgColor
	 *            Background color.
	 */

	public FilledCircle(Point center, int radius, Color fgColor, Color bgColor) {
		super();
		this.center = center;
		this.radius = radius;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
		name = String.format("Filled circle %d", ++count);
	}

	@Override
	public boolean showProperties() {
		JPanel dialogPanel = new JPanel(new BorderLayout());

		JPanel labels = new JPanel(new GridLayout(4, 0));
		labels.add(new JLabel("Center x"));
		labels.add(new JLabel("Center y"));
		labels.add(new JLabel("Radius"));

		JPanel fields = new JPanel(new GridLayout(4, 0));
		JSpinner centerX = new JSpinner(new SpinnerNumberModel(center.x, 0, maxPoint, 1));
		fields.add(centerX);
		JSpinner centerY = new JSpinner(new SpinnerNumberModel(center.y, 0, maxPoint, 1));
		fields.add(centerY);
		JSpinner radius = new JSpinner(new SpinnerNumberModel(this.radius, 0, maxPoint, 1));
		fields.add(radius);

		JColorChooser fgColorChooser = new JColorChooser(fgColor);
		fgColorChooser.setVisible(false);
		JColorChooser bgColorChooser = new JColorChooser(bgColor);

		// fgColorChooser is hidden at start because jdialog isn't resizable.
		// So on smaller screen user can't reach ok and cancel buttons.
		JPanel fgColorPanel = new JPanel(new BorderLayout());
		JToggleButton fgHide = new JToggleButton("Hide/Show");
		fgHide.setSelected(true);
		fgHide.addActionListener(e -> fgColorChooser.setVisible(!fgHide.isSelected()));
		JPanel fgPanel = new JPanel();
		fgPanel.add(new JLabel("Foreground color"));
		fgPanel.add(fgHide);
		fgColorPanel.add(fgPanel, BorderLayout.NORTH);
		fgColorPanel.add(fgColorChooser, BorderLayout.CENTER);

		JPanel bgColorPanel = new JPanel(new BorderLayout());
		JPanel panel = new JPanel();
		panel.add(new JLabel("Background color"));
		bgColorPanel.add(panel, BorderLayout.NORTH);
		bgColorPanel.add(bgColorChooser, BorderLayout.CENTER);

		JPanel colors = new JPanel(new BorderLayout());
		colors.add(fgColorPanel, BorderLayout.NORTH);
		colors.add(bgColorPanel, BorderLayout.SOUTH);

		dialogPanel.add(colors, BorderLayout.SOUTH);
		dialogPanel.add(labels, BorderLayout.WEST);
		dialogPanel.add(fields, BorderLayout.CENTER);

		JScrollPane pane = new JScrollPane(dialogPanel);

		int selection = JOptionPane.showConfirmDialog(null, pane, String.format("Edit %s properties", name),
				JOptionPane.OK_CANCEL_OPTION);

		if (selection == JOptionPane.OK_OPTION) {
			center = new Point((int) centerX.getModel().getValue(), (int) centerY.getModel().getValue());
			this.radius = (int) radius.getModel().getValue();
			fgColor = fgColorChooser.getColor();
			bgColor = bgColorChooser.getColor();

			return true;
		}

		return false;
	}

	@Override
	public void paint(Graphics g, int translationX, int translationY) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(bgColor);
		g2d.fillOval(center.x - radius - translationX, center.y - radius - translationY, radius * 2, radius * 2);
		g2d.setColor(fgColor);
		g2d.drawOval(center.x - radius - translationX, center.y - radius - translationY, radius * 2, radius * 2);
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

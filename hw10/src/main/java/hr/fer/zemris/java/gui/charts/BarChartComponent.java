package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

/**
 * Component that represents bar chart. It draws itself with given data.
 * 
 * @author Mihael Jaić
 *
 */

public class BarChartComponent extends JComponent {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Data for drawing.
	 */
	private BarChart barChart;
	/**
	 * Space from edge of component space.
	 */
	private static final int spaceFromEdge = 10;
	/**
	 * Space between description and numbers.
	 */
	private static final int spaceBetween = 10;
	/**
	 * Arrow size.
	 */
	private static final int arrowSize = 5;
	/**
	 * Font size.
	 */
	private static final int fontSize = 14;
	/**
	 * Size of line between graph and numbers.
	 */
	private static final int numberLine = 4;
	/**
	 * Orange color.
	 */
	private static final Color myOrange = new Color(255, 100, 0);
	
	/**
	 * Constructor that gets needed data for drawing.
	 * 
	 * @param barChart
	 *            Data for drawing.
	 */

	public BarChartComponent(BarChart barChart) {
		if (barChart == null) {
			throw new IllegalArgumentException();
		}

		this.barChart = barChart;
		setMinimumSize(new Dimension(200, 200));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Insets insets = getInsets();
		Dimension dim = getSize();

		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(barChart.getxDescription());

		int yHighestNumberWidth = fm.stringWidth(Integer.toString(barChart.getMaximalY()));

		// Start of x axis.
		int x1 = insets.left + spaceFromEdge + fontSize + spaceBetween + yHighestNumberWidth + numberLine;
		// Start of y axis.
		int y1 = dim.height - (insets.bottom + spaceFromEdge + fontSize * 2 + spaceBetween + numberLine);
		// End of x axis.
		int x2 = dim.width - insets.right - arrowSize - spaceFromEdge;
		// End of y axis.
		int y2 = insets.top + spaceFromEdge + arrowSize;

		g.setFont(new Font("Arial", Font.BOLD, fontSize));
		g.setColor(Color.BLACK);

		int yAmount = (barChart.getMaximalY() - barChart.getMinimalY()) / barChart.getDifference();
		int ySize = (y1 - y2) / yAmount;
		
		Graphics2D g2d = (Graphics2D) g;
		g.drawString(barChart.getxDescription(), (x2 - x1) / 2 - w / 2 + x1, dim.height - insets.bottom - spaceFromEdge);

		drawYAxis(x1, x2, y1, y2, g, g2d, yAmount, ySize, fm);
		
		drawXAxis(x1, x2, y1, y2, g, g2d, ySize, fm);
		
		g2d.drawLine(x1, y1, x1, y2);
		drawArrow(new int[] { x2 + arrowSize * 2, x2, x2 }, new int[] { y1, y1 + arrowSize, y1 - arrowSize }, g2d);
		drawArrow(new int[] { x1, x1 - arrowSize, x1 + arrowSize }, new int[] { y2 - 2 * arrowSize, y2, y2 }, g2d);

		AffineTransform defaultAt = g2d.getTransform();
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		g2d.setTransform(at);

		g.drawString(barChart.getyDescription(), -fm.stringWidth(barChart.getyDescription()) - (y1 - y2) / 2,
				insets.left + spaceFromEdge + fontSize / 2);

		g2d.setTransform(defaultAt);
	}

	/**
	 * Draws y axis of bar chart.
	 * 
	 * @param x1
	 *            Start of x axis.
	 * @param x2
	 *            End of x axis.
	 * @param y1
	 *            Start of y axis.
	 * @param y2
	 *            End of y axis.
	 * @param g
	 *            Graphics.
	 * @param g2d
	 *            2D graphics.
	 * @param yAmount
	 *            Number of y values.
	 * @param ySize
	 *            Size between each value in pixels.
	 * @param fm
	 *            Font metrics.
	 */

	private void drawYAxis(int x1, int x2, int y1, int y2, Graphics g, Graphics2D g2d, int yAmount, int ySize,
			FontMetrics fm) {
		for (int i = 0, tempy1 = y1; i <= yAmount; i++, tempy1 -= ySize) {
			g2d.drawLine(x1, tempy1, x1 - numberLine, tempy1);

			String number = Integer.toString(barChart.getMinimalY() + i * barChart.getDifference());
			int numberSize = fm.stringWidth(number);

			g.drawString(number, x1 - spaceFromEdge - numberLine - numberSize, tempy1 + fontSize / 2);

			if (i == 0) {
				continue;
			}
			
			g.setColor(Color.ORANGE);
			g2d.drawLine(x1, tempy1, x2, tempy1);
			g.setColor(Color.BLACK);

		}
	}

	/**
	 * Draws x axis of bar chart.
	 * 
	 * @param x1
	 *            Start of x axis.
	 * @param x2
	 *            End of x axis.
	 * @param y1
	 *            Start of y axis.
	 * @param y2
	 *            End of y axis.
	 * @param g
	 *            Graphics.
	 * @param g2d
	 *            2D graphics.
	 * @param ySize
	 *            Size between each y value in pixels.
	 * @param fm
	 *            Front metrics.
	 */

	private void drawXAxis(int x1, int x2, int y1, int y2, Graphics g, Graphics2D g2d, int ySize, FontMetrics fm) {
		int xAmount = barChart.getValues().size();
		int xSize = (x2 - x1) / xAmount;
		
		for (int i = 0, tempX = x1; i <= xAmount; i++, tempX += xSize) {
			g.setColor(Color.BLACK);
			g2d.drawLine(tempX, y1, tempX, y1 + numberLine);
			
			if (i < xAmount) {
				XYValue values = barChart.getValues().get(i);
				String currentX = Integer.toString(values.getX());
				int numberSize = fm.stringWidth(currentX);
				g.drawString(currentX, xSize / 2 + tempX - numberSize / 2, y1 + numberLine + fontSize);

				int value = (values.getY() - barChart.getMinimalY()) * ySize / barChart.getDifference();
				g.setColor(myOrange);
				g2d.fillRect(tempX, y1 - value, xSize, value);
				
				g.setColor(Color.WHITE);
				g2d.drawLine(tempX, y1, tempX, y1 - value);
				
				g.setColor(Color.ORANGE);
				g2d.drawLine(tempX, y1 - value, tempX, y2);
			} else {
				g.setColor(Color.ORANGE);
				g2d.drawLine(tempX, y1, tempX, y2);
			}
			
			
		}
		
		g.setColor(Color.BLACK);
		g2d.drawLine(x1, y1, x2, y1);
	}

	/**
	 * Draws arrow.
	 * 
	 * @param xPoints
	 *            X points
	 * @param yPoints
	 *            Y points.
	 * @param g2d
	 *            2D graphics.
	 */

	private void drawArrow(int[] xPoints, int[] yPoints, Graphics2D g2d) {
		Polygon p = new Polygon(xPoints, yPoints, 3);

		g2d.fillPolygon(p);
	}
}

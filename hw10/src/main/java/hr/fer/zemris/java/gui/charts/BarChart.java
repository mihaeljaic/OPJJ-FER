package hr.fer.zemris.java.gui.charts;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Structure that stores information for bar chart. Information is used for
 * drawing {@link BarChartComponent}.
 * 
 * @author Mihael Jaić
 *
 */

public class BarChart {
	/**
	 * X and Y values.
	 */
	private List<XYValue> values;
	/**
	 * X axis description.
	 */
	private String xDescription;
	/**
	 * Y axis description.
	 */
	private String yDescription;
	/**
	 * Minimal Y value.
	 */
	private int minimalY;
	/**
	 * Maximal Y value.
	 */
	private int maximalY;
	/**
	 * Difference between each y value displayed on graph.
	 */
	private int difference;

	/**
	 * Constructor that stores all attributes needed for drawing bar chart. Null
	 * values are not allowed. Maximal y value has to be greater than minimal y
	 * value. Negative values are not allowed. Also difference between each y
	 * value on graph has to be positive.
	 * 
	 * @param values
	 *            Values.
	 * @param xDescription
	 *            Description of x axis.
	 * @param yDescription
	 *            Desription of y axis.
	 * @param minimalY
	 *            Minimal y value.
	 * @param maximalY
	 *            Maximal y value.
	 * @param difference
	 *            Difference between each y value displayed on graph.
	 */

	public BarChart(List<XYValue> values, String xDescription, String yDescription, int minimalY, int maximalY,
			int difference) {
		if (values == null || xDescription == null || yDescription == null || maximalY < minimalY || difference <= 0
				|| minimalY < 0) {
			throw new IllegalArgumentException();
		}

		maximalY = maximalY % difference == 0 ? maximalY : maximalY + difference - maximalY % difference;

		this.values = values;
		this.xDescription = xDescription;
		this.yDescription = yDescription;
		this.minimalY = minimalY;
		this.maximalY = maximalY;
		this.difference = difference;

		Collections.sort(values, new Comparator<XYValue>() {
			@Override
			public int compare(XYValue o1, XYValue o2) {
				return o1.getX() - o2.getX();
			}

		});

	}

	/**
	 * Gets all values
	 * 
	 * @return Values.
	 */

	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * Gets description of x axis.
	 * 
	 * @return Description of x axis.
	 */

	public String getxDescription() {
		return xDescription;
	}

	/**
	 * Gets description of y axis.
	 * 
	 * @return Description of y axis.
	 */

	public String getyDescription() {
		return yDescription;
	}

	/**
	 * Gets minimal y value.
	 * 
	 * @return Minimal y value.
	 */

	public int getMinimalY() {
		return minimalY;
	}

	/**
	 * Gets maximal y value.
	 * 
	 * @return Maximal y value.
	 */

	public int getMaximalY() {
		return maximalY;
	}

	/**
	 * Gets difference between each y value displayed on graph.
	 * 
	 * @return Difference.
	 */

	public int getDifference() {
		return difference;
	}
}

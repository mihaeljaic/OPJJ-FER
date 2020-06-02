package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

import hr.fer.zemris.java.gui.calc.Calculator;

/**
 * Layout manager for {@link Calculator} GUI. It can store 31 components.
 * Components are stored in 5 rows and 7 columns. Each component takes position
 * at one row and column except first one which is shown from (1, 1) to (1, 5)
 * position. Adding components can be done with two constrictions. Using String
 * or using {@link RCPosition} class. In both ways user chooses row and column
 * number where he wants to place component. There can not be multiple
 * components on same row and column.
 * 
 * @author Mihael Jaić
 *
 */

public class CalcLayout implements LayoutManager2 {
	/**
	 * Space between components.
	 */
	private int spacing;
	/**
	 * Number of components.
	 */
	private static final int componentCount = 31;
	/**
	 * Number of rows.
	 */
	private static final int rowCount = 5;
	/**
	 * Number of columns.
	 */
	private static final int columnCount = 7;
	/**
	 * Components.
	 */
	private Component[] components;

	/**
	 * Constructor that sets spacing between components. Spacing has to be
	 * greater or equal to zero.
	 * 
	 * @param spacing
	 *            Space between components.
	 */

	public CalcLayout(int spacing) {
		if (spacing < 0) {
			throw new IllegalArgumentException();
		}

		this.spacing = spacing;
		components = new Component[componentCount];
	}

	/**
	 * Empty constructor that sets spacing to zero.
	 */

	public CalcLayout() {
		components = new Component[componentCount];
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		if (!name.matches("\\s*[0-9]+\\s*,\\s*[0-9]+\\s*")) {
			throw new IllegalArgumentException();
		}

		String[] numbers = name.split(",");

		add(comp, Integer.parseInt(numbers[0].trim()), Integer.parseInt(numbers[1].trim()));
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		for (int i = 0; i < componentCount; i++) {
			if (comp == components[i]) {
				components[i] = null;
				break;
			}
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Insets insets = parent.getInsets();

		int preferredHeight = 0;
		int preferredWidth = 0;

		for (Component c : components) {
			if (c == null) {
				continue;
			}
			Dimension d = c.getPreferredSize();
			if (d != null) {
				preferredHeight = Math.max(preferredHeight, d.height);
				preferredWidth = Math.max(preferredWidth, d.width);
			}
		}

		return new Dimension(preferredWidth * columnCount + insets.left + insets.right + spacing * (columnCount - 1),
				preferredHeight * rowCount + insets.top + insets.bottom + spacing * (rowCount - 1));
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Insets insets = parent.getInsets();

		int minWidth = 0;
		int minHeight = 0;

		for (Component c : components) {
			if (c == null) {
				continue;
			}
			Dimension d = c.getMinimumSize();

			if (d != null) {
				minWidth = Math.max(minWidth, d.width);
				minHeight = Math.max(minHeight, d.height);
			}
		}

		return new Dimension(insets.left + insets.right + minWidth * columnCount + spacing * (columnCount - 1),
				insets.top + insets.bottom + minHeight * rowCount + spacing * (rowCount - 1));
	}

	/**
	 * Adds component to layout.
	 * 
	 * @param comp
	 *            Component.
	 * @param row
	 *            Row number.
	 * @param column
	 *            Column number.
	 */

	private void add(Component comp, int row, int column) {
		if (!validPosition(row, column)) {
			throw new IllegalArgumentException();
		}

		if (row == 1) {
			if (column == 1) {
				if (components[0] != null) {
					throw new IllegalArgumentException();
				}
				components[0] = comp;

			} else {
				if (components[column - 5] != null) {
					throw new IllegalArgumentException();
				}
				components[column - 5] = comp;
			}
		} else {
			if (components[(row - 2) * 7 + column + 2] != null) {
				throw new IllegalArgumentException();
			}
			components[(row - 2) * 7 + column + 2] = comp;
		}
	}

	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		int contWidth = parent.getWidth() - (insets.left + insets.right);
		int contHeight = parent.getHeight() - (insets.top + insets.bottom);

		int width = (int) ((contWidth - spacing * (columnCount - 1)) / (double) columnCount);

		int height = (int) ((contHeight - spacing * (rowCount - 1)) / (double) rowCount);

		if (components[0] != null) {
			components[0].setBounds(0, 0, width * rowCount + spacing * (rowCount - 1), height);
		}

		if (components[1] != null) {
			components[1].setBounds(width * rowCount + spacing * rowCount, 0, width, height);
		}

		if (components[2] != null) {
			components[2].setBounds(width * (columnCount - 1) + spacing * (columnCount - 1), 0, width, height);
		}

		for (int i = 0; i < componentCount - 3; i++) {
			if (components[i + 3] == null) {
				continue;
			}

			components[i + 3].setSize(width, height);
			components[i + 3].setBounds((i % columnCount) * (width + spacing),
					(i / columnCount + 1) * (height + spacing), width, height);
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints instanceof String) {
			addLayoutComponent((String) constraints, comp);
			return;
		}

		if (!(constraints instanceof RCPosition)) {
			throw new IllegalArgumentException();
		}

		RCPosition position = (RCPosition) constraints;
		int row = position.getRow();
		int column = position.getColumn();

		add(comp, row, column);
	}

	/**
	 * Checks if given row and column numbers are correct.
	 * 
	 * @param row
	 *            Row number.
	 * @param column
	 *            Column number.
	 * @return True if given row and column numbers are correct, false
	 *         otherwise.
	 */

	private boolean validPosition(int row, int column) {
		if (row < 1 || row > 5 || column < 1 || column > 7) {
			return false;
		}
		if (row == 1 && column != 1 && column != 6 && column != 7) {
			return false;
		}

		return true;
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		Insets insets = target.getInsets();

		int maxWidth = Integer.MAX_VALUE;
		int maxHeight = Integer.MAX_VALUE;

		for (Component c : components) {
			if (c == null) {
				continue;
			}
			Dimension d = c.getMaximumSize();

			if (d == null) {
				continue;
			}

			maxWidth = Math.min(maxWidth, d.width);
			maxHeight = Math.min(maxHeight, d.height);
		}

		return new Dimension(insets.right + insets.left + maxWidth * columnCount + spacing * (columnCount - 1),
				insets.top + insets.bottom + maxHeight * rowCount + spacing * (rowCount - 1));
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

}

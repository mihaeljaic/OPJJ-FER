package hr.fer.zemris.java.hw11.jnotepadpp.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

/**
 * Layout for status bar.
 * 
 * @author Mihael Jaić
 *
 */

public class StatusBarLayout implements LayoutManager2 {
	/**
	 * Number of components
	 */
	private static final int componentNumber = 6;
	/**
	 * Layout height.
	 */
	private static final int layoutHeight = 18;
	/**
	 * Separator width.
	 */
	private static final int separatorWidth = 5;
	/**
	 * Components.
	 */
	private Component[] components = new Component[componentNumber];
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		for (int i = 0; i < componentNumber; i++) {
			if (components[i] == comp) {
				components[i] = null;
				break;
			}
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(parent.getWidth(), layoutHeight);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(parent.getWidth(), layoutHeight);
	}

	@Override
	public void layoutContainer(Container parent) {
		for (Component comp : components) {
			if (comp == null) {
				throw new IllegalStateException("Some of components are missing.");
			}
		}
		
		Insets insets = parent.getInsets();
		
		int width = parent.getWidth() - insets.left - insets.right;
		
		components[0].setBounds(insets.left, insets.top, width / 3 - separatorWidth, layoutHeight);
		components[1].setBounds(width / 3 - separatorWidth, insets.top, separatorWidth, layoutHeight);
		
		final int compWidth = (int) Math.round(width / 9.0);
		final int clockWidth = components[5].getPreferredSize().width;
		
		components[2].setBounds(width / 3, insets.top, compWidth, layoutHeight);
		components[3].setBounds(width / 3 + compWidth, insets.top, compWidth, layoutHeight);
		components[4].setBounds(width / 3 + 2 * compWidth, insets.top, compWidth, layoutHeight);
		components[5].setBounds(width - clockWidth - insets.right, insets.top, clockWidth, layoutHeight);
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (!(constraints instanceof Integer)) {
			throw new IllegalArgumentException();
		}
		
		Integer position = (Integer) constraints;
		if (position < 0 || position >= componentNumber) {
			throw new IllegalArgumentException();
		}
		
		components[position] = comp;
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(target.getWidth(), layoutHeight);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0f;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0f;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

}

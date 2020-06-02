package hr.fer.zemris.java.hw16.jvdraw.color;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * Label that is observer to color providers. It listens to foreground and
 * background color providers and displays text according to their color
 * selections.
 * 
 * @author Mihael Jaić
 *
 */

public class JColorLabel extends JLabel implements ColorChangeListener {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;
	/** Foreground color provider. */
	private IColorProvider fgColorProvider;
	/** Background color provider. */
	private IColorProvider bgColorProvider;

	/**
	 * Constructor that sets foreground and background color providers.
	 * 
	 * @param fgColorProvider
	 *            Foreground color provider.
	 * @param bgColorProvider
	 *            Background color provider.
	 */

	public JColorLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		super();
		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;

		fgColorProvider.addColorChangeListener(this);
		bgColorProvider.addColorChangeListener(this);

		updateText();
	}

	/**
	 * Updates label text when color on one of color providers changes.
	 */

	private void updateText() {
		Color fgColor = fgColorProvider.getCurrentColor();
		Color bgColor = bgColorProvider.getCurrentColor();

		setText(String.format("Foreground color: (%d, %d, %d), background color: (%d, %d, %d).", fgColor.getRed(),
				fgColor.getGreen(), fgColor.getBlue(), bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()));
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		updateText();
	}

}

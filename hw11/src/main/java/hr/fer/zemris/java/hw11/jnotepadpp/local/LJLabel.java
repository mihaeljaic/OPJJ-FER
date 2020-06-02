package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JLabel;

/**
 * Localized label that updates it's text to language specified by localization
 * provider.
 * 
 * @author Mihael Jaić
 *
 */

public class LJLabel extends JLabel {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Key.
	 */
	String key;
	/**
	 * Provider.
	 */
	ILocalizationProvider provider;
	/**
	 * Number.
	 */
	private int number;
	/**
	 * Label text.
	 */
	private String name;
	/**
	 * Number that indicates that dash should be displayed as number.
	 */
	private static final int dashNumber = -1;

	/**
	 * Constructor that gets key and provider and registers itself to provider.
	 * 
	 * @param key
	 *            Key.
	 * @param provider
	 *            Localization provider.
	 */

	public LJLabel(String key, ILocalizationProvider provider) {
		if (key == null || provider == null) {
			throw new IllegalArgumentException();
		}

		this.key = key;
		this.provider = provider;

		name = provider.getString(key);
		setText(String.format("%s:%d", name, number));

		provider.addLocalizationListener(() -> {
			name = provider.getString(key);
			updateLabel();
		});
	}

	/**
	 * Sets number to be displayed on label.
	 * 
	 * @param number
	 *            Number.
	 */

	public void setNumber(int number) {
		this.number = number;
		updateLabel();
	}

	/**
	 * Updates label text.
	 */

	private void updateLabel() {
		if (number == dashNumber) {
			setText(String.format("%s:-", name));
			return;
		}

		setText(String.format("%s:%d", name, number));
	}
}

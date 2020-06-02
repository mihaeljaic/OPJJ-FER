package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Action that changes it's name and description by localized provider.
 * 
 * @author Mihael Jaić
 *
 */

public abstract class LocalizableAction extends AbstractAction {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Key.
	 */
	private String key;
	/**
	 * Provider.
	 */
	private ILocalizationProvider provider;

	/**
	 * Constructor that gets key for translating and localized provider and
	 * registers itself on provider.
	 * 
	 * @param key
	 *            Key
	 * @param provider
	 *            Provider.
	 */

	public LocalizableAction(String key, ILocalizationProvider provider) {
		if (key == null || provider == null) {
			throw new IllegalArgumentException();
		}

		this.key = key;
		this.provider = provider;

		putValue(Action.NAME, provider.getString(key));
		putValue(Action.SHORT_DESCRIPTION, provider.getString(String.format("%sDescription", key)));

		provider.addLocalizationListener(() -> {
			putValue(Action.NAME, this.provider.getString(this.key));
			putValue(Action.SHORT_DESCRIPTION, this.provider.getString(String.format("%sDescription", this.key)));
		});
	}

}

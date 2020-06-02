package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Localization listener. Should be registered to {@link ILocalizationProvider}
 * and listens to localization changes.
 * 
 * @author Mihael Jaić
 *
 */

public interface ILocalizationListener {
	/**
	 * Changes text according to new language.
	 */
	public void localizationChanged();
}

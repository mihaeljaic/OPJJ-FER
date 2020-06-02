package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Subject in observer pattern. Contains listeners that are informed for every
 * localization change.
 * 
 * @author Mihael Jaić
 *
 */

public interface ILocalizationProvider {
	/**
	 * Adds new localization listener.
	 * 
	 * @param listener
	 *            Listener.
	 */
	public void addLocalizationListener(ILocalizationListener listener);

	/**
	 * Removes listener.
	 * 
	 * @param listener
	 *            Listener.
	 */
	public void removeLocalizationListener(ILocalizationListener listener);

	/**
	 * Gets localized string for given key.
	 * 
	 * @param key
	 *            Key.
	 * @return Localized string for given key.
	 */
	public String getString(String key);
}

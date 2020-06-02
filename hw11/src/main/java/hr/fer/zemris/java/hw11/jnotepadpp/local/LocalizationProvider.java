package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton class that implements {@link ILocalizationProvider} and offers
 * support for translating to 3 languages: english("en"), german("de") and
 * croatian("hr") by given key. After language changes all listeners are
 * informed about the change.
 * 
 * @author Mihael Jaić
 *
 */

public class LocalizationProvider extends AbstractLocalizationProvider {
	/**
	 * Current language.
	 */
	private String language;
	/**
	 * Localization translations.
	 */
	private ResourceBundle bundle;
	/**
	 * Instance of class.
	 */
	private static LocalizationProvider instance = new LocalizationProvider();

	/**
	 * Private constructor that sets language to default("en") and loads
	 * localization resource bundle.
	 */

	private LocalizationProvider() {
		language = "en";
		loadBundle();
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	/**
	 * Gets instance of provider class.
	 * 
	 * @return Provider.
	 */

	public static LocalizationProvider getInstance() {
		return instance;
	}

	/**
	 * Gets current language.
	 * 
	 * @return Current language.
	 */

	public String getLanguage() {
		return language;
	}

	/**
	 * Sets language and informs all listeners about the change. Only "en", "hr"
	 * and "de" languages are allowed.
	 * 
	 * @param language
	 *            Langugage.
	 */

	public void setLanguage(String language) {
		if (!language.equals("en") && !language.equals("hr") && !language.equals("de")) {
			throw new IllegalArgumentException("Invalid language.");
		}

		this.language = language;
		loadBundle();

		fire();
	}

	/**
	 * Loads bundle from memory for given language.
	 */

	private void loadBundle() {
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.localization", locale);
	}
}

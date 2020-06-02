package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Bridge between {@link ILocalizationProvider} and
 * {@link ILocalizationListener}s. Offers methods to connect and disconnect
 * listeners from provider.
 * 
 * @author Mihael Jaić
 *
 */

public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	/**
	 * Connection status.
	 */
	private boolean connected = false;
	/**
	 * Listener to localization provider that when localization changes informs
	 * all listeners about the change.
	 */
	private ILocalizationListener listener;
	/**
	 * Localization provider.
	 */
	private ILocalizationProvider provider;

	/**
	 * Constructor that gets localization provider.
	 * 
	 * @param provider
	 *            Localization provider.
	 */

	public LocalizationProviderBridge(ILocalizationProvider provider) {
		if (provider == null) {
			throw new IllegalArgumentException();
		}

		listener = () -> {
			fire();
		};

		this.provider = provider;
	}

	@Override
	public String getString(String key) {
		return provider.getString(key);
	}

	/**
	 * Connects to localization provider.
	 */

	public void connect() {
		if (connected) {
			throw new IllegalStateException();
		}

		provider.addLocalizationListener(listener);

		connected = true;
	}

	/**
	 * Disconnects from localization provider.
	 */

	public void disconnect() {
		if (!connected) {
			throw new IllegalStateException();
		}

		provider.removeLocalizationListener(listener);

		connected = false;
	}

}

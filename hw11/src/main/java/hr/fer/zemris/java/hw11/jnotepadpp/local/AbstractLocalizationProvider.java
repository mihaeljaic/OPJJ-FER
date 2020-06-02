package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of {@link ILocalizationProvider}, holds reference to
 * all registered listeners and offers method to inform all listeners about the
 * localization change. Doesn't offer translation of given key.
 * 
 * @author Mihael Jaić
 *
 */

public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	/**
	 * Listeners.
	 */
	private List<ILocalizationListener> listeners = new ArrayList<>();

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException();
		}

		listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException();
		}

		listeners.remove(listener);
	}

	/**
	 * Informs all listeners about the change of localization.
	 */

	public void fire() {
		for (ILocalizationListener listener : listeners) {
			listener.localizationChanged();
		}
	}

}

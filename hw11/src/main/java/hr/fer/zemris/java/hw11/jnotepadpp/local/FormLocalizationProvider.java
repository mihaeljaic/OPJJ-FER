package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Class represents localization provider that is responsible for localization
 * of given window.
 * 
 * @author Mihael Jaić
 *
 */

public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Constructor that connect
	 * 
	 * @param provider
	 * @param frame
	 */

	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
		});
	}
}

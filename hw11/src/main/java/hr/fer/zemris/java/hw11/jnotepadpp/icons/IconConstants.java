package hr.fer.zemris.java.hw11.jnotepadpp.icons;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadpp;

/**
 * Icon constants for {@link JNotepadpp}.
 * 
 * @author Mihael Jaić
 *
 */

public class IconConstants {
	/**
	 * Icon for closing tab.
	 */
	public final Icon closeIcon;
	/**
	 * Icon for creating new document.
	 */
	public final Icon newIcon;
	/**
	 * Icon for opening existing document.
	 */
	public final Icon openIcon;
	/**
	 * Icon for saving document.
	 */
	public final Icon saveIcon;
	/**
	 * Icon for saving document as.
	 */
	public final Icon notSavedIcon;
	/**
	 * Icon for closing tab in toolbar.
	 */
	public final Icon closeDocumentIcon;
	/**
	 * Icon for cut action.
	 */
	public final Icon cutIcon;
	/**
	 * Icon for copy action.
	 */
	public final Icon copyIcon;
	/**
	 * Icon for paste action.
	 */
	public final Icon pasteIcon;
	/**
	 * Icon for statistics.
	 */
	public final Icon statisticsIcon;

	/**
	 * Constructor that loads all icons.
	 * 
	 * @param frame
	 *            Window.
	 */

	public IconConstants(JFrame frame) {
		closeIcon = loadIcon("close.png", frame);
		newIcon = loadIcon("new.png", frame);
		openIcon = loadIcon("open.png", frame);
		saveIcon = loadIcon("save.png", frame);
		notSavedIcon = loadIcon("notSaved.png", frame);
		closeDocumentIcon = loadIcon("closeDocument.png", frame);
		cutIcon = loadIcon("cut.png", frame);
		copyIcon = loadIcon("copy.png", frame);
		pasteIcon = loadIcon("paste.png", frame);
		statisticsIcon = loadIcon("statistics.png", frame);
	}

	/**
	 * Loads icon from disk.
	 * 
	 * @param path
	 *            Path.
	 * @param frame
	 *            Window.
	 * @return Icon.
	 */

	private Icon loadIcon(String path, JFrame frame) {
		InputStream is = this.getClass().getResourceAsStream(path);
		if (is == null) {
			JOptionPane.showMessageDialog(frame, "Couldn't load icons.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		byte[] bytes = null;

		try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
			int nRead;
			final int bufferSize = 4096;
			byte[] data = new byte[bufferSize];

			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();

			bytes = buffer.toByteArray();
			is.close();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(frame, "Couldn't load icons.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		return new ImageIcon(bytes);
	}
}

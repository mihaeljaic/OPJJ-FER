package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Action constants for {@link JNotepadpp}.
 * 
 * @author Mihael Jaić
 *
 */

public class ActionConstants {
	/**
	 * Window.
	 */
	private JNotepadpp frame;
	/**
	 * Cached text from cut and copy actions.
	 */
	String copiedText;
	/**
	 * Opens new tab.
	 */
	Action newTab;
	/**
	 * Opens existing document.
	 */
	Action openDocument;
	/**
	 * Saves document as.
	 */
	Action saveAsAction;
	/**
	 * Saves document.
	 */
	Action saveAction;
	/**
	 * Removes tab.
	 */
	Action removeTab;
	/**
	 * Cuts selected text.
	 */
	Action cutAction;
	/**
	 * Copies selected text.
	 */
	Action copyAction;
	/**
	 * Pastes cached text into document.
	 */
	Action pasteAction;
	/**
	 * Gets statistical info about document.
	 */
	Action statisticalInfo;
	/**
	 * Converts selected text to upper case letters.
	 */
	Action toUpperCase;
	/**
	 * Converts selected text to lower case letters.
	 */
	Action toLowerCase;
	/**
	 * Inverts case of letters in selected text.
	 */
	Action invertCase;
	/**
	 * Removes selected duplicate lines.
	 */
	Action uniqueLines;
	/**
	 * Changes language to english.
	 */
	Action englishLanguage;
	/**
	 * Changes language to german.
	 */
	Action germanLanguage;
	/**
	 * Changes language to croatian.
	 */
	Action croatianLanguage;
	/**
	 * Sorts selected lines in ascending order.
	 */
	Action ascending;
	/**
	 * Sorts selected lines in descending order.
	 */
	Action descending;

	/**
	 * Constructor that creates actions.
	 * 
	 * @param frame
	 *            Window.
	 */

	public ActionConstants(JNotepadpp frame) {
		this.frame = frame;

		initDocumentManipulation();
		initSelectedTextManipulation();
		initSort();
		initLetterManipulation();
		initLanguages();
	}

	/**
	 * Creates actions that are doucemnt manipulation.
	 */

	public void initDocumentManipulation() {
		newTab = new LocalizableAction("new", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.addTab(frame.tabs.getTabCount(), null, "");
			}
		};

		newTab.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		newTab.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

		openDocument = new LocalizableAction("open", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Open file");
				if (fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
					return;
				}

				File fileName = fc.getSelectedFile();
				Path filePath = fileName.toPath();
				if (frame.tabPaths.contains(filePath)) {
					JOptionPane.showMessageDialog(frame, "File is already in editor.", "Information",
							JOptionPane.INFORMATION_MESSAGE);
					int index = frame.tabPaths.indexOf(filePath);
					frame.tabs.setSelectedIndex(index);
					return;
				}

				if (!Files.isReadable(filePath)) {
					JOptionPane.showMessageDialog(frame, "Couldn't read from file " + filePath + "!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				byte[] data = null;
				try {
					data = Files.readAllBytes(filePath);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(frame, "Error occured while trying to read from " + filePath + "!",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				String text = new String(data, StandardCharsets.UTF_8);

				int index = frame.tabs.getTabCount();
				frame.tabPaths.add(index, filePath);
				frame.addTab(index, fileName.getName(), text);
			}
		};

		openDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

		saveAsAction = new LocalizableAction("saveAs", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				saveAs();
			}
		};

		saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));

		saveAction = new LocalizableAction("save", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = frame.tabs.getSelectedIndex();

				if (frame.tabPaths.get(index) != null) {
					save(frame.tabPaths.get(index));
				} else {
					saveAs();
				}

			}
		};

		saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		removeTab = new LocalizableAction("close", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				removeTab(frame.tabs.getSelectedIndex());
			}
		};

		removeTab.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
	}

	/**
	 * Creates actions that offer operations on selected text.
	 */

	private void initSelectedTextManipulation() {
		cutAction = new LocalizableAction("cut", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = frame.tabs.getSelectedIndex();
				if (index < 0) {
					return;
				}
				JTextArea editor = getTextArea(index);
				Document doc = editor.getDocument();

				int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
				if (len == 0) {
					return;
				}

				int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());

				try {
					copiedText = doc.getText(offset, len);
					doc.remove(offset, len);
					pasteAction.setEnabled(true);
				} catch (BadLocationException ex) {
					JOptionPane.showMessageDialog(frame, "Error while cuting text.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};

		cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt X"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

		copyAction = new LocalizableAction("copy", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = frame.tabs.getSelectedIndex();
				if (index < 0) {
					return;
				}

				JTextArea editor = getTextArea(index);
				Document doc = editor.getDocument();

				int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
				if (len == 0) {
					return;
				}

				int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());

				try {
					copiedText = doc.getText(offset, len);
					pasteAction.setEnabled(true);
				} catch (BadLocationException ex) {
					JOptionPane.showMessageDialog(frame, "Error while copying text.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};

		copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt C"));
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

		pasteAction = new LocalizableAction("paste", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = frame.tabs.getSelectedIndex();
				if (index < 0) {
					return;
				}

				JTextArea editor = getTextArea(index);
				int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
				int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());

				String text = editor.getText();
				if (len == 0) {
					editor.setText(text.substring(0, offset) + copiedText + text.substring(offset));
				} else {
					editor.setText(text.substring(0, offset) + copiedText + text.substring(offset + len));
				}

				editor.getCaret().setVisible(true);
			}
		};

		pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
		pasteAction.setEnabled(false);

		statisticalInfo = new LocalizableAction("statistics", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea textArea = getTextArea(frame.tabs.getSelectedIndex());
				String text = textArea.getText();

				int nonBlankLength = 0;
				int linesNumber = 1;

				for (char c : text.toCharArray()) {
					if (!Character.isWhitespace(c)) {
						nonBlankLength++;
					}

					if (!String.valueOf(c).matches(".")) {
						linesNumber++;
					}
				}

				JOptionPane.showMessageDialog(frame,
						String.format("Your document has %d characters, %d non-blank characters and %d lines.",
								text.length(), nonBlankLength, linesNumber),
						"Statistical info", JOptionPane.INFORMATION_MESSAGE);
			}
		};
	}

	/**
	 * Creates actions that offer operations with letters in selected text.
	 */

	private void initLetterManipulation() {
		toUpperCase = new LocalizableAction("toUpper", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				changeLetters(c -> Character.toUpperCase(c));
			}
		};

		toUpperCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
		toUpperCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);

		toLowerCase = new LocalizableAction("toLower", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				changeLetters(c -> Character.toLowerCase(c));
			}
		};

		toLowerCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		toLowerCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);

		invertCase = new LocalizableAction("invert", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				changeLetters(c -> {
					if (Character.isLowerCase(c)) {
						return Character.toUpperCase(c);
					}

					return Character.toLowerCase(c);
				});
			}
		};

		invertCase.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		invertCase.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);

		uniqueLines = new LocalizableAction("unique", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea editor = getTextArea(frame.tabs.getSelectedIndex());
				int startPos = Math.min(editor.getCaret().getMark(), editor.getCaret().getDot());
				int endPos = Math.max(editor.getCaret().getMark(), editor.getCaret().getDot());

				Document document = editor.getDocument();

				String text = null;
				int firstLine = 0;
				try {
					firstLine = editor.getLineStartOffset(editor.getLineOfOffset(startPos));
					text = document.getText(firstLine,
							editor.getLineEndOffset(editor.getLineOfOffset(endPos)) - firstLine);
				} catch (BadLocationException e1) {
					JOptionPane.showMessageDialog(frame, "Couldn't remove unique lines.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Set<String> lines = new HashSet<>();
				StringBuilder sb = new StringBuilder();
				char[] charText = text.toCharArray();
				for (int i = 0, deleted = 0; i <= charText.length; i++) {
					if (i < charText.length && String.valueOf(charText[i]).matches(".")) {
						sb.append(charText[i]);

						// If there is end of line or end of text.
					} else {
						if (lines.contains(sb.toString())) {
							try {
								document.remove(firstLine + i - sb.length() - deleted,
										i < charText.length ? sb.length() + 1 : sb.length());
								deleted += sb.length() + 1;
							} catch (BadLocationException e1) {
								JOptionPane.showMessageDialog(frame, "Couldn't remove unique lines.", "Error",
										JOptionPane.ERROR_MESSAGE);
								return;
							}

						} else {
							lines.add(sb.toString());
						}

						sb.setLength(0);
					}
				}
			}
		};

		uniqueLines.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt U"));
		uniqueLines.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
	}

	/**
	 * Creates actions that sort selected lines.
	 */

	private void initSort() {
		ascending = new LocalizableAction("ascending", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				sortLines(true);
			}
		};

		ascending.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt A"));
		ascending.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

		descending = new LocalizableAction("descending", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				sortLines(false);
			}
		};

		descending.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt D"));
		ascending.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
	}

	/**
	 * Creates actions that change language of GUI.
	 */

	private void initLanguages() {
		englishLanguage = new LocalizableAction("english", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("en");
			}
		};

		englishLanguage.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt E"));
		englishLanguage.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);

		germanLanguage = new LocalizableAction("german", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("de");
			}
		};

		germanLanguage.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));
		germanLanguage.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);

		croatianLanguage = new LocalizableAction("croatian", frame.flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("hr");
			}
		};

		croatianLanguage.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control H"));
		croatianLanguage.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_H);
	}

	/**
	 * Sorts selected lines in specified order.
	 * 
	 * @param ascending
	 *            If true sorts in ascending order. If false sorts in descending
	 *            order.
	 */

	private void sortLines(boolean ascending) {
		JTextArea editor = getTextArea(frame.tabs.getSelectedIndex());
		int startPos = Math.min(editor.getCaret().getMark(), editor.getCaret().getDot());
		int endPos = Math.max(editor.getCaret().getMark(), editor.getCaret().getDot());

		Document document = editor.getDocument();

		String text = null;
		int firstLine = 0;
		try {
			firstLine = editor.getLineStartOffset(editor.getLineOfOffset(startPos));
			text = document.getText(firstLine, editor.getLineEndOffset(editor.getLineOfOffset(endPos)) - firstLine);
			document.remove(firstLine, text.length());
		} catch (BadLocationException e1) {
			JOptionPane.showMessageDialog(frame, "Couldn't sort lines.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// String separator = System.lineSeparator();
		// For some reason this doesn't work with splitting and appending in
		// result string :/

		List<String> lines = Arrays.asList(text.split("\n"));
		StringBuilder sb = new StringBuilder(text.length());
		Locale locale = new Locale(LocalizationProvider.getInstance().getLanguage());
		Collator collator = Collator.getInstance(locale);

		lines.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return collator.compare(o1, o2) * (ascending ? 1 : -1);
			}
		});

		for (String s : lines) {
			sb.append(String.format("%s\n", s));
		}
		sb.setLength(sb.length() - 1);

		try {
			document.insertString(firstLine, sb.toString(), null);
		} catch (BadLocationException e1) {
			JOptionPane.showMessageDialog(frame, "Couldn't sort lines.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/**
	 * Saves document into specified path.
	 * 
	 * @param path
	 *            Path.
	 * @return True if file was saved, false otherwise.
	 */

	private boolean save(Path path) {
		int index = frame.tabs.getSelectedIndex();

		try {
			Files.write(path, getTextArea(index).getText().getBytes(StandardCharsets.UTF_8));
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(frame, "Error while saving file.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		JOptionPane.showMessageDialog(frame, "File has been saved.", "Information", JOptionPane.INFORMATION_MESSAGE);

		JLabel label = (JLabel) ((JPanel) frame.tabs.getTabComponentAt(index)).getComponent(0);
		label.setIcon(frame.icons.saveIcon);

		saveAction.setEnabled(false);
		saveAsAction.setEnabled(false);

		return true;
	}

	/**
	 * Gets text area at selected index from tab.
	 * 
	 * @param index
	 *            Index.
	 * @return Text area.
	 */

	private JTextArea getTextArea(int index) {
		JScrollPane scrollPane = (JScrollPane) frame.tabs.getComponentAt(index);
		JViewport text = scrollPane.getViewport();

		return (JTextArea) text.getView();
	}

	/**
	 * Saves document in specified path.
	 */

	private void saveAs() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Save file");
		if (fc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(frame, "Didn't save the file.", "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		File selectedFile = fc.getSelectedFile();
		if (selectedFile.exists()) {
			int pressed = JOptionPane.showConfirmDialog(frame, "File already exists. Would you like to overwrite file?",
					"Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			if (pressed == JOptionPane.NO_OPTION) {
				return;
			}
		}

		selectedFile.getParentFile().mkdirs();
		try {
			selectedFile.createNewFile();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(frame, "Couldn't create file.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!save(selectedFile.toPath())) {
			return;
		}

		int index = frame.tabs.getSelectedIndex();
		JPanel panel = (JPanel) frame.tabs.getTabComponentAt(index);
		JLabel label = (JLabel) panel.getComponent(0);

		label.setText(selectedFile.getName());
		label.setIcon(frame.icons.saveIcon);
		frame.tabPaths.remove(index);
		frame.tabPaths.add(index, selectedFile.toPath());

		saveAction.setEnabled(false);
		saveAsAction.setEnabled(false);
	}

	/**
	 * Removes tab.
	 * 
	 * @param index
	 *            Index of tab that will be removed.
	 * @return True if tab was removed, false otherwise.
	 */

	boolean removeTab(int index) {
		if (index >= 0) {
			JLabel label = (JLabel) ((JPanel) frame.tabs.getTabComponentAt(index)).getComponent(0);
			if (label.getIcon() == frame.icons.notSavedIcon) {
				String name = label.getText();

				int pressed = JOptionPane.showConfirmDialog(frame,
						String.format("Document '%s' was changed. Save changes?", name), "Save changes?",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (pressed == JOptionPane.CANCEL_OPTION || pressed == JOptionPane.CLOSED_OPTION) {
					return false;
				}

				if (pressed == JOptionPane.YES_OPTION) {
					Path path = frame.tabPaths.get(index);
					if (path == null) {
						saveAs();
					} else {
						save(path);
					}
				}
			}

			frame.tabPaths.remove(index);
			JTextArea editor = getTextArea(index);
			editor.getCaret().deinstall(editor);
			frame.tabs.removeTabAt(index);

			return true;
		}

		return false;
	}

	/**
	 * Changes letters in selected text by given function.
	 * 
	 * @param function
	 *            Function which changes case of letters.
	 */

	private void changeLetters(Function<Character, Character> function) {
		JTextArea editor = getTextArea(frame.tabs.getSelectedIndex());
		int dot = editor.getCaret().getDot();
		int mark = editor.getCaret().getMark();

		String text = editor.getText();
		StringBuilder sb = new StringBuilder(Math.abs(dot - mark));
		sb.append(text.substring(0, Math.min(mark, dot)));
		for (char c : editor.getText().substring(Math.min(dot, mark), Math.max(dot, mark)).toCharArray()) {
			sb.append(function.apply(c));
		}

		sb.append(text.substring(Math.max(dot, mark)));
		editor.setText(sb.toString());
		editor.setCaretPosition(Math.max(dot, mark));
	}

}

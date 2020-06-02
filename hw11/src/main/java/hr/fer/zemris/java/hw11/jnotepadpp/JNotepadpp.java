package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import hr.fer.zemris.java.hw11.jnotepadpp.icons.IconConstants;
import hr.fer.zemris.java.hw11.jnotepadpp.layout.StatusBarLayout;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJLabel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Text editor application that allows editing of multiple documents. Offers
 * standard text editor methods like creating new document, opening existing
 * document, saving document, etc. Offers option for changing language to
 * english, croatian and german. All operations are displayed in toolbar or
 * menu. For more information hover over operation names.
 * 
 * @author Mihael Jaić
 *
 */

public class JNotepadpp extends JFrame {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Tabs.
	 */
	JTabbedPane tabs;
	/**
	 * Tab counter.
	 */
	private int tabCount;
	/**
	 * Paths for each tab.
	 */
	ArrayList<Path> tabPaths;
	/**
	 * Length label in status bar.
	 */
	JLabel lengthL;
	/**
	 * Label that displays current line position in text.
	 */
	JLabel lineL;
	/**
	 * Label that displays current column position in text.
	 */
	JLabel columnL;
	/**
	 * Label that displays amount of selected text.
	 */
	JLabel selectedL;
	/**
	 * Label that displays time.
	 */
	JLabel timeL;
	/**
	 * Localization provider.
	 */
	FormLocalizationProvider flp;
	/**
	 * Icon constants.
	 */
	IconConstants icons;
	/**
	 * Action constants.
	 */
	private ActionConstants actions;
	/**
	 * Exit action.
	 */
	Action exitAction;

	/**
	 * Constructor that initializes GUI.
	 */

	public JNotepadpp() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setTitle("Untitled - JNotepad++");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				((Clock) timeL).stopRequested = true;
				exitApplication();
			}
		});

		initGUI();
	}

	/**
	 * Initializes GUI.
	 */

	private void initGUI() {
		Container content = getContentPane();

		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

		icons = new IconConstants(this);
		
		createActions();
		
		JPanel tabsAndBar = new JPanel(new BorderLayout());
		tabPaths = new ArrayList<>();
		tabs = new JTabbedPane();
		addTab(0, null, "");
		tabs.addChangeListener(tabListener);

		tabsAndBar.add(tabs, BorderLayout.CENTER);

		createMenus();
		createToolBars();

		tabsAndBar.add(createStatusBar(), BorderLayout.PAGE_END);
		content.add(tabsAndBar, BorderLayout.CENTER);
	}

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepadpp().setVisible(true);
		});
	}

	/**
	 * Creates status bar.
	 * 
	 * @return Reference to panel that is status bar.
	 */

	private JPanel createStatusBar() {
		JPanel statusPanel = new JPanel(new StatusBarLayout());
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

		lengthL = new LJLabel("length", flp);
		lengthL.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(lengthL, Integer.valueOf(0));

		statusPanel.add(new JSeparator(SwingConstants.VERTICAL), Integer.valueOf(1));

		lineL = new LJLabel("line", flp);
		((LJLabel) lineL).setNumber(1);
		lineL.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(lineL, Integer.valueOf(2));

		columnL = new LJLabel("column", flp);
		columnL.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(columnL, Integer.valueOf(3));

		selectedL = new LJLabel("selected", flp);
		selectedL.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(selectedL, Integer.valueOf(4));

		timeL = new Clock();
		timeL.setHorizontalAlignment(SwingConstants.RIGHT);
		statusPanel.add(timeL, Integer.valueOf(5));

		return statusPanel;
	}

	/**
	 * Creates tool bars.
	 */

	private void createToolBars() {
		JToolBar toolBar = new JToolBar("Tool bar");

		JButton newButton = new JButton(actions.newTab);
		newButton.setIcon(icons.newIcon);

		JButton openButton = new JButton(actions.openDocument);
		openButton.setIcon(icons.openIcon);

		JButton saveButton = new JButton(actions.saveAction);
		saveButton.setIcon(icons.saveIcon);

		JButton saveAsButton = new JButton(actions.saveAsAction);
		saveAsButton.setIcon(icons.saveIcon);

		JButton closeDocumentButton = new JButton(actions.removeTab);
		closeDocumentButton.setIcon(icons.closeDocumentIcon);

		JButton cutButton = new JButton(actions.cutAction);
		cutButton.setIcon(icons.cutIcon);

		JButton copyButton = new JButton(actions.copyAction);
		copyButton.setIcon(icons.copyIcon);

		JButton pasteButton = new JButton(actions.pasteAction);
		pasteButton.setIcon(icons.pasteIcon);

		JButton statisticsButton = new JButton(actions.statisticalInfo);
		statisticsButton.setIcon(icons.statisticsIcon);

		toolBar.add(newButton);
		toolBar.add(openButton);
		toolBar.add(saveButton);
		toolBar.add(saveAsButton);
		toolBar.add(closeDocumentButton);
		toolBar.addSeparator();
		toolBar.add(cutButton);
		toolBar.add(copyButton);
		toolBar.add(pasteButton);
		toolBar.addSeparator();
		toolBar.add(statisticsButton);

		getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Creates exit action.
	 */

	private void createActions() {
		actions = new ActionConstants(this);
		
		exitAction = new LocalizableAction("exit", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				((Clock) timeL).stopRequested = true;
				exitApplication();
			}
		};

		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control E"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);

		changeSelectedTextActions(false);
	}

	/**
	 * Creates menus.
	 */

	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(flp.getString("file"));
		menuBar.add(fileMenu);

		fileMenu.add(actions.newTab);
		fileMenu.add(actions.openDocument);
		fileMenu.add(actions.saveAction);
		fileMenu.add(actions.saveAsAction);
		fileMenu.add(actions.removeTab);
		fileMenu.addSeparator();
		fileMenu.add(exitAction);

		JMenu editMenu = new JMenu(flp.getString("edit"));
		menuBar.add(editMenu);

		editMenu.add(actions.cutAction);
		editMenu.add(actions.copyAction);
		editMenu.add(actions.pasteAction);

		JMenu statistics = new JMenu(flp.getString("statistics"));
		menuBar.add(statistics);

		statistics.add(actions.statisticalInfo);

		JMenu languages = new JMenu(flp.getString("languages"));
		menuBar.add(languages);

		languages.add(actions.englishLanguage);
		languages.add(actions.croatianLanguage);
		languages.add(actions.germanLanguage);

		JMenu tools = new JMenu(flp.getString("tools"));
		menuBar.add(tools);

		JMenu changeCase = new JMenu(flp.getString("changeCase"));
		tools.add(changeCase);

		changeCase.add(actions.toUpperCase);
		changeCase.add(actions.toLowerCase);
		changeCase.add(actions.invertCase);

		JMenu sort = new JMenu(flp.getString("sort"));
		tools.add(sort);

		sort.add(actions.ascending);
		sort.add(actions.descending);
		tools.add(actions.uniqueLines);

		flp.addLocalizationListener(() -> {
			fileMenu.setText(flp.getString("file"));
			editMenu.setText(flp.getString("edit"));
			statistics.setText(flp.getString("statistics"));
			languages.setText(flp.getString("languages"));
			tools.setText(flp.getString("tools"));
			changeCase.setText(flp.getString("changeCase"));
			sort.setText(flp.getString("sort"));
		});

		setJMenuBar(menuBar);
	}

	/**
	 * Gets text area from index in tabs.
	 * 
	 * @param index
	 *            Index.
	 * @return Text area.
	 */

	private JTextArea getTextArea(int index) {
		JScrollPane scrollPane = (JScrollPane) tabs.getComponentAt(index);
		JViewport text = scrollPane.getViewport();

		return (JTextArea) text.getView();
	}

	/**
	 * Adds new tab.
	 * 
	 * @param index
	 *            Index.
	 * @param title
	 *            Title of tab.
	 * @param content
	 *            Content in text area.
	 */

	void addTab(int index, String title, String content) {
		String tabTitle = String.format("Tab%d", ++tabCount);
		if (title == null) {
			tabPaths.add(null);
		}

		JLabel label = new JLabel(title == null ? "Untitled" : title, icons.saveIcon, JLabel.CENTER);
		JTextArea text = new JTextArea();
		text.setText(content);
		text.getDocument().addDocumentListener(docListener);
		text.addCaretListener(caretListener);
		text.getCaret().setVisible(true);

		tabs.addTab(tabTitle, new JScrollPane(text));

		JButton close = new JButton(icons.closeIcon);
		close.addActionListener(new RemoveTabListener(tabTitle));
		close.setOpaque(false);
		close.setPreferredSize(new Dimension(12, 12));

		JPanel panel = new JPanel(new FlowLayout());
		panel.add(label);
		panel.add(close);
		panel.setOpaque(false);

		tabs.setTabComponentAt(index, panel);
		tabs.setSelectedIndex(index);
		Path path = tabPaths.get(index);
		tabs.setToolTipTextAt(index, path == null ? flp.getString("empty") : path.toAbsolutePath().toString());
		
		flp.addLocalizationListener(() -> {
			if (index < tabs.getTabCount()) {
				tabs.setToolTipTextAt(index, path == null ? flp.getString("empty") : path.toAbsolutePath().toString());
			}
		});
		
		actions.saveAction.setEnabled(false);
		actions.saveAsAction.setEnabled(false);
	}

	/**
	 * Document listener that updates save actions and length label.
	 */

	private final DocumentListener docListener = new DocumentListener() {

		@Override
		public void insertUpdate(DocumentEvent e) {
			textChanged();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			textChanged();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textChanged();
		}

		private void textChanged() {
			JLabel label = (JLabel) ((JPanel) tabs.getTabComponentAt(tabs.getSelectedIndex())).getComponent(0);
			JTextArea editor = getTextArea(tabs.getSelectedIndex());
			label.setIcon(icons.notSavedIcon);

			((LJLabel) lengthL).setNumber(editor.getText().length());
			actions.saveAction.setEnabled(true);
			actions.saveAsAction.setEnabled(true);
		}

	};

	/**
	 * Listener that removes tab.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class RemoveTabListener implements ActionListener {
		/**
		 * Tab title.
		 */
		private String tabTitle;

		/**
		 * Constructor that gets tab title that it will remove.
		 * 
		 * @param tabTitle
		 *            Tab title.
		 */

		private RemoveTabListener(String tabTitle) {
			this.tabTitle = tabTitle;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			actions.removeTab(tabs.indexOfTab(tabTitle));
		}
	};

	/**
	 * Tab listener that sets availabilty of actions and updates status bar.
	 */

	private final ChangeListener tabListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (tabs.getTabCount() == 0) {
				JNotepadpp.this.setTitle("JNotepad++");
				actions.saveAction.setEnabled(false);
				actions.saveAsAction.setEnabled(false);
				actions.removeTab.setEnabled(false);
				actions.pasteAction.setEnabled(false);
				actions.statisticalInfo.setEnabled(false);
				changeSelectedTextActions(false);

				final int dashNumber = -1;
				((LJLabel) lengthL).setNumber(dashNumber);
				((LJLabel) lineL).setNumber(dashNumber);
				((LJLabel) columnL).setNumber(dashNumber);
				((LJLabel) selectedL).setNumber(dashNumber);

				return;
			}

			actions.removeTab.setEnabled(true);
			actions.statisticalInfo.setEnabled(true);
			actions.pasteAction.setEnabled(actions.copiedText != null);

			JTextArea editor = getTextArea(tabs.getSelectedIndex());
			editor.getCaret().setVisible(true);
			editor.getCaret().setSelectionVisible(true);
			setStatusBar(editor);

			int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
			changeSelectedTextActions(len > 0);

			((LJLabel) lengthL).setNumber(editor.getText().length());

			if ((tabs.getTabComponentAt(tabs.getSelectedIndex()) instanceof JPanel)
					&& ((JLabel) ((JPanel) tabs.getTabComponentAt(tabs.getSelectedIndex())).getComponent(0))
							.getIcon() == icons.saveIcon) {
				actions.saveAction.setEnabled(false);
				actions.saveAsAction.setEnabled(false);

			} else if ((tabs.getTabComponentAt(tabs.getSelectedIndex()) instanceof JPanel)) {
				actions.saveAction.setEnabled(true);
				actions.saveAsAction.setEnabled(true);
			}

			Path path = tabPaths.get(tabs.getSelectedIndex());
			String pathString = path == null ? "Untitled" : path.toAbsolutePath().toString();
			JNotepadpp.this.setTitle(String.format("%s - JNotepad++", pathString));
		}
	};

	/**
	 * Checks if there are unsaved documents. If there are asks users if he
	 * wants to save them or not. User can also cancel exiting the application.
	 * If there weren't unsaved documents or user didn't press cancel exits
	 * application.
	 */

	void exitApplication() {
		boolean unsaved = false;
		for (int i = 0; i < tabs.getTabCount(); i++) {
			if (((JLabel) ((JPanel) tabs.getTabComponentAt(i)).getComponent(0)).getIcon() == icons.notSavedIcon) {
				unsaved = true;
			}

			getTextArea(i).getCaret().deinstall(getTextArea(i));
		}

		if (unsaved) {
			int pressed = JOptionPane.showConfirmDialog(JNotepadpp.this,
					"There are unsaved changes. Do you wish to save changes before exiting?", "Exit",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (pressed == JOptionPane.CANCEL_OPTION || pressed == JOptionPane.CLOSED_OPTION) {
				return;
			}

			if (pressed == JOptionPane.YES_OPTION) {
				for (int i = 1; i < tabs.getComponentCount();) {
					if (!actions.removeTab(0)) {
						i++;
					}
				}
			}
		}

		dispose();
	}

	/**
	 * Caret listener that updates status bar.
	 */

	private final CaretListener caretListener = new CaretListener() {

		@Override
		public void caretUpdate(CaretEvent e) {
			JTextArea editor = getTextArea(tabs.getSelectedIndex());
			setStatusBar(editor);
		}

	};

	/**
	 * Updates status bar.
	 * 
	 * @param editor
	 *            Text editor.
	 */

	private void setStatusBar(JTextArea editor) {
		int dot = editor.getCaret().getDot();
		int mark = editor.getCaret().getMark();

		int selected = Math.abs(dot - mark);
		changeSelectedTextActions(selected > 0);

		int column = 0;
		int line = 1;
		char[] text = editor.getText().toCharArray();
		for (int i = 0; i < dot; i++) {
			if (!String.valueOf(text[i]).matches(".")) {
				line++;
				column = 0;
			} else {
				column++;
			}
		}

		((LJLabel) lineL).setNumber(line);
		((LJLabel) columnL).setNumber(column);
		((LJLabel) selectedL).setNumber(selected);
	}

	/**
	 * Changes availability of actions that are linked with selection of text.
	 * 
	 * @param status
	 *            Status.
	 */

	private void changeSelectedTextActions(boolean status) {
		actions.cutAction.setEnabled(status);
		actions.copyAction.setEnabled(status);
		actions.toUpperCase.setEnabled(status);
		actions.toLowerCase.setEnabled(status);
		actions.invertCase.setEnabled(status);
		actions.uniqueLines.setEnabled(status);
		actions.ascending.setEnabled(status);
		actions.descending.setEnabled(status);
	}

	/**
	 * Clock label that displays time and date in format "yyyy/mm/dd 00:00:00".
	 * Label is refreshed every second.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class Clock extends JLabel {
		/**
		 * Default serial version.
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * Checks if worker thread needs to be terminated.
		 */
		private volatile boolean stopRequested = false;
		/**
		 * Amount of time worker thread sleeps until next refresh of label.
		 */
		private final int sleepTime = 1000;

		/**
		 * Constructor that starts worker thread that refreshes clock every
		 * second.
		 */

		private Clock() {
			DateTimeFormatter time = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);

			Thread worker = new Thread(() -> {
				while (true) {
					SwingUtilities.invokeLater(() -> {
						LocalDateTime dateTime = LocalDateTime.now();
						setText(String.format("%d/%02d/%02d %s", dateTime.getYear(), dateTime.getMonth().getValue(),
								dateTime.getDayOfMonth(), dateTime.format(time)));
					});

					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException ignorable) {
					}

					if (stopRequested) {
						return;
					}
				}
			});

			worker.setDaemon(true);
			worker.start();
		}
	}
}

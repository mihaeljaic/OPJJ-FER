package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.color.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.color.JColorLabel;
import hr.fer.zemris.java.hw16.jvdraw.geometrical.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geometrical.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.geometrical.Line;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelImpl;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingObjectListModel;
import hr.fer.zemris.java.hw16.jvdraw.model.JDrawingCanvas;

/**
 * Program that offers user to draw geometrical objects. Supported geometrical
 * objects are: line, circle and filled circle. User can choose which one he
 * wants to draw from button group. User has to click on canvas to start
 * drawing. User can also pick foreground and background color. Foreground color
 * is used when drawing line, circle and filled circle's edges. Background color
 * is used for filling filled circle.<br>
 * Program shows user all drawn geometrical object in list. User can edit every
 * object. <br>
 * Program uses files with jvd extension. User can open(ctrl+O) existing jvd
 * files, save(ctrl+S) current document and can export(ctrl+E) current document
 * as image. Supported image formats for exporting are "jpg", "png" and "gif".
 * <p>
 * Jvd file contains text information about all geometrical objects. Each
 * objects represents one line. <br>
 * Line objects are formatted as: "LINE x0 y0 x1 y1 red green blue"<br>
 * Circle objects are formatted as: "CIRCLE centerx centery radius red green
 * blue"<br>
 * Filled circle objects are formatted as: "FCIRCLE centerx centery radius red
 * green blue red green blue".
 * </p>
 * 
 * @author Mihael Jaić
 *
 */

public class JVDraw extends JFrame {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;
	/** Flag that tracks unsaved changes on canvas. */
	private boolean changes;
	/** Draw button group. */
	private ButtonGroup drawGroup;
	/** Foreground color area. */
	private JColorArea fgColorArea;
	/** Background color area. */
	private JColorArea bgColorArea;
	/** Canvas. */
	private JDrawingCanvas canvas;
	/** Drawing model. */
	private DrawingModel drawingModel;
	/** Path to current document that is on canvas. */
	private Path savePath;

	/**
	 * Constructor that initializes GUI.
	 */

	public JVDraw() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setTitle("JVDraw - Untitled");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		initGUI();
	}

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JVDraw().setVisible(true));
	}

	/**
	 * Initializes GUI.
	 */

	private void initGUI() {
		drawingModel = new DrawingModelImpl();

		createCanvas();
		createList();

		createToolbar();
		createMenu();
	}

	/**
	 * Creates canvas.
	 */

	private void createCanvas() {
		canvas = new JDrawingCanvas(drawingModel);
		canvas.setOpaque(true);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (canvas.getClicked()) {
					String selectedShape = drawGroup.getSelection().getActionCommand();
					GeometricalObject newObject = null;

					if (selectedShape.equals("Line")) {
						newObject = new Line(canvas.getStart(), e.getPoint(), fgColorArea.getCurrentColor());
					} else if (selectedShape.equals("Circle")) {
						Point start = canvas.getStart();
						int radius = (int) Math.round(e.getPoint().distance(start));
						newObject = new Circle(start, radius, fgColorArea.getCurrentColor());
					} else {
						Point start = canvas.getStart();
						int radius = (int) Math.round(e.getPoint().distance(start));
						newObject = new FilledCircle(start, radius, fgColorArea.getCurrentColor(),
								bgColorArea.getCurrentColor());
					}

					drawingModel.add(newObject);

					canvas.setClicked(false);
					changes = true;
				} else {
					String selectedShape = drawGroup.getSelection().getActionCommand();
					canvas.setSelectedShape(selectedShape);
					canvas.setClicked(true);
					canvas.setStart(e.getPoint());
				}
			}
		});

		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (canvas.getClicked()) {
					canvas.setEnd(e.getPoint());
					canvas.repaint();
				}
			}
		});

		drawingModel.addDrawingModelListener(canvas);
	}

	/**
	 * Creates jlist.
	 */

	private void createList() {
		JPanel centralPanel = new JPanel(new BorderLayout());

		DrawingObjectListModel listModel = new DrawingObjectListModel();
		listModel.setDrawingModel(drawingModel);
		drawingModel.addDrawingModelListener(listModel);

		JList<GeometricalObject> jList = new JList<>(listModel);
		jList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					GeometricalObject selectedObject = jList.getSelectedValue();
					if (selectedObject.showProperties()) {
						changes = true;
						drawingModel.change(selectedObject);
					}
				}
			}
		});
		jList.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					GeometricalObject selectedObject = jList.getSelectedValue();

					if (selectedObject != null) {
						drawingModel.remove(selectedObject);
						changes = true;
					}
				}
			}
		});

		JScrollPane scroll = new JScrollPane(jList);
		scroll.setPreferredSize(new Dimension(200, 500));

		centralPanel.add(scroll, BorderLayout.EAST);
		centralPanel.add(canvas, BorderLayout.CENTER);

		add(centralPanel, BorderLayout.CENTER);
	}

	/**
	 * Creates toolbar.
	 */

	private void createToolbar() {
		JToolBar toolBar = new JToolBar();

		fgColorArea = new JColorArea(Color.RED);
		fgColorArea.setMinimumSize(fgColorArea.getPreferredSize());
		fgColorArea.setMaximumSize(fgColorArea.getPreferredSize());
		fgColorArea.addMouseListener(new ColorMouseListener(fgColorArea, "Choose foreground color"));
		toolBar.add(fgColorArea);
		toolBar.addSeparator();
		fgColorArea.addColorChangeListener(canvas);
		canvas.setFgColorProv(fgColorArea);

		bgColorArea = new JColorArea(Color.BLUE);
		bgColorArea.setMinimumSize(bgColorArea.getPreferredSize());
		bgColorArea.setMaximumSize(bgColorArea.getPreferredSize());
		bgColorArea.addMouseListener(new ColorMouseListener(bgColorArea, "Choose background color"));
		toolBar.add(bgColorArea);
		toolBar.addSeparator();
		bgColorArea.addColorChangeListener(canvas);
		canvas.setBgColorProv(bgColorArea);
		canvas.newColorSelected(null, null, null);

		JColorLabel label = new JColorLabel(fgColorArea, bgColorArea);
		add(label, BorderLayout.PAGE_END);

		drawGroup = new ButtonGroup();

		JToggleButton line = new JToggleButton("Line");
		line.addActionListener(e -> {
			canvas.setSelectedShape("Line");
		});
		line.setActionCommand("Line");
		line.setSelected(true);
		drawGroup.add(line);
		toolBar.add(line);
		toolBar.addSeparator();

		JToggleButton circle = new JToggleButton("Circle");
		circle.addActionListener(e -> {
			canvas.setSelectedShape("Circle");
		});
		circle.setActionCommand("Circle");
		drawGroup.add(circle);
		toolBar.add(circle);
		toolBar.addSeparator();

		JToggleButton filledCircle = new JToggleButton("Filled circle");
		filledCircle.addActionListener(e -> {
			canvas.setSelectedShape("Filled circle");
		});
		filledCircle.setActionCommand("Filled circle");
		drawGroup.add(filledCircle);
		toolBar.add(filledCircle);
		toolBar.addSeparator();

		add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Mouse listener that listens to mouse clicks on color area's and activates
	 * color changing options on click.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class ColorMouseListener extends MouseAdapter {
		/** Color area. */
		private JColorArea colorArea;
		/** Option text. */
		private String text;

		/**
		 * Constructor that sets all attributes.
		 * 
		 * @param colorArea
		 *            Color area.
		 * @param text
		 *            Option text.
		 */

		public ColorMouseListener(JColorArea colorArea, String text) {
			super();
			this.colorArea = colorArea;
			this.text = text;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Color newColor = JColorChooser.showDialog(JVDraw.this, text, colorArea.getCurrentColor());
			if (newColor != null) {
				colorArea.setSelectedColor(newColor);
				colorArea.repaint();
			}
		}
	}

	/**
	 * Creates menu.
	 */

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu("File");
		menuBar.add(file);

		OpenAction openAction = new OpenAction();
		openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		openAction.putValue(Action.NAME, "Open");
		openAction.putValue(Action.SHORT_DESCRIPTION, "Opens new document.");
		JMenuItem open = new JMenuItem(openAction);
		file.add(open);

		SaveAction saveAction = new SaveAction();
		saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveAction.putValue(Action.NAME, "Save");
		saveAction.putValue(Action.SHORT_DESCRIPTION, "Saves document.");
		JMenuItem save = new JMenuItem(saveAction);
		file.add(save);

		SaveAsAction saveAsAction = new SaveAsAction();
		saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		saveAsAction.putValue(Action.NAME, "Save as");
		saveAsAction.putValue(Action.SHORT_DESCRIPTION, "Saves document as specified file.");
		JMenuItem saveAs = new JMenuItem(saveAsAction);
		file.add(saveAs);

		ExportAction exportAction = new ExportAction();
		exportAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control E"));
		exportAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		exportAction.putValue(Action.NAME, "Export");
		exportAction.putValue(Action.SHORT_DESCRIPTION, "Exports document as image.");
		JMenuItem export = new JMenuItem(exportAction);
		file.add(export);
		file.addSeparator();

		ExitAction exitAction = new ExitAction();
		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt E"));
		exitAction.putValue(Action.NAME, "Exit");
		exitAction.putValue(Action.SHORT_DESCRIPTION, "Exits program.");
		JMenuItem exit = new JMenuItem(exitAction);
		file.add(exit);

		setJMenuBar(menuBar);
	}

	/**
	 * Offers user to exit application.
	 */

	private void exit() {
		if (!changes) {
			dispose();
			return;
		}

		int pressed = JOptionPane.showConfirmDialog(JVDraw.this,
				"There are unsaved changes. Do you wish to save changes before exiting?", "Exit",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if (pressed == JOptionPane.CANCEL_OPTION || pressed == JOptionPane.CLOSED_OPTION) {
			return;
		}

		if (pressed == JOptionPane.YES_OPTION) {
			if (savePath == null) {
				saveAs();
			} else {
				save(savePath);
			}
		}

		dispose();
	}

	/**
	 * Action that opens new document. New document needs to have .jvd
	 * extension. For .jvd file format see {@link JVDraw}.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class OpenAction extends AbstractAction {
		/** Default serial version. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (changes) {
				int pressed = JOptionPane.showConfirmDialog(JVDraw.this,
						"There are unsaved changes. Do you wish to save changes before opening document?",
						"Save changes?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

				if (pressed == JOptionPane.CANCEL_OPTION || pressed == JOptionPane.CLOSED_OPTION) {
					return;
				}

				if (pressed == JOptionPane.YES_OPTION) {
					if (savePath == null) {
						saveAs();

					} else {
						save(savePath);
					}
				}
			}

			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JVDraw files", "jvd");
			fc.setFileFilter(filter);

			fc.setDialogTitle("Open file");
			if (fc.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File fileName = fc.getSelectedFile();
			if (!fileName.getPath().endsWith(".jvd")) {
				showError("Invalid file extension. Program accepts only files with .jvd extensions", "Error");
				return;
			}

			List<String> content = null;
			try {
				content = Files.readAllLines(fileName.toPath(), StandardCharsets.UTF_8);
			} catch (IOException e1) {
				showError(String.format("Couldn't read from file %s!", fileName.getPath()), "Error");
				return;
			}

			List<GeometricalObject> objects = parseContent(content);
			if (objects == null) {
				return;
			}

			drawingModel.clear();
			for (GeometricalObject object : objects) {
				drawingModel.add(object);
			}

			savePath = fileName.toPath();
			JVDraw.this.setTitle(String.format("JVDraw - %s", savePath.toAbsolutePath()));
			changes = false;
		}

		/**
		 * Parses all geometrical objects from given jvd file.
		 * 
		 * @param content
		 *            Jvd file content.
		 * @return List of geometrical objects parsed from jvd file.
		 */

		private List<GeometricalObject> parseContent(List<String> content) {
			List<GeometricalObject> objects = new ArrayList<>();

			for (String line : content) {
				String[] data = line.split("\\s+");

				if (data[0].equals("LINE")) {
					Line drawnLine = createLine(data);
					if (drawnLine == null)
						return null;
					objects.add(drawnLine);

				} else if (data[0].equals("CIRCLE")) {
					Circle circle = createCircle(data);
					if (circle == null)
						return null;
					objects.add(circle);

				} else if (data[0].equals("FCIRCLE")) {
					FilledCircle filledCircle = createFilledCircle(data);
					if (filledCircle == null)
						return null;
					objects.add(filledCircle);

				} else {
					showError("Invalid data in file!", "Couldn't open file!");
					return null;
				}
			}

			return objects;
		}

		/**
		 * Creates line geometrical object from jvd file.
		 * 
		 * @param data
		 *            Text line from jvd file.
		 * @return Line geometrical object parsed from given text.
		 */

		private Line createLine(String[] data) {
			if (data.length != 8) {
				showError("Invalid arguments for LINE object!", "Couldn't open file!");
				return null;
			}

			try {
				return new Line(new Point(Integer.valueOf(data[1]), Integer.valueOf(data[2])),
						new Point(Integer.valueOf(data[3]), Integer.valueOf(data[4])),
						new Color(Integer.valueOf(data[5]), Integer.valueOf(data[6]), Integer.valueOf(data[7])));
			} catch (IllegalArgumentException e) {
				showError("Invalid arguments for LINE object!", "Couldn't open file!");
				return null;
			}
		}

		/**
		 * Creates circle geometrical object from jvd file.
		 * 
		 * @param data
		 *            Text line from jvd file.
		 * @return Circle geometrical object parsed from given text.
		 */

		private Circle createCircle(String[] data) {
			if (data.length != 7) {
				showError("Invalid arguments for CIRCLE object!", "Couldn't open file!");
				return null;
			}

			try {
				return new Circle(new Point(Integer.valueOf(data[1]), Integer.valueOf(data[2])),
						Integer.valueOf(data[3]),
						new Color(Integer.valueOf(data[4]), Integer.valueOf(data[5]), Integer.valueOf(data[6])));
			} catch (IllegalArgumentException e) {
				showError("Invalid arguments for CIRCLE object!", "Couldn't open file!");
				return null;
			}
		}

		/**
		 * Creates filled circle geometrical object from jvd file.
		 * 
		 * @param data
		 *            Text line from jvd file.
		 * @return Filled circle geometrical object parsed from given text.
		 */

		private FilledCircle createFilledCircle(String[] data) {
			if (data.length != 10) {
				showError("Invalid arguments for FCIRCLE object!", "Couldn't open file!");
				return null;
			}

			try {
				return new FilledCircle(new Point(Integer.valueOf(data[1]), Integer.valueOf(data[2])),
						Integer.valueOf(data[3]),
						new Color(Integer.valueOf(data[4]), Integer.valueOf(data[5]), Integer.valueOf(data[6])),
						new Color(Integer.valueOf(data[7]), Integer.valueOf(data[8]), Integer.valueOf(data[9])));
			} catch (IllegalArgumentException e) {
				showError("Invalid arguments for FCIRCLE object!", "Couldn't open file!");
				return null;
			}
		}

	}

	/**
	 * Action that saves document. If document's path is unknown offers user to
	 * save document as.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class SaveAction extends AbstractAction {
		/** Default serial version. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!changes) {
				JOptionPane.showMessageDialog(JVDraw.this, "No changes on document detected.", "Didn't save file",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if (savePath == null) {
				saveAs();
			} else {
				save(savePath);
			}
		}

	}

	/**
	 * Action that offers user to choose destination file in which will current
	 * document be saved.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class SaveAsAction extends AbstractAction {
		/** Default serial version. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveAs();
		}

	}

	/**
	 * Offers user to choose destination file in which current document will be
	 * saved.
	 */

	private void saveAs() {
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JVDraw files", "jvd");
		fc.setFileFilter(filter);

		fc.setDialogTitle("Save file as");
		if (fc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(JVDraw.this, "Didn't save the file.", "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		File selectedFile = fc.getSelectedFile();
		String path = selectedFile.getAbsolutePath();
		if (!path.endsWith(".jvd")) {
			selectedFile = new File(path + ".jvd");
		}

		if (selectedFile.exists()) {
			int pressed = JOptionPane.showConfirmDialog(JVDraw.this,
					"File already exists. Would you like to overwrite file?", "Overwrite", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);

			if (pressed != JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(JVDraw.this, "Didn't save the file.", "Information",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

		} else {
			selectedFile.getParentFile().mkdirs();
			try {
				selectedFile.createNewFile();
			} catch (IOException e1) {
				showError("Couldn't create file.", "Error");
				return;
			}
		}

		if (save(selectedFile.toPath())) {
			JVDraw.this.setTitle(String.format("JVDraw - %s", savePath.toAbsolutePath()));
		}
	}

	/**
	 * Saves document to given path.
	 * 
	 * @param path
	 *            Path to file
	 * @return True if file was saved successfully. False otherwise.
	 */

	private boolean save(Path path) {
		List<String> data = createData();

		try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			for (String line : data) {
				bw.write(line);
			}
		} catch (IOException e) {
			showError("Error while saving file.", "Error");
			return false;
		}

		changes = false;
		savePath = path;
		JOptionPane.showMessageDialog(JVDraw.this, "File has been successfully saved.", "File saved",
				JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	/**
	 * Creates text data for jvd file for all geometrical objects on canvas.
	 * 
	 * @return Lines that form jvd file.
	 */

	private List<String> createData() {
		List<String> data = new ArrayList<>();
		for (int i = 0, size = drawingModel.getSize(); i < size; i++) {
			GeometricalObject object = drawingModel.getObject(i);

			if (object instanceof Line) {
				Line line = (Line) object;
				data.add(String.format("LINE %d %d %d %d %d %d %d%n", line.getStart().x, line.getStart().y,
						line.getEnd().x, line.getEnd().y, line.getColor().getRed(), line.getColor().getGreen(),
						line.getColor().getBlue()));

			} else if (object instanceof Circle) {
				Circle circ = (Circle) object;
				Color col = circ.getColor();
				data.add(String.format("CIRCLE %d %d %d %d %d %d%n", circ.getCenter().x, circ.getCenter().y,
						circ.getRadius(), col.getRed(), col.getGreen(), col.getBlue()));
			} else {
				FilledCircle fCirc = (FilledCircle) object;
				Color fgCol = fCirc.getFgColor();
				Color bgCol = fCirc.getBgColor();

				data.add(String.format("FCIRCLE %d %d %d %d %d %d %d %d %d%n", fCirc.getCenter().x, fCirc.getCenter().y,
						fCirc.getRadius(), fgCol.getRed(), fgCol.getGreen(), fgCol.getBlue(), bgCol.getRed(),
						bgCol.getGreen(), bgCol.getBlue()));
			}
		}

		return data;
	}

	/**
	 * Action that exports current file on canvas as image to specified file.
	 * File has to be in "jpg", "png" or "gif" format.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class ExportAction extends AbstractAction {
		/** Default serial version. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (drawingModel.getSize() == 0) {
				JOptionPane.showMessageDialog(JVDraw.this, "Nothing to export. Canvas is empty", "Empty canvas",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif");
			fc.setFileFilter(filter);

			fc.setDialogTitle("Export file");
			if (fc.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File selectedFile = fc.getSelectedFile();
			String fileName = selectedFile.getPath();
			String extension = "";

			int i = fileName.lastIndexOf('.');
			int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

			if (i > p) {
				extension = fileName.substring(i + 1);
			}
			if (!(extension.equals("jpg") || extension.equals("png") || extension.equals("gif"))) {
				showError("Only files with jpg, png and gif extensions are valid.", "Invalid extension");
				return;
			}

			if (selectedFile.exists()) {
				int pressed = JOptionPane.showConfirmDialog(JVDraw.this,
						"File already exists. Would you like to overwrite file?", "Overwrite",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (pressed != JOptionPane.YES_OPTION) {
					JOptionPane.showMessageDialog(JVDraw.this, "Didn't export the file.", "Information",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			} else {
				selectedFile.getParentFile().mkdirs();
				try {
					selectedFile.createNewFile();
				} catch (IOException e1) {
					showError("Couldn't create file.", "Error");
					return;
				}
			}

			BoundingBox box = calculateBoundingBox();
			BufferedImage image = new BufferedImage(box.maxX - box.minX, box.maxY - box.minY,
					BufferedImage.TYPE_3BYTE_BGR);
			drawImage(image.createGraphics(), box);

			try {
				ImageIO.write(image, extension, selectedFile);
			} catch (IOException e1) {
				showError("Couldn't create image file", "Error");
				return;
			}

			JOptionPane.showMessageDialog(JVDraw.this, "File successfully exported.", "Information",
					JOptionPane.INFORMATION_MESSAGE);
		}

		/**
		 * Draws all geometrical objects translated according to bounding box.
		 * So image has minimal size.
		 * 
		 * @param g2d
		 *            Graphics.
		 * @param box
		 *            Bounding box.
		 */

		private void drawImage(Graphics2D g2d, BoundingBox box) {
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, box.maxX - box.minX, box.maxY - box.minY);
			for (int i = 0, size = drawingModel.getSize(); i < size; i++) {
				drawingModel.getObject(i).paint(g2d, box.minX, box.minY);
			}

			g2d.dispose();
		}

	}

	/**
	 * Action that offers user to exit application.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class ExitAction extends AbstractAction {
		/** Default serial version. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exit();
		}

	}

	/**
	 * Class that models bounding box that is used while drawing image for
	 * exporting.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	public static class BoundingBox {
		/** Minimum x object coordinate */
		private int minX;
		/** Minimum y object coordinate. */
		private int minY;
		/** Maximum x object coordinate. */
		private int maxX;
		/** Maximum y object coordinate. */
		private int maxY;

		/**
		 * Constructor that sets all attributes.
		 * 
		 * @param minX
		 *            Minimum x object coordinate.
		 * @param minY
		 *            Minimum y object coordinate.
		 * @param maxX
		 *            Maximum x object coordinate.
		 * @param maxY
		 *            Maximum y object coordinate.
		 */

		public BoundingBox(int minX, int minY, int maxX, int maxY) {
			super();
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
		}
	}

	/**
	 * Calculates bounding box for all geometrical objects displayed on canvas.
	 * 
	 * @return Bounding box for all geometrical objects displayed on canvas.
	 */

	private BoundingBox calculateBoundingBox() {
		BoundingBox box = new BoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);

		for (int i = 0, size = drawingModel.getSize(); i < size; i++) {
			GeometricalObject object = drawingModel.getObject(i);
			BoundingBox objectBox = object.getBoundingBox();

			box.minX = Math.min(box.minX, objectBox.minX);
			box.minY = Math.min(box.minY, objectBox.minY);
			box.maxX = Math.max(box.maxX, objectBox.maxX);
			box.maxY = Math.max(box.maxY, objectBox.maxY);
		}

		return box;
	}

	/**
	 * Shows dialog with error message.
	 * 
	 * @param message
	 *            Error message.
	 * @param title
	 *            Error title.
	 */

	private void showError(String message, String title) {
		JOptionPane.showMessageDialog(JVDraw.this, message, title, JOptionPane.ERROR_MESSAGE);
	}

}

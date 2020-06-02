package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program that draws bar chart by given input data. Program accepts command
 * line argument that is path to given file. In file needs to be data for
 * drawing bar chart it needs to be in following form: First line is description
 * of x axis, second line is description of y axis. In third line are all values
 * of pairs of x and y values. Pairs need to be separated with space and values
 * need to be separated by comma. Fourth line is maximum y value, fifth line is
 * minium y value and last(sixth) line is difference between each y value that
 * will be drawn on graph. All numbers are integers and need to be non-negative.
 * Maximum y value has to be greater than minium y value and difference has to
 * be positive number.
 * <p>
 * Run program with './src/main/resources/demonstration.txt' for demonstration.
 * </p>
 * 
 * @author Mihael Jaić
 *
 */

public class BarChartDemo extends JFrame {
	/**
	 * Default serial id.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Bar chart.
	 */
	private BarChart barChart;
	/**
	 * Path.
	 */
	private String path;

	/**
	 * Constructor that initializes GUI.
	 * 
	 * @param barChart
	 *            Bar chart.
	 * @param path
	 *            Path.
	 */

	public BarChartDemo(BarChart barChart, String path) {
		if (barChart == null || path == null) {
			throw new IllegalArgumentException();
		}

		this.barChart = barChart;
		this.path = path;

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Bar chart demo");

		initGUI();

		pack();
		setLocationRelativeTo(null);
		setSize(320, 280);
	}

	/**
	 * Initializes GUI.
	 */

	private void initGUI() {
		JPanel panel = new JPanel(new BorderLayout());

		JLabel label = new JLabel(path);
		label.setHorizontalAlignment(JLabel.CENTER);
		panel.add(label, BorderLayout.NORTH);

		panel.add(new BarChartComponent(barChart));

		getContentPane().add(panel);
		setMinimumSize(getContentPane().getMinimumSize());
	}

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Expected argument for path to file.");
			System.exit(1);
		}

		StringBuilder sb = new StringBuilder();
		for (String s : args) {
			sb.append(s);
			sb.append(" ");
		}

		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}

		SwingUtilities.invokeLater(() -> {
			new BarChartDemo(readFile(sb.toString()), sb.toString()).setVisible(true);
		});
	}

	/**
	 * Reads and parses data from given file.
	 * 
	 * @param path
	 *            Path.
	 * @return Data that was extracted from given file.
	 */

	private static BarChart readFile(String path) {
		Path file = null;
		BarChart barChart = null;
		try {
			file = Paths.get(path);
		} catch (InvalidPathException ex) {
			error("Invalid path.");
		}

		try (BufferedReader reader = Files.newBufferedReader(file)) {
			List<XYValue> values = new ArrayList<>();

			String xDescription = reader.readLine();
			String yDescription = reader.readLine();

			String coordinates = reader.readLine();
			if (!coordinates.matches("[0-9]+,[0-9]+(\\s+[0-9]+,[0-9]+)*")) {
				error("Invalid data for bar chart.");
			}

			for (String s : coordinates.split("\\s+")) {
				String[] temp = s.split(",");

				values.add(new XYValue(Integer.parseInt(temp[0]), Integer.parseInt(temp[1])));
			}

			String temp1 = reader.readLine();
			String temp2 = reader.readLine();
			String temp3 = reader.readLine();

			int minimalY = 0;
			int maximalY = 0;
			int difference = 0;
			try {
				minimalY = Integer.parseInt(temp1);
				maximalY = Integer.parseInt(temp2);
				difference = Integer.parseInt(temp3);

			} catch (NumberFormatException ex) {
				error("Invalid data for bar chart.");
			}

			try {
				barChart = new BarChart(values, xDescription, yDescription, minimalY, maximalY, difference);
			} catch (IllegalArgumentException ex) {
				error("Invalid data for bar chart.");
			}

		} catch (IOException e) {
			error("Couldn't read from file.");
		}

		return barChart;
	}

	/**
	 * Method exits program with writing appropriate message to user.
	 * 
	 * @param message
	 *            Message.
	 */

	private static void error(String message) {
		System.out.println(message);
		System.exit(1);
	}
}

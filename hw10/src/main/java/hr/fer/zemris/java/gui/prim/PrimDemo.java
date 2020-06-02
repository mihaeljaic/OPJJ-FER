package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program that prints out prime numbers in two lists. This program is
 * demonstration of {@link PrimListModel} class. For generating next prime
 * number press next button.
 * 
 * @author Mihael Jaić
 *
 */

public class PrimDemo extends JFrame {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that initializes GUI.
	 */

	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Prim demo");

		initGUI();

		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * Initializes GUI.
	 */

	private void initGUI() {
		JPanel panel = new JPanel(new BorderLayout());

		PrimListModel listModel = new PrimListModel();

		JScrollPane list1 = new JScrollPane(new JList<>(listModel));
		JScrollPane list2 = new JScrollPane(new JList<>(listModel));

		JPanel grid = new JPanel(new GridLayout(1, 2));
		grid.add(list1);
		grid.add(list2);

		panel.add(grid, BorderLayout.CENTER);

		JButton next = new JButton("Next");
		next.addActionListener(e -> {
			listModel.next();
		});

		panel.add(next, BorderLayout.SOUTH);

		getContentPane().add(panel);
	}

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new PrimDemo().setVisible(true);
		});
	}

}

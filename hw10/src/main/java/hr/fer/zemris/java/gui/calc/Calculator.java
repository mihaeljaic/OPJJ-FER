package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Calculator program that offers some standard binary and unary operations like
 * trigonometric functions and arithemtic operations. Numbers are added to
 * screen by pressing number buttons. Unary operations are calculated
 * immediately if number is given. Binary operations need two numbers and in
 * order to calculate them user needs to press '=' button. If result of
 * operation is 'INFINITY' or 'NaN' user can clear screen or start typing
 * numbers and letters will disappear. Program also offers pushing displayed
 * number on screen on stack, and can later get them back on screen with pop
 * command. With activating 'inv' button user can switch some functions to their
 * inverse functions.
 * 
 * @author Mihael Jaić
 *
 */

public class Calculator extends JFrame {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Mapped unary operations.
	 */
	private Map<String, Function<Double, Double>> operations;
	/**
	 * Mapped binary operations.
	 */
	private Map<String, BiFunction<Double, Double, Double>> binaryOperations;
	/**
	 * Remembers if dot is present on screen.
	 */
	private boolean dot;
	/**
	 * Remembers last operator.
	 */
	private String lastOperator;
	/**
	 * Label text that represents screen of calculator.
	 */
	private JLabel screen;
	/**
	 * Previous number that was on screen.
	 */
	private String lastNumber;
	/**
	 * Used for comparing real numbers.
	 */
	private static double epsilon = 1e-9;
	/**
	 * Stack.
	 */
	private Stack<String> stack;

	/**
	 * Constructor that initializes GUI.
	 */

	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("My first calculator");

		initGUI();

		pack();

		setLocationRelativeTo(null);
	}

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator().setVisible(true);
		});
	}

	/**
	 * Initializes GUI.
	 */

	private void initGUI() {
		JPanel panel = new JPanel(new CalcLayout(5));

		screen = new JLabel("", SwingConstants.RIGHT);
		screen.setBackground(Color.ORANGE);
		screen.setOpaque(true);
		panel.add(screen, new RCPosition(1, 1));

		addNumbers(panel);

		JButton nthPower = new JButton("x^n");
		addOperators(panel, nthPower);

		addFunctions(panel, nthPower);

		JButton clear = new JButton("clr");
		clear.addActionListener(e -> {
			screen.setText("");
			dot = false;
		});

		JButton reset = new JButton("res");
		reset.addActionListener(e -> {
			screen.setText("");
			dot = false;
			lastNumber = null;
			lastOperator = null;
			stack.clear();
		});

		stack = new Stack<>();
		JButton push = new JButton("push");
		push.addActionListener(e -> {
			String number = screen.getText();
			if (number.isEmpty()) {
				return;
			}
			stack.push(number);
		});

		JButton pop = new JButton("pop");
		pop.addActionListener(e -> {
			if (stack.isEmpty()) {
				screen.setText("Stack is empty!");
				return;
			}
			String text = stack.pop();
			screen.setText(text);
			dot = text.contains(".");
		});

		panel.add(clear, new RCPosition(1, 7));
		panel.add(reset, new RCPosition(2, 7));
		panel.add(push, new RCPosition(3, 7));
		panel.add(pop, new RCPosition(4, 7));

		getContentPane().add(panel);

		setMinimumSize(getContentPane().getMinimumSize());
	}

	/**
	 * Adds binary operators on screen.
	 * 
	 * @param panel
	 *            Panel.
	 * @param nthPower
	 *            Button that will receive operator listener.
	 */

	private void addOperators(JPanel panel, JButton nthPower) {
		OperationListener operationListener = new OperationListener();

		binaryOperations = new HashMap<>();
		binaryOperations.put("+", (a, b) -> a + b);
		binaryOperations.put("-", (a, b) -> a - b);
		binaryOperations.put("/", (a, b) -> a / b);
		binaryOperations.put("*", (a, b) -> a * b);
		binaryOperations.put("x^n", (x, n) -> Math.pow(x, n));
		binaryOperations.put("x^(1/n)", (x, n) -> Math.pow(x, 1 / n));

		JButton calculate = new JButton("=");
		calculate.addActionListener(e -> {
			if (lastOperator == null) {
				return;
			}
			String secondNumber = screen.getText();
			if (secondNumber.isEmpty()) {
				return;
			}

			double first = Double.parseDouble(lastNumber);
			double second = Double.parseDouble(secondNumber);

			double result = binaryOperations.get(lastOperator).apply(first, second);

			if (Math.abs(result - (int) result) < epsilon) {
				dot = false;
				screen.setText(Integer.toString((int) result));
			} else {
				dot = true;
				screen.setText(Double.toString(result));
			}

			lastOperator = null;
			lastNumber = null;
		});

		JButton division = new JButton("/");
		division.addActionListener(operationListener);

		JButton multiply = new JButton("*");
		multiply.addActionListener(operationListener);

		JButton sub = new JButton("-");
		sub.addActionListener(operationListener);

		JButton add = new JButton("+");
		add.addActionListener(operationListener);

		nthPower.addActionListener(operationListener);

		panel.add(calculate, new RCPosition(1, 6));
		panel.add(division, new RCPosition(2, 6));
		panel.add(multiply, new RCPosition(3, 6));
		panel.add(sub, new RCPosition(4, 6));
		panel.add(add, new RCPosition(5, 6));
		panel.add(nthPower, new RCPosition(5, 1));
	}

	/**
	 * Adds numbers, dot and sign change buttons to GUI.
	 * 
	 * @param panel
	 *            Panel.
	 */

	private void addNumbers(JPanel panel) {
		JButton[] numbers = new JButton[10];

		NumberListener numberListener = new NumberListener();
		for (int i = 0; i < 10; i++) {
			numbers[i] = new JButton(Integer.toString(i));
			numbers[i].addActionListener(numberListener);
		}

		JButton dot = new JButton(".");
		dot.addActionListener(numberListener);
		JButton sign = new JButton("+/-");
		sign.addActionListener(numberListener);

		panel.add(numbers[7], new RCPosition(2, 3));
		panel.add(numbers[8], new RCPosition(2, 4));
		panel.add(numbers[9], new RCPosition(2, 5));
		panel.add(numbers[4], new RCPosition(3, 3));
		panel.add(numbers[5], new RCPosition(3, 4));
		panel.add(numbers[6], new RCPosition(3, 5));
		panel.add(numbers[1], new RCPosition(4, 3));
		panel.add(numbers[2], new RCPosition(4, 4));
		panel.add(numbers[3], new RCPosition(4, 5));
		panel.add(numbers[0], new RCPosition(5, 3));
		panel.add(sign, new RCPosition(5, 4));
		panel.add(dot, new RCPosition(5, 5));
	}

	/**
	 * Adds functions to GUI.
	 * 
	 * @param panel
	 *            Panel
	 * @param nthPower
	 *            Adds text change to button.
	 */

	private void addFunctions(JPanel panel, JButton nthPower) {
		FunctionListener function = new FunctionListener();
		operations = new HashMap<>();

		JButton oneOverX = new JButton("1/x");
		operations.put("1/x", x -> 1 / x);
		oneOverX.addActionListener(function);

		JButton log = new JButton("log");
		operations.put("log", x -> Math.log10(x));
		operations.put("10^", x -> Math.pow(10, x));
		log.addActionListener(function);

		JButton ln = new JButton("ln");
		operations.put("ln", x -> Math.log(x));
		operations.put("e^", x -> Math.pow(Math.E, x));
		ln.addActionListener(function);

		JButton sin = new JButton("sin");
		operations.put("sin", x -> Math.sin(x));
		operations.put("asin", x -> Math.asin(x));
		sin.addActionListener(function);

		JButton cos = new JButton("cos");
		operations.put("cos", x -> Math.cos(x));
		operations.put("acos", x -> Math.acos(x));
		cos.addActionListener(function);

		JButton tan = new JButton("tan");
		operations.put("tan", x -> Math.tan(x));
		operations.put("atan", x -> Math.atan(x));
		tan.addActionListener(function);

		JButton ctg = new JButton("ctg");
		operations.put("ctg", x -> 1 / Math.tan(x));
		operations.put("actg", x -> Math.tan(1 / x));
		ctg.addActionListener(function);

		JCheckBox inverse = new JCheckBox("Inv");
		inverse.addActionListener(e -> {
			if (inverse.isSelected()) {
				log.setText("10^");
				ln.setText("e^");
				nthPower.setText("x^(1/n)");
				sin.setText("asin");
				cos.setText("acos");
				tan.setText("atan");
				ctg.setText("actg");
			} else {
				log.setText("log");
				ln.setText("ln");
				nthPower.setText("x^n");
				sin.setText("sin");
				cos.setText("cos");
				tan.setText("tg");
				ctg.setText("ctg");
			}
		});

		panel.add(oneOverX, new RCPosition(2, 1));
		panel.add(sin, new RCPosition(2, 2));
		panel.add(log, new RCPosition(3, 1));
		panel.add(cos, new RCPosition(3, 2));
		panel.add(ln, new RCPosition(4, 1));
		panel.add(tan, new RCPosition(4, 2));
		panel.add(ctg, new RCPosition(5, 2));
		panel.add(inverse, new RCPosition(5, 7));
	}

	/**
	 * Function listener that performs function strategy on given number.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class FunctionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton function = (JButton) e.getSource();

			String number = screen.getText();
			if (!number.matches("((-)?[0-9]+)|((-)?[0-9]*[.][0-9]+)")) {
				return;
			}
			Double result = operations.get(function.getText()).apply(Double.parseDouble(number));
			if (Math.abs(result - result.intValue()) < epsilon) {
				dot = false;
				screen.setText(Integer.toString(result.intValue()));
			} else {
				screen.setText(Double.toString(result));
				dot = Double.isFinite(result);
			}
		}

	}

	/**
	 * Listener that adds typed numbers to screen. Or changes sign if it is sign
	 * button that was pressed.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class NumberListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			String number = button.getText();
			if (!screen.getText().matches("(-)?[0-9.]*")) {
				screen.setText("");
			}

			if (number.equals(".")) {
				if (dot) {
					return;
				}

				dot = true;
			}

			if (number.equals("+/-")) {
				String text = screen.getText();
				if (text.startsWith("-")) {
					screen.setText(text.substring(1));
				} else {
					screen.setText("-" + text);
				}
				return;
			}

			screen.setText(screen.getText() + number);
		}

	}

	/**
	 * Operation listener that remembers button on screen and current operator
	 * so user can enter second number to perform operation.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class OperationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (lastOperator != null) {
				return;
			}

			lastNumber = screen.getText();
			if (!lastNumber.matches("((-)?[0-9]+)|((-)?[0-9]*[.][0-9]+)")) {
				return;
			}

			JButton button = (JButton) e.getSource();
			lastOperator = button.getText();

			screen.setText("");
			dot = false;
		}

	}
}

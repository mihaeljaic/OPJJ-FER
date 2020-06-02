package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model for complex numbers. Complex numbers have their real and imaginary
 * part. Offers standard operations with complex numbers like powering,
 * dividing, etc. Object is immutable. When performing operations like add new
 * complex number is created and old one remains the same.
 * 
 * @author Mihael Jaić
 *
 */

public class Complex {
	/**
	 * Real part.
	 */
	private double real;
	/**
	 * Imaginary part.
	 */
	private double imaginary;
	/**
	 * Module.
	 */
	private double module;
	/**
	 * Angle.
	 */
	private double angle;
	/**
	 * Used for comparing real numbers with zero.
	 */
	private static final double epsilon = 1e-6;

	/**
	 * Complex number with zero real and imaginary part.
	 */
	public static final Complex ZERO = new Complex(0, 0);
	/**
	 * Complex number with real part equal to one and imaginary part zero.
	 */
	public static final Complex ONE = new Complex(1, 0);
	/**
	 * Complex number with negative part equal to minus one and imaginary zero.
	 */
	public static final Complex ONE_NEG = new Complex(-1, 0);
	/**
	 * Complex number with real part zero and imaginary part one.
	 */
	public static final Complex IM = new Complex(0, 1);
	/**
	 * Complex number with imaginary part -1.
	 */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Constructor that sets imaginary and real part to zero.
	 */

	public Complex() {
		this(0, 0);
	}

	/**
	 * Constructor that sets real and imaginary values and also calulates module
	 * and angle of complex number.
	 * 
	 * @param re
	 *            Real part.
	 * @param im
	 *            Imaginary part.
	 */

	public Complex(double re, double im) {
		real = re;
		imaginary = im;

		calculateModuleAndAngle();
	}

	/**
	 * Parses complex number from given expression. Expression has to be in
	 * format a + ib or a - ib. Where a is real part and b imaginary. Real or imaginary
	 * part can be dropped of but one of them has to be present.
	 * 
	 * @param expression
	 *            Expression.
	 * @throws IllegalArgumentException
	 *             If expression is invalid.
	 */

	public Complex(String expression) throws IllegalArgumentException {
		Pattern pattern = Pattern.compile(
				"(\\s*(-)?[0-9]+(.)?[0-9]*\\s*((\\+|-)\\s*i([0-9]+(.)?[0-9]*)?)?\\s*)|(\\s*(-)?i([0-9]+(.)?[0-9]*)?\\s*)");
		Matcher matcher = pattern.matcher(expression);

		if (!matcher.matches()) {
			throw new IllegalArgumentException();
		}

		extract(expression);

		calculateModuleAndAngle();
	}

	/**
	 * Extracts complex number from given expression.
	 * 
	 * @param expression
	 *            Expression.
	 */

	private void extract(String expression) {
		String[] complex = expression.trim().split("(?=i)|(?=\\-)|(?=\\+)");

		if (complex.length == 1) {
			if (complex[0].startsWith("i")) {
				imaginary = complex[0].length() == 1 ? 1 : Double.parseDouble(complex[0].substring(1));

			} else {
				real = Double.parseDouble(complex[0]);
			}

		} else if (complex.length == 2) {
			imaginary = -(complex[1].length() == 1 ? 1 : Double.parseDouble(complex[1].substring(1)));

		} else {
			real = Double.parseDouble(complex[0]);
			imaginary = complex[2].length() == 1 ? 1 : Double.parseDouble(complex[2].substring(1));
			imaginary = complex[1].startsWith("-") ? -imaginary : imaginary;
		}
	}

	/**
	 * Calculates module and angle.
	 */

	private void calculateModuleAndAngle() {
		module = Math.sqrt(real * real + imaginary * imaginary);

		double temp = Math.atan2(imaginary, real);
		if (temp < 0) {
			temp += 2 * Math.PI;
		}
		angle = temp;
	}

	/**
	 * Gets module.
	 * 
	 * @return Module.
	 */

	public double module() {
		return module;
	}

	/**
	 * Multiplies this complex number with other.
	 * 
	 * @param c
	 *            Other complex number.
	 * @return New complex number that is result of multiplication between this
	 *         and other complex number.
	 */

	public Complex multiply(Complex c) {
		return new Complex(real * c.real - imaginary * c.imaginary, imaginary * c.real + real * c.imaginary);
	}

	/**
	 * Divides this complex number with other.
	 * 
	 * @param c
	 *            Other complex number.
	 * @return This complex number divided by other.
	 */

	public Complex divide(Complex c) {
		double newModule = module / c.module;
		double cosfi = Math.cos(angle - c.angle);
		double sinfi = Math.sin(angle - c.angle);

		return new Complex(newModule * cosfi, newModule * sinfi);
	}

	/**
	 * Adds this complex number with other.
	 * 
	 * @param c
	 *            Other complex number.
	 * @return Adds this complex number with other.
	 */

	public Complex add(Complex c) {
		return new Complex(real + c.real, imaginary + c.imaginary);
	}

	/**
	 * Subs this complex number with other.
	 * 
	 * @param c
	 *            Other complex number.
	 * @return New complex number that is equal to this complex number
	 *         subtracted by other.
	 */

	public Complex sub(Complex c) {
		return new Complex(real - c.real, imaginary - c.imaginary);
	}

	/**
	 * Negates this complex number.
	 * 
	 * @return New complex number that has opposite value of this number.
	 */

	public Complex negate() {
		return new Complex(-real, -imaginary);
	}

	/**
	 * Calculates complex number to the nth power.
	 * 
	 * @param n
	 *            Power.
	 * @return New complex number that is equal this complex number to the nth
	 *         power.
	 */

	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Number n can't be negative.");
		}

		double newModule = Math.pow(module, n);
		double cosfi = Math.cos(n * angle);
		double sinfi = Math.sin(n * angle);

		return new Complex(newModule * cosfi, newModule * sinfi);
	}

	/**
	 * Calulates nth root of complex number. Result is list of n complex numbers
	 * that are solution.
	 * 
	 * @param n
	 *            Root.
	 * @return Nth root of complex number.
	 */

	public List<Complex> root(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("Number n has to be positive.");
		}

		List<Complex> solutions = new ArrayList<>();
		double newModule = Math.pow(module, 1.0 / n);

		for (int i = 0; i < n; i++) {
			double cosfi = Math.cos((angle + 2 * i * Math.PI) / n);
			double sinfi = Math.sin((angle + 2 * i * Math.PI) / n);
			solutions.add(new Complex(newModule * cosfi, newModule * sinfi));
		}

		return solutions;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (Math.abs(real) > epsilon) {
			sb.append(String.format("%f ", real));
			if (Math.abs(imaginary) > epsilon) {
				sb.append(String.format("%s %fi", imaginary < 0 ? "-" : "+", Math.abs(imaginary)));
			}

		} else {
			sb.append(String.format("%fi", imaginary));
		}

		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Complex)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		
		Complex other = (Complex) obj;
		return Math.abs(real - other.real) < epsilon && Math.abs(imaginary - other.imaginary) < epsilon;
	}
}

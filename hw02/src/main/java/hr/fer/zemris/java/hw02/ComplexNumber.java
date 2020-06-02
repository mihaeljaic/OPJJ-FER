package hr.fer.zemris.java.hw02;

/**
 * Class represents an unmodifiable complex number. Complex number consists of
 * real and imaginary numbers.
 * 
 * Class offers operations of adding, subtracting, multiplying, calculating nth
 * power and calculating nth root of complex number. After operations new object
 * of complex number is created.
 *
 * @author Mihael Jaić
 *
 */

public class ComplexNumber {

	/**
	 * Constant used for comparing real numbers.
	 */
	private static final double EPSILON = 1e-6;

	/**
	 * Real part of complex number.
	 */
	private final double real;
	/**
	 * Imaginary part of complex number.
	 */
	private final double imaginary;
	/**
	 * Magnitude of complex number.
	 */
	private final double magnitude;
	/**
	 * Angle of complex number in radians from [0, 2PI>.
	 */
	private final double angle;

	/**
	 * Constructor that initializes all atributtes from given real and imaginary
	 * part. If both numbers are zero angle is set to zero aswell.
	 * 
	 * @param real
	 *            Real part of complex number.
	 * @param imaginary
	 *            Imaginary part of complex number.
	 */

	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
		magnitude = Math.sqrt(real * real + imaginary * imaginary);

		double temp = Math.atan2(imaginary, real);
		if (temp < 0) {
			temp += 2 * Math.PI;
		}
		angle = temp;
	}

	/**
	 * Factory method that creates new complex number by setting its real part
	 * to given value and imaginary part to zero.
	 * 
	 * @param real
	 *            Value of real part of complex number.
	 * @return Reference to new object.
	 */

	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0.0);
	}

	/**
	 * Factory method that creates new complex number by setting its imaginary
	 * part to given value and real part to zero.
	 * 
	 * @param imaginary
	 *            Value of imaginary part of complex number.
	 * @return Reference to new object.
	 */

	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0.0, imaginary);
	}

	/**
	 * Factory method that creates new complex number by given magnitude and
	 * angle values.
	 * 
	 * @param magnitude
	 *            Absolute value of complex number.
	 * @param angle
	 *            Angle of complex number in radians.
	 * @return Reference to newly created object.
	 * @throws IllegalArgumentException
	 *             If magnitude is less than 0.
	 */

	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) throws IllegalArgumentException {
		if (magnitude < 0) {
			throw new IllegalArgumentException("Magnitude can't be negative.");
		}

		return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}

	/**
	 * Factory method that creates new complex number by parsing the given
	 * string. String can be given in only 3 ways. First way is by giving only
	 * real part value. Second way is by giving only imaginary part value(it has
	 * to be terminated with letter "i"). Third way is by giving both real part
	 * and imaginary part values(order doesn't matter). There can't be any
	 * spaces between numbers. Example: "-2.71-3.15i"
	 * 
	 * @param s
	 *            Given string that represents complex number.
	 * @return Reference to new complex number object.
	 * @throws IllegalArgumentException
	 *             If string is null.
	 */

	public static ComplexNumber parse(String s) throws IllegalArgumentException {
		if (!(s instanceof String)) {
			throw new IllegalArgumentException("Argument s is not String.");
		}

		// Splits string for every appearance of "i" or "+" or "-".
		String[] expression = s.split("(?=i)|(?=\\-)|(?=\\+)");

		double real = 0.0;
		double imaginary = 0.0;

		if (expression.length > 3) {
			// Regular expression can have only 3 parts: real number, imaginary
			// number and imaginary unit.
			throw new IllegalArgumentException("Invalid expression.");

		} else if (expression.length == 1) {
			// This expression can contain only real number because imaginary
			// number
			// contains letter "i" along with number which would make length 2
			// of splitted string.
			try {
				real = Double.parseDouble(expression[0]);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Invalid expression");
			}

		} else if (expression.length == 2) {
			// Only imaginary number can be in this expression.
			try {
				int indexOfi = indexOfImaginaryUnit(expression);
				if (indexOfi == -1) {
					throw new IllegalArgumentException("Invalid expression.");
				}

				imaginary = Double.parseDouble(expression[0]);

			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Invalid expression.");
			}

		} else if (expression.length == 3) {
			// Expression potentially contains real and imaginary numbers.
			try {
				int indexOfi = indexOfImaginaryUnit(expression);
				if (indexOfi == -1) {
					throw new IllegalArgumentException("Invalid expression");
				}

				if (indexOfi == 1) {
					imaginary = Double.parseDouble(expression[0]);
					real = Double.parseDouble(expression[2]);

				} else {
					imaginary = Double.parseDouble(expression[1]);
					real = Double.parseDouble(expression[0]);

				}

			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Invalid expression.");
			}

		}

		return new ComplexNumber(real, imaginary);
	}

	/**
	 * Returns index of string that is equal to "i" in array of strings. If
	 * there is no occurence of "i" returns -1.
	 * 
	 * @param stringArray
	 *            Array of strings.
	 * @return Index of "i" or -1 if there is no "i" in any of the strings.
	 */

	private static int indexOfImaginaryUnit(String[] stringArray) {
		int index = 0;
		for (String s : stringArray) {
			if (s.equals("i")) {
				return index;
			}
			index++;
		}

		return -1;
	}

	/**
	 * Gets real value of complex number.
	 * 
	 * @return Real value of complex number.
	 */

	public double getReal() {
		return real;
	}

	/**
	 * Gets imaginary value of complex number.
	 * 
	 * @return Imaginary value of complex number.
	 */

	public double getImaginary() {
		return imaginary;
	}

	/**
	 * Gets magnitude of complex number.
	 * 
	 * @return Magnitude of complex number.
	 */

	public double getMagnitude() {
		return magnitude;
	}

	/**
	 * Gets angle of complex number. Number is in radians in interval [0, 2PI].
	 * 
	 * @return Angle of complex number.
	 */

	public double getAngle() {
		return angle;
	}

	/**
	 * Adds 2 complex numbers. Returns new object of complex number as result.
	 * 
	 * @param c
	 *            Second operand in addition.
	 * @return New complex number as result of operation.
	 * @throws IllegalArgumentException
	 *             If c is not instance of ComlexNumber class.
	 */

	public ComplexNumber add(ComplexNumber c) throws IllegalArgumentException {
		if (!(c instanceof ComplexNumber)) {
			throw new IllegalArgumentException("Argument c is not complex number.");
		}

		return new ComplexNumber(real + c.getReal(), imaginary + c.getImaginary());
	}

	/**
	 * Subtracts 2 complex numbers. Returns new complex number as result.
	 * 
	 * @param c
	 *            Second operand in subtraction.
	 * @return Complex number that is result of subtraction.
	 * @throws IllegalArgumentException
	 *             If c is not instance of ComplexNumber class.
	 */

	public ComplexNumber sub(ComplexNumber c) throws IllegalArgumentException {
		if (!(c instanceof ComplexNumber)) {
			throw new IllegalArgumentException("Argument c is not complex number.");
		}

		return new ComplexNumber(real - c.getReal(), imaginary - c.getImaginary());
	}

	/**
	 * Multiplies 2 complex numbers. Returns new complex number as result.
	 * 
	 * @param c
	 *            Second operand in multiplication.
	 * @return Complex number that is result of multiplication.
	 * @throws IllegalArgumentException
	 *             If c is not instance of ComplexNumber class.
	 */

	public ComplexNumber mul(ComplexNumber c) throws IllegalArgumentException {
		if (!(c instanceof ComplexNumber)) {
			throw new IllegalArgumentException("Argument c is not complex number.");
		}

		return new ComplexNumber(real * c.getReal() - imaginary * c.getImaginary(),
				imaginary * c.getReal() + real * c.getImaginary());
	}

	/**
	 * Divides 2 complex numbers. Returns new complex number as result.
	 * 
	 * @param c
	 *            Second operand in division.
	 * @return Complex number that is result of division.
	 * @throws IllegalArgumentException
	 *             If c is not instance of ComplexNumber class.
	 */

	public ComplexNumber div(ComplexNumber c) throws IllegalArgumentException {
		if (!(c instanceof ComplexNumber)) {
			throw new IllegalArgumentException("Argument c is not complex number.");
		}

		if (Math.abs(c.getMagnitude()) < EPSILON) {
			throw new IllegalArgumentException("Divisor's magnitude cannot be 0.");
		}

		double newMagnitude = magnitude / c.getMagnitude();
		double cosfi = Math.cos(angle - c.getAngle());
		double sinfi = Math.sin(angle - c.getAngle());

		return new ComplexNumber(newMagnitude * cosfi, newMagnitude * sinfi);
	}

	/**
	 * Calculates complex number to the nth power. Returns new complex number as
	 * result. n can't be negative.
	 * 
	 * @param n
	 *            Exponent.
	 * @return Complex number that is result of operation.
	 * @throws IllegalArgumentException
	 *             If n is negative.
	 */

	public ComplexNumber power(int n) throws IllegalArgumentException {
		if (n < 0) {
			throw new IllegalArgumentException("Number n can't be negative.");
		}

		double newMagnitude = Math.pow(magnitude, n);
		double cosfi = Math.cos(n * angle);
		double sinfi = Math.sin(n * angle);

		return new ComplexNumber(newMagnitude * cosfi, newMagnitude * sinfi);
	}

	/**
	 * Calculates nth root of the complex number using de Moivre's formula.
	 * Returns n new complex numbers which are roots of given complex number.
	 * Solutions are generated in order that first solution is closest to (angle
	 * = 0) and others go in counterclockwise direction. Number n has to be
	 * positive.
	 * 
	 * @param n
	 *            Root.
	 * @return Complex number Array of solutions of nth root of given complex
	 *         number.
	 * @throws IllegalArgumentException
	 *             If n isn't positive.
	 */

	public ComplexNumber[] root(int n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException("Number n has to be positive.");
		}

		ComplexNumber[] solutions = new ComplexNumber[n];
		double newMagnitude = Math.pow(magnitude, 1.0 / n);

		for (int i = 0; i < n; i++) {
			double cosfi = Math.cos((angle + 2 * i * Math.PI) / n);
			double sinfi = Math.sin((angle + 2 * i * Math.PI) / n);
			solutions[i] = new ComplexNumber(newMagnitude * cosfi, newMagnitude * sinfi);
		}

		return solutions;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ComplexNumber)) {
			return false;
		}

		ComplexNumber other = (ComplexNumber) obj;

		return Math.abs(real - other.getReal()) < EPSILON && Math.abs(imaginary - other.getImaginary()) < EPSILON;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (Math.abs(real) > EPSILON) {
			sb.append(String.format("%.3f ", real));
			if (Math.abs(imaginary) > EPSILON) {
				sb.append(String.format("%s %.3fi", imaginary < 0 ? "-" : "+", Math.abs(imaginary)));
			}

		} else {
			sb.append(String.format("%.3fi", imaginary));
		}

		return sb.toString();
	}

}

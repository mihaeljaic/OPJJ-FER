package hr.fer.zemris.math;

/**
 * Class models complex polynomial expression in form of function f(z)
 * zn*z^n+zn-1*z^(n-1)+...+z2*z^2+z1*z+z0, where z0 to zn are coefficients for
 * each power of z.
 * 
 * @author Mihael Jaić
 *
 */

public class ComplexPolynomial {
	/**
	 * Factors.
	 */
	private Complex[] factors;

	/**
	 * Constructor that gets factors of complex polynomial.
	 * 
	 * @param factors
	 */

	public ComplexPolynomial(Complex... factors) {
		if (factors.length == 0) {
			throw new IllegalArgumentException();
		}

		this.factors = factors;
	}

	/**
	 * Gets order of complex polynomial.
	 * 
	 * @return Order of complex polynomial.
	 */

	public short order() {
		return (short) (factors.length - 1);
	}

	/**
	 * Multiplies this complex polyomial with other polynomial p. Result is new
	 * complex polynomial, both multiplicants don't remain the same.
	 * 
	 * @param p
	 *            Other polynomial.
	 * @return Multiplication of this polynomial and other polynomial p.
	 */

	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] newFactors = new Complex[factors.length + p.factors.length - 1];

		for (int i = 0, length = newFactors.length; i < length; i++) {
			newFactors[i] = new Complex();
		}

		for (int i = 0, length1 = factors.length; i < length1; i++) {
			for (int j = 0, length2 = p.factors.length; j < length2; j++) {
				newFactors[i + j] = newFactors[i + j].add(factors[i].multiply(p.factors[j]));
			}
		}

		return new ComplexPolynomial(newFactors);
	}

	/**
	 * Derives current polynomial.
	 * 
	 * @return Derivation of current polynomial.
	 */

	public ComplexPolynomial derive() {
		if (factors.length == 1) {
			return new ComplexPolynomial(Complex.ZERO);
		}

		Complex[] newFactors = new Complex[factors.length - 1];
		for (int i = 1; i < factors.length; i++) {
			newFactors[i - 1] = factors[i].multiply(new Complex(i, 0));
		}

		return new ComplexPolynomial(newFactors);
	}

	/**
	 * Computes polynomial value at given point z.
	 * 
	 * @param z
	 *            Complex number.
	 * @return Value of polynomial in point z.
	 */

	public Complex apply(Complex z) {
		Complex result = factors[0];
		// Used for avoiding power operation.
		Complex temp = z;

		for (int i = 1; i < factors.length; i++) {
			result = result.add(factors[i].multiply(temp));
			temp = temp.multiply(z);
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = factors.length - 1; i >= 0; i--) {
			if (i == 0) {
				sb.append(String.format("%s", factors[i]));
				break;
			}

			sb.append(String.format("(%s)z%s+", factors[i], i > 1 ? "^" + i : ""));
		}

		return sb.toString();
	}
}

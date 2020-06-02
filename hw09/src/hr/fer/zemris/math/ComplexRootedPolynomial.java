package hr.fer.zemris.math;

/**
 * Class models roots of complex polynomial in form of (z-z1)*(z-z2)*...*(z-zn)
 * where z1 to zn are his roots.
 * 
 * @author Mihael Jaić
 *
 */

public class ComplexRootedPolynomial {
	/**
	 * Roots.
	 */
	private Complex[] roots;

	/**
	 * Constructor that accepts roots of complex polynomial.
	 * 
	 * @param roots
	 *            Roots.
	 * @throws IllegalArgumentException
	 *             If no roots were sent to method.
	 */

	public ComplexRootedPolynomial(Complex... roots) throws IllegalArgumentException {
		if (roots.length == 0) {
			throw new IllegalArgumentException();
		}

		this.roots = roots;
	}

	/**
	 * Calculates expression of rooted polynomial by substituting z with given
	 * complex number.
	 * 
	 * @param z
	 *            Complex number.
	 * @return Solution of equation with given complex number as substitute of z
	 *         in initial expression.
	 */

	public Complex apply(Complex z) {
		if (roots.length == 0) {
			return z;
		}

		Complex result = z.sub(roots[0]);
		for (int i = 1; i < roots.length; i++) {
			result = result.multiply(z.sub(roots[i]));
		}

		return result;
	}

	/**
	 * Transforms rooted polynomial expression into complex polynomial. See
	 * {@link ComplexPolynomial}.
	 * 
	 * @return This expression as complex polynomial.
	 */

	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(roots[0].negate(), Complex.ONE);
		for (int i = 1; i < roots.length; i++) {
			result = result.multiply(new ComplexPolynomial(roots[i].negate(), Complex.ONE));
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Complex c : roots) {
			sb.append(String.format("(z - (%s))", c));
		}

		return sb.toString();
	}

	/**
	 * Finds index of closest root to complex number z within given treshold. If
	 * there is no closest root that is within treshold -1 is returned instead.
	 * 
	 * @param z
	 *            Complex number.
	 * @param treshold
	 *            Treshold.
	 * @return Index of closest root to complex number z.
	 */

	public int indexOfClosestRootFor(Complex z, double treshold) {
		int index = -1;
		double lowestDistance = treshold;

		for (int i = 0; i < roots.length; i++) {
			double distance = roots[i].sub(z).module();
			if (distance < lowestDistance) {
				index = i;
				lowestDistance = distance;
			}
		}

		return index;
	}
}

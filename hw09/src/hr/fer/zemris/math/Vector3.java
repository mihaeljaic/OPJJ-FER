package hr.fer.zemris.math;

/**
 * Class is model for vector in 3 dimensions. Offers some standard operations on
 * vectors like dot product, vector product, etc. Vector is unmodifiable, when
 * some of operations are performed on vector new vector is created and old
 * remains the same.
 * 
 * @author Mihael Jaić
 *
 */

public class Vector3 {
	/**
	 * X coordinate.
	 */
	private final double x;
	/**
	 * Y coordinate.
	 */
	private final double y;
	/**
	 * Z coordinate.
	 */
	private final double z;
	/**
	 * Used for comparing real numbers.
	 */
	private static final double epsilon = 1e-6;

	/**
	 * Constructor that accepts coordinates.
	 * 
	 * @param x
	 *            X coordinate.
	 * @param y
	 *            Y coordinate.
	 * @param z
	 *            Z coordinate.
	 */

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Calculates norm of vector.
	 * 
	 * @return Norm.
	 */

	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Gets this vector in normalized form.
	 * 
	 * @return Normalized vector.
	 */

	public Vector3 normalized() {
		double norm = norm();
		if (norm < epsilon) {
			return new Vector3(0, 0, 0);
		}

		return new Vector3(x / norm, y / norm, z / norm);
	}

	/**
	 * Adds this vector with other vector.
	 * 
	 * @param other
	 *            Other vector.
	 * @return This vector added with other vector.
	 */

	public Vector3 add(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}

	/**
	 * Subs this vector with other vector.
	 * 
	 * @param other
	 *            Other vector.
	 * @return This vector subtracted by other vector.
	 */

	public Vector3 sub(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}

	/**
	 * Dot product of this and other vector.
	 * 
	 * @param other
	 *            Other vector.
	 * @return Dot product of this and other vector.
	 */

	public double dot(Vector3 other) {
		return x * other.x + y * other.y + z * other.z;
	}

	/**
	 * Cross product of this and other vector.
	 * 
	 * @param other
	 *            Other vector.
	 * @return New vector that is result of cross product of this vector with
	 *         other.
	 */

	public Vector3 cross(Vector3 other) {
		return new Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
	}

	/**
	 * Mulitplies this vector with scalar.
	 * 
	 * @param s
	 *            Scalar.
	 * @return New vector that is result of multiplication of this vector with
	 *         scalar.
	 */

	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}
	
	/**
	 * Calculates cosine of angle between this vector and other.
	 * 
	 * @param other Other vector.
	 * @return Cosine of angle between this vector and other.
	 */
	
	public double cosAngle(Vector3 other) {
		return this.dot(other) / (norm() * other.norm());
	}
	
	/**
	 * Gets x coordinate.
	 * 
	 * @return X coordinate.
	 */
	
	public double getX() {
		return x;
	}
	
	/**
	 * Gets y coordinate.
	 * 
	 * @return Y coordinate.
	 */
	
	public double getY() {
		return y;
	}
	
	/**
	 * Gets z coordinate.
	 * 
	 * @return Z coordinate.
	 */
	
	public double getZ() {
		return z;
	}
	
	/**
	 * Converts vector's coordinates into array in form of [x, y, z].
	 * 
	 * @return Array of coordinates.
	 */
	
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	public String toString() {
		return String.format("(%f, %f, %f)", x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vector3)) {
			return false;
		}
		
		if (this == obj) {
			return true;
		}
		
		Vector3 other = (Vector3) obj;
		return Math.abs(x - other.x) < epsilon && Math.abs(y - other.y) < epsilon && Math.abs(z - other.z) < epsilon;
	}
}

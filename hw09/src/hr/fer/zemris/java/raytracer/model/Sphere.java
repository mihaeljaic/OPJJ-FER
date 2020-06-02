package hr.fer.zemris.java.raytracer.model;

/**
 * Graphical object that models sphere in 3dimensional space.
 * 
 * @author Mihael Jaić
 *
 */

public class Sphere extends GraphicalObject {
	/**
	 * Center.
	 */
	private Point3D center;
	/**
	 * Radius.
	 */
	private double radius;
	/**
	 * Constant for difusion of red color.
	 */
	private double kdr;
	/**
	 * Constant for difusion of green color.
	 */
	private double kdg;
	/**
	 * Constant of difusion of blue color.
	 */
	private double kdb;
	/**
	 * Constant of reflection of red color.
	 */
	private double krr;
	/**
	 * Constant of reflection of green color.
	 */
	private double krg;
	/**
	 * Constant of reflection of blue color.
	 */
	private double krb;
	/**
	 * Index of refraction.
	 */
	private double krn;
	
	/**
	 * Constructor that sets all attributes.
	 * 
	 * @param center
	 * @param radius
	 * @param kdr
	 * @param kdg
	 * @param kdb
	 * @param krr
	 * @param krg
	 * @param krb
	 * @param krn
	 */
	
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D startCenter = ray.start.sub(center);
		double discriminant = Math.pow(ray.direction.scalarProduct(startCenter), 2) - Math.pow(startCenter.norm(), 2)
				+ radius * radius;
		if (discriminant < 0) {
			// No intersection.
			return null;
		}
		
		double shortestDistance = -startCenter.scalarProduct(ray.direction) - Math.sqrt(discriminant);
		
		Point3D intersection = ray.start.add(ray.direction.scalarMultiply(shortestDistance));		
		
		return new RayIntersection(intersection, shortestDistance, true) {

			@Override
			public Point3D getNormal() {
				return this.getPoint().sub(center).normalize();
			}
			
			@Override
			public double getKrr() {
				return krr;
			}
			
			@Override
			public double getKrn() {
				return krn;
			}
			
			@Override
			public double getKrg() {
				return krg;
			}
			
			@Override
			public double getKrb() {
				return krb;
			}
			
			@Override
			public double getKdr() {
				return kdr;
			}
			
			@Override
			public double getKdg() {
				return kdg;
			}
			
			@Override
			public double getKdb() {
				return kdb;
			}
		};
	}

}

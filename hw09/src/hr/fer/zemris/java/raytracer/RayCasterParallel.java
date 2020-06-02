package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Multithread program that draws spheres using Phong illumination model with 3
 * light sources.
 * 
 * @author Mihael Jaić
 *
 */

public class RayCasterParallel {

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		RayTracerViewer.show(new RayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Calculates color for each pixel on image. Uses Fork-Join framework and
	 * RecursiveAction for parallelization.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class RayTracerProducer implements IRayTracerProducer {

		@Override
		public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical, int width,
				int height, long requestNo, IRayTracerResultObserver observer) {
			short[] red = new short[width * height];
			short[] green = new short[width * height];
			short[] blue = new short[width * height];

			Point3D zAxis = view.sub(eye).normalize();
			Point3D yAxis = viewUp.sub(zAxis.scalarMultiply(zAxis.scalarProduct(viewUp))).normalize();
			Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();

			Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2.0))
					.add(yAxis.scalarMultiply(vertical / 2.0));

			Scene scene = RayTracerViewer.createPredefinedScene();

			ForkJoinPool pool = new ForkJoinPool();

			pool.invoke(new Tracer(width, height, 0, height - 1, horizontal, vertical, xAxis, yAxis, screenCorner, red,
					green, blue, scene, eye));

			pool.shutdown();

			observer.acceptResult(red, green, blue, requestNo);
		}

	}

	/**
	 * Calculates color for each pixel in image.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class Tracer extends RecursiveAction {
		/**
		 * Default serial version.
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * Treshold.
		 */
		private static final int treshold = 16;
		/**
		 * Ambient color intensity.
		 */
		private static final int ambientComponent = 15;
		/**
		 * Width.
		 */
		private int width;
		/**
		 * Height.
		 */
		private int height;
		/**
		 * Minimal height.
		 */
		private int yMin;
		/**
		 * Maximal height.
		 */
		private int yMax;
		/**
		 * Horizontal.
		 */
		private double horizontal;
		/**
		 * Vertical.
		 */
		private double vertical;
		/**
		 * Scene.
		 */
		private Scene scene;
		/**
		 * Eye position.
		 */
		private Point3D eye;
		/**
		 * X axis normalized vector.
		 */
		private Point3D xAxis;
		/**
		 * Y axis normalized vec
		 */
		private Point3D yAxis;
		/**
		 * Screen corner position.
		 */
		private Point3D screenCorner;
		/**
		 * Intensity of red color accross pixels.
		 */
		private short[] red;
		/**
		 * Intensity of green color accross pixels.
		 */
		private short[] green;
		/**
		 * Intensity of blue color accross pixels.
		 */
		private short[] blue;

		/**
		 * Constructor that sets all attributes needed for calculating color for
		 * each pixel.
		 * 
		 * @param width
		 * @param height
		 * @param yMin
		 * @param yMax
		 * @param horizontal
		 * @param vertical
		 * @param xAxis
		 * @param yAxis
		 * @param screenCorner
		 * @param red
		 * @param green
		 * @param blue
		 * @param scene
		 * @param eye
		 */

		public Tracer(int width, int height, int yMin, int yMax, double horizontal, double vertical, Point3D xAxis,
				Point3D yAxis, Point3D screenCorner, short[] red, short[] green, short[] blue, Scene scene,
				Point3D eye) {
			super();
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.screenCorner = screenCorner;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.eye = eye;
			this.scene = scene;
		}

		@Override
		protected void compute() {
			if (yMax - yMin + 1 <= treshold) {
				computeDirect();
				return;
			}

			invokeAll(
					new Tracer(width, height, yMin, yMin + (yMax - yMin) / 2, horizontal, vertical, xAxis, yAxis,
							screenCorner, red, green, blue, scene, eye),
					new Tracer(width, height, yMin + (yMax - yMin) / 2 + 1, yMax, horizontal, vertical, xAxis, yAxis,
							screenCorner, red, green, blue, scene, eye));
		}

		/**
		 * Computes color values of pixel for specific height interval.
		 */

		private void computeDirect() {
			short[] rgb = new short[3];
			int offset = yMin * width;
			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply((double) x * horizontal / (width - 1)))
							.sub(yAxis.scalarMultiply((double) y * vertical / (height - 1)));

					Ray ray = Ray.fromPoints(eye, screenPoint);
					trace(scene, ray, rgb);

					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					offset++;
				}
			}
		}

		/**
		 * Finds closest object that intersects with observer's view ray. Then
		 * calculates color intensity of intersection for each light source.
		 * 
		 * @param scene
		 *            Scene.
		 * @param ray
		 *            Eye.
		 * @param rgb
		 *            Colors.
		 */

		private void trace(Scene scene, Ray ray, short[] rgb) {
			RayIntersection closestIntersection = null;
			for (GraphicalObject object : scene.getObjects()) {
				RayIntersection intersection = object.findClosestRayIntersection(ray);

				if (intersection != null && closestIntersection == null) {
					closestIntersection = intersection;
				} else if (intersection != null && intersection.getDistance() < closestIntersection.getDistance()) {
					closestIntersection = intersection;
				}
			}

			if (closestIntersection == null) {
				for (int i = 0; i < rgb.length; i++) {
					rgb[i] = 0;
				}
				return;
			}

			determineColorFor(scene, rgb, closestIntersection, ray);
		}

		/**
		 * Adds color intensity to intersection point on object for each light
		 * source. If object is in shadow light source doesn't provide any light
		 * to it.
		 * 
		 * @param scene
		 *            Scene.
		 * @param rgb
		 *            Colors intensity.
		 * @param intersection
		 *            Intersection.
		 * @param eye
		 *            Eye.
		 */

		private void determineColorFor(Scene scene, short[] rgb, RayIntersection intersection, Ray eye) {
			for (int i = 0; i < rgb.length; i++) {
				rgb[i] = ambientComponent;
			}

			for (LightSource source : scene.getLights()) {
				Ray ray = Ray.fromPoints(source.getPoint(), intersection.getPoint());
				double distance = intersection.getPoint().sub(ray.start).norm();
				final double epsilon = 1e-8;
				
				boolean isShortest = true;
				for (GraphicalObject object : scene.getObjects()) {
					RayIntersection sourceIntersect = object.findClosestRayIntersection(ray);

					if (sourceIntersect != null && sourceIntersect.getDistance() + epsilon < distance) {
						isShortest = false;
						break;
					}
				}

				if (isShortest) {
					colorPhong(intersection, rgb, eye, source);
				}

			}

		}

		/**
		 * Adds diffuse and reflective component's colors intensity to given
		 * intersection.
		 * 
		 * @param intersection
		 *            Intersection.
		 * @param rgb
		 *            Colors intensity.
		 * @param eye
		 *            Eye.
		 * @param source
		 *            Light source.
		 */

		private void colorPhong(RayIntersection intersection, short[] rgb, Ray eye, LightSource source) {
			Point3D v = eye.start.sub(intersection.getPoint().normalize()).normalize();
			Point3D l = source.getPoint().sub(intersection.getPoint()).normalize();
			Point3D n = intersection.getNormal();

			double lScalarn = Math.max(0, l.scalarProduct(n));
			Point3D r = l.sub(n.scalarMultiply(2 * l.scalarProduct(n))).normalize();

			double reflectiveComp = Math.max(0, Math.pow(r.scalarProduct(v), intersection.getKrn()));

			rgb[0] += (short) (source.getR()
					* (intersection.getKdr() * lScalarn + intersection.getKrr() * reflectiveComp));
			rgb[1] += (short) (source.getG()
					* (intersection.getKdg() * lScalarn + intersection.getKrg() * reflectiveComp));
			rgb[2] += (short) (source.getB()
					* (intersection.getKdb() * lScalarn + intersection.getKrb() * reflectiveComp));
		}

	}

}

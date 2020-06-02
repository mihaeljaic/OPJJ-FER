package hr.fer.zemris.java.raytracer;

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
 * Program draws spheres using Phong illumination model with 3 light sources.
 * 
 * @author Mihael Jaić
 *
 */

public class RayCaster {

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Gets ray tracer producer that does the calculation of intensity of rgb
	 * colors for each pixel.
	 * 
	 * @return Ray tracer producer.
	 */

	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			private static final int ambientComponent = 15;

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D zAxis = view.sub(eye).normalize();
				Point3D yAxis = viewUp.sub(zAxis.scalarMultiply(zAxis.scalarProduct(viewUp))).normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();

				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2.0))
						.add(yAxis.scalarMultiply(vertical / 2.0));

				Scene scene = RayTracerViewer.createPredefinedScene();
				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner
								.add(xAxis.scalarMultiply((double) x * horizontal / (width - 1)))
								.sub(yAxis.scalarMultiply((double) y * vertical / (height - 1)));

						Ray ray = Ray.fromPoints(eye, screenPoint);
						tracer(scene, ray, rgb);

						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}
				
				observer.acceptResult(red, green, blue, requestNo);
			}

			private void tracer(Scene scene, Ray ray, short[] rgb) {
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

			private void determineColorFor(Scene scene, short[] rgb, RayIntersection intersection, Ray eye) {
				for (int i = 0; i < rgb.length; i++) {
					rgb[i] = ambientComponent;
				}

				for (LightSource source : scene.getLights()) {
					Ray ray = Ray.fromPoints(source.getPoint(), intersection.getPoint());
					double distance = intersection.getPoint().sub(ray.start).norm();

					boolean isShortest = true;
					for (GraphicalObject object : scene.getObjects()) {
						RayIntersection sourceIntersect = object.findClosestRayIntersection(ray);
						final double epsilon = 1e-8;

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

			private void colorPhong(RayIntersection intersection, short[] rgb, Ray eye, LightSource source) {
				Point3D v = eye.start.sub(intersection.getPoint().normalize()).normalize();
				Point3D l = source.getPoint().sub(intersection.getPoint()).normalize();
				Point3D n = intersection.getNormal();

				double lScalarn = Math.max(0, l.scalarProduct(n));
				Point3D r = l.sub(n.scalarMultiply(2 * l.scalarProduct(n))).normalize();
				
				double reflectiveComp = Math.pow(v.scalarProduct(r), intersection.getKrn());
				
				rgb[0] += (short) (source.getR() * (intersection.getKdr() * lScalarn + intersection.getKrr() * reflectiveComp));
				rgb[1] += (short) (source.getG() * (intersection.getKdg() * lScalarn + intersection.getKrg() * reflectiveComp));
				rgb[2] += (short) (source.getB() * (intersection.getKdb() * lScalarn + intersection.getKrb() * reflectiveComp));
			}
		};
	}
}

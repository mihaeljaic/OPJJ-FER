package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Program that draws fractals according to given input complex polynomial
 * roots. User must enter at least 2 complex roots. To terminate entering roots
 * and start drawing fractal type 'done'. Complex numbers have to be in format 
 * a + ib or a - ib. Where a and b are some real numbers. Both a or b can be
 * dropped in that case their value is set to 0. If both of them aren't present
 * there has to be imginary unit. For example input 'i' is legal and will mean
 * complex number with real part equal to zero and imaginary part equal to one.
 * 
 * @author Mihael Jaić
 *
 */

public class Newton {

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

		List<Complex> rootsList = new ArrayList<>();
		while (true) {
			System.out.printf("Root %d> ", rootsList.size() + 1);

			String line = sc.nextLine();
			if (line.equals("done") && rootsList.size() < 2) {
				System.out.println("At least 2 roots have to be entered.");
				System.exit(1);
			}

			if (line.equals("done")) {
				break;
			}

			try {
				rootsList.add(new Complex(line));
			} catch (IllegalArgumentException ex) {
				System.out.printf("Couldn't parse expression: %s%n", line);
				System.exit(1);
			}
		}
		sc.close();

		Complex[] roots = new Complex[rootsList.size()];
		for (int i = 0; i < roots.length; i++) {
			roots[i] = rootsList.get(i);
		}

		System.out.println("Image of fractal will appear shortly. Thank you.");
		FractalViewer.show(new MyProducer(new ComplexRootedPolynomial(roots)));
	}

	/**
	 * Class that does calculations of each segment of screen to display
	 * fractal.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class CalculationWork implements Callable<Void> {
		/**
		 * Minimimal real.
		 */
		private double reMin;
		/**
		 * Maximal real.
		 */
		private double reMax;
		/**
		 * Minimal imaginary.
		 */
		private double imMin;
		/**
		 * Maximal imaginary.
		 */
		private double imMax;
		/**
		 * Width.
		 */
		private int width;
		/**
		 * Height.
		 */
		private int height;
		/**
		 * Minimal y coordinate in height.
		 */
		private int yMin;
		/**
		 * Maximal y coordinate in height.
		 */
		private int yMax;
		/**
		 * Data.
		 */
		private short[] data;
		/**
		 * Offset.
		 */
		private int offset;
		/**
		 * Complex polynomial.
		 */
		private ComplexPolynomial polynom;
		/**
		 * Derivate of complex polynomial.
		 */
		private ComplexPolynomial derivate;
		/**
		 * Rooted complex polynomial.
		 */
		private ComplexRootedPolynomial root;

		/**
		 * Upper limit for number of iterations when calculating zn.
		 */
		private static final int maxIterations = 256;
		/**
		 * Convergence treshold.
		 */
		private static final double convergenceThreshold = 1e-3;
		/**
		 * Acceptable treshold.
		 */
		private static final double acceptableThreshold = 2e-3;

		/**
		 * Constructor that accepts all attributes.
		 * 
		 * @param reMin
		 * @param reMax
		 * @param imMin
		 * @param imMax
		 * @param width
		 * @param height
		 * @param yMin
		 * @param yMax
		 * @param data
		 * @param polynom
		 * @param derivate
		 * @param root
		 */

		private CalculationWork(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin,
				int yMax, short[] data, ComplexPolynomial polynom, ComplexPolynomial derivate,
				ComplexRootedPolynomial root) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.data = data;
			this.offset = yMin * width;

			this.polynom = polynom;
			this.derivate = derivate;
			this.root = root;
		}

		@Override
		public Void call() throws Exception {
			for (int y = yMin; y < yMax + 1; y++) {
				for (int x = 0; x < width; x++) {
					Complex zn = mapToComplexPlain(x, y, 0, width, yMin, yMax, reMin, reMax, imMin, imMax);

					int iter = 0;
					do {
						Complex numerator = polynom.apply(zn);
						Complex denominator = derivate.apply(zn);
						Complex fraction = numerator.divide(denominator);

						Complex zn1 = zn.sub(fraction);
						if (zn1.sub(zn).module() < convergenceThreshold) {
							zn = zn1;
							break;
						}

						zn = zn1;
						iter++;
					} while (iter < maxIterations);

					data[offset++] = (short) (root.indexOfClosestRootFor(zn, acceptableThreshold) + 1);
				}
			}

			return null;
		}

		/**
		 * Calculates zn point.
		 * 
		 * @param x
		 *            X.
		 * @param y
		 *            Y.
		 * @param xMin
		 *            Minimal x.
		 * @param xMax
		 *            Maximal x.
		 * @param yMin
		 *            Minimal y.
		 * @param yMax
		 *            Maximal y.
		 * @param reMin
		 *            Minimal real value.
		 * @param reMax
		 *            Maximal real value.
		 * @param imMin
		 *            Minimal imaginary value.
		 * @param imMax
		 *            Maximal imaginary value.
		 * @return zn
		 */

		private Complex mapToComplexPlain(int x, int y, int xMin, int xMax, int yMin, int yMax, double reMin,
				double reMax, double imMin, double imMax) {
			double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
			double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

			return new Complex(cre, cim);
		}

	}

	/**
	 * Producer that calculates every pixel for GUI to show fractals.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class MyProducer implements IFractalProducer {
		/**
		 * Polynomial.
		 */
		private ComplexPolynomial polynom;
		/**
		 * Derivate.
		 */
		private ComplexPolynomial derivate;
		/**
		 * Root.
		 */
		private ComplexRootedPolynomial root;
		/**
		 * Pool.
		 */
		private ExecutorService pool;

		/**
		 * Constructor that gets complex rooted polynomial to calculate fractal
		 * image.
		 * 
		 * @param root
		 *            Complex rooted polynomial.
		 */

		private MyProducer(ComplexRootedPolynomial root) {
			this.root = root;
			polynom = root.toComplexPolynom();
			derivate = polynom.derive();

			pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), runnable -> {
				Thread thread = new Thread(runnable);
				thread.setDaemon(true);
				return thread;
			});
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer) {

			short[] data = new short[width * height];
			final int numberOfTracks = 8 * Runtime.getRuntime().availableProcessors();
			int numberOfYPerTrack = height / numberOfTracks;

			List<Future<Void>> results = new ArrayList<>();

			for (int i = 0; i < numberOfTracks; i++) {
				int yMin = i * numberOfYPerTrack;
				int yMax = (i + 1) * numberOfYPerTrack - 1;
				if (i == numberOfTracks - 1) {
					yMax = height - 1;
				}

				CalculationWork work = new CalculationWork(reMin, reMax, imMin, imMax, width, height, yMin, yMax, data,
						polynom, derivate, root);
				results.add(pool.submit(work));
			}

			for (Future<Void> work : results) {
				try {
					work.get();
				} catch (InterruptedException | ExecutionException e) {
				}
			}

			observer.acceptResult(data, (short) (polynom.order() + 1), requestNo);
		}
	}

}

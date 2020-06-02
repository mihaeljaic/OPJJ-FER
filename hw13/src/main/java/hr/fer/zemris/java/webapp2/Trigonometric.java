package hr.fer.zemris.java.webapp2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that gets two parameters 'a' and 'b' and calculates sinus and cosinus
 * of all integers in interval [a, b]. If any of parameters wasn't given or if
 * it isn't integer it will be set to default value. If start value is greater
 * than end value they will be swapped.<br>
 * Servlet is available with url
 * <a href="http://localhost:8080/webapp2/trigonometric">Trigonometric</a>.
 * 
 * @author Mihael Jaić
 *
 */

public class Trigonometric extends HttpServlet {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Default start value.
	 */
	private static final int defaultStartValue = 0;
	/**
	 * Default end value.
	 */
	private static final int defaultEndValue = 360;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer startValue = Util.getIntegerParameter(req, "a");
		Integer endValue = Util.getIntegerParameter(req, "b");

		startValue = startValue == null ? defaultStartValue : startValue;
		endValue = endValue == null ? defaultEndValue : endValue;

		if (startValue > endValue) {
			int swap = startValue;
			startValue = endValue;
			endValue = swap;
		}

		if (endValue > startValue + 720) {
			endValue = startValue + 720;
		}

		List<Double> sinus = new ArrayList<>();
		List<Double> cosinus = new ArrayList<>();

		for (int i = startValue; i <= endValue; i++) {
			sinus.add(Math.sin(Math.toRadians(i)));
			cosinus.add(Math.cos(Math.toRadians(i)));
		}

		req.setAttribute("a", startValue);
		req.setAttribute("b", endValue);
		req.setAttribute("sinus", sinus);
		req.setAttribute("cosinus", cosinus);

		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}

}

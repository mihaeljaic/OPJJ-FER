package hr.fer.zemris.java.webapp2;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that accepts one parameter 'color' and sets it as background to all
 * pages. Supported colors are 'WHITE', 'RED', 'GREEN' and 'CYAN'. If no
 * parameter was provided or parameter is invalid background color remains the
 * same as before.<br>
 * Servlet is available with url
 * <a href="http://localhost:8080/webapp2/setcolor">Set color</a>.
 * 
 * @author Mihael Jaić
 *
 */

public class SetColor extends HttpServlet {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Background colors that can be put on page.
	 */
	private static final Set<String> colors;

	static {
		colors = new HashSet<>();
		colors.add("WHITE");
		colors.add("RED");
		colors.add("GREEN");
		colors.add("CYAN");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String color = req.getParameter("color");

		if (color != null && colors.contains(color.toUpperCase())) {
			req.getSession().setAttribute("pickedBgCol", color.toUpperCase());
		}

		req.getRequestDispatcher("index.jsp").forward(req, resp);
	}

}

package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * In case of login or registration error gets all info from sesssion and sends
 * it to main servlet. So session can be cleared of attributes. Also in case
 * valid username was provided and wrong password saves username so it can be
 * rewritten to user for next login attempt.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servleti/error")
public class Error extends HttpServlet {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String regError = (String) req.getSession().getAttribute("regError");
		if (regError != null) {
			req.getSession().removeAttribute("regError");
			req.setAttribute("message", regError);
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		String message = (String) req.getSession().getAttribute("error");
		String nick = (String) req.getSession().getAttribute("nick");
		req.getSession().removeAttribute("error");
		req.getSession().removeAttribute("nick");
		req.setAttribute("error", message);
		req.setAttribute("nick", nick);

		req.getRequestDispatcher("/servleti/main").forward(req, resp);
	}

}

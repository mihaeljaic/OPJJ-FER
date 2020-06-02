package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Main servlet that displays main page. Main page consists of login form if
 * user is not logged in. And list of already registered users.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servleti/main")
public class Main extends HttpServlet {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String status = (String) req.getAttribute("error");
		List<BlogUser> blogUsers = DAOProvider.getDAO().getAllUsers();

		req.setAttribute("isEmpty", blogUsers.isEmpty());
		req.setAttribute("users", blogUsers);
		req.setAttribute("status", status == null ? "not logged in" : status);
		req.setAttribute("nick", req.getAttribute("nick"));

		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}

}

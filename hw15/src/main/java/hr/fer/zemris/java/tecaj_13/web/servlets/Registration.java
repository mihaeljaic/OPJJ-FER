package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.util.Util;

/**
 * Servlet that registers new user to database. Checks if provided data is
 * valid. Null values are not allowed. Duplicate nicknames are not allowed.
 * Password has to be at least 6 characters long. Nick can't be empty string.
 * Attributes have to satisfy length restrictions.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servleti/register")
public class Registration extends HttpServlet {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String firstName = req.getParameter("fName");
		String lastName = req.getParameter("lName");
		String email = req.getParameter("email");
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");

		if (!checkRegistrationData(firstName, lastName, email, nick, password, req)) {
			System.out.println("Failed registration attempt.");
			resp.sendRedirect("/blog/servleti/error");
			return;
		}

		try {
			DAOProvider.getDAO().registerUser(firstName, lastName, nick, email, password);
			resp.sendRedirect("/blog/servleti/main");
		} catch (DAOException e) {
			System.out.printf("Error while adding new user: %s", e.getMessage());
			resp.sendRedirect("/blog/servleti/main");
		}
	}

	/**
	 * Checks if registration data is valid.
	 * 
	 * @param firstName
	 *            First name.
	 * @param lastName
	 *            Last name.
	 * @param email
	 *            Email.
	 * @param nick
	 *            Nickname.
	 * @param password
	 *            Password.
	 * @param req
	 *            Request.
	 * @return True if registration data is valid, false otherwise.
	 */

	private boolean checkRegistrationData(String firstName, String lastName, String email, String nick, String password,
			HttpServletRequest req) {
		if (firstName == null || lastName == null || email == null || nick == null || password == null) {
			req.getSession().setAttribute("regError", "Registration failed: some of data is null.");
			return false;
		}

		if (firstName.length() > Util.ATTRIBUTE_LENGTH_RESTRICTIONS
				|| lastName.length() > Util.ATTRIBUTE_LENGTH_RESTRICTIONS
				|| email.length() > Util.ATTRIBUTE_LENGTH_RESTRICTIONS
				|| nick.length() > Util.ATTRIBUTE_LENGTH_RESTRICTIONS) {
			req.getSession().setAttribute("regError",
					String.format("Registration failed: some of properties are too long. Max length allowed : %d.",
							Util.ATTRIBUTE_LENGTH_RESTRICTIONS));
			return false;
		}

		if (DAOProvider.getDAO().getUser(nick) != null) {
			req.getSession().setAttribute("regError",
					String.format("Registration failed: User with nickname: '%s' already exists.", nick));
			return false;
		}

		if (nick.trim().isEmpty()) {
			req.getSession().setAttribute("regError", "Registration failed: nickname can't be empty string.");
			return false;
		}

		if (password.length() < 6) {
			req.getSession().setAttribute("regError",
					"Registration failed: password has to be at least 6 characters long.");
			return false;
		}

		return true;
	}

}

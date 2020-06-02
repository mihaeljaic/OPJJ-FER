package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.util.Util;

/**
 * Login servlet that checks if user provided valid nickname and password
 * combination. If nickname and password combination is wrong user is informed
 * by error.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servleti/login")
public class Login extends HttpServlet {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String password = req.getParameter("password");
		String nick = req.getParameter("nick");

		System.out.println("Registered attempt to login");

		BlogUser blogUser = DAOProvider.getDAO().getUser(nick);
		if (blogUser == null) {
			System.out.printf("User %s doesn't exist.%n", nick);
			req.getSession().setAttribute("error", String.format("User with nick: '%s' doesn't exist!", nick));
			resp.sendRedirect("/blog/servleti/error");
			return;
		}

		String passwordHash = Util.getPasswordHash(password);
		if (blogUser.getPasswordHash().equals(passwordHash)) {
			System.out.printf("Password for user %s is correct. Logging in user...", nick);

			req.getSession().setAttribute("current.user.id", blogUser.getId());
			req.getSession().setAttribute("current.user.fn", blogUser.getFirstName());
			req.getSession().setAttribute("current.user.ln", blogUser.getLastName());
			req.getSession().setAttribute("current.user.nick", blogUser.getNick());

			resp.sendRedirect("/blog/servleti/main");
		} else {
			req.getSession().setAttribute("error", "Invalid password!");
			req.getSession().setAttribute("nick", nick);
			resp.sendRedirect("/blog/servleti/error");
			System.out.printf("Wrong password for user: %s.%n", nick);
		}
	}

}

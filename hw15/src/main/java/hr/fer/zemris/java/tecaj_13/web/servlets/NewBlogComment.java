package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Creates new blog comment in database.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servleti/newComment")
public class NewBlogComment extends HttpServlet {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String message = req.getParameter("message");
		String stringID = req.getParameter("id");

		Long id = null;
		try {
			id = Long.valueOf(stringID);
		} catch (NumberFormatException ex) {
			System.out.println("Invalid id.");
			return;
		}

		final int messageMaxLength = 4096;
		if (message.length() > messageMaxLength) {
			req.getSession().setAttribute("regError", String.format(
					"Error while creating new comment. Message length has to be shorter than %d", messageMaxLength));
			System.out.println("Failed to create new blog comment.");
			resp.sendRedirect("/blog/servleti/error");
			return;
		}

		DAO dao = DAOProvider.getDAO();
		String nick = (String) req.getSession().getAttribute("current.user.nick");
		BlogUser user = nick == null ? null : dao.getUser(nick);

		dao.createBlogComment(message, user, id);
		BlogEntry entry = dao.getBlogEntry(id);
		String authorNick = entry.getBlogUser().getNick();

		resp.sendRedirect(String.format("/blog/servleti/author/%s/%d", authorNick, entry.getId()));
	}

}

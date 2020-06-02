package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.util.Util;

/**
 * Creates new blog entry.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servleti/newBlog")
public class NewBlogEntry extends HttpServlet {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = (String) req.getSession().getAttribute("current.user.nick");

		String title = req.getParameter("title");
		String text = req.getParameter("text");

		if (!checkBlogData(title, text, req)) {
			System.out.println("Failed to create new blog entry.");
			resp.sendRedirect("/blog/servleti/error");
			return;
		}

		DAOProvider.getDAO().createBlogEntry(title, text, nick);

		resp.sendRedirect(String.format("/blog/servleti/author/%s", nick));
	}

	/**
	 * Checks if data for new blog entry is valid.
	 * 
	 * @param title
	 *            Title.
	 * @param text
	 *            Text.
	 * @param req
	 *            Request.
	 * @return True if data for new blog entry is valid, false otherwise.
	 */

	private boolean checkBlogData(String title, String text, HttpServletRequest req) {
		if (title == null || text == null) {
			req.getSession().setAttribute("regError", "Couldn't create new blog because title or text were null.");
			return false;
		}

		if (title.trim().isEmpty()) {
			req.getSession().setAttribute("regError", "Error while creating new blog. Title can't be empty string.");
			return false;
		}

		if (title.length() > Util.ATTRIBUTE_LENGTH_RESTRICTIONS) {
			req.getSession().setAttribute("regError",
					String.format("Error while creating new blog. Title is too long. Only %d characters allowed.",
							Util.ATTRIBUTE_LENGTH_RESTRICTIONS));
			return false;
		}

		final int textLengthRestriction = 4096;
		if (text.length() > textLengthRestriction) {
			req.getSession().setAttribute("regError",
					String.format("Error while creating new blog. Text is too long. Only %d characters allowed.",
							textLengthRestriction));
			return false;
		}

		return true;
	}

}

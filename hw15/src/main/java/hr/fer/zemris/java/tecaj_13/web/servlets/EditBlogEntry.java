package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;

/**
 * Enables editing of blog entry with given id.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servleti/edit")
public class EditBlogEntry extends HttpServlet {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String stringid = req.getParameter("id");
		String editedText = req.getParameter("edited");

		Long id = null;
		try {
			id = Long.valueOf(stringid);
		} catch (NumberFormatException ex) {
			return;
		}

		DAOProvider.getDAO().updateBlogEntry(id, editedText);
		String nick = DAOProvider.getDAO().getBlogEntry(id).getBlogUser().getNick();

		resp.sendRedirect(String.format("/blog/servleti/author/%s", nick));
	}

}

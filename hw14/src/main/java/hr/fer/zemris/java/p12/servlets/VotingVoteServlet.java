package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * Servlet that gets parameter 'voteID' and increments poll option's vote count
 * width given id.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servlets/voting-vote")
public class VotingVoteServlet extends HttpServlet {
	/** Default serial id. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("voteID");
		if (id == null) {
			throw new RuntimeException("Servlet needs parameter 'voteID'.");
		}
		DAOProvider.getDao().updateVoteCount(id);

		resp.sendRedirect("/voting-app/servlets/voting-results");
	}

}

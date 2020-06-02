package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.dao.model.PollOption;

/**
 * Servlet that gets poll results from database and displays them on
 * votingRes.jsp page.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servlets/voting-results")
public class VotingResultsServlet extends HttpServlet {
	/** Default serial id. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = (String) req.getSession().getAttribute("pollID");
		List<PollOption> results = DAOProvider.getDao().getPollOptions(id);

		Collections.sort(results, (a, b) -> b.getVotesCount() - a.getVotesCount());

		req.setAttribute("results", results);
		req.setAttribute("poll", DAOProvider.getDao().getPoll(id));

		req.getRequestDispatcher("/WEB-INF/pages/votingRes.jsp").forward(req, resp);
	}

}

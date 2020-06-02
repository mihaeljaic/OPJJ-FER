package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.dao.model.PollOption;

/**
 * Servlet that loads poll options from given poll id as parameter. Poll options
 * are then displayed on votingIndex.jsp page.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servlets/voting")
public class VotingServlet extends HttpServlet {
	/** Default serial id. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("pollID");
		if (id == null) {
			throw new RuntimeException("Parameter 'pollID' wasn't provided.");
		}
		List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(id);
		
		req.getSession().setAttribute("pollID", id);
		req.setAttribute("pollOptions", pollOptions);
		req.setAttribute("poll", DAOProvider.getDao().getPoll(id));

		req.getRequestDispatcher("/WEB-INF/pages/votingIndex.jsp").forward(req, resp);
	}

}

package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.dao.model.Poll;

/**
 * Servlet that gets available polls from database and displays them using
 * votingPolls.jsp page.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servlets/index.html")
public class PollServlet extends HttpServlet {
	/** Defaul serial id. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Poll> polls = DAOProvider.getDao().getPolls();
		req.setAttribute("polls", polls);

		req.getRequestDispatcher("/WEB-INF/pages/votingPolls.jsp").forward(req, resp);
	}

}

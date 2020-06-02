package hr.fer.zemris.java.webapp2;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that extracts results of voting from result file. Forwards request to
 * page that displays voting results.<br>
 * Servlet is available with url
 * <a href="http://localhost:8080/webapp2/voting-results">Voting results</a>.
 * 
 * @author Mihael Jaić
 *
 */

public class VotingResults extends HttpServlet {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Result> results = Util.getResults(req, resp);
		if (results == null) {
			return;
		}

		req.setAttribute("results", results);

		req.getRequestDispatcher("/WEB-INF/pages/votingRes.jsp").forward(req, resp);
	}

	/**
	 * Result of voting for each band.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	public static class Result {
		/**
		 * Band name.
		 */
		private String bandName;
		/**
		 * Vote count.
		 */
		private int voteCount;
		/**
		 * Song url.
		 */
		private String songurl;

		/**
		 * Constructor that sets attributes.
		 * 
		 * @param bandName
		 *            Band name.
		 * @param voteCount
		 *            Vote count.
		 * @param songurl
		 *            Song url.
		 */

		public Result(String bandName, int voteCount, String songurl) {
			this.bandName = bandName;
			this.voteCount = voteCount;
			this.songurl = songurl;
		}

		/**
		 * Gets band name.
		 * 
		 * @return Band name.
		 */

		public String getBandName() {
			return bandName;
		}

		/**
		 * Gets vote count.
		 * 
		 * @return Vote count.
		 */

		public int getVoteCount() {
			return voteCount;
		}

		/**
		 * Gets song url.
		 * 
		 * @return Song url.
		 */

		public String getSongurl() {
			return songurl;
		}
	}

}

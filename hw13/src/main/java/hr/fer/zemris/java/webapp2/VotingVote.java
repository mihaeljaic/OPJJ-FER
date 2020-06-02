package hr.fer.zemris.java.webapp2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.webapp2.Voting.BandInfo;

/**
 * Servlet that increments vote count for band with given id parameter. If band
 * with given id doesn't exist vote is ignored and user is redirected to vote
 * results page.<br>
 * Example url that votes for band with id 28.
 * <a href="http://localhost:8080/webapp2/voting-vote?id=28">Vote</a>.
 * 
 * @author Mihael Jaić
 *
 */

public class VotingVote extends HttpServlet {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/votingresults.txt");

		File file = Paths.get(fileName).toFile();

		if (!file.exists()) {
			synchronized (Util.getMutex()) {
				if (!Util.createFile(req, resp)) {
					Util.status(req, resp, String.format("%sCouldn't create result file.", Util.INTERNAL_SERVER_ERROR));
					return;
				}
			}
		}

		@SuppressWarnings("unchecked")
		Map<String, BandInfo> bands = (Map<String, BandInfo>) req.getSession().getAttribute("bands");
		if (bands == null) {
			bands = Util.getBands(req, resp);
			if (bands == null) {
				return;
			}
		}

		String voteID = req.getParameter("id");
		if (!bands.containsKey(voteID)) {
			resp.sendRedirect(req.getContextPath() + "/voting-results");
			return;
		}

		synchronized (Util.getMutex()) {
			if (!increment(fileName, voteID, resp, req)) {
				// Error occurred.
				return;
			}
		}

		resp.sendRedirect(req.getContextPath() + "/voting-results");
	}

	/**
	 * Increments vote count for band with given id.
	 * 
	 * @param path
	 *            Path to result file.
	 * @param voteID
	 *            Band id.
	 * @param resp
	 *            Response.
	 * @param req
	 *            Request.
	 * @return True if increment was successful, false otherwise.
	 * @throws IOException
	 *             I/O error.
	 * @throws ServletException
	 *             Servlet error.
	 */

	private boolean increment(String path, String voteID, HttpServletResponse resp, HttpServletRequest req)
			throws IOException, ServletException {
		List<String> newLines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);

		for (int i = 0, len = newLines.size(); i < len; i++) {
			String[] temp = newLines.get(i).split("\\t");
			if (temp.length != 2) {
				Util.status(req, resp, String.format("%sInvalid result file format.", Util.INTERNAL_SERVER_ERROR));
				return false;
			}

			if (temp[0].equals(voteID)) {
				int oldValue = Integer.valueOf(temp[1]);
				newLines.set(i, String.format("%s\t%d", voteID, (oldValue + 1)));
				break;
			}
		}

		Files.write(Paths.get(path), newLines, StandardCharsets.UTF_8);

		return true;
	}

}

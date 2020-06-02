package hr.fer.zemris.java.webapp2;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that extracts information about bands for voting and sends data to
 * voting index jsp page that offers user to vote for band.<br>
 * Servlet is available with url
 * <a href="http://localhost:8080/webapp2/voting">Vote</a>.
 * 
 * @author Mihael Jaić
 *
 */

public class Voting extends HttpServlet {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		Map<String, BandInfo> bands = (Map<String, BandInfo>) req.getSession().getAttribute("bands");
		if (bands == null) {
			bands = Util.getBands(req, resp);
			if (bands == null) {
				return;
			}
		}

		req.setAttribute("bands", bands);

		req.getRequestDispatcher("/WEB-INF/pages/votingIndex.jsp").forward(req, resp);
	}

	/**
	 * Information about band.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	public static class BandInfo {
		/**
		 * Band id.
		 */
		private String id;
		/**
		 * Band name.
		 */
		private String bandName;
		/**
		 * Band song url.
		 */
		private String songurl;

		/**
		 * Constructor that sets all attributes.
		 * 
		 * @param id
		 *            Band id.
		 * @param bandName
		 *            Band name.
		 * @param songurl
		 *            Song url.
		 */

		public BandInfo(String id, String bandName, String songurl) {
			super();
			this.id = id;
			this.bandName = bandName;
			this.songurl = songurl;
		}

		/**
		 * Gets band id.
		 * 
		 * @return Band id.
		 */

		public String getId() {
			return id;
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
		 * Gets song url.
		 * 
		 * @return Song url.
		 */

		public String getSongurl() {
			return songurl;
		}
	}

}

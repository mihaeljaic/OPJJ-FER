package hr.fer.zemris.java.webapp2;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import hr.fer.zemris.java.webapp2.Voting.BandInfo;
import hr.fer.zemris.java.webapp2.VotingResults.Result;

/**
 * Util class that offers methods for servlets and jsp pages.
 * 
 * @author Mihael Jaić
 *
 */

public class Util {
	/**
	 * Prevents multiple threads to write/read from result text file at the same
	 * time.
	 */
	private static final Object mutex = new Object();
	/**
	 * True story.
	 */
	public static final String STORY = "Did you ever hear the tragedy of Darth Plagueis The Lit? I thought not."
			+ "It’s not a story the Police would tell you.  It’s a Stoner legend. Darth Plagueis was a Dark Lord of the Stoners, "
			+ "so high and so lit he could use the Force to influence the marijuana to create... blunts. He had such"
			+ " a knowledge of the Lit Side that he could even keep the ones he cared about... from being caught by the police. "
			+ " He became so high... the only thing he was afraid of was losing his blunt, which eventually, of course, he did. Unfortunately, "
			+ "he taught his friend everything he knew, then his friend called the police while he was sleeping. Ironic."
			+ "He could save others from the police... but not himself.";
	/**
	 * Server error message.
	 */
	public static final String INTERNAL_SERVER_ERROR = "<h2>500 Internal server error</h2>";
	/**
	 * Bad request error.
	 */
	public static final String BAD_REQUEST_ERROR = "<h2>400 Bad Request</h2>";

	/**
	 * Gets mutex.
	 * 
	 * @return Mutex.
	 */

	public static Object getMutex() {
		return mutex;
	}

	/**
	 * Creates pie chart from given dataset.
	 * 
	 * @param dataset
	 *            Pie chart dataset.
	 * @param title
	 *            Title of chart.
	 * @return Pie chart.
	 */

	public static JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		final int startAngle = 290;
		plot.setStartAngle(startAngle);
		final float foregroundAlpha = 0.5f;
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(foregroundAlpha);

		return chart;
	}

	/**
	 * Sends image as servlet response.
	 * 
	 * @param response
	 *            Response.
	 * @param image
	 *            Image that will be sent.
	 * @throws IOException
	 *             In case of I/O error.
	 */

	public static void sendImage(HttpServletResponse response, BufferedImage image) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", bos);

		bos.flush();
		byte[] imageBytes = bos.toByteArray();
		bos.close();

		response.setContentType("image/png");
		response.setContentLengthLong(imageBytes.length);
		response.getOutputStream().write(imageBytes);

		response.getOutputStream().flush();
	}

	/**
	 * Creates new result file for given bands with vote count set to zero to
	 * all bands.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @return True if file was successfully created, false otherwise.
	 * @throws IOException
	 *             I/O error.
	 * @throws ServletException
	 *             Servlet error.
	 */

	public static boolean createFile(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String path = req.getServletContext().getRealPath("/WEB-INF/votingresults.txt");
		File file = Paths.get(path).toFile();

		if (!file.createNewFile()) {
			return false;
		}

		@SuppressWarnings("unchecked")
		Map<String, BandInfo> bands = (Map<String, BandInfo>) req.getSession().getAttribute("bands");
		if (bands == null) {
			bands = getBands(req, resp);
			if (bands == null) {
				return false;
			}
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
			for (String bandId : bands.keySet()) {
				bw.write(String.format("%s\t0", bandId));
				bw.newLine();
			}

			bw.flush();
		}

		return true;
	}

	/**
	 * Returns integer from request parameter with provided name. If there is no
	 * such parameter or parameter is not integer null is returned.
	 * 
	 * @param request
	 *            Request.
	 * @param name
	 *            Parameter name.
	 * @return Integer value of parameter. Or null if parameter doesn't exist.
	 * @throws IllegalArgumentException
	 *             If request or name are null.
	 */

	public static Integer getIntegerParameter(HttpServletRequest request, String name) throws IllegalArgumentException {
		if (request == null || name == null) {
			throw new IllegalArgumentException();
		}

		String parameter = request.getParameter(name);
		if (parameter == null) {
			return null;
		}

		try {
			return Integer.valueOf(parameter);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	/**
	 * Returns background color from session. If there is no color in session
	 * retunrs default color 'WHITE'.
	 * 
	 * @param request
	 *            Request.
	 * @return Background color.
	 */

	public static String getBgColor(HttpServletRequest request) {
		String color = (String) request.getSession().getAttribute("pickedBgCol");
		return color == null ? "WHITE" : color;
	}

	/**
	 * Generates random color.
	 * 
	 * @return Random color.
	 */

	public static String getRandomColor() {
		String randColor = null;
		double random = Math.random();
		if (random < 0.2) {
			randColor = "RED";
		} else if (random < 0.4) {
			randColor = "BLUE";
		} else if (random < 0.6) {
			randColor = "GREEN";
		} else if (random < 0.8) {
			randColor = "YELLOW";
		} else {
			randColor = "BLACK";
		}

		return randColor;
	}

	/**
	 * If error occurred calls jsp page that writes message to user.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response
	 * @param message
	 *            Error message.
	 * @throws ServletException
	 *             Servlet error.
	 * @throws IOException
	 *             I/O error.
	 */

	public static void status(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {
		req.setAttribute("status", message);
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}

	/**
	 * Gets results from result file. Returns null in case of error. If result
	 * file doesn't exist creates new result file.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @return Results of voting.
	 * @throws IOException
	 *             I/O error.
	 * @throws ServletException
	 */

	public static List<Result> getResults(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/votingresults.txt");

		File file = Paths.get(fileName).toFile();

		if (!file.exists()) {
			synchronized (Util.getMutex()) {
				if (!Util.createFile(req, resp)) {
					Util.status(req, resp, String.format("%sCouldn't create result file.", Util.INTERNAL_SERVER_ERROR));
					return null;
				}
			}
		}

		List<String> lines = null;
		synchronized (Util.getMutex()) {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		}

		@SuppressWarnings("unchecked")
		Map<String, BandInfo> bands = (Map<String, BandInfo>) req.getSession().getAttribute("bands");
		if (bands == null) {
			bands = getBands(req, resp);
			if (bands == null) {
				return null;
			}
		}

		List<Result> results = new ArrayList<>();
		for (String line : lines) {
			String[] temp = line.split("\\t");
			if (temp.length != 2) {
				Util.status(req, resp, String.format("%sInvalid result file format.", Util.INTERNAL_SERVER_ERROR));
				return null;
			}

			BandInfo band = bands.get(temp[0]);
			if (band == null) {
				Util.status(req, resp, String.format("%sCouldn't find band.", Util.INTERNAL_SERVER_ERROR));
				return null;
			}

			results.add(new Result(band.getBandName(), Integer.valueOf(temp[1]), band.getSongurl()));
		}

		Collections.sort(results, (a, b) -> b.getVoteCount() - a.getVoteCount());
		return results;
	}

	/**
	 * Gets informations about bands. Returns null in case of error.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @return Information about bands.
	 * @throws ServletException
	 *             Servlet error.
	 * @throws IOException
	 *             I/O error.
	 */

	public static Map<String, BandInfo> getBands(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/voting-definition.txt");

		List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

		// Linked hash map is used to preserve band order.
		Map<String, BandInfo> bands = new LinkedHashMap<>();
		for (String line : lines) {
			String[] temp = line.split("\\t");
			if (temp.length != 3) {
				Util.status(req, resp,
						String.format("%sInvalid voting definition file format.", Util.INTERNAL_SERVER_ERROR));
				return null;
			}

			String id = temp[0];
			if (bands.containsKey(id)) {
				Util.status(req, resp, String.format("%sDuplicate band id.", Util.INTERNAL_SERVER_ERROR));
				return null;
			}

			bands.put(id, new BandInfo(id, temp[1], temp[2]));
		}

		req.getSession().setAttribute("bands", bands);
		req.getSession().setAttribute("bandsNumber", bands.size());

		return bands;
	}

}

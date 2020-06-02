package hr.fer.zemris.java.webapp2;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import hr.fer.zemris.java.webapp2.VotingResults.Result;

/**
 * Servlet that creates pie chart from voting results and draws image from
 * it.<br>
 * Servlet is available with url
 * <a href="http://localhost:8080/webapp2/voting-graphics">Vote result graphics</a>.
 * 
 * @author Mihael Jaić
 *
 */

public class VotingGraphics extends HttpServlet {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Default image height.
	 */
	private static final int defaultHeight = 400;
	/**
	 * Default image width.
	 */
	private static final int defaultWidth = 400;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Result> results = Util.getResults(req, resp);
		if (results == null) {
			return;
		}

		PieDataset dataset = createDataset(results);
		JFreeChart chart = Util.createChart(dataset, "Band voting");

		Integer width = Util.getIntegerParameter(req, "width");
		Integer height = Util.getIntegerParameter(req, "height");

		width = width == null ? defaultWidth : width;
		height = height == null ? defaultHeight : height;

		if (width < defaultWidth || height < defaultHeight) {
			Util.status(req, resp, "Width and height need to be at least 400 each.");
			return;
		}

		BufferedImage image = chart.createBufferedImage(width, height);

		Util.sendImage(resp, image);
	}

	/**
	 * Creates pie chart dataset from given voting results.
	 * 
	 * @param results
	 *            Voting results.
	 * @return Pie chart dataset.
	 */

	private PieDataset createDataset(List<Result> results) {
		DefaultPieDataset pie = new DefaultPieDataset();

		for (Result result : results) {
			pie.setValue(result.getBandName(), result.getVoteCount());
		}

		return pie;
	}

}

package hr.fer.zemris.java.p12.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.dao.model.Poll;
import hr.fer.zemris.java.p12.dao.model.PollOption;

/**
 * Servlet that gets poll results and draws pie chart from results.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servlets/voting-graphics")
public class VotingGraphicsServlet extends HttpServlet {
	/** Default serial id. */
	private static final long serialVersionUID = 1L;
	/** Default image width and length. */
	private static final int defaultDimension = 400;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = (String) req.getSession().getAttribute("pollID");
		List<PollOption> results = DAOProvider.getDao().getPollOptions(id);
		Poll poll = DAOProvider.getDao().getPoll(id);

		PieDataset dataset = createDataset(results);
		JFreeChart chart = createChart(dataset, poll.getTitle());

		BufferedImage image = chart.createBufferedImage(defaultDimension, defaultDimension);

		resp.setContentType("image/png");
		ImageIO.write(image, "png", resp.getOutputStream());

		resp.getOutputStream().flush();
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

	private JFreeChart createChart(PieDataset dataset, String title) {
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
	 * Creates pie chart dataset from given voting results.
	 * 
	 * @param results
	 *            Voting results.
	 * @return Pie chart dataset.
	 */

	private PieDataset createDataset(List<PollOption> results) {
		DefaultPieDataset pie = new DefaultPieDataset();

		for (PollOption result : results) {
			pie.setValue(result.getTitle(), result.getVotesCount());
		}

		return pie;
	}

}

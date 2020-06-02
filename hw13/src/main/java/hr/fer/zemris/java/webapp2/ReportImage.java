package hr.fer.zemris.java.webapp2;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * Servlet that creates pie chart image that is sent as response to client.<br>
 * Servlet is available on following url
 * <a href="http://localhost:8080/webapp2/reportImage">Report image</a>.
 * 
 * @author Mihael Jaić
 *
 */

public class ReportImage extends HttpServlet {
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
		PieDataset dataset = createDataset();
		JFreeChart chart = Util.createChart(dataset, "OS usage");

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
	 * Creates dataset for pie chart.
	 * 
	 * @return Dataset for pie chart.
	 */

	private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Linux", 29);
		result.setValue("Mac", 20);
		result.setValue("Windows", 51);

		return result;
	}

}

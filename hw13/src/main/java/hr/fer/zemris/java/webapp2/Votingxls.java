package hr.fer.zemris.java.webapp2;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.webapp2.VotingResults.Result;

/**
 * Servlet that creates and sends excel document. Document contains information
 * about voting for favorite band. It has two columns. First column are band
 * names and second column are vote counts for each band.<br>
 * Servlet is available with url
 * <a href="http://localhost:8080/webapp2/voting-xls">Voting results xls</a>.
 * 
 * @author Mihael Jaić
 *
 */

public class Votingxls extends HttpServlet {
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

		sendExcelDocument(resp, results);
	}

	/**
	 * Creates and sends excel document. Document contains information about
	 * voting for favorite band. It has two columns. First column are band names
	 * and second column are vote counts for each band.
	 * 
	 * @param resp
	 *            Response.
	 * @param results
	 *            Results.
	 * @throws IOException
	 *             I/O error.
	 */

	private void sendExcelDocument(HttpServletResponse resp, List<Result> results) throws IOException {
		HSSFWorkbook hwb = new HSSFWorkbook();

		HSSFSheet sheet = hwb.createSheet("sheet");

		HSSFRow title = sheet.createRow((short) 0);
		title.createCell(0).setCellValue("Band name");
		title.createCell(1).setCellValue("Vote count");

		for (int i = 1, size = results.size(); i <= size; i++) {
			HSSFRow rowhead = sheet.createRow((short) i);
			rowhead.createCell((short) 0).setCellValue(results.get(i - 1).getBandName());
			rowhead.createCell((short) 1).setCellValue(results.get(i - 1).getVoteCount());
		}

		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; " + "filename=\"voteResults.xls\"");
		hwb.write(resp.getOutputStream());

		resp.getOutputStream().flush();
		hwb.close();
	}

}

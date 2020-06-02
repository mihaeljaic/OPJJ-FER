package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.dao.model.Poll;
import hr.fer.zemris.java.p12.dao.model.PollOption;

/**
 * Servlet that creates and sends excel document. Document contains information
 * about voting poll. It has two columns. First column contains voting poll
 * titles and second column are vote counts for each poll option.
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servlets/voting-xls")
public class VotingxlsServlet extends HttpServlet {
	/** Default serial version id. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = (String) req.getSession().getAttribute("pollID");
		List<PollOption> results = DAOProvider.getDao().getPollOptions(id);
		Poll poll = DAOProvider.getDao().getPoll(id);

		Collections.sort(results, (a, b) -> b.getVotesCount() - a.getVotesCount());

		sendExcelDocument(resp, results, poll.getTitle());
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
	 * @param pollTitle
	 *            Poll title.
	 * @throws IOException
	 *             I/O error.
	 */

	private void sendExcelDocument(HttpServletResponse resp, List<PollOption> results, String pollTitle)
			throws IOException {
		HSSFWorkbook hwb = new HSSFWorkbook();

		HSSFSheet sheet = hwb.createSheet("sheet");

		HSSFRow title = sheet.createRow((short) 0);
		title.createCell(0).setCellValue(pollTitle);
		title.createCell(1).setCellValue("Vote count");

		for (int i = 1, size = results.size(); i <= size; i++) {
			HSSFRow rowhead = sheet.createRow((short) i);
			rowhead.createCell((short) 0).setCellValue(results.get(i - 1).getTitle());
			rowhead.createCell((short) 1).setCellValue(results.get(i - 1).getVotesCount());
		}

		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; " + "filename=\"voteResults.xls\"");
		hwb.write(resp.getOutputStream());

		resp.getOutputStream().flush();
		hwb.close();
	}
}

package hr.fer.zemris.java.webapp2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servlet that creates excel document from given parameters 'a', 'b' and 'n'
 * and sends it to user. Document contains n sheets. Each sheet has two columns.
 * First column contains all integers in interval [a, b] and in second column
 * are those numbers raised to the i-th power where i is number of sheet.<br>
 * Parameter a has to be in interval [-100, 100].<br>
 * Parameter b has to be in interval [-100, 100].<br>
 * Parameter n has to be in interval [1, 5].<br>
 * Servlet is available with url
 * <a href="http://localhost:8080/webapp2/powers?a=0&b=100&n=3">Powers</a>.
 * 
 * @author Mihael Jaić
 *
 */

public class Powers extends HttpServlet {
	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String aString = req.getParameter("a");
		String bString = req.getParameter("b");
		String nString = req.getParameter("n");

		if (aString == null || bString == null || nString == null) {
			Util.status(req, resp,
					String.format("%sParameters 'a', 'b' and 'n' need to be provided.", Util.BAD_REQUEST_ERROR));
			return;
		}

		int a = 0;
		int b = 0;
		int n = 0;
		try {
			a = Integer.valueOf(aString);
			b = Integer.valueOf(bString);
			n = Integer.valueOf(nString);

		} catch (NumberFormatException ex) {
			Util.status(req, resp, String.format("%sParameters need to be integers.", Util.BAD_REQUEST_ERROR));
			return;
		}

		if (!checkParameters(a, b, n, req, resp)) {
			return;
		}

		if (a > b) {
			int temp = a;
			a = b;
			b = temp;
		}

		try {
			createExcelDocument(a, b, n, req, resp);
		} catch (RuntimeException ex) {
			Util.status(req, resp, String.format("%sCouldn't create excel document.", Util.INTERNAL_SERVER_ERROR));
		}
	}

	/**
	 * Creates excel document with n sheets. In each sheet there are two
	 * columns. First column contains all integers in interval [a, b] and in
	 * second column are those numbers raised to the i-th power where i is
	 * number of sheet.
	 * 
	 * @param a
	 *            Start number.
	 * @param b
	 *            End number.
	 * @param n
	 *            Number of sheets.
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @throws IOException
	 *             I/O error.
	 * @throws ServletException
	 *             Serlvet error.
	 */

	private void createExcelDocument(int a, int b, int n, HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		HSSFWorkbook hwb = new HSSFWorkbook();

		for (int i = 0; i < n; i++) {
			HSSFSheet sheet = hwb.createSheet(String.format("sheet %d", i + 1));

			HSSFRow title = sheet.createRow((short) 0);
			title.createCell(0).setCellValue("x");
			String exponent = i == 0 ? "" : String.format("^%d", i + 1);
			title.createCell(1).setCellValue(String.format("x%s", exponent));

			for (int j = a, row = 1; j <= b; j++, row++) {
				HSSFRow rowhead = sheet.createRow((short) row);
				rowhead.createCell((short) 0).setCellValue(j);
				rowhead.createCell((short) 1).setCellValue(Math.round(Math.pow(j, i + 1)));
			}
		}

		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; " + "filename=\"powersss.xls\"");
		hwb.write(resp.getOutputStream());

		resp.getOutputStream().flush();
		hwb.close();
	}

	/**
	 * Checks if given parameters are in correct interval. If they aren't
	 * apropiate message is sent to user.
	 * 
	 * @param a
	 *            Start number.
	 * @param b
	 *            End number.
	 * @param n
	 *            Amoung of document pages and power.
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @return True if parameters are valid, false otherwise.
	 * @throws ServletException
	 *             Servlet error.
	 * @throws IOException
	 *             I/O error.
	 */

	private boolean checkParameters(int a, int b, int n, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (a < -100 || a > 100) {
			Util.status(req, resp,
					String.format("%sParameter 'a' has to be in interval [-100, 100].", Util.BAD_REQUEST_ERROR));
			return false;
		}

		if (b < -100 || b > 100) {
			Util.status(req, resp,
					String.format("%sParameter 'b' has to be in interval [-100, 100].", Util.BAD_REQUEST_ERROR));
			return false;
		}

		if (n < 1 || n > 5) {
			Util.status(req, resp,
					String.format("%sParameter 'n' has to be in interval [1, 5].", Util.BAD_REQUEST_ERROR));
			return false;
		}

		return true;
	}

}

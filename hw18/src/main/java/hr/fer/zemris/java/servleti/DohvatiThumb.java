package hr.fer.zemris.java.servleti;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servleti/dohvatiThumb")
public class DohvatiThumb extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String staza = req.getParameter("staza");
		
		resp.setContentType("image/jpg");
		resp.getOutputStream().write(Files.readAllBytes(Paths.get(req.getServletContext().getRealPath("/WEB-INF/thumbnails/" + staza))));
		resp.getOutputStream().flush();
	}
	
}

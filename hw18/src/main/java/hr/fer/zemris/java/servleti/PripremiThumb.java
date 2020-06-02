package hr.fer.zemris.java.servleti;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.util.Util;

@WebServlet("/servleti/pripremiThumb")
public class PripremiThumb extends HttpServlet {
	private List<String> thumbs = new ArrayList<>();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Pozvano");
		String tag = req.getParameter("tagslike");
		
		System.out.println(tag);
		List<String> slike = Util.tagoviSlike.get(tag);

		List<String> temp = ucitajThumbs(slike, req);

		String[] polje = new String[thumbs.size()];
		temp.toArray(polje);

		Gson gson = new Gson();
		resp.getWriter().write(gson.toJson(polje));
		resp.getWriter().flush();
	}

	private List<String> ucitajThumbs(List<String> slike, HttpServletRequest req) throws IOException {
		String dir = req.getServletContext().getRealPath("/WEB-INF/thumbnails").toString();
		String src = req.getServletContext().getRealPath("/WEB-INF/slike").toString();
		new File(dir).mkdir();
		
		List<String> temp = new ArrayList<>();
		for (String slika : slike) {
			if (!thumbs.contains(slika)) {
				new File(dir + "/" + slika).createNewFile();
				BufferedImage resizedImage = new BufferedImage(150, 150, BufferedImage.TYPE_3BYTE_BGR);
				Graphics2D g = resizedImage.createGraphics();
				g.drawImage(ImageIO.read(new File(src + "/" + slika)), 0, 0, 150, 150, null);
				g.dispose();
				g.setComposite(AlphaComposite.Src);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				
				thumbs.add(slika);
			}
			
			temp.add(slika);
		}
		
		return temp;
	}

}

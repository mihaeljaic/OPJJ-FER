package hr.fer.zemris.java.servleti;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.model.Slika;
import hr.fer.zemris.java.util.Util;

@WebServlet("/servleti/tagovi")
public class DohvatiTagoveServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<String> tagovi = dohvatiTagove(req);

		String[] polje = new String[tagovi.size()];
		tagovi.toArray(polje);
		
		for (String tag : polje) {
			System.out.println(tag);
		}
		
		Gson gson = new Gson();
		resp.getWriter().write(gson.toJson(polje));
		resp.getWriter().flush();
	}

	private List<String> dohvatiTagove(HttpServletRequest req) throws IOException {
		Map<String, List<String>> tagoviSlike = new HashMap<>();
		List<String> lines = Files.readAllLines(Paths.get(req.getServletContext().getRealPath("/WEB-INF/opisnik.txt")));
		List<String> tagovi = new ArrayList<>();
		List<Slika> popisSlika = new ArrayList<>();
		
		String zadnjaSlika = null;
		String zadnjiOpis = null;
		for (int i = 1, len = lines.size(); i - 1 < len; i++) {
			if (i % 3 == 1) {
				zadnjaSlika = lines.get(i - 1);
			} else if (i % 3 == 0) {
				List<String> temp = new ArrayList<>();
				for (String tag : lines.get(i - 1).split(",")) {
					tag = tag.trim();
					if (tagoviSlike.containsKey(tag)) {
						List<String> slike = tagoviSlike.get(tag);
						slike.add(zadnjaSlika);
						tagoviSlike.put(tag, slike);
					} else {
						List<String> slike = new ArrayList<>();
						slike.add(zadnjaSlika);
						tagoviSlike.put(tag, slike);
						tagovi.add(tag);
					}
					
					temp.add(tag);
				}
				
				popisSlika.add(new Slika(zadnjaSlika, zadnjiOpis, temp));
				
			} else {
				zadnjiOpis = lines.get(i - 1);
			}
		}
		
		Util.tagoviSlike = tagoviSlike;
		Util.popisSlika = popisSlika;
		return tagovi;
	}

}

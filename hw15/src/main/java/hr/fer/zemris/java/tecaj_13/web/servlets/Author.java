package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Servlet that handles all responses commited to blog entries. That are mapped
 * to "/servleti/author/*".
 * 
 * @author Mihael Jaić
 *
 */

@WebServlet("/servleti/author/*")
public class Author extends HttpServlet {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		int pathLength = pathLength(path);

		if (pathLength > 3 || pathLength < 2) {
			req.setAttribute("message", "Invalid url path!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		if (pathLength == 2) {
			serveBlogAuthor(req, resp, path);

		} else {
			serveBlogEntry(req, resp, path);
		}
	}

	/**
	 * Displays list of entries written by user with given nick. If user is
	 * logged in as provided nick he can add new or edit existing blogs.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @param path
	 *            url path.
	 * @throws ServletException
	 * @throws IOException
	 */

	private void serveBlogAuthor(HttpServletRequest req, HttpServletResponse resp, String path)
			throws ServletException, IOException {
		String nick = path.replaceAll("/", "");
		BlogUser author = DAOProvider.getDAO().getUser(nick);

		if (author == null) {
			req.setAttribute("message", String.format("Invalid nickname! '%s'", nick));
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		req.setAttribute("blogs", author.getEntries());
		req.setAttribute("author", nick);
		req.setAttribute("noBlogs", author.getEntries().isEmpty());
		if (req.getSession().getAttribute("current.user.id") != null) {
			req.setAttribute("nick", req.getSession().getAttribute("current.user.nick"));
			req.setAttribute("logedin", nick.equals(req.getSession().getAttribute("current.user.nick")));
		} else {
			req.setAttribute("logedin", false);
		}

		req.getRequestDispatcher("/WEB-INF/pages/blogEntries.jsp").forward(req, resp);
	}

	/**
	 * If request is mapped to "/servleti/author/NICK/new" or
	 * "/servleti/author/NICK/edit" it will enable creating new blog entry or
	 * editing existing entry. Otherwise it will display blog with given blog
	 * id.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @param path
	 *            url path extension.
	 * @throws ServletException
	 * @throws IOException
	 */

	private void serveBlogEntry(HttpServletRequest req, HttpServletResponse resp, String path)
			throws ServletException, IOException {
		String[] temp = path.split("/");
		String nick = temp[1];
		String option = temp[2];

		BlogUser author = DAOProvider.getDAO().getUser(nick);

		if (author == null) {
			req.setAttribute("message", String.format("Invalid nickname! '%s'", nick));
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		req.setAttribute("blogs", author.getEntries());
		req.setAttribute("author", nick);
		if (option.equals("new")) {
			if (!checkNickname(nick, req, resp)) {
				return;
			}

			req.getRequestDispatcher("/WEB-INF/pages/newBlog.jsp").forward(req, resp);
		} else if (option.equals("edit")) {
			if (!checkNickname(nick, req, resp)) {
				return;
			}

			editBlog(req, resp, req.getParameter("id"));
		} else {
			showBlog(req, resp, option, nick);
		}
	}

	/**
	 * Checks if logged user's nickname matches authorization nickname.
	 * 
	 * @param nick
	 *            Nick.
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @return True if logged user's nickname matches authorization nickname,
	 *         false otherwise.
	 * @throws ServletException
	 * @throws IOException
	 */

	private boolean checkNickname(String nick, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!nick.equals(req.getSession().getAttribute("current.user.nick"))) {
			req.setAttribute("message", String.format("You must be logged in as '%s' to perform this action!", nick));
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return false;
		}

		return true;
	}

	/**
	 * Prepares attributes for edit page.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @param stringid
	 *            blog entry id in string form.
	 * @throws ServletException
	 * @throws IOException
	 */

	private void editBlog(HttpServletRequest req, HttpServletResponse resp, String stringid)
			throws ServletException, IOException {
		if (stringid == null) {
			req.setAttribute("message", "Blog entry id was not provided!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		Long id = null;
		try {
			id = Long.valueOf(stringid);
		} catch (NumberFormatException ex) {
			req.setAttribute("message", String.format("Invalid blog entry id! '%s'", stringid));
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(id);
		if (entry == null) {
			req.setAttribute("message", String.format("Blog entry with id %d doesn't exist!", id));
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		req.setAttribute("blogEntry", entry);
		req.getRequestDispatcher("/WEB-INF/pages/editBlog.jsp").forward(req, resp);
	}

	/**
	 * Prepares attributes for page that shows blog.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @param option
	 *            Blog entry id.
	 * @param nick
	 *            User nick.
	 * @throws ServletException
	 * @throws IOException
	 */

	private void showBlog(HttpServletRequest req, HttpServletResponse resp, String option, String nick)
			throws ServletException, IOException {
		long id = 0;
		try {
			id = Long.valueOf(option);
		} catch (NumberFormatException ex) {
			req.setAttribute("message", String.format("Invalid blog entry id! %s", option));
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
		if (blogEntry == null) {
			req.setAttribute("message", String.format("Blog entry with id %d doesn't exist!", id));
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		String logedNick = (String) req.getSession().getAttribute("current.user.nick");
		req.setAttribute("blogEntry", blogEntry);
		req.setAttribute("logedin", logedNick != null && logedNick.equals(nick));
		req.setAttribute("nocomments", blogEntry.getComments().isEmpty());
		req.getRequestDispatcher("/WEB-INF/pages/showBlog.jsp").forward(req, resp);
	}

	/**
	 * Calculates url path extension length.
	 * 
	 * @param path
	 *            url path extension.
	 * @return Length of url path extension.
	 */

	private int pathLength(String path) {
		return path.split("/").length;
	}
}

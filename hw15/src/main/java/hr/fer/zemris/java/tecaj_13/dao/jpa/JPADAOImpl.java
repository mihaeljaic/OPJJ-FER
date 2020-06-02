package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.util.Util;

/**
 * This is implementation of DAO subsystem using JPA technology.
 * 
 * @author Mihael Jaić
 *
 */

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public BlogUser getUser(String nick) throws DAOException {
		EntityManager entityManager = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		List<BlogUser> blogUser = (List<BlogUser>) entityManager.createNamedQuery("BlogUser.queryGetUserByNick")
				.setParameter("nick", nick).getResultList();

		return blogUser.isEmpty() ? null : blogUser.get(0);
	}

	@Override
	public void registerUser(String firstName, String lastName, String nick, String email, String password)
			throws DAOException {
		EntityManager entityManager = JPAEMProvider.getEntityManager();

		BlogUser user = new BlogUser();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setNick(nick);
		user.setPasswordHash(Util.getPasswordHash(password));

		entityManager.persist(user);

		System.out.println("Created new user");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlogUser> getAllUsers() throws DAOException {
		EntityManager entityManager = JPAEMProvider.getEntityManager();

		return (List<BlogUser>) entityManager.createNamedQuery("BlogUser.queryGetAllUsers").getResultList();
	}

	@Override
	public void createBlogEntry(String title, String text, String userNick) throws DAOException {
		EntityManager entityManager = JPAEMProvider.getEntityManager();

		BlogUser user = getUser(userNick);
		if (user == null) {
			// TODO: error
			return;
		}

		BlogEntry blogEntry = new BlogEntry();
		blogEntry.setBlogUser(user);
		blogEntry.setCreatedAt(new Date());
		blogEntry.setLastModifiedAt(blogEntry.getCreatedAt());
		blogEntry.setTitle(title);
		blogEntry.setText(text);

		entityManager.persist(blogEntry);
	}

	@Override
	public void createBlogComment(String message, BlogUser user, Long entryID) throws DAOException {
		EntityManager entityManager = JPAEMProvider.getEntityManager();

		BlogEntry entry = getBlogEntry(entryID);
		if (entry == null) {
			// TODO: error
			return;
		}

		String userEmail = user == null ? "Unknown" : user.getEmail();
		String nick = user == null ? "Anonymous" : user.getNick();

		BlogComment comment = new BlogComment();
		comment.setBlogEntry(entry);
		comment.setUsersEMail(userEmail);
		comment.setUsersNick(nick);
		comment.setMessage(message);
		comment.setPostedOn(new Date());

		entityManager.persist(comment);
	}

	@Override
	public void updateBlogEntry(Long id, String editedText) throws DAOException {
		EntityManager entityManager = JPAEMProvider.getEntityManager();

		BlogEntry blogEntry = entityManager.find(BlogEntry.class, id);
		blogEntry.setText(editedText);
		blogEntry.setLastModifiedAt(new Date());
	}

}
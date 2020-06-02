package hr.fer.zemris.java.tecaj_13.dao;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Interface towards subsystem for data persistency.
 * 
 * @author Mihael Jaić
 *
 */

public interface DAO {
	/**
	 * Gets blog entry from given blog entry id.
	 * 
	 * @param id
	 *            Blog entry id.
	 * @return Blog entry.
	 * @throws DAOException
	 *             If error occurred while reaching data.
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Gets blog user from given nick name.
	 * 
	 * @param nick
	 *            Nickname.
	 * @return Blog user.
	 * @throws DAOException
	 *             If error occurred while reaching data.
	 */
	public BlogUser getUser(String nick) throws DAOException;

	/**
	 * Adds blog user to database.
	 * 
	 * @param firstName
	 *            First name.
	 * @param lastName
	 *            Last name.
	 * @param nick
	 *            Nick name.
	 * @param email
	 *            Email.
	 * @param password
	 *            Password
	 * @throws DAOException
	 *             If error occurred while creating blog user.
	 */
	public void registerUser(String firstName, String lastName, String nick, String email, String password)
			throws DAOException;

	/**
	 * Gets list of all blog users.
	 * 
	 * @return List of all blog users.
	 * @throws DAOException
	 *             If error occurred while reaching data.
	 */
	public List<BlogUser> getAllUsers() throws DAOException;

	/**
	 * Adds blog entry to database.
	 * 
	 * @param title
	 *            Blog title.
	 * @param text
	 *            Blog text.
	 * @param userNick
	 *            Blog user nickname.
	 * @throws DAOException
	 *             If error occurred while creating blog entry.
	 */
	public void createBlogEntry(String title, String text, String userNick) throws DAOException;

	/**
	 * Adds blog comment to database.
	 * 
	 * @param message
	 *            Comment message.
	 * @param user
	 *            Blog user.
	 * @param entryID
	 *            Blog entry id.
	 * @throws DAOException
	 *             If error occurred while creating blog comment.
	 */
	public void createBlogComment(String message, BlogUser user, Long entryID) throws DAOException;

	/**
	 * Updates blog entrie's text by given id.
	 * 
	 * @param id
	 *            Blog entry id.
	 * @param editedText
	 *            New text.
	 * @throws DAOException
	 *             If error occurred while updating entry.
	 */
	public void updateBlogEntry(Long id, String editedText) throws DAOException;
}
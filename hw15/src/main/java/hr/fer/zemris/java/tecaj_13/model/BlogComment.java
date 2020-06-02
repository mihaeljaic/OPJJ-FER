package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Blog comment table that holds information about comment posted on website.
 * 
 * @author Mihael Jaić
 *
 */

@Entity
@Table(name = "blog_comments")
public class BlogComment {
	/** Comment id. */
	private Long id;
	/** Blog entry in which was comment posted. */
	private BlogEntry blogEntry;
	/** Email from user that posted comment. */
	private String usersEMail;
	/** Nickname from user that posted comment. */
	private String usersNick;
	/** Comment message. */
	private String message;
	/** Date and time when comment was posted. */
	private Date postedOn;

	/**
	 * Gets id.
	 * 
	 * @return id.
	 */

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets id.
	 * 
	 * @param id
	 *            comment id.
	 */

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets blog entry.
	 * 
	 * @return Blog entry.
	 */

	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * Sets blog entry.
	 * 
	 * @param blogEntry
	 *            Blog entry.
	 */

	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Gets user's email.
	 * 
	 * @return User's email.
	 */

	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Sets user's email.
	 * 
	 * @param usersEMail
	 *            User's email.
	 */

	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Gets user's nickname.
	 * 
	 * @return User's nickname.
	 */

	public String getUsersNick() {
		return usersNick;
	}

	/**
	 * Sets user's nickname.
	 * 
	 * @param usersNick
	 *            User's nickname.
	 */

	public void setUsersNick(String usersNick) {
		this.usersNick = usersNick;
	}

	/**
	 * Gets comment message.
	 * 
	 * @return Comment message.
	 */

	@Column(length = 4096, nullable = false)
	public String getMessage() {
		return message;
	}

	/**
	 * Sets comment message.
	 * 
	 * @param message
	 *            Comment message.
	 */

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets date when comment was posted.
	 * 
	 * @return Date when comment was posted.
	 */

	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Sets date when comment was posted.
	 * 
	 * @param postedOn
	 */

	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
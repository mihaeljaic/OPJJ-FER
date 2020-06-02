package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Blog user table that holds information about every user that has signed up to
 * website. Users with duplicate nickname are not allowed.
 * 
 * @author Mihael Jaić
 *
 */

@NamedQueries({
		@NamedQuery(name = "BlogUser.queryGetUserByNick", query = "select b from BlogUser as b where b.nick=:nick"),
		@NamedQuery(name = "BlogUser.queryGetAllUsers", query = "select b from BlogUser as b") })
@Entity
@Table(name = "blog_users")
@Cacheable(true)
public class BlogUser {
	/** User's id. */
	private Long id;
	/** User's first name. */
	private String firstName;
	/** User's last name. */
	private String lastName;
	/** User's nickname. */
	private String nick;
	/** User's email. */
	private String email;
	/** Password hash. */
	private String passwordHash;
	/** Blog entries written by user. */
	private List<BlogEntry> entries = new ArrayList<>();

	/**
	 * Gets user's id.
	 * 
	 * @return User's id.
	 */

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets user's id.
	 * 
	 * @param id
	 *            User's id.
	 */

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets all entries written by user.
	 * 
	 * @return Entries written by user.
	 */

	@OneToMany(mappedBy = "blogUser", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("lastModifiedAt DESC")
	public List<BlogEntry> getEntries() {
		return entries;
	}

	/**
	 * Sets entries written by user.
	 * 
	 * @param entries
	 *            Entries written by user.
	 */

	public void setEntries(List<BlogEntry> entries) {
		this.entries = entries;
	}

	/**
	 * Gets user's first name.
	 * 
	 * @return User's first name.
	 */

	@Column(length = 200, nullable = false)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets user's first name.
	 * 
	 * @param firstName
	 *            User's firts name.
	 */

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets user's last name.
	 * 
	 * @return User's last name.
	 */

	@Column(length = 200, nullable = false)
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets user's last name.
	 * 
	 * @param lastName
	 *            User's last name.
	 */

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets user's nick name.
	 * 
	 * @return User's nick name.
	 */

	@Column(length = 200, unique = true, nullable = false)
	public String getNick() {
		return nick;
	}

	/**
	 * Sets user's nick name.
	 * 
	 * @param nick
	 *            User's nick name.
	 */

	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Gets user's email.
	 * 
	 * @return User's email.
	 */

	@Column(length = 200, nullable = false)
	public String getEmail() {
		return email;
	}

	/**
	 * Sets user's email.
	 * 
	 * @param email
	 *            User's email.
	 */

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets password hash.
	 * 
	 * @return Password hash.
	 */

	@Column(length = 40, nullable = false)
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Sets password hash.
	 * 
	 * @param passwordHash
	 *            Password hash.
	 */

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
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
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Blog entry that holds information about blog entry that was added on website.
 * 
 * @author Mihael Jaić
 *
 */

@NamedQueries({
		@NamedQuery(name = "BlogEntry.upit1", query = "select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when") })
@Entity
@Table(name = "blog_entries")
@Cacheable(true)
public class BlogEntry {
	/** Blog entry id. */
	private Long id;
	/** Comments. */
	private List<BlogComment> comments = new ArrayList<>();
	/** Created on. */
	private Date createdAt;
	/** Last modified at. */
	private Date lastModifiedAt;
	/** Blog entry title. */
	private String title;
	/** Blog entry text. */
	private String text;
	/** Blog user that added this blog entry. */
	private BlogUser blogUser;

	/**
	 * Gets blog entry id.
	 * 
	 * @return Blog entry id.
	 */

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets blog entry id.
	 * 
	 * @param id
	 *            Blog entry id.
	 */

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets comments posted on this entry.
	 * 
	 * @return Comments.
	 */

	@OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}

	/**
	 * Sets comments.
	 * 
	 * @param comments
	 *            Comments.
	 */

	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Gets blog user.
	 * 
	 * @return Blog user.
	 */

	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogUser getBlogUser() {
		return blogUser;
	}

	/**
	 * Sets blog user.
	 * 
	 * @param blogUser
	 *            Blog user.
	 */

	public void setBlogUser(BlogUser blogUser) {
		this.blogUser = blogUser;
	}

	/**
	 * Gets date and time of creation of entry.
	 * 
	 * @return Date and time of creation of entry.
	 */

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets creation date.
	 * 
	 * @param createdAt
	 *            Creation date.
	 */

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets date of last modification.
	 * 
	 * @return Date of last modification.
	 */

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Sets date of last modification.
	 * 
	 * @param lastModifiedAt
	 *            Date of last modification.
	 */

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Gets title.
	 * 
	 * @return Title.
	 */

	@Column(length = 200, nullable = false)
	public String getTitle() {
		return title;
	}

	/**
	 * Sets title.
	 * 
	 * @param title
	 *            Title.
	 */

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets blog entry text.
	 * 
	 * @return Blog entry text.
	 */

	@Column(length = 4096, nullable = false)
	public String getText() {
		return text;
	}

	/**
	 * Sets blog entry text.
	 * 
	 * @param text
	 *            Blog entry text.
	 */

	public void setText(String text) {
		this.text = text;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
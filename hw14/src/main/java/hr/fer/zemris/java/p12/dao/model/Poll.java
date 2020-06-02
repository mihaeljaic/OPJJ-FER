package hr.fer.zemris.java.p12.dao.model;

/**
 * Class that models poll data.
 * 
 * @author Mihael Jaić
 *
 */

public class Poll {
	/** Poll id. */
	private final int id;
	/** Poll title. */
	private final String title;
	/** Poll message. */
	private final String message;

	/**
	 * Constructor that sets all attributes.
	 * 
	 * @param id
	 *            Poll id.
	 * @param title
	 *            Poll title.
	 * @param message
	 *            Poll message.
	 */

	public Poll(int id, String title, String message) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
	}

	/**
	 * Gets poll id.
	 * 
	 * @return Poll id.
	 */

	public int getId() {
		return id;
	}

	/**
	 * Gets poll title.
	 * 
	 * @return Poll title.
	 */

	public String getTitle() {
		return title;
	}

	/**
	 * Gets poll message.
	 * 
	 * @return Poll message.
	 */

	public String getMessage() {
		return message;
	}

}

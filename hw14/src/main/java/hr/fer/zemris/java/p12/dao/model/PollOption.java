package hr.fer.zemris.java.p12.dao.model;

/**
 * Class that models poll option offered in poll.
 * 
 * @author Mihael Jaić
 *
 */

public class PollOption {
	/** Poll option id. */
	private int id;
	/** Poll option title. */
	private String title;
	/** Poll option url link. */
	private String link;
	/** Number of votes for this poll option. */
	private int votesCount;

	/**
	 * Constructor that sets all attributes.
	 * 
	 * @param id
	 *            Poll option id.
	 * @param title
	 *            Poll option title.
	 * @param link
	 *            Poll option url link.
	 * @param votesCount
	 *            Number of votes for this poll option.
	 */

	public PollOption(int id, String title, String link, int votesCount) {
		super();
		this.id = id;
		this.title = title;
		this.link = link;
		this.votesCount = votesCount;
	}

	/**
	 * Gets poll option id.
	 * 
	 * @return Poll option id.
	 */

	public int getId() {
		return id;
	}

	/**
	 * Gets poll option title.
	 * 
	 * @return Poll option title.
	 */

	public String getTitle() {
		return title;
	}

	/**
	 * Gets poll option url link.
	 * 
	 * @return Gets poll option url link.
	 */

	public String getLink() {
		return link;
	}

	/**
	 * Gets number of votes for this poll option.
	 * 
	 * @return Number of votes for this poll option.
	 */

	public int getVotesCount() {
		return votesCount;
	}

}

package hr.fer.zemris.java.p12.dao;

import java.util.List;

import hr.fer.zemris.java.p12.dao.model.Poll;
import hr.fer.zemris.java.p12.dao.model.PollOption;

/**
 * Interface towards subsystem for data persistency.
 * 
 * @author Mihael Jaić
 *
 */
public interface DAO {
	/**
	 * Gets list of available polls.
	 * 
	 * @return List of available polls.
	 * @throws DAOException
	 *             If error occurred while getting polls.
	 */
	public List<Poll> getPolls() throws DAOException;

	/**
	 * Gets list of poll options with given poll id.
	 * 
	 * @param id
	 *            Poll id.
	 * @return List of poll options with given poll id.
	 * @throws DAOException
	 *             If error occurred while getting poll options.
	 */
	public List<PollOption> getPollOptions(String id) throws DAOException;

	/**
	 * Gets poll with given poll id.
	 * 
	 * @param id
	 *            Poll id.
	 * @return Poll with given id.
	 * @throws DAOException
	 *             If error occurred while getting poll.
	 */
	public Poll getPoll(String id) throws DAOException;

	/**
	 * Increments vote count in {@link PollOption} for given id.
	 * 
	 * @param id
	 *            Poll option id.
	 * @throws DAOException
	 *             If error occurred while updating poll option.
	 */
	public void updateVoteCount(String id) throws DAOException;

}
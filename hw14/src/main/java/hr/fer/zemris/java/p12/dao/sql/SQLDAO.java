package hr.fer.zemris.java.p12.dao.sql;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.dao.model.Poll;
import hr.fer.zemris.java.p12.dao.model.PollOption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is implementation of DAO subsystem using SQL technology. This concrete
 * implementation expects that connection is available via
 * {@link SQLConnectionProvider} class.
 * 
 * @author Mihael Jaić
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> getPolls() throws DAOException {
		List<Poll> polls = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select * from polls");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						Poll poll = new Poll(rs.getInt(1), rs.getString(2), rs.getString(3));
						polls.add(poll);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Error while reaching polls.", e);
		}

		return polls;
	}

	@Override
	public List<PollOption> getPollOptions(String id) throws DAOException {
		List<PollOption> polls = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();

		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
					String.format("select * from polloptions where pollid = %d", Integer.valueOf(id)));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						PollOption pollOptions = new PollOption(rs.getInt(1), rs.getString(2), rs.getString(3),
								rs.getInt(5));
						polls.add(pollOptions);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				pst.close();
			}
		} catch (SQLException e) {
			throw new DAOException("Error while reaching poll options.", e);
		}

		return polls;
	}

	@Override
	public void updateVoteCount(String id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		try {
			PreparedStatement pst = con.prepareStatement(String
					.format("update polloptions set votescount = votescount + 1 where id = %d", Integer.valueOf(id)));
			try {
				pst.executeUpdate();
			} finally {
				pst.close();
			}
		} catch (SQLException e) {
			throw new DAOException("Error while updating poll options.", e);
		}
	}

	@Override
	public Poll getPoll(String id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		PreparedStatement pst = null;
		Poll poll = null;
		try {
			pst = con.prepareStatement(String.format("select * from polls where id = %d", Integer.valueOf(id)));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if (rs != null && rs.next()) {
						poll = new Poll(rs.getInt(1), rs.getString(2), rs.getString(3));
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Error while reaching poll.", e);
		}
		
		return poll;
	}

}
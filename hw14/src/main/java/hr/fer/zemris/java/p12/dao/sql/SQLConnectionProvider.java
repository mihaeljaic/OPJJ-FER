package hr.fer.zemris.java.p12.dao.sql;

import java.sql.Connection;

/**
 * Saves connections to database in ThreadLocal object.
 * 
 * @author Mihael Jaić
 */
public class SQLConnectionProvider {
	/** Connections to database. */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();

	/**
	 * Sets connection to current thread.
	 * 
	 * @param con
	 *            Connection towards database.
	 */
	public static void setConnection(Connection con) {
		if (con == null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}

	/**
	 * Gets connection for current thread.
	 * 
	 * @return Connection towards database.
	 */
	public static Connection getConnection() {
		return connections.get();
	}

}
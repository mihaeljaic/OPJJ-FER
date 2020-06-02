package hr.fer.zemris.java.p12;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Initializes database for voting. <B>In order for this app to work, appropriate
 * database needs to be created.</B>.Database properties is located in
 * "/WEB-INF/dbsettings.properties"(relative path to servletcontext). Creates
 * two tables in database: Polls and PollOptions(if they don't already exist).
 * Information about polls is located in "/WEB-INF/polls.txt". In that files
 * there are information about each poll and path for poll options for each
 * poll.<br>
 * Polls is in following format: first row comes poll title, second row poll
 * message and in third row is path to poll options. Polls have to be separated
 * with ";".
 * 
 * @author Mihael Jaić
 *
 */

@WebListener
public class Initialization implements ServletContextListener {
	/** Relative path to database settings. */
	private static final String propertiesRelativePath = "/WEB-INF/dbsettings.properties";
	/** SQL command that creates polls table. */
	private static final String createTablePolls = "CREATE TABLE Polls(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, title VARCHAR(150) NOT NULL, message CLOB(2048) NOT NULL)";
	/** SQL command that creates poll options table. */
	private static final String createTablePollOptions = "CREATE TABLE PollOptions (id BIGINT PRIMARY KEY" + " "
			+ "GENERATED ALWAYS AS IDENTITY, optionTitle VARCHAR(100) NOT NULL, optionLink VARCHAR(150) NOT NULL,"
			+ " pollID BIGINT, votesCount BIGINT, FOREIGN KEY (pollID) REFERENCES Polls(id))";
	/** SQL error code for creating already existing table. */
	private static final String errorCode = "X0Y32";
	/** Relative path to polls file. */
	private static final String pollsRelativePath = "/WEB-INF/polls.txt";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Error while initializing pool.", e1);
		}

		cpds.setJdbcUrl(getConnectionURL(sce));
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);

		Connection con = null;
		try {
			con = cpds.getConnection();
		} catch (SQLException e) {
			System.out.println("Couldn't connect to database.");
			System.exit(0);
		}

		createTables(con);
		checkPollData(sce.getServletContext(), con);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates tables:: polls and polloptions.
	 * 
	 * @param con
	 *            Database connection.
	 */

	private void createTables(Connection con) {
		try {
			PreparedStatement pst = con.prepareStatement(createTablePolls);
			pst.executeUpdate();
			pst.close();
		} catch (SQLException ex) {
			if (!ex.getSQLState().equals(errorCode)) {
				throw new RuntimeException("Error while creating tables");
			}
		}

		try {
			PreparedStatement pst = con.prepareStatement(createTablePollOptions);
			pst.executeUpdate();
			pst.close();
		} catch (SQLException ex) {
			if (!ex.getSQLState().equals(errorCode)) {
				throw new RuntimeException("Error while creating tables");
			}
		}
	}

	/**
	 * Loads data for polls and polls options. Fills tables from poll text file.
	 * 
	 * @param context
	 *            Context used for gaining path to polls.
	 * @param con
	 *            Database connection.
	 */

	private void checkPollData(ServletContext context, Connection con) {
		String text = null;
		try {
			text = new String(Files.readAllBytes(Paths.get(context.getRealPath(pollsRelativePath))),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Couldn't open polls data file!");
			System.exit(0);
		}

		String[] pollData = text.split(";\\R");
		for (String poll : pollData) {
			String[] temp = poll.split("\\R");
			fillPolls(temp[0], temp[1], context.getRealPath(temp[2]), con);
		}
	}

	/**
	 * Inserts polls and polloptions from polls file.
	 * 
	 * @param title
	 *            Poll title.
	 * @param message
	 *            Poll message.
	 * @param path
	 *            Path to polls file.
	 * @param con
	 *            Database connection.
	 */

	private void fillPolls(String title, String message, String path, Connection con) {
		try {
			PreparedStatement insert = con
					.prepareStatement(String.format("insert into Polls values (DEFAULT, '%s', '%s')", title, message));
			insert.executeUpdate();

			ResultSet newID = con.prepareStatement("SELECT IDENTITY_VAL_LOCAL() from polls").executeQuery();
			newID.next();
			fillPollOptions(path, newID.getInt(1), con);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Couldn't fill database.");
		}
	}

	/**
	 * Inserts polloptions in polloptions table.
	 * 
	 * @param path
	 *            Path to polloptions file.
	 * @param id
	 *            Poll id.
	 * @param con
	 *            Database connection.
	 * @throws SQLException
	 *             SQL error.
	 */

	private void fillPollOptions(String path, int id, Connection con) throws SQLException {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't open poll options file.");
		}

		for (String line : lines) {
			String[] temp = line.split("\t");
			if (temp.length != 2) {
				throw new RuntimeException("Invalid file format.");
			}

			PreparedStatement pst = con.prepareStatement(
					String.format("insert into PollOptions values (DEFAULT, '%s', '%s', %d, 0)", temp[0], temp[1], id));
			pst.executeUpdate();
		}
	}

	/**
	 * Gets connection url for connection to database.
	 * 
	 * @param sce
	 *            Servlet context event.
	 * @return Connection url.
	 */

	private String getConnectionURL(ServletContextEvent sce) {
		Properties dbProperties = new Properties();

		try (InputStream inputStream = Files.newInputStream(
				Paths.get(sce.getServletContext().getRealPath(propertiesRelativePath)), StandardOpenOption.READ)) {
			dbProperties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't open database properties file.");
		}

		String host = dbProperties.getProperty("host");
		String port = dbProperties.getProperty("port");
		String name = dbProperties.getProperty("name");
		String user = dbProperties.getProperty("user");
		String password = dbProperties.getProperty("password");

		if (host == null || port == null || name == null || user == null || password == null) {
			throw new RuntimeException("Missing properties in database properties file.");
		}

		return String.format("jdbc:derby://%s:%s/%s;user=%s;password=%s", host, port, name, user, password);
	}

}
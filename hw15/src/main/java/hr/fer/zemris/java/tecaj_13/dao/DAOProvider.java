package hr.fer.zemris.java.tecaj_13.dao;
import hr.fer.zemris.java.tecaj_13.dao.jpa.JPADAOImpl;

/**
 * Singleton class that knows who to return as service provider for data
 * persistency subsystem.
 * 
 * @author Mihael Jaić
 * 
 */

public class DAOProvider {
	/** Instance of dao provider. */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Gets instance of dao provider.
	 * 
	 * @return Object that encapsulates access to data persistency layer.
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}
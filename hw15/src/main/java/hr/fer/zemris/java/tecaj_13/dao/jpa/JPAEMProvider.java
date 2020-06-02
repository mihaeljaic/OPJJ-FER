package hr.fer.zemris.java.tecaj_13.dao.jpa;

import hr.fer.zemris.java.tecaj_13.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * {@link EntityManager} provider. Sets entity manager for every visiting
 * thread.
 * 
 * @author Mihael Jaić
 *
 */

public class JPAEMProvider {
	/** Thread local. */
	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * Gets entity manager.
	 * 
	 * @return Entity manager.
	 */

	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if (ldata == null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Commits transaction and closes entity manager.
	 * 
	 * @throws DAOException
	 *             If entity manager was unable to close.
	 */

	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if (ldata == null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch (Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch (Exception ex) {
			if (dex != null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if (dex != null)
			throw dex;
	}

	/**
	 * Class that holds reference to data(entity manager).
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class LocalData {
		/** Entity manager. */
		EntityManager em;
	}

}
package hr.fer.zemris.java.tecaj_13.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * {@link EntityManagerFactory} provider.
 * 
 * @author Mihael Jaić
 *
 */

public class JPAEMFProvider {
	/** Entity manager factory. */
	public static EntityManagerFactory emf;

	/**
	 * Gets entity manager factory.
	 * 
	 * @return Entity manager factory.
	 */

	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Sets entity manager factory.
	 * 
	 * @param emf
	 *            Entity manager factory.
	 */

	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}
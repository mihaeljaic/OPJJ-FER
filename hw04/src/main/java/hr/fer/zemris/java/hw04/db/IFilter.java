package hr.fer.zemris.java.hw04.db;

/**
 * Strategy that filters student records by specific criteria defined by
 * strategy implementation.
 * 
 * @author Mihael Jaić
 *
 */

public interface IFilter {
	/**
	 * Checks if student satisfies expression.
	 * 
	 * @param record
	 *            Student record.
	 * @return True if student satisfies expression, false otherwise.
	 */
	public boolean accepts(StudentRecord record);
}

package hr.fer.zemris.java.hw04.db;

/**
 * Strategy for getting field values of student record's attributes.
 * 
 * @author Mihael Jaić
 *
 */

public interface IFieldValueGetter {
	/**
	 * Gets some field value of student record.
	 * 
	 * @param record
	 *            Student record.
	 * @return Value of specified field value of student record.
	 */
	public String get(StudentRecord record);
}

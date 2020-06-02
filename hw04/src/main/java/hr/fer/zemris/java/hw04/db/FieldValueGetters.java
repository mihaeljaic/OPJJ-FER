package hr.fer.zemris.java.hw04.db;

/**
 * Class that offers static references to implementations of {@link IFieldValueGetter}.
 * 
 * @author Mihael Jaić
 *
 */

public class FieldValueGetters {
	/**
	 * Getter for first name of student.
	 */
	public static final IFieldValueGetter FIRST_NAME;
	/**
	 * Getter for last name of student.
	 */
	public static final IFieldValueGetter LAST_NAME;
	/**
	 * Getter for jmbag of student.
	 */
	public static final IFieldValueGetter JMBAG;
	
	static {
		FIRST_NAME = record -> record.getFirstName();
		LAST_NAME = record -> record.getLastName();
		JMBAG = record -> record.getJmbag();
	}
}

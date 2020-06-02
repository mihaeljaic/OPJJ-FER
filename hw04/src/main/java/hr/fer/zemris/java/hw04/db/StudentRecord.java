package hr.fer.zemris.java.hw04.db;

/**
 * Class that stores data about students.
 * 
 * @author Mihael Jaić
 *
 */

public class StudentRecord {
	/**
	 * jmbag.
	 */
	private final String jmbag;
	/**
	 * Last name.
	 */
	private final String lastName;
	/**
	 * First name.
	 */
	private final String firstName;
	/**
	 * Final grade.
	 */
	private final int finalGrade;

	/**
	 * Sets student's attributes.
	 * 
	 * @param jmbag
	 *            jmbag.
	 * @param lastName
	 *            Last name.
	 * @param firstName
	 *            First name.
	 * @param finalGrade
	 *            Final grade.
	 * @throws IllegalArgumentException
	 *             If any jmbag, first name or last name is null.
	 */

	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade)
			throws IllegalArgumentException {
		super();

		if (jmbag == null || lastName == null || firstName == null) {
			throw new IllegalArgumentException("Values null are not allowed in student record.");
		}

		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}

	/**
	 * Gets jmbag.
	 * 
	 * @return jmbag.
	 */

	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Gets last name.
	 * 
	 * @return Last name.
	 */

	public String getLastName() {
		return lastName;
	}

	/**
	 * Gets first name
	 * 
	 * @return First name.
	 */

	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets final grade.
	 * 
	 * @return Final grade.
	 */

	public int getFinalGrade() {
		return finalGrade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return jmbag + " " + lastName + " " + firstName + " " + finalGrade;
	}

}

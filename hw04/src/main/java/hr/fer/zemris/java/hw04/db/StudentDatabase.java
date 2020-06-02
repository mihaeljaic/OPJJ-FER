package hr.fer.zemris.java.hw04.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.collections.SimpleHashtable;

/**
 * Database that stores student records. Accepts text input which is parsed into
 * student records data. Text has to be in following form. First comes jmbag.
 * Then comes one or more last names. After it comes one or more first names.
 * And last attribute must end with integer number that represents final
 * grade.There can be multiple first names and last names but they have to be
 * separated with single space ' ' while jmbag, firstName, lastName and grade
 * have to be separated with '\t'. No duplicates are allowed!
 * 
 * @author Mihael Jaić
 *
 */

public class StudentDatabase {
	/**
	 * List of student records.
	 */
	private List<StudentRecord> studentRecords;
	/**
	 * Hash table of student records.
	 */
	private SimpleHashtable<String, StudentRecord> studentRecordsHash;

	/**
	 * Input text that is parsed into student records.
	 * 
	 * @param content
	 *            Input text.
	 * @throws IllegalArgumentException
	 *             If text is null. Or text isn't in valid format.
	 */

	public StudentDatabase(List<String> content) throws IllegalArgumentException {
		if (content == null) {
			throw new IllegalArgumentException("Input string can't be null.");
		}

		fillListAndHash(content);
	}

	/**
	 * Gets student record from given studen's jmbag. If there is no student
	 * with such jmbag null is returned.
	 * 
	 * @param jmbag
	 *            Student's jmbag.
	 * @return Student record.
	 * @throws IllegalStateException
	 *             If there are no student records in database.
	 */

	public StudentRecord forJMBAG(String jmbag) throws IllegalStateException {
		if (studentRecordsHash == null) {
			throw new IllegalStateException("No student records in database.");
		}

		return studentRecordsHash.get(jmbag);
	}

	/**
	 * Iterates through list of student records and performs filtering by given
	 * strategy implementation of filter.
	 * 
	 * @param filter
	 *            Strategy implementation of filter.
	 * @return List of students that were accepted by filter.
	 * @throws IllegalArgumentException
	 *             If filter is null.
	 */

	public List<StudentRecord> filter(IFilter filter) throws IllegalArgumentException {
		if (filter == null) {
			throw new IllegalArgumentException("Filter can't be null.");
		}

		List<StudentRecord> tempList = new ArrayList<>();

		for (StudentRecord studentRecord : studentRecords) {
			if (filter.accepts(studentRecord)) {
				tempList.add(studentRecord);
			}
		}

		return tempList;
	}

	/**
	 * Fills list and hashtable with student records which are parsed from text
	 * input.
	 * 
	 * @param content
	 *            Text input.
	 */

	private void fillListAndHash(List<String> content) {
		studentRecords = new ArrayList<>(content.size());
		studentRecordsHash = new SimpleHashtable<>(content.size());

		for (String s : content) {
			StudentRecord studentRecord = makeStudentRecord(s.split("\t"));

			studentRecords.add(studentRecord);
			studentRecordsHash.put(studentRecord.getJmbag(), studentRecord);
		}
	}

	/**
	 * Makes student record from given string array.
	 * 
	 * @param data
	 *            String array.
	 * @return Student record.
	 */

	private StudentRecord makeStudentRecord(String[] data) {
		if (data.length != 4) {
			throw new IllegalArgumentException("Invalid number of attributes. At line " + (studentRecords.size() + 1));
		}

		if (studentRecordsHash.containsKey(data[0])) {
			throw new IllegalArgumentException("JMBAG " + data[0] + " already exists.");
		}

		int finalGrade = 0;

		try {
			finalGrade = Integer.parseInt(data[3]);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Student's grade is expected as last.");
		}

		return new StudentRecord(data[0], data[1], data[2], finalGrade);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (StudentRecord record : studentRecords) {
			sb.append(String.format("%s%n", record));
		}

		// Removes last '\n'.
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
}

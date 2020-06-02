package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Database is built from "database.txt" file in this project.
 * 
 * @author Mihael Jaić
 *
 */

public class StudentDatabaseTest {

	private StudentDatabase database;

	@Before
	public void init() throws IllegalArgumentException, IOException {
		database = new StudentDatabase(Files.readAllLines(Paths.get("database.txt"), StandardCharsets.UTF_8));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNull() {
		StudentDatabase database = new StudentDatabase(null);
	}

	@Test
	public void testAllStudentsAreInDatabase() throws IllegalArgumentException, IOException {
		// Accepts all student records.
		List<StudentRecord> allStudents = database.filter(record -> true);
		// Accepts no student records.
		List<StudentRecord> emptyList = database.filter(record -> false);
		
		// There are 63 student records in "database.txt" file.
		assertEquals(63, allStudents.size());
		assertEquals(0, emptyList.size());
		
		// Prints database on standard output.
		printAll();
	}
	
	public void printAll() {
		System.out.println(database);
	}

	@Test
	public void testSomeStudentsForJMBAG() throws IllegalArgumentException, IOException {
		StudentRecord student1 = new StudentRecord("0000000012", "Franković", "Hrvoje", 5);
		StudentRecord student2 = new StudentRecord("0000000008", "Ćurić", "Marko", 5);
		StudentRecord student3 = new StudentRecord("0000000024", "Karlović", "Đive", 5);
		StudentRecord student4 = new StudentRecord("0000000029", "Kos-Grabar", "Ivo", 2);

		assertTrue(studentsHaveSameAttributes(student1, database.forJMBAG("0000000012")));
		assertTrue(studentsHaveSameAttributes(student2, database.forJMBAG("0000000008")));
		assertTrue(studentsHaveSameAttributes(student3, database.forJMBAG("0000000024")));
		assertTrue(studentsHaveSameAttributes(student4, database.forJMBAG("0000000029")));
	}

	private boolean studentsHaveSameAttributes(StudentRecord s1, StudentRecord s2) {
		return s1.getJmbag().equals(s2.getJmbag()) && s1.getFirstName().equals(s2.getFirstName())
				&& s1.getLastName().equals(s2.getLastName()) && s1.getFinalGrade() == s2.getFinalGrade();
	}


}

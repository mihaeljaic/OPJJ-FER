package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class FieldValueGettersTest {
	
	
	@Test
	public void test1() {
		StudentRecord record = new StudentRecord("0000000002", "Bakamović", "Petra", 3);
		
		IFieldValueGetter jmbag = FieldValueGetters.JMBAG;
		IFieldValueGetter lastName = FieldValueGetters.LAST_NAME;
		IFieldValueGetter firstName = FieldValueGetters.FIRST_NAME;
		
		assertEquals("0000000002", jmbag.get(record));
		assertEquals("Bakamović", lastName.get(record));
		assertEquals("Petra", firstName.get(record));
	}
	
	@Test
	public void test2() {
		StudentRecord record = new StudentRecord("0000000024", "Karlović", "Đive", 3);
		
		IFieldValueGetter jmbag = FieldValueGetters.JMBAG;
		IFieldValueGetter lastName = FieldValueGetters.LAST_NAME;
		IFieldValueGetter firstName = FieldValueGetters.FIRST_NAME;
		
		assertEquals("0000000024", jmbag.get(record));
		assertEquals("Karlović", lastName.get(record));
		assertEquals("Đive", firstName.get(record));
	}

}

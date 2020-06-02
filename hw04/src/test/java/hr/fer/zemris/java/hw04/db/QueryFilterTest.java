package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class QueryFilterTest {

	@Test
	public void testFilter() {
		List<StudentRecord> records = new ArrayList<>();
		List<ConditionalExpression> expr = new ArrayList<>();
		
		records.add(new StudentRecord("0000000018", "Gužvinec", "Matija", 3));
		records.add(new StudentRecord("0000000048", "Rezić", "Bruno", 5));
		records.add(new StudentRecord("0000000003", "Bosnić", "Andrea", 4));
		records.add(new StudentRecord("0000000038", "Markotić", "Josip", 3));
		records.add(new StudentRecord("0000000011", "Dvorničić", "Jura", 4));
		records.add(new StudentRecord("0000000023", "Kalvarešin", "Ana", 4));
		
		expr.add(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Jasna",
				ComparisonOperators.LESS));
		expr.add(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000023",
				ComparisonOperators.NOT_EQUALS));
		
		QueryFilter filter = new QueryFilter(expr);
		
		assertFalse(filter.accepts(records.get(0)));
		assertTrue(filter.accepts(records.get(1)));
		assertTrue(filter.accepts(records.get(2)));
		assertFalse(filter.accepts(records.get(3)));
		assertFalse(filter.accepts(records.get(4)));
		assertFalse(filter.accepts(records.get(5)));
	}

}

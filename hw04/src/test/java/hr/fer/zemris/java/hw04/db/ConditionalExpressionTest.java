package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ConditionalExpressionTest {
	private List<StudentRecord> records;

	@Before
	public void init() {
		records = new ArrayList<>();

		records.add(new StudentRecord("0000000024", "Karlović", "Đive", 5));
		records.add(new StudentRecord("0000000048", "Rezić", "Bruno", 5));
		records.add(new StudentRecord("0000000003", "Bosnić", "Andrea", 4));
		records.add(new StudentRecord("0000000038", "Markotić", "Josip", 3));
		records.add(new StudentRecord("0000000011", "Dvorničić", "Jura", 4));
		records.add(new StudentRecord("0000000023", "Kalvarešin", "Ana", 4));
	}

	@Test
	public void testExpression1() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bos*",
				ComparisonOperators.LIKE);

		// Only student "Andrea Bosnić" will satisfy expression.
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(0)),
				expr.getStringLiteral()));
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(1)),
				expr.getStringLiteral()));
		assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(2)),
				expr.getStringLiteral()));
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(3)),
				expr.getStringLiteral()));
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(4)),
				expr.getStringLiteral()));
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(5)),
				expr.getStringLiteral()));
	}

	@Test
	public void testExpression2() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Jasna",
				ComparisonOperators.LESS);

		// This should be false because 'Đ' has higher value than 'J'.
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(0)),
				expr.getStringLiteral()));
		assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(1)),
				expr.getStringLiteral()));
		assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(2)),
				expr.getStringLiteral()));
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(3)),
				expr.getStringLiteral()));
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(4)),
				expr.getStringLiteral()));
		assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(5)),
				expr.getStringLiteral()));
	}

	@Test
	public void testExpression3() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "J*",
				ComparisonOperators.LIKE);

		// Only "Josip" and "Jura" start with "J".
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(0)),
				expr.getStringLiteral()));
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(1)),
				expr.getStringLiteral()));
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(2)),
				expr.getStringLiteral()));
		assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(3)),
				expr.getStringLiteral()));
		assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(4)),
				expr.getStringLiteral()));
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(5)),
				expr.getStringLiteral()));
	}

	@Test
	public void testExpression4() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000023",
				ComparisonOperators.NOT_EQUALS);

		assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(0)),
				expr.getStringLiteral()));
		assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(1)),
				expr.getStringLiteral()));
		assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(2)),
				expr.getStringLiteral()));
		assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(3)),
				expr.getStringLiteral()));
		assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(4)),
				expr.getStringLiteral()));
		assertFalse(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(records.get(5)),
				expr.getStringLiteral()));
	}

}

package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.*;

import java.lang.reflect.Array;

import org.junit.Before;
import org.junit.Test;

public class ArrayIndexedCollectionTest {

	private ArrayIndexedCollection collection;
	private ArrayIndexedCollection collection2;
	private ArrayIndexedCollection collection3;

	@Before
	public void init() {
		collection = new ArrayIndexedCollection();

		collection.add(new Integer(20));
		collection.add("New York");
		collection.add("San Francisco");

		collection2 = new ArrayIndexedCollection(collection);
		collection2.remove(0);
		collection2.insert("Trabant", 1);

		collection3 = new ArrayIndexedCollection(collection2);
		collection3.clear();
	}

	@Test(expected = IllegalArgumentException.class)
	public void addNull() {
		collection.add(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void arrayWithNegativeInitialCapacity() {
		new ArrayIndexedCollection(-5);
	}

	@Test
	public void addAndToArrayTest() {
		Object[] expected = new Object[] { new Integer(20), "New York", "San Francisco" };
		assertArrayEquals(expected, collection.toArray());
	}

	@Test
	public void addAllAndToArrayTest() {
		ArrayIndexedCollection temp = new ArrayIndexedCollection(collection);
		assertArrayEquals(collection.toArray(), temp.toArray());
	}

	@Test
	public void sizeOfCollection1() {
		assertEquals(3, collection.size());
	}

	@Test
	public void testIfCollection3IsCleared() {
		assertEquals(0, collection3.size());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void getOutOfArray() {
		collection.get(3);
	}

	@Test
	public void getSecondObjectInInitialArray() {
		assertEquals("New York", collection.get(1));
	}

	@Test
	public void indexOfNull() {
		assertEquals(-1, collection.indexOf(null));
	}

	@Test
	public void indexOfSanFranciscoInFirstCollection() {
		assertEquals(2, collection.indexOf("San Francisco"));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void removeOutOfArray() {
		collection.remove(3);
	}

	@Test
	public void testIf20IsOutOfCollection2() {
		assertEquals(-1, collection2.indexOf(20));
	}

	@Test
	public void testIfNewYorkIsMovedToPosition0InCollection2() {
		assertEquals(0, collection2.indexOf("New York"));
	}

	@Test
	public void testIfTrabantIsInsertedAtPosition1() {
		assertEquals(1, collection2.indexOf("Trabant"));
	}

	@Test
	public void testIfSanFranciscoIsAgainAtPosition2InCollection2() {
		assertEquals(2, collection2.indexOf("San Francisco"));
	}

	@Test
	public void CheckIFNewYorkIsRemovedFromCollection3() {
		assertEquals(-1, collection3.indexOf("New York"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void insertNull() {
		collection.insert(null, 1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void insertOutOfArray() {
		collection.insert(new Integer(25), -2);
	}

	@Test
	public void clearEmptyArray() {
		ArrayIndexedCollection temp = new ArrayIndexedCollection();
		temp.clear();
	}

	@Test
	public void containsNull() {
		assertEquals(false, collection.contains(null));
	}

	@Test
	public void collectionContainsNewYork() {
		assertEquals(true, collection.contains("New York"));
	}
	
	@Test
	public void resizeTest() {
		ArrayIndexedCollection c1 = new ArrayIndexedCollection(2);
		c1.addAll(collection);
		c1.add("aaa");
		c1.add("bbb");
		assertEquals(5, c1.size());
	}

}

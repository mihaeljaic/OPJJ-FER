package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LinkedListIndexedCollectionTest {
	
	private ArrayIndexedCollection c1;
	private LinkedListIndexedCollection linkedList1;
	private LinkedListIndexedCollection linkedList2;
	private LinkedListIndexedCollection linkedList3;
	
	@Before
	public void init() {
		c1 = new ArrayIndexedCollection();
		
		c1.add(new Integer(20));
		c1.add("New York");
		c1.add("San Francisco");
		
		linkedList1 = new LinkedListIndexedCollection(c1);
		linkedList2 = new LinkedListIndexedCollection(linkedList1);
		linkedList3 = new LinkedListIndexedCollection(c1);
		
		linkedList2.remove(1);
		linkedList2.insert("Los Angeles", 2);
		linkedList2.insert("Asd", 3);
		linkedList2.remove("Asd");
		
		linkedList3.clear();
	}

	@Test
	public void addAll() {
		// Tests addAll, forEach, add and toArray methods.
		assertArrayEquals(c1.toArray(), linkedList1.toArray());
	}
	
	@Test
	public void elementsInLinkedList2() {
		Object[] expected = new Object[] {
				new Integer(20), "San Francisco", "Los Angeles"
		};
		assertArrayEquals(expected, linkedList2.toArray());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addNull() {
		linkedList1.add(null);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void getElementOutOfArray() {
		linkedList1.get(-2);
	}
	
	@Test
	public void getElementAtIndex1InList1() {
		assertEquals("New York", linkedList1.get(1));
	}
	
	@Test
	public void indexOfNewYorkInList1() {
		assertEquals(1, linkedList1.indexOf("New York"));
	}
	
	@Test
	public void checkIfSanFranciscoHasMovedToPositionOneInList2() {
		assertEquals(1, linkedList2.indexOf("San Francisco"));
	}
	
	@Test
	public void checkIfNewYorkIsRemovedFromList2() {
		assertEquals(-1, linkedList2.indexOf("New York"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void insertNullValue() {
		linkedList1.insert(null, 1);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void insertOutOfList() {
		linkedList1.insert("asd", 5);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void removeOutOfArray() {
		linkedList1.remove(-1);
	}
	
	@Test
	public void sizeOfList1() {
		assertEquals(3, linkedList1.size());
	}
	
	@Test
	public void sizeOfList2() {
		assertEquals(3, linkedList2.size());
	}
	
	@Test
	public void sizeOfList3() {
		assertEquals(0, linkedList3.size());
	}
	
	@Test
	public void list1ContainsLosAngeles() {
		assertEquals(false, linkedList1.contains("Los Angeles"));
	}
	
	@Test
	public void list2ContainsLosAngeles() {
		assertEquals(true, linkedList2.contains("Los Angeles"));
	}
	
	@Test
	public void list2ContainsAsd() {
		assertEquals(false, linkedList2.contains("Asd"));
	}

}

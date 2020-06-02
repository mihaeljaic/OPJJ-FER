package hr.fer.zemris.java.hw04.collections;

import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

import hr.fer.zemris.java.hw04.collections.*;
import hr.fer.zemris.java.hw04.collections.SimpleHashtable.TableEntry;

// Some tests may fail because of different hashing of String objects!!!

public class SimpleHashtableTest {

	/**
	 * Example hash table used in most test cases.
	 * 
	 * @return Example hash table.
	 */

	private SimpleHashtable<String, Integer> exampleHashTable() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);

		return examMarks;
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCapacity() {
		new SimpleHashtable<Object, Object>(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPutNullKey() {
		SimpleHashtable<Object, Integer> hash = new SimpleHashtable<>();
		hash.put(null, 4);
	}

	@Test
	public void testContainsNullKeyOrInvalidTypeKey() {
		SimpleHashtable<Integer, Integer> examMarks = new SimpleHashtable<>();

		assertFalse(examMarks.containsKey(null));
		assertFalse(examMarks.containsKey("asd"));
	}

	@Test
	public void testContainsKey() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		assertTrue(examMarks.containsKey("Jasna"));
		assertTrue(examMarks.containsKey("Kristina"));
		assertFalse(examMarks.containsKey("iVaNa"));
		assertFalse(examMarks.containsKey("asd"));
	}

	@Test
	public void testContainsNullValueOrInvalidTypeValue() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();

		assertFalse(examMarks.containsValue(null));
		assertFalse(examMarks.containsValue("asd"));
	}

	@Test
	public void testContainsValue() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		assertTrue(examMarks.containsValue(2));
		assertTrue(examMarks.containsValue(5));
		assertFalse(examMarks.containsValue(20));
		assertFalse(examMarks.containsValue(1));
	}

	@Test
	public void testSize() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();

		assertEquals(0, examMarks.size());

		examMarks = exampleHashTable();

		assertEquals(4, examMarks.size());
	}

	@Test
	public void testIsEmpty() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>();

		assertTrue(examMarks.isEmpty());

		examMarks = exampleHashTable();

		assertFalse(examMarks.isEmpty());
	}

	@Test
	public void testGetValue() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		assertEquals(null, examMarks.get(null));
		assertEquals(null, examMarks.get("Sandra"));

		assertEquals(Integer.valueOf(5), examMarks.get("Ivana"));
		assertEquals(Integer.valueOf(2), examMarks.get("Jasna"));
		assertEquals(Integer.valueOf(5), examMarks.get("Kristina"));
	}

	@Test
	public void testRemoveNullOrNonExistingKey() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		examMarks.remove(null);
		examMarks.remove(5);
		examMarks.remove("Sandra");

		assertEquals(4, examMarks.size());
		assertTrue(examMarks.containsKey("Ivana") && examMarks.containsKey("Jasna") && examMarks.containsKey("Kristina")
				&& examMarks.containsKey("Ante"));
	}

	@Test
	public void testRemove2Keys() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		examMarks.remove("Ivana");
		examMarks.remove("Ante");

		assertEquals(2, examMarks.size());
		assertFalse(examMarks.containsKey("Ivana") || examMarks.containsKey("Ante"));
		assertTrue(examMarks.containsKey("Jasna") && examMarks.containsKey("Kristina"));
	}

	@Test
	public void testToString() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		// This may vary depending on string hash function in different java
		// versions. "Ante" is in 6th slot and others are in 7th slot in table.
		assertEquals("[Ante=2, Ivana=5, Jasna=2, Kristina=5]", examMarks.toString());

		examMarks.remove("Jasna");
		examMarks.put("Štefica", 5);

		// "Štefica" should hash into 6th slot.
		assertEquals("[Ante=2, Štefica=5, Ivana=5, Kristina=5]", examMarks.toString());
	}

	@Test
	public void testClear() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		examMarks.clear();

		assertEquals(0, examMarks.size());
		assertFalse(examMarks.containsKey("Ivana") || examMarks.containsKey("Ante") || examMarks.containsKey("Jasna")
				|| examMarks.containsKey("Kristina"));

		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);

		assertEquals(4, examMarks.size());
		assertTrue(examMarks.containsKey("Ivana") && examMarks.containsKey("Ante") && examMarks.containsKey("Jasna")
				&& examMarks.containsKey("Kristina"));
	}

	@Test
	public void testAddLotsOfKeysResizeTest() {
		SimpleHashtable<String, Integer> hash = new SimpleHashtable<>(2);
		int numberOfKeys = 1000;

		for (int i = 0; i < numberOfKeys; i++) {
			hash.put(Integer.toString(i), i);
		}

		assertEquals(numberOfKeys, hash.size());

		for (int i = 0; i < numberOfKeys; i++) {
			assertTrue(hash.containsKey(Integer.toString(i)));
		}
	}


	@Test
	public void testIteratorValidRemove() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				iter.remove();
			}
		}

		assertEquals(3, examMarks.size());
		assertFalse(examMarks.containsKey("Ivana"));
	}

	@Test
	public void testIteratorRemoveAll() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			iter.remove();
		}

		assertEquals(0, examMarks.size());
		assertFalse(examMarks.containsKey("Ante") || examMarks.containsKey("Ivana") || examMarks.containsKey("Jasna")
				|| examMarks.containsKey("Kristina"));
	}

	@Test(expected = IllegalStateException.class)
	public void testIteratorRemoveTwice() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				iter.remove();
				iter.remove();
			}
		}
	}

	@Test(expected = ConcurrentModificationException.class)
	public void testIteratorModifiedOutside() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		for (TableEntry<String, Integer> pair : examMarks) {
			if (pair.getKey().equals("Ivana")) {
				// After this next iterator command will throw exception.
				examMarks.remove(pair.getKey());
			}
		}
	}

	@Test(expected = ConcurrentModificationException.class)
	public void testIteratorModifiedByOtherIterator() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		Iterator<TableEntry<String, Integer>> iter = examMarks.iterator();

		for (TableEntry<String, Integer> pair : examMarks) {
			while (iter.hasNext()) {
				TableEntry<String, Integer> pair2 = iter.next();

				if (pair2.getKey().equals("Ivana")) {
					// Inner iterator removes element. Next command in outer
					// iterator will throw exception.
					iter.remove();
				}
			}
		}
	}

	@Test
	public void testIteratorHasNextEmptyTable() {
		SimpleHashtable<String, Integer> hash = new SimpleHashtable<>();

		Iterator<TableEntry<String, Integer>> it = hash.iterator();
		assertFalse(it.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void testIteratorGetNextEmptyTable() {
		SimpleHashtable<String, Integer> hash = new SimpleHashtable<>();

		Iterator<TableEntry<String, Integer>> it = hash.iterator();
		// There is no next element. Should throw exception.
		it.next();
	}

	@Test(expected = NoSuchElementException.class)
	public void testIteratorGetNextNoMoreElementsInTable() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		Iterator<TableEntry<String, Integer>> it = examMarks.iterator();
		while (it.hasNext()) {
			it.next();
		}
		// No more elements in hash table
		it.next();

	}

	@Test(expected = IllegalStateException.class)
	public void testIteratorRemoveEmptyTable() {
		SimpleHashtable<String, Integer> hash = new SimpleHashtable<>();

		Iterator<TableEntry<String, Integer>> it = hash.iterator();
		// There is nothing to be removed.
		it.remove();
	}

	@Test
	public void testIteratorNextElementAfterRemove() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		Iterator<TableEntry<String, Integer>> it = examMarks.iterator();

		it.next();
		// Removes Ante. Should be first element in table.
		it.remove();

		// Next key now should be Ivana. Because it was second key in original
		// table.
		assertEquals("Ivana", it.next().getKey());
	}

	@Test
	public void testIteratorPassedAllElements() {
		SimpleHashtable<String, Integer> examMarks = exampleHashTable();

		int count = 0;
		// Order of elements depends on string hashing!!!
		for (TableEntry<String, Integer> pair : examMarks) {
			if (count == 0) {
				assertEquals("Ante", pair.getKey());
			} else if (count == 1) {
				assertEquals("Ivana", pair.getKey());
			} else if (count == 2) {
				assertEquals("Jasna", pair.getKey());
			} else {
				assertEquals("Kristina", pair.getKey());
			}

			count++;
		}

		assertEquals(count, examMarks.size());
	}

}

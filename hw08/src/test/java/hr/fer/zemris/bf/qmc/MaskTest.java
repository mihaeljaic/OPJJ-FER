package hr.fer.zemris.bf.qmc;

import static org.junit.Assert.*;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class MaskTest {

	@Test
	public void testFirstConstructor() {
		Mask mask = new Mask(1, 3, false);
		
		assertEquals(1, mask.countOfOnes());
		assertEquals(0, mask.getValueAt(1));
		assertEquals(1, mask.getValueAt(2));
		assertEquals(3, mask.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetValueAtIllegalPosition() {
		Mask mask = new Mask(1, 3, false);
		
		mask.getValueAt(3);
	}
	
	@Test
	public void testCountOfOnes() {
		Mask mask = new Mask(7, 3, false);
		
		assertEquals(3, mask.countOfOnes());
	}
	
	@Test
	public void testToString() {
		Mask mask = new Mask(7, 3, false);
		Mask mask2 = new Mask(2, 3, true);
		
		mask.setCombined(true);
		
		assertEquals("111 . * [7]", mask.toString());
		assertEquals("010 D   [2]", mask2.toString());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCombineWithDifferentLength() {
		Mask mask = new Mask(7, 3, false);
		Mask mask2 = new Mask(7, 4, false);
		
		mask.combineWith(mask2);
	}
	
	@Test
	public void testCombineWithUncombinable() {
		Set<Integer> set = new TreeSet<>();
		set.add(0);
		set.add(1);
		Mask mask = new Mask(new byte[] {0, 0, 2}, set, false);
		
		Set<Integer> set2 = new TreeSet<>();
		set2.add(1);
		Mask mask2 = new Mask(new byte[] {0, 0, 1}, set2, false);
		
		// Can't combine 2 with 1.
		assertFalse(mask.combineWith(mask2).isPresent());
	}
	
	@Test
	public void testCombineWith() {
		Set<Integer> set = new TreeSet<>();
		set.add(0);
		set.add(1);
		Mask mask = new Mask(new byte[] {0, 0, 2}, set, false);
		
		Set<Integer> set2 = new TreeSet<>();
		set2.add(4);
		set2.add(5);
		Mask mask2 = new Mask(new byte[] {1, 0, 2}, set2, true);
		
		Optional<Mask> combinedMask = mask.combineWith(mask2);
		
		Set<Integer> expectedSet = new TreeSet<>();
		expectedSet.add(0);
		expectedSet.add(1);
		expectedSet.add(4);
		expectedSet.add(5);
		Mask expectedMask = new Mask(new byte[] {2, 0, 2}, expectedSet, false);
		
		assertEquals(expectedMask, combinedMask.get());
	}
}

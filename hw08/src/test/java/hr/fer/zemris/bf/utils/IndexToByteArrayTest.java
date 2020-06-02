package hr.fer.zemris.bf.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import hr.fer.zemris.bf.qmc.Mask;

public class IndexToByteArrayTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeWidth() {
		Util.indexToByteArray(5, -2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNHigherThan32() {
		Util.indexToByteArray(5, 34);
	}
	
	@Test
	public void testPositiveNumbere1() {
		byte[] expected = new byte[] { 1, 1 };

		assertArrayEquals(expected, Util.indexToByteArray(3, 2));
	}

	@Test
	public void testPositiveNumber2() {
		byte[] expected = new byte[] { 0, 0, 1, 1 };

		assertArrayEquals(expected, Util.indexToByteArray(3, 4));
	}

	@Test
	public void testPositiveNumber3() {
		byte[] expected = new byte[] { 0, 0, 0, 0, 1, 1 };

		assertArrayEquals(expected, Util.indexToByteArray(3, 6));
	}

	@Test
	public void testNegativeNumber1() {
		byte[] expected = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 0 };

		assertArrayEquals(expected, Util.indexToByteArray(-2, 32));
	}

	@Test
	public void testNegativeNumber2() {
		byte[] expected = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 };

		assertArrayEquals(expected, Util.indexToByteArray(-2, 16));
	}

	@Test
	public void testShorterWidth() {
		byte[] expected = new byte[] { 0, 0, 1, 1 };

		assertArrayEquals(expected, Util.indexToByteArray(19, 4));
	}
	
}

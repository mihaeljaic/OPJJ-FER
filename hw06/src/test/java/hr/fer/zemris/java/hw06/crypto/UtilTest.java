package hr.fer.zemris.java.hw06.crypto;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

public class UtilTest {

	@Test(expected = IllegalArgumentException.class)
	public void testHexToByteInvalidInput() {
		Util.hextobyte("ghjj");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHexToByteUnevenNumberOfCharacters() {
		Util.hextobyte("12345");
	}

	@Test
	public void testByteToHex() {
		assertEquals("01ae22", Util.bytetohex(new byte[] { 1, -82, 34 }));
	}

	@Test
	public void testHexToByte() {
		assertArrayEquals(new byte[] { 1, -82, 34 }, Util.hextobyte("01aE22"));
	}

	@Test
	public void testByteToHex2() {
		assertEquals("000a0f", Util.bytetohex(new byte[] { 0, 10, 15 }));
	}

	@Test
	public void testHexToByte2() {
		assertArrayEquals(new byte[] { 0, 10, 15 }, Util.hextobyte("000A0f"));
	}
}

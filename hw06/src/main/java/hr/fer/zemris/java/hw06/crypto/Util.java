package hr.fer.zemris.java.hw06.crypto;

/**
 * Class that offers static methods for converting hex to byte and converting
 * byte to hex.
 * 
 * @author Mihael Jaić
 *
 */

public class Util {

	/**
	 * Converts given string into array of bytes. String's length has to be even
	 * number and only symbols representing hexadecimal numbers are accepted. If
	 * string is empty, empty array is returned.
	 * 
	 * @param keyText
	 *            Hex string that will be transformed into byte array.
	 * @return Array of bytes.
	 * @throws IllegalArgumentException
	 *             If string's length is uneven or string contains symbols that
	 *             aren't hexadecimal values.
	 */

	public static byte[] hextobyte(String keyText) throws IllegalArgumentException {
		if (keyText == null || (keyText.length() & 1) != 0 || (!validString(keyText))) {
			throw new IllegalArgumentException("Invalid hex string");
		}

		if (keyText.length() == 0) {
			return new byte[0];
		}

		byte[] hexValues = new byte[keyText.length() / 2];
		String lowerCase = keyText.toLowerCase();

		for (int i = 0; i < lowerCase.length(); i += 2) {
			char c1 = lowerCase.charAt(i);
			char c2 = lowerCase.charAt(i + 1);

			hexValues[i / 2] = convertToByte(c1, c2);
		}

		return hexValues;
	}

	/**
	 * Converts 2 hex chars into byte. Chars are in big-endian notation.
	 * 
	 * @param c1
	 *            First character.
	 * @param c2
	 *            Second character.
	 * @return Byte that stores values of both chars.
	 */

	private static byte convertToByte(char c1, char c2) {
		byte b1 = (byte) (c1 > '9' ? c1 - 'a' + 10 : c1 - '0');
		byte b2 = (byte) (c2 > '9' ? c2 - 'a' + 10 : c2 - '0');

		// b1 is more significant hex. So is shifted to it's place.
		return (byte) ((b1 << 4) | b2);
	}

	/**
	 * Checks if string contains only characters that represent hexadecimal
	 * numbers.
	 * 
	 * @param s
	 *            String.
	 * @return True if string is valid, false otherwise.
	 */

	private static boolean validString(String s) {
		for (Character c : s.toLowerCase().toCharArray()) {
			if (!(Character.isDigit(c) || (c >= 'a' && c <= 'f'))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Converts array of bytes into hexadecimal values stored in string in
	 * big-endian notation.
	 * 
	 * @param byteArray
	 *            Array of bytes.
	 * @return String representation of hexadecimal values from byte array.
	 */

	public static String bytetohex(byte[] byteArray) {
		if (byteArray.length == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder(byteArray.length * 2);

		for (byte b : byteArray) {
			// Shifts most significant hex to right.
			sb.append(toCharacter((b >> 4) & 0xf));
			sb.append(toCharacter(b & 0xf));
		}

		return sb.toString();
	}

	/**
	 * Converts byte to character that represents its hexadecimal value.
	 * 
	 * @param b
	 *            Byte.
	 * @return Character representation of hexadecimal value of byte.
	 */

	private static Character toCharacter(int b) {
		return (char) (b >= 10 ? 'a' + b - 10 : '0' + b);
	}

}

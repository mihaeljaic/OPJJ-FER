package hr.fer.zemris.java.tecaj_13.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilization class for other classes in project.
 * 
 * @author Mihael Jaić
 *
 */

public class Util {
	/** Database length restrictions for some table attributes. */
	public static final int ATTRIBUTE_LENGTH_RESTRICTIONS = 200;
	
	/**
	 * Calculates password hash from given password using SHA-1 algorithm and
	 * returns password in hexadecimal form.
	 * 
	 * @param password
	 *            Password.
	 * @return Digested hexadecimal form of password by using SHA-1 algorithm.
	 */

	public static String getPasswordHash(String password) {
		String hashedPassword = "";

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			hashedPassword = bytetohex(messageDigest.digest(password.getBytes("UTF-8")));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			System.exit(0);
		} catch (UnsupportedEncodingException e) {
			System.out.println("Named charset is not supported.");
			System.exit(0);
		}

		return hashedPassword;
	}

	/**
	 * Converts array of bytes into hexadecimal values stored in string in
	 * big-endian notation.
	 * 
	 * @param byteArray
	 *            Array of bytes.
	 * @return String representation of hexadecimal values from byte array.
	 */

	private static String bytetohex(byte[] byteArray) {
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

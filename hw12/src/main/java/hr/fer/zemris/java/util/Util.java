package hr.fer.zemris.java.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Util class that offers method for reading from file.
 * 
 * @author Mihael Jaić
 *
 */

public class Util {

	/**
	 * Reads data from file and returns data content as string type.
	 * 
	 * @param fileName
	 *            Path to file.
	 * @return Data content in form of string.
	 */

	public static String readFile(String fileName) {
		StringBuilder sb = new StringBuilder();

		try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(fileName))) {
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(String.format("%s%n", line));
			}

		} catch (IOException e) {
			System.out.println("Could not read from file.");
			System.exit(0);
		}
		// Removes last new line character.
		sb.setLength(sb.length() > 0 ? sb.length() - 1 : 0);

		return sb.toString();
	}
}

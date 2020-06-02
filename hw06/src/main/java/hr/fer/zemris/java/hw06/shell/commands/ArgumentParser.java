package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Util class for shell command classes. Offers method for parsing path into
 * string and removing leading spaces from string.
 * 
 * @author Mihael Jaić
 *
 */

public abstract class ArgumentParser {

	/**
	 * Converts string to path. String can contain double quotes. If string
	 * contains double quotes it must start and end with double quotes. Also
	 * inside double quotes escaping of '\' and '"' characters is allowed. If
	 * given path is invalid exception is thrown.
	 * 
	 * @param arguments
	 *            String representing path.
	 * @return Path.
	 * @throws InvalidPathException
	 *             If given path is invalid.
	 */

	static Path stringToPath(String arguments) throws InvalidPathException {
		if (arguments.startsWith("\"")) {
			if (arguments.length() <= 2 || (!arguments.endsWith("\""))) {
				throw new InvalidPathException(arguments, "path has to end with '\"'");
			}

			StringBuilder sb = new StringBuilder(arguments.length());

			boolean isEscaped = false;
			// Removes starting and ending quotes.
			for (Character c : arguments.substring(1, arguments.length() - 1).toCharArray()) {
				if ((!isEscaped) && c == '"') {
					throw new InvalidPathException(arguments, "quotes have to be escaped inside quoted path");
				}

				if (c == '\\' && (!isEscaped)) {
					isEscaped = true;
					// Skips backslash in case escape character is following.
					continue;
				}
				if (isEscaped && (c != '\\' && c != '"')) {
					// Adds backslash because it wasn't escaped.
					sb.append('\\');
				}
				sb.append(c);
				isEscaped = false;
			}

			return Paths.get(sb.toString());
		}

		return Paths.get(arguments);
	}

	/**
	 * Removes leading spaces from string.
	 * 
	 * @param argument
	 *            String.
	 * @return String without leading spaces.
	 */

	static String removeStartingSpaces(String argument) {
		int index = 0;

		for (; index < argument.length() && argument.charAt(index) == ' '; index++);

		return argument.substring(index);
	}
}

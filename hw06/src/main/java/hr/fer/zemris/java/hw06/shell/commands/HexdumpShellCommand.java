package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.crypto.Util;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command gets 1 argument, path to file. Prints file's hexadecimal values of
 * bytes.Print form is in 3 groups from left to right. First is current position
 * of bytes in file. In second group 16 bytes values are printed in hexadecimal
 * form. In third group string value is current bytes is printed.
 * 
 * @author Mihael Jaić
 *
 */

public class HexdumpShellCommand implements ShellCommand {
	/**
	 * Command name.
	 */
	private static String commandName = "hexdump";
	/**
	 * Command description.
	 */
	private static List<String> commandDescription;
	/**
	 * Buffer size.
	 */
	private static final int BUFFER_SIZE = 16;
	/**
	 * If byte has less than this value it will be printed as '.' symbol.
	 */
	private static final int BYTE_MIN_VALUE = 32;
	/**
	 * If byte has value more than this then it will be printed as '.' symbol.
	 */
	private static final int BYTE_MAX_VALUE = 127;
	/**
	 * Length of one row that is displayed on screen.
	 */
	private static final int ROW_SIZE = 77;
	/**
	 * Leading zeroes used to display hex value of current position in file.
	 */
	private static final String LEADING_ZEROES = "00000000";
	/**
	 * 
	 */
	private static final int HEX_DIGITS_LENGTH = 50;
	/**
	 * Position at which comes separator '|' on output.
	 */
	private static final int SEPARATOR_POSITION = 21;

	static {
		commandDescription = new ArrayList<>();
		commandDescription.add("Command gets 1 argument, path to file.");
		commandDescription.add("Prints file's hexadecimal values of bytes.");
		commandDescription.add("Print form is in 3 groups from left to right.");
		commandDescription.add("First is current position of bytes in file.");
		commandDescription.add("In second group 16 bytes values are printed in hexadecimal form.");
		commandDescription.add("In third group string value is current bytes is printed.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null || arguments.trim().length() == 0) {
			env.writeln("hexdump receives 1 argument.");
			return ShellStatus.CONTINUE;
		}

		arguments = ArgumentParser.removeStartingSpaces(arguments);

		if ((!arguments.startsWith("\"")) && arguments.split("\\s+").length != 1) {
			env.writeln("non quoted path can't contain spaces");
			return ShellStatus.CONTINUE;
		}

		Path path = null;
		try {
			path = ArgumentParser.stringToPath(arguments);
		} catch (InvalidPathException ex) {
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}

		if (Files.isDirectory(path) || (!Files.isReadable(path))) {
			env.writeln(String.format("Could not read from %s", arguments));
			return ShellStatus.CONTINUE;
		}

		try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
			byte[] buffer = new byte[BUFFER_SIZE];
			int position = 0;

			while (true) {
				int r = is.read(buffer);
				if (r < 1) {
					break;
				}

				env.writeln(hexdump(buffer, position, r));
				position += BUFFER_SIZE;
			}

		} catch (IOException e) {
			env.writeln(String.format("Could not read from file %s", arguments));
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Creates string for output. First writes position in file in hexadecimal
	 * form. Then comes 16 byte displayed as hex values and after that comes
	 * string representation of those byte values. See
	 * {@link HexdumpShellCommand} for examples.
	 * 
	 * @param buffer
	 *            Buffer.
	 * @param position
	 *            Position in file.
	 * @param bufferLength
	 *            Amount of read bytes.
	 * @return String for output.
	 */

	private String hexdump(byte[] buffer, int position, int bufferLength) {
		StringBuilder sb = new StringBuilder(ROW_SIZE);

		sb.append(String.format("%s: ",
				(LEADING_ZEROES + Integer.toHexString(position)).substring(Integer.toHexString(position).length())));

		String hex = Util.bytetohex(buffer).substring(0, bufferLength * 2).toUpperCase();

		// i -> index in output row
		// pos -> position in hex string
		for (int i = 0, pos = 0; i < HEX_DIGITS_LENGTH;) {
			if (pos >= hex.length() - 1) {
				while (i < HEX_DIGITS_LENGTH) {
					sb.append(i == SEPARATOR_POSITION + 2 || i == HEX_DIGITS_LENGTH - 2 ? '|' : ' ');
					i++;
				}
				break;
			}

			sb.append(String.format("%s", hex.substring(pos, pos + 2)));
			sb.append(i == SEPARATOR_POSITION ? '|' : ' ');
			i += 3;
			pos += 2;
		}

		sb.append(bytesToString(buffer, bufferLength));

		return sb.toString();
	}

	/**
	 * Converts array of bytes to string. If byte has value less than minimal or
	 * more than maximal it is displayed as '.' character.
	 * 
	 * @param bytes
	 *            Array of bytes.
	 * @param bufferLength
	 *            Amount of bytes read after last readingfrom file.
	 * @return String representation of array of bytes.
	 */

	private String bytesToString(byte[] bytes, int bufferLength) {
		StringBuilder sb = new StringBuilder(bytes.length);

		for (int i = 0; i < bufferLength; i++) {
			sb.append((char) (bytes[i] < BYTE_MIN_VALUE || bytes[i] > BYTE_MAX_VALUE ? '.' : bytes[i]));
		}

		return sb.toString();
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(commandDescription);
	}

}

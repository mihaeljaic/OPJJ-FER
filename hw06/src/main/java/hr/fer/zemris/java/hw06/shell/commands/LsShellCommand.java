package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that prints all files in given directory. Accepts one argument that
 * is path to directory. Prints out details about each file. First 4 rows
 * display: if file is directory, is it readable, is it writable and is it
 * executable. If '-' character is printed means that file doesn't have that
 * attribute. After that comes file size, creation date and finally file's name.
 * 
 * @author Mihael Jaić
 *
 */

public class LsShellCommand implements ShellCommand {
	/**
	 * Command name.
	 */
	private static String commandName = "ls";
	/**
	 * Command description.
	 */
	private static List<String> commandDescription;

	static {
		commandDescription = new ArrayList<>();
		commandDescription.add("Command that prints all files in given directory.");
		commandDescription.add("Accepts one argument that is path to directory.");
		commandDescription.add("Prints out details about each file.");
		commandDescription.add("First 4 rows display: if file is directory, is it readable,");
		commandDescription.add("is it writable and is it executable.");
		commandDescription.add("If '-' character is printed means that file doesn't have that attribute.");
		commandDescription.add("After that comes file size, creation data and finally file's name.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null || arguments.trim().length() == 0) {
			env.writeln("Expected argument for ls command.");
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

		if (!Files.isDirectory(path)) {
			env.writeln(String.format("%s isn't directory.", arguments));
			return ShellStatus.CONTINUE;
		}

		try {
			Files.list(path).forEach(p -> {
				try {
					env.writeln(String.format("%s%10d %s %s", getFileSpecifications(p), Files.size(p),
							getCreationDate(p), p.getFileName()));

				} catch (IOException e) {
					env.writeln(String.format("Could not read file specifications %s", p.getFileName()));
				}

			});
		} catch (IOException e) {
			env.writeln("Could not list files.");
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(commandDescription);
	}

	/**
	 * Gets creation date of file.
	 * 
	 * @param path
	 *            File.
	 * @return Creation date of file.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */

	private String getCreationDate(Path path) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);

		BasicFileAttributes attributes = null;

		attributes = faView.readAttributes();

		FileTime fileTime = attributes.creationTime();
		String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

		return formattedDateTime;
	}

	/**
	 * Gets details about file. Is it a directory, is it readable, writable or
	 * executable.
	 * 
	 * @param path
	 *            File.
	 * @return Information about file.
	 */

	private String getFileSpecifications(Path path) {
		StringBuilder sb = new StringBuilder(4);

		sb.append(Files.isDirectory(path) ? 'd' : '-');
		sb.append(Files.isReadable(path) ? 'r' : '-');
		sb.append(Files.isWritable(path) ? 'w' : '-');
		sb.append(Files.isExecutable(path) ? 'x' : '-');

		return sb.toString();
	}

}

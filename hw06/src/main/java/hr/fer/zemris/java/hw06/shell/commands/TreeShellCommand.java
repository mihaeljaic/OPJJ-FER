package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command visits subtree of given directory and prints visited file names.
 * Accepts one argument that is path to root directory.
 * 
 * @author Mihael Jaić
 *
 */

public class TreeShellCommand implements ShellCommand {
	/**
	 * Command name.
	 */
	private static String commandName = "tree";
	/**
	 * Command description.
	 */
	private static List<String> commandDescription;

	static {
		commandDescription = new ArrayList<>();
		commandDescription.add("Command visits subtree of given directory and prints visited file names.");
		commandDescription.add("Accepts one argument that is path to root directory.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null || arguments.trim().length() == 0) {
			env.writeln("Expected argument for tree command.");
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
			env.writeln(String.format("%s isn't direcotory", path.getFileName()));
			return ShellStatus.CONTINUE;
		}

		writeTree(new File(path.toString()), env, 0);

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
	 * Visits subtree of given directory. While visiting prints out file names.
	 * 
	 * @param root
	 *            Root directory.
	 * @param env
	 *            Environment.
	 * @param deviation
	 *            Alignment.
	 */

	private void writeTree(File root, Environment env, int deviation) {
		if (root == null) {
			return;
		}

		env.writeln(String.format("%s%s", generateSpaces(deviation), root.getName()));

		deviation += 2;

		File[] listedFiles = root.listFiles();
		if (listedFiles == null) {
			return;
		}

		for (File f : listedFiles) {
			if (f.isDirectory()) {
				writeTree(f, env, deviation);
			}
			env.writeln(String.format("%s%s", generateSpaces(deviation), f.getName()));
		}
	}

	/**
	 * Generates string that contains specified amount of spaces.
	 * 
	 * @param amount
	 *            Amount of spaces.
	 * @return String that contains specified amount of spaces.
	 */

	private String generateSpaces(int amount) {
		StringBuilder sb = new StringBuilder();

		while (amount-- > 0) {
			sb.append(' ');
		}

		return sb.toString();
	}

}

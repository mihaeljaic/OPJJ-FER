package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command creates new directory and all non existent parent directories. Accepts one argument that is
 * path. If directory already exists nothing is done.
 * 
 * @author Mihael Jaić
 *
 */

public class MkdirShellCommand implements ShellCommand {
	/**
	 * Command name.
	 */
	private static String commandName = "mkdir";
	/**
	 * Command description.
	 */
	private static List<String> commandDescription;

	static {
		commandDescription = new ArrayList<>();
		commandDescription.add("Command creates new directory and all non existent parent directories.");
		commandDescription.add("Accepts one argument that is path.");
		commandDescription.add("If directory already exists nothing is done.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null || arguments.trim().length() == 0) {
			env.writeln("mkdir command receives 1 argument");
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

		if (Files.isDirectory(path)) {
			env.writeln(String.format("%s directory already exists", arguments));
			return ShellStatus.CONTINUE;
		}

		try {
			Files.createDirectories(Paths.get(arguments));
		} catch (IOException e) {
			env.writeln("Could not create directory");
			return ShellStatus.CONTINUE;
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}

}

package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
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
 * Command that accepts 1 or 2 arguments. First argument has to be path to file
 * that is readable. Second argument is charset. Command prints out content of
 * file using given charset. If charset isn't provided then default charset from
 * user's platform is taken.
 * 
 * @author Mihael Jaić
 *
 */

public class CatShellCommand implements ShellCommand {
	/**
	 * Command name.
	 */
	private static String commandName = "cat";
	/**
	 * Command description.
	 */
	private static List<String> commandDescription;

	static {
		commandDescription = new ArrayList<>();
		commandDescription.add("Command accepts 1 or 2 arguments");
		commandDescription.add("First argument is path to file that is readable.");
		commandDescription.add("Second argument is charset.");
		commandDescription.add("Command prints out content of file using given charset.");
		commandDescription.add("If charset isn't provided default charset is taken from user's platform.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null || arguments.trim().length() == 0) {
			env.writeln("cat command receives 1 or 2 arguments");
			return ShellStatus.CONTINUE;
		}

		arguments = ArgumentParser.removeStartingSpaces(arguments);

		Path path = null;
		Charset charset = Charset.defaultCharset();
		String pathPart = "";
		String charsetPart = "";

		if (!arguments.startsWith("\"")) {
			String[] args = arguments.split("\\s+");
			if (args.length > 2) {
				env.writeln("Too many arguments in cat command");
				return ShellStatus.CONTINUE;
			}

			pathPart = args[0];
			if (args.length == 2)
				charsetPart = args[1];

		} else {
			int quoteIndex = arguments.lastIndexOf('"');

			if (quoteIndex == 0) {
				env.writeln(String.format("%s is not terminated with quotes", arguments));
				return ShellStatus.CONTINUE;
			}

			pathPart = arguments.substring(0, quoteIndex + 1);
			charsetPart = arguments.substring(quoteIndex + 1);

			if (!charsetPart.startsWith(" ")) {
				env.writeln("path and charset need to be separated with space");
				return ShellStatus.CONTINUE;
			}
		}

		charsetPart = ArgumentParser.removeStartingSpaces(charsetPart);

		if (charsetPart.length() > 0) {
			try {
				if (!Charset.isSupported(charsetPart)) {
					env.writeln(String.format("Given charset %s isn't available on current platform.", charsetPart));
					return ShellStatus.CONTINUE;
				}

				charset = Charset.forName(charsetPart);
			} catch (IllegalCharsetNameException ex) {
				env.writeln("illegal charset name in cat command");
				return ShellStatus.CONTINUE;
			}
		}

		try {
			path = ArgumentParser.stringToPath(pathPart);
		} catch (InvalidPathException ex) {
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}

		if (!Files.exists(path)) {
			env.writeln(String.format("file %s doesn't exist.", pathPart));
			return ShellStatus.CONTINUE;
		}

		try (BufferedReader br = Files.newBufferedReader(path, charset)) {
			String line = null;
			while ((line = br.readLine()) != null) {
				env.writeln(line);
			}

		} catch (IOException e) {
			env.writeln(String.format("could not read from file %s.", path));
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
		return Collections.unmodifiableList(commandDescription);
	}

}

package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that accepts 2 arguments, source and destination path. Copies
 * source file into destination. Source has to be file. If destination file
 * already exists user can overwrite it. If destination file is directory then
 * new file will be placed into that directory. It will be named same as source
 * file.
 * 
 * @author Mihael Jaić
 *
 */

public class CopyShellCommand implements ShellCommand {
	/**
	 * Command name.
	 */
	private static String commandName = "copy";
	/**
	 * Command desricption.
	 */
	private static List<String> commandDescription;
	/**
	 * Buffer size;
	 */
	private static final int BUFFER_SIZE = 4096;

	static {
		commandDescription = new ArrayList<>();
		commandDescription.add("Command that accepts 2 arguments, source and destination path.");
		commandDescription.add("Copies source file into destination.");
		commandDescription.add("Source has to be file.");
		commandDescription.add("If destination file already exists user can overwrite it.");
		commandDescription.add("If destination file is directory then new file will be placed into that directory.");
		commandDescription.add("It will be named same as source file.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null || arguments.trim().length() == 0) {
			env.writeln("copy command receives 2 arguments");
			return ShellStatus.CONTINUE;
		}

		arguments = ArgumentParser.removeStartingSpaces(arguments);
		String path1 = "";
		String path2 = "";

		if (!arguments.startsWith("\"")) {
			int firstSpace = arguments.indexOf(' ');

			if (firstSpace == -1 || firstSpace == arguments.length()) {
				env.writeln("copy command receives 2 arguments");
				return ShellStatus.CONTINUE;
			}

			path1 = arguments.substring(0, firstSpace);
			path2 = arguments.substring(firstSpace + 1);

			path2 = ArgumentParser.removeStartingSpaces(path2);

			if (path2.length() == 0) {
				env.writeln("copy command receives 2 arguments");
				return ShellStatus.CONTINUE;
			}

			if (path2.startsWith("\"") && path2.lastIndexOf('"') != path2.length() - 1) {
				env.writeln("second path quote isn't terminated");
				return ShellStatus.CONTINUE;
			}

			if ((!path2.startsWith("\"")) && path2.split("\\s+").length > 1) {
				env.writeln("copy command receives 2 arguments");
				return ShellStatus.CONTINUE;
			}

		} else {
			int quoteTerminate1 = findQuoteTerminate(arguments);
			if (quoteTerminate1 == -1) {
				env.writeln(String.format("invalid arguments %s", arguments));
				return ShellStatus.CONTINUE;
			}

			path1 = arguments.substring(0, quoteTerminate1 + 1);
			path2 = arguments.substring(quoteTerminate1 + 1);

			if (path2.trim().length() == 0) {
				env.writeln("copy command receives 2 arguments");
				return ShellStatus.CONTINUE;
			}

			if (!path2.startsWith(" ")) {
				env.writeln("paths have to be separated");
				return ShellStatus.CONTINUE;
			}

			path2 = ArgumentParser.removeStartingSpaces(path2);

			if (path2.startsWith("\"")) {
				int quoteTerminate2 = path2.lastIndexOf('"');
				if (quoteTerminate2 == 0 || quoteTerminate2 != path2.length() - 1) {
					env.writeln(String.format("path %s must end with with quotes", path2));
					return ShellStatus.CONTINUE;
				}

			} else {
				if (path2.split("\\s+").length != 1) {
					env.writeln("too many arguments in copy command");
					return ShellStatus.CONTINUE;
				}
			}
		}

		Path source = null;
		try {
			source = ArgumentParser.stringToPath(path1);
		} catch (InvalidPathException ex) {
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}

		Path destination = null;
		try {
			destination = ArgumentParser.stringToPath(path2);
		} catch (InvalidPathException ex) {
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}

		if (!Files.exists(source)) {
			env.writeln(String.format("file doesn't exist", path1));
			return ShellStatus.CONTINUE;
		}

		if (Files.isDirectory(source)) {
			env.writeln("can't copy directory. only files are allowed");
			return ShellStatus.CONTINUE;
		}

		if (Files.isDirectory(destination) || destination.getFileName().toString().indexOf('.') == -1) {
			destination = Paths.get(destination.toString() + '/' + source.getFileName());
		}

		if (Files.exists(destination)) {
			env.writeln("file already exists overwrite?(Type Y for yes or anything else for no): ");
			if (!env.readLine().equals("Y")) {
				env.writeln("did not copy file");
				return ShellStatus.CONTINUE;
			}
		}

		File f = new File(destination.toAbsolutePath().toString());

		if (f.getParentFile() != null) {
			f.getParentFile().mkdirs();
		}

		try {
			f.createNewFile();
		} catch (IOException e1) {
			env.writeln(String.format("could not create file %s", destination));
			return ShellStatus.CONTINUE;
		}

		try (InputStream is = Files.newInputStream(source, StandardOpenOption.READ);
				OutputStream os = Files.newOutputStream(destination, StandardOpenOption.WRITE)) {
			byte[] buffer = new byte[BUFFER_SIZE];

			while (true) {
				int r = is.read(buffer);
				if (r < 1) {
					break;
				}

				os.write(buffer);
			}

		} catch (IOException e) {
			env.writeln(String.format("could not read from file %s", source));
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
	 * Finds index of quote that isn't escaped in given string. If there is no
	 * such character -1 is returned.
	 * 
	 * @param arguments
	 *            Input string.
	 * @return Index of quote that terminates path.
	 */

	private int findQuoteTerminate(String arguments) {
		for (int i = 1; i < arguments.length(); i++) {
			if (arguments.charAt(i) == '"' && arguments.charAt(i - 1) != '\\') {
				return i;
			}
		}

		return -1;
	}

}

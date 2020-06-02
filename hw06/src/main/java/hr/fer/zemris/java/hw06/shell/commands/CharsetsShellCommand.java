package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command lists all charsets available on user's platform.
 * 
 * @author Mihael Jaić
 *
 */

public class CharsetsShellCommand implements ShellCommand {
	/**
	 * Command name.
	 */
	private static String commandName = "charsets";
	/**
	 * Command description.
	 */
	private static List<String> commandDescription;

	static {
		commandDescription = new ArrayList<>();
		commandDescription.add("Prints out names of all supported charsets for this Java platform.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments != null && arguments.trim().length() > 0) {
			env.writeln("Command charsets doesn't expect arguments.");
			return ShellStatus.CONTINUE;
		}

		SortedMap<String, Charset> availableCharsets = Charset.availableCharsets();

		env.writeln("available charsets:");

		for (String s : availableCharsets.keySet()) {
			env.writeln(s);
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

package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Shell command that prints description of given command. If command isn't
 * given it prints out description of all commands.
 * 
 * @author Mihael Jaić
 *
 */

public class HelpShellCommand implements ShellCommand {
	/**
	 * Command name.
	 */
	private static String commandName = "help";
	/**
	 * Command description.
	 */
	private static List<String> commandDescription;
	/**
	 * Spaces used for alignment while printing command descriptions.
	 */
	private static final String SPACES_AFTER_COMMANDS = "          ";

	static {
		commandDescription = new ArrayList<>();
		commandDescription.add("Command that prints out description of given command name.");
		commandDescription.add("If no command name is given prints out description of all commands.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null || arguments.trim().length() == 0) {
			for (ShellCommand command : env.commands().values()) {
				env.write(getCommandDescription(command));
			}
			return ShellStatus.CONTINUE;
		}

		arguments = ArgumentParser.removeStartingSpaces(arguments);

		ShellCommand command = env.commands().get(arguments);

		if (command == null) {
			env.writeln(String.format("%s command doesn't exist", arguments));
			return ShellStatus.CONTINUE;
		}

		env.write(getCommandDescription(command));

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
	 * Collects all description about command and puts it into string so it can
	 * be printed.
	 * 
	 * @param command
	 *            Command.
	 * @return String containing description of command.
	 */

	private String getCommandDescription(ShellCommand command) {
		StringBuilder sb = new StringBuilder(command.getCommandName());

		int i = 0;
		for (String description : command.getCommandDescription()) {
			// Fills spaces before command descriptions.
			sb.append(String.format("%s%n",
					i++ == 0 ? (SPACES_AFTER_COMMANDS + description).substring(command.getCommandName().length())
							: (SPACES_AFTER_COMMANDS + description)));
		}

		return sb.toString();
	}

}

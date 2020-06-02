package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Class that represents shell command.
 * 
 * @author Mihael Jaić
 *
 */

public interface ShellCommand {
	/**
	 * Executes this command.
	 * 
	 * @param env
	 *            Environment.
	 * @param arguments
	 *            Arguments for this command given by user.
	 * @return Status of command.
	 */

	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Gets command name.
	 * 
	 * @return Command name;
	 */

	String getCommandName();

	/**
	 * Gets command description.
	 * 
	 * @return Command description.
	 */

	List<String> getCommandDescription();

}

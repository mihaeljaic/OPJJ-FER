package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that terminates shell.
 * 
 * @author Mihael Jaić
 *
 */

public class ExitShellCommand implements ShellCommand {
	/**
	 * Command name.
	 */
	private static String commandName = "exit";
	/**
	 * Command description.
	 */
	private static List<String> commandDescription;
	
	static {
		commandDescription = new ArrayList<>();
		commandDescription.add("Command that terminates shell.");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments != null && arguments.trim().length() > 0) {
			env.writeln("Command exit doesn't expect arguments");
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.TERMINATE;
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

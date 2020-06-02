package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command accepts 1 or 2 arguments. First argument has to be "PROMPT",
 * "MULTILINE" or "MORELINES". "PROMPT" is symbol for prompt. "MULTILINE" is
 * symbol that appears if 1 command is in more than 1 line. "MORELINES" symbol
 * moves command into new line. If second argument isn't given prints out
 * current symbol. If second argument is given changes current symbol to second
 * argument. Second argument has to be single character.
 * 
 * @author Mihael Jaić
 *
 */

public class SymbolShellCommand implements ShellCommand {
	/**
	 * Command name.
	 */
	private static String commandName = "symbol";
	/**
	 * Command description.
	 */
	private static List<String> commandDescription;

	static {
		commandDescription = new ArrayList<>();
		commandDescription.add("Command accepts 1 or 2 arguments.");
		commandDescription.add("First argument has to be \"PROMPT\", \"MULTILINE\" or \"MORELINES\".");
		commandDescription.add("\"PROMPT\" is symbol for command prompt.");
		commandDescription.add("\"MULTILINE\" is symbol that appears if 1 command is in more than 1 line.");
		commandDescription.add("\"MORELINES\" symbol moves command into new line.");
		commandDescription.add("If second argument isn't given prints out current symbol.");
		commandDescription.add("If second argument is given changes current symbol to second argument.");
		commandDescription.add("Second argument has to be single character.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null || arguments.trim().length() == 0) {
			env.writeln("Symbol command expects 1 or 2 arguments.");
			return ShellStatus.CONTINUE;
		}

		arguments = ArgumentParser.removeStartingSpaces(arguments);

		String[] args = arguments.split("\\s+");

		if (args.length > 2) {
			env.writeln("Symbol command expects 1 or 2 arguments.");
			return ShellStatus.CONTINUE;
		}

		if (!validArgument(args[0].toUpperCase())) {
			env.writeln(String.format("Unknown argument %s", args[0]));
			return ShellStatus.CONTINUE;
		}

		if (args.length == 2 && args[1].length() != 1) {
			env.writeln(String.format("Single character is expected after %s", args[0]));
			return ShellStatus.CONTINUE;
		}

		args[0] = args[0].toUpperCase();

		if (args.length == 1) {
			env.writeln(String.format("Symbol for %s is '%c'", args[0], getSymbol(env, args[0])));
			return ShellStatus.CONTINUE;
		}

		env.writeln(String.format("Symbol for %s changed from '%c' to '%c'", args[0], getSymbol(env, args[0]),
				args[1].charAt(0)));
		setSymbol(env, args[0], args[1].charAt(0));

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
	 * Checks if given argument is valid.
	 * 
	 * @param arg
	 *            Argument.
	 * @return True if argument is valid, false otherwise.
	 */

	private boolean validArgument(String arg) {
		return arg.equals("PROMPT") || arg.equals("MORELINES") || arg.equals("MULTILINE");
	}

	/**
	 * Gets symbol requested by user.
	 * 
	 * @param env
	 *            Environment.
	 * @param arg
	 *            Argument.
	 * @return Symbol requested by user.
	 */

	private Character getSymbol(Environment env, String arg) {
		switch (arg) {
		case "PROMPT":
			return env.getPromptSymbol();
		case "MORELINES":
			return env.getMorelinesSymbol();
		case "MULTILINE":
			return env.getMultilineSymbol();
		default:
			return null;
		}
	}

	/**
	 * Sets symbol requested by user.
	 * 
	 * @param env
	 *            Environment.
	 * @param arg
	 *            Argument.
	 * @param symbol
	 *            Symbol.
	 */

	private void setSymbol(Environment env, String arg, char symbol) {
		switch (arg) {
		case "PROMPT":
			env.setPromptSymbol(symbol);
			return;
		case "MORELINES":
			env.setMorelinesSymbol(symbol);
			return;
		case "MULTILINE":
			env.setMultilineSymbol(symbol);
			return;
		}
	}
}

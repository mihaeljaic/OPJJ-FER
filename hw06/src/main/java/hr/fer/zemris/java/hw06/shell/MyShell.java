package hr.fer.zemris.java.hw06.shell;

/**
 * Shell program that offers variety of methods with files. Such as charsets,
 * cat, ls,tree, copy, mkdir and hexdump. There is also symbol command for
 * changing symbols in command prompt. Program doesn't use any command line
 * arguments. Type help for description of all commands supported by shell. Path
 * can be specified either by absolute or relative path. Path can be given with
 * or without double quotes. If path is given without quotes there no spaces are
 * allowed while in double quotes spaces are allowed. Commands can span into
 * mulitple rows using morelines symbol. Morelines symbol has to be used as last
 * and is removed from command. Default morelines symbol is '\'.Program
 * terminates with exit command.
 * 
 * @author Mihael Jaić
 *
 */

public class MyShell {

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		Environment env = new EnvironmentImplementation();

		System.out.println("Welcome to MyShell v 1.0");

		while (true) {
			try {
				env.write(env.getPromptSymbol().toString() + ' ');

				StringBuilder reader = new StringBuilder();
				reader.append(env.readLine());

				while (isMultilineCommand(reader, env)) {
					env.write(env.getMultilineSymbol().toString() + ' ');
					// deletes multiline symbol
					reader.setLength(reader.length() - 1);
					reader.append(env.readLine());
				}

				int firstSpace = reader.toString().indexOf(' ');

				String commandName = reader.toString();
				String arguments = null;
				if (firstSpace != -1) {
					commandName = reader.toString().substring(0, firstSpace);
					arguments = reader.toString().substring(firstSpace + 1);
				}

				if (!env.commands().containsKey(commandName)) {
					env.writeln(String.format("%s is not valid command", commandName));
					continue;
				}

				ShellCommand command = env.commands().get(commandName);
				ShellStatus status = command.executeCommand(env, arguments);

				if (status == ShellStatus.TERMINATE) {
					break;
				}
			} catch (ShellIOException ex) {
				System.out.printf("%s%nProgram will now terminate%n", ex.getMessage());
				System.exit(1);
			}
		}

	}

	/**
	 * Checks if given command ends with multiline symbol.
	 * 
	 * @param sb
	 *            Command.
	 * @param env
	 *            Environment.
	 * @return True if given command ends with multiline symbol, false
	 *         otherwise.
	 */

	private static boolean isMultilineCommand(StringBuilder sb, Environment env) {
		return sb.toString().endsWith(env.getMorelinesSymbol().toString());
	}
}

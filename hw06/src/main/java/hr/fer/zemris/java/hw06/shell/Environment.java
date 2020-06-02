package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

/**
 * Interface that serves as communication between user and shell. Offers methods
 * of writing and reading from standard input/output as well as getting all
 * commands supported by shell.
 * 
 * @author Mihael Jaić
 *
 */

public interface Environment {
	/**
	 * Reads line from standard input.
	 * 
	 * @return String that was read from standard input.
	 * @throws ShellIOException
	 *             If couldn't read from standard input.
	 */
	String readLine() throws ShellIOException;

	/**
	 * Writes given string on standard output
	 * 
	 * @param text
	 *            Text that will be written on standard output.
	 * @throws ShellIOException
	 *             If something went wrong while writing on standard output.
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Writes given string on standard output and goes to new line.
	 * 
	 * @param text
	 *            Text that will be written on standard output.
	 * @throws ShellIOException
	 *             If something went wrong while writing on standard output.
	 */

	void writeln(String text) throws ShellIOException;

	/**
	 * Gets unmodifiable map of shell commands.
	 * 
	 * @return Unmodofiable map of shell commands.
	 */

	SortedMap<String, ShellCommand> commands();

	/**
	 * Gets multiline symbol.
	 * 
	 * @return Multiline symbol.
	 */

	Character getMultilineSymbol();

	/**
	 * Sets multiline symbol.
	 * 
	 * @param symbol
	 *            Multiline symbol.
	 */

	void setMultilineSymbol(Character symbol);

	/**
	 * Gets prompt symbol.
	 * 
	 * @return Prompt symbol.
	 */

	Character getPromptSymbol();

	/**
	 * Sets prompt symbol.
	 * 
	 * @param symbol
	 *            Prompt symbol.
	 */

	void setPromptSymbol(Character symbol);

	/**
	 * Gets morelines symbol.
	 * 
	 * @return Symbol for more lines.
	 */

	Character getMorelinesSymbol();

	/**
	 * Sets morelines symbol.
	 * 
	 * @param symbol
	 *            Morelines symbol.
	 */

	void setMorelinesSymbol(Character symbol);
}

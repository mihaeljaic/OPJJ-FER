package hr.fer.zemris.java.hw06.shell;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;

/**
 * Implementation of {@link Environment}. Contains all shell commands.
 * 
 * @author Mihael Jaić
 *
 */

public class EnvironmentImplementation implements Environment {
	/**
	 * Scanner for reading from standard input.
	 */
	private Scanner sc;
	/**
	 * Map that contains
	 */
	private SortedMap<String, ShellCommand> commands;
	/**
	 * Symbol for multiline commands.
	 */
	private char multilineSymbol;
	/**
	 * Symbol for prompt.
	 */
	private char promptSymbol;
	/**
	 * Symbol for more lines.
	 */
	private char morelinesSymbol;
	
	/**
	 * Initializes environment.
	 */
	
	public EnvironmentImplementation() {
		sc = new Scanner(System.in);
		
		multilineSymbol = '|';
		promptSymbol = '>';
		morelinesSymbol = '\\';
		
		commands = new TreeMap<>();
		
		commands.put("exit", new ExitShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("copy", new CopyShellCommand());
	}
	
	@Override
	public String readLine() throws ShellIOException {
		try {
			return sc.nextLine();
		} catch (NoSuchElementException | IllegalStateException ex) {
			throw new ShellIOException("Failed to read line");
		}
	}

	@Override
	public void write(String text) throws ShellIOException {
		System.out.print(text);
	}

	@Override
	public void writeln(String text) throws ShellIOException {
		System.out.println(text);
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return Collections.unmodifiableSortedMap(commands);
	}

	@Override
	public Character getMultilineSymbol() {
		return multilineSymbol;
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		multilineSymbol = symbol;
	}

	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		promptSymbol = symbol;
	}

	@Override
	public Character getMorelinesSymbol() {
		return morelinesSymbol;
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		morelinesSymbol = symbol;
	}

}

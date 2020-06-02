package hr.fer.zemris.java.custom.collections;

/**
 * Exception that informs user that it was tried to be read from empty
 * stack.
 * 
 * @author Mihael Jaić
 *
 */

public class EmptyStackException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs new runtime exception with message
	 * "Stack is empty".
	 */
	public EmptyStackException() {
		super("Stack is empty.");
	}

}

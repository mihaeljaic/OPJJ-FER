package hr.fer.zemris.java.hw05.observer1;

/**
 * Interface that represents observer of integer storage. Implementations of
 * this interface can register to integer storage. When value in storage changes
 * they are called to do their job.
 * 
 * @author Mihael Jaić
 *
 */

public interface IntegerStorageObserver {
	/**
	 * Informs observer that value of storage has changed. After what observer
	 * does his job.
	 * 
	 * @param istorage
	 *            Integer storage.
	 */
	public void valueChanged(IntegerStorage istorage);
}

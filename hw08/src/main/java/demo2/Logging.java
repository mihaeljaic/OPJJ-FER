package demo2;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for testing Logger class.
 */

public class Logging {
	/**
	 * Log.
	 */
	private static final Logger LOG = Logger.getLogger("demo2");

	/**
	 * Method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		Level[] levels = new Level[] { Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE, Level.FINER,
				Level.FINEST };
		for (Level l : levels) {
			LOG.log(l, "This is message of " + l + " level.");
		}
	}
}
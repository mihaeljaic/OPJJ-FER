package hr.fer.zemris.java.hw16.jvdraw.model;

/**
 * Drawing model listener that listens to changes on drawing model. Reacts
 * according to change on drawing model.
 * 
 * @author Mihael Jaić
 *
 */

public interface DrawingModelListener {
	/**
	 * When geometrical objects are added acts depending on implementation.
	 * 
	 * @param source
	 *            Drawing model.
	 * @param index0
	 *            Start index.
	 * @param index1
	 *            End index(inclusice).
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * When geometrical objects are removed acts depending on implementation.
	 * 
	 * @param source
	 *            Drawing model.
	 * @param index0
	 *            Start index.
	 * @param index1
	 *            End index(inclusive).
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * When geometrical objects are changed acts depending on implementation.
	 * 
	 * @param source
	 *            Drawing model.
	 * @param index0
	 *            Start index.
	 * @param index1
	 *            End index(inclusive).
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}

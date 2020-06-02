package hr.fer.zemris.java.hw16.jvdraw.model;

import hr.fer.zemris.java.hw16.jvdraw.geometrical.GeometricalObject;

/**
 * Drawing model that acts as subject in observer pattern. It holds information
 * for all geometrical objects. When some of objects is added, removed or it
 * changes all listeners are informed about the change.
 * 
 * @author Mihael Jaić
 *
 */

public interface DrawingModel {
	/**
	 * Gets number of geometrical objects.
	 * 
	 * @return Number of geometrical objects.
	 */
	public int getSize();

	/**
	 * Gets geometrical object at specified index.
	 * 
	 * @param index
	 *            Index of geometrical object.
	 * @return Geometrical object.
	 */
	public GeometricalObject getObject(int index);

	/**
	 * Adds geometrical object to model. And informs all listeners about the
	 * change.
	 * 
	 * @param object
	 *            Geometrical object.
	 */
	public void add(GeometricalObject object);

	/**
	 * Removes geometrical object from model. And informs all listeners about
	 * change.
	 * 
	 * @param object
	 *            Geometrical object.
	 */
	public void remove(GeometricalObject object);

	/**
	 * Informs all listeners about the change on specific object.
	 * 
	 * @param object
	 *            Geometrical object.
	 */
	public void change(GeometricalObject object);

	/**
	 * Removes all geometrical objects from model.
	 */
	public void clear();

	/**
	 * Adds drawing model listener.
	 * 
	 * @param l
	 *            Drawing model listener.
	 */
	public void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Removes drawing model listener.
	 * 
	 * @param l
	 *            Drawing model listener.
	 */

	public void removeDrawingModelListener(DrawingModelListener l);
}

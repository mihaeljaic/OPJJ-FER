package hr.fer.zemris.java.hw16.jvdraw.model;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.jvdraw.geometrical.GeometricalObject;

/**
 * Drawing model listener that listens to {@link DrawingModel}. When drawing
 * model changes it changes jlist accordingly. Displays list of all geometrical
 * objects drawn on canvas.
 * 
 * @author Mihael Jaić
 *
 */

public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {
	/** Default serial version. */
	private static final long serialVersionUID = 1L;
	/** Drawing model. */
	private DrawingModel drawingModel;

	/**
	 * Constructor that sets drawing model.
	 * 
	 * @param drawingModel
	 *            Drawing model.
	 */

	public void setDrawingModel(DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
	}

	/**
	 * Gets drawing model.
	 * 
	 * @return Drawing model.
	 */

	public DrawingModel getDrawingModel() {
		return drawingModel;
	}

	@Override
	public int getSize() {
		return drawingModel.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return drawingModel.getObject(index);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		fireIntervalAdded(source, index0, index1);
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		fireIntervalRemoved(source, index0, index1);
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		fireContentsChanged(source, index0, index1);
	}

}

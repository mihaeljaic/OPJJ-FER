package hr.fer.zemris.java.hw16.jvdraw.model;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.geometrical.GeometricalObject;

/**
 * Drawing model that stores information about geometrical objects on canvas and
 * informs all listeners when they change.
 * 
 * @author Mihael Jaić
 *
 */

public class DrawingModelImpl implements DrawingModel {
	/** Listeners. */
	private List<DrawingModelListener> listeners = new ArrayList<>();
	/** Geometrical objects. */
	private List<GeometricalObject> objects = new ArrayList<>();

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		if (index < 0 || index >= objects.size()) {
			throw new IllegalArgumentException(String.format("Index has to be in range [0, %d]", objects.size() - 1));
		}

		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);
		for (DrawingModelListener listener : listeners) {
			listener.objectsAdded(this, objects.size() - 1, objects.size() - 1);
		}
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void remove(GeometricalObject object) {
		int index = objects.indexOf(object);
		if (index == -1) {
			return;
		}

		for (DrawingModelListener listener : listeners) {
			listener.objectsRemoved(this, index, index);
		}

		objects.remove(index);
	}

	@Override
	public void change(GeometricalObject object) {
		int index = objects.indexOf(object);
		if (index == -1) {
			return;
		}

		for (DrawingModelListener listener : listeners) {
			listener.objectsChanged(this, index, index);
		}
	}

	@Override
	public void clear() {
		if (objects.isEmpty()) {
			return;
		}

		for (DrawingModelListener listener : listeners) {
			listener.objectsRemoved(this, 0, objects.size() - 1);
		}

		objects.clear();
	}

}

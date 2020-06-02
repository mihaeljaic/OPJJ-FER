package hr.fer.zemris.java.gui.prim;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class PrimListModelTest {

	@Test
	public void testSize() {
		PrimListModel list = new PrimListModel();
		
		assertEquals(1, list.getSize());
		
		list.next();
		list.next();
		
		assertEquals(3, list.getSize());
	}
	
	@Test
	public void testNext() {
		PrimListModel list = new PrimListModel();
		
		for (int i = 0; i < 10; i++) {
			list.next();
		}
		
		assertEquals(1, (int) list.getElementAt(0));
		assertEquals(2, (int) list.getElementAt(1));
		assertEquals(11, (int) list.getElementAt(5));
		assertEquals(23, (int) list.getElementAt(9));
		assertEquals(29, (int) list.getElementAt(10));
	}

}

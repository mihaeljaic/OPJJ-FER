package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class ObjectMultistackTest {

	@Test(expected = IllegalArgumentException.class)
	public void testPushNullKey() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.push(null, new ValueWrapper(3));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPushNullWrapper() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.push("Slavica", null);
	}
	
	@Test(expected = ObjectMultistackException.class)
	public void testPopEmptyStack() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.pop("Wayne Rooney");
	}
	
	@Test(expected = ObjectMultistackException.class)
	public void testPopNull() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.pop(null);
	}
	
	@Test(expected = ObjectMultistackException.class)
	public void testPopAfterPushingAndPopping() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.push("Key", new ValueWrapper(5));
		multistack.pop("Key");
		
		// Should throw.
		multistack.pop("Key");
	}
	
	@Test(expected = ObjectMultistackException.class)
	public void testPeekEmptyStack() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.peek("asd");
	}
	
	@Test(expected = ObjectMultistackException.class)
	public void testPeekNull() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.peek(null);
	}
	
	@Test(expected = ObjectMultistackException.class)
	public void testPeekEmptyAfterPoping() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.push("Key", new ValueWrapper(5));
		multistack.pop("Key");
		
		multistack.peek("Key");
	}
	
	@Test
	public void testIsEmpty() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		assertTrue(multistack.isEmpty("Key"));
		
		multistack.push("Key", new ValueWrapper(5));
		
		assertFalse(multistack.isEmpty("Key"));	
		assertTrue(multistack.isEmpty("Keys"));
		
		multistack.pop("Key");
		
		assertTrue(multistack.isEmpty("Key"));
	}
	
	@Test
	public void testPushSomeValues() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.push("Key", new ValueWrapper(1));
		multistack.push("Key", new ValueWrapper(2));
		multistack.push("Price", new ValueWrapper(3.50));
		multistack.push("Price", new ValueWrapper("27.5"));
		multistack.push("Key", new ValueWrapper(-20));
		
		assertEquals(-20, multistack.peek("Key").getValue());
		assertEquals(-20, multistack.pop("Key").getValue());
		assertEquals(2, multistack.peek("Key").getValue());
		assertEquals(2, multistack.pop("Key").getValue());
		assertEquals(1, multistack.peek("Key").getValue());
		assertEquals(1, multistack.pop("Key").getValue());
		
		assertTrue(multistack.isEmpty("Key"));
		
		assertEquals("27.5", multistack.peek("Price").getValue());
		assertEquals("27.5", multistack.pop("Price").getValue());
		
		multistack.push("Price", new ValueWrapper("3e+2"));
		
		assertEquals("3e+2", multistack.peek("Price").getValue());
		assertEquals("3e+2", multistack.pop("Price").getValue());
		assertEquals(3.50, multistack.peek("Price").getValue());
		assertEquals(3.50, multistack.pop("Price").getValue());
		
		assertTrue(multistack.isEmpty("Price"));
	}
}

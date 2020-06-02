package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ObjectStackTest {
	
	private ObjectStack stack1;
	private ObjectStack stack2;
	
	@Before
	public void init() {
		stack1 = new ObjectStack();
		
		stack1.push(new Integer(20));
		stack1.push("New York");
		stack1.push("San Francisco");
		
		stack1.pop();
		
		stack2 = new ObjectStack();
		stack2.push("Asd");
		
		stack2.clear();
	}
	
	@Test
	public void isEmptyStack2() {
		assertEquals(true, stack2.isEmpty());
	}
	
	@Test
	public void isEmptyStack1() {
		assertEquals(false, stack1.isEmpty());
	}
	
	@Test
	public void sizeOfStack1() {
		assertEquals(2, stack1.size());
	}
	
	@Test
	public void sizeOfStack2() {
		assertEquals(0, stack2.size());
	}
	
	@Test(expected=EmptyStackException.class)
	public void readingFromEmptyStack() {
		stack2.pop();
	}
	
	@Test(expected=EmptyStackException.class)
	public void peekingEmptyStack() {
		stack2.peek();
	}
	
	@Test
	public void elementAtTopOfStack1() {
		assertEquals("New York", stack1.peek());
	}

}

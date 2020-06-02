package hr.fer.zemris.java.hw01;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Mihael Jaić
 *
 */
public class UniqueNumbersTest extends UniqueNumbers {
	
	/**
	 * 
	 */
	@Test
	public void testAddNodeExerciseExampleNode1(){
		TreeNode glava = null;
		glava = addNode(glava, 42);
		glava = addNode(glava, 76);
		glava = addNode(glava, 21);
		glava = addNode(glava, 76);
		glava = addNode(glava, 35);
		assertEquals(42, glava.value);
	}
	
	/**
	 * 
	 */
	@Test
	public void testAddNodeExerciseExampleNode2(){
		TreeNode glava = null;
		glava = addNode(glava, 42);
		glava = addNode(glava, 76);
		glava = addNode(glava, 21);
		glava = addNode(glava, 76);
		glava = addNode(glava, 35);
		assertEquals(76, glava.right.value);
	}
	
	/**
	 * 
	 */
	@Test
	public void testAddNodeExerciseExampleNode3(){
		TreeNode glava = null;
		glava = addNode(glava, 42);
		glava = addNode(glava, 76);
		glava = addNode(glava, 21);
		glava = addNode(glava, 76);
		glava = addNode(glava, 35);
		assertEquals(21, glava.left.value);
	}
	
	/**
	 * 
	 */
	@Test
	public void testAddNodeExerciseExampleNode4(){
		TreeNode glava = null;
		glava = addNode(glava, 42);
		glava = addNode(glava, 76);
		glava = addNode(glava, 21);
		glava = addNode(glava, 76);
		glava = addNode(glava, 35);
		assertEquals(35, glava.left.right.value);
	}

	/**
	 * 
	 */
	@Test
	public void testContainsValueEmptyTree() {
		TreeNode root = null;
		assertEquals(false, containsValue(root, 5));
	}
	
	/**
	 * 
	 */
	@Test
	public void testContainsValueOneNode() {
		TreeNode root = new TreeNode();
		root.value = 6;
		assertEquals(true, containsValue(root, 6));
	}
	
	/**
	 * 
	 */
	@Test
	public void testContainsValueOneNodeWrongValue() {
		TreeNode root = new TreeNode();
		root.value = 6;
		assertEquals(false, containsValue(root, 5));
	}
	
	/**
	 * 
	 */
	@Test
	public void testContainsValueExerciseExample(){
		TreeNode glava = null;
		glava = addNode(glava, 42);
		glava = addNode(glava, 76);
		glava = addNode(glava, 21);
		glava = addNode(glava, 76);
		glava = addNode(glava, 35);
		assertEquals(true, containsValue(glava, 76));
	}
	
	/**
	 * 
	 */
	@Test
	public void testContainsValueExerciseExampleWrong(){
		TreeNode glava = null;
		glava = addNode(glava, 42);
		glava = addNode(glava, 76);
		glava = addNode(glava, 21);
		glava = addNode(glava, 76);
		glava = addNode(glava, 35);
		assertEquals(false, containsValue(glava, 33));
	}

	

	/**
	 * 
	 */
	@Test
	public void testTreeSizeEmpty() {
		TreeNode glava = null;
		assertEquals(0, treeSize(glava));
	}
	
	/**
	 * 
	 */
	@Test
	public void testTreeSizeExerciseExample(){
		TreeNode glava = null;
		glava = addNode(glava, 42);
		glava = addNode(glava, 76);
		glava = addNode(glava, 21);
		glava = addNode(glava, 76);
		glava = addNode(glava, 35);
		assertEquals(4, treeSize(glava));
	}
	
	/**
	 * 
	 */
	@Test
	public void testTreeSizeFourNodes() {
		TreeNode glava = new TreeNode();
		glava.left = new TreeNode();
		glava.right = new TreeNode();
		glava.value = 5;
		glava.left.value = 3;
		glava.right.value = 8;
		glava.right.left = new TreeNode();
		glava.right.left.value = 6;
		assertEquals(4, treeSize(glava));
	}
	
	/**
	 * 
	 */
	@Test
	public void testTreeSizeFiveNodes(){
		TreeNode glava = new TreeNode();
		glava.value = 7;
		glava.left = new TreeNode();
		glava.left.value = 4;
		glava.left.left = new TreeNode();
		glava.left.left.value = 3;
		glava.left.right = new TreeNode();
		glava.left.right.value = 5;
		glava.right = new TreeNode();
		glava.right.value = 11;
		assertEquals(5, treeSize(glava));
	}

	

}

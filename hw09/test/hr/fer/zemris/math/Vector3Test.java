package hr.fer.zemris.math;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Sphere;

public class Vector3Test {
	private static final double epsilon = 1e-6;
	
	@Test
	public void testNorm() {
		Vector3 v = new Vector3(2.5, -1.66, 3.25);
		
		assertEquals(4.4235845193688794, v.norm(), epsilon);
	}
	
	@Test
	public void testNormalized() {
		Vector3 v = new Vector3(2.5, 3, -1.25);
		
		assertEquals(new Vector3(0.609711, 0.731653, -0.304855), v.normalized());
	}
	
	@Test
	public void testAdd() {
		Vector3 v1 = new Vector3(-2, 3.25, 1.75);
		Vector3 v2 = new Vector3(-2.2, -2.2, -2.2);
		
		assertEquals(new Vector3(-4.2, 1.05, -0.45), v1.add(v2));
	}
	
	@Test
	public void testSub() {
		Vector3 v1 = new Vector3(1.5, 1.5, 1.5);
		Vector3 v2 = new Vector3(0.5, 0.5, 0.5);
		
		assertEquals(new Vector3(1, 1, 1), v1.sub(v2));
	}
	
	@Test
	public void testDot() {
		Vector3 v1 = new Vector3(1.76, -1.15, 0.25);
		Vector3 v2 = new Vector3(0.56, 0.75, 1);
		
		assertEquals(0.3731 , v1.dot(v2), epsilon);
	}
	
	@Test
	public void testCross() {
		Vector3 v1 = new Vector3(1, 2, 3);
		Vector3 v2 = new Vector3(0.5, 1.5, 2.5);
		
		assertEquals(new Vector3(0.5, -1, 0.5), v1.cross(v2));
	}
	
	@Test
	public void testScale() {
		Vector3 v = new Vector3(1, 1, 1);
		
		assertEquals(new Vector3(3.5, 3.5, 3.5), v.scale(3.5));
	}
	
	@Test
	public void testCosAngle() {
		Vector3 v1 = new Vector3(0.5, 1.25, -0.5);
		Vector3 v2 = new Vector3(-1, 2.2, 0.5);
		
		assertEquals(0.5643186, v1.cosAngle(v2), epsilon);
	}

}

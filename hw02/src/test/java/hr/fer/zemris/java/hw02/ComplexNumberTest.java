package hr.fer.zemris.java.hw02;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ComplexNumberTest {
	
	private final static double EPSILON = 1e-8;
	
	private ComplexNumber c1;
	private ComplexNumber c2;
	
	@Before
	public void init() {
		c1 = new ComplexNumber(2.5, -3);
		c2 = new ComplexNumber(2, 3);
	}
	
	@Test
	public void getReal() {
		assertEquals(2.5, c1.getReal(), EPSILON);
	}
	
	@Test
	public void getImaginary() {
		assertEquals(-3.0, c1.getImaginary(), EPSILON);
	}
	
	@Test
	public void getMagnitudeTest() {
		assertEquals(3.9051248379533272, c1.getMagnitude(), EPSILON);
	}
	
	@Test
	public void getAnglePositiveAngle() {
		assertEquals(0.982793723247, new ComplexNumber(2.5, 3.75).getAngle(), EPSILON);
	}
	
	@Test
	public void getAngleRealAndImaginaryValuesZero() {
		assertEquals(0, new ComplexNumber(0.0, 0.0).getAngle(), EPSILON);
	}
	
	@Test
	public void getAngleNegativeAngle() {
		// Angle has to be in interval [0, 2PI>.
		assertEquals(5.4977871437825865, new ComplexNumber(2.0, -2.0).getAngle(), EPSILON);
	}
	
	@Test
	public void fromReal() {
		assertEquals(-2.5, ComplexNumber.fromReal(-2.5).getReal(), EPSILON);
	}
	
	@Test
	public void fromImaginary() {
		assertEquals(-2.5, ComplexNumber.fromImaginary(-2.5).getImaginary(), EPSILON);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void fromMagnitudeAndAngleMagnitudeLessThanZero() {
		ComplexNumber.fromMagnitudeAndAngle(-2.0, 3.0);
	}
	
	@Test
	public void fromMagnitudeAndAngleAngleEqualsHalfPIRealValue() {
		assertEquals(0.0, ComplexNumber.fromMagnitudeAndAngle(2.5, 1.5707963267948966).getReal(), EPSILON);
	}
	
	@Test
	public void fromMagnitudeAndAngleAngleEqualsHalfPIImValue() {
		assertEquals(2.5, ComplexNumber.fromMagnitudeAndAngle(2.5, 1.5707963267948966).getImaginary(), EPSILON);
	}
	
	@Test
	public void fromMagnitudeAndAngleAngleIsGreaterThan2PIRealValue() {
		assertEquals(1.08595980781, ComplexNumber.fromMagnitudeAndAngle(2.0, 7.28).getReal(), EPSILON);
	}
	
	@Test
	public void fromMagnitudeAndAngleAngleIsGreaterThan2PIImValue() {
		assertEquals(1.679491380098, ComplexNumber.fromMagnitudeAndAngle(2.0, 7.28).getImaginary(), EPSILON);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void parseIllegalExpression1() {
		ComplexNumber.parse("sads");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void parseIllegalExpression2() {
		ComplexNumber.parse("3.5+i2.0");
	}
	
	@Test
	public void parseLegalExpression1() {
		assertEquals(-2.5, ComplexNumber.parse("-2.5").getReal(), EPSILON);
	}
	
	@Test
	public void parseLegalExpression2() {
		assertEquals(3.75, ComplexNumber.parse("3.75i").getImaginary(), EPSILON);
	}
	
	@Test
	public void parseLegalExpression3RealValue() {
		assertEquals(-3.0, ComplexNumber.parse("-3.0+2.0i").getReal(), EPSILON);
	}
	
	@Test
	public void parseLegalExpression3ImValue() {
		assertEquals(2.0, ComplexNumber.parse("2.0i-3.0").getImaginary(), EPSILON);
	}
	
	@Test
	public void addRealValue() {
		assertEquals(4.5, c1.add(c2).getReal(), EPSILON);
	}
	
	@Test
	public void addImValue() {
		assertEquals(0.0, c1.add(c2).getImaginary(), EPSILON);
	}
	
	@Test
	public void subRealValue() {
		assertEquals(0.5, c1.sub(c2).getReal(), EPSILON);
	}
	
	@Test
	public void subImValue() {
		assertEquals(-6, c1.sub(c2).getImaginary(), EPSILON);
	}
	
	@Test
	public void mulRealValue() {
		assertEquals(14.0, c1.mul(c2).getReal(), EPSILON);
	}
	
	@Test
	public void mulImValue() {
		assertEquals(1.5, c1.mul(c2).getImaginary(), EPSILON);
	}
	
	@Test
	public void divRealValue() {
		assertEquals(-0.3076923, c1.div(c2).getReal(), EPSILON);
	}
	
	@Test
	public void divImValue() {
		assertEquals(-1.038461538, c1.div(c2).getImaginary(), EPSILON);
	}
	
	@Test
	public void toFifthPowerCNumber1RealValue() {
		assertEquals(-296.09375, c1.power(5).getReal(), EPSILON);
	}
	
	@Test
	public void toFifthPowerCNumber1ImValue() {
		assertEquals(858.5625, c1.power(5).getImaginary(), EPSILON);
	}
	
	@Test
	public void toSeventhPowerCNumber2RealValue() {
		assertEquals(6554, c2.power(7).getReal(), EPSILON);
	}
	
	@Test
	public void toSeventhPowerCNumber2ImValue() {
		assertEquals(4449, c2.power(7).getImaginary(), EPSILON);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void powerNLessThanZero() {
		c1.power(-2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void rootNEqualsZero() {
		c1.root(0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void rootNLessThanZero() {
		c1.root(-2);
	}
	
	@Test
	public void thirdRootOfCNumber1() {
		ComplexNumber[] expectedResult = new ComplexNumber[] {
				new ComplexNumber(-0.361428879, 1.532712301), new ComplexNumber(-1.1466533, -1.0793627),
				new ComplexNumber(1.5080822, -0.4533496)
		};
		assertArrayEquals(expectedResult, c1.root(3));
	}
	
	@Test
	public void thirdRootOfCNumber2() {
		ComplexNumber[] expectedResult = new ComplexNumber[] {
				new ComplexNumber(1.451856618, 0.493403534), new ComplexNumber(-1.1532283, 1.0106429),
				new ComplexNumber(-0.2986283, -1.504046)
		};
		assertArrayEquals(expectedResult, c2.root(3));
	}
	
	@Test
	public void toStringTestCNumber1() {
		assertEquals("2.500 - 3.000i", c1.toString());
	}
	
	@Test
	public void toStringTestCNumber2() {
		assertEquals("2.000 + 3.000i", c2.toString());
	}
	
	
}

package package1;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestStudent {

	//Test default constructor
	@Test
	public void testDefaultConstructor() {
		Student student = new Student();
		assertEquals(student.getName(), "");
		assertEquals(student.getgNumber(), "000000000");
		assertTrue(student.getGpa()==0.0);
		Student other = new Student();
		assertTrue(student.equals(other));
	}
	
	//Test constructor with parameters
	@Test
	public void testGoodConstructor() {
		Student student = new Student("First Last", "G1234", 1.0);
		assertEquals(student.getName(), "First Last");
		assertEquals(student.getgNumber(), "G1234");
		assertTrue(student.getGpa()==1);
		Student student2 = new Student();
		student2.setName("First Last");
		student2.setgNumber("G1234");
		student2.setGpa(1);
		assertTrue(student.equals(student2));
		Student other = new Student("Other Last", "G1234", 4);
		Student other2 = new Student("Other Last", "G1234", 0);
		assertFalse(student2.equals(other));
		assertFalse
		(other.equals(other2));
	}
	
	//Test other constructor invalid name
	@Test (expected = InvalidArgumentException.class)
	public void testNoName() {
		new Student("", "G1234", 1.0);
	}
	
//	@Test (expected = InvalidArgumentException.class)
//	public void testOnlyFirstName() {
//		new Student("First", "G1234", 1.0);
//	}
	
//	@Test (expected = InvalidArgumentException.class)
//	public void testBadName() {
//		new Student("First A", "G1234", 1.0);
//	}
	
//	@Test (expected = InvalidArgumentException.class)
//	public void testBadName2() {
//		new Student("A B", "G1234", 2.0);
//	}
	
	//Test other constructor invalid gnumber
	@Test (expected = InvalidArgumentException.class)
	public void testNoGNumber() {
		new Student("First Last", "", 2);
	}
	
//	@Test (expected = InvalidArgumentException.class)
//	public void testBadGNumber() {
//		new Student("First Last", "1233", 2);
//	}
	
//	@Test (expected = InvalidArgumentException.class)
//	public void testBadGNumber2() {
//		new Student("First Last", "G12", 2);
//	}
	
//	@Test (expected = InvalidArgumentException.class)
//	public void testBadGNumber3() {
//		new Student("First Last", "G123356", 2);
//	}
	
	//Test other constructor invalid gpa
	@Test (expected = InvalidArgumentException.class)
	public void testNegativeGPA() {
		new Student("First Last", "G1233", -0.2);
	}
	
	@Test (expected = InvalidArgumentException.class)
	public void testBigGPA() {
		new Student("First Last", "G1233", 4.001);
	}
	
	//Test compareTo
	@Test
	public void testCompareTo() {
		Student a = new Student("Aaa Bbb", "G1234", 2.0);
		Student b = new Student("Abb Bcc", "G1235", 3.0);
		Student c = new Student("Eee Cdd", "G1236", 4.0);
		Student d = new Student("Abb Bcc", "G1237", 1.0);
		assertTrue(a.compareTo(c)<0);
		assertTrue(a.compareTo(b)<0);
		assertTrue(b.compareTo(d)==0);
		assertTrue(c.compareTo(a)>0);
	}
	
	//Test toString
	@Test
	public void testToString() {
		Student a = new Student("Aaa Bbb", "G1234", 2.0);
		String s = "Aaa Bbb, G1234, 2.0; \n";
		assertEquals(a.toString(),s);
	}
	
	//Test equals other student
	@Test
	public void testEqualsStudent() {
		Student a = new Student("Aaa Bbb", "G1234", 2.0);
		Student b = new Student("Abb Bcc", "G1235", 3.0);
		Student c = new Student("Eee Cdd", "G1236", 4.0);
		Student d = new Student("Abb Bcc", "G1235", 3);
		assertFalse(a.equals(b));
		assertTrue(b.equals(d));
		assertTrue(c.equals(new Student("Eee Cdd", "G1236", 4.0)));
		assertFalse(c.equals(d));
	}
	
//	//Test equals other object
//	@Test
//	public void testEqualsObject() {
//		Student a = new Student("Aaa Bbb", "G1234", 2.0);
//		Student b = new Student("Abb Bcc", "G1235", 3.0);
//		Object c = new Student("Eee Cdd", "G1236", 4.0);
//		Object d = new Student("Abb Bcc", "G1235", 3);
//		assertFalse(a.equals(d));
//		assertTrue(b.equals(d));
//		assertFalse(b.equals(c));
//	}
	
}

package package1;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestNode {

	@Test
	public void nodeConstructorsTest() {
		Student s1 = new Student("Adam", "G012345",3.0);
		Node<Student> nodeS1 = new Node(s1, null, null);

		//Testing constructor
		assertEquals(nodeS1.getData().getName(),"Adam");
		assertEquals(nodeS1.getData().getgNumber(),"G012345");
		assertEquals(nodeS1.getData().getGpa(),3.0);
		assertNull(nodeS1.getPrevious());
		assertNull(nodeS1.getNext());
		//Test other constructor
		Node<Student> nodeNull = new Node();
		assertNull(nodeNull.getData());
		assertNull(nodeNull.getPrevious());
		assertNull(nodeNull.getNext());
	}

	@Test
	public void nodeSetMethods(){
		Student s1 = new Student("Adam", "G012345",3.0);
		Node<Student> nodeS1 = new Node(s1, null, null);
		Node<Student> nodeNull = new Node();
		
		//Set Methods
		Student s2 = new Student("Frank","G987",4.0);
		nodeNull.setData(s2);
		assertEquals(nodeNull.getData().getName(),"Frank");
		assertEquals(nodeNull.getData().getgNumber(),"G987");
		assertEquals(nodeNull.getData().getGpa(), 4.0);
		nodeS1.setNext(nodeNull);
		assertEquals(nodeS1.getNext().getData(),nodeNull.getData());
		assertNull(nodeNull.getPrevious());
		nodeNull.setPrevious(nodeS1);
		assertEquals(nodeNull.getPrevious().getData(),nodeS1.getData());
	}
	
	//Testing equals in node
	@Test
	public void testNodeEquals() {
		Student s1 = new Student("Adam", "G012345",3.0);
		Student s2 = new Student("Jake", "G02343", 3.0);
		Node<Student> n1 = new Node<Student>(s1,null,null);
		assertTrue(n1.equals(new Node<Student>(s1,null,null)));
		assertFalse(n1.equals(new Node<Student>(s2,null,n1)));
	}
}

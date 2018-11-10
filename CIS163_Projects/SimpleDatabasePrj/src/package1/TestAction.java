package package1;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAction {

	//Test Action Constructor Only Operation
	@Test
	public void testActionName() {
		Action a1 = new Action("a");
		assertEquals("a", a1.getOperation());
		assertEquals(null, a1.getNext());
		assertEquals(null, a1.getPrevious());
		assertEquals(null, a1.getStudent());
		assertEquals(null, a1.getNode1());
		assertEquals(null, a1.getNode2());
	}

	//Test Action Constructor Operation + Student
	@Test
	public void testActionStudent() {
		Action a1 = new Action("u", new Student("Aaa", "G123", 3));
		assertEquals("u", a1.getOperation());
		assertEquals(null, a1.getNext());
		assertEquals(null, a1.getPrevious());
		assertTrue(a1.getStudent().equals(
				new Student("Aaa", "G123", 3)));
		assertEquals(null, a1.getNode1());
		assertEquals(null, a1.getNode2());
	}

	//Test Action Constructor Operation + 3 Students
	@Test
	public void testAction3Students() {
		Action a1 = new Action("i", new Student("Aaa", "G123", 3),
				new Student("Bbb", "G124", 2), 
				new Student("Ccc", "G125", 4));
		assertEquals("i", a1.getOperation());
		assertTrue(a1.getStudent().equals(
				new Student("Aaa", "G123", 3)));
		assertTrue(a1.getPrevious().equals(
				new Student("Bbb", "G124", 2)));
		assertTrue(a1.getNext().equals(
				new Student("Ccc", "G125", 4)));
		assertEquals(null, a1.getNode1());
		assertEquals(null, a1.getNode2());
	}

	//Test Action Constructor Operation + Node
	@Test
	public void testActionNode() {
		Student student = new Student("Aaa", "G123", 3);
		Action a1 = new Action("d", 
				new Node<Student>(student, null, null));
		assertEquals("d", a1.getOperation());
		assertEquals(null, a1.getNext());
		assertEquals(null, a1.getPrevious());
		assertEquals(null, a1.getStudent());
		assertTrue(a1.getNode1().equals(
				new Node<Student>(student, null, null)));
		assertEquals(null, a1.getNode2());
	}

	//Test Action Constructor Operation 2 Nodes
	@Test
	public void testAction2Nodes() {
		Student student = new Student("Aaa", "G123", 3);
		Student student2 = new Student("Bbb", "G124", 4);
		Node<Student> n1 = new Node<Student>(student, null, null);
		Node<Student> n2 = new Node<Student>(student2, n1, null);
		Action a1 = new Action("d", n1, n2);
		assertEquals("d", a1.getOperation());
		assertEquals(null, a1.getNext());
		assertEquals(null, a1.getPrevious());
		assertEquals(null, a1.getStudent());
		assertTrue(a1.getNode1().equals(
				new Node<Student>(student, null, null)));
		assertTrue(a1.getNode2().equals(
				new Node<Student>(student2, null, null)));
	}
}

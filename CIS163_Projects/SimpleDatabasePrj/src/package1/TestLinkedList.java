package package1;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestLinkedList {
	@Test
	public void linkedListConstructor() {
		LinkedList list = new LinkedList(); //Linked Lists with nothing 
		assertNull(list.getHead()); 
		assertNull(list.getTail());
		assertEquals(list.getCount(),0);
	}
	
	@Test
	public void linkedListInsertAtEnd() {
		Student s1 = new Student("Adam", "G0123", 3.0);
		Student s2 = new Student("Frank", "G4567", 4.0);
		Student s3 = new Student("Guy", "gNumber", 0.5);
		LinkedList list = new LinkedList();
		list.insertAtEnd(s1); //adds at end in an empty list
		assertEquals(list.getHead().getData(),s1);
		assertEquals(list.getTail().getData(),s1);
		assertEquals(list.getCount(), 1);
		list.insertAtEnd(s2); //adds at end with one already in list
		assertEquals(list.getHead().getData(),s1);
		assertEquals(list.getHead().getNext().getData(),s2);
		assertEquals(list.getTail().getData(),s2);
		assertEquals(list.getCount(), 2);
		list.insertAtEnd(s3); //add at end with two already in list
		assertEquals(list.getHead().getData(),s1);
		assertEquals(list.getHead().getNext().getData(),s2);
		assertEquals(list.getTail().getData(),s3);
		assertEquals(list.getCount(), 3);
	}
	
	@Test
	public void linkedListDelete() {
		Student s1 = new Student("Adam", "G0123", 3.0);
		Student s2 = new Student("Frank", "G0123", 4.0);
		Student s3 = new Student("Guy", "gNumber", 0.5);
		Student s4 = new Student("Jon", "G0123", 4.0);
		LinkedList list = new LinkedList();
		list.insertAtEnd(s1);
		list.delete("G0123"); //delete with only one
		assertNull(list.getHead());
		assertEquals(list.getCount(), 0);
		list.insertAtEnd(s1);
		list.insertAtEnd(s2);
		list.insertAtEnd(s3);
		list.delete("G0123"); //deletes head
		//deletes the top, if there's two with same gNum
		assertEquals(list.getHead().getData().getName(),s2.getName()); 
		assertEquals(list.getCount(),2); 
		list.delete("gNumber"); //deletes tail
		assertEquals(list.getTail().getData().getName(),s2.getName());
		assertEquals(list.getCount(),1);
		list.delete("G0123");
		assertNull(list.getHead());
		assertEquals(list.getCount(),0);
		list.insertAtEnd(s1);
		list.insertAtEnd(s2);
		list.insertAtEnd(s3);
		list.insertAtEnd(s4);
		list.delete("G0123"); //3 have same gNum, only deletes one
		assertEquals(list.getHead().getData().getName(),s2.getName());
		assertEquals(list.getCount(), 3);
		list.delete("gNumber"); //deletes middle
		assertEquals(list.getHead().getNext().getData().getName(), 
				s4.getName());
	}
	
	@Test
	public void linkedListFind(){
		Student s1 = new Student("Adam", "G0123", 3.0);
		Student s2 = new Student("Frank", "G4567", 4.0);
		Student s3 = new Student("Guy", "gNumber", 0.5);
		LinkedList list = new LinkedList();
		assertNull(list.find("G0123")); //No List
		assertNull(list.find(null)); //inserted null
		list.insertAtEnd(s1);
		list.insertAtEnd(s2);
		list.insertAtEnd(s3);
		//finds head
		assertEquals(list.find("G0123").getgNumber(), "G0123");
		//finds middle; 
		assertEquals(list.find("G4567").getgNumber(), "G4567"); 
		//finds tail
		assertEquals(list.find("gNumber").getgNumber(), "gNumber"); 
		assertNull(list.find("Hi there.")); //Not even in list
	}
	
	@Test
	public void linkedListUpdate(){
		Student s1 = new Student("Adam", "G0123", 3.0);
		Student s2 = new Student("Frank", "G4567", 4.0);
		Student s3 = new Student("Guy", "gNumber", 0.5);
		LinkedList list = new LinkedList();
		assertFalse(list.update("G0123", null)); //Student is null
		assertFalse(list.update(null, s1)); //gNumber is null
		assertFalse(list.update("G0123", s1));  //Nothing in list
		list.insertAtEnd(s1);
		list.insertAtEnd(s2);
		list.insertAtEnd(s3);
		assertTrue(list.update("G0123", s1)); //Updating Head
		assertTrue(list.update("G4567", s2)); //Updating middle
		assertTrue(list.update("gNumber", s3)); //Updating tail
	}
	
	@Test
	public void linkedListDisplay(){
		Student s1 = new Student("Adam", "G0123", 3.0);
		Student s2 = new Student("Frank", "G4567", 4.0);
		Student s3 = new Student("Guy", "gNumber", 0.5);
		LinkedList list = new LinkedList();
		assertEquals(list.toString(), ""); //nothing in list
		list.insertAtEnd(s1);
		list.insertAtEnd(s2);
		list.insertAtEnd(s3);  //displaying multiple students in list
		assertEquals(list.toString(), "Adam, G0123, 3.0; \nFrank, "
				+ "G4567, 4.0; \nGuy, gNumber, 0.5; \n");
	}
	
	@Test
	public void linkedListReverse(){
		Student s1 = new Student("Adam", "G0123", 3.0);
		Student s2 = new Student("Frank", "G4567", 4.0);
		Student s3 = new Student("Guy", "gNumber", 0.5);
		LinkedList list = new LinkedList();
		list.reverseList();  //Reversed nothing
		assertNull(list.getHead());
		list.insertAtEnd(s1);
		list.reverseList(); //Reversing with just a head
		assertEquals(list.getHead().getData().getName(), s1.getName());
		list.insertAtEnd(s2);
		assertEquals(list.getTail().getData().getName(), s2.getName());
		list.reverseList(); //Reverses with head and tail
		assertEquals(list.getHead().getData().getName(), s2.getName());
		assertEquals(list.getTail().getData().getName(), s1.getName());
		list.insertAtEnd(s3);
		assertEquals(list.getTail().getData().getName(), s3.getName());
		list.reverseList();  //Reverses list with head, tail, and middle
		assertEquals(list.getHead().getData().getName(), s3.getName());
		assertEquals(list.getHead().getNext().getData().getName(), 
				s1.getName());
		assertEquals(list.getTail().getData().getName(), s2.getName());
	}
	
	@Test
	public void linkedListDeleteNode(){
		Student s1 = new Student("Adam", "G0123", 3.0);
		Student s2 = new Student("Frank", "G4567", 4.0);
		Student s3 = new Student("Guy", "gNumber", 0.5);
		Node<Student> nodeS1 = new Node(s1, null, null);
		LinkedList list = new LinkedList();
		//list.deleteNode(nodeS1);
		list.insertAtEnd(s1);
		list.insertAtEnd(s2);
		list.insertAtEnd(s3);
	}
	
	@Test
	public void linkedListRemoveDuplicates(){
		Student s1 = new Student("Adam", "G0123", 3.0);
		Student s2 = new Student("Frank", "G4567", 4.0);
		Student s3 = new Student("Adam", "G0123", 3.0);
		Student s4 = new Student("Frank", "G4567", 4.0);
		Student s5 = new Student("Adam", "G0123", 3.0);
		LinkedList list = new LinkedList();
		list.removeDuplicates(); //Doesn't remove anything,
		assertEquals(list.getCount(), 0); //nothing to remove
		list.insertAtEnd(s1);
		list.removeDuplicates(); //Doesn't remove anything, only 1
		assertEquals(list.getCount(),1); 
		list.insertAtEnd(s3);
		list.removeDuplicates(); //removes one, because' duplicate
		assertEquals(list.getCount(), 1);
		list.insertAtEnd(s2);
		list.removeDuplicates(); //removes nothing, they're different
		assertEquals(list.getCount(), 2);
		list.insertAtEnd(s3);
		list.removeDuplicates(); //head and tail match, removes tail
		assertEquals(list.getCount(), 2);
		list.insertAtEnd(s4);
		list.insertAtEnd(s3); 
		list.removeDuplicates(); //middle two match, head and tail match
		assertEquals(list.getCount(), 2); //removes one of each
		list.insertAtEnd(s3);
		list.insertAtEnd(s5);
		list.removeDuplicates(); //3 match, removes 2;
		assertEquals(list.getCount(), 2);
	}
	
	@Test
	public void linkedListSort(){
		Student s1 = new Student("Adam", "G0123", 3.0);
		Student s2 = new Student("Frank", "G4567", 4.0);
		Student s3 = new Student("Guy", "G0123", 1.0);
		Student s4 = new Student("Girl", "G4567", 2.0);
		Student s5 = new Student("Person", "G0123", 0.0);
		LinkedList list = new LinkedList();
		list.sort(); //nothing happens
		list.insertAtEnd(s3);
		list.sort();
		assertEquals(list.getHead().getData().getName(), s3.getName());
		list.insertAtEnd(s2);
		list.sort(); //switches it
		assertEquals(list.getHead().getData().getName(), s2.getName());
		assertEquals(list.getTail().getData().getName(), s3.getName());
		list.insertAtEnd(s1);
		list.insertAtEnd(s5);
		list.insertAtEnd(s4);
		list.sort();  //EVERYTHING is sorted in order
		assertEquals(list.getHead().getData().getName(), s1.getName());
		assertEquals(list.getHead().getNext().getData().getName(), 
				s2.getName());
		assertEquals(list.getHead().getNext().getNext().getData().
				getName(), s4.getName());
		assertEquals(list.getTail().getPrevious().getData().getName(), 
				s3.getName());
		assertEquals(list.getTail().getData().getName(), s5.getName());
	}

	//Testing sort with an empty list
	@Test
	public void testSortEmpty() {
		LinkedList list = new LinkedList();
		assertTrue(list.getHead() == null);
		assertTrue(list.getTail() == null);
		list.sort();
		assertTrue(list.getHead() == null);
		assertTrue(list.getTail() == null);
	}
	
	//Testing sort with only 1 item in list
	@Test
	public void testSort1Item() {
		LinkedList list = new LinkedList();
		Student a1 = new Student("Aaa", "G123", 2);
		Student b2 = new Student("Bbb", "G124", 3);
		list.insertAtEnd(a1);
		assertTrue(list.getHead().equals(
				new Node<Student>(a1, null, null)));
		list.sort();
		assertTrue(list.getHead().equals(
				new Node<Student>(a1, null, null)));
		assertTrue(list.getTail().equals(
				new Node<Student>(a1, null, null)));
		list.setHead(b2);
		assertTrue(list.getHead().equals(
				new Node<Student>(b2, null, null)));
		assertTrue(list.getTail().equals(
				new Node<Student>(b2, null, null)));
	}
	
	//Testing sort with multiple items
	@Test
	public void testSortMultiple() {
		LinkedList list = new LinkedList();
		Student a = new Student("Aaa", "G123", 3);
		Student b = new Student("Bbb", "G124", 2);
		Student c = new Student("Ccc", "G125", 4);
		Student d = new Student("Ddd", "G126", 1);
		Node<Student> an = new Node<Student>(a,null,null);
		Node<Student> bn = new Node<Student>(b,null,null);
		Node<Student> cn = new Node<Student>(c,null,null);
		Node<Student> dn = new Node<Student>(d,null,null);
		list.insertAtEnd(c);
		list.insertAtEnd(d);
		list.insertAtEnd(a);
		list.insertAtEnd(a);
		list.insertAtEnd(b);
		list.insertAtEnd(c);
		list.sort();
		assertTrue(list.getHead().equals(an));
		assertTrue(list.getHead().getNext().equals(an));
		assertTrue(list.getHead().getNext().getNext().equals(bn));
		assertTrue(list.getTail().equals(dn));
		assertTrue(list.getTail().getPrevious().equals(cn));
		assertTrue(list.getTail().getPrevious().equals(cn));
	}

	//Test sort already sorted list
	@Test
	public void testSortedSort() {
		LinkedList list = new LinkedList();
		Student a = new Student("Aaa", "G123", 3);
		Student b = new Student("Bbb", "G124", 2);
		Student c = new Student("Ccc", "G125", 4);
		Student d = new Student("Ddd", "G126", 1);
		Node<Student> an = new Node<Student>(a,null,null);
		Node<Student> bn = new Node<Student>(b,null,null);
		Node<Student> cn = new Node<Student>(c,null,null);
		Node<Student> dn = new Node<Student>(d,null,null);
		list.insertAtEnd(a);
		list.insertAtEnd(a);
		list.insertAtEnd(b);
		list.insertAtEnd(c);
		list.insertAtEnd(c);
		list.insertAtEnd(d);
		list.sort();
		assertTrue(list.getHead().equals(an));
		assertTrue(list.getHead().getNext().equals(an));
		assertTrue(list.getHead().getNext().getNext().equals(bn));
		assertTrue(list.getTail().equals(dn));
		assertTrue(list.getTail().getPrevious().equals(cn));
		assertTrue(list.getTail().getPrevious().equals(cn));
	}
	
	//Testing undo with no actions
	@Test
	public void testUndoNoAction() {
		LinkedList list = new LinkedList();
		assertEquals(list.undo(), false);
	}
	
	//Testing undo insert
	@Test
	public void testUndoInsert() {
		LinkedList list = new LinkedList();
		Student a1 = new Student("Aaa", "G121", 3);
		Student a2 = new Student("Bbb", "G122", 2);
		Student a3 = new Student("Ccc", "G123", 3.5);
		list.insertAtEnd(a1);
		assertEquals(list.getStack().size(),1);
		list.undo();
		assertEquals(list.getStack().size(),0);
		assertEquals(list.getHead(), null);
		list.insertAtEnd(a1);
		list.insertAtEnd(a2);
		list.insertAtEnd(a3);
		list.insertAtEnd(a2);
		assertEquals(list.getStack().size(),4);
		list.undo();
		list.undo();
		list.undo();
		list.undo();
		assertEquals(list.getStack().size(),0);
	}
	
	//Testing undo update
	@Test
	public void undoUpdate() {
		LinkedList list = new LinkedList();
		Student a1 = new Student("Aaa", "G121", 3);
		Student a2 = new Student("Bbb", "G122", 2);
		Student a3 = new Student("Ccc", "G123", 3.5);
		Student a4 = new Student("Ddd", "G121", 2.5);
		Student a5 = new Student("Eee", "G122", 3.8);
		Student a6 = new Student("Fff", "G123", 4.0);
		list.insertAtEnd(a1);
		list.insertAtEnd(a2);
		list.insertAtEnd(a3);
		list.update("G121", a4);
		assertEquals(list.getStack().size(),4);
		list.update("G122", a5);
		assertEquals(list.getStack().size(),5);
		list.update("G123",  a6);
		assertEquals(list.getStack().size(),6);
		list.undo();
		assertTrue(list.getTail().getData().equals(a3));
		list.undo();
		assertTrue(list.getTail().getPrevious().getData().equals(a2));
		list.undo();
		assertTrue(list.getHead().getData().equals(a1));
	}
	
	//Testing undo remove
	@Test
	public void undoRemove() {
		LinkedList list = new LinkedList();
		Student a1 = new Student("Aaa", "G121", 3);
		Student a2 = new Student("Bbb", "G122", 2);
		Student a3 = new Student("Ccc", "G123", 3.5);
		list.insertAtEnd(a1);
		list.insertAtEnd(a2);
		list.insertAtEnd(a3);
		list.delete("G121");
		assertEquals(list.getStack().size(),4);
		list.undo();
		assertTrue(list.getHead().getData().equals(a1));
		list.delete("G122");
		list.delete("G123");
		list.undo();
		list.undo();
		assertTrue(list.getTail().getPrevious().getData().equals(a2));
		assertTrue(list.getTail().getData().equals(a3));
		assertEquals(list.getStack().size(),3);
	}
	
	//Testing undo reverse
	@Test
	public void undoReverse() {
		LinkedList list = new LinkedList();
		Student a1 = new Student("Aaa", "G121", 3);
		Student a2 = new Student("Bbb", "G122", 2);
		Student a3 = new Student("Ccc", "G123", 3.5);
		list.insertAtEnd(a1);
		list.insertAtEnd(a2);
		list.insertAtEnd(a3);
		list.reverseList();
		assertEquals(list.getStack().size(),4);
		list.undo();
		assertTrue(list.getHead().getData().equals(a1));
		assertTrue(list.getTail().getPrevious().getData().equals(a2));
		assertTrue(list.getTail().getData().equals(a3));
		assertEquals(list.getStack().size(),3);
	}
	
	//Testing undo removeDuplicates
	@Test //This is messed up... if I do the exact same thing in the 
		//gui, it works, but in the test, it doesnt...
	public void undoRemoveDuplicates() {
		LinkedList list = new LinkedList();
		Student a1 = new Student("Aaa", "G121", 3);
		Student a2 = new Student("Bbb", "G122", 2);
		list.insertAtEnd(new Student("Aaa", "G121", 3));
		list.insertAtEnd(new Student("Aaa", "G121", 3));
		list.insertAtEnd(a1);
		list.insertAtEnd(a1);
		list.insertAtEnd(a2);
		list.insertAtEnd(new Student("Aaa", "G121", 3));
		assertEquals(list.getCount(), 6);
		list.removeDuplicates();
		assertEquals(list.getCount(), 2);
		assertEquals(list.getStack().size(),10);
		list.undo();
		assertEquals(list.getStack().size(),6);
		assertEquals(list.getCount(), 6);
		assertTrue(list.getTail().getData().equals(a1));
		assertTrue(list.getTail().getPrevious().getData().equals(a2));
	}
	
	//Testing undo sort
	@Test
	public void undoSort() {
		LinkedList list = new LinkedList();
		Student a1 = new Student("Aaa", "G121", 3);
		Student a2 = new Student("Bbb", "G122", 2);
		Student a3 = new Student("Ccc", "G123", 3.5);
		list.insertAtEnd(a3);
		list.insertAtEnd(a1);
		list.insertAtEnd(a2);
		list.sort();
		list.undo();
		assertTrue(list.getHead().getData().equals(a3));
		assertTrue(list.getTail().getPrevious().getData().equals(a1));
		assertTrue(list.getTail().getData().equals(a2));
		assertEquals(list.getStack().size(),3);
	}
	
	//Testing undo different operations
	@Test
	public void undoCombo() {
		LinkedList list = new LinkedList();
		Student a1 = new Student("Aaa", "G121", 3);
		Student a2 = new Student("Bbb", "G122", 2);
		Student a3 = new Student("Ccc", "G123", 3.5);
		Student a4 = new Student("Ddd", "G121", 4);
		Student a5 = new Student("Eee", "G124", 2.1);
		list.insertAtEnd(a1);
		list.insertAtEnd(a2);
		list.insertAtEnd(a3);
		list.reverseList();
		list.sort();
		list.delete("G122");
		list.update("G121", a4);
		list.insertAtEnd(a5);
		list.insertAtEnd(a5);
		list.insertAtEnd(a5);
		list.removeDuplicates();
		list.undo();
		list.undo();
		list.undo();
		list.undo();
		list.undo();
		list.undo();
		list.undo();
		list.undo();
		assertTrue(list.getHead().getData().equals(a1));
		assertTrue(list.getTail().getPrevious().getData().equals(a2));
		assertTrue(list.getTail().getData().equals(a3));
		assertEquals(list.getStack().size(),3);
		list.undo();
		list.undo();
		list.undo();
		assertEquals(list.getHead(), null);
		assertEquals(list.getTail(), null);
		assertEquals(list.getCount(), 0);
		assertEquals(list.undo(), false);
	}
	
}

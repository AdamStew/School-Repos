package package1;

import java.io.Serializable;
import java.util.Stack;

/***********************************************************************
 * A Linked List class, that has methods that manipulate the Linked List 
 * in various ways, such as inserting, deleting, sorting, updating, 
 * reversing, etc.
 * @author Frank Derry Wanye, Adam Stewart
 * @version 04/13/2015
 **********************************************************************/
public class LinkedList implements Serializable{

	/** A default serial that allows the class student data to be
	 * saved and loaded. */
	private static final long serialVersionUID = 1L;

	/** Integer value that keeps track of how many nodes are in the 
	 * Linked List */
	private int count;

	/** Keeps track of what Node<Student> is currently at the top/head 
	 * of the Linked List */
	private Node<Student> head;

	/** Keeps track of what Node<Student> is currently at the 
	 * bottom/tail of the Linked List */
	private Node<Student> tail;

	/** A stack that keeps track of all the Actions performed, in order 
	 * to undo in the correct order */
	private Stack<Action> stack;

	/** Static boolean that keeps track if something is being 
	 * undo-reversed, so another Action isn't added to the stack */
	private static boolean uReverse = false;

	/** Static boolean that keeps track if something is being 
	 * undo-deleted, so another Action isn't added to the stack. */
	private static boolean uDelete = false;

	/** Static boolean that keeps track if something is being 
	 * undo-insert, so another Action isn't added to the stack. */
	private static boolean uInsert = false; 

	/** Static boolean that keeps track if something is being 
	 * undo-remove-duplicates, so another Action isn't added to the 
	 * stack. */
	private static boolean uDuplicates = false; 

	/** Static boolean that keeps track if something is being 
	 * undo-sort, so another Action isn't added to the stack. */
	private static boolean uSort = false; 

	/** Static boolean that keeps track if something is being 
	 * undo-update, so another Action isn't added to the stack. */
	private static boolean uUpdate = false;

	/*******************************************************************
	 * Default constructor sets all instance variables to null values or
	 *  values of 0.  Instantiates Stack<Action>.  
	 ******************************************************************/
	public LinkedList() {
		count = 0;
		head = null;
		tail = head;
		stack = new Stack<Action>();
	}

	/*******************************************************************
	 * Returns the count of the total Nodes<Student> in the Linked List
	 * @return count, an integer value
	 ******************************************************************/
	public int getCount() {
		return count;
	}

	/*******************************************************************
	 * Sets the value of the integer count.
	 * @param count of type Integer
	 ******************************************************************/
	public void setCount(int count) {
		this.count = count;
	}

	/*******************************************************************
	 * Returns a Node<Student> that currently lies at the top of the 
	 * Linked List
	 * @return head Node<Student>
	 ******************************************************************/
	public Node<Student> getHead() {
		return head;
	}

	/*******************************************************************
	 * Sets the Node<Student> that is at the top of the Linked List, 
	 * assuming Nodes currently already exist in the List.
	 * @param head of type Node<Student>
	 ******************************************************************/
	public void setHead(Node<Student> head) {
		if(head == tail) {
			this.head = head;
			this.tail = head;
		}
		else
			this.head = head;
	}

	/*******************************************************************
	 * Sets the Node<Student> that is at the top of the Linked List, 
	 * accounting for if Nodes currently exist in the List or not.
	 * @param student of type Student
	 ******************************************************************/
	public void setHead(Student student) {
		if(head == null) {
			head = tail = new Node<Student>(student, null, null);
			count++;
		}
		else if(head == tail) {
			head.setData(student);
			tail = head;
		}
		else {
			head.setData(student);
		}
	}

	/*******************************************************************
	 * Returns a Node<Student> that currently lies at the bottom of the 
	 * Linked List
	 * @return tail Node<Student>
	 ******************************************************************/
	public Node<Student> getTail() {
		return tail;
	}

	/*******************************************************************
	 * Sets the Node<Student> that is at the bottom of the Linked List, 
	 * assuming Nodes currently already exist in the List.
	 * @param tail of type Node<Student>
	 ******************************************************************/
	public void setTail(Node<Student> tail) {
		this.tail = tail;
	}

	/*******************************************************************
	 * Sets the Node<Student> that is at the bottom of the Linked List, 
	 * accounting for if Nodes currently exist in the List or not.
	 * @param student of type Student
	 ******************************************************************/
	public void setTail(Student student) {
		if(head == null) {
			head = tail = new Node<Student>(student, null, null);
			count++;
		}
		else if(head == tail) {
			head.setData(student);
			tail = head;
		}
		else {
			tail.setData(student);
		}
	}

	/*******************************************************************
	 * Returns Stack<Action> that is keeping track of all actions 
	 * performed.
	 * @return  Stack<Action>
	 ******************************************************************/
	public Stack<Action> getStack() {
		return stack;
	}

	/*******************************************************************
	 * Resets the Stack<Action> to empty.
	 ******************************************************************/
	public void resetStack() {
		stack = null;
	}

	/*******************************************************************
	 * Inserts a new Node<Student> at the end of the Linked List.
	 * @param student of type Student
	 ******************************************************************/
	public void insertAtEnd(Student student) {
		//when there's no list
		if(head == null) {
			head = tail = new Node<Student>(student, null, null);
			count++;
		}
		//when there is a list
		else {
			tail.setNext(new Node<Student>(student, null, tail));
			tail = tail.getNext();
			count++;
		}
		if(uInsert == false) //If undoing is being done, doesn't add to
			stack.push(new Action("remove", tail)); //the stack
	}

	/*******************************************************************
	 * Deletes the first Node<Student> in the LinkedList based off of 
	 * the gNumber inserted.
	 * @param gNumber, a String of the Node<Student> trying to be 
	 * deleted
	 * @return boolean True if successfully deleted, false if not
	 ******************************************************************/
	public boolean delete(String gNumber) {
		//when there's no list or gNumber is not in the getData()base
		if(head == null || find(gNumber) == null)
			return false;
		//deleting top
		if(head.getData().getgNumber().equals(gNumber)) {
			//if only one item in list
			if(count == 1) {
				if(uDelete == false) //Doesn't add action to the stack
					stack.push(new Action("insert", head.getData(), 
							null, null)); //if undoing
				head = tail = null;
				count--;
				return true;
			}
			if(uDelete == false)
				stack.push(new Action("insert", head.getData(), null, 
						head.getNext().getData()));
			setHead(head.getNext());
			if(head != null) {
				head.getPrevious().setNext(null);
				head.setPrevious(null);
			}
			count--;
			return true;
		}
		//deleting middle
		Node<Student> temp = head;
		while(temp.getNext()!=null) {
			//checks if gNumber is getNext() in list
			if(temp.getNext().getData().getgNumber().equals(gNumber)){
				//checks if gNumber is last in list, updates tail
				if(temp.getNext() == tail) {
					if(uDelete == false)
						stack.push(new Action("insert", tail.getData(), 
								tail.getPrevious().getData(), null));
					setTail(temp);
					tail.setNext(null);
					count--;
					return true;
				}
				if(uDelete == false)
					stack.push(new Action("insert", temp.getNext().getData(), 
							temp.getData(), temp.getNext().getNext().getData()));
				temp.setNext(temp.getNext().getNext());
				temp.getNext().setPrevious(temp);
				count--;
				return true;
			}
			temp = temp.getNext();
		}
		return false;  //if it somehow gets here, then it didn't delete
	}

	/*******************************************************************
	 * Finds a Student in the LinkedList, based on the gNumber.
	 * @param gNumber, a String of Node<Student> trying to be found
	 * @return A Student, with the matching gNumber
	 ******************************************************************/
	public Student find(String gNumber) {
		//if no list
		if(head==null)
			return null;
		//if there is a list
		Node<Student> temp = head;
		while(temp!=null) { //Scans until found matching gNumber
			if(temp.getData().getgNumber().equals(gNumber))
				return temp.getData();
			temp = temp.getNext();
		}
		return null;
	}

	/*******************************************************************
	 * Updates a Node<Student>'s data to the new desired info.  
	 * Can't change the gNumber.
	 * @param gNumber, a String of Node<Student> trying to be updated
	 * @param student, type Student of the information that will be 
	 * updated
	 * @return a boolean value, True if successfully updated, False if 
	 * not
	 ******************************************************************/
	public boolean update(String gNumber, Student student) {
		//if no list, no gNumber, or no student
		if(head == null || gNumber == null || student == null)
			return false;
		if(find(gNumber) == null) //checks if gNumber is in the list
			return false;
		Node<Student> temp = head;
		while(!temp.getData().getgNumber().equals(gNumber))
			temp = temp.getNext();
		Student original = temp.getData();
		temp.setData(student); //Changes the desired info
		if(uUpdate == false) //Doesn't add to the stack if being undone
			stack.push(new Action("update", original));
		return true;
	}

	/*******************************************************************
	 * Generates a String containing the name, gNumber, and GPA of each 
	 * student in the LinkedList, starting from the top, all the way to 
	 * the bottom.
	 ******************************************************************/
	public String toString() {
		String result = "";
		Node<Student> temp = head;
		while(temp!=null) { //Scans LinkedList
			result += temp.getData().toString();
			temp = temp.getNext();
		}
		return result;
	}

	/*******************************************************************
	 * Takes the current LinkedList, and puts it in the complete 
	 * opposite order.  Head and tail switch, along with all the other 
	 * values.
	 ******************************************************************/
	public void reverseList() {
		//when there's no list, or only 1 item, do nothing
		if(head == null || count == 1)
			return;
		Node<Student> newHead=head;
		head=tail;
		tail=newHead;
		Node<Student> temp=head;
		while(temp!=null){ //Scanning List
			newHead = temp.getNext();
			temp.setNext(temp.getPrevious()); //Switches Students
			temp.setPrevious(newHead); //Switches Students
			temp = temp.getNext();
		}
		if(uReverse == false) //Doesn't add the action to the stack if
			stack.push(new Action("reverse")); //being undone
	}

	/*******************************************************************
	 * Deletes the selected Node<Student> from the LinkedList
	 * @param node, type Node<Student>
	 ******************************************************************/
	private void deleteNode(Node<Student> node) {
		//when there's no list or node not in list
		if(head == null || node == null)
			return;
		//checks if node is next in list
		if(node.equals(head) && head.getNext() == null)
			head = tail = null;
		else if(node.equals(head)) { //if head
			head = head.getNext();
			head.setPrevious(null);
		}
		else if(node.equals(tail)) { //if tail
			setTail(tail.getPrevious());
			tail.setNext(null);
		}
		else { //if middle
			node.getPrevious().setNext(node.getNext());
			node.getNext().setPrevious(node.getPrevious());
		}
		count--; //one less in the List
	}


	/*******************************************************************
	 * Scans the whole LinkedList and deletes any Node<Student> if their
	 * data is the exact same (same name, gNumber, and GPA), so that 
	 * there is only one left.
	 ******************************************************************/
	public void removeDuplicates() {
		//no list
		if(head == null || count == 1)
			return;
		Node<Student> temp = head;
		Node<Student> compare = head;
		while(temp != null) { //First pointer scans
			while(compare.getNext() != null) {  //Second pointer scans
				if(temp.getData().equals(compare.getNext().getData())){
					if(uDuplicates == false) { //if undoing, don't stack
						if(compare.getNext().getNext() == null)
							stack.push(new Action("duplicate",
									compare.getNext().getData(), 
									compare.getData(), null));
						else
							stack.push(new Action("duplicate", 
									compare.getNext().getData(), 
									compare.getData(), 
									compare.getNext().getNext().
									getData()));
					}
					deleteNode(compare.getNext()); //Delete the dupe.
				}
				else
					compare = compare.getNext(); //move second pointer
			}
			temp = temp.getNext(); //move first pointer over
			compare = temp;
		}
	}

	/*******************************************************************
	 * Scans the entire LinkedList, ordering it based on alphabetical 
	 * order of their first names.
	 ******************************************************************/
	public void sort() {
		if(head == null || head == tail) //checks if 0 or 1 in the List
			return;
		Node<Student> index = head;
		int ordered = 0; //Keeps track of how many Nodes are in order
		while(index.getNext() != null){ //Scans the list
			//checks to see if the two nodes next to each other are  
			//currently in order, if not in order, then swaps the two
			if(index.getData().compareTo(index.getNext().getData())>0) {
				if(uSort == false) //Doesn't stack if undoing
					stack.push(new Action("swap",index.getNext(),
							index));
				swap(index, index.getNext());
			}
			else{
				ordered++; //if already in order, add to # ordered
			}
			if (index.getNext()!= null) //increments assuming next isn't
				index = index.getNext();//null.  Important because order
		}							    //changes do to the swapping
		if(getCount()-1!=ordered) 
			sort(); //If EVERYTHING isn't in order, then just swap again
	}

	/*******************************************************************
	 * Takes two Node<Student> in the LinkedList and swaps them in order
	 * @param node1 the first Node<Student> trying to be swapped
	 * @param node2 the second Node<Student> trying to be swapped
	 ******************************************************************/
	private void swap(Node<Student> node1, Node<Student> node2) {
		if(node1 == null || node2 == null) //assuming valid nodes
			throw new IllegalArgumentException();
		Student s1 = node1.getData();
		Student s2 = node2.getData();
		node1.setData(s2); //swap data
		node2.setData(s1); // swap data
	}

	/*******************************************************************
	 * Undoes the last performed Action to the LinkedList (Insert, 
	 * Delete, Update, Reverse, RemoveDuplicates, or Sort).
	 * @return a boolean value, True meaning Action was undone 
	 * successfully, False meaning nothing was undone
	 ******************************************************************/
	public boolean undo() {
		if(stack.isEmpty()) //Checks if anything can be undone
			return false;
		if(stack.peek().getOperation().equals("swap")) { //Undoing sort
			uSort = true;
			while(stack.peek().getOperation().equals("swap")){
				Action temp = stack.pop();
				swap(temp.getNode1(),temp.getNode2());//swaps nodes back
			}
			uSort = false;
			return true;
		}
		//undoing remove duplicates
		if(stack.peek().getOperation().equals("duplicate")) {
			uDuplicates = true;
			while(stack.peek().getOperation().equals("duplicate")){
				Action temp = stack.pop();
				insertBetween(temp.getStudent(),temp.getPrevious(),
						temp.getNext());//inserts the duplicates back
			}
			uDuplicates = false;
			return true;
		}
		Action temp = stack.pop();
		if(temp.getOperation().equals("reverse")) {//Undoing reverse
			uReverse = true;
			reverseList(); //re-reverses list
			uReverse = false;
			return true;
		}
		if(temp.getOperation().equals("remove")) { //Undoing an Insert
			uInsert = true;
			deleteNode(temp.getNode1()); //Deletes the node inserted
			uInsert = false;
			return true;
		}
		if(temp.getOperation().equals("insert")) { //Undoing a delete
			uDelete = true;
			insertBetween(temp.getStudent(), temp.getPrevious(), 
					temp.getNext()); //inserts deleted node back
			uDelete = false;
			return true;
		}
		if(temp.getOperation().equals("update")) { //Undoing an update
			uUpdate = true;
			update(temp.getStudent().getgNumber(),temp.getStudent());
			uUpdate = false; //Re-updates to old information
		}
		return false;
	}

	/*******************************************************************
	 * Inserts a new Node before another Node
	 * @param s1 the Student's data being inserted into the new Node
	 * @param next the Student's information that is before the new Node
	 ******************************************************************/
	private void insertBefore(Student s1, Student next) {
		if(head == null) //check if list exists
			head = new Node<Student>(s1, null, null);
		else if(s1 == null || next == null) //checks if valid info
			throw new IllegalArgumentException();
		else {
			Node<Student>temp = head;
			while(!temp.getData().equals(next)) { //Looks for node that
				temp = temp.getNext();       //you're inserting before
			}
			temp.setPrevious(new Node<Student>(s1,temp,temp.
					getPrevious())); //Makes new node in front
			if(temp.getPrevious().getPrevious() == null)//checks if what
				head = temp.getPrevious(); //inserted is the new head
			else
				temp.getPrevious().getPrevious().setNext(
						temp.getPrevious());//otherwise set the one 
		}					//before next, previous to the new Student
	}

	/*******************************************************************
	 * Inserts a new Node between two other Nodes
	 * @param s1 the Student's data being inserted into the new Node
	 * @param previous the Students info that is after new Node
	 * @param next the Student's information that is before the new Node
	 ******************************************************************/
	private void insertBetween(Student s1, Student previous, 
			Student next) {
		if(s1 == null) //checks if valid info
			return;
		if(previous == null && next == null) { //checks if list exists
			head = new Node<Student>(s1,null,null);
			count++;
			return;
		}
		if(next != null) { //all cases where you're inserting before
			insertBefore(s1, next);
		}
		else { //only case you insert after (which is after tail)
			tail.setNext(new Node<Student>(s1,null,tail));
			tail = tail.getNext(); //sets new tail
		}
		count++;
	}

	/******************************************************************
	 * A class that helps with the implementation of the undo method:
	 * it contains the action that needs to be performed and the 
	 * necessary Student and Node information needed to perform that 
	 * action
	 * @author Frank Derry Wanye, Adam Stewart
	 * @version 04/14/2015
	 *****************************************************************/
	private class Action implements Serializable{
		
		/** A default serial that allows the class student data to be
		 * saved and loaded. */
		private static final long serialVersionUID = 1L;
		
		/** The operation represents the operation that is to be 
		 * performed to undo an action. Values can be:
		 * "remove" for undoing an insert
		 * "swap" for undoing a sort
		 * "duplicate" for undoing remove duplicates
		 * "reverse" for undoing a reversal of the linked list
		 * "insert" for undoing a delete operation, and
		 * "update" for undoing an update operation */
		private String operation;

		/** Stores a node needed to perform an action */
		private Node<Student> node1;
		
		/** Stores a node needed to perform an action */
		private Node<Student> node2;
		
		/** Stores a student needed to perform an action */
		private Student student;
		
		/** Stores the student from the previous node */
		private Student previous;
		
		/** Stores the student from the next node */
		private Student next;

		/**************************************************************
		 * A constructor that only contains an operation - used to undo
		 * a reverse operation
		 * @param operation is the operation to be performed in the
		 * undo method
		 *************************************************************/
		public Action(String operation) {
			this.operation = operation;
		}

		/**************************************************************
		 * A constructor that contains an operation and a node - used
		 * to undo an insert operation
		 * @param operation is the operation to be performed in the
		 * undo method
		 * @param node1 is the node to be deleted
		 *************************************************************/
		public Action(String operation, Node<Student> node1) {
			this.operation = operation;
			this.node1 = node1;
		}

		/**************************************************************
		 * A constructor that contains an operation and two nodes - 
		 * used to undo the sort operation
		 * @param operation is the operation to be performed in the
		 * undo method
		 * @param node1 is the first node to be swapped
		 * @param node2 is the other node to be swapped
		 *************************************************************/
		public Action(String operation, Node<Student> node1, 
				Node<Student> node2) {
			this.operation = operation;
			this.node1 = node1;
			this.node2 = node2;
		}
		
		/**************************************************************
		 * A constructor that contains an operation and three students
		 * - used to undo the delete and remove duplicates operations
		 * @param operation is the operation to be performed in the
		 * undo method
		 * @param student is the student to be inserted
		 * @param previous is the student after which this student is
		 * to be inserted
		 * @param next is the student before which this student is to
		 * be inserted
		 *************************************************************/
		public Action(String operation, Student student, 
				Student previous, Student next) {
			this.operation = operation;
			this.student = student;
			this.previous = previous;
			this.next = next;
		}

		/**************************************************************
		 * A constructor that contains an operation and one student - 
		 * used to undo the update operation
		 * @param operation is the operation to be undone
		 * @param student is the student that was updated
		 *************************************************************/
		public Action(String operation, Student student) {
			this.operation = operation;
			this.student = student;
		}

		/************************************************************** 
		 * Obtains the operation to be performed
		 * @return a string containing the operation to be performed
		 * in the undo method
		 * ***********************************************************/
		public String getOperation() {
			return operation;
		}

		/**************************************************************
		 * Obtains a node needed to perform the undo action
		 * @return the node1 is the node that was swapped or inserted
		 *************************************************************/
		public Node<Student> getNode1() {
			return node1;
		}

		/**************************************************************
		 * Obtains the student needed to perform the undo operation
		 * @return the student is the student which was deleted or
		 * updated
		 *************************************************************/
		public Node<Student> getNode2() {
			return node2;
		}

		/**
		 * @return the student
		 */
		public Student getStudent() {
			return student;
		}

		/**************************************************************
		 * Obtains the student before the student that was deleted
		 * @return the previous student
		 *************************************************************/
		public Student getPrevious() {
			return previous;
		}

		/**************************************************************
		 * Obtains the student after the student that was deleted
		 * @return the next student
		 *************************************************************/
		public Student getNext() {
			return next;
		}

	}
}

package package1;

import java.io.Serializable;

/******************************************************************
 * A class that helps with the implementation of the undo method:
 * it contains the action that needs to be performed and the 
 * necessary Student and Node information needed to perform that 
 * action
 * @author Frank Derry Wanye, Adam Stewart
 * @version 04/14/2015
 *****************************************************************/
public class Action implements Serializable{

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
	 * Obtains a node needed to perform the undo action
	 * @return the node2 is the node that was swapped or inserted
	 *************************************************************/
	public Node<Student> getNode2() {
		return node2;
	}

	/**************************************************************
	 * Obtains the student needed to perform the undo operation
	 * @return the student is the student which was deleted or
	 * updated
	 *************************************************************/
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
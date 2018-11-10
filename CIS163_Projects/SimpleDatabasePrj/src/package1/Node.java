package package1;

import java.io.Serializable;

/***********************************************************************
 * A node that takes a generic type, a next (where it's pointing towards
 * next), and a previous (where it's pointing towards previously).
 * @author Frank Derry Wanye, Adam Stewart
 * @version 04/13/2015
 **********************************************************************/
public class Node<E> implements Serializable{

	/** A default serial that allows the class student data to be
	 * saved and loaded. */
	private static final long serialVersionUID = 1L;

	/** The information (object/number) that goes into the node. */
	private E data;
	
	/** The node the current node is pointing towards next */
	private Node<E> next;
	
	/** The node the current node is pointing towards before it */
	private Node<E> previous;

	/*******************************************************************
	 * Default constructor sets all instance variables to null values.
	 ******************************************************************/
	public Node() {
		this.data = null;
		this.next = null;
		this.previous = null;
	}
	
	/*******************************************************************
	 * Constructor that sets the values based on the parameters inserted
	 * for data, next, and previous.
	 * @param data is a generic type variable that takes any 
	 * object/number.
	 * @param next is a Node<E> that the current node points 
	 * towards next
	 * @param previous is a Node<E> that the current node points 
	 * towards before 
	 ******************************************************************/
	public Node(E data, Node<E> next, Node<E> previous) {
		super();
		this.data = data;
		this.next = next;
		this.previous = previous;
	}
	
	/*******************************************************************
	 * Returns the previous Node<E>.
	 * @return the previous Node<E>
	 ******************************************************************/
	public Node<E> getPrevious() {
		return previous;
	}

	/*******************************************************************
	 * Sets the previous Node<E>.
	 * @param previous Node<E> to be pointed toward previously
	 ******************************************************************/
	public void setPrevious(Node<E> previous) {
		this.previous = previous;
	}

	/*******************************************************************
	 * Returns the data inside the node.
	 * @return the data, generic type
	 ******************************************************************/
	public E getData() {
		return data;
	}

	/*******************************************************************
	 * Sets the nodes data, takes a generic type.
	 * @param data of type E, to be in the Node
	 ******************************************************************/
	public void setData(E data) {
		this.data = data;
	}

	/*******************************************************************
	 * Returns the next Node<E>.
	 * @return the next Node<E>
	 ******************************************************************/
	public Node<E> getNext() {
		return next;
	}

	/*******************************************************************
	 *Sets the next Node<E>.
	 * @param next Node<E> to be pointed toward next
	 ******************************************************************/
	public void setNext(Node<E> next) {
		this.next = next;
	}

	/*******************************************************************
	 * Equals method that checks to see if two Nodes<Student> are equal 
	 * to each other, returning true if it is, false if not.
	 * @param other Node<Student> to be compared to
	 * @return True if equal, false if not
	 ******************************************************************/
	public boolean equals(Node<Student> other){
		return this.data.equals(other.data);
	}
	
}

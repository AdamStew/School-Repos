package package1;

/**********************************************************************
 * An interface that specifies the methods to be implemented by the 
 * database
 * @author Frank Derry Wanye, Adam Stewart
 * @version 04/14/2015
 **********************************************************************/
public interface ISimpleDatabase {

	/*******************************************************************
	 * Inserts a new Node<Student> at the end of the Linked List.
	 * @param student of type Student
	 ******************************************************************/
	void insert(Student student);

	/*******************************************************************
	 * Deletes the first Node<Student> in the LinkedList based off of 
	 * the gNumber inserted.
	 * @param gNumber, a String of the Node<Student> trying to be 
	 * deleted
	 * @return boolean True if successfully deleted, false if not
	 ******************************************************************/
	boolean delete(String gNumber);

	/*******************************************************************
	 * Updates a Node<Student>'s data to the new desired info.  
	 * Can't change the gNumber.
	 * @param gNumber, a String of Node<Student> trying to be updated
	 * @param student, type Student of the information that will be 
	 * updated
	 * @return a boolean value, True if successfully updated, False if 
	 * not
	 ******************************************************************/
	boolean update(String gNumber, Student student);

	/*******************************************************************
	 * Generates a String containing the name, gNumber, and GPA of each 
	 * student in the LinkedList, starting from the top, all the way to 
	 * the bottom.
	 ******************************************************************/
	String display();

	/*******************************************************************
	 * Finds a Student in the LinkedList, based on the gNumber.
	 * @param gNumber, a String of Node<Student> trying to be found
	 * @return A Student, with the matching gNumber
	 ******************************************************************/
	Student find(String gNumber);

	/*******************************************************************
	 * Takes the current LinkedList, and puts it in the complete 
	 * opposite order.  Head and tail switch, along with all the other 
	 * values.
	 ******************************************************************/
	void reverseList();

	/*******************************************************************
	 * Scans the whole LinkedList and deletes any Node<Student> if their
	 * data is the exact same (same name, gNumber, and GPA), so that 
	 * there is only one left.
	 ******************************************************************/
	void removeDuplicates();

	/*******************************************************************
	 * Scans the entire LinkedList, ordering it based on alphabetical 
	 * order of their first names.
	 ******************************************************************/
	void sort();

	/*******************************************************************
	 * Undoes the last performed Action to the LinkedList (Insert, 
	 * Delete, Update, Reverse, RemoveDuplicates, or Sort).
	 * @return a boolean value, True meaning Action was undone 
	 * successfully, False meaning nothing was undone
	 ******************************************************************/
	boolean undo();

	/******************************************************************
	 * A method that loads the database of students from a file
	 * with a specified file name
	 * @param fileName is the name of the file from which the database
	 * is to be loaded
	 *****************************************************************/
	void loadDB(String fileName);

	/******************************************************************
	 * A method that saves off the database to a specified file name
	 * @param fileName is the name of the file to which the database
	 * is to be saved
	 *****************************************************************/
	void saveDB(String fileName);

}

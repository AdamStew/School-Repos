package package1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**********************************************************************
 * Represents a database containing a linked list of students
 * @author Frank Derry Wanye, Adam Stewart
 * @version 04/14/2015
 **********************************************************************/
public class SimpleDatabase implements ISimpleDatabase{

	/** A list containing information about students */
	LinkedList list = new LinkedList();
	
	@Override
	public void insert(Student student) {
		list.insertAtEnd(student);
	}

	@Override
	public boolean delete(String gNumber) {
		return list.delete(gNumber);
	}

	@Override
	public boolean update(String gNumber, Student student) {
		return list.update(gNumber, student);
	}
	
	@Override
	public String display() {
		return list.toString();
	}

	@Override
	public Student find(String gNumber) {
		return list.find(gNumber);
	}

	@Override
	public void reverseList() {
		list.reverseList();
	}

	@Override
	public void removeDuplicates() {
		list.removeDuplicates();
		
	}

	@Override
	public void sort() {
		list.sort();
	}
	
	@Override
	public boolean undo() {
		return list.undo();
	}

	/******************************************************************
	 * A method that loads the database from a specified file name, and
	 * resets the stack of undo operations in the linked list
	 * @param fileName is the name of the file from which the database
	 * is to be loaded
	 *****************************************************************/
	@Override
	public void loadDB(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			list = (LinkedList) is.readObject();
			is.close();
			list.resetStack();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/******************************************************************
	 * Saves the serialized database to a specified file name
	 * @param fileName is the name of the file to which the database
	 * is to be saved
	 *****************************************************************/
	@Override
    public void saveDB(String fileName) {
    	try {
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(list);
			os.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
    }
	
}

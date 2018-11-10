package package1;

import java.io.Serializable;

/**********************************************************************
 * Represents a student who's information is stored in the linked list
 * @author Frank Derry Wanye, Adam Stewart
 * @version 04/14/2015
 **********************************************************************/
public class Student implements Comparable<Student>, Serializable{

	/** A default serial that allows the class student data to be
	 * saved and loaded. */
	private static final long serialVersionUID = 1L;
	
	/** The name of the student must start with a capital letter and
	 * be at least 2 letters long. */
	private String name;
	
	/** The GNumber of the student must start with a "G", followed by
	 * 4 integers. It is the unique identifier of the student */
	private String gNumber;
	
	/** The student's GPA must be between 1.0 and 4.0 */
	private double gpa;
	
	/******************************************************************
	 * Default constructor sets all instance variables to default, 
	 * non-null values. 
	 *****************************************************************/
	public Student() {
		this.name = "";
		this.gNumber = "G0000";
		this.gpa = 0.0;
	}

	/******************************************************************
	 * Constructor sets the student's name, GNumber and GPA to 
	 * specified values
	 * @param name is the name of the student
	 * @param gNumber is the GNumber of the student
	 * @param gpa is the GPA of the student
	 * @throws InvalidArgumentException when the name or GNumber is 
	 * empty, or when the GPA is below 0 or greater than 4
	 *****************************************************************/
	public Student(String name, String gNumber, double gpa) {
		if(name.equals(""))
			throw new InvalidArgumentException();
		else
			this.name = name;
		if(gNumber.equals(""))
			throw new InvalidArgumentException();
		else
			this.gNumber = gNumber;
		if(gpa < 0 || gpa > 4.0)
			throw new InvalidArgumentException();
		else
			this.gpa = gpa;
	}

	/******************************************************************
	 * Obtains the name of the student
	 * @return the name of the student
	 *****************************************************************/
	public String getName() {
		return name;
	}

	/******************************************************************
	 * Sets the name of the student to a specified name String
	 * @param name is the name of the student
	 *****************************************************************/
	public void setName(String name) {
		this.name = name;
	}

	/******************************************************************
	 * Obtains the GNumber of the student
	 * @return the gNumber of the student
	 *****************************************************************/
	public String getgNumber() {
		return gNumber;
	}

	/******************************************************************
	 * Sets the student's GNumber to a specified GNumber String
	 * @param gNumber is the GNumber of the student
	 *****************************************************************/
	public void setgNumber(String gNumber) {
		this.gNumber = gNumber;
	}

	/******************************************************************
	 * Obtains the GPA of the student
	 * @return the gpa of the student
	 *****************************************************************/
	public double getGpa() {
		return gpa;
	}

	/******************************************************************
	 * Sets the student's GPA to a specified gpa double
	 * @param gpa is the student's GPA
	 *****************************************************************/
	public void setGpa(double gpa) {
		this.gpa = gpa;
	}

	/******************************************************************
	 * Compares this student's name to that of another student, and 
	 * returns a negative value if this student's name is 
	 * alphabetically before the other student's name, a positive value
	 * if this student's name is alphabetically after the other 
	 * students', and 0 if they have the same name
	 * @param other is the other student to which this student is 
	 * compared
	 * @return an integer value
	 *****************************************************************/
	@Override
	public int compareTo(Student other) {
		return this.name.compareTo(other.name);
	}
	
	/******************************************************************
	 * Converts this student to a string representation of the form
	 * "name, GNumber, GPA; " and starts a new line of text
	 * @return a String representing this student
	 *****************************************************************/
	public String toString() {
		return name + ", " + gNumber + ", " + gpa + "; \n";
	}
	
	/******************************************************************
	 * Returns true if the name, GNumber and GPA of this student are
	 * exactly the same as those of another student, returns false
	 * otherwise
	 * @param other is the other student
	 * @return true or false
	 *****************************************************************/
	public boolean equals(Student other) {
		return this.name.equals(other.name) && this.gNumber.equals(
				other.gNumber) && this.gpa == other.gpa;
	}
	
}


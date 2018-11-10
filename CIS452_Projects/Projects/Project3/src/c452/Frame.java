package c452;

/********************************************************************************************************************
 * @author Adam Stewart																								*
 * 																													*
 * A 'Frame' object, used to represent some traits of what a Frame in physical memory is like.  Solely a 			*
 * representative for simulation purposes.																			*
 ********************************************************************************************************************/
public class Frame {

	private int processNum; //The Process' numerical value that owns the Page in this Frame.
	private int pageNum; //The Page number that is currently residing in this Frame.
	private boolean valid; //If the page is valid or not.
	
	/****************************************************************************************************************
	 * Constructor to create a Frame with all default traits, such as: Page number = -1, Process number = -1; and 	*
	 * valid = false.  Can be changed later, obviously.																*
	 ****************************************************************************************************************/
	public Frame() {
		this.pageNum = -1;
		this.processNum = -1;
		this.valid = false;
	}
	
	/****************************************************************************************************************
	 * Constructor to create a Frame with the Page number value of pageNum and Process number value of processNum.	*
	 * Validity is automatically set to true.																		*
	 * 																												*
	 * @param int processNum - Numerical value that owns the Page located in this Frame.							*
	 * @param int pageNum - Numerical value used to represent the Page residing in this Frame.						*
	 ****************************************************************************************************************/
	public Frame(int processNum, int pageNum) {
		this.pageNum = pageNum;
		this.processNum = processNum;
		this.valid = true;
	}
	
	/****************************************************************************************************************
	 * Gets the Process number of the Process who owns the Page in this Frame.  May return -1 if Frame is invalid.	*
	 * 																												*
	 * @return int - Numerical value of the Process who's Page resides in this Frame.								*
	 ****************************************************************************************************************/
	public int getProcessNum() {
		if(this.valid)
			return this.processNum;
		return -1;
	}
	
	/****************************************************************************************************************
	 * Gets the Page's number that currently resides in this Frame.  May return -1 if Frame is invalid.				*
	 * 																												*
	 * @return int - Numerical value of the Page that currently resides in this Frame.								*
	 ****************************************************************************************************************/
	public int getPageNum() {
		if(this.valid)
			return this.pageNum;
		return -1;
	}
	
	/****************************************************************************************************************
	 * Checks to see if the current Frame is valid or not.															*
	 * 																												*
	 * @returns boolean - True if it's valid, False if it's not.													*
	 ****************************************************************************************************************/
	public boolean checkValidity() {
		return this.valid;
	}
	
	/****************************************************************************************************************
	 * Sets the validity of the Frame to 'valid'.																	*
	 * 																												*
	 * @param boolean valid - The boolean value of you're setting the Page to.										*
	 ****************************************************************************************************************/
	public void setValidity(boolean validity) {
		this.valid = validity;
	}
	
	/****************************************************************************************************************
	 * Sets the Frame's Page number and Process number to whatever the user sets to.  Also automatically changes 	*
	 * the validity to true.																						*
	 * 																												*
	 * @param int processNum - Numerical value that owns the Page located in this Frame.							*
	 * @param int pageNum - Numerical value used to represent the Page residing in this Frame.						*
	 ****************************************************************************************************************/
	public void setFrame(int processNum, int pageNum) {
		this.processNum = processNum;
		this.pageNum = pageNum;
		this.valid = true;
	}
	
	/****************************************************************************************************************
	 * Checks to see if two Frames are equal to each other.  This is determined by comparing the Process number, 	*
	 * Page number, and validity of each Frame.  This Overrides the native .equals method.							*
	 * 																												*
	 * @return boolean - Returns true if both Frames are equal and false if both Frames are not equal.				*
	 ****************************************************************************************************************/
	@Override
	public boolean equals(Object obj) {
		if(obj == null) 
			return false;
		Frame frame = (Frame) obj;
		if(this.processNum == frame.getProcessNum() && this.pageNum == frame.pageNum 
				&& this.valid == frame.checkValidity())
			return true;
		else
			return false;
	}
	
}

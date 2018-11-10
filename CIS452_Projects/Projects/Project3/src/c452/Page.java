package c452;

/********************************************************************************************************************
 * @author Adam Stewart																								*
 * 																													*
 * A 'Page' object, used to represent some traits of what a Page in memory is like.  Solely a representative for	*
 * simulation purposes.																								*
 ********************************************************************************************************************/
public class Page {

	private int frameNumLocation; //Location is physical memory, or on the Frame Table.
	private boolean valid; //If the page is valid or not.
	
	/****************************************************************************************************************
	 * Constructor for a Page. It'll be a page for whatever Process processNum is, with a default valid value of 	*
	 * false.  Can be changed manually easily.																		*
	 ****************************************************************************************************************/
	public Page() {
		this.frameNumLocation = -1;
		this.valid = false;
	}
	
	/****************************************************************************************************************
	 * Checks to see if the current Page is valid or not.															*
	 * 																												*
	 * @returns boolean - True if it's valid, False if it's not.													*
	 ****************************************************************************************************************/
	public boolean checkValidity() {
		return this.valid;
	}
	
	/****************************************************************************************************************
	 * Gets the Page's location in Physical Memory / Frame Table.  Value may be blank, or -1, if not located on the	*
	 * Frame Table.  Also may return -1 if data isn't valid.														*
	 * 																												*
	 * @returns int - Returns the numerical value of where this Page lies on the Frame Table.						*
	 ****************************************************************************************************************/
	public int getFrameNumLocation() {
		if(this.valid)
			return this.frameNumLocation;
		return -1;
	}
	
	/****************************************************************************************************************
	 * Sets the validity of the Page to 'valid'.																	*
	 * 																												*
	 * @param boolean valid - The boolean value of you're setting the Page to.										*
	 ****************************************************************************************************************/
	public void setValidity(boolean valid) {
		this.valid = valid;
	}
	
	/****************************************************************************************************************
	 * Sets the location of this Page in Physical Memory / Frame Table.  Should set it to -1, if you're "removing"	*
	 * it.  Also automatically changes validity to true.															*
	 * 																												*
	 * @param int frameNumLocation - The numerical value of where this Page is in the Frame Table.  -1 if it isn't.	*
	 ****************************************************************************************************************/
	public void setFrameNumLocation(int frameNumLocation) {
		this.frameNumLocation = frameNumLocation;
		this.valid = true;
	}
	
}

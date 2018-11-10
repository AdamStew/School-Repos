package c452;

import java.util.ArrayList;

/********************************************************************************************************************
 * @author Adam Stewart																								*
 *																													*
 * A 'Free Frame List' object, used to represent some traits of what a Frame Frame List in paging is like.  Solely	*
 * a representative for simulation purposes.																		*
 ********************************************************************************************************************/
public class FreeFrameList {

	private ArrayList<Integer> ffl; //This will keep track of the indices of free Frames.
	
	/****************************************************************************************************************
	 * Constructor that will create an ArrayList with all indices being true for all of size.  Size should be equal	*
	 * to the size of the Frame Table that this corresponds with.													*
	 * 																												*
	 * @param int size - How many Frames are initially free.  This should be equal to the size of the Frame Table.	*
	 ****************************************************************************************************************/
	public FreeFrameList(int size) {
		this.ffl = new ArrayList<Integer>();
		for(int i=0; i < size; i++) {
			this.ffl.add(i); //Initially every spot is free.
		}
	}
	
	/****************************************************************************************************************
	 * Obtain and removes a free Frame index.  If there is no free Frames, it'll return -1.							*
	 * 																												*
	 * @return int - THe index of a free Frame.  It'll return -1 if there are none.									*
	 ****************************************************************************************************************/
	public int getFreeFrameIndex() {
		if(this.ffl.size() > 0)
			return this.ffl.remove(0);
		return -1;
	}
	
	/****************************************************************************************************************
	 * Adds a free Frame index to our list.  This technically should only be called if we had Processes finishing	*
	 * or timing out.																								*
	 * 																												*
	 * @param int frameIndex - Numerical value of the open Frame index we're adding to our Free Frame List.			*
	 ****************************************************************************************************************/
	public void addFreeFrameIndex(int frameIndex) {
		this.ffl.add(frameIndex);
	}
	
	/****************************************************************************************************************
	 * Resets the Free Frame List.  Basically empties it, and reinstantiates it to the value of size.  Again, the 	*
	 * size should be equal to the size of the Frame Table.															*
	 * 																												*
	 * @param int size - How many Frames are initially free.  This should be equal to the size of the Frame Table.	*
	 ****************************************************************************************************************/
	public void resetFreeFrameLits(int size) {
		this.ffl.clear();
		for(int i=0; i < size; i++) {
			this.ffl.add(i);
		}
	}
	
	/****************************************************************************************************************
	 * Prints the Free Frame List in a somewhat readable format. It'll let you know if the list is empty.			*
	 ****************************************************************************************************************/
	public void print() {
		if(this.ffl.size() > 0) {
			System.out.println("*FREE FRAME TABLE*");
			System.out.println("Free Frame Number");
			for(int i=0; i < this.ffl.size(); i++) {
			System.out.println(this.ffl.get(i));
			}
		} else
			System.out.println("No free frames currently.");
	}
	
}

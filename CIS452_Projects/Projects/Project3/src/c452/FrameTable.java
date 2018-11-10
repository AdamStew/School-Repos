package c452;

import java.util.LinkedList;
import java.util.Queue;

/********************************************************************************************************************
 * @author Adam Stewart																								*
 *																													*
 * A 'Frame Table' object, used to represent some traits of what a Frame Table in paging is like.  Solely a 		*
 * representative for simulation purposes.  Also keeps track of LRU and Free Frame List here.						*
 ********************************************************************************************************************/
public class FrameTable {

	private Frame ft[]; //Structure of the FrameTable.  See constructor.
	private Queue<Frame> lru; //This will keep track of the LRU candidate, assuming it's full.  HEAD == VICTIM.
	private FreeFrameList ffl; //This will keep track of what Frames are free, so we don't have to scan.
	private boolean pageFaultTriggered; //This will denote if a Page Fault was just triggered from the last insert.
	
	/****************************************************************************************************************
	 * Constructor for a Frame Table that will be created in size-rows, which will keep track of what Frame each 	*
	 * Page is located on.  Also creates an empty Queue that will keep track of LRU replacement, and a Free Frame	*
	 * List, which will keep track of what Frames are currently open (that way we don't have to scan through the 	*
	 * Frame Table).  All Frames are initially invalid.																*
	 * 																												* 
	 * @param int size - Number of rows the PageT able, which will be equal the max value the LRU Queue can get.	*
	 ****************************************************************************************************************/
	public FrameTable(int size) {
		this.ft = new Frame[size];
		this.lru = new LinkedList<Frame>();
		this.ffl = new FreeFrameList(size);
		this.pageFaultTriggered = false;
		
		for(int i=0; i<size; i++) {
			this.ft[i] = new Frame();
		}
	}
	
	/****************************************************************************************************************
	 * Gets the Page number of the desired Frame based on frameNum.  Returns -1 if the Frame wasn't valid.			*
	 * 																												*
	 * @param int frameNum - Numerical value of the Frame you're trying to get the Page number of.					*
	 * @return int - Numerical value of the Page number in this Frame.  May be -1 if the Frame is invalid.			*
	 ****************************************************************************************************************/
	public int getPageNum(int frameNum) {
		if(this.ft[frameNum].checkValidity())
			return this.ft[frameNum].getPageNum();
		return -1;
	}
	
	/****************************************************************************************************************
	 * Gets the Process number who owns the Page inside the desired Frame based on frameNum.  Returns -1 if the 	*
	 * Frame wasn't valid.																							*
	 * 																												*
	 * @param int frameNum - Numerical value of the Frame you're tring to get the Page number of.					*
	 * @return int - Numerical value of the Process number who owns the Page inside the desired Frame.  May be -1.	*
	 ****************************************************************************************************************/
	public int getProcessNum(int frameNum) {
		if(this.ft[frameNum].checkValidity())
			return this.ft[frameNum].getProcessNum();
		return -1;
	}
	
	/***************************************************************************************************************&
	 * Gets Frame Table data and formats in an easier to access 2D-Array.  [i][0] = Process Numbers 				*
	 * [i][1] = Page Numbers																						*
	 * 																												*
	 * @return Integer[][] - Frame Table data. [i][0] -> procNums [i][1] -> pageNUms								*
	 ****************************************************************************************************************/
	public Integer[][] getFrameTableData() {
		Integer[][] data = new Integer[this.ft.length][2];
		for(int i=0; i < this.ft.length; i++) {
			if(this.ft[i].checkValidity()) {
				data[i][0] = this.ft[i].getProcessNum();
				data[i][1] = this.ft[i].getPageNum();
			}
		}
		return data;
	}
	
	/****************************************************************************************************************
	 *  Adds to the FrameTable the location of pageNum in physical memory based on LRU.  Sets valid to 1, if 		*
	 *  it wasn't already.																							*
	 *  																											*
	 *  @param int processNum - The Process's numerical value that owns the desired page.							*
	 *  @param int pageNum - The Page number you're setting the location of.										*
	 *  @return int - Returns the index on the Frame Table where this new Page was set to.							*
	 ****************************************************************************************************************/
	public int setFrameNum(int processNum, int pageNum) {
		int freeFrameIndex;
		Frame frame = new Frame(processNum, pageNum);
		
		//First and foremost, we need to see if this Page is already in our Frame Table!
		if((freeFrameIndex = checkFrameTableIfExists(processNum, pageNum)) != -1) { 
			reuseFrame(frame); //If it does exist, then put where it is on the LRU Queue at the tail.
			this.pageFaultTriggered = false;
			return freeFrameIndex;
		} else if((freeFrameIndex = ffl.getFreeFrameIndex()) != -1) { //Check to see if there is an open Frame on FFL.
			this.ft[freeFrameIndex].setFrame(processNum, pageNum); //If there is, just take the first slot.
			this.lru.add(frame); //Since there's a slot, there's no way our LRU is full, just add the new Frame.
			this.pageFaultTriggered = true;
			return freeFrameIndex;
		} else { //If there's no free Frames either, then we must boot the least recently used!
			Frame victim = this.lru.poll(); //Remove the victim.
			this.lru.add(frame); //Put in new tail.
			int victimIndex = getFrameIndex(victim); //Get the index on the Frame Table of the victim.
			this.ft[victimIndex].setFrame(processNum, pageNum); //Set it to the new Frame.
			this.pageFaultTriggered = true;
			return victimIndex;
		}
	}
	
	/****************************************************************************************************************
	 * Goes through the LRU Queue, and determines the LRU victim.  If there isn't enough valid Frames for a victim,	*
	 * then returns null.																							*
	 * 																												*
	 * @return Frame - Returns the Frame that is the current victim.  Returns null if there is no victim.			*
	 ****************************************************************************************************************/
	public Frame getLRUVictim() {
		if(this.lru.size() == this.ft.length) { //Check to see if LRU Queue is even full.
			return this.lru.peek();
		} else {
			return null; //If there is no victim, return null.
		}
	}
	
	/****************************************************************************************************************
	 * Checks to see if a Page Fault was just triggered.  True if it was, False if it wans't.						*
	 * 																												*
	 * @return boolean - True if a Page Fault was triggered, otherwise False.										*
	 ****************************************************************************************************************/
	public boolean isPageFaultTriggered() {
		return this.pageFaultTriggered;
	}
	
	/****************************************************************************************************************
	 * Resets the value of the Page Fault trigger, incase it ever needs to manually change back.					*
	 ****************************************************************************************************************/
	public void resetPageFaultTrigger() {
		this.pageFaultTriggered = false;
	}
	
	/****************************************************************************************************************
	 * Prints the Frame Table, Free Frame List, and the LRU Queue in somewhat readable formats.						*
	 ****************************************************************************************************************/
	public void print() {
		System.out.println("*Physical Memory / Frame Table*");
		System.out.println("Frame #   ProcID   Page #"); //These are supposed to be columns.
		for(int i=0; i < this.ft.length; i++) {
			if(i < 10) 
				System.out.println(i + "         " + this.ft[i].getProcessNum() + "        " + this.ft[i].getPageNum());
			else
				System.out.println(i + "        " + this.ft[i].getProcessNum() + "        " + this.ft[i].getPageNum());
				
		}
		System.out.println("");
		this.ffl.print();
		System.out.println("");
		System.out.println("*LRU*");
		boolean victim = true;
		
		if(this.lru.size() != this.ft.length)
			System.out.println("***NOT FULL YET***");
		for(Frame f : this.lru) {
			if(victim)
				System.out.println("Proc: " + f.getProcessNum() + "   Page: " + f.getPageNum() + " <--VICTIM");
			else 
				System.out.println("Proc: " + f.getProcessNum() + "   Page: " + f.getPageNum());
			victim = false;
		}
	}
	
	/****************************************************************************************************************
	 * Checks to see if a specific Page from a specific Process is already in the Frame Table by comparing the  	*
	 * Page number, Process number, and validity.																	*
	 * 																												*
	 * @param int processNum - The Process's numerical value that is checking a specific page.						*
	 * @param int pageNum - The Page number that you're checking exists in the Frame Table (from a desired Process) *
	 * @return int - Returns the index value in the Frame Table in which it exists.  If it doesn't exist, then -1.	*
	 ****************************************************************************************************************/
	public int checkFrameTableIfExists(int processNum, int pageNum) {
		for(int i=0; i < this.ft.length; i++) {
			if(this.ft[i].checkValidity() && this.ft[i].getProcessNum() == processNum 
					&& this.ft[i].getPageNum() == pageNum)
				return i;
		}
		return -1;
	}
	
	/****************************************************************************************************************
	 * "Reuses" a Frame by removing it from wherever it is in the LRU Queue, and putting it at the tail, since it 	*
	 * was just recently used.  This method should only be called if you checked to see if it's on the Frame Table	*
	 * first.																										*
	 * 																												*
	 * @param Frame frame - Moves this specific Frame in the LRU Queue.												*
	 ****************************************************************************************************************/
	private void reuseFrame(Frame frame) {
		this.lru.remove(frame);
		this.lru.add(frame);
	}
	
	/****************************************************************************************************************
	 * Locates the index of the desired Frame on the Frame Table.  If it cannot be located, it returns -1.			*
	 * 																												*
	 * @param Frame frame - Frame in which you're trying to locate the index of.									*
	 * @return int - Numerical value of the index.  Returns -1 if it wasn't found.									*
	 ****************************************************************************************************************/
	private int getFrameIndex(Frame frame) {
		for(int i=0; i < this.ft.length; i++) {
			if(this.ft[i].equals(frame))
				return i;
		}
		return -1;
	}
	
}

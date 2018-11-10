package c452;

/********************************************************************************************************************
 * @author Adam Stewart																								*
 *																													*
 * A 'Page Table' object, used to represent some traits of what a Page Table in paging is like.  Solely a 			*
 * representative for simulation purposes.																			*
 ********************************************************************************************************************/
public class PageTable {

	private Page[] pt; //Structure of Page Table.  See constructor.
	private int processNum; //The process number of this Page Table.
	private int pageFaultCounter; //The counter for this Page Table that will keep track of the Page Faults.
	
	/****************************************************************************************************************
	 * Constructor for a Page Table that will be created in size-rows, which will keep track of what Frame each 	*
	 * Page is located on.  Initially sets validity to false, since there's no actual data yet.						*
	 * 		
	 * @param int processNum - The Process number of this Page Table.																										* 
	 * @param int size - Number of rows the Page Table is.															*
	 ****************************************************************************************************************/
	public PageTable(int processNum, int size) {
		this.processNum = processNum;
		this.pt = new Page[size];
		this.pageFaultCounter = 0;
		
		for(int i=0; i<size; i++) {
			this.pt[i] = new Page();
		}
	}

	/****************************************************************************************************************
	 * Checks to see if the data in the Page Table is valid.  If it is, returns the Frame number in which the Page 	*
	 * is located.																									*
	 * 																												*
	 * @param int pageNum - The Page number in which you're desiring to receive the Frame number of.				*
	 ****************************************************************************************************************/
	public int getFrameNumLocation(int pageNum) {
		if(this.pt[pageNum].checkValidity())
			return this.pt[pageNum].getFrameNumLocation();
		return -1;
	}
	
	/****************************************************************************************************************
	 * Gets the Process number (basically the owner) of this Page Table.  Doesn't matter if the data is valid or	*
	 * not.																											*
	 * 																												*
	 * @return int - Returns the numerical value of the Process number that currently owns this Page Table.			*
	 ****************************************************************************************************************/
	public int getProcessNum() {
		return this.processNum;
	}
	
	/****************************************************************************************************************
	 * Gets the number of times this Page Table / Process has experienced a Page Fault.								*
	 * 																												*
	 * @return int- The numerical value of how many times this Page Table / Process has experienced a Page Fault.	*
	 ****************************************************************************************************************/
	public int getPageFaultCounter() {
		return this.pageFaultCounter;
	}
	
	/***************************************************************************************************************&
	 * Gets Page Table data and formats in an easier to access array.  [i] = Frame Numbers 							*																				*
	 * 																												*
	 * @return Integer[] - Page Table data. [i] = Frame Numbers														*
	 ****************************************************************************************************************/
	public Integer[] getPageTableData() {
		Integer[] data = new Integer[this.pt.length];
		for(int i=0; i < this.pt.length; i++) {
			if(this.pt[i].checkValidity())
				data[i] = this.pt[i].getFrameNumLocation();
		}
		return data;
	}
	
	/****************************************************************************************************************
	 *  Adds to the Page Table the location of pageNum in physical memory based on frameNum.  Sets valid to true,  	*
	 *  if it wasn't already.																						*
	 *  																											*
	 *  @param int pageNum - The Page number you're setting the location of.										*
	 *  @param int frameNum - The Frame number that the Page number is located.										*
	 ****************************************************************************************************************/
	public void setPageNum(int pageNum, int frameNum) {
		this.pt[pageNum].setFrameNumLocation(frameNum);
		this.pt[pageNum].setValidity(true);
	}
	
	/****************************************************************************************************************
	 * Resets the value of the Page Fault counter in this Page Table (back to 0).									*
	 ****************************************************************************************************************/
	public void resetPageFaultCounter() {
		this.pageFaultCounter = 0;
	}
	
	/****************************************************************************************************************
	 * Increments the Page Fault counter by one for this Page Table / Process.										*
	 ****************************************************************************************************************/
	public void incrementPageFaultCounter() {
		this.pageFaultCounter++;
	}
	
	/****************************************************************************************************************
	 * Prints out the Page Table in a somewhat readable format.  It will only print the Pages that are valid.  Or	* 
	 * didn't have their Frame number location kicked off by LRU.													*
	 ****************************************************************************************************************/
	public void print() {
		System.out.println("*PAGE TABLE*");
		System.out.println("Process " + this.processNum + ": ");
		System.out.println("Page    Frame"); //These are supposed to be "columns".
		for(int i=0; i < this.pt.length; i++) {
			if(this.pt[i].checkValidity() && this.pt[i].getFrameNumLocation() != -1)
				System.out.println(i + "       " + this.pt[i].getFrameNumLocation());	
		}
		System.out.println("");
	}
	
}

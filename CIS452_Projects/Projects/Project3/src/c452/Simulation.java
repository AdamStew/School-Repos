package c452;

import java.io.*;
import java.util.*;

/********************************************************************************************************************
 * @author Adam Stewart																								*
 *																													*
 * A Simulation object used to run and keep tabs on a current simulation of a Page Replacement System.				*
 ********************************************************************************************************************/
public class Simulation {

	private static final int PAGE_SIZE = 1024; //Size of each Page in this simulation.
	private static final int FRAME_TABLE_SIZE = 16; //Size of each Frame Table in this simulation.
	private static final int PAGE_TABLE_SIZE = 64; //Size of each Page Table in this simulation.
	
	private ArrayList<String> data; //ArrayList to keep track of all of the instructions in the .data file.
	private HashMap<String, List<String>> references; //HashMap (basically MultiMap) to keep track of what references
													  //each process made.
	private FrameTable ft; //Frame Table to keep track of physical memory locations of the Pages.
	private int frameTableSize; //Integer value to keep track of the size of the Frame Table.
	private PageTable[] ptList; //Array of Page Tables to keep track of each Process' info.
	private int pageTableSize; //Integer value to keep track of the size of the Page Tables.
	private int index; //Integer value that will keep track of what index in the data array we are on.
	private boolean completed; //Boolean that will determine if a simulation is currently completed or not.
	
	/****************************************************************************************************************
	 * Creates a Page Replacement Simulation based on the desired size of the Page Table and desired size of the 	*
	 * Frame Table.  Certain data will be kept track of automatically such as number of references and what 		*
	 * instruction we're on.																						*
	 * 																												*
	 * @param int pageTableSize - Desired size of the Page Table (should be power of 2).								*
	 * @param int frameTableSize - Desired size of the Frame Table (should be a power of 2).							*
	 ****************************************************************************************************************/
	public Simulation(int pageTableSize, int frameTableSize) {
		this.data = new ArrayList<String>();
		this.references = new HashMap<String, List<String>>();
		this.ft = new FrameTable(frameTableSize);
		this.pageTableSize = pageTableSize;
		this.frameTableSize = frameTableSize;
		this.index = 0;
		this.completed = false;
	}
	
	/****************************************************************************************************************
	 * This takes in a path for a file.  File should be of type .data.  All of the data is checked and data 		*
	 * structures are used accordingly to fit the number of processes referenced.  This also fills the data array.	*
	 * 																												*
	 * @param String filePath - Path to the desired .data file.														*
	 ****************************************************************************************************************/
	public void receiveData(String filePath) {
		File file = new File(filePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			//Read entire file, store in an ArrayLIst.
			String line = br.readLine();
			String[] part;
			while(line != null) {
				this.data.add(line);
				part = line.split(":");
				List<String> temp = this.references.get(part[0]);
				if(temp == null) { //Check if a list for that process exists, if not, create one.
					temp = new ArrayList<String>();
				}
				temp.add(part[1].trim());
				this.references.put(part[0], temp);
				line = br.readLine();
			}
			br.close();
			
			//Create an array of Page Tables to hold a table for each Process in the file.
			this.ptList = new PageTable[this.references.size()];
			for(int i=0; i < this.ptList.length; i++) {
				ptList[i] = new PageTable(i,this.pageTableSize);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/****************************************************************************************************************
	 * Checks to see if the current simulation is over.																*
	 * 																												*
	 * @return boolean - True if simulation is complete, False if not.												*
	 ****************************************************************************************************************/
	public boolean isCompleted() {
		return this.completed;
	}
	
	/**
	 * 
	 */
	public void resetSimulation() {
		this.completed = false;
		this.index = 0;
	}
	
	/****************************************************************************************************************
	 * Returns the total number of Processes in the currently running simulation.									*
	 * 																												*
	 * @return int - Numerical value of the number of processes in this current simulation.							*
	 ****************************************************************************************************************/
	public int getTotalProcesses() {
		return this.ptList.length;
	}
	
	/****************************************************************************************************************
	 * Returns the size of the Frame Table in the current simulation.												*
	 * 																												*
	 * @return int - Numerical value of the size of the current Frame Table.										*
	 ****************************************************************************************************************/
	public int getFrameTableSize() {
		return this.frameTableSize;
	}
	
	/****************************************************************************************************************
	 * Returns the size of the Page Tables in the current simulation.												*
	 * 																												*
	 * @return int - Numerical value of the size of the current Page Tables.										*
	 ****************************************************************************************************************/
	public int getPageTableSize() {
		return this.pageTableSize;
	}
	
	/****************************************************************************************************************
	 * Returns the current Frame Table within the simulation.														*
	 * 																												*
	 * @return FrameTable - Returns the FrameTable at any given point in the simulation.							*
	 ****************************************************************************************************************/
	public FrameTable getFrameTable() {
		return this.ft;
	}
	
	/****************************************************************************************************************
	 * Returns information in the Frame Table in an easy to access 2D-array, which simply displays a value of the	*
	 * [processNum][pageNum], assuming it's valid.  Just easy access to certain data.								*
	 * 																												*
	 * @return Integer[][] - Returns 2D-array of FrameTable data based on [processNum][pageNum].					*
	 ****************************************************************************************************************/
	public Integer[][] getFrameTableData() {
		return this.ft.getFrameTableData();
	}
	
	/****************************************************************************************************************
	 * Returns information in the Page Table in an easy to access array, which simply displays a value of the		*
	 * [frameNum], assuming it's valid.  Just easy access to certain data.											*
	 * 																												*
	 * @param int processNum - THe Process number of the desired Page Table in which you're desiring data from.		*
	 * @return Integer[] - Returns an array of of PageTable data based on [frameNum].								*
	 ****************************************************************************************************************/
	public Integer[] getPageTableData(int processNum) {
		if(this.ptList.length >= processNum)
			return this.ptList[processNum].getPageTableData();
		return null;
	}
	
	/****************************************************************************************************************
	 * Returns the next instruction in the data file in a String format.  Says what process is accessing what page.	*
	 * 																												*
	 * @return String - Returns a String of the next instruction in the format of "Process P# accessing page pg#".	*
	 ****************************************************************************************************************/
	public String getExecutionCode() {
		if(this.index < this.data.size()) {
			String part[] = this.data.get(this.index).split(":");
			return "Process " + part[0] + " accessing page " + Integer.parseInt(part[1].trim().substring(1), 2);
		}
		return "Simulation complete.";
	}
	
	/****************************************************************************************************************
	 * Takes a step in the simulation, assuming the simulation isn't complete.  Checks to see if we have a victim	*
	 * and that we need to kick someone out of the Frame Table.  If we necessary, remove them from the Frame Table,	*
	 * update his Page Table, and then take their old spot.  Otherwise, just take a free spot, and notify that 		*
	 * Page Table that they've caused another Page Fault, if necessary.												*
	 ****************************************************************************************************************/
	public void step() {
		if(this.index < this.data.size()) {
			String part[] = this.data.get(this.index).split(":");
			int pageNum = Integer.parseInt(part[1].trim(), 2); //Get the Page num from the file's line. 
			int processNum = Integer.parseInt(part[0].trim().substring(1)); //Get the Process num from the file's line.
			System.out.println("Process " + part[0] + " accessing page " + pageNum + ". ");
			Frame victim = ft.getLRUVictim();
			if(victim != null && this.ft.checkFrameTableIfExists(processNum, pageNum) == -1) //If we have a victim 
			//and the current process/pageNum isn't already in our table, then remove his spot on his Page Table.
				{ptList[victim.getProcessNum()].setPageNum(victim.getPageNum(), -1); System.out.println("VIctim!");}
			int frameNum = ft.setFrameNum(processNum, pageNum);
			ptList[processNum].setPageNum(pageNum, frameNum);
			if(ft.isPageFaultTriggered()) //If we triggered a Page Fault, increment the counter for that process.
				ptList[processNum].incrementPageFaultCounter();
			this.index++;
		} else {
			this.completed = true;
		}
	}
	
	/****************************************************************************************************************
	 * Takes as many steps as necessary to find a Page Fault.  When it finds one, it stops right before it.			*
	 ****************************************************************************************************************/
	public void stepUntilFault() {
		boolean broke = false;
		while(!broke && this.index < this.data.size()) { 
			String part[] = this.data.get(this.index).split(":");
			int pageNum = Integer.parseInt(part[1].trim(), 2); //Get the Page num from the file's line. 
			int processNum = Integer.parseInt(part[0].trim().substring(1)); //Get the Process num from the file's line.
			if(ft.checkFrameTableIfExists(processNum, pageNum) != -1) { //First, check if it'll fault.
				System.out.println("Process " + part[0] + " accessing page " + pageNum + ". ");
				int frameNum = ft.setFrameNum(processNum, pageNum);
				ptList[processNum].setPageNum(pageNum, frameNum);
				this.index++;
			} else //If it faults, stop.
				broke = true;
		}
		if(!broke) //If we escaped the loop without breaking, then we completed.
			this.completed = true;
	}
	
	/******************************************************************************************************************
	 * Code keep stepping, but stops AFTER it hits a fault (as opposed to just before Faulting).
	 * public void stepUntilFault() {
	 *	boolean broke = false;
	 *	while(!broke && this.index < this.data.size()) { 
	 *		String part[] = this.data.get(this.index).split(":");
	 *		int pageNum = Integer.parseInt(part[1].trim(), 2); //Get the Page num from the file's line. 
	 *		int processNum = Integer.parseInt(part[0].trim().substring(1)); //Get the Process num from the file's line.
	 *		System.out.println("Process " + part[0] + " accessing page " + pageNum + ". ");
	 *		Frame victim = ft.getLRUVictim();
	 *		if(victim != null) //If we have a victim, remove his spot on his Page Table.
	 *			ptList[victim.getProcessNum()].setPageNum(victim.getPageNum(), -1);
	 *		int frameNum = ft.setFrameNum(processNum, pageNum);
	 *		ptList[processNum].setPageNum(pageNum, frameNum);
	 *		if(ft.isPageFaultTriggered()) { //If we triggered a Page Fault, increment the counter for that process.
	 *			ptList[processNum].incrementPageFaultCounter();
	 *			broke = true; //We found the page fault we desired, so quit looking.
	 *		}
	 *		this.index++;
	 *	}
	 *	if(!broke) //If we escaped the loop without breaking, then we completed.
	 *		this.completed = true;
	 *	}
	 ***************************************************************************************************************** /
	
	/****************************************************************************************************************
	 * Just run until we have no more instructions in our data array to run.  Even if we Page Fault, keep running.	*
	 ****************************************************************************************************************/
	public void stepUntilCompleted() {
		while(this.index < this.data.size()) { 
			String part[] = this.data.get(this.index).split(":");
			int pageNum = Integer.parseInt(part[1].trim(), 2); //Get the Page num from the file's line. 
			int processNum = Integer.parseInt(part[0].trim().substring(1)); //Get the Process num from the file's line.
			System.out.println("Process " + part[0] + " accessing page " + pageNum + ". ");
			Frame victim = ft.getLRUVictim();
			if(victim != null && this.ft.checkFrameTableIfExists(processNum, pageNum) == -1)//If we have a victim 
				//and the current process/pageNum isn't already in our table, then remove his spot on his Page Table.
				ptList[victim.getProcessNum()].setPageNum(victim.getPageNum(), -1);
			int frameNum = ft.setFrameNum(processNum, pageNum);
			ptList[processNum].setPageNum(pageNum, frameNum);
			if(ft.isPageFaultTriggered()) //If we triggered a Page Fault, increment the counter for that process.
				ptList[processNum].incrementPageFaultCounter();
			this.index++;
		}
		this.completed = true;
	}
	
	/****************************************************************************************************************
	 * Just prints off all of the status information from Page Tables, Frame Tables, Free Frame List, and the LRU.	*
	 ****************************************************************************************************************/
	public void printStatus() {
		for(int i=0; i < ptList.length; i++) {
			this.ptList[i].print();
		}
		this.ft.print();
	}
	
	/****************************************************************************************************************
	 * Only printable if our simulation has officially completed.  Prints off final statistics of the simulation	*
	 * such as KB of total pages used, total number of references, and total number of Page Faults for each 		*
	 * process.																										*
	 * 																												*
	 * @return String - Gigantic String of all end-simulation data for each process.								*
	 ****************************************************************************************************************/
	public String printFinalStats() {
		String finalString = new String();
		if(this.completed) {
			int i = 0;
			System.out.println("**FINAL STATS**");
			finalString += "**FINAL STATS**\n";
			for(String key : references.keySet()) {
				List<String> totalReferences = references.get(key);
				Set<String> uniquePages = new HashSet<String>(totalReferences); //Sets don't allow duplicates.
				
				//Print total size (in pages) of each process and total number of memory references of each process.
				System.out.println("Process " + key + " has " + uniquePages.size() * PAGE_SIZE + " KB of pages, with "
						+ totalReferences.size() + " total memory reference(s), and "+ ptList[i].getPageFaultCounter() 
						+ " page fault(s). ");
				finalString += ("Process " + key + " has " + uniquePages.size() * PAGE_SIZE + " KB of pages, with "
						+ totalReferences.size() + " total memory reference(s), and "+ ptList[i].getPageFaultCounter() 
						+ " page fault(s). \n");
				i++;
			}
		}
		return finalString;
	}
	
}

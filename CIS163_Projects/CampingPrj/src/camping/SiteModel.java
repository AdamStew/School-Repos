package camping;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/***********************************************************************
 * Represents a table of sites that gets returns into the GUI, along 
 * with all of the methods that manipulate the table
 * @author Frank Derry Wanye, Adam Stewart
 * @version 03/17/2015
 **********************************************************************/
public class SiteModel extends AbstractTableModel {

	/** ArrayList of Sites, that contain all of the added Sites */
	private ArrayList<Site> listSites;

	/** An array of Strings that contains the header for the Abstract 
	 * Table Model */
	private String[] columnNames = { "Name Reserving", "Checked in",
			"Days staying", "Site Number", "Tent/RV info" };

	/*******************************************************************
	 * Constructor for the SiteModel that instantiates Abstract Table 
	 * Model and an ArrayList of Sites
	 ******************************************************************/
	public SiteModel() {
		super();
		listSites = new ArrayList<Site>();
	}

	@Override
	/*******************************************************************
	 * Get the string title of a selected column of the Abstract Table
	 * Model
	 ******************************************************************/
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/*******************************************************************
	 * Gets the size of the total number of sites scheduled
	 * @return an Integer value, which is the size of the ArrayList
	 ******************************************************************/
	public int getSize() {
		return listSites.size();
	}

	@Override
	/*******************************************************************
	 * Gets the size of the array of Strings that go on the column 
	 * header
	 * @return an Integer value, which is the length of the array
	 ******************************************************************/
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	/*******************************************************************
	 * Gets the size of the ArrayList of Sites, determining how many 
	 * rows there are
	 * @return an Integer value, which is the length of the ArrayList
	 ******************************************************************/
	public int getRowCount() {
		return listSites.size();
	}

	@Override
	/*******************************************************************
	 * Depending on the row and column of the AbstractTableModel 
	 * selected, returns an object that corresponds with it
	 * @return the value of an object (either String, Integer, Date, or 
	 * Site type)
	 ******************************************************************/
	public Object getValueAt(int row, int col) {
		switch(col) {
		case 0: 
			return (listSites.get(row).getNameReserving()); //returns
		case 1:											   //String
			return (DateFormat.getDateInstance(DateFormat.SHORT).format(
					listSites.get(row).getCheckIn().getTime()));//return
		case 2:											       //date
			return (listSites.get(row).getDaysStaying()); //returns int
		case 3:
			return (listSites.get(row).getSiteNumber()); //returns int
		case 4: 
			if (listSites.get(row) instanceof RV) //check if RV or tent
				return ("Power required: "+  //returns power
						((RV)listSites.get(row)).getPower()) + " Volts.";
			else 
				return ("Number of tenters: "+//returns number of people 
						((Tent)listSites.get(row)).getNumOfTenters());
		default: //otherwise, it's just null
			return null;
		}
	}

	/*******************************************************************
	 * Adds a site into the list of people scheduled (ArrayList), or 
	 * informs you if there's an error.  Displays estimated price aswell
	 * @param site is a Site type 
	 ******************************************************************/
	public void addSite(Site site) {

		boolean contains = false;  //determines if site is taken already
		for(int i = 0; i < listSites.size(); i++) {
			if(listSites.get(i).getSiteNumber()==site.getSiteNumber()
					&& listSites.get(i).getCheckOut().after(
							site.getCheckIn()) && 
							site.getCheckOut().after(
									listSites.get(i).getCheckIn())) {
				contains = true; //true if check in & out date is valid
			}
		}
		if(areTaken(site)) { //determines if all the sites are already 
			//taken for a desired date
			JOptionPane.showMessageDialog(null, "All sites are taken");
			return; //ends method
		}
		if(contains) {  //if contains is true, then the site is taken
			JOptionPane.showMessageDialog(null, "That site is taken");
		}
		else {
			listSites.add(site); //otherwise, the site gets added to the 
			fireTableRowsInserted(0,listSites.size()); //list of sites
			JOptionPane.showMessageDialog(null, "You owe: $" + 
					site.getCost());  //An estimated price is given
			selectionSort(listSites);//All of the sites are then sorted
			//alphabetically by first name
		}
	}

	/*******************************************************************
	 * Removes the site being selected by the user
	 * @param i is the site index number of the table being selected
	 ******************************************************************/
	public void removeSite(int i) {
		if(i == -1) //determines if anything is selected at all
			return; //gets out of the method
		listSites.remove(i); //deletes the site from the ArrayList
		fireTableRowsDeleted(0,listSites.size()); //resizes the table
	}

	/*******************************************************************
	 * Returns the site of a desired index value of the ArrayList
	 * @param i is the index number of an ArrayList of type Site
	 * @return a Site type, of a desired index
	 ******************************************************************/
	public Site getSite (int i) {
		return listSites.get(i);
	}

	/*******************************************************************
	 * Updates the selected Site to what you desire, if availability 
	 * exists
	 * @param i is the index number of the table being selected
	 * @param site is the new Site that will take the place of the old 
	 * one
	 ******************************************************************/
	public void updateSite (int i, Site site) {

		selectionSort(listSites); //All the sites are resorted 
		//alphabetically by first name
		fireTableRowsUpdated(0, listSites.size()); //resizes the table
	}

	/*******************************************************************
	 * Saves the current Sites in the table to a serial file
	 * @param filename is the name of the serial file you'd like to save
	 * it as
	 ******************************************************************/
	public void saveSerial(String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(listSites); //Saves the ArrayList of Sites
			os.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*******************************************************************
	 * Loads a serial file that contains all of the saved Site 
	 * reservations
	 * @param filename is the name of the serial file you'd like to load
	 ******************************************************************/
	public void loadSerial(File filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream is = new ObjectInputStream(fis);
			listSites = (ArrayList<Site>) is.readObject(); //updates the
			fireTableRowsInserted(0,listSites.size()-1);//ArrayList to 
			is.close();								 //saved Sites
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*******************************************************************
	 * Saves the current Sites in the table to a text file
	 * @param fileName is the name of the text file you'd like to save
	 * it as
	 ******************************************************************/
	public void saveText(String fileName) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new 
					FileWriter(fileName)));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < listSites.size(); i++)
			out.println(listSites.get(i).toString()); //Saves the 
		out.close();								//ArrayList to the
	}										//text file with toString()

	/*******************************************************************
	 * Loads a text file that contains all of the saved Site 
	 * reservations
	 * @param fileName is the name of the text file you'd like to load
	 ******************************************************************/
	public void loadText(File fileName) {
		try {
			Scanner fileReader = new Scanner(fileName);
			listSites = new ArrayList<Site>();
			while(fileReader.hasNext()){
				String siteS = fileReader.nextLine();
				SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
				GregorianCalendar cal = new GregorianCalendar();
				int days;
				int sitenmr;
				int special;
				String[] parts = siteS.split("-"); //splits text file by
				days = Integer.parseInt(parts[2]);//the '-' implemented
				sitenmr = Integer.parseInt(parts[3]);//by the toString()
				special = Integer.parseInt(parts[4]);//and parses them
				Site site;
				if(siteS.contains("-Tent")){ //Determines if it's a tent
					site = new Tent();
					site.setNameReserving(parts[0]);
					try{
						Date date = fmt.parse(parts[1]);
						cal.setTime(date);
						site.setCheckIn(cal);
						site.setDaysStaying(days);
						site.setSiteNumber(sitenmr);
						((Tent)site).setNumOfTenters(special);
					}
					catch (ParseException e) {
						e.printStackTrace();
					}
				}
				else {
					site = new RV(); //Determines if it's an RV
					site.setNameReserving(parts[0]);
					try{
						Date date = fmt.parse(parts[1]);
						cal.setTime(date);
						site.setCheckIn(cal);
						site.setDaysStaying(days);
						site.setSiteNumber(sitenmr);
						((RV)site).setPower(special);
					}
					catch (ParseException e) {
						e.printStackTrace();
					}

				}
				listSites.add(site); //Updates the ArrayList to the 
			}						//Sites loaded from the text file
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		fireTableRowsInserted(0,listSites.size()-1);
	}

	/*******************************************************************
	 * Gives the option for the user to check out of their site
	 * @param row is the site index number of the table being selected
	 ******************************************************************/
	public void checkOut(int row) {
		if(row == -1) //Determines if anything is selected at all
			return;
		Site site = listSites.get(row); //Gets Site information
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String cOut = JOptionPane.showInputDialog(
				"When are you checking out?",df.format( //Asks for check
						site.getCheckOut().getTime()));//out date, while
		Date newDate;		//defaulting to the estimated check out date
		if(cOut == null) //if there's no date inputed
			return;     //then get out of the method
		try {
			newDate = df.parse(cOut);
			//checks if you're checking out AFTER your check in date
			if(!newDate.after(site.getCheckIn().getTime())) { 
				JOptionPane.showMessageDialog(null,"Invalid date. "
						+ "Please enter a date after your check in "
						+ "date.");
				return; //Gets out of the method
			}
			site.setCheckOut(newDate);
			JOptionPane.showMessageDialog(null,"You owe:" + 
					site.getCost()); //Determines actual cost of stay
		}
		catch (ParseException e) {
			//determines if valid date is entered
			JOptionPane.showMessageDialog(null,"Invalid date. "
					+ "Please use the mm/dd/yyyy format.");
		}
		removeSite(row); //removes the site, after they check out
	}

	/*******************************************************************
	 * Determines if ALL of the sites are taken on a specific date
	 * @param site is the Site type that the user is trying to schedule
	 * @return boolean true if all of the sites are taken on a specific
	 * date, or false if not
	 ******************************************************************/
	private boolean areTaken(Site site) {
		ArrayList <Integer> sites = new ArrayList <Integer>();
		for(Site thisSite: listSites) { //checks all of the already 
			if(thisSite.getCheckOut().after(//scheduled sites if there's
					site.getCheckIn()) && site.getCheckOut().after(
							thisSite.getCheckIn()) && //a free site open
							!sites.contains(thisSite.getSiteNumber()))
				sites.add(thisSite.getSiteNumber());
		}
		if(sites.size()==5) //if all of the sites are taken, then true
			return true;
		return false;
	}

	/*******************************************************************
	 * Returns the entire ArrayList of scheduled sites
	 * @return an ArrayList of Sites
	 ******************************************************************/
	public ArrayList<Site> getAllSites() {
		return this.listSites;
	}

	/*******************************************************************
	 * Compares two sites to see which name they're reserved under comes
	 * first in the alphabet
	 * @param s1 is a Site type that is compared against the other
	 * @param s2 is a Site type that is compared against the other
	 * @return an Integer value, -1 being s1 comes first, 1 being  s2
	 * comes first, and 0 being that they're exactly the same
	 ******************************************************************/
	public int compareTo(Site s1, Site s2){
		if(s1.getNameReserving().compareToIgnoreCase(
				s2.getNameReserving())<0){
			return -1; //s1 comes first
		}else if(s1.getNameReserving().compareToIgnoreCase
				(s2.getNameReserving())>0){
			return 1; //s2 comes first
		}else{
			return 0; //exactly the same
		}
	}

	/*******************************************************************
	 * Swaps two Sites within the ArrayList of Sites
	 * @param site is an ArrayList<Site>
	 * @param index1 is the index number of one of the sites being 
	 * swapped
	 * @param index2 is the index number of one of the sites being 
	 * swapped
	 ******************************************************************/
	private static <T extends Comparable<T>> void swap(ArrayList<Site> 
			site, int index1, int index2){
		Site temp = site.get(index1);
		site.set(index1, site.get(index2));
		site.set(index2, temp);
	}

	/*******************************************************************
	 * Does a selection sort throughout the ArrayList of Sites, 
	 * determining their spots in alphabetical order
	 * @param site is an ArrayList<Site>
	 ******************************************************************/
	public <T extends Comparable<T>> void selectionSort(ArrayList<Site> 
			site){  
		for(int i=0; i < site.size()-1; i++){
			int min = i;
			for(int scan = i+1; scan <site.size(); scan++){
				if(compareTo(site.get(scan),site.get(min))<0){
					min = scan;
				}
			}
			swap(site, min, i);
		}
	}
}

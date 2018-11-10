package camping;

import java.text.SimpleDateFormat;
import java.util.Date;

/***********************************************************************
 * Represents a Tent, a type of site that can be reserved for a certain 
 * number of days
 * @author Frank Derry Wanye, Adam Stewart
 * @version 03/17/2015
 **********************************************************************/
public class Tent extends Site{
	
	/** The number of people staying in the Tent */
	protected int numOfTenters;
	
	/*******************************************************************
	 * Default constructor sets all instance variables to default,
	 * non-null values
	 ******************************************************************/
	public Tent() {
		super();
		this.numOfTenters = 0;
	}
	
	/*******************************************************************
	 * Returns the number of people staying in the Tent
	 * @return a positive integer
	 ******************************************************************/
	public int getNumOfTenters() {
		return numOfTenters;
	}

	/*******************************************************************
	 * Sets the number of tenters to a positive integer value
	 * @return numOfTenters is the number of people staying in the Tent
	 ******************************************************************/
	public void setNumOfTenters(int numOfTenters) {
		this.numOfTenters = numOfTenters;
	}
	
	/*******************************************************************
	 * Calculates the cost of reserving the Tent for the specified
	 * number of days, with the specified number of tenters
	 * @return the cost of reserving the site
	 ******************************************************************/
	public double getCost() {
		return 3*daysStaying*numOfTenters;
	}
	
	/*******************************************************************
	 * Generates a string containing the name of the reserver, the 
	 * check in date, the duration of stay, the site number, and the 
	 * number of people staying in the Tent separated by "-" characters
	 * @return a String containing Tent data
	 ******************************************************************/
	public String toString() {
		Date date = getCheckIn().getTime();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		return getNameReserving() + "-" + df.format(date) + "-" 
				+ getDaysStaying() + "-" + getSiteNumber() + "-" +
				getNumOfTenters() + "-Tent";
	}
	
}

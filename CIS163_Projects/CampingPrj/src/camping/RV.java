package camping;

import java.text.SimpleDateFormat;
import java.util.Date;

/***********************************************************************
 * Represents an RV, a type of site that can be reserved for a certain 
 * number of days
 * @author Frank Derry Wanye, Adam Stewart
 * @version 03/17/2015
 **********************************************************************/
public class RV extends Site{
	
	/** The power needed for the RV. Can be 30, 40 or 50 */
	protected int power;
	
	/*******************************************************************
	 * Default constructor sets all instance variables to default,
	 * non-null values
	 ******************************************************************/
	public RV() {
		super();
		this.power = 0;
	}
	
	/*******************************************************************
	 * Returns the power needed for the RV
	 * @return 30, 40 or 50
	 ******************************************************************/
	public int getPower() {
		return power;
	}

	/*******************************************************************
	 * Sets the power needed for the RV to 30, 40 or 50
	 * @return power is the power needed for the RV
	 ******************************************************************/
	public void setPower(int power) {
		this.power = power;
	}
	
	/*******************************************************************
	 * Calculates the cost of reserving the RV for the specified
	 * number of days
	 * @return the cost of reserving the site
	 ******************************************************************/
	public double getCost() {
		return 30*daysStaying;
	}
	
	/*******************************************************************
	 * Generates a string containing the name of the reserver, the 
	 * check in date, the duration of stay, the site number, and the 
	 * power needed for an RV separated by the "-" character
	 * @return a String containing RV data
	 ******************************************************************/
	public String toString() {
		Date date = getCheckIn().getTime();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		return getNameReserving() + "-" + df.format(date) + "-" 
				+ getDaysStaying() + "-" + getSiteNumber() + "-" +
				getPower() + "-RV";
	}
	
}

package camping;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/***********************************************************************
 * Represents a site that can be reserved for a certain number of days
 * @author Frank Derry Wanye, Adam Stewart
 * @version 03/17/2015
 **********************************************************************/
public abstract class Site implements Serializable {
	
	/** Default serial number for the site */
	private static final long serialVersionUID = 1L;
	
	/** The name of the person who reserved the site */
	protected String nameReserving;
	
	/** The date on which the site will be occupied */
	protected GregorianCalendar checkIn;
	
	/** The estimated number of days for which the site will be
	 * occupied */
	protected int daysStaying;
	
	/** The date on which the customer checks out of the site */
	protected GregorianCalendar checkOut;
	
	/** The number of the sites. Value ranges from 1 to 5 */
	protected int siteNumber;

	/*******************************************************************
	 * Default constructor sets all instance variables to default,
	 * non-null values
	 ******************************************************************/
	public Site() {
		this.nameReserving = "";
		this.checkIn = new GregorianCalendar(TimeZone.getTimeZone(
				"EST"));
		this.daysStaying = 0;
		this.checkOut = new GregorianCalendar(TimeZone.getTimeZone(
				"EST"));
		this.siteNumber = 0;
	}
	
	/*******************************************************************
	 * Returns the name of the person reserving a site
	 * @return the name of the reserver
	 ******************************************************************/
	public String getNameReserving() {
		return nameReserving;
	}

	/*******************************************************************
	 * Sets the name of the person reserving to a String formatted
	 * to only contain letters and spaces
	 * @param nameReserving is the name of the person reserving the site
	 ******************************************************************/
	public void setNameReserving(String nameReserving) {
		this.nameReserving = nameReserving;
	}

	/*******************************************************************
	 * Returns the date on which the customer intends to check in/ date
	 * on which customer checked in
	 * @return a GregorianCalendar set to a date of the format 
	 * "MM/dd/yyyy"
	 ******************************************************************/
	public GregorianCalendar getCheckIn() {
		return checkIn;
	}

	/*******************************************************************
	 * Sets the date on which the customer intends to check in/ date
	 * on which customer checked in to a GregorianCalendar set to a 
	 * date of the format "MM/dd/yyyy"
	 * @param checkIn is set to a date of the format "MM/dd/yyyy"
	 ******************************************************************/
	public void setCheckIn(GregorianCalendar checkIn) {
		this.checkIn = checkIn;
	}

	/*******************************************************************
	 * Returns the number of days the customer intends to stay at a
	 * site
	 * @return a positive integer
	 ******************************************************************/
	public int getDaysStaying() {
		return daysStaying;
	}

	/*******************************************************************
	 * Sets the number of days the customer intends to stay at a site,
	 * and automatically sets the checkOut date by adding the intended
	 * number of days to the checkIn date
	 * @param daysStaying is the expected number of days the customer
	 * will stay at a site
	 ******************************************************************/
	public void setDaysStaying(int daysStaying) {
		this.daysStaying = daysStaying;
		GregorianCalendar in = new GregorianCalendar();
		in.setTime(checkIn.getTime());
		in.add(Calendar.DAY_OF_MONTH, getDaysStaying());
		checkOut = in;
	}

	/*******************************************************************
	 * Returns the date on which the customer intends to check out
	 * @return a GregorianCalendar set to a date of the format 
	 * "MM/dd/yyyy"
	 ******************************************************************/
	public GregorianCalendar getCheckOut() {
		return checkOut;
	}

	/*******************************************************************
	 * Sets the date on which the customer is checking out to a 
	 * GregorianCalendar set to a date of the format "MM/dd/yyyy", and
	 * automatically sets the number of days staying to the difference
	 * in days between the check out and check in dates.
	 * @param checkOut is set to a date of the format "MM/dd/yyyy"
	 ******************************************************************/
	public void setCheckOut(Date date) {
		checkOut.setTime(date);
	    long span = checkOut.getTimeInMillis() - 
	    		checkIn.getTimeInMillis();
	    GregorianCalendar c3 = new GregorianCalendar();
	    c3.setTimeInMillis(span);
	    daysStaying = (int)(c3.getTimeInMillis()/(1000*60*60*24));
	}

	/*******************************************************************
	 * Returns the number of the reserved site
	 * @return a positive integer between 1 and 5
	 ******************************************************************/
	public int getSiteNumber() {
		return siteNumber;
	}

	/*******************************************************************
	 * Sets the number of the site reserved to an integer between 1 
	 * and 5
	 * @param siteNumber is the number of the site to be reserved
	 ******************************************************************/
	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}
	
	/*******************************************************************
	 * Calculates the cost of reserving the site depending on its type
	 * and the number of days it was reserved for
	 * @return the cost of reserving the site
	 ******************************************************************/
	public abstract double getCost();
	
	/*******************************************************************
	 * Generates a string containing all information about a site, 
	 * separated by the "-" character
	 * @return a String containing all site data
	 ******************************************************************/
	public abstract String toString();
	
}

import java.text.DecimalFormat;

/**
 * Lab 4
 * 
 * @author (Adam Stewart) 
 * @version (9/18/14)
 */
public class Date
{
      private int month;
      private int day;
      private int year;
   
  public Date (int pMonth, int pDay, int pYear) {
      month = pMonth;
      day = pDay;
      year = pYear; 
      }
      
  public void setMonth (int pMonth) {
       month = pMonth; }
      
  public int getMonth () {
      return month; }
      
  public void setDay (int pDay) {
      day = pDay; }
      
  public int getDay () {
      return day; }
      
  public void setYear (int pYear) {
      year = pYear; }
      
  public int getYear () {
      return year; }
      
  public String toString () {
      DecimalFormat df = new DecimalFormat ("##00");
      String Monthing = ("JanFebMarAprMayJunJulAugSepOctNovDec");
      //return "Month: " + df.format(month) + " Day: " + df.format(getDay()) + " Year: " + getYear();
      return Monthing.substring ((month-1)*3, month*3) + " " + df.format(getDay()) + ", " + 
      df.format(getYear()) ;
      //First month (jan) is 0, hence (month-1).  You multiply by 3, so more than 3 characters isn't
      //recorded, since each abbreviation is only 3 characters long.
    }
      
      
}

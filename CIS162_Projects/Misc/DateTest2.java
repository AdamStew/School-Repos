
/**
 * Lab 4
 * 
 * @author (Adam Stewart) 
 * @version (9/18/14)
 */
public class DateTest2
{
    public static void main (String [] args) {  
      Date d1 = new Date(6,5,1996);
      System.out.print("Month: " + d1.getMonth());
      System.out.print("  Day: " + d1.getDay());
      System.out.println("  Year: " + d1.getYear());
      d1.setMonth(8);
      d1.setDay(14);
      d1.setYear(1980);
      System.out.print("Month: " + d1.getMonth());
      System.out.print("  Day: " + d1.getDay());
      System.out.println(" Year: " + d1.getYear());
      System.out.println(d1.toString()); }
}

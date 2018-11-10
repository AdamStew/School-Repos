
/**
 * Lab 3 Activity III
 * 
 * @author (Adam Stewart) 
 * @version (9/11/14)
 */
import java.util.*;
public class DateParser
{ public static void main (String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter a date: ");
    String slash = sc.next();
    int Month = slash.indexOf("/");
    String month = slash.substring(0,Month);
    System.out.println("month: " + month);
    // "Month" = where to stop, "month" = numerical number for the month.
    
    Month++;
    int Day = slash.indexOf("/", Month);
    String day = slash.substring(Month, Day);
    System.out.println("day: " + day);
    //Skip "Day++;", since there's nothing that follows the year.
    
    int Year = slash.indexOf("/", Day);
    Year++;
    String year = slash.substring(Year);
    System.out.println("year: " + year);
   
}
}

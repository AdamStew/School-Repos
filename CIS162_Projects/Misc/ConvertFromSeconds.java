
/**
 * Write a description of class ConvertFromSeconds here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
public class ConvertFromSeconds
{
    public static void main (String[] args) {
        Scanner scan = new Scanner (System.in);
        System.out.print ("Time in seconds? ");
        int seconds = scan.nextInt();
        int minutes = (seconds/60);
        int hours = (minutes/60);
        System.out.println ("This is " + hours + " hours, " + minutes%60 + " minutes, and " + seconds%60 + " seconds.");}
}

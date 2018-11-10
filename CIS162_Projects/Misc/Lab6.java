
/**
 * Write a description of class Lab6 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Lab6
{
    public static void main (String[] args) {
        task1(15);
        task2("Gone with the wind");
        task3("This is a test.");
    }
    
    public static void task1 (int nbr){
        nbr = 10;
        while(nbr>=0){
            System.out.println(nbr);
            nbr--;
        }
        System.out.println("Blastoff!");
    }
    
    public static void task2 (String windy, int nbr) {
        windy = "Gone with the wind";
        nbr = 0;
        while(nbr<18){
            System.out.println (windy.substring(0, nbr));
            nbr++;
        }
    }
    
    public static void task3 (String test, int count) {
        test = "This is a test.";
        int space1 = test.indexOf(" ");
        int space2 = test.indexOf(" ",space1);
        int space3 = test.indexOf(" ", space2);
        count = 0;
        while(count>=0){
            count++;
            System.out.println count;
        }
    }
}

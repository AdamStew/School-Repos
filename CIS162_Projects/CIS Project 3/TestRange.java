
/**
 * Testing the Range class
 * 
 * @author Adam Stewart 
 * @version 10/16/14
 */
public class TestRange
{
    public static void main(String[] args) {
        Range r1 = new Range(50, 60);
        
        System.out.println("Testing Range Class - Adam Stewart");
        System.out.println("==================================");
        
        System.out.println("Range: (50, 60)");
        System.out.println("Range's Low: " + r1.getLow());
        System.out.println("Range's High: " + r1.getHigh());
        System.out.println("Is 49 in range? " + r1.inRange(49));
        System.out.println("Is 51 in range? " + r1.inRange(51));
        System.out.println("Range's Average: " + r1.getAverage());
    }
}

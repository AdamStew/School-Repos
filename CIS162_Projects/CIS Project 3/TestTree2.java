
/**
 * Write a description of class TestTree here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TestTree2
{
    public static void main(String[] args) {
        Tree t1 = new Tree ("Oak", 40, 80);
        
        System.out.println("Testing Tree Class - Adam Stewart");
        System.out.println("=================================");
        
        System.out.println("Tree: (Oak, 40, 80)");
        System.out.println("Range's Low: " + t1.getLow());
        System.out.println("Range's High: " + t1.getHigh());
        System.out.println("Range's Name: " + t1.getName());
        System.out.println("Is 39 in range? " + t1.inRange(39));
        System.out.println("Is 41 in range? " + t1.inRange(41));
        System.out.println("Tree's Average: " + t1.getAverageHeight());
    }
}

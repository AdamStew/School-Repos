
/**
 * Write a description of class ItemTester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ItemTester
{
    public static void main(String[] args){
        Item WM = new Item("Watermelon  ", "An enormous melon of the water.  ", 3.50);
        System.out.println("===============");
        System.out.println("++ITEM TESTER++");
        System.out.println("===============");
        System.out.println("");
        System.out.println("TESTING NAME");
        System.out.println("============");
        System.out.println("Item Name: " + WM.getName());
        System.out.println("");
        System.out.println("TESTING DESC");
        System.out.println("============");
        System.out.println("Item Description: " + WM.getDesc());
        System.out.println("");
        System.out.println("TESTING WEIGHT");
        System.out.println("==============");
        System.out.println("Item Weight: " + WM.getWeight());
    }
}


/**
 * Write a description of class RoomTester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.*;

public class RoomTester
{
    public static void main(String[] args){
        Item WM = new Item("Watermelon", "An enormous melon of the water", 3.50);
        Item TP = new Item("Toliet Paper", "A roll of 2-ply toliet paper", 8.25);
        Room Bedroom = new Room ("Small bedroom upstairs", WM);
        Room Bathroom = new Room ("Large bathroom upstairs");
        Bedroom.addNeighbor("South", Bathroom);
        Bathroom.addNeighbor("North", Bedroom);
        
        System.out.println("===============");
        System.out.println("++ROOM TESTER++");
        System.out.println("===============");
        System.out.println("");
        System.out.println("TESTING DESC");
        System.out.println("============");
        System.out.println("Room Description for bedroom: " + Bedroom.getDesc());
        System.out.println("Room Description for bathroom: " + Bathroom.getDesc());
        System.out.println("");
        System.out.println("TESTING ITEM");
        System.out.println("============");
        System.out.println("Does the Bedroom have an item?  " + Bedroom.hasItem());
        System.out.println("Does the Bathroom have an item?  " + Bathroom.hasItem());
        System.out.println("Bedroom Item: " + Bedroom.getItem().getName());
        System.out.println("Bathroom Item: " + Bathroom.getItem());
        System.out.println("");
        System.out.println("ADDING/REMOVING ITEM");
        System.out.println("=====================");
        Bathroom.addItem(TP); 
        System.out.println("Bathroom Item Added: " + Bathroom.getItem().getName());
        Bathroom.removeItem();
        System.out.println("Bathroom Item Removed: " + Bathroom.getItem());
        System.out.println("");
        System.out.println("TESTING HASHMAP");
        System.out.println("===============");
        System.out.println("Bedroom neighbor south: " + Bedroom.getNeighbor("South").getDesc());
        System.out.println("Bedroom neighbor east: " + Bedroom.getNeighbor("East"));
        System.out.println("Bathroom neighbor: " + Bathroom.getNeighbor("North").getDesc());
        System.out.println("");
        System.out.println("TESTING LONG DESCRIPTION");
        System.out.println("========================");
        System.out.println("Bedroom Long Description: " + Bedroom.getLongDescription());
        System.out.println("Bathroom Long Description: " + Bathroom.getLongDescription());
        System.out.println("");
        
        
        
    }
}

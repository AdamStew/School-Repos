
import java.util.*;

public class Room
{
    private HashMap <String, Room> myNeighbors;
    private String desc;
    private Item item;
    
    public Room(String pDesc){
        desc=pDesc;
        item=null;
        myNeighbors = new HashMap <String, Room> ();
    }
    
    public Room(String pDesc, Item pItem){
        desc=pDesc;
        item=pItem;
        myNeighbors = new HashMap <String, Room> ();
    }
    
    public void setDesc(String pDesc){
        desc=pDesc;
    }
    
    public String getDesc(){
        return desc;
    }
    
    public void setItem(Item pItem){
        item=pItem;
    }
    
    public Item getItem(){
        return item;
    }
    
    public void addItem(Item i){
        setItem(i);
    }
    
    public boolean hasItem(){
        if(getItem()!=null){
            return true;
        }else{
            return false;
        }
    }
    
    public void addNeighbor(String pDirection, Room r){
        myNeighbors.put(pDirection, r);
    }
    
    public Room getNeighbor(String pDirection){
        return myNeighbors.get(pDirection);
    }
    
    public Item removeItem(){
       try{
           return item;
        }
       finally{
           item=null;
        }
    }
    
    public String getLongDescription(){
        String n=getDesc();
        if(hasItem()==true){
            String m=getItem().getName();
            return "You are in the " + n.toLowerCase() + ".  " + "You see a(n) " + m.toLowerCase() + ".";
        }else{
            return "You are in the " + n.toLowerCase() + ".";
        }
    }
}

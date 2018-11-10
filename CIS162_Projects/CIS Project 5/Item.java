
public class Item
{
    private String name;
    private String desc;
    private double weight;
    
    
    public Item(String pName, String pDesc, double pWeight){
        name=pName;
        desc=pDesc;
        weight=pWeight;
    }

    public void setName(String pName){
        name=pName;
    }

    public String getName(){
        return name;
    }
    
    public void setDesc(String pDesc){
        desc=pDesc;
    }
    
    public String getDesc(){
        return desc;
    }
    
    public void setWeight(double pWeight){
        weight=pWeight;
    }
    
    public double getWeight(){
        return weight;
    }
}

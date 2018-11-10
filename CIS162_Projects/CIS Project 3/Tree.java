
/**
 * Write a description of class Tree here.
 * 
 * @author Adam Stewart 
 * @version 10/18/14
 */
public class Tree
{       private String name;
        private int low;
        private int high;
        Range range;
        
    public Tree(String pname, int plow, int phigh){
        range = new Range(plow, phigh);
        name = pname;
        low = plow;
        high = phigh;
    }
    
    public int getLow(){
        return range.getLow();
    }
    
    public int getHigh(){
        return range.getHigh();
    }
    
    public String getName(){
        return name;
    }
    
    public boolean inRange(int value){
        if(range.inRange(value)){
            return true;
        }else{
            return false;
        }
    }
    
    public String toString(){
        return name + "\t" + getLow() + "\t" + getHigh();
    }
    
     public double getAverageHeight(){
        double averageHeight = range.getAverage();
        return averageHeight;
    }
}


/**
 * Gets Range Values of Trees
 * 
 * @author Adam Stewart
 * @version 10/16/14
 */

public class Range
{       private int low;
        private int high;

    public Range(int plow, int phigh){
        low = plow;
        high = phigh;
    }

    public int getLow(){
        return low; 
    }

    public int getHigh(){
        return high;
    }

    public boolean inRange(int value){
        if(value >= low && value <= high){
            return true;
        }else{
            return false;
        }
    }
    
    public double getAverage(){
        double average = ((low+high)*0.5);
        return average;
    }
}

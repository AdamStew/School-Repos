
/**
 * Write a description of class TreelistOfTrees here.
 *
 * @author CIS162
 * @version Fall 2014
 */
import java.util.ArrayList;
public class TreeDB
{

    private ArrayList < Tree > listOfTrees;

    /**
     * Constructor for objects of class TreelistOfTrees
     */
    public TreeDB()
    {
        listOfTrees = new ArrayList < Tree >( );

        Tree t1 = new Tree( "Pin Oak",      59, 72 );
        Tree t2 = new Tree( "White Pine",   50, 80 );
        Tree t3 = new Tree( "Silver Maple", 50, 80 );
        Tree t4 = new Tree( "Tulip Tree",   70, 90 );
        Tree t5 = new Tree( "River Birch",  40, 70 );

        listOfTrees.add( t1 );
        listOfTrees.add( t2 );
        listOfTrees.add( t3 );
        listOfTrees.add( t4 );
        listOfTrees.add( t5 );
    }

    public String toString() {
        String result = "";
        for(Tree theseTrees : listOfTrees){
            String allTrees = theseTrees.toString()+"\n";
            result = result + allTrees;
        }
        return result;
    }

    public String queryByName(String key) {
        String result = "";
        for(Tree thisTree : listOfTrees) {
            String thisName = thisTree.getName();
            if(thisName.contains(key)) {
                result = result + thisTree.toString()+"\n";
            }
        }
        return result; 
    }

    public String queryByPossibleHeight( int key ) {
        String result = "";
        for(Tree rangeTree: listOfTrees)
        {
            if(rangeTree.inRange(key))
                {
                    result = result + rangeTree.toString() + "\n";
                }
        }
        return result;
    }

    public int getMaxHeight( ){
        int maxH = listOfTrees.get(0).getHigh();
        for(Tree maxTree : listOfTrees){ 
            int max = maxTree.getHigh();
            if(max > maxH){
                maxH = max;
            }
        }
        return maxH;
    }

    public int getMinHeight( )
    {
        int minH = listOfTrees.get(0).getLow();
        for(Tree minTree : listOfTrees){ 
            int min = minTree.getLow();
            if(min < minH){
                minH = min;
            }
        }
        return minH;
    }

    public double getAverageHeight( ){
        double average = 0;
        for(Tree avgTree : listOfTrees){
            average = average + avgTree.getAverageHeight();
        }
        average=average/listOfTrees.size();
        return average;
    }
}
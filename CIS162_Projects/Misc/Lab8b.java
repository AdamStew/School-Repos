import java.util.*;

public class Lab8b{

    public static void main(String[] args){
        ArrayList<String> friends = new ArrayList<String>();
        friends.add("Steve");
        friends.add("Abbie");
        friends.add("Michelle");
        friends.add("Bill");
        friends.add("Mack");
        friends.add("Mike");
        friends.add("Tim");
        printAllThatStartWith(friends,"M");
    }

    //write this method so that it searches through all of the elements
    //of the ArrayList ary and prints only those that start with the 
    //parameter src.  
    //You can use the String method startsWith
    
    public static void printAllThatStartWith(ArrayList<String> ary, String src){
        String result = "";
        int i = 0;
        while(i < ary.size()){
            if (ary.get(i).startsWith(src)){
                result = result + (ary.get(i) + ", ");
            }
            i++;
        }
        System.out.println(result);
    }
}
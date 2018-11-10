import java.util.*;

public class FoodTester{

    public static void main(String[] args){
        Cart myCart = new Cart();
        myCart.add("carrots","f&v",2,1.50);
        myCart.add("milk","dairy",2,1.90);
        myCart.add("Capn Crunch","grain",3,4.15);
        myCart.add("Ham","meat",1,3.50);
        myCart.add("Bread","grain",3,2.25);
        myCart.add("lettuce","f&v",1,2.40);
        myCart.add("yogurt","dairy",6,.95);
        myCart.add("donuts","grain",6,1.10);
        System.out.println(myCart.total());
        String word="d";
        word += "ai";
        ArrayList<FoodType> tmp = myCart.getList(word+"ry");
        System.out.println(tmp);
    }

}

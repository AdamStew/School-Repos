import java.util.*;

public class Cart{

    private ArrayList<FoodType> cart;
    private Scanner key;
    public Cart(){
        cart = new ArrayList<FoodType>();
        key = new Scanner(System.in);
    }

    public void add(){
        System.out.print("name:");
        String var1 = key.next();
        System.out.print("category:");
        String var2 = key.next();
        System.out.print("qty:");
        int var3 = key.nextInt();
        System.out.print("price:");
        double var4 = key.nextDouble();
        FoodType ft = new FoodType(var1,var2,var3,var4);
        cart.add(ft);
    }

    public void add(String pName, String pCat, int pQty, double pPrice){
        FoodType ft = new FoodType(pName,pCat,pQty,pPrice);
        cart.add(ft);
    }

    public double total(){
        double tot=0;
        int i=0;
        while(i<cart.size()){
            tot += cart.get(i).totalCost();
            i++;
        }
        return tot;
    }

    public ArrayList<FoodType> getList(String pCat){
        ArrayList<FoodType> list = new ArrayList<FoodType>();
        int i=0;
        while(i<cart.size()){
            if(cart.get(i).getCat().equals(pCat))
                list.add(cart.get(i));
            i++;
        }
        return list;
    }

}

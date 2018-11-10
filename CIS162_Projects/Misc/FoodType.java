public class FoodType{

    private String name, cat;
    private int qty;
    private double price;

    public FoodType(){
        name="";
        cat="";
        qty=0;
        price=0;
    }

    public FoodType(String pName, String pCat, int pQty, double pPrice){
        name=pName;
        cat=pCat;
        qty=pQty;
        price=pPrice;
    }

    public String getName(){
        return name;
    }

    public String getCat(){
        return cat;
    }

    public int getQty(){
        return qty;
    }

    public double getPrice(){
        return price;
    }

    public void setName(String pName){
        name = pName;
    }

    public void setCat(String pCat){
        cat = pCat;
    }

    public void setQty(int pQty){
        qty = pQty;
    }

    public void setPrice(double pPrice){
        price = pPrice;
    }

    public double totalCost(){
        return qty * price;
    }

    public String toString(){
        return name + ", " + totalCost();
    }

    public static void main(String[] args){
        FoodType ft1 = new FoodType();
        ft1.setName("Rice");
        System.out.println(ft1);
        ft1.setPrice(1.50);
        ft1.setQty(2);
        System.out.println(ft1);
        FoodType ft2 = new FoodType("Eggs","meat",1,.75);
        System.out.println(ft2);
    }
}

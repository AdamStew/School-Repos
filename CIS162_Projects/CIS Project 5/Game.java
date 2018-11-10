
import java.util.*;
public class Game
{
    private ArrayList <Item> inventory;
    private Room curRoom;
    private Item item;
    private String msg;

    private Item DragonFruit;
    private Item GoatHead;
    private Item FruitPunch; 
    private Item Popsicles;
    private Item Gum;
    private Item Cart;

    private Room Entrance;
    private Room FruitIsle;
    private Room BreadsIsle;
    private Room MeatIsle;
    private Room BeveragesIsle;
    private Room DessertsIsle;
    private Room DairyIsle;
    private Room Checkout;

    public Game(){
        inventory = new ArrayList();
        createRooms();
        setWelcomeMessage();
        curRoom = Entrance;
    }

    private void createRooms(){
        DragonFruit = new Item("Dragon Fruit", "Red/pinkish spiky fruit; it's on your list", 15.00);
        GoatHead = new Item("Goat Head", "A severed goat's head; it's on your list", 25.00);
        FruitPunch = new Item("Fruit Punch", "2-Liter container of fruit punch; it's on your list", 6.00);
        Popsicles = new Item("Popsicles", "An 8-pack of popsicles; this isn't on your list however", 4.00);
        Gum = new Item("Gum", "A pack of flavorfull gum; this is on your list", 50.00);
        Cart = new Item("Shopping Cart", "A cart to push all of your purchases to your home", 100.00);
        
        Entrance = new Room ("Entrance");
        FruitIsle = new Room ("Fruit Isle", DragonFruit);
        BreadsIsle = new Room ("Bread Isle");
        MeatIsle = new Room ("Meat Isle", GoatHead);
        BeveragesIsle = new Room ("Beverages Isle", FruitPunch);
        DessertsIsle = new Room ("Desserts Isle", Popsicles);
        DairyIsle = new Room ("Dairy Isle");
        Checkout = new Room ("Checkout", Cart);

        Entrance.addNeighbor("West", Checkout);
        Entrance.addNeighbor("North", FruitIsle);
        FruitIsle.addNeighbor("South", Entrance);
        FruitIsle.addNeighbor("West", DairyIsle);
        FruitIsle.addNeighbor("North", BreadsIsle);
        BreadsIsle.addNeighbor("South", FruitIsle);
        BreadsIsle.addNeighbor("West", DessertsIsle);
        BreadsIsle.addNeighbor("North", MeatIsle);
        MeatIsle.addNeighbor("South", BreadsIsle);
        MeatIsle.addNeighbor("West", BeveragesIsle);
        BeveragesIsle.addNeighbor("South", DessertsIsle);
        BeveragesIsle.addNeighbor("East", MeatIsle);
        DessertsIsle.addNeighbor("South", DairyIsle);
        DessertsIsle.addNeighbor("East", BreadsIsle);
        DessertsIsle.addNeighbor("North", BeveragesIsle);
        DairyIsle.addNeighbor("South", Checkout);
        DairyIsle.addNeighbor("East", FruitIsle);
        DairyIsle.addNeighbor("North", DessertsIsle);
        Checkout.addNeighbor("East", Entrance);
        Checkout.addNeighbor("North", DairyIsle);
    }

    private void setWelcomeMessage(){
        msg = "Welcome to the Grocery Store.  You have a list of things to purchase, so get to it." + "\n"
        + "Grocery List: Dragon Fruit, Goat's Head, Popsicles, and Gum" + "\n \n You are in the entrance.";
    }

    public String getMessage(){
        return msg;
    }

    public void help(){
        if(curRoom.getDesc().equals("Entrance")){
            msg = "There isn't much to buy here, get shoppin!";
        }
        if(curRoom.getDesc().equals("Fruit Isle")){
            msg = "I could go for some fruit.";
        }
        if(curRoom.getDesc().equals("Bread Isle")){
            msg = "There's a man who looks thirsty.";
        }
        if(curRoom.getDesc().equals("Meat Isle")){
            msg = "There's some exotic meat here, some of it looks interesting.";
        }
        if(curRoom.getDesc().equals("Beverages Isle")){
            msg = "Do I really want a drink?";
        }
        if(curRoom.getDesc().equals("Desserts Isle")){
            msg = "Desserts always look good.";
        }
        if(curRoom.getDesc().equals("Dairy Isle")){
            msg = "I'm lactose-intolerant!";
        }
        if(curRoom.getDesc().equals("Checkout")){
            msg = "I better have everything on my list before I come here.";
        }
    }

    public void look(){
        msg = curRoom.getLongDescription();
    }

    public void move(String direction){
        Room nextRoom = curRoom.getNeighbor(direction);
        if (nextRoom == null){
            msg = "You can't go in that direction";
        }else{
            curRoom = nextRoom;
            msg = curRoom.getLongDescription();
        }
    }

    public boolean gameOver(){
        if((curRoom.getDesc().equals("Checkout") && inventory.size() >= 4 && ((inventory.get(0).getName().equals("Gum")) 
        || (inventory.get(1).getName().equals("Gum")) || 
        (inventory.get(2).getName().equals("Gum")) || (inventory.get(3).getName().equals("Gum"))))){
            msg = "The game is over!";
            return true;
        }else{
            return false;
        }
    }

    public void take(){
        if(curRoom.getItem() == null){
            msg = "There is no item in this room.";
        }else{
            if(curRoom.getItem().getWeight()>50){
                msg = "You can't leave until you've gotten everything on your list.";
            }else{
                if(curRoom.getItem() != null){
                    msg = "You're now holding the " + curRoom.getItem().getName() + ".";
                    inventory.add(curRoom.getItem());
                    curRoom.removeItem();
                }
            }
        }
    }

    private Item checkForItem(String name){ 
        item = new Item ("","", 0.0);
        for(int i=0; i<inventory.size();i++){
            if(inventory.get(i).getName().contains(name)){
                msg = inventory.get(i).getName();
                item.setName(inventory.get(i).getName());
                item.setDesc(inventory.get(i).getDesc());
                item.setWeight(inventory.get(i).getWeight());
            }else{
                return null;
            }
        }
        return item;
    }

    public void drop (String name){ 
        if(inventory.size() == 0){
            msg = "You're not holding any items.";
        }else{
            for(int i=0; i<inventory.size();i++){
                if(curRoom.getItem() == null && inventory.get(i).getName().contains(name)){
                    curRoom.setItem(inventory.get(i));
                    inventory.remove(i);
                    msg = "You have successfully dropped an item.";
                }else{
                    msg = "This room has too many items already in here.";
                }
            }
        }
    }

    public void show(){  
        msg = "";
        if(inventory.get(0) == null){
            msg = "You're not holding any items.";
        }else{
            for(int i=0; i<inventory.size();i++){
                msg = msg + inventory.get(i).getName() + "\t";
            }
        }
    }

    public void trade(){ 
        if(curRoom.getDesc().equals("Bread Isle") && ((inventory.get(0).getName().equals("Fruit Punch")) || (inventory.get(1).getName().equals("Fruit Punch")) || 
            (inventory.get(2).getName().equals("Fruit Punch")) || (inventory.get(3).getName().equals("Fruit Punch")))){
            msg = "You traded a nice man in the store your fruit punch for gum.";
            inventory.remove(FruitPunch);
            inventory.add(Gum);
        }
    }

    public boolean starve(){
        msg = "RIP: tummy.";
        curRoom=Checkout;
        inventory.add(Popsicles);
        inventory.add(Gum);
        inventory.add(GoatHead);
        inventory.add(DragonFruit);
        return true;
    }

    public static void main(String args[]){
        Game g = new Game();
        System.out.println(g.getMessage());
        g.take();
        System.out.println(g.getMessage());
        g.move("South");
        System.out.println(g.getMessage());
        g.drop("book");
        System.out.println(g.getMessage());
        g.move("North");
        System.out.println(g.getMessage());
        g.take();
        System.out.println(g.getMessage());
        g.move("South");
        System.out.println(g.getMessage());
        g.checkForItem("Dragon"); 
        System.out.println(g.getMessage());
        g.drop("Dragon");  
        System.out.println(g.getMessage());
        g.take();
        System.out.println(g.getMessage());
        g.move("West");
        System.out.println(g.getMessage());
        g.move("North");
        System.out.println(g.getMessage());
        g.move("North");
        System.out.println(g.getMessage());
        g.take();
        System.out.println(g.getMessage());
        g.move("East");
        System.out.println(g.getMessage());
        g.move("North");
        System.out.println(g.getMessage());
        g.take();
        System.out.println(g.getMessage());
        g.move("West");
        System.out.println(g.getMessage());
        g.take();
        System.out.println(g.getMessage());
        g.move("South");
        System.out.println(g.getMessage());
        g.move("East");
        System.out.println(g.getMessage());
        g.show();
        System.out.println(g.getMessage());
        g.trade();
        System.out.println(g.getMessage());
        g.show();
        System.out.println(g.getMessage());
        g.move("South");
        System.out.println(g.getMessage());
        g.move("South");
        System.out.println(g.getMessage());
        g.move("West");
        System.out.println(g.getMessage());
        if(g.gameOver()){
            System.out.println(g.getMessage());
        }
        Game g1 = new Game();
        System.out.println(g1.getMessage());
        g1.starve();
        System.out.println(g1.getMessage());
        if(g.gameOver()){
            System.out.println(g.getMessage());
        }
    }
}

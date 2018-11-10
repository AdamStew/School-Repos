import java.util.*;

public class TestNum
 { public static void main (String[] args) {
    Scanner sc = new Scanner(System.in);
    Num n1= new Num(0);
    System.out.print("Enter a number: ");
    int val= sc.nextInt();
    n1.setNum(val);
    System.out.print("Enter another number: ");
    Num n2 = new Num(sc.nextInt());
    if(n1.equals(n2)){
        System.out.println("They're equal!");
    }else{
        System.out.println("They're not equal.");
    }
  }
}

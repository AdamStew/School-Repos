

public class Num
{
   private int n;
   
   public Num(int pNum){
       n=pNum;
   }
    
   public void setNum(int pNum){
       n=pNum;
   }
    
   public int getNum(){
       return n;
   }
   
   public boolean equals(Num other){
       if(n==other.n){
           return true;
       }else{
           return false;
       }
   }
   
   public String toSring(){
       return "num= " + n;
    }
    
}

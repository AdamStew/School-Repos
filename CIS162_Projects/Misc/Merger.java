
/**
 * Write a description of class Merger here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Merger
{
    public static void main(String[] args){
        int[] first = {3, 4, 9, 13, 15, 22, 30, 35, 44, 50, 51, 53};
        int[] second = {1, 5, 8, 14, 20, 22, 33, 38, 41, 49};
        int[] third = merge(first,second);
        for(int v: third)
            System.out.print(v+" ");
        System.out.println();
    }

    public static int[] merge(int[] ary1, int[] ary2){
        int n = ary1.length+ary2.length;
        int[]comb=new int[n];
        for(int i=0, j=0, k=0; i<comb.length; i++){
            if(j<ary1.length && k==ary2.length){
                comb[i]=ary1[j];
                j++;
            }
            if(j==ary1.length && k<ary2.length){
                comb[i]=ary1[k];
                k++;
            }
            if(j<ary1.length && k<ary2.length){
                if(ary1[j]<ary2[k]){
                    comb[i]=ary1[j];
                    j++;
                }else{
                    comb[i]=ary2[k];
                    k++;
                }
            }
        }
        return comb;
    }
}

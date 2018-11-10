
/**
 * Write a description of class Quadratic here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Quadratic
{public static void main (String[] args)
    {double a = 1;
     double b = 4;
     double c = 4;
     
     double QUADRATIC_FORMULA_X = ((-1*b) + (Math.sqrt ((b * b - ((4 * a * c)))))) / (2 * a);
     double QUADRATIC_FORMULA_Y = ((-1*b) - (Math.sqrt ((b * b - ((4 * a * c)))))) / (2 * a);
     
     double x = QUADRATIC_FORMULA_X;
     double y = QUADRATIC_FORMULA_Y;
     
     System.out.println ("Answer is:" + x);
     System.out.println ("Answer is:" + y);
}
}


/**
 * Write a description of class AreaofaTriangle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AreaofaTriangle
{ public static void main (String[] args) {
    double a = 3;
    double b = 3;
    double c = 1;
    double s = (0.5*(a + b + c));
    double area = Math.sqrt((s*(s-a)*(s-b)*(s-c)));
    System.out.println ("The area of a triangle is: " + area);}
}

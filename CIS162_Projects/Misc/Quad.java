
/**
 * Write a description of class Quad here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Quad
{    
        private double a;
        private double b;
        private double c;
     
        public Quad () {
            a = 0;
            b = 0;
            c = 0;
        }
        
        public void setA (double pA) {
            a=pA;
        }
            
        public double getA() {
            return a; 
        }
            
        public void setB (double pB) {
            b=pB; 
        }
            
        public double getB() {
            return b; 
        }
            
        public void setC (double pC) {
            c=pC; 
        }
            
        public double getC() {
            return c; 
        }
            
        public double getRoot1() {
            return (-b + (Math.sqrt ((Math.pow(b,2) - ((4 * a * c)))))) / (2 * a); 
        }
            
        public double getRoot2() {
            return (-b - (Math.sqrt ((Math.pow(b,2) - ((4 * a * c)))))) / (2 * a); 
        }
            
        public String toString() {
            return a + "x^2" + b + "x" + c; 
        }
        
        public boolean isPositive() {
            if (getRoot1() > 0) {
                return true;
            }else{
                return false;
            }    
        }
        }       
            
        

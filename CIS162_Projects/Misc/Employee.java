import java.text.*;

public class Employee{

	private String name;
	private double hours;
	
  public Employee(){
    name = "";
    hours = 0;
  }
  
  public void setName(String pName){
    name = pName;
  }
  
  public void setHours(double pHours){
    hours = pHours;
  }
  
  public String getName(){
    return name;
  }
  
  public double getHours(){
    return hours;
  }
  
  public double getPay(){
    double pay = hours * 10.00;
    return pay;
  }

	public String toString(){
//		DecimalFormat df = new DecimalFormat("##0.0");
	  return name + "\t" + getPay();
	 }

	public static void main(String[] args){
		Employee emp1 = new Employee();
		emp1.setName("Jill");
		emp1.setHours(35);
		System.out.println(emp1.getName());
		System.out.println(emp1.getPay());
		System.out.println(emp1.toString());
	}
}

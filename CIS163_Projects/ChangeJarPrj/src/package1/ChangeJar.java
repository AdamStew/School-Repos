package package1;
import java.util.*;
import java.io.*;

public class ChangeJar {


	private int quarters;
	private int dimes;
	private int nickels;
	private int pennies;
	private ChangeJar jar;
	private static boolean flag;

	/*******************************************************************
	 * Default constructor that sets the ChangeJar to zero.  
	 ******************************************************************/
	public ChangeJar(){
		this.quarters = 0;
		this.dimes = 0;
		this.nickels = 0;
		this.pennies = 0;
	}
	public int getQuarters() {
		return quarters;
	}

	public void setQuarters(int quarters) {
		this.quarters = quarters;
	}

	public int getDimes() {
		return dimes;
	}

	public void setDimes(int dimes) {
		this.dimes = dimes;
	}

	public int getNickels() {
		return nickels;
	}

	public void setNickels(int nickels) {
		this.nickels = nickels;
	}

	public int getPennies() {
		return pennies;
	}

	public void setPennies(int pennies) {
		this.pennies = pennies;
	}
		
	/*******************************************************************
 	* A constructor that initializes the instance variables with the 
 	* provided value converted to quarters, dimes, nickels, and pennies.
 	* For example, if amount was 1.34 then you would have 5 quarters,
 	* 1 nickel, 4 pennies.   
 	* 
 	* @param amont - Double value.
 	* @throws IllegalArgumentException  if amount is negative.
 	*******************************************************************/

	public ChangeJar(double amount){
		if (amount < 0){
			throw new IllegalArgumentException();
		}else{
			this.quarters = (int)(amount / .25);
			amount = amount % .25;
			this.dimes = (int)(amount / .10);
			amount = amount % .10;
			this.nickels = (int)(amount / .05);
			amount = amount % .05;
			this.pennies = (int)(amount / .01);
		}
	}

	/*******************************************************************
	* A constructor that initializes the instance variables with the 
	* other ChangeJar parameter.  
 	* 
 	* @param other - ChangeJar value.
 	* @throws IllegalArgumentException  if any values are negative.
 	*******************************************************************/
	public ChangeJar(ChangeJar other){
		if (other.quarters < 0 || other.dimes < 0 || other.nickels < 0 
				|| other.pennies < 0){
			throw new IllegalArgumentException();
		}else{
			this.quarters = other.quarters;
			this.dimes = other.dimes;
			this.nickels = other.nickels;
			this.pennies = other.pennies;
		}
	}
	
	/*******************************************************************
	 * A constructor that accepts a string as a parameter with the 
	 * provided value converted to quarters, dimes, nickels, and 
	 * pennies.  For example, if amount was “1.34” then you would have 5
	 * quarters, 1 nickel, 4 pennies.   
	 * 
	 * @param amount - String type.
	 * @throws IllegalArgumentException  if any integer values are 
	 * negative.
	 * @throws NullPointerException  if the string is null.
	 * @throws NumberFormatException  if the string does not contain a 
	 * parsable double.  
	 ******************************************************************/
	public ChangeJar(String amount){
		double change = Double.parseDouble(amount);
		if (change < 0){
			throw new IllegalArgumentException();
		}else{
			this.quarters = (int)(change / .25);
			change = change % .25;
			this.dimes = (int)(change / .10);
			change = change % .10;
			this.nickels = (int)(change / .05);
			change = change % .05;
			this.pennies = (int)(change / .01);
		}
	}

	/*******************************************************************
	 * A method that returns true if “this” ChangeJar object is exactly 
	 * the same (in terms of the amount in the ChangeJar) as the other 
	 * object (Note: you must cast the other object as a ChangeJar 
	 * object). 
	 * 
	 * @param other - Object type.
	 * @return Boolean
	 ******************************************************************/
	public boolean equals(Object other){
		if((this.quarters == ((ChangeJar)other).quarters) && 
				(this.dimes == ((ChangeJar)other).dimes) && 
				(this.nickels == ((ChangeJar)other).nickels) && 
				(this.pennies == ((ChangeJar)other).pennies)){
			return true;
		}else{
			return false;
		}
	}

	/*******************************************************************
	 * A method that returns true if “this” ChangeJar object is exactly 
	 * the same (in terms of the amount in the ChangeJar) as the other 
	 * object. 
	 * 
	 * @param other - ChangeJar value.
	 * @return Boolean
	 * @throws IllegalArgumentException  if any values are negative.
	 ******************************************************************/
	public boolean equals(ChangeJar other){
		if (other.quarters < 0 || other.dimes < 0 || other.nickels < 0 
				|| other.pennies < 0){
			throw new IllegalArgumentException();
		}else{
			if((this.quarters == other.quarters) && (this.dimes == 
					other.dimes) && (this.nickels == other.nickels) 
					&& (this.pennies == other.pennies)){
				return true;
			}else{
				return false;
			}
		}
	}

	/*******************************************************************
	 * A static method that returns true if ChangeJar object jar1 is 
	 * exactly the same (in terms of amount in the ChangeJar)  as if 
	 * ChangeJar object jar2.
	 * 
	 * @param jar1 - ChangeJar value.
	 * @param jar2 - ChangeJar value.
	 * @return Boolean
	 * @throws IllegalArgumentException  if any ChangeJar values are 
	 * negative.
	 ******************************************************************/
	public boolean equals(ChangeJar jar1, ChangeJar jar2){
		if (jar1.quarters < 0 || jar1.dimes < 0 || jar1.nickels < 0 || 
				jar1.pennies < 0 || jar2.quarters < 0 || jar2.dimes < 0
				|| jar2.nickels < 0 || jar2.pennies < 0){
			throw new IllegalArgumentException();
		}else{
			if((jar1.quarters == jar2.quarters) && (jar1.dimes == 
					jar2.dimes) && (jar1.nickels == jar2.nickels) 
					&& (jar1.pennies == jar2.pennies)){
				return true;
			}else{
				return false;
			}
		}
	}

	/*******************************************************************
	 * A method that returns 1 if “this” ChangeJar object is greater 
	 * than (based upon the total in the ChargeJar) the other ChangeJar 
	 * object; returns -1 if the “this” ChangeJar object is less than 
	 * the other ChangeJar; returns 0 if the “this” ChangeJar object is 
	 * equal to the other ChangeJar object.
	 * 
	 * @param other - ChangeJar value.
	 * @return Integer
	 * @throws IllegalArgumentException  if any ChangeJar values are 
	 * negative.
	 ******************************************************************/
	public int compareTo(ChangeJar other){
		if (other.quarters < 0 || other.dimes < 0 || other.nickels < 0 
				|| other.pennies < 0){
			throw new IllegalArgumentException();
		}else{
			if((quarters > other.quarters) || (quarters == 
					other.quarters && dimes > other.dimes) || (quarters 
					== other.quarters && dimes == other.dimes && nickels
					> other.nickels) || (quarters == other.quarters && 
					dimes == other.dimes && nickels == other.nickels && 
					pennies > other.pennies)){
				return 1;
			}
			if((quarters < other.quarters) || (quarters == 
					other.quarters && dimes < other.dimes) || (quarters 
					== other.quarters && dimes == other.dimes	&& 
					nickels < other.nickels) || (quarters == 
					other.quarters && dimes == other.dimes && nickels 
					== other.nickels && pennies < other.pennies)){
				return -1;
			}else{
				return 0;
			}
		}
	}

	/*******************************************************************
	 * A method that returns 1 if  ChangeJar object jar1 is greater (in 
	 * terms of the amount in the ChangeJar) than ChangeJar object jar2; 
	 * returns -1 if the ChangeJar object jar1 is less than ChangeJar 
	 * jar2;  returns 0 if the ChangeJar object jar1 is equal to 
	 * ChangeJar object jar2.
	 * 
	 * @param jar1 - ChangeJar value.
	 * @param jar2 - ChangeJar value.
	 * @return Integer
	 * @throws IllegalArgumentException  if any ChangeJar values are
	 * negative
	 ******************************************************************/
	public static int compareTo(ChangeJar jar1, ChangeJar jar2){
		if (jar1.quarters < 0 || jar1.dimes < 0 || jar1.nickels < 0 || 
				jar1.pennies < 0 || jar2.quarters < 0 || jar2.dimes < 0
				|| jar2.nickels < 0 || jar2.pennies < 0){
			throw new IllegalArgumentException();
		}else{
			if((jar1.quarters > jar2.quarters) || (jar1.quarters == 
					jar2.quarters && jar1.dimes > jar2.dimes) || (
					jar1.quarters== jar2.quarters && jar1.dimes == 
					jar2.dimes && jar1.nickels > jar2.nickels) || 
					(jar1.quarters == jar2.quarters && jar1.dimes == 
					jar2.dimes && jar1.nickels == jar2.nickels && 
					jar1.pennies > jar2.pennies)){
				return 1;
			}
			if((jar1.quarters < jar2.quarters) || (jar1.quarters == 
					jar2.quarters && jar1.dimes < jar2.dimes) || 
					(jar1.quarters == jar2.quarters && jar1.dimes == 
					jar2.dimes && jar1.nickels < jar2.nickels) || 
					(jar1.quarters == jar2.quarters && jar1.dimes == 
					jar2.dimes && jar1.nickels == jar2.nickels && 
					jar1.pennies < jar2.pennies)){
				return -1;
			}else{
				return 0;
			}
		}
	}

	/*******************************************************************
	 * A method that subtracts the parameters from the “this” ChangeJar 
	 * object.  You may assume all of the parameter are positive.
	 * 
	 * @param quarters - Positive integer value.
	 * @param dimes - Positive integer value.
	 * @param nickels - Positive integer value. 
	 * @param pennies - Positive integer value.
	 * @throws IllegalArgumentException  if any integer values are 
	 * negative.
	 ******************************************************************/
	public void subtract(int quarters, int dimes, int nickels, 
			int pennies){
		if(flag == false){
			if (quarters < 0 || dimes < 0 || nickels < 0 || pennies < 0 
					|| (this.quarters - quarters < 0) || (this.dimes - 
					dimes < 0) || (this.nickels - nickels < 0) || (
					this.pennies - pennies < 0)){
				throw new IllegalArgumentException();
			}else{
				this.quarters -= quarters;
				this.dimes -= dimes;
				this.nickels -= nickels;
				this.pennies -= pennies;
			}
		}
	}

	/*******************************************************************
	 * A method that subtracts ChangeJar other to the “this” ChangeJar 
	 * object. 
	 * 
	 * @param other - ChangeJar type.
	 * @throws IllegalArgumentException  if any values are 
	 * negative.
	 ******************************************************************/
	public void subtract (ChangeJar other){
		if(flag == false){
			if (other.quarters < 0 || other.dimes < 0 || other.nickels 
					< 0 || other.pennies < 0 || (this.quarters - 
					other.quarters < 0) || (this.dimes - other.dimes < 
					0) || (this.nickels - other.nickels < 0) || (
					this.pennies - other.pennies < 0)){
				throw new IllegalArgumentException();
			}else{
				this.quarters -= other.quarters;
				this.dimes -= other.dimes;
				this.nickels -= other.nickels;
				this.pennies -= other.pennies;
			}
		}
	}
	
	/*******************************************************************
	 * A method that subtracts String amount to the “this” ChangeJar 
	 * object. 
	 * 
	 * @param amount - String type
	 * @throws IllegalArgumentException  if any integer values are 
	 * negative.
	 * @throws NullPointerException  if the string is null.
	 * @throws NumberFormatException  if the string does not contain a 
	 * parsable double.  
	 ******************************************************************/
	public void subtract(String amount){
		double change = Double.parseDouble(amount);
		if(flag == false){
			if((change < 0) || (this.quarters < 0) || (this.dimes < 0) 
					|| (this.nickels < 0) || (this.pennies < 0)){
				throw new IllegalArgumentException();
			}else{
				this.quarters = (this.quarters - ((int)(change / .25)));
				change = change % .25;
				this.dimes = (this.dimes - ((int)(change / .10)));
				change = change % .10;
				this.nickels = (this.nickels - ((int)(change / .05)));
				change = change % .05;
				this.pennies = (this.pennies - ((int)(change / .01)));
			}
		}
	}

	/*******************************************************************
	 * A method that decrements the “this” ChangeJar by 1 penny.
	 * 
	 * @throws IllegalArgumentException  if values are negative.
	 ******************************************************************/
	public void dec(){
		if(flag == false){
			if (pennies <= 0){
				throw new IllegalArgumentException();
			}else{
				this.pennies--;
			}
		}
	}

	/*******************************************************************
	 * A method that adds the parameters from the “this” ChangeJar 
	 * object.  You may assume all of the parameter are positive.
	 * 
	 * @param quarters - Integer value.
	 * @param dimes - Integer value.
	 * @param nickels - Integer value.
	 * @param pennies - Integer value.
	 * @throws IllegalArgumentException  if any integer values are 
	 * negative.
	 ******************************************************************/
	public void add(int quarters, int dimes, int nickels, int pennies){
		if(flag == false){
			if (quarters < 0 || dimes < 0 || nickels < 0 || pennies < 0)
			{
				throw new IllegalArgumentException();
			}else{
				this.quarters += quarters;
				this.dimes += dimes;
				this.nickels += nickels;
				this.pennies += pennies;
			}
		}
	}

	/*******************************************************************
	 * A method that add ChangeJar other to the “this” ChangeJar object. 
	 * 
	 * @param other - ChangeJar type.
	 * @throws IllegalArgumentException  if any integer values are 
	 * negative.
	 ******************************************************************/
	public void add(ChangeJar other){
		if(flag == false){
			if (other.quarters < 0 || other.dimes < 0 || other.nickels 
					< 0 || other.pennies < 0){
				throw new IllegalArgumentException();
			}else{
				this.quarters += other.quarters;
				this.dimes += other.dimes;
				this.nickels += other.nickels;
				this.pennies += other.pennies;
			}
		}
	}

	/*******************************************************************
	 * A method that add String amount to the "this" ChangeJar object.
	 * 
	 * @param amount - String type.
	 * @throws IllegalArgumentException  if any integer values are 
	 * negative.
	 * @throws NullPointerException  if the string is null.
	 * @throws NumberFormatException  if the string does not contain a 
	 * parsable double. 
	 ******************************************************************/
	
	public void add(String amount){
		double change = Double.parseDouble(amount);
		if(flag == false){
			if(change < 0){
				throw new IllegalArgumentException();
			}else{
				this.quarters = (this.quarters + ((int)(change / .25)));
				change = change % .25;
				this.dimes = (this.dimes + ((int)(change / .10)));
				change = change % .10;
				this.nickels = (this.nickels + ((int)(change / .05)));
				change = change % .05;
				this.pennies = (this.pennies + ((int)(change / .01)));
			}
		}
	}
	
	/*******************************************************************
	 * A method that increments the “this” ChangeJar by 1 penny.
	 ******************************************************************/
	public void inc(){
		if(flag == false){
			this.pennies++;
		}
	}

	/*******************************************************************
	 * Method that returns a string that represents a ChangeJar with the
	 * following format:  “10 quarters, 1 dime, 0 nickels, 1 penny”.  
	 * All variations of plural characters are accounted for.
	 ******************************************************************/
	public String toString(){
		if((this.quarters > 1 || this.quarters < 1) && (this.dimes > 1 
				|| this.dimes <1) && (this.nickels > 1 || this.nickels 
						< 1) && (this.pennies > 1 || this.pennies <1))
			return this.quarters + " quarters, " + this.dimes + 
					" dimes, " + this.nickels + " nickels, " + 
					this.pennies + " pennies.  \n";
		else if(this.quarters == 1 && (this.dimes > 1 || this.dimes <1)
				&& (this.nickels > 1 || this.nickels < 1) && 
				(this.pennies > 1 || this.pennies <1))
			return this.quarters + " quarter, " + this.dimes + 
					" dimes, " + this.nickels + " nickels, " + 
					this.pennies + " pennies.  \n";
		else if((this.quarters > 1 || this.quarters < 1) && this.dimes 
				== 1 && (this.nickels > 1 || this.nickels < 1) && 
				(this.pennies > 1 || this.pennies <1))
			return this.quarters + " quarters, " + this.dimes + 
					" dime, " + this.nickels + " nickels, " + 
					this.pennies + " pennies.  \n";
		else if((this.quarters > 1 || this.quarters < 1) && 
				(this.dimes > 1 || this.dimes <1) && this.nickels == 1 
				&& (this.pennies > 1 || this.pennies <1))
			return this.quarters + " quarters, " + this.dimes + 
					" dimes, " + this.nickels + " nickel, " + 
					this.pennies + " pennies.  \n";
		else if((this.quarters > 1 || this.quarters < 1) && (this.dimes
				> 1 || this.dimes <1) && (this.nickels > 1 || 
				this.nickels < 1) && this.pennies == 1)
			return this.quarters + " quarters, " + this.dimes + 
					" dimes, " + this.nickels + " nickels, " + 
					this.pennies + " penny.  \n";
		else if(this.quarters == 1 && this.dimes == 1 && (this.nickels 
				> 1 || this.nickels < 1) && (this.pennies > 1 || 
				this.pennies <1))
			return this.quarters + " quarter, " + this.dimes + " dime, "
					+ this.nickels + " nickels, " + this.pennies + 
					" pennies.  \n";
		else if(this.quarters == 1 && (this.dimes > 1 || this.dimes <1) 
				&& this.nickels == 1 && (this.pennies > 1 || 
				this.pennies <1))
			return this.quarters + " quarter, " + this.dimes + 
					" dimes, " + this.nickels + " nickel, " + 
					this.pennies + " pennies.  \n";
		else if(this.quarters == 1 && (this.dimes > 1 || this.dimes <1) 
				&& (this.nickels > 1 || this.nickels < 1) && 
				this.pennies == 1)
			return this.quarters + " quarter, " + this.dimes + 
					" dimes, " + this.nickels + " nickels, " + 
					this.pennies + " penny.  \n";
		else if((this.quarters > 1 || this.quarters < 1) && this.dimes 
				== 1 && this.nickels == 1 && (this.pennies > 1 || 
						this.pennies <1))
			return this.quarters + " quarters, " + this.dimes + 
					" dime, " + this.nickels + " nickel, " + 
					this.pennies + " pennies.  \n";
		else if((this.quarters > 1 || this.quarters < 1) && this.dimes 
				== 1 && (this.nickels > 1 || this.nickels < 1) && 
				this.pennies == 1)
			return this.quarters + " quarters, " + this.dimes + 
					" dime, " + this.nickels + " nickels, " + 
					this.pennies + " penny.  \n";
		else if((this.quarters > 1 || this.quarters < 1) && (this.dimes 
				> 1 || this.dimes <1) && this.nickels == 1 && 
				this.pennies == 1)
			return this.quarters + " quarters, " + this.dimes + 
					" dimes, " + this.nickels + " nickel, " + 
					this.pennies + " penny.  \n";
		else if(this.quarters == 1 && this.dimes == 1 && this.nickels ==
				1 && (this.pennies > 1 || this.pennies <1))
			return this.quarters + " quarter, " + this.dimes + " dime, "
					+ this.nickels + " nickel, " + this.pennies + 
					" pennies.  \n";
		else if(this.quarters == 1 && this.dimes == 1 && (this.nickels >
				1 || this.nickels < 1) && this.pennies == 1)
			return this.quarters + " quarter, " + this.dimes + " dime, "
					+ this.nickels + " nickels, " + this.pennies + 
					" penny.  \n";
		else if(this.quarters == 1 && (this.dimes > 1 || this.dimes <1) 
				&& this.nickels == 1 && this.pennies == 1)
			return this.quarters + " quarter, " + this.dimes + 
					" dimes, " + this.nickels + " nickel, " + 
					this.pennies + " penny.  \n";
		else if((this.quarters > 1 || this.quarters < 1) && this.dimes 
				== 1 && this.nickels == 1 && this.pennies == 1)
			return this.quarters + " quarters, " + this.dimes + 
					" dime, " + this.nickels + " nickel, " + 
					this.pennies + " penny.  \n";
		else if(this.quarters == 1 && this.dimes == 1 && this.nickels ==
				1 && this.pennies == 1)
			return this.quarters + " quarter, " + this.dimes + " dime, "
					+ this.nickels + " nickel, " + this.pennies + 
					" penny.  \n";
		else 
			return "";
	}

	/*******************************************************************
	 * A method that saves the “this” ChangeJar to a file; use the 
	 * parameter filename for the name of the file.
	 * 
	 * @param fileName - String type.
	 * @throws IOException  if the named file exists but is a directory 
	 * rather than a regular file, does not exist but cannot be created, 
	 * or cannot be opened for any other reason 
	 ******************************************************************/
	public void save(String fileName){
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter
					(fileName)));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		out.println(this.quarters + " " + this.dimes + " " 
				+ this.nickels + " " + this.pennies);
		out.close(); 

	}

	/*******************************************************************
	 * A  method that loads the “this” ChangeJar object from a file; use
	 * the parameter filename for the name of the file.
	 * 
	 * @param fileName - String type.
	 * @throws NullPointerException  If the pathname argument is null
	 * @throws InputMismatchException  if the next token does not match 
	 * the Integer regular expression, or is out of range 
	 * @throws NoSuchElementException  if input is exhausted 
	 * @throws IllegalStateException  if this scanner is closed  
	 ******************************************************************/
	public void load(String fileName){
		try{
			// open the data file
			Scanner fileReader = new Scanner(new File(fileName)); 

			// reads ints
			quarters = fileReader.nextInt();
			System.out.println ("Quarter(s) " + this.quarters);
			dimes = fileReader.nextInt();
			System.out.println ("Dime(s) " + this.dimes);
			nickels = fileReader.nextInt();
			System.out.println ("Nickel(s) " + this.nickels);
			pennies = fileReader.nextInt();
			System.out.println ("Penn/y/ies " + this.pennies);
		}

		// could not find file
		catch(FileNotFoundException error) {
			System.out.println("File not found ");
		}

		// problem reading the file
		catch(IOException error){
			System.out.println("Oops!  Something went wrong.");
		}

	}
	
	/*******************************************************************
	 * A method that turns ‘off’ (false) and ‘on’ (true) any subtract/
	 * add methods in ChangeJar based on the false of the variable 'on'.
	 *In other words, when false, prevents any subtract/add method from 
	 *changing (mutate) the state of the “this” object as it relates to 
	 *the amount in the ChangeJar.
	 * 
	 * @param on - Boolean type.
	 ******************************************************************/
	public static void mutate(Boolean on){
		if(!on){
			flag = true;
		}else{
			flag = false;
		}
	}

	public static void main(String [] args){
		ChangeJar s = new ChangeJar("2.82");
		System.out.println("2.82 Amount: \n" + s);

		s = new ChangeJar("8");
		System.out.println("8 Amount: \n" + s);

		s = new ChangeJar(".28");
		System.out.println(".28 Amount: \n" + s);

		ChangeJar s1 = new ChangeJar();
		System.out.println("0 Amount: \n" + s1);

		s1.add(1,1,1,100);
		System.out.println("1,1,1,100 Amount: \n" + s1);

		ChangeJar s2 = new ChangeJar(41.99);
		s2.add(0,0,0,99);
		for (int i = 0; i < 100; i++)
			s2.dec();
		System.out.println("amount: \n" + s2);
		
		ChangeJar s3 = new ChangeJar(1.00);
		System.out.println(s3.getQuarters() + "\n");
		
		ChangeJar s4 = new ChangeJar(1.00);
		s3.add(s4);
		System.out.println(s3.getQuarters() + "\n");
		
		ChangeJar s5 = new ChangeJar(1.01);
		s5.dec();
		System.out.println(s5.getPennies() + "\n");
		
		System.out.print(s3.compareTo(s5));
		System.out.print(s4.compareTo(s4));
		System.out.println(s5.compareTo(s3));
		
		s5.inc();
		System.out.println("\n" + s5.getPennies() + "\n");
		s5.inc();
		System.out.println(s5.getPennies() + "\n");
		
		mutate(false);
		s5.inc();
		System.out.println(s5.getPennies() + "\n");
		
		mutate(true);
		s5.inc();
		System.out.println(s5.getPennies() + "\n");
		
		mutate(false);
		s5.subtract(0,0,0,3);
		System.out.println(s5.getPennies() + "\n");
		
		mutate(true);
		s5.subtract(0,0,0,3);
		System.out.println(s5.getPennies() + "\n");
	}
}

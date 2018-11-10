package c452;

import java.io.*;
import java.util.*;

public class PartB {

	private static final int FRAME_TABLE_SIZE = 16;
	private static final int PAGE_TABLE_SIZE = 64;
	
	public static void main(String args[]) {
		Simulation sim = new Simulation(PAGE_TABLE_SIZE, FRAME_TABLE_SIZE);
		sim.receiveData("src/c452/input3a.data");
		try {
			
			BufferedReader brInput = new BufferedReader(new InputStreamReader(System.in));
			
			//Simulation start.
			System.out.println("Press enter to progress, or 'status' to display current system status.");
			while(!sim.isCompleted()) {
				String s = brInput.readLine();
				if(s == null)
					System.out.println("Please enter a valid input!");
				else if(s.trim().equals("")) //Progress to next instruction.
					sim.step();
				else if(s.trim().toLowerCase().equals("fault"))
					sim.stepUntilFault();
				else if(s.trim().toLowerCase().equals("complete"))
					sim.stepUntilCompleted();
				else if(s.trim().toLowerCase().equals("status")) //Print out status of tables.
					sim.printStatus();
				else if(s.trim().toLowerCase().equals("quit"))
					System.exit(0);
				else
					System.out.println("Please enter a valid input!");
			}
			sim.printFinalStats();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

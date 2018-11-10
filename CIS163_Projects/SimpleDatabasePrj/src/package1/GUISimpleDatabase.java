package package1;

import javax.swing.JFrame;

public class GUISimpleDatabase {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Simple Database");
		SimpleDatabasePanel panel = new SimpleDatabasePanel();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		
	}
	
}

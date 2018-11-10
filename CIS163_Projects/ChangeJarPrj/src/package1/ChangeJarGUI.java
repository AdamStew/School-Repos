package package1;

import javax.swing.JFrame;


public class ChangeJarGUI {
	public static void main(String[] args){
		JFrame frame = new JFrame("ChangeJar");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MyChangeJarPanel panel = new MyChangeJarPanel();
		frame.getContentPane().add(panel);
		
		frame.pack();
		frame.setSize(1200, 700);
		frame.setVisible(true);
	}
}

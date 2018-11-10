package package1;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

public class Surround {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Surround");
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Options");
		menuBar.add(menu);
		JMenuItem quitItem = new JMenuItem("Quit Game");
		JMenuItem newGameItem = new JMenuItem("New Game");
		menu.add(quitItem);
		menu.add(newGameItem);
		frame.setJMenuBar(menuBar);
		SurroundPanel panel = new SurroundPanel(quitItem, newGameItem);
		JScrollPane scrollPane = new JScrollPane(panel);
		frame.getContentPane().add(scrollPane);
		frame.pack();
		frame.setVisible(true);
	}
}

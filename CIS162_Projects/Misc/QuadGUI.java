import javax.swing.JFrame;

/**
 * Write a description of class QuadGUI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class QuadGUI
{
  public static void main (String[] args) {
   JFrame frame = new JFrame ("Quad");
   frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
   
   QuadPanel panel = new QuadPanel();
   
   frame.getContentPane().add(panel);
   frame.pack();
   frame.setVisible(true); 
}
}

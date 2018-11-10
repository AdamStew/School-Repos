import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Write a description of class QuadPanel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class QuadPanel extends JPanel
{  
    private JLabel inputLabela, inputLabelb, inputLabelc, outputLabel1, outputLabel2, resultLabel1; 
    private JLabel resultLabel2;
    private JTextField a, b, c;
    private JButton push;
    double pA, pB, pC;
    Quad quad1 = new Quad();
    
    public QuadPanel()
    {  
       inputLabela=new JLabel ("Coefficient of x^2: ");
       inputLabelb=new JLabel ("Cofficient of x: ");
       inputLabelc=new JLabel ("c: ");
       outputLabel1=new JLabel ("Answers: ");
       outputLabel2=new JLabel (" and ");
       resultLabel1=new JLabel ("---");
       resultLabel2=new JLabel ("---");
       push=new JButton ("Enter");
       push.addActionListener (new ButtonListener()); 
       a=new JTextField(5);
       b=new JTextField(5);
       c=new JTextField(5);
       
       
       add(inputLabela);
       add(a);
       add(inputLabelb);
       add(b);
       add(inputLabelc);
       add(c);
       add(push);
       add(outputLabel1);
       add(resultLabel1);
       add(outputLabel2);
       add(resultLabel2);
       
       
       setPreferredSize (new Dimension(500, 100));
       setBackground (Color.white);
    }
    private class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
        
         double A, B, C;
         
         String texta=a.getText();  //gets text from JTextFields
         String textb=b.getText();
         String textc=c.getText();
         
         A=Double.parseDouble(texta);  //Converts to double
         B=Double.parseDouble(textb);
         C=Double.parseDouble(textc);
         
         quad1.setA(A);  //Puts them into the quad1 object
         quad1.setB(B);
         quad1.setC(C);
         
         resultLabel1.setText(Double.toString(quad1.getRoot1()));  //Puts the results into the
         resultLabel2.setText(Double.toString(quad1.getRoot2())); //resultLabels
        }
    }
}

/**
 * GUI: A simple GUI for a simple Library
 * 
 * @author CIS162 
 * @version Spring/Summer 2014
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class GUI 
{
    JButton buttonAuthorContains,buttonTitleContains;
    JPanel panel;
    JTextArea results,parameter;
    Library library;
    JFrame myGUI;
    JLabel labelParameter,labelResults;

    public static void main(String args[]) {
        new GUI();
    }

    public GUI() {
        library = new Library();
        myGUI = new JFrame("A simple library");
        
        myGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        labelParameter = new JLabel("Input Parameter");
        labelResults = new JLabel("Results");
       
       
        buttonAuthorContains = new JButton("Author contains");
        buttonTitleContains = new JButton("Title contains");
        ButtonListener buttonListener = new ButtonListener();
        
        buttonAuthorContains.addActionListener(buttonListener);
        buttonTitleContains.addActionListener(buttonListener);
        
        results = new JTextArea(10,30);
        parameter = new JTextArea(1,12);
        
        panel.add(labelParameter);
        panel.add(parameter);
       
        panel.add(buttonAuthorContains);
         panel.add(buttonTitleContains);
        panel.add(labelResults);
        panel.add(results);
        myGUI.add(panel);
        myGUI.pack();
        myGUI.setSize(1000,300);
        myGUI.setVisible(true);

    }

    public class ButtonListener implements ActionListener{

        public void actionPerformed(ActionEvent event){

            if (event.getSource()== buttonAuthorContains) {
                String p = parameter.getText(); 
                results.setText(library.authorContains(p));
            }
            if (event.getSource()== buttonTitleContains) {
                String p = parameter.getText();
                results.setText(library.titleContains(p));
            }
        }
    }

}
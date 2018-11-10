import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.util.*;

/*************************************************************
 * GUI for a Zip Code Database
 * 
 * @author Scott Grissom
 * @version October 7, 2011
 ************************************************************/
public class GUI extends JPanel{

    /** the analyzer that doe all the real work */
    Game myGame;

    /** Buttons to initiate each action */
    JButton north, east, south, west, take, drop, look, hold, help, trade, starve;

    /** GUI frame */
    JFrame myGUI;
    JTextArea results;

    /** menu items */
    // if attempting the challenge activity
    JMenuBar menus;
    JMenu fileMenu;
    JMenuItem quitItem, newItem;

    /*****************************************************************
     * Main Method
     ****************************************************************/ 
    public static void main(String args[]){
        new GUI();
    }

    /*****************************************************************
     * constructor installs all of the GUI components
     ****************************************************************/    
    public GUI(){

        // establish the frame
        myGUI = new JFrame();
        myGUI.setSize(600,300);
        myGUI.setTitle("Grocery Shopping");    

        // create display area
        results = new JTextArea(30,60);
        JScrollPane scrollPane = new JScrollPane(results);
        myGUI.add(BorderLayout.CENTER, scrollPane);

        // create data entry panel
        help = new JButton("help");
        take = new JButton("take");
        drop = new JButton("drop");
        look = new JButton("look");
        hold = new JButton("holding");  
        //shrink = new JButton("shrink");
        trade = new JButton("trade");
        starve = new JButton("starve");

        JPanel actionPanel = new JPanel();
        actionPanel.add(new JLabel("Actions: "));
        actionPanel.add(help);
        actionPanel.add(take);
        actionPanel.add(drop);
        actionPanel.add(look); 
        actionPanel.add(hold);  
        //actionPanel.add(shrink);
        actionPanel.add(trade);
        actionPanel.add(starve);

        myGUI.add(BorderLayout.SOUTH, actionPanel);

        // create button panel
        JPanel directionPanel = new JPanel();
        directionPanel.setLayout(new BoxLayout(directionPanel, BoxLayout.Y_AXIS));
        north = new JButton("North");
        south = new JButton("South");
        east = new JButton("East");
        west = new JButton("West");
        directionPanel.add(new JLabel("Directions"));
        directionPanel.add(north);
        directionPanel.add(south);
        directionPanel.add(east);
        directionPanel.add(west);
        myGUI.add(BorderLayout.EAST, directionPanel);

        // register the listeners
        ButtonListener listener = new ButtonListener();
        help.addActionListener(listener);
        take.addActionListener(listener);
        drop.addActionListener(listener);
        look.addActionListener(listener);
        hold.addActionListener(listener);
        trade.addActionListener(listener);
        starve.addActionListener(listener);
        north.addActionListener(listener);
        south.addActionListener(listener);
        east.addActionListener(listener);
        west.addActionListener(listener);

        // set up File menu
        fileMenu = new JMenu("File");
        quitItem = new JMenuItem("Quit");
        newItem = new JMenuItem("New Game");
        fileMenu.add(newItem);
        fileMenu.add(quitItem);
        menus = new JMenuBar();
        myGUI.setJMenuBar(menus);
        menus.add(fileMenu);

        // register the menu items with the action listener
        quitItem.addActionListener(listener);
        newItem.addActionListener(listener);

        myGUI.setVisible(true);
        myGUI.pack();

        newGame();
    }

    private void gameOver(){
        help.setEnabled(false);
        take.setEnabled(false);
        drop.setEnabled(false);
        look.setEnabled(false);
        hold.setEnabled(false);
        trade.setEnabled(false);
        starve.setEnabled(false);
        north.setEnabled(false);
        south.setEnabled(false);
        east.setEnabled(false);
        west.setEnabled(false);
    }

    private void newGame(){
        myGame = new Game();
        help.setEnabled(true);
        take.setEnabled(true);
        drop.setEnabled(true);
        look.setEnabled(true);
        hold.setEnabled(true);
        trade.setEnabled(true);
        starve.setEnabled(true);
        north.setEnabled(true);
        south.setEnabled(true);
        east.setEnabled(true);
        west.setEnabled(true);
        results.setText("");
    }

    /*****************************************************************
     * This method is called when any button is clicked.  The proper
     * internal method is called as needed.
     * 
     * @param e the event that was fired
     ****************************************************************/       
    private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){

            // extract the button that was clicked
            JComponent buttonPressed = (JComponent) e.getSource();

            if (buttonPressed == quitItem){
                System.exit(1);
            }else if (buttonPressed == newItem){
                newGame();
            }else if (buttonPressed == east){
                myGame.move("East");
            }else if (buttonPressed == west){
                myGame.move("West");
            }else if (buttonPressed == north){
                myGame.move("North");
            }else if (buttonPressed == south){
                myGame.move("South");
            }else if (buttonPressed == take){
                myGame.take();
            }else if (buttonPressed == look){
                myGame.look();
            }else if (buttonPressed == hold){
                myGame.show();
            }else if (buttonPressed == help){
                myGame.help();
            }else if (buttonPressed == drop){
                String item = JOptionPane.showInputDialog(null, "What do you want to drop?");
                if (item != null)
                    myGame.drop(item);  
            }else if (buttonPressed == trade){
                myGame.trade();
            }else if (buttonPressed == starve){
                myGame.starve();
            }

            results.append("\n\n" + myGame.getMessage());   

            if (myGame.gameOver()){
                gameOver();
            }

        }
    }

}
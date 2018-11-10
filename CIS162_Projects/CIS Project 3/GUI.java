
/**
 * A very simple GUI.
 *
 * @author CIS162
 * @version Fall 2014
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class GUI
{
    JButton buttonListAll,buttonQueryByName,buttonNameContains,buttonQueryByHeight,
    buttonMaxHeight,buttonMinHeight,buttonAvgHeight;
    JPanel panel;
    JTextArea results,parameter;
    TreeDB treeDB;
    JFrame myGUI;
    JLabel labelParameter,labelResults;

    public static void main( String args[ ] ) {
        new GUI( );
    }

    public GUI( ) {
        treeDB = new TreeDB( );
        myGUI  = new JFrame( "A TreeDB" );

        myGUI.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        panel = new JPanel();
        labelParameter      = new JLabel( "Input Parameter" );
        labelResults        = new JLabel( "Results" );

        buttonListAll       = new JButton( "List All Trees" );
        buttonQueryByName   = new JButton( "Query by Name" );
        buttonQueryByHeight = new JButton( "Query by Height" );
        buttonMaxHeight     = new JButton( "Max Height" );
        buttonMinHeight     = new JButton( "Min Height" );
        buttonAvgHeight     = new JButton( "Average Height" );
        ButtonListener buttonListener = new ButtonListener( );

        buttonListAll.addActionListener( buttonListener );
        buttonQueryByName.addActionListener( buttonListener );
        buttonMaxHeight.addActionListener( buttonListener );
        buttonMinHeight.addActionListener( buttonListener );
        buttonAvgHeight.addActionListener( buttonListener );
        buttonQueryByHeight.addActionListener( buttonListener );

        results = new JTextArea( 10,20 );
        parameter = new JTextArea( 1,12 );

        panel.add( labelParameter );
        panel.add( parameter );

        panel.add( buttonListAll );
        panel.add( buttonQueryByName );
        panel.add( buttonQueryByHeight );
        panel.add( buttonMaxHeight);
        panel.add( buttonMinHeight);
        panel.add( buttonAvgHeight);
        panel.add( labelResults );
        panel.add( results );

        myGUI.add( panel );

        myGUI.pack( );
        myGUI.setSize( 280,300 );
        myGUI.setVisible( true );
    }

    public class ButtonListener implements ActionListener{
        public void actionPerformed( ActionEvent event ){
            
            if (event.getSource( ) == buttonQueryByName) {
                String p = parameter.getText();
                results.setText(treeDB.queryByName(p));
            }

            if (event.getSource( ) == buttonListAll) {
                results.setText(treeDB.toString());
            }

            if (event.getSource( ) == buttonQueryByHeight) {
                int p = Integer.parseInt(parameter.getText());
                results.setText(treeDB.queryByPossibleHeight(p));
            }

            if (event.getSource( ) == buttonMaxHeight) {
                results.setText(String.valueOf(treeDB.getMaxHeight()));
            }
            
            if (event.getSource( ) == buttonMinHeight) {
                results.setText(String.valueOf(treeDB.getMinHeight()));
            }
            
            if (event.getSource( ) == buttonAvgHeight) {
                results.setText(String.valueOf(treeDB.getAverageHeight()));
            }
        }
    }
}
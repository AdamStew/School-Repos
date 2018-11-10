/**
 * PhoneNumberDisplayPanel displays a phone number, which is to be shared by 
 * reference with both a Keypad and an auxiliary ControlPanel.
 * 
 * @author (your name) 
 * @version (10-20-2014)
 */
import javax.swing.*;
import java.awt.*;

public class PhoneNumberDisplayPanel extends JPanel
{

    /**
     * Constructor for a PhoneNumberDisplayPanel
     * 
     * @param  phoneNumberLabel   is the primary display in this project.
     */
    public PhoneNumberDisplayPanel( JLabel phoneNumberLabel )
    {
        setBorder( BorderFactory.createLineBorder( Color.MAGENTA, 3 ) );
        setBackground( Color.WHITE );

        
        /***********************************************************************
         * adds the phoneNumberLabel to this JPanel.
         */
        add(phoneNumberLabel);
    }

}

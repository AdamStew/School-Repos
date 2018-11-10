package camping;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.*;

/***********************************************************************
 * This class creates a dialog menu featuring all the requirements to 
 * make/schedule an RV at a reservation site
 * @author Frank Derry Wanye and Adam Stewart
 * @version 03/17/2015
 **********************************************************************/
public class DialogCheckInRV extends JDialog implements ActionListener{

	/** The text box that the user will enter their name for the site 
	 * class */
	private JTextField nameTxt;
	
	/** The text box that the user will enter their desired entering 
	 * date for the site class */
	private JTextField occupyedOnTxt;
	
	/** The text box that the user will enter their desired number of 
	 * days that they would like to stay */
	private JTextField stayingTxt;
	
	/** The text box that the user will enter their desired site 
	 * location number */
	private JTextField siteNumberTxt;
	
	/** The text box that the user will enter their desired amount of 
	 * power for their RV */
	private JTextField powerTxt;
	
	/** The OK button that will appear on the dialog for RV 
	 * reservations, which confirm their inputs */
	private JButton okButton;
	
	/** The cancel button that will appear on the dialog for RV 
	 * reservations, which cancels their current process */
	private JButton cancelButton;
	
	/** A boolean that determines if the user presses OK (true) or 
	 * cancel (false) */
	private boolean closeStatus;
	
	/** Boolean that determines the close status of the dialog box */
	public static final boolean OK = true;
	
	/** Boolean that determines the close status of the dialog box */
	public static final boolean CANCEL = false;

	/** A RV that gets all the parameters entered in the dialog box, 
	 * and then gets scheduled into the GUI */
	private RV unit;
	
	/*******************************************************************
	 * Constructor creates a new window containing information that 
	 * needs to be filled out in order to schedule a site with an RV
	 * @param paOccupy is the parent frame.
	 * @param d an object of the site class
	 ******************************************************************/
	public DialogCheckInRV(JFrame paOccupy, Site d) {
		
		super(paOccupy,true);
		
		setTitle("Reserve an RV");
		closeStatus = CANCEL;
		setSize(400,200);
		
		unit = (RV)d;
		
		//Prevent user from closing window
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		//Instantiate and display text input boxes
		JPanel textBoxes = new JPanel();
		textBoxes.setLayout(new GridLayout(5,2));
		
		//Text box for name, with the default of 'Jane Doe'
		textBoxes.add(new JLabel("Name of reserver:"));
		nameTxt = new JTextField("Jane Doe",30);
		textBoxes.add(nameTxt);
		
		//Text box for the site number, with the default of '1'
		textBoxes.add(new JLabel("Requested site number:"));
		siteNumberTxt = new JTextField("1",30);
		textBoxes.add(siteNumberTxt);
		
		//Text box for the scheduled arrival date, with the default of 
		//today's date
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date(); //Current Date
		textBoxes.add(new JLabel("Date arriving:"));
		occupyedOnTxt = new JTextField(df.format(date) + "",30);
		textBoxes.add(occupyedOnTxt);
		
		//Text box for the estimated length of stay, with the default 
		//of '3'
		textBoxes.add(new JLabel("Length of stay:"));
		stayingTxt = new JTextField("3",30);
		textBoxes.add(stayingTxt);
		
		//Text box for the desired amount of power, with the default 
		//of '50'
		textBoxes.add(new JLabel("Power required:"));
		powerTxt = new JTextField("50",30);
		textBoxes.add(powerTxt);
		
		//Sends the text box panel to the center
		getContentPane().add(textBoxes, BorderLayout.CENTER);
		
		//Separate JPanel for buttons
		JPanel buttons = new JPanel();
		
		//Instantiates buttons
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		
		//Adds buttons to button panel
		buttons.add(okButton);
		buttons.add(cancelButton);
		
		//Adds buttons to action listener
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		//Sends the buttons panel down under the text box panel
		getContentPane().add(buttons, BorderLayout.SOUTH);

		setSize(400,400);
		setVisible(true);
	}

	@Override
	/*******************************************************************
	 * Confirms or cancels the desired RV scheduling
	 * @param e is the act of clicking on the OK button or cancel button
	 ******************************************************************/
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == cancelButton)
			dispose();
		//Fills the Site if OK is clicked
		if(e.getSource() == okButton) {
			closeStatus = OK;
			//Sets the format in which the check in date must be entered
			SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
			fmt.setLenient(false);
			Date date;
			GregorianCalendar occupied = new GregorianCalendar();
			//siteNumber, duration, numStaying hold values for parsing
			int siteNumber;
			int duration;
			int power;
			//Prints an error message if the name reserving field
			//contains anything other than letters
			if(nameTxt.getText().matches("[a-zA-Z ]+"))
				unit.setNameReserving(nameTxt.getText());
			else {
				JOptionPane.showMessageDialog(null, "Please use only "
						+ "letters and spaces when entering the name.");
				return;
			}
			//Prints error message if date entered is in the wrong
			//format
			try {
				date = fmt.parse(occupyedOnTxt.getText());
				occupied.setTime(date);
				unit.setCheckIn(occupied);
			} catch (ParseException exception) {
				JOptionPane.showMessageDialog(null, "Please enter a "
						+ "valid date with the format mm/dd/yyyy");
				return;
			}
			//Prints an error message if the site number field is in the
			//wrong format, or not between 1 and 5
			if(siteNumberTxt.getText().matches("[0-9]+"))
				siteNumber = Integer.parseInt(siteNumberTxt.getText());
			else {
				JOptionPane.showMessageDialog(null, "Please enter an "
						+ "integer between 1 and 5");
				return;
			}
			if(siteNumber < 1 || siteNumber > 5){
				JOptionPane.showMessageDialog(null, "Please enter an "
						+ "integer between 1 and 5");
				return;
			}
			//Prints an error message if the days staying field is in
			//the wrong format or too large or too small
			if(stayingTxt.getText().matches("[0-9]+"))
				duration = Integer.parseInt(stayingTxt.getText());
			else {
				JOptionPane.showMessageDialog(null, "Please enter a "
						+ "positive integer.");
				return;
			}
			if(duration < 1 || duration > 100){
				JOptionPane.showMessageDialog(null, "Please enter a "
						+ "positive integer.");
				return;
			}
			//Prints an error message if the power needed field
			//is in the wrong format or too large or too small
			if(powerTxt.getText().matches("[0-9]+"))
				power = Integer.parseInt(powerTxt.getText());
			else {
				JOptionPane.showMessageDialog(null, "Please enter a "
						+ "value of 30, 40, 50.");
				return;
			}
			if(power != 30 && power != 40 && power != 50) {
				JOptionPane.showMessageDialog(null, "Please enter a "
						+ "value of 30, 40, 50.");
				return;
			}
			unit.setSiteNumber(siteNumber);
			unit.setDaysStaying(duration);
			unit.setPower(power);
			dispose();
		}
		
	}
	
	/*******************************************************************
	 * Checks to see what the current status of the dialog is
	 * @return true if the OK button is hit, and false if the cancel 
	 * button is hit
	 ******************************************************************/
	public boolean getCloseStatus() {
		return closeStatus;
	}
	
}

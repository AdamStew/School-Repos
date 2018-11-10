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
 * make/schedule a tent at a reservation site
 * @author Frank Derry Wanye and Adam Stewart
 * @version 03/17/2015
 **********************************************************************/
public class DialogUpdateSite extends JDialog 
		implements ActionListener{

	/** The text box that the user will enter their updated name for the
	 * site class */
	private JTextField nameTxt;

	/** The text box that the user will enter their desired updated 
	 * entering date for the site class */
	private JTextField occupyedOnTxt;

	/** The text box that the user will enter their desired updated 
	 * number of days that they would like to stay */
	private JTextField stayingTxt;

	/** The text box that the user will enter their desired updated site 
	 * location number */
	private JTextField siteNumberTxt;

	/** The text box that the user will enter their desired updated 
	 * amount of power for their RV or updated amount of people for 
	 * their tent site  */
	private JTextField special;

	/** The OK button that will appear on the dialog for site
	 * reservations, which confirm their inputs */
	private JButton okButton;

	/** The cancel button that will appear on the dialog for site 
	 * reservations, which cancels their current process */
	private JButton cancelButton;

	/** A boolean that determines if the user presses OK (true) or 
	 * cancel (false) */
	private boolean closeStatus;

	/** A site that gets all the parameters entered in the dialog box, 
	 * and then gets scheduled into the GUI */
	private Site unit;

	/** Boolean that determines the close status of the dialog box */
	public static final boolean OK = true;
	
	/** Boolean that determines the close status of the dialog box */
	public static final boolean CANCEL = false;

	/*******************************************************************
	 * Constructor creates a new window containing information that 
	 * needs to be filled out in order to schedule a site with a tent
	 * @param paOccupy is the parent frame.
	 * @param d an object of the site class
	 ******************************************************************/
	public DialogUpdateSite(JFrame paOccupy, Site d) {

		super(paOccupy,true);

		unit = d;

		setTitle("Update a Site");
		closeStatus = CANCEL;
		setSize(400,200);


		//Prevent user from closing window
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		//Instantiate and display text input boxes
		JPanel textBoxes = new JPanel();
		textBoxes.setLayout(new GridLayout(5,2));

		//Text box for name, with the default of their current name of 
		//reserver
		textBoxes.add(new JLabel("Name of reserver:"));
		nameTxt = new JTextField(d.getNameReserving(),30);
		textBoxes.add(nameTxt);

		//Text box for the site number, with the default of their 
		//current site number
		textBoxes.add(new JLabel("Requested site number:"));
		siteNumberTxt = new JTextField(""+d.getSiteNumber(),30);
		textBoxes.add(siteNumberTxt);

		//Text box for the scheduled arrival date, with the default of 
		//their current scheduled date of arrival
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date date = d.getCheckIn().getTime();
		textBoxes.add(new JLabel("Date arriving:"));
		occupyedOnTxt = new JTextField(df.format(date) + "",30);
		textBoxes.add(occupyedOnTxt);

		//Text box for the estimated length of stay, with the default 
		//of their current estimated length of stay
		textBoxes.add(new JLabel("Length of stay:"));
		stayingTxt = new JTextField(""+d.getDaysStaying(),30);
		textBoxes.add(stayingTxt);

		//Text box for the desired amount of power, with the default 
		//of their current amount of power desired, assuming what 
		//they're updating is of the type RV
		if(d instanceof RV) {
			textBoxes.add(new JLabel("Power needed:"));
			special = new JTextField(""+((RV)d).getPower(),30);
		}
		else {
			//Text box for the desired amount of people staying, with 
			//the default of their current estimated number of people 
			//staying, assuming what they're updating is of the type 
			//tent
			textBoxes.add(new JLabel("Number of people staying:"));
			special = new JTextField(""+((Tent)d).getNumOfTenters()
					,30);
		}
		textBoxes.add(special);

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
			int specialValue;
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
			unit.setSiteNumber(siteNumber);

			unit.setDaysStaying(duration);
			//Prints an error message if the number of tenters field
			//is in the wrong format or too large or too small
			if(special.getText().matches("[0-9]+"))
				specialValue = Integer.parseInt(special.getText());
			else {
				JOptionPane.showMessageDialog(null, "Please enter an "
						+ "integer between 1 and 100.");
				return;
			}
			if(unit instanceof Tent) {
				if(specialValue < 1 || specialValue > 101) {
					JOptionPane.showMessageDialog(null, "Please enter "
							+ "an integer between 1 and 100.");
					return;
				}
				((Tent)unit).setNumOfTenters(specialValue);
			}
			else {
				if(specialValue != 30 && specialValue != 40 && 
						specialValue != 50) {
					JOptionPane.showMessageDialog(null, "Please enter"
							+ " a value of 30, 40 or 50.");
					return;
				}
				((RV)unit).setPower(specialValue);

			}
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


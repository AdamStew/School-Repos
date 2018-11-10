package camping;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

/***********************************************************************
 * This class searches through the list of sites based on name, date,
 * site number, type of site or days for which the site is reserved,
 * and displays the data on those sites in a separate window.
 * @author Frank Derry Wanye and Adam Stewart
 * @version 03/17/2015
 **********************************************************************/
public class DialogSearch extends JDialog implements 
		ActionListener{

	/** Container for the information on sites */
	private JPanel listPanel;

	/** Button used to close the window */
	private JButton okButton;

	/** Contains the information to be displayed in listPanel */
	private ArrayList<String> data;

	/** The date format used throughout the project */
	private final SimpleDateFormat df = new SimpleDateFormat(
			"MM/dd/yyyy");

	/** Contains the search parameter */
	private String search;
	
	/*******************************************************************
	 * Constructor creates a new window containing information on sites
	 * based on a search parameter
	 * @param paOccupy is the parent frame.
	 * @param sites is the list of sites to be searched through.
	 ******************************************************************/
	public DialogSearch(JFrame paOccupy, ArrayList<Site> sites) {

		//Parent constructor, does not allow parent frame to be
		//accessed while this window is open
		super(paOccupy,true);

		//Lets user select type of search and type in search parameter
		SearchTypeDialog dialog = new SearchTypeDialog(this);

		//Obtains the search parameter
		search = dialog.getSearchArg();

		GridLayout layout;

		listPanel = new JPanel();
		listPanel.setBackground(Color.WHITE);

		int rows;

		data= new ArrayList<String>();

		//Searches through list of sites and adds data on sites that 
		//match the search criteria to the data list
		if(dialog.getSearchBy() == 0) {
			setTitle("Sites reserved by people whose name "
					+ "contains: " + search);
			for(Site thisSite: sites) {
				if(thisSite.getNameReserving().contains(search))
					addLine(thisSite);
			}
		}
		if(dialog.getSearchBy() == 1) {
			Date date;
			try {
				date = df.parse(search);
				setTitle("Camp status given the date: "+ 
						df.format(date));
				for(Site thisSite: sites) {
					if(date.after(thisSite.getCheckIn().getTime())) {
						String name = thisSite.getNameReserving();
						String checkIn = "Checked in: " + df.format(
								thisSite.getCheckIn().getTime());
						String siteNumber = "Site # " + 
								thisSite.getSiteNumber();
						String daysStaying = "Estimated days: " + 
								thisSite.getDaysStaying();
						String daysRemaining = "Days remaining: ";
						GregorianCalendar newDate = 
								new GregorianCalendar();
						newDate.setTime(date);
						long span = thisSite.getCheckOut().
								getTimeInMillis() - 
								newDate.getTimeInMillis();
						GregorianCalendar c3 = new GregorianCalendar();
						c3.setTimeInMillis(span);
						daysRemaining += (int)(c3.getTimeInMillis()/
								(1000*60*60*24));
						data.add(name);
						data.add(checkIn);
						data.add(siteNumber);
						data.add(daysStaying);
						data.add(daysRemaining);
					}
				}
			} //The catch statement was already checked in an earlier
			//method, so in theory this should never be executed
			catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(dialog.getSearchBy() == 2) {
			setTitle("Data on reservations for this site: " + search);
			int siteNmbr = Integer.parseInt(search);
			for(Site thisSite: sites) {
				if(thisSite.getSiteNumber() == siteNmbr)
					addLine(thisSite);
			}
		}
		if(dialog.getSearchBy() == 3) {
			setTitle("Data on reservations for " + search + "s");
			for(Site thisSite: sites) {
				if(search.toLowerCase().equals("tent") && 
						thisSite instanceof Tent)
					addLine(thisSite);
				if(search.toLowerCase().equals("rv") && 
						thisSite instanceof RV) 
					addLine(thisSite);
			}
		}
		if(dialog.getSearchBy() == 4) {
			setTitle("Data on reservations for a minimum of " + search
					+ " days");
			int duration = Integer.parseInt(search);
			for(Site thisSite: sites) {
				if(thisSite.getDaysStaying() >= duration)
					addLine(thisSite);
			}
		}
		
		//Determines the number of columns needed for the layout
		if(dialog.getSearchBy()==1){
			rows = data.size()/5;
			layout = new GridLayout(rows,5);
		}
		else {
			rows = data.size()/4;
			layout = new GridLayout(rows,4);
		}

		if(data.size()==0) {
			JOptionPane.showMessageDialog(null, "No sites matched your "
					+ "search.");
			return;
		}

		setSize(1200,(20*rows) + 60);
		listPanel.setLayout(layout);

		//Displays search result data in JLabels
		for(String thisString: data) {
			JLabel label = new JLabel(thisString);
			listPanel.add(label);
		}

		getContentPane().add(listPanel, BorderLayout.CENTER);
		okButton = new JButton("Ok");
		okButton.addActionListener(this);
		getContentPane().add(okButton, BorderLayout.SOUTH);

		setVisible(true);
	}

	@Override
	/*******************************************************************
	 * Closes the dialog frame when the Ok button is clicked
	 * @param e is the act of clicking on the Ok button
	 ******************************************************************/
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == okButton) {
			dispose();
		}
	}

	/*******************************************************************
	 * Method used to obtain the name of the person reserving, check in 
	 * date, site number and estimated stay duration of a site which 
	 * matches the search criteria, convert the data to strings and add
	 * them to the data list
	 * @param site is a site that matches search criteria 
	 ******************************************************************/
	private void addLine(Site site) {
		String name = site.getNameReserving();
		String checkIn = "Checked in: " + df.format(
				site.getCheckIn().getTime());
		String siteNumber = "Site # " + 
				site.getSiteNumber();
		String daysStaying = "Estimated days: " + 
				site.getDaysStaying();
		data.add(name);
		data.add(checkIn);
		data.add(siteNumber);
		data.add(daysStaying);
	}

	/*******************************************************************
	 * This class allows the user to select the type of search the user
	 * wants to conduct, and type in the search parameters
	 * @author Frank Derry Wanye, Adam Stewart
	 * @version 03/17/2015
	 ******************************************************************/
	private class SearchTypeDialog extends JDialog implements 
	ActionListener {

		/** The container for the tabs */
		private JTabbedPane panel;
		
		/** Buttons used to set search parameter and close the dialog */
		private JButton[] okButtons;
		
		/** Store content of the dialog's tabs */
		private JComponent[] tabs;
		
		/** Store the instructions for entering search parameters */
		private JLabel[] labels;
		
		/** Allow user to type in search parameters */
		private JTextField[] texts;
		
		/** Determines whether the dialog should close or not */
		private boolean disposable = false;

		/** Stores an integer that represents what kind of argument the
		 * user wants to search by.
		 * 0 = search by name
		 * 1 = search by date
		 * 2 = search by site number
		 * 3 = search by type of site
		 * 4 = search by duration */
		private int searchBy;

		/** Stores the search argument that the user enters */
		private String searchArg;

		/***************************************************************
		 * Constructor creates a tabbed window allowing the user to 
		 * choose the type of and type in the search parameter
		 * @param dialog is the parent frame of the dialog
		 **************************************************************/
		public SearchTypeDialog(JDialog dialog) {

			//Parent constructor, does not allow parent frame to be
			//accessed while this dialog is open
			super(dialog, true);

			panel = new JTabbedPane();

			okButtons = new JButton[5];

			labels = new JLabel[5];

			texts = new JTextField[5];

			tabs = new JPanel[5];

			labels[0] = new JLabel("Enter the name or part of name you"
					+ " want to search by:");
			labels[1] = new JLabel("Enter the date you want to search"
					+ " by:");
			labels[2] = new JLabel("Enter the number of the site you"
					+ " want to search by:");
			labels[3] = new JLabel("Enter tent if you want to search"
					+ " by tent or RV if you want to search by RV:");
			labels[4] = new JLabel("Enter the minimum duration you "
					+ "want to search by:");

			//Adds the labels, text fields and ok buttons to the tabs
			for(int i = 0; i < 5; i++) {
				tabs[i] = new JPanel();
				tabs[i].setLayout(new BoxLayout(tabs[i],
						BoxLayout.PAGE_AXIS));
				okButtons[i] = new JButton("Ok");
				okButtons[i].addActionListener(this);
				texts[i] = new JTextField("",30);
				tabs[i].add(labels[i]);
				tabs[i].add(texts[i]);
				tabs[i].add(okButtons[i]);
			}

			//Adds the tabs to the tabbed panel
			panel.addTab("Name", tabs[0]);
			panel.addTab("Date", tabs[1]);
			panel.addTab("Site", tabs[2]);
			panel.addTab("Type of Site", tabs[3]);
			panel.addTab("Days Staying", tabs[4]);

			//Sets the default values of the text fields
			texts[0].setText("Jane Doe");
			Date date = new Date();
			texts[1].setText(df.format(date));
			texts[2].setText("1");
			texts[3].setText("Tent");
			texts[4].setText("3");

			getContentPane().add(panel);
			setSize(400,200);
			setVisible(true);
		}

		@Override
		/***************************************************************
		 * Determines the search type and performs user input checking
		 * based on which tab's ok button is clicked. Closes the dialog
		 * if the user input is correct after ok button is clicked
		 * @param e is the act of clicking on the Ok button
		 **************************************************************/
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == okButtons[0]) {
				if(texts[0].getText().matches("[a-zA-Z ]+")){
					searchArg = texts[0].getText();
					searchBy = 0;
					disposable = true;
				}
				else {
					JOptionPane.showMessageDialog(null,"Please use "
							+ "only letters and spaces when entering"
							+ " the name.");
					return;
				}
			}
			if(e.getSource() == okButtons[1]) {
				try{
					SimpleDateFormat df = new SimpleDateFormat(
							"MM/dd/yyyy");
					df.setLenient(false);
					df.parse(texts[1].getText());
					searchArg = texts[1].getText();
					searchBy = 1;
					disposable = true;
				}
				catch (ParseException exception) {
					JOptionPane.showMessageDialog(null, "Please enter a"
							+ " valid date with the format mm/dd/yyyy");
					return;
				}
			}
			if(e.getSource() == okButtons[2]) {
				if(texts[2].getText().matches("[1-5]")){
					searchArg = texts[2].getText();
					searchBy = 2;
					disposable = true;
				}
				else{
					JOptionPane.showMessageDialog(null, "Please enter"
							+ " an integer between 1 and 5.");
					return;
				}
			}
			if(e.getSource() == okButtons[3]) {
				if(texts[3].getText().toLowerCase().equals("tent")){
					searchArg = texts[3].getText().toLowerCase();
					searchBy = 3;
					disposable = true;
				}
				else {
					if(texts[3].getText().toLowerCase().equals("rv")){
						searchArg = texts[3].getText().toLowerCase();
						searchBy = 3;
						disposable = true;
					}
					else{
						JOptionPane.showMessageDialog(null, "Please "
								+ " enter \"Tent\" or \"RV\".");
						return;
					}
				}
			}
			if(e.getSource() == okButtons[4]) {
				if(texts[4].getText().matches("[1-9][0-9]*")){
					searchArg = texts[4].getText();
					searchBy = 4;
					disposable = true;
				}
				else{
					JOptionPane.showMessageDialog(null, "Please enter"
							+ " a positive integer.");
					return;
				}
			}
			if(disposable == true)
				dispose();
		}

		/***************************************************************
		 * Returns the type of search the user wants to perform
		 * 0 = search by name
		 * 1 = search by date
		 * 2 = search by site number
		 * 3 = search by type of site
		 * 4 = search by duration
		 * @return 0,1,2,3,4
		 **************************************************************/
		public int getSearchBy() {
			return searchBy;
		}

		/***************************************************************
		 * Returns the search parameter the user typed in
		 * @return a name, a date, or a number
		 **************************************************************/
		public String getSearchArg() {
			return searchArg;
		}

	}

}

package camping;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

/***********************************************************************
 *The entire GUI (reservation application) and all of the JMenuItems 
 *with their actions in the ActionListener
 * @author Frank Derry Wanye, Adam Stewart
 * @version 03/17/2015
 **********************************************************************/
public class GUICampingReg extends JFrame implements ActionListener{

	/** Top bar of the GUI that holds the JMenus file, checkIn, and 
	 * options */
	private JMenuBar menuBar;
	
	/** JMenu that opens buttons to JMenuItems saveText, openText, 
	 * saveSerial, openSerial, exit, and search*/
	private JMenu file;
	
	/** JMenu that opens buttons to JMenuItems checkInTent and 
	 * checkInRV*/
	private JMenu checkIn;
	
	/** JMenu that opens buttons to JMenuItems checkOut, delete, and 
	 * update */
	private JMenu options;
	
	/** JMenuItem that's within the options JMenu */
	private JMenuItem checkOut;
	
	/** JMenuItem that's within the file JMenu */
	private JMenuItem saveText;
	
	/** JMenuItem that's within the file JMenu */
	private JMenuItem openText;
	
	/** JMenuItem that's within the file JMenu */
	private JMenuItem saveSerial;
	
	/** JMenuItem that's within the file JMenu */
	private JMenuItem openSerial;
	
	/** JMenuItem that's within the file JMenu */
	private JMenuItem exit;
	
	/** JMenuItem that's within the checkIn JMenu */
	private JMenuItem checkInTent;
	
	/** JMenuItem that's within the checkIn JMenu */
	private JMenuItem checkInRV;
	
	/** JMenuItem that's within the options JMenu */
	private JMenuItem delete;
	
	/** JMenuItem that's within the options JMenu */
	private JMenuItem update;
	
	/** JMenuItem that's within the file JMenu */
	private JMenuItem search;
	
	/** JTable that displays the rows and columns of site information*/
	private JTable table;
	
	/** Contains abstract table model with an ArrayList of sites */
	private SiteModel model;
	
	/** Scroll bar to navigate GUI */
	private JScrollPane scrollPane;
	
	/** File chooser that's used for picking a file for opening text and
	 *  serial files */
	final private JFileChooser fc = new JFileChooser();

	/*******************************************************************
	 * Opens GUI with JTable, JMenuBar, JMenus, JMenuItems, and a 
	 * SiteModel so users can navigate and operate the camp reservation
	 *  application
	 ******************************************************************/
	public GUICampingReg() {
		//Declaring the menu bar
		menuBar = new JMenuBar();

		//Declaring the menus
		file = new JMenu("File");
		checkIn = new JMenu("Check In");
		options = new JMenu("Site Options");

		//Declaring the menu items
		checkOut = new JMenuItem("Check Out");
		saveText = new JMenuItem("Save as Text");
		openText = new JMenuItem("Open Text File");
		saveSerial = new JMenuItem("Save as Serial");
		openSerial = new JMenuItem("Open Serial File");
		checkInTent = new JMenuItem("Check into a Tent");
		checkInRV = new JMenuItem("Check into an RV");
		update = new JMenuItem("Update Site");
		delete = new JMenuItem("Delete Site");
		search = new JMenuItem("Search Sites");
		exit = new JMenuItem("Exit");

		//Adding the menus to the menu bar
		menuBar.add(file);
		menuBar.add(checkIn);
		menuBar.add(options);

		//Adding the menu items to the menus
		file.add(saveText);
		file.add(openText);
		file.add(saveSerial);
		file.add(openSerial);
		file.add(search);
		file.add(exit);
		checkIn.add(checkInTent);
		checkIn.add(checkInRV);
		options.add(checkOut);
		options.add(update);
		options.add(delete);

		//Adding action listeners to all menu items
		saveText.addActionListener(this);
		openText.addActionListener(this);
		saveSerial.addActionListener(this);
		openSerial.addActionListener(this);
		exit.addActionListener(this);
		checkInTent.addActionListener(this);
		checkInRV.addActionListener(this);
		checkOut.addActionListener(this);
		search.addActionListener(this);
		update.addActionListener(this);
		delete.addActionListener(this);

		setJMenuBar(menuBar);
		model = new SiteModel();
		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		add(scrollPane);

		setVisible(true);
		setSize(800,500);
	}

	/*******************************************************************
	 * Starts the GUI
	 * @param args argument that runs the GUI
	 ******************************************************************/
	public static void main(String[] args) {
		new GUICampingReg();
	}

	@Override
	/*******************************************************************
	 * Runs methods when JMenuItems are clicked
	 * @param e is the act of clicking on the saveText, openText, 
	 * saveSerial, openSerial, exit, checkInTent, checkOut, checkInRV, 
	 * delete, search, or update JMenuItems
	 ******************************************************************/
	public void actionPerformed(ActionEvent e) {
		JMenuItem menu = (JMenuItem) e.getSource();
		if(menu == saveText) {
			String filename = JOptionPane.showInputDialog(
					"Enter file name"); //Name that you'd like to save
			model.saveText(filename);  //the text file as
		}
		if(menu == openText) {
			int returnVal = fc.showOpenDialog(this); //file chooser to 
													//open desired file
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				model.loadText(file);  //loads text file
			}
		}
		if(menu == saveSerial) {
			String filename = JOptionPane.showInputDialog(
					"Enter file name"); //Name that you'd like to save
			model.saveSerial(filename);//the serial file as
		}
		if(menu == openSerial) {
			int returnVal2 = fc.showOpenDialog(this); //file chooser to
													 //open desired file
			if (returnVal2 == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				model.loadSerial(file);  //loads serial file
			}
		}
		if(menu == exit) {
			System.exit(0); //exits GUI
		}
		if(menu == checkInTent) {
			Tent tent = new Tent();
			DialogCheckInTent dia = new DialogCheckInTent(this, tent);
			if (dia.getCloseStatus() == dia.OK) { //gets info from 
				model.addSite(tent);             //tent dialog and adds
			}									//it to the Site Model
			revalidate();  //Refreshes
			repaint(); 
		}
		if(menu == checkOut) {
			model.checkOut(table.getSelectedRow()); //checks out on the
		}                                          //selected row
		if(menu == checkInRV) {
			RV rv = new RV();
			DialogCheckInRV dia = new DialogCheckInRV(this, rv);
			if (dia.getCloseStatus() == dia.OK) { //gets info from
				model.addSite(rv);				 //RV dialog and adds
			}									//it to the Site Model
			revalidate();  //Refreshes
			repaint();
		}
		if(menu == delete) {
			model.removeSite(table.getSelectedRow()); //deletes the 
		}											 //selected row
		if(menu == search) {
			DialogSearch dialog = new DialogSearch( //gets info from
					this, model.getAllSites());    //the search dialog
		}
		if(menu == update) {  //updates the current selected row
			Site site = model.getSite(table.getSelectedRow()); 
			DialogUpdateSite dia = new DialogUpdateSite(this, site);
			if (dia.getCloseStatus() == dia.OK) {//and adds to SiteModel
				model.updateSite(table.getSelectedRow(),site);
				revalidate();  //Refreshes
				repaint();
			}
			
		}
	}
}

package c452;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

/********************************************************************************************************************
 * @author Adam Stewart																								*
 *																													*
 * GUI for a Page Replacement simulation.  All the user has to do is select a file, and step along as they like.	*
 ********************************************************************************************************************/
public class GUI extends JFrame implements ActionListener {

	private JPanel frameTablePanel; //Panel to keep track of Frame Table stuff.
	private JPanel buttonPanel; //Panel to have interactive data and buttons.
	private JTabbedPane pageTablePane; //Tabs to keep track of each separate Page Table stuff.
	private JLabel currentExecution; //Label to display the current next execution instruction.
	private JLabel[] frameTable; //Array of Labels to display the Frame Table information.
	private JLabel[][] pageTables; //2D-Array of Labels to display the Page Tables of information.
	private JButton step; //Button to step once in the instructions.
	private JButton stepFault; //Button to keep stepping automatically until encountering a Page Fault.
	private JButton stepComplete; //Button to keep stepping until the simulation is 100% complete.
	private JMenuBar menuBar; //Menu bar to hold certain options.
	private JMenu menu; //Menu bar icon that holds all menu options.
	private JMenuItem newSim; //Menu bar item that allows the user to select a new simulation to run.
	private Simulation sim; //Simulation object that runs the inter-workings of the actual simulation.
	private int pageTableSize; //Variable for the user to select the desired Page Table size (not used currently).
	private int frameTableSize; //Variable for the user to select the desired Frame Table size (not used currently).
	private String path; //String path selected by the user to the next simulation file.
	private final JFileChooser fc; //FileChooser that the user uses to location the desired next simulation file.
	private boolean inProgress; //Boolean that keeps track if we're currently in process with a simulation.
	
	/****************************************************************************************************************
	 * Sets up the GUI of the simulation.  Set-up is 100% complete after a file is selected.						*
	 ****************************************************************************************************************/
	public GUI() {
		
		//Menu Bar objects and buttons.
		this.menuBar = new JMenuBar();
	    this.menu = new JMenu("Menu");
	    this.newSim = new JMenuItem("New Simulation");
	    this.newSim.addActionListener(this);
	    this.menuBar.add(menu);
	    this.menu.add(newSim);
	    
	    //Button panel objects.  Contrary to the name, it doesn't just have buttons.
	    this.buttonPanel = new JPanel();
	    this.currentExecution = new JLabel();
	    this.step = new JButton(">>");
	    this.stepFault = new JButton("Next >> Fault");
	    this.stepComplete = new JButton("Complete");
	    this.step.addActionListener(this);
	    this.stepFault.addActionListener(this);
	    this.stepComplete.addActionListener(this);
	    this.buttonPanel.add(currentExecution);
	    this.buttonPanel.add(step);
	    this.buttonPanel.add(stepFault);
	    this.buttonPanel.add(stepComplete);
	    
	    //Frame Table panel.  Most of the setup is completed in the setUpSimulation() method.
	    this.frameTablePanel = new JPanel();
	    this.frameTablePanel.setLayout(new BoxLayout(frameTablePanel, BoxLayout.PAGE_AXIS));
	    
	    //Default stuff.
	    this.fc = new JFileChooser();
	    this.inProgress = false;
	    
	    //Start to set up the layout.  More layout is done in the setUpSimulation() method.
	    setLayout(new BorderLayout(0, 0));
	    add(buttonPanel, BorderLayout.SOUTH);
	    this.pageTablePane = new JTabbedPane(JTabbedPane.LEFT);
	    setJMenuBar(menuBar);
	    setVisible(true);
	    setSize(700, 450);
		
	}
	
	private void setUpSimulation() {
		//We set up the simulation once the file we're running has been entered.
		this.inProgress = true;
		this.sim = new Simulation(64, 16); //Hardcoded default sizes of the PageTable, FrameTable.
		this.sim.receiveData(this.path); //The path of the file we selected.
		
		//Set up more Frame Table Panel stuff.
		JLabel title = new JLabel("FrameTable:");
	    title.setFont(new Font("Calibri", Font.PLAIN, 24));
	    frameTablePanel.add(title);
		
		//Sets up tabs.  We have a tab for every process with no details on any of them.
		this.pageTables = new JLabel[this.sim.getTotalProcesses()][this.sim.getPageTableSize()]; //2D-Array of labels.
																				//One for each potential table slot.
		JPanel[] pageTablePanels = new JPanel[this.sim.getTotalProcesses()]; //Array of panels, one for each tab.
		
		//In each panel/tab, set up the layout, give it a title label, and instantiate everything for use later on.
		for(int i=0; i < this.sim.getTotalProcesses(); i++) {
			pageTablePanels[i] = new JPanel();
			pageTablePanels[i].setLayout(new BoxLayout(pageTablePanels[i], BoxLayout.PAGE_AXIS));
			pageTablePanels[i].add(new JLabel("Process " + i + " Page Table: "));
			for(int j=0; j < this.sim.getPageTableSize(); j++) {
				this.pageTables[i][j] = new JLabel();
				pageTablePanels[i].add(this.pageTables[i][j]);
			}
			this.pageTablePane.add("Proc " + i, pageTablePanels[i]);
		}
		
		//Now we have an array of labels for each table slot on the Frame Table, which we also instantiate.
		this.frameTable = new JLabel[this.sim.getFrameTableSize()];
		for(int i=0; i < this.frameTable.length; i++) {
			this.frameTable[i] = new JLabel();
			this.frameTablePanel.add(this.frameTable[i]);
		}
		
		this.currentExecution.setText("Next instruction: " + this.sim.getExecutionCode()); //Displays 1st instruction.
		
		//Last of the setup for the layout on the GUI.
		add(this.pageTablePane, BorderLayout.WEST);
		add(this.frameTablePanel, BorderLayout.CENTER);
		repaint();
		revalidate();
	}
	
	/****************************************************************************************************************
	 * This resets the GUI, so that it's ready for a new simulation.  Does this by removing all Page Table and 		*
	 * Frame Table GUI Panel stuff.																					*
	 ****************************************************************************************************************/
	private void resetGUI() {
		this.pageTablePane.removeAll(); //Remove Page Table GUI stuff.
		this.frameTablePanel.removeAll(); //Remove Frame Table GUI stuff.
		
		repaint();
		revalidate();
	}
	
	/****************************************************************************************************************
	 * Updates the information in the Frame Table GUI Panel.  Also displays the current victim (if there is one) 	*
	 * in pink font.																								*
	 ****************************************************************************************************************/
	private void updateFrameTable() {
		Integer[][] data = this.sim.getFrameTableData();
		for(int i=0; i < this.frameTable.length; i++) {
			this.frameTable[i].setForeground(Color.BLACK);
			if(data[i] != null && data[i][0] != null && data[i][1] != null)
				this.frameTable[i].setText("Frame " + i + " | P" + data[i][0] + " | Page " + data[i][1]);
			else
				this.frameTable[i].setText("Frame " + i);
		}
		
		//Display victim (if there is one) with a special color.
		Frame victim = this.sim.getFrameTable().getLRUVictim();
		if(victim != null) {
			int index = this.sim.getFrameTable().checkFrameTableIfExists(victim.getProcessNum(), victim.getPageNum());
			this.frameTable[index].setForeground(Color.PINK);
		}
		
		repaint();
		revalidate();
	}
	
	/****************************************************************************************************************
	 * Updates the information in the Page Table GUI Tabs.															*
	 ****************************************************************************************************************/
	private void updatePageTables() {
		for(int i=0; i < this.sim.getTotalProcesses(); i++) {
			Integer[] data = this.sim.getPageTableData(i);
			for(int j=0; j < this.sim.getPageTableSize(); j++) {
				if(data[j] != null) {
					if(data[j] != -1)
						this.pageTables[i][j].setText("Page " + j + " | Frame " + data[j]);
					else
						this.pageTables[i][j].setText("");
				}
			}
		}
	}
	
	/****************************************************************************************************************
	 * Updates the text label of what the next instruction is.  If the simulation is completed, then also display	*
	 * the final simulation stats.																					*
	 ****************************************************************************************************************/
	private void updateInstruction() {
		this.currentExecution.setText("Next instruction: " + this.sim.getExecutionCode());
		if(this.sim.isCompleted()) {
			JOptionPane.showMessageDialog(null, this.sim.printFinalStats());
			this.inProgress = false;
		}
	}
	
	/****************************************************************************************************************
	 * Checks to see what action was performed (aka what button or menuicon was clicked) and does various things	*
	 * accordingly.																									*
	 * 																												*
	 * @param ActionEvent event - Event of what action was performed by the user.									*
	 ****************************************************************************************************************/
	@Override
	public void actionPerformed(ActionEvent event) {
		JComponent buttonPressed = (JComponent)event.getSource();
		if(buttonPressed == this.step && this.inProgress) { 
			this.sim.step();
			updateFrameTable();
			updatePageTables();
			updateInstruction();
		} else if(buttonPressed == this.stepFault && this.inProgress) {
			this.sim.stepUntilFault();
			updateFrameTable();
			updatePageTables();
			updateInstruction();
		} else if(buttonPressed == this.stepComplete && this.inProgress) {
			this.sim.stepUntilCompleted();
			updateFrameTable();
			updatePageTables();
			updateInstruction();
		} else if(buttonPressed == this.newSim) {
			int returnVal = fc.showOpenDialog(GUI.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            //This is where a real application would open the file.
	            String fileName = file.getName().trim();
	            System.out.println("Opening: " + fileName + ".");
	            String exten = fileName.split("\\.")[1]; //Get file extension.
	            if(exten.trim().equals("data")) { //Check if we have the correct extension.
	            	this.path = file.getPath();
	            	resetGUI();
	            	setUpSimulation();
	            } else {
	            	//Wrong type of file selected.
	            	JOptionPane.showMessageDialog(null, "You selected a file with a wrong file extension.");
	            }
	        } else {
	            System.out.println("Cancelled file selection."); //Do nothing, it was cancelled.
	        }
		}
	}
}

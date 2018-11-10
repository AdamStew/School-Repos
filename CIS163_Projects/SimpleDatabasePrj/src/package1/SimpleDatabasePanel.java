package package1;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleDatabasePanel extends JPanel implements ActionListener{

	private JPanel buttonPanel;

	private JScrollPane output;

	private JLabel name;

	private JTextField nameTxt;

	private JLabel gNum;

	private JTextField gNumTxt;

	private JLabel gpa;

	private JTextField gpaTxt;

	private JTextArea outputTxt;
	
	private JLabel fileName;
	
	private JTextField fileNameTxt;

	private JButton insert, delete, find, reverse, duplicate, display, 
	load, save, undo, sort, update;

	private SimpleDatabase database;

	public SimpleDatabasePanel() {
		database = new SimpleDatabase();
		buttonPanel = new JPanel();
		name = new JLabel("Name");
		gNum = new JLabel("GNumber");
		gpa = new JLabel("GPA");
		fileName = new JLabel("File Name");
		nameTxt = new JTextField(15);
		gNumTxt = new JTextField(15);
		gpaTxt = new JTextField(15);
		fileNameTxt = new JTextField(15);
		outputTxt = new JTextArea(10,40);
		output = new JScrollPane(outputTxt);
		insert = new JButton("Insert");
		delete = new JButton("Delete");
		find = new JButton("Find");
		sort = new JButton("Sort");
		reverse = new JButton("Reverse");
		duplicate = new JButton("Duplicate");
		display = new JButton("Display");
		load = new JButton("Load");
		save = new JButton("Save");
		undo = new JButton("Undo");
		update = new JButton("Update");
		insert.addActionListener(this);
		delete.addActionListener(this);
		find.addActionListener(this);
		reverse.addActionListener(this);
		sort.addActionListener(this);
		duplicate.addActionListener(this);
		display.addActionListener(this);
		load.addActionListener(this);
		save.addActionListener(this);
		undo.addActionListener(this);
		update.addActionListener(this);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		buttonPanel.setLayout(new GridLayout(5,4));
		buttonPanel.add(name);
		buttonPanel.add(nameTxt);
		buttonPanel.add(insert);
		buttonPanel.add(delete);
		buttonPanel.add(gNum);
		buttonPanel.add(gNumTxt);
		buttonPanel.add(find);
		buttonPanel.add(duplicate);
		buttonPanel.add(gpa);
		buttonPanel.add(gpaTxt);
		buttonPanel.add(display);
		buttonPanel.add(reverse);
		buttonPanel.add(fileName);
		buttonPanel.add(fileNameTxt);
		buttonPanel.add(load);
		buttonPanel.add(save);
		buttonPanel.add(sort);
		buttonPanel.add(undo);
		buttonPanel.add(update);
		add(buttonPanel);
		add(output);
	}

	public static void main(String[] args) {
		new SimpleDatabasePanel(); 
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		String name = nameTxt.getText();
		if(!name.matches("[A-Z][a-z]+")) {
			JOptionPane.showMessageDialog(null,"Please type in a "
					+ "valid name");
			return;
		}
		String gNumber = gNumTxt.getText();
		if(!gNumber.matches("G[0-9]{4}")) {
			JOptionPane.showMessageDialog(null,"GNumber must start with"
					+ " 'G', followed by 4 digits");
			return;
		}
		Double gpa = 0.0;
		if(!gpaTxt.getText().matches("[0-4].??[0-9]*+")) {
			JOptionPane.showMessageDialog(null,"GPA must be between "
					+ "1.0 and 4.0");
			return;
		}
		else
			gpa = Double.parseDouble(gpaTxt.getText());
		
		if(button == insert) {
			Student s = new Student(name,gNumber,gpa);
			database.insert(s);
			outputTxt.append("Inserted: " + s.toString());
		}
		if(button == delete) {
			database.delete(gNumber);
			outputTxt.append("Deleted " + gNumber + "\n");
		}
		if(button == find) {
			if(database.find(gNumber)!= null)
				outputTxt.append("Found:"+ database.find(gNumber));
			else 
				outputTxt.append("No such student found \n");
		}
		if(button == reverse) {
			database.reverseList();
			outputTxt.append("Reversed the list \n");
		}
		if(button == duplicate) {
			database.removeDuplicates();
			outputTxt.append("Removed duplicates \n");
		}
		if(button == display) {
			outputTxt.append("************************************ \n");
			outputTxt.append(database.display());
			outputTxt.append("************************************ \n");
		}
		if(button == sort) {
			database.sort();
			outputTxt.append("Database has been sorted \n");
		}
		if(button == undo) {
			if(database.undo())
				outputTxt.append("Last action undone \n");
			else
				outputTxt.append("Nothing left to undo \n");
		}
		if(button == update) {
			Student updating = new Student(name, gNumber, gpa);
			if(database.update(gNumber, updating))
				outputTxt.append("Updated "+ gNumber + "\n");
			else
				outputTxt.append("GNumber wasn't found \n");
		}
		String file1 = fileNameTxt.getText();
		if(file1.isEmpty() && (button == load || button == save)){
			JOptionPane.showMessageDialog(null, "Please type in a "
					+ "file name");
			return;
		}
		if(button == load) {
			database.loadDB(file1);
			outputTxt.append("Database loaded from file1 \n");
		}
		if(button == save) {
			database.saveDB(file1);
			outputTxt.append("Database saved to file1 \n");
		}
	}

}

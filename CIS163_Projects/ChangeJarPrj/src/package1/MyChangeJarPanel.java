package package1;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyChangeJarPanel extends JPanel {
	
	private JLabel i1;
	private JLabel i2;
	private JLabel i3;
	private JLabel i4;
	
	private JButton [] buttons;
	private JButton b1;
	private JButton b2;
	private JButton b3;
	private JButton b4;
	private JButton b5;
	private JButton b6;
	private JButton b7;
	private JButton b8;
	private JButton b9;
	private JButton b10;
	
	private ChangeJar c1;
	private ChangeJar c2;
	private ChangeJar c3;
	
	public MyChangeJarPanel(){
		
		i1 = new JLabel("", SwingConstants.CENTER);
		i2 = new JLabel("", SwingConstants.CENTER);
		i3 = new JLabel("", SwingConstants.CENTER);
		i4 = new JLabel("", SwingConstants.CENTER);
		
		b1 = new JButton(".");
		b2 = new JButton("Clear");
		b3 = new JButton("Lid ON");
		b4 = new JButton("Lid OFF");
		b5 = new JButton("Add to ChangeJar #1");
		b6 = new JButton("Add to ChangeJar #2");
		b7 = new JButton("Add to ChangeJar #3");
		b8 = new JButton("Subtract from ChangeJar #1");
		b9 = new JButton("Subtract from ChangeJar #2");
		b10 = new JButton("Subtract from ChangeJar #3");
		
		c1 = new ChangeJar("1.23");
		c2 = new ChangeJar(2.16);
		c3 = new ChangeJar();
		
		buttons = new JButton[10];
				
		setLayout(new GridLayout(8,3));
		
		ButtonListener listener = new ButtonListener();
		
		add(i1);
		add(i2);
		add(i3);
		
		
		b1.addActionListener(listener);
		b2.addActionListener(listener);
		b3.addActionListener(listener);
		b4.addActionListener(listener);
		b5.addActionListener(listener);
		b6.addActionListener(listener);
		b7.addActionListener(listener);
		b8.addActionListener(listener);
		b9.addActionListener(listener);
		b10.addActionListener(listener);
		
		add(b1);
		add(b2);
		
		for(int i = 0; i < buttons.length; i++){
			buttons[i] = new JButton ("" + i);
			buttons[i].addActionListener(listener);
			add(buttons[i]);
		}
		
		add(b3);
		add(i4);
		add(b4);
		add(b5);
		add(b6);
		add(b7);
		add(b8);
		add(b9);
		add(b10);
	
		
		i1.setText("ChangeJar #1:  " + c1.toString());
		i2.setText("ChangeJar #2:  " + c2.toString()); 
		i3.setText("ChangeJar #3:  " + c3.toString());
	}
	
	private class ButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			
			
			
			for(int i = 0; i < buttons.length; i++){
				if(i4.getText().contains("Lid ON")){
					i4.setText("");
					i4.setText("Lid ON");
				}else{
					if(event.getSource() == buttons[i]){
						i4.setText(i4.getText() + "" + i);
					}
				}
			}
			if(event.getSource() == b1){
				i4.setText(i4.getText() + ".");
			}
			if(event.getSource() == b2){
				i4.setText("");
			}
			if(event.getSource() == b3){
				ChangeJar.mutate(false);
				i4.setText("");
				i4.setText("Lid ON");
			}
			if(event.getSource() == b4){
				ChangeJar.mutate(true);
				i4.setText("");
			}
			if(event.getSource() == b5){
				c1.add(i4.getText());
				i4.setText("");
				i1.setText("ChangeJar #1:  " + c1.toString());
			}
			if(event.getSource() == b6){
				c2.add(i4.getText());
				i4.setText("");
				i2.setText("ChangeJar #2:  " + c2.toString());
			}
			if(event.getSource() == b7){
				c3.add(i4.getText());
				i4.setText("");
				i3.setText("ChangeJar #3:  " + c3.toString());
			}
			if(event.getSource() == b8){
				c1.subtract(i4.getText());
				i4.setText("");
				i1.setText("ChangeJar #1:  " + c1.toString());
			}
			if(event.getSource() == b9){
				c2.subtract(i4.getText());
				i4.setText("");
				i2.setText("ChangeJar #2:  " + c2.toString());
			}
			if(event.getSource() == b10){
				c3.subtract(i4.getText());
				i4.setText("");
				i3.setText("ChangeJar #3:  " + c3.toString());
			}
		}
	}
}

package package1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**********************************************************************
 * This class displays the game board for the Surround game, and the 
 * number of wins for each player. 
 * @author Frank Derry Wanye, Adam Stewart
 * @version 02/09/2015
 *********************************************************************/
public class SurroundPanel extends JPanel {
	
	/** The square grid of click-able buttons on the game board */
	private JButton[][] board;
	
	/** A stack of disabled buttons that reflects the bottom row of 
	 * the board */
	private JButton[] topButtons;
	
	/** A stack of disabled buttons that reflects the top row of the
	 * board */
	private JButton[] bottomButtons;
	
	/** A stack of disabled buttons that reflects the right-most row of 
	 * the board */
	private JButton[] leftButtons;
	
	/** A stack of disabled buttons that reflects the left-most row of 
	 * the board */
	private JButton[] rightButtons;
	
	/** An instance of the game engine to control the game */
	private SurroundGame game;
	
	/** The current size of the board, in terms of number of buttons.
	 * The default board size in game is 10 by 10 */
	private static int BDSIZE;
	
	/** Menu item that quits the game */
	private JMenuItem quitItem;
	
	/** Menu item that starts a new game */
	private JMenuItem newGameItem;
	
	/** Displays the win count for each player */
	private JPanel statusPanel;
	
	/** Displays the game board */
	private JPanel boardPanel;
	
	/** Displays the click-able buttons on the board */
	private JPanel buttonsPanel;
	
	/** Displays the helper buttons to the left of the board */
	private JPanel leftPanel;
	
	/** Displays the helper buttons to the right of the board */
	private JPanel rightPanel;
	
	/** Displays the helper buttons above of the board */
	private JPanel topPanel;
	
	/** Displays the helper buttons below the board */
	private JPanel bottomPanel;
	
	/** Displays each player's win count */
	private JLabel[] playerLabels;
	
	/** Displays the number of player who's turn it is */
	private JLabel turns;
	
	/** Enables buttons and menu items to perform actions when 
	 * clicked on */ 
	private ButtonListener listener;
	
	/** Stores the square layout of the click-able buttons */
	private GridLayout grid;
	
	/** Stores the layout of the helper buttons to the left and right
	 * of the board */
	private GridLayout vertical;
	
	/** Stores the layout of the helper buttons above and below the 
	 * board */
	private GridLayout horizontal;
	
	/** Stores the default size of all the buttons */
	private Dimension buttonSize;

	/******************************************************************
	 * Constructor starts a new game with the default settings of a 
	 * 10*10 board size, and 2 players, with player 1 starting the game,
	 * and displays a panel containing the game board, helper 
	 * buttons and player win counts, within the game frame.
	 * @param quitItem is a menu item used to exit the game frame.
	 * @param newGameItem is a menu item used to start a new game.
	 *****************************************************************/
	SurroundPanel(JMenuItem quitItem, JMenuItem newGameItem) {

		this.quitItem = quitItem;
		this.newGameItem = newGameItem;

		listener = new ButtonListener();

		setBoard(10,2,1);

		this.quitItem.addActionListener(listener);
		this.newGameItem.addActionListener(listener);

	}
	
	/******************************************************************
	 * Displays a player's number when a player clicks on a button,
	 * and then switches turn if there is no winner. If a player loses,
	 * clears the buttons with that player's number, resets the game
	 * board when a new game is started or when a player has won, and
	 * finally exits the game frame when the quit game menu item is
	 * selected.
	 * @author Frank Derry Wanye, Adam Stewart
	 * @version 02/09/2015
	 *****************************************************************/
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			for (int r = 0; r < BDSIZE; r++) {
				for (int c = 0; c < BDSIZE; c++) {
					if (event.getSource() == board[r][c]) {
						//If the button clicked has not been previously 
						//selected
						if (game.select(r, c)) {
							board[r][c].setText(String.valueOf(
									game.getPlayer()));
							game.setCell(r, c);
							game.isLoser();
							clearLoser();
							addWinner();
						}
						else
							JOptionPane.showMessageDialog(null, 
									"Pick again.");
					}
				}
			}
			if (event.getSource() == quitItem) {
				System.exit(0);
			}
			if (event.getSource() == newGameItem) {
				newGame();
			}
		}
	}
	
	/******************************************************************
	 * Instantiates and displays all buttons on the game board, 
	 * including the helper buttons, using specified values for the 
	 * size of the game board, the number of players in the game, and
	 * the number of the player who starts the game. Also sets up and 
	 * displays the number of wins for each player in the game.
	 * @param BDSIZE is the number of buttons that make up a side of
	 * the board.
	 * @param totalPlayers is the number of players in the game
	 * @param player is the number of the player who starts the game
	 *****************************************************************/
	private void setBoard(int BDSIZE, int totalPlayers, int player) {
		
		this.BDSIZE = BDSIZE;
		
		boardPanel = new JPanel();
		statusPanel = new JPanel();
		boardPanel.setLayout(new BorderLayout());

		leftPanel = new JPanel();
		rightPanel = new JPanel();
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		buttonsPanel = new JPanel();

		grid = new GridLayout(BDSIZE, BDSIZE);
		horizontal = new GridLayout(1,BDSIZE);
		vertical = new GridLayout(BDSIZE,1);

		buttonsPanel.setLayout(grid);
		leftPanel.setLayout(vertical);
		rightPanel.setLayout(vertical);
		bottomPanel.setLayout(horizontal);
		topPanel.setLayout(horizontal);

		topButtons = new JButton[BDSIZE+2];
		bottomButtons = new JButton[BDSIZE+2];
		leftButtons = new JButton[BDSIZE];
		rightButtons = new JButton[BDSIZE];

		buttonSize = new Dimension(50,50);
		
		//Setting up the click-able buttons and adds them to the board
		board = new JButton[BDSIZE][BDSIZE];
		for (int r = 0; r < BDSIZE; r++) {
			for (int c = 0; c < BDSIZE; c++) {
				board[r][c] = new JButton("");
				board[r][c].setBackground(Color.white);
				board[r][c].setPreferredSize(buttonSize);
				board[r][c].addActionListener(listener);
				buttonsPanel.add(board[r][c]);
			}
		}
		
		//Setting up the top row of helper buttons
		for(int i = 0; i < BDSIZE+2; i++) {
			topButtons[i] = new JButton("");
			topButtons[i].setBackground(Color.lightGray);
			topButtons[i].setPreferredSize(buttonSize);
			topButtons[i].setEnabled(false);
			topPanel.add(topButtons[i]);
		}
		topButtons[0].setText("#");
		topButtons[BDSIZE+1].setText("#");
		
		//Setting up the bottom row of helper buttons
		for(int i = 0; i < BDSIZE+2; i++) {
			bottomButtons[i] = new JButton("");
			bottomButtons[i].setBackground(Color.lightGray);
			bottomButtons[i].setPreferredSize(buttonSize);
			bottomButtons[i].setEnabled(false);
			bottomPanel.add(bottomButtons[i]);
		}
		bottomButtons[0].setText("#");
		bottomButtons[BDSIZE+1].setText("#");
		
		//Setting up the left column of helper buttons
		for(int i = 0; i < BDSIZE; i++) {
			leftButtons[i] = new JButton("");
			leftButtons[i].setBackground(Color.lightGray);
			leftButtons[i].setPreferredSize(buttonSize);
			leftButtons[i].setEnabled(false);
			leftPanel.add(leftButtons[i]);
		}

		//Setting up the right column of helper buttons
		for(int i = 0; i < BDSIZE; i++) {
			rightButtons[i] = new JButton("");
			rightButtons[i].setBackground(Color.lightGray);
			rightButtons[i].setPreferredSize(buttonSize);
			rightButtons[i].setEnabled(false);
			rightPanel.add(rightButtons[i]);
		}
		
		game = new SurroundGame(BDSIZE, totalPlayers, player);

		GridLayout stack = new GridLayout(game.getTotalPlayers()+2,1);
		statusPanel.setLayout(stack);

		//Setting up the player status panel
		turns = new JLabel ("Player's Turn: "+game.getPlayer());
		turns.setPreferredSize(new Dimension(100,50));
		statusPanel.add(turns);
		JLabel wins = new JLabel("Player wins");
		wins.setBorder(BorderFactory.createLineBorder(Color.black));
		statusPanel.add(wins);
		playerLabels = new JLabel[game.getTotalPlayers()];
		for(int i = 0; i < game.getTotalPlayers(); i++) {
			int j = i+1;
			playerLabels[i] = new JLabel("Player " + j + ": " + 
					game.winCount[i]);
			statusPanel.add(playerLabels[i]);
		}

		boardPanel.add(BorderLayout.CENTER, buttonsPanel);
		boardPanel.add(BorderLayout.NORTH, topPanel);
		boardPanel.add(BorderLayout.WEST, leftPanel);
		boardPanel.add(BorderLayout.EAST, rightPanel);
		boardPanel.add(BorderLayout.SOUTH, bottomPanel);

		add(statusPanel);
		add(boardPanel);

	}
	
	/******************************************************************
	 * Resets all buttons which displayed the player number of a player
	 * that has lost the game.
	 *****************************************************************/
	private void clearLoser() {
		for(int row = 0; row < BDSIZE; row++) {
			for(int col = 0; col < BDSIZE; col++) {
				if(!board[row][col].getText().equals("")){
					String txt = board[row][col].getText();
					int player = Integer.parseInt(txt);
					if(game.getLosers().contains(player))
						board[row][col].setText("");
				}
			}
		}
		setHelpers();
	}

	/******************************************************************
	 * Color-codes all click-able buttons depending on how many
	 * opposing players have surrounded them. White means no other 
	 * players surrounding it (or the button is next to one that is 
	 * "owned" by the current player, making it safe). Cyan means one
	 * opposing player is next to the button. Orange means two 
	 * opposing players are next to the button. Red means three 
	 * opposing players surround the button. Black means the button is
	 * surrounded by opposing players - clicking on it is suicide.
	 *****************************************************************/
	private void reColor() {
		for(int row = 0; row < BDSIZE; row++) {
			for(int col = 0; col < BDSIZE; col++) {
				if(game.getSurround(row, col)==0)
					board[row][col].setBackground(Color.white);
				if(game.getSurround(row, col)==1)
					board[row][col].setBackground(Color.cyan);
				if(game.getSurround(row, col)==2)
					board[row][col].setBackground(Color.orange);
				if(game.getSurround(row, col)==3)
					board[row][col].setBackground(Color.red);
				if(game.getSurround(row, col)==4)
					board[row][col].setBackground(Color.black);
				if(!game.select(row, col))
					board[row][col].setBackground(Color.white);
			}
		}
	}

	/******************************************************************
	 * Switches player turn if there is no winner in the game, resets
	 * the game board, including helper buttons if there is a draw or 
	 * if a player has won, updating the player win count and player
	 * turn labels if necessary.
	 *****************************************************************/
	private void addWinner() {
		if(game.isWinner()==-1){
			reColor();
			setHelpers();
			turns.setText("Player's Turn: " + game.getPlayer());
			return;
		}
		if(game.isWinner()==0){
			JOptionPane.showMessageDialog(null, "This round resulted "
					+ "in a draw.");
			game.reset();
			reColor();
			setHelpers();
			for(int row = 0; row < BDSIZE; row++) {
				for(int col = 0; col < BDSIZE; col++) {
					board[row][col].setText("");
				}
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Player " + 
					game.getPlayer() + " has won!");
			int i = game.getPlayer()-1;
			game.winCount[i]++;
			playerLabels[i].setText("Player " + game.getPlayer() + 
					": " + game.winCount[i]);
			game.reset();
			reColor();
			for(int row = 0; row < BDSIZE; row++) {
				for(int col = 0; col < BDSIZE; col++) {
					board[row][col].setText("");
				}
			}
			setHelpers();
		}
	}
	
	/******************************************************************
	 * Sets up the helper buttons for the game board. Helper buttons
	 * reflect the player numbers on the opposite side of the board,
	 * helping the player to see which players surround a particular
	 * button.
	 *****************************************************************/
	private void setHelpers() {
		for (int row = 0; row < BDSIZE; row++) {
			for (int col = 0; col < BDSIZE; col++) {
				String number = board[row][col].getText();
				if(row == BDSIZE-1) {
					topButtons[col+1].setText(number);
				}
				if(row == 0) {
					bottomButtons[col+1].setText(number);
				}
				if(col == BDSIZE-1) {
					leftButtons[row].setText(number);
				}
				if(col == 0) {
					rightButtons[row].setText(number);
				}
			}
		}
	}

	/******************************************************************
	 * Starts a new game with user entered values for the size of the 
	 * board, the number of players, and the number of the player who
	 * starts the game.
	 *****************************************************************/
	private void newGame() {
		
		String boardSize = JOptionPane.showInputDialog(null, 
				"Enter the size of the board.", "10");
		if(boardSize==null)
			return;
		if(boardSize.matches("-?\\d+")) {
			BDSIZE = Integer.parseInt(boardSize);
			if(BDSIZE <= 3 || BDSIZE >= 20) {
				BDSIZE = 10;
				JOptionPane.showMessageDialog(null, "Board size must "
						+ "be between 4 and 19.");
			}
		}
		else {
			JOptionPane.showMessageDialog(null, 
					"You must enter a number");
		}
		
		int totalPlayers = 2;
		String players = JOptionPane.showInputDialog(null, 
				"Enter the number of players:", "2");
		if(players==null)
			return;
		if(players.matches("-?\\d+")) {
			totalPlayers = Integer.parseInt(players);
			if(totalPlayers < 2 || totalPlayers > 10) {
				totalPlayers = 2;
				JOptionPane.showMessageDialog(null, "There must"
						+ " be between 2 and 10 players.");
			}
		}
		else {
			JOptionPane.showMessageDialog(null, 
					"You must enter a number");
		}
		int player = 1;
		String startPlayer = JOptionPane.showInputDialog(null, 
				"Which player starts the game?", "1");
		if(startPlayer == null)
			return;
		if(startPlayer.matches("-?\\d+")) {
			player = Integer.parseInt(startPlayer);
			if(player < 1 || player > totalPlayers) {
				player = 1;
				JOptionPane.showMessageDialog(null, "Player "
						+ "number must be between 1 and " + 
						totalPlayers + ".");
			}
		}
		else {
			JOptionPane.showMessageDialog(null, 
					"You must enter a number");
		}
		game.reset();
		remove(boardPanel);
		remove(statusPanel);
		setBoard(BDSIZE, totalPlayers, player);
		setHelpers();
		revalidate();
		repaint();
	}
}
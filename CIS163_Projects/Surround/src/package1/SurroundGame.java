package package1;

import java.util.ArrayList;

import javax.swing.JOptionPane;

/***********************************************************************
 * This class contains all the methods that allows the game to function 
 * the way it does.
 *@author Frank Derry Wanye, Adam Stewart
 *@version 02/09/2015
 **********************************************************************/
public class SurroundGame {
	
	/** A two-dimensional array of the Cell Object that makes up the 
	 * board */
	private Cell[][] board;
	
	/** The value of the total size of the row (minus 1 because' 
	 * array) */
	private int rowEnd;
	
	/** The value of the total size of the column (minus 1 because' 
	 * array) */
	private int colEnd;
	
	/** The total size of the board */
	private int BDSIZE;
	
	/** Current player's turn. */
	private int player; 
	
	/** The total number of players playing. */
	private int totalPlayers;
	
	/** An array that keeps track of a player's number of wins.  */
	public int[] winCount;
	
	/** An ArrayList of all the current losers of a game. */
	private ArrayList <Integer> losers;

	/*******************************************************************
	 * A method that makes a board out of cell objects, states the 
	 * number of total players, and creates a new ArrayList for those 
	 * who are eliminated to go into, for as long as the values are 
	 * within parameters.
	 * 
	 * @param BDSIZE - An integer value that is the size of the board of
	 * the game.
	 * @param totalPlayers - An integer value for all of the players.
	 * @param player - An integer value displaying who's turn it is.
	 ******************************************************************/
	public SurroundGame(int BDSIZE, int totalPlayers, int player) {
		this.BDSIZE = BDSIZE;
		board = new Cell[BDSIZE][BDSIZE];
		rowEnd = BDSIZE-1;
		colEnd = BDSIZE-1;
		this.totalPlayers = totalPlayers;
		this.player = player;
		losers = new ArrayList <Integer>();
		winCount = new int[totalPlayers];
		for(int i = 0; i < totalPlayers; i++){
			winCount[i] = 0;
		}
	}

	/*******************************************************************
	 * Gets the total amount of the currently eliminated players.
	 * 
	 * @return An ArrayList containing the losers of the current game.
	 ******************************************************************/
	public ArrayList <Integer> getLosers() {
		return losers;
	}

	/*******************************************************************
	 * A boolean method that determines whether or not positions are the
	 * board are null or not.
	 * 
	 * @param row - Integer value of the row.
	 * @param col - Integer value of the column.
	 * @return A boolean if it is null or not.
	 ******************************************************************/
	public Boolean select (int row, int col) {
		return board[row][col] == null;
	}

	/*******************************************************************
	 * Resets every spot are the board to being null, and creates a new 
	 * ArrayList of players eliminated.
	 ******************************************************************/
	public void reset() {
		for(int r = 0; r < BDSIZE; r++) {
			for(int c = 0; c < BDSIZE; c++) {
				board[r][c] = null;
			}
		}
		losers = new ArrayList<Integer>();
	}

	/*******************************************************************
	 * Gets an integer value of a certain player's number.
	 * 
	 * @return - An integer value of a player's number.
	 ******************************************************************/
	public int getPlayer() {
		return this.player;
	}

	/*******************************************************************
	 * Gets an integer value of the total number of players in the 
	 * current game.
	 * 
	 * @return - An integer value of the total number of current 
	 * players.
	 ******************************************************************/
	public int getTotalPlayers() {
		return this.totalPlayers;
	}

	/*******************************************************************
	 * Scans the board of the current game, and removes a players 
	 * positions on the board if they are at any point eliminated from 
	 * the game.
	 * 
	 * @param r - Integer value of the row of the board.
	 * @param c - Integer value of the column of the board.
	 ******************************************************************/
	public void removeLoser(int r, int c) {
		int loser = board[r][c].getPlayerNumber();
		for(int row = 0; row <= rowEnd; row++) {
			for(int col = 0; col <= colEnd; col++) {
				if(!select(row,col)){
					if(board[row][col].getPlayerNumber() ==
							loser){
						board[row][col]=null;
					}
				}
			}
		}
	}

	/*******************************************************************
	 * Scans through the board to see if any players have currently been
	 * eliminated from the game.  This is also binded across the sides.
	 ******************************************************************/
	public void isLoser() {
		for(int row = 0; row <= rowEnd; row++) {
			for(int col = 0; col <= colEnd; col++) {
				if(!select(row,col)) {
					int rowUp = row-1;
					if(rowUp<0)  //Instead of scanning out of bounds, it
						rowUp = rowEnd;  //jumps to the other side.
					int rowDown = row+1;
					if(rowDown>rowEnd)
						rowDown = 0;
					int colLeft = col -1;
					if(colLeft<0)
						colLeft = colEnd;
					int colRight = col +1;
					if(colRight>colEnd)
						colRight = 0;
					if(!select(rowUp,col) && !select(rowDown,col) &&
							!select(row,colLeft) && 
							!select(row,colRight) 
							&& board[row][colLeft].getPlayerNumber()
							!= board[row][col].getPlayerNumber()
							&& board[row][colRight].getPlayerNumber()
							!= board[row][col].getPlayerNumber()
							&& board[rowUp][col].getPlayerNumber()
							!= board[row][col].getPlayerNumber()
							&& board[rowDown][col].getPlayerNumber()
							!= board[row][col].getPlayerNumber()) {
						losers.add(board[row][col].getPlayerNumber());
						removeLoser(row,col);

					}
				}
			}
		}
	}

	/*******************************************************************
	 * Checks to see if there's only one player remaining.
	 * 
	 * @return - An integer value of the last remaining player and the 
	 * winner of the game.
	 ******************************************************************/
	public int isWinner() {
		if(losers.size() == totalPlayers - 1) {
			for(int p = 1; p <= totalPlayers; p++) {
				if(!losers.contains(p))
					return 1;
			}
		}
		int count = 0;
		for(int row = 0; row < BDSIZE; row++) {
			for(int col = 0; col < BDSIZE; col++) {
				if(!select(row,col))
					count++;
			}
		}
		if(count == BDSIZE*BDSIZE)
			return 0;
		nextPlayer();
		return -1;
	}

	/*******************************************************************
	 * Determines the next players turn, and skipping players who have 
	 * been eliminated from the game.
	 * 
	 * @return - Gets the integer value for player, who is the current 
	 * player's turn.
	 ******************************************************************/
	public int nextPlayer() {
		player++;
		if(losers.contains(player))
			player++;
		if(player > totalPlayers)
			player = 1;
		if(losers.contains(player))
			player++;
		return player;
	}

	/*******************************************************************
	 * Sets a spot on the board to a player's number value depending on 
	 * what spot the player has chosen.
	 * 
	 * @param row - Integer value for the row on the board.
	 * @param col - Integer value for the column on the board.
	 ******************************************************************/
	public void setCell(int row, int col) {
		board[row][col] = new Cell(player);
	}

	/*******************************************************************
	 * Scans the spots on the board and determines the amount of other 
	 * players that are surrounding your positions on the board.
	 * 
	 * @param row - Integer value for the row on the board.
	 * @param col - Integer value for the column on the board.
	 * @return - Integer value for the total amount of people around you
	 * at a given spot on the board.
	 ******************************************************************/
	public int getSurround(int row, int col) {
		int count = 0;
		int rowUp = row-1;
		if(rowUp<0)
			rowUp = rowEnd;
		int rowDown = row+1;
		if(rowDown>rowEnd)
			rowDown = 0;
		int colLeft = col -1;
		if(colLeft<0)
			colLeft = colEnd;
		int colRight = col +1;
		if(colRight>colEnd)
			colRight = 0;
		if( !select(row,colLeft) && 
				board[row][colLeft].getPlayerNumber() != player)
			count++;
		if( !select(row,colRight) && 
				board[row][colRight].getPlayerNumber() != player)
			count++;
		if( !select(rowUp,col) && board[rowUp][col].getPlayerNumber()
				!= player)
			count++;
		if( !select(rowDown,col) && 
				board[rowDown][col].getPlayerNumber() != player)
			count++;
		if( !select(rowDown,col) && 
				board[rowDown][col].getPlayerNumber()==player || 
				!select(rowUp,col) && 
				board[rowUp][col].getPlayerNumber()==player || 
				!select(row,colLeft) &&
				board[row][colLeft].getPlayerNumber()==player || 
				!select(row,colRight) && 
				board[row][colRight].getPlayerNumber()==player )
			count = 0;
		return count;
	}
}

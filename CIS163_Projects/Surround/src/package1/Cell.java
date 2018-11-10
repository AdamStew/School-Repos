package package1;

/**********************************************************************
 * This class is a cell object which can hold a int.  It's what allows 
 * us to know who's winning. 
 * @author Frank Derry Wanye, Adam Stewart
 * @version 02/09/2015
 *********************************************************************/
public class Cell {
	
	/** The player's number inside a cell. */
	private int playerNumber;
	
	/*******************************************************************
	 * Sets a cell value into the cell object.
	 * @param playerNumber - Integer inside a cell.  (the player's 
	 * number)
	 ******************************************************************/
	public Cell(int playerNumber) {
		this.playerNumber = playerNumber;
	}
	
	/*******************************************************************
	 * Returns the number of the player currently occupying a cell.
	 * @return - Returns the integer inside the cell.
	 ******************************************************************/
	public int getPlayerNumber() {
		return playerNumber;
	}
}

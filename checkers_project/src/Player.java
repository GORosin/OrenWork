/*
  Player.java

  Version:
     $Id: Player.java,v 1.1 2002/10/22 21:12:53 se362 Exp $

  Revisions:
     $Log: Player.java,v $
     Revision 1.1  2002/10/22 21:12:53  se362
     Initial creation of case study

 */

import java.awt.*;
		   
/**
*  A class representation of the Player object.  This object
*  contains the methods needed when one of the users clicks
*  on one of the buttons within the GUI.
*
*  @author
*/

public abstract class Player {

    // Instance of the rules class which will be used to
    // validate moves and check for game ending conditions
    // once a move has been made.
    // Instance of the move class which will be
    // created when a user makes a move.
    //Move   theMove;
    int currentStart;
    int currentEnd;
    private final int    playerNumber;
    private String playerName;
    private Color  playerColor;
    private boolean active;
    
    /**
     * Create a new instance of a Player object to represent
     * one of the users.
     * 
     * @param num       The number of the player.
     * param newDriver Driver which will control this.
     */
    Player(int num, String name, Color color){
	this.playerName = name;
	this.playerColor = color;
	this.playerNumber = num;

    }

    /**
     * Make an instance of a Move that was just made and pass it
     * to theRules by calling its validateMove method.
     *
     * param start The starting spot of the move.  The legal
     *              squares on the checkers board are numbered
     *              from 1 to 32, left to right, top to bottom.
     * param end   The ending spot of the move.
     *
     * @return true If move was made, false otherwise*/
    public void makeMove(int start, int end ){
	    currentStart = start;
	    currentEnd = end;

    }


    public abstract void sendMove();

    public abstract void waitForPlayer();

    /**
     * This method is used for when a user has clicked on the 
     * "Quit" button on the GUI.  It handles exiting  the game.
     * 
     * @param message the player who quit
     * @pre  game is in progress
     * @post message is sent to driver to end the game
     */
    public abstract void endOfGame( String message );


    /**
     * Method that is invoked when the end of game conditions have 
     * been met.  If they have been, this method is called in both 
     * players to notify them of this with a message.  Implementation 
     * differs for local player and network player.
     *
     * @param player Message indicating the end of the game.
     */
    public abstract void endInDraw( Player player ); 
   


    /**
     * Returns the number for this player
     * 
     * @pre the player has a number
     * 
     * @return playerNumber
     */
    public int getNumber(){
	return getPlayerNumber();
    }

    /**
     * Sets the players name
     * 
     * @param name the name to be set
     */
    void setName(String name){
	setPlayerName(name);
    }
    
    /**
     * Return the color of this player
     * 
     * @return the color of this player
     */
    public Color getColor() {
	return getPlayerColor();
    }

    /**
     * A string representation of this object.
     * 
     * @return a String representation of this object.
     */
    public String toString(){
        return ("Player.  name = " + getPlayerName());
    }

    public void setActive(){
        this.active = true;
    }
    public void setInactive(){
        this.active = false;
    }
    public boolean isActive(){
        return active;
    }

    public String getPlayerName() {
        return playerName;
    }
    private void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    private int getPlayerNumber() {
        return playerNumber;
    }

    private Color getPlayerColor() {
        return playerColor;
    }
    void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }


}//Player.java

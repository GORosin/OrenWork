/*
  LocalPlayer.java

  Version:
    $Id: LocalPlayer.java,v 1.1 2002/10/22 21:12:52 se362 Exp $

  Revisions:
    $Log: LocalPlayer.java,v $
    Revision 1.1  2002/10/22 21:12:52  se362
    Initial creation of case study

 */

import javax.swing.*;
import java.awt.*;

/**
 *  This class inherits from the player. 
 *  This class identifies that the local player 
 *  is the second player in the game.
 *
 *  @author
 */

public class LocalPlayer extends Player {
    
    /**
     * This is a default constructor for this object
     */
    public LocalPlayer(int num,String name, Color color){
	super( num, name, color );
    }

	@Override

	public void sendMove() {
	}

	@Override
	public void waitForPlayer() {
    }


	/**
     * Method that is invoked when the end of game conditions have 
     * been met.  Fire off an action event to tell the GUI to display 
     * endMessage in a dialogue box.  When the user clicks OK, call 
     * endGame in theButtonHandler.
     *
     * @param endMessage  Message indicating the end of the game.
     */
    public void endOfGame( String endMessage ){
	
	JOptionPane.showMessageDialog( null,
               "Game has ended because: "
       	       + endMessage,
       	       "Game Over",
	       JOptionPane.INFORMATION_MESSAGE );
	
	System.exit( 0 );
	
    }
    
    /**
     * Method that is invoked when the end of game conditions have 
     * been met.  If they have been, this method is called in both 
     * players to notify them of this with a message.  Implementation 
     * differs for local player and network player.
     *
     * param endMessage  Message indicating the end of the game.
     */
    public void endInDraw( Player player ){
	
	// This display end of game dialog telling that a game has 
	// ended in a draw
	JOptionPane.showMessageDialog( null,
       	       player.getPlayerName() + " accepted a draw."
	       + "\n\nClick OK to end the program.",
      	       "Game Over",
               JOptionPane.INFORMATION_MESSAGE );
	
	System.exit( 0 );
    }
    
}//LocalPlayer.java


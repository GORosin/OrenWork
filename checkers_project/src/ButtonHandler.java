/*
  ButtonHandler.java

  Version
    $Id: ButtonHandler.java,v 1.1 2002/10/22 21:12:52 se362 Exp $

  Revisions:
    $Log: ButtonHandler.java,v $
    Revision 1.1  2002/10/22 21:12:52  se362
    Initial creation of case study

 */

import java.awt.*;

/**
 * An interface between the GUI and the kernel classes in a checkers game.
 * 
 * author
 */

class ButtonHandler extends Component {
    
    public static final String playerSwitch = "switch";
    private final Game game;
    // The numbers associated with the timer
    
    /**
     * Constructor for the facade.  Initializes the data members.
     *
     * param newBoard  Board  object ButtonHandler will manipulate.
     *
     */
    public ButtonHandler(Game game) {
        this.game = game;
    }
    /**
     * Tell the kernel that the user has quit/resigned the game
     * or quit the program
     */
    public void pressQuit() {
        // Alert players and the kernel that one person
        // has quit calls quitGame() for both players
        if(game.getPlayerOne().isActive()) {
            game.getPlayerOne().endOfGame(game.getPlayerOne().getPlayerName() + " quit the game" );
        }
        else{
            game.getPlayerTwo().endOfGame(game.getPlayerTwo().getPlayerName() + " quit the game" );
        }
    }

    /**
     * Tell the kernel that the user has requested a draw.
     */
    public void pressDraw() {

        // Alerts both players and the kernel that one person
        // has offered a draw calls offerDraw() on both players
       if(game.getPlayerOne().isActive()){
           game.getPlayerOne().endInDraw(game.getPlayerOne());
       }
       else{
           game.getPlayerTwo().endInDraw(game.getPlayerTwo());
       }

    }
}


// ButtonHandler.java
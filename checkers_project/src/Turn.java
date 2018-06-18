import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is a class created by the refactoring team
 * This classes main function is to represent the changes in a game between turns
 * @author Stephen Cook (sjc5897@g.rit.edu)
 */
public class Turn {
    private final Game game;
    private int startSpace = 99; // Starting space for current move
    private int endSpace = 99; // Ending space for current move
    private ActionListener actionListener;
    public static final String update = "update";


    public Turn(Game game) {
        this.game = game;
    }

    /**
     * This method should be called to select a space on the board,
     * either as the starting point or the ending point for a move.
     * The ButtonHandler will interpret this selection and send a move on to
     * the kernel when two spaces have been selected.
     *
     * @param space an int indicating which space to move to,
     *              according to the standard checkers numbering
     *              scheme, left to right and top to bottom.
     */
    public void selectSpace(int space) {

        // When button is click, take info from the GUI
        if (startSpace == 99) {

            // Set startSpace to space
            startSpace = space;

        } else if (endSpace == 99) {
            if (space == startSpace) {

                // Viewed as un-selecting the space selected
                // Set startSpace to predetermined unselected value
                startSpace = 99;

            } else {
                // The endSpace will be set to space
                endSpace = space;
                makeLocalMove();
            }
        }

        generateActionPerformed();
    }

    /**
     * Send a move on to the kernel, i.e. call makeMove() in
     * the LocalPlayer and inform it whose turn it is.
     *
     * @pre startSpace is defined
     * @pre endSpace is defined
     */
    private void makeLocalMove() {

        //make sure startSpace and endSpace are defined
        if (startSpace != 99 && endSpace != 99) {
            // Takes the information of a move and calls makeMove()
            // in a localPlayer
            if (game.getPlayerOne().isActive()) {
                game.getTheRules().validateMove(new Move(startSpace, endSpace));
                game.getPlayerOne().makeMove(startSpace, endSpace);
            } else {
                game.getTheRules().validateMove(new Move(startSpace, endSpace));
                game.getPlayerTwo().makeMove(startSpace, endSpace);

            }
        }

        // Reset startSpace and endSpace to 99
        startSpace = 99;
        endSpace = 99;

    }

    /**
     * Adds an action listener to the facade
     */
    public void addActionListener(ActionListener a) {
        actionListener = AWTEventMulticaster.add(actionListener, a);
        //Adds an action listener to the facade
    }

    /**
     * Generates an action. This is inherited from Component
     */
    private void generateActionPerformed() {

        if (actionListener != null) {
            actionListener.actionPerformed(
                    new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "update"));
            // Fires an event associated with timer, or move made on GUI
        }
    }

    public void endTurn(Player player, int space) {

        // Check to see if player passed in was the active player
        // If player passed in was active player, check for multiple
        // jump (space is none negative)
        if (player.isActive()) {

            // Inform the player that the move was not valid,
            // or to make another jump
            if (space < 0) {
                JOptionPane.showMessageDialog(null,
                        player.getPlayerName() + " made an illegal move",
                        "Invalid Move", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        player.getPlayerName() + " please make" +
                                " another jump", "Multiple Jump Possible",
                        JOptionPane.INFORMATION_MESSAGE);

                // Get the GUI to update
                player.setInactive();
                if (player.equals(this.game.getPlayerOne())) {
                    game.getPlayerTwo().setActive();
                } else {
                    game.getPlayerOne().setActive();
                }

                // If game is networked tell networked player to send
                // the move
                if (game.getGameType() instanceof GameHost
                        || game.getGameType() instanceof GameClient) {
                    player.sendMove();
                }
            }
        } else if (!player.isActive()) {
            // If game is networked, tell networked player to send move
            if (game.getGameType() instanceof GameHost
                    || game.getGameType() instanceof GameClient) {
                if (player.equals(game.getPlayerOne())) {
                    game.getPlayerTwo().sendMove();
                    game.getPlayerTwo().waitForPlayer();
                } else {
                    game.getPlayerOne().sendMove();
                    game.getPlayerOne().waitForPlayer();
                }
            }


            // Inform the other player to make a move and
            // tell facade to update any listening GUIs and
            // reset the timer
            if (game.getPlayerOne().isActive()) {
                game.getPlayerOne().setInactive();
                game.getPlayerTwo().setActive();
            } else {
                game.getPlayerTwo().setInactive();
                game.getPlayerOne().setActive();
            }

        }

    }

    /**
     * Return an int indicating which player's turn it is.
     * ( e.g. 1 for player 1 )
     *
     * @return int   The number of the player whose turn it is.
     * @pre game is in progress
     */
    public int whosTurn() {

        // Return the integer value of the activePlayer object
        if (game.getPlayerOne().isActive()) {
            return game.getPlayerOne().getNumber();
        } else {
            return game.getPlayerTwo().getNumber();
        }
    }
}

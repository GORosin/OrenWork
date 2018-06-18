import java.awt.*;
import java.net.URL;
import javax.swing.*;

/**
 * This is a class created by the refactoring team. This is meant to break up the Facade and Driver classes
 * This class focuses mostly on the responsibility of the the Game Start
 * This class creates the players, rule, board and starts the turns.
 * @author Stephen Cook (sjc5897@g.rit.edu)
 */
class Game {

    private GameType gameType;
    private final Rules theRules;
    private final Board theBoard;
    private final ButtonHandler theButtonHandler;
    private Turn turn;

    public Game(){
        this.theBoard = new Board();
        this.theButtonHandler = new ButtonHandler(this);
        this.theRules = new Rules(this);
    }

    public Player getCurrentPlayer() {
        return gameType.getActivePlayer();
    }

    public void setEnabled(JLabel playerOneLabel, JTextField playerOneField, JLabel playerTwoLabel, JTextField playerTwoField) {
        gameType.setEnabled(playerOneLabel, playerOneField, playerTwoLabel, playerTwoField);
    }
    public void setGameType(GameType type) {
        this.gameType = type;
    }
    public GameType getGameType() {
        return gameType;
    }
    /**
     * This method will start the game play. Letting the first person
     * move their piece and so on
     * <p>
     * pre  There are 2 players to play, and all pregame conditions are
     * in place
     * post The first person is able to make their first move
     */
    public void startGame() {

        if (gameType.getType().equals("HOST")) {
            gameType.waitForConnect(2);
            //( (NetworkPlayer)playerTwo).waitForConnect();
        } else if (gameType.getType().equals("CLIENT")) {
            //( (NetworkPlayer)playerOne).connectToHost();
            gameType.connectToHost(1);
        }

        // Tell player with the correct color to make a move
        if (gameType.getColor(1) == Color.white) {
            gameType.setActive(1);
            gameType.setInactive(2);
        } else {
            gameType.setActive(2);
            gameType.setInactive(1);
        }
        turn = new Turn(this);

    }

    /**
     * Set the game mode: a local game or a network game
     *
     * param mode the mode of the game
     * @pre we are in the setup for a game

    public void setGameMode(int mode) throws Exception {
        // Check to make sure that mode is a legal value
        // Call setGameMode() in driver class passing it
        // the legal mode.  If mode is not a legal value
        // an exception will be thrown
        if (mode == LOCALGAME || mode == HOSTGAME || mode == CLIENTGAME) {
            gameType = mode;
        } else {
            throw new Exception("Invalid Game Mode");
        }
        this.getTheButtonHandler().setGameType(gameType);
    }
    /**
     * Tell the kernel to connect to the specified host to
     * start a network game.
     *
     * @param host sets url host
     * @pre host != null
     */
    public void setHost(URL host) {
        // Makes sure host isn't null
        // Calls setHost() in driver
        if (host != null) {
            gameType.setHost(host);
        }
    }
    /**
     * Create a player with the given type and player number.
     *
     * @param
     */
    public void createPlayer(String name1, String name2){
        Color color1;
        Color color2;
        if (Math.random() > .5) {
            color1 = Color.blue;
            color2 = Color.white;
        } else {
            color1 = Color.white;
            color2 = Color.blue;
        }
        gameType.createPlayer(1,name1, color1);
        //playerOne = gameType.getPlayerOne();
        gameType.createPlayer(2,name2, color2);
        //playerTwo = gameType.getPlayerTwo();
        /*
        if (gameType == HOSTGAME || gameType == CLIENTGAME) {
            playerOne = new NetworkPlayer( 1, theRules, theButtonHandler ,name1, color1);
            playerTwo = new NetworkPlayer( 2, theRules, theButtonHandler ,name2, color2);
        } else {
            playerOne = new LocalPlayer( 1, theRules, theButtonHandler, name1, color1 );
            playerTwo = new LocalPlayer( 2, theRules, theButtonHandler, name2, color2 );
        }
        */
    }


    public Player getPlayerOne() {
        return gameType.getPlayerOne();
    }

    public Player getPlayerTwo() {
        return gameType.getPlayerTwo();
    }

    public Board getTheBoard() {
        return theBoard;
    }

    public ButtonHandler getTheButtonHandler() {
        return theButtonHandler;
    }

    public Rules getTheRules() {
        return theRules;
    }


    public void endGame( String message ){

        // Call endOfGame on both players with the given message
        getPlayerOne().endOfGame( message );
        getPlayerTwo().endOfGame( message );

        // When players have acknowledged the end of game
        // call System.exit()
        System.exit( 0 );
    }
    public Player getOppositePlayer() {
        // Returns the player who's getTurnStatus is false
        if (getPlayerOne().isActive()) {
            return (getPlayerTwo());
        } else {
            return getPlayerOne();
        }
    }
    public Turn getTurn(){
            return this.turn;
        }

}


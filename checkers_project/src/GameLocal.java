import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by abigail_tran on 12/4/17.
 */
public class GameLocal implements GameType {
    private Player playerOne;
    private Player playerTwo;
    public GameLocal() {
    }

    public void setEnabled(JLabel playerOneLabel, JTextField playerOneField, JLabel playerTwoLabel, JTextField playerTwoField) {
    }

    @Override
    public void createPlayer(int playerNum,String name, Color color) {
        if (playerNum == 1) {
            playerOne = new LocalPlayer(playerNum, name, color);
        } else if (playerNum == 2) {
            playerTwo = new LocalPlayer(playerNum,  name, color);
        }
    }


    @Override
    public Player getPlayerOne() {
        return playerOne;
    }

    @Override
    public Player getPlayerTwo() {
        return playerTwo;
    }


    @Override
    public void waitForConnect(int player) {
        if (player == 1) {
            playerOne.waitForPlayer();
        } else {
            playerTwo.waitForPlayer();
        }
    }

    @Override
    public void connectToHost(int player) {

    }

    @Override
    public void setHost(URL host) {

    }

    @Override
    public Color getColor(int player) {
        if (player == 1) {
            return playerOne.getColor();
        } else {
            return playerTwo.getColor();
        }
    }

    @Override
    public void setActive(int player) {
        if (player == 1) {
            playerOne.setActive();
        } else {
            playerTwo.setActive();
        }
    }

    @Override
    public void setInactive(int player) {
        if (player == 1) {
            playerOne.setInactive();
        } else {
            playerTwo.setInactive();
        }
    }

    @Override
    public String getType() {
        return "LOCAL";
    }
    @Override
    public Player getActivePlayer() {
        if (playerOne.isActive()) {
            return playerOne;
        } else {
            return playerTwo;
        }
    }
}

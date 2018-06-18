import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by abigail_tran on 12/4/17.
 */
public class GameHost implements GameType{

    private Player playerOne;
    private Player playerTwo;
    public GameHost() {
    }
    public void setEnabled(JLabel playerOneLabel, JTextField playerOneField, JLabel playerTwoLabel, JTextField playerTwoField) {
        playerTwoLabel.setEnabled(false);
        playerTwoField.setEnabled(false);
    }

    public String getType() {
        return "HOST";
    }

    @Override
    public void createPlayer(int playerNum,String name, Color color) {
        if (playerNum == 1) {
            playerOne = new NetworkPlayer(playerNum, name, color );
        } else if (playerNum == 2) {
            playerTwo = new NetworkPlayer(playerNum,  name, color);
        }
    }

    public Player getPlayerOne() {
        return playerOne;
    }

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

        if (player == 1) {
            setOppositeFields(playerOne, playerTwo);
        } else {
            setOppositeFields(playerTwo, playerOne);
        }
    }
    private void setOppositeFields(Player player1, Player player2) {
        ((NetworkPlayer)player1).connectToHost();
        String playerTwoName = ((NetworkPlayer) player1).takeName();
        player2.setName(playerTwoName);
        Color playerTwoColor = ((NetworkPlayer) player1).takeColor();
        player2.setPlayerColor(playerTwoColor);
    }

    @Override
    public void setHost(URL host) {
        ((NetworkPlayer)playerOne).setHost(host);

        ((NetworkPlayer)playerTwo).setHost(host);

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
    public Player getActivePlayer() {
        if (playerOne.isActive()) {
            return playerOne;
        } else {
            return playerTwo;
        }
    }
}

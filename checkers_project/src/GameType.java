import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by abigail_tran on 12/4/17.
 */
public interface GameType {
    void setEnabled(JLabel playerOneLabel, JTextField playerOneField, JLabel playerTwoLabel, JTextField playerTwoField);
    void createPlayer(int playerNum,String name, Color color);
    Player getPlayerOne();
    Player getPlayerTwo();
    void waitForConnect(int player);
    void connectToHost(int player);
    void setHost(URL host);
    Color getColor(int player);
    void setActive(int player);
    void setInactive(int player);
    String getType();
    Player getActivePlayer();
}

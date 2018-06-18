/*
 * Firstscreen.java
 *
 *  * Version:
 *   $Id: Firstscreen.java,v 1.1 2002/10/22 21:12:52 se362 Exp $
 *
 * Revisions:
 *   $Log: Firstscreen.java,v $
 *   Revision 1.1  2002/10/22 21:12:52  se362
 *   Initial creation of case study
 *
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author
 * @version 
 */

class Firstscreen extends JFrame implements ActionListener{

    private final Game game;

    private JTextField IPField;
    private ButtonGroup gameModes;
    // End of variables declaration


    /** 
     * Creates new form Firstscreen
     *
     * param buttonHandler a buttonHandler object for the GUI to interact with
     *     
     */

    public Firstscreen() {

	super( "First screen" );
	    game = new Game();
        initComponents();
        pack();
    }
    

    /** 
     * This method is called from within the constructor to
     * initialize the form.
     * 
     */

    private void initComponents() {

        JRadioButton localGameButton = new JRadioButton();
        JRadioButton hostGameButton = new JRadioButton();
        JRadioButton joinGameButton = new JRadioButton();
	    gameModes = new ButtonGroup();
        IPField = new JTextField();
        JLabel IPLabel = new JLabel();
        JButton OKButton = new JButton();
        JButton cancelButton = new JButton();
        JLabel IPExampleLabel = new JLabel();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm();
            }
        }
        );
        
	gameModes.add(localGameButton);
        gameModes.add(hostGameButton);
	gameModes.add(joinGameButton);
		
	localGameButton.setActionCommand("local");
        localGameButton.setText("Local game");
        localGameButton.addActionListener(this);
        localGameButton.setSelected( true );
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        getContentPane().add(localGameButton, gridBagConstraints1);
        
        
        hostGameButton.setActionCommand("host");
        hostGameButton.setText("Host game");
        hostGameButton.addActionListener(this);
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        getContentPane().add(hostGameButton, gridBagConstraints1);
        
        
        joinGameButton.setActionCommand("join");
        joinGameButton.setText("Join game");
        joinGameButton.addActionListener(this);
        
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        getContentPane().add(joinGameButton, gridBagConstraints1);
        
        
        IPField.setBackground( Color.white );
        IPField.setName("textfield5");
        IPField.setForeground( Color.black);
        IPField.setText("IP address goes here");
        IPField.setEnabled( false );
        IPField.addActionListener(this);
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 3;
        getContentPane().add(IPField, gridBagConstraints1);
        
        IPLabel.setName("label10");
        IPLabel.setBackground(new Color (204, 204, 204));
        IPLabel.setForeground(Color.black);
        IPLabel.setText("IP address:");
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 3;
        getContentPane().add(IPLabel, gridBagConstraints1);
        
        OKButton.setText("OK");
        OKButton.setActionCommand("ok");
        OKButton.setName("button6");
        OKButton.setBackground(new Color (212, 208, 200));
        OKButton.setForeground(Color.black);
        OKButton.addActionListener(this);
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.insets = new Insets(30, 0, 0, 0);
        getContentPane().add(OKButton, gridBagConstraints1);
        
        cancelButton.setText("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.setName("button7");
        cancelButton.setBackground(new Color (212, 208, 200));
        cancelButton.setForeground(Color.black);
        cancelButton.addActionListener(this);
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 3;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.insets = new Insets(30, 0, 0, 0);
        getContentPane().add(cancelButton, gridBagConstraints1);
        
        IPExampleLabel.setName("label11");
        IPExampleLabel.setBackground(new Color (204, 204, 204));
        IPExampleLabel.setForeground(Color.black);
        IPExampleLabel.setText("Ex: 123.456.789.123");
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 4;
        getContentPane().add(IPExampleLabel, gridBagConstraints1);
        
        
    }

    /**
     *  
     * Exit the Application
     *
     * 
     */

    private void exitForm() {
        System.exit (0);
    }

	/**
	 * This takes care of when an action takes place. It will check the 
	 * action command of all components and then decide what needs to be done.
	 *
	 * @param e the event that has been fired
	 * 
	 */
	
	public void actionPerformed( ActionEvent e ){
		
	    try{
		//this code handles disabling the IP field unless
		//the join game radio button is selected
            switch ((e.getActionCommand())) {
                case "join":
                    IPField.setEnabled(true);
                    break;
                case "local":
                    IPField.setEnabled(false);
                    break;
                case "host":
                    IPField.setEnabled(false);

                    //this next if statement takes care of when the
                    //OK button is selected and goes to the second
                    //screen setting the desired options

                    break;
                case "ok":

                    //a temporary button to use for determining the game type
                    ButtonModel tempButton = gameModes.getSelection();
                    GameType newType;
                    //if check to see of the local radio button is selected
                    switch (tempButton.getActionCommand()) {
                        case "local":
                            newType = new GameLocal();
                            //set up a local game
                            game.setGameType(newType);


                            //hide the Firstscreen, make a Secondscreen and show it
                            this.setVisible(false);
                            Secondscreen next = new Secondscreen(game, this);
                            next.setVisible(true);

                            //if the host game button is selected
                            break;
                        case "host":

                            //set up to host a game
                            newType = new GameHost();
                            game.setGameType(newType);
                            //game.setGameMode(ButtonHandler.HOSTGAME);


                            //hide the Firstscreen, make the Secondscreen and show it
                            this.setVisible(false);
                            next = new Secondscreen(game, this);
                            next.setVisible(true);

                            //if the join game button is selected
                            break;
                        case "join":

                            //set up to join a game
                            newType = new GameClient();
                            game.setGameType(newType);

                            //try to connect
                            try {

                                //create a URL from the IP address in the IPfield
                                URL address = new URL("http://" + IPField.getText());
                                //set the host
                                game.setHost(address);

                                //hide the Firstscreen, make and show the Second screen
                                this.setVisible(false);
                                next = new Secondscreen(game, this);
                                next.setVisible(true);

                                //catch any exceptions
                            } catch (MalformedURLException x) {
                                JOptionPane.showMessageDialog(null,
                                        "Invalid host address",
                                        "Error",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }//end of networking catch statement


                            //set up to connect to another person
                            break;
                    }


                    //if they hit cancel exit the game
                    break;
                case "cancel":
                    System.exit(0);
            }
		
	    } catch( Exception x ) {
		System.err.println( x.getMessage() );
	    }//end of general catch statement

	}//end of actionPerformed

}//Firstscreen.java

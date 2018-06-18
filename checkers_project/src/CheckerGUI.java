/*
 * CheckerGUI.java
 * 
 * The actual board.
 *
 * Created on January 25, 2002, 2:34 PM
 * 
 * Version
 * $Id: CheckerGUI.java,v 1.1 2002/10/22 21:12:52 se362 Exp $
 * 
 * Revisions
 * $Log: CheckerGUI.java,v $
 * Revision 1.1  2002/10/22 21:12:52  se362
 * Initial creation of case study
 *
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author
 * @version 
 */

class CheckerGUI extends JFrame implements ActionListener{
    
    //the facade for the game
    
    private static Game gameStart;
    private final Vector possibleSquares = new Vector();//a vector of the squares

    private JLabel PlayerOneLabel;
    private JLabel playerTwoLabel;
    private JLabel TurnLabel;
    
    //the names and time left
    private static String playerOnesName="";
    private static String playerTwosName="";
    private static final String timeLeft="";

    /** 
     *
     * Constructor, creates the GUI and all its components
     *
     * @param name1 the first players name
     * @param name2 the second players name
     *
     */

    public CheckerGUI(Game gameStart, String name1, String name2 ) {

        super("Checkers");

	//long names mess up the way the GUI displays
	//this code shortens the name if it is too long
        String nameOne, nameTwo;
        if(name1.length() > 7 ){
            nameOne = name1.substring(0,7);
        }else{
            nameOne = name1;
        }
        if(name2.length() > 7 ){
            nameTwo = name2.substring(0,7);
        }else{
            nameTwo = name2;
        }
                
        playerOnesName = nameOne;
        playerTwosName = nameTwo;
        CheckerGUI.gameStart = gameStart;
        register();
        
        initComponents ();
        pack ();
        update();
    }
    
    
    /*
     * This method handles setting up the timer
     */
    
    private void register() {
	
        try{
	    gameStart.getTurn().addActionListener( this );
	  
        }catch( Exception e ){
            
            System.err.println( e.getMessage() );
         
        }
    }
    
    /**
     * This method is called from within the constructor to
     * initialize the form. It initializes the components
     * adds the buttons to the Vector of squares and adds
     * an action listener to the components 
     *
     */
    private void initComponents() {

        //sets the layout and adds listener for closing window
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints1;

        //add window listener
        addWindowListener(new WindowAdapter() {
                              public void windowClosing(WindowEvent evt) {
                                  exitForm();
                              }
                          }
        );
        this.setResizable( false );

        int x_coordinate = 0;
        int y_coordinate = 1;
        int numOfButton = 64;
        for (int i = 0; i < numOfButton; i++) {
            JButton newButton = new JButton();
            newButton.addActionListener(this);
            newButton.setPreferredSize(new Dimension(80, 80));
            newButton.setActionCommand(String.valueOf(i));
            if ( (i/8) % 2 == 0 && i%2 == 0 ) {
                newButton.setBackground(Color.white);
            } else if ( (i/8) % 2 == 1 && i%2 == 1 ) {
                newButton.setBackground(Color.white);
            }
            else {

                newButton.setBackground( new Color(204, 204, 153));
            }
            GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = x_coordinate % 8;
            gridBagConstraints.gridy = y_coordinate;
            x_coordinate++;
            if (x_coordinate % 8 == 0) {
                y_coordinate++;
            }
            getContentPane().add(newButton, gridBagConstraints);

            possibleSquares.add(newButton);
        }

        PlayerOneLabel = new JLabel();
        playerTwoLabel = new JLabel();
	    TurnLabel = new JLabel();

        JLabel warningLabel = new JLabel();
        JLabel timeRemainingLabel = new JLabel();
        JLabel secondsLeftLabel = new JLabel();

        JButton resignButton = new JButton();
        resignButton.addActionListener( this );

        JButton drawButton = new JButton();
        drawButton.addActionListener( this );
	      

        
        PlayerOneLabel.setText("Player 1:     " + playerOnesName );
        PlayerOneLabel.setForeground( Color.black );

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 4;
        getContentPane().add(PlayerOneLabel, gridBagConstraints1);
        
        playerTwoLabel.setText("Player 2:     " + playerTwosName );
        playerTwoLabel.setForeground( Color.black );
		
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.gridwidth = 4;
        getContentPane().add(playerTwoLabel, gridBagConstraints1);
        
        TurnLabel.setText("");
        TurnLabel.setForeground( new Color( 0, 100 , 0 ) );
        
        gridBagConstraints1.gridx=8;
        gridBagConstraints1.gridy=1;
        getContentPane().add(TurnLabel, gridBagConstraints1 );
        
        warningLabel.setText( "" );
        warningLabel.setForeground( Color.red );
		
        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 2;
        getContentPane().add(warningLabel, gridBagConstraints1 );
        
        timeRemainingLabel.setText("Time Remaining:");
        timeRemainingLabel.setForeground( Color.black );
		
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 3;
        getContentPane().add(timeRemainingLabel, gridBagConstraints1);
        
        secondsLeftLabel.setText( timeLeft + " sec.");
        secondsLeftLabel.setForeground( Color.black );
        
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 4;
        getContentPane().add(secondsLeftLabel, gridBagConstraints1);
        
        resignButton.setActionCommand("resign");
        resignButton.setText("Resign");
        
	gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 7;
        getContentPane().add(resignButton, gridBagConstraints1);
        
        drawButton.setActionCommand("draw");
        drawButton.setText("Draw");
        
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 6;
        getContentPane().add(drawButton, gridBagConstraints1);
	
    }
    
    /** 
     * 
     * Exit the Application
     *
     * param  window event
     *
     */
    private void exitForm() {
        gameStart.getTheButtonHandler().pressQuit();

    }

    /**
     * Takes care of input from users, handles any actions performed
     *
     * @param e  the event that has occurred
     */

    public void actionPerformed( ActionEvent e ) {

	try{
	    //if a square gets clicked
	    if( e.getActionCommand().equals(  "1" ) ||
		e.getActionCommand().equals(  "3" ) ||
		e.getActionCommand().equals(  "5" ) ||
		e.getActionCommand().equals(  "7" ) ||
		e.getActionCommand().equals(  "8" ) ||
		e.getActionCommand().equals( "10" ) ||
		e.getActionCommand().equals( "12" ) ||
		e.getActionCommand().equals( "14" ) ||
		e.getActionCommand().equals( "17" ) ||
		e.getActionCommand().equals( "19" ) ||
		e.getActionCommand().equals( "21" ) ||
		e.getActionCommand().equals( "23" ) ||
		e.getActionCommand().equals( "24" ) ||
		e.getActionCommand().equals( "26" ) ||
		e.getActionCommand().equals( "28" ) ||
		e.getActionCommand().equals( "30" ) ||
		e.getActionCommand().equals( "33" ) ||
		e.getActionCommand().equals( "35" ) ||
		e.getActionCommand().equals( "37" ) ||
		e.getActionCommand().equals( "39" ) ||
		e.getActionCommand().equals( "40" ) ||
		e.getActionCommand().equals( "42" ) ||
		e.getActionCommand().equals( "44" ) ||
		e.getActionCommand().equals( "46" ) ||
		e.getActionCommand().equals( "49" ) ||
		e.getActionCommand().equals( "51" ) ||
		e.getActionCommand().equals( "53" ) ||
		e.getActionCommand().equals( "55" ) ||
		e.getActionCommand().equals( "56" ) ||
		e.getActionCommand().equals( "58" ) ||
		e.getActionCommand().equals( "60" ) ||
		e.getActionCommand().equals( "62" ) ) {

		//call selectSpace with the button pressed
		gameStart.getTurn().selectSpace(
				   Integer.parseInt( e.getActionCommand() ) );

		//if draw is pressed
	    }else if( e.getActionCommand().equals( "draw" ) ){
		//does sequence of events for a draw
		gameStart.getTheButtonHandler().pressDraw();

		//if resign is pressed
	    }else if( e.getActionCommand().equals( "resign" ) ) {
		//does sequence of events for a resign
		gameStart.getTheButtonHandler().pressQuit();

		//if the source came from the facade
	    }else if( e.getSource().equals(gameStart.getTurn()) ) {

		//if its a player switch event
            switch ((e.getActionCommand())) {
                case ButtonHandler.playerSwitch:
                    //if it is an update event
                    break;
                case Turn.update:
                    //update the GUI
                    update();
                    break;
                default:
                    throw new Exception("unknown message from facade");
            }
	    }
	    //catch various Exceptions
	}catch( NumberFormatException excep ){
	    System.err.println(
		     "GUI exception: Error converting a string to a number" );
	}catch( NullPointerException exception ){
	    System.err.println( "GUI exception: Null pointerException "
				+ exception.getMessage() );
	    exception.printStackTrace();
	}catch( Exception except ){
	    System.err.println( "GUI exception: other: "
				+ except.getMessage() );
	    except.printStackTrace();
	}

    }


    /**
     * Updates the GUI reading the pieces in the board
     * Puts pieces in correct spaces, updates who's turn it is
     *
     * param the board
     */
    
    private void update(){
	
	
	if( checkEndConditions() ){
	    
	    gameStart.endGame(" ");
	}
	//the board to read information from
	Board board = gameStart.getTheBoard();
	//a temp button to work with
	JButton temp;
	
	//go through the board
	for( int i = 1; i < board.sizeOf(); i++ ){
	    
	    // if there is a piece there
	    if( board.occupied( i ) ){
		
		//check to see if color is blue
		if( board.colorAt( i ) == Color.blue ){

		    //if there is a  single piece there
		    if((board.getPieceAt(i)).getType() == Board.SINGLE){

			//show a blue single piece in that spot board
			temp = (JButton)possibleSquares.get(i);

			//get the picture from the web
			try{
			    temp.setIcon(
			      new ImageIcon( new URL("file:BlueSingle.gif") ));
			}catch( MalformedURLException e ){
			    System.out.println(e.getMessage());
			}

			//if there is a kinged piece there
		    }else if((board.getPieceAt(i)).getType() == Board.KING){

			//show a blue king piece in that spot board
			temp= (JButton)possibleSquares.get(i);

			//get the picture from the web
			try{
			    temp.setIcon(
			      new ImageIcon(new URL("file:BlueKing.gif") ) );
			}catch( Exception ignored){}
			
		    }

		    //check to see if the color is white        
		}else if( board.colorAt( i ) == Color.white ){

		    //if there is a single piece there
		    if((board.getPieceAt(i)).getType() == Board.SINGLE){

			//show a blue single piece in that spot board
			temp = (JButton)possibleSquares.get(i);

			//get the picture from the web
			try{
			    temp.setIcon(
			      new ImageIcon(new URL("file:WhiteSingle.gif")));
			}catch( Exception ignored){}
			
			//if there is a kinged piece there
		    }else if((board.getPieceAt(i)).getType() == Board.KING){

			//show a blue king piece in that spot board
			temp = (JButton)possibleSquares.get(i);

			//get the picture from the web
			try{
			    temp.setIcon(
			      new ImageIcon(new URL("file:WhiteKing.gif") ) );
			}catch( Exception ignored){}
		    }
                                //if there isn't a piece there
		}
	    }else {
		//show no picture
		temp = (JButton)possibleSquares.get(i);
		temp.setIcon( null );
	    }
	}
	
	//this code updates who's turn it is
	if(gameStart.getTurn().whosTurn() == 2 ){
	    playerTwoLabel.setForeground( Color.red );
	    PlayerOneLabel.setForeground(Color.black );
	    TurnLabel.setText( playerTwosName + "'s turn ");
	}else if( gameStart.getTurn().whosTurn() == 1 ){
	    PlayerOneLabel.setForeground( Color.red );
	    playerTwoLabel.setForeground(Color.black );
	    TurnLabel.setText( playerOnesName + "'s turn" );
	}
    }

    /**
     * Checks the ending conditions for the game
     * see if there a no pieces left
     *
     * @return the return value for the method
     *           true if the game should end
     *           false if game needs to continue 
     */

    private boolean checkEndConditions(){
           
	    //the return value
            boolean retVal = false;
            try{
		//the number of each piece left
		int whitesGone = 0 , bluesGone = 0;
		
		//the board to work with
		Board temp = gameStart.getTheBoard();
		
		//go through all the spots on the board
		for( int i=1; i<temp.sizeOf(); i++ ){
		    //if there is a piece there
		    if( temp.occupied( i  ) ){
			//if its a blue piece there
			if( (temp.getPieceAt( i )).getColor() == Color.blue ){
			    // increment number of blues
			    bluesGone++;
			    //if the piece is white
			}else if( (temp.getPieceAt( i )).getColor()
				  == Color.white ){
			    //increment number of whites
			    whitesGone++;
			}
		    }
		}//end of for loop
		
		//if either of the number are 0
		if( whitesGone == 0 || bluesGone == 0 ){
		    retVal = true;
		}

            }catch( Exception e ){
                
                System.err.println( e.getMessage() );            
                
            }
            return retVal;
            
        }//checkEndConditions

}

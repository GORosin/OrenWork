/*
 * NetworkPlayer.java
 *
 * Version:
 *   $Id: NetworkPlayer.java,v 1.1 2002/10/22 21:12:53 se362 Exp $
 *
 * Revisions:
 *   $Log: NetworkPlayer.java,v $
 *   Revision 1.1  2002/10/22 21:12:53  se362
 *   Initial creation of case study
 *
 */

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 *  This class inherits from player. It is involved in the network game.
 *  This identifies that a network player is the second player.
 *
 *  @author
 */
public class NetworkPlayer extends Player {

   // The commands that can be send back and forth
   private static final int RESIGN = 0;
   private static final int DRAWOFFER = 1;
   private static final int ACCEPTDRAW = 2;
   private static final int REFUSEDRAW = 3;
   private static final int ENDOFGAME = 4;
   private static final int ROGER = 90;
   private static final int RESEND = 98;

    // The port number we will read from
   private static final int PORTNUM = 1051;

    // The host we'll connect to if we're a remote system
   private URL host = null;

   // The socket used for communication
   private Socket clientSocket = null;

   // The socket that listens for a connect request
   private final ServerSocket serverSocket = null;

   // Where we send our output; wraps clientSocket's outputStream
   private ObjectOutputStream out = null;

   // Where we receive our input; wraps clientSocket's inputStream
   private ObjectInputStream in = null;

   // Where we temporarily store input received and future output
   private Object inputObj = null;


    /**
    *  Constructor that creates a default object of this class
    */
   public NetworkPlayer(int playerNum, String name, Color color ) {

    	// call super classes (player) constructor to give it starting info
    	super( playerNum, name, color );
    	// set values to correct initial state
   }


    
   /**
    * Set the host that we'll connect to if we're a remote system
    */
   public void setHost( URL host ) {
        this.host = host;
   }


    /**
    * This method establishes a connection to the host
    *
    * param host - the host to connect to
    *
    * @pre the host specified exists
    * @post connection to the host has been established
    */
   public void connectToHost()
   {
   	// create the proper sockets and attempt to establish a connection
       try {
           // Connect to host
           clientSocket = new Socket( host.getHost(), PORTNUM );

           // Streams for communication over network
           out = new ObjectOutputStream( clientSocket.getOutputStream() );
           in = new ObjectInputStream( clientSocket.getInputStream() );
       } catch (IOException e ) {

           System.err.println("Couldn't get I/O for connection to: "
                              + host.getHost() + "\n" + e
                              + "\nProgram exiting...");
           cleanup();
           // theButtonHandler.endGame();
       } catch (Exception e) {
           System.err.println(
			  "An exception occurred while attempting to connect"
                              + "to: " + host.getHost() + "\n" + e
                              + "\nProgram exiting...");
           cleanup();
           // theButtonHandler.endGame();
       }

       // once a connection is established, begin setting up the game
       // send the host our name
       sendName();

   }


   /**
    * This method takes the name of the other player and stores it
    *
    *
    * @pre name is null
    * @post the name is displayed at the other end of the connection
    */
   public String takeName()
   {
       Object inputObj = null;
       Integer outputObj;

       // repeat until a string is received and confirmation is sent
       do  {
            // read the name
           try {
                inputObj = in.readObject();
           } catch ( Exception e ) {
                   // add better error detect later

                   System.err.println( "Error reading name from network"
                                       + " stream:\n" + e );
           }

            // make sure it's OK
            if ( inputObj instanceof String ) {

                // DEBUG
                System.out.println( "Received name: " + inputObj );


                // Set the other player's name
                System.out.println( "Set the name to " + inputObj);
                outputObj = ROGER;

                try {
                    System.out.println( "Sending confirm" );

                    out.writeObject( outputObj );
                    out.flush();
                } catch ( IOException e ) {
                       System.err.println( "IOException while sending confirm"
					   + " over network stream:\n" + e);
                }
                return (String) inputObj;
            } else {
                outputObj = RESEND;
                try {
                    out.writeObject( outputObj );
                    out.flush();
                } catch ( IOException e ) {
                       System.err.println( "IOException while sending confirm"
					   + " over network stream:\n" + e);
                }
                return (String) inputObj;
            }
       } while (outputObj == RESEND );
   }


   /**
    * This method sends the name of this player to the other computer
    *
    * @pre name is not null
    * @post the host has the proper name for the player
    */
   private void sendName()
   {
      Integer inputObj = null;
      // Keep looping until confirm is received
   	do {
        // use the socket's output stream to send our name to the other player
          try {
               // TEMPORARY HACK FOR COMPILE
               //String name = "My Name";
               out.writeObject(getPlayerName());
               out.flush();
               //DEBUG
               System.out.println( "Sent name: " + getPlayerName());
          } catch ( IOException e ) {
               System.err.println( "IOException while sending name"
                                   + " over network stream:\n" + e);
          }
          // get confirmation from host
          try {
               inputObj = (Integer) in.readObject();
          } catch ( Exception e ) {
                // add better error detect later
                System.err.println( "Error getting confirmation from network"
                                    + " stream:\n" + e );
          }
      } while (inputObj == RESEND );
   }


   /**
    * This method gets the color of this player from the other computer;
    * this method will only be executed by the client computer.
    *
    * @pre the color is null
    * @post this computer has the proper color for the player
    */
   public Color takeColor()
   {
       Object inputObj = null;
       Integer outputObj;
       // repeat until a Color is received and confirmation is sent
       do {
            // read the Color
           try {
                inputObj = in.readObject();
           } catch ( Exception e ) {
                   // add better error detect later
                   System.err.println( "Error reading color from network"
                                       + " stream:\n" + e );
           }
            // make sure it's OK


            if ( inputObj instanceof Color ) {
                // DEBUG
                System.out.println( "Received color: " + inputObj );
                // Set this player's color
                setPlayerColor(( Color ) inputObj);
                // Set the other player's color
                Color oppositeColor;
                if (inputObj == Color.white) {
                    oppositeColor = Color.blue;
                } else {
                    oppositeColor = Color.white;
                }


                outputObj = ROGER;
                try {
                    out.writeObject( outputObj );
                    out.flush();
                } catch ( IOException e ) {
                       System.err.println( "IOException while sending confirm"
                                               + " over network stream:\n" + e);
                }
                return oppositeColor;
            } else {
                outputObj = RESEND;
                try {
                    out.writeObject( outputObj );
                    out.flush();
                } catch ( IOException e ) {
                       System.err.println( "IOException while sending confirm"
                                               + " over network stream:\n" + e);
                }
                return null;
            }
       } while (outputObj == RESEND );
   }


    /**
    * Wait for the other player to send us a move or command.  If there is a
    * timeout, we generate an actionEvent telling the GUI.  We must then
    * send the move along to Rules, or call processCommand.
    *
    * @pre the game is in progress
    * @post we have received a move from the other player.
    */
   public void waitForPlayer()
   {
       //DEBUG
       System.out.println( "Entered waitForPlayer." );
       
       Integer outputObj;
       
       // We'll loop until we get good input
       do {
            // Receive something from the remote player
            try {
                   inputObj = in.readObject();
                   System.out.println(
			       "in waitForPlayer:  Read incoming object." );
            } catch ( Exception e ) {
                   // add better error detect later
                   System.err.println( "Error reading command from network"
                                       + " stream:\n" + e);
            }
            if ( inputObj instanceof Integer ) {
                // If it's an Integer, it's a command, so process it.
                outputObj = processCommand((Integer) inputObj);
                try {
                        out.writeObject( outputObj );
                } catch ( IOException e ) {
                       System.err.println( "IOException while sending confirm"
					   + " over network stream:\n" + e);
                }
		
                // this may conflict with ButtonHandler class
                if (outputObj == ACCEPTDRAW ) {
                    cleanup();
                }
                return;
            } else if ( inputObj instanceof NetworkMove ) {

		// DEBUG:
                System.out.println( "Move received." );
                outputObj = ROGER;
                try {
                        out.writeObject( outputObj );
                } catch ( IOException e ) {
                       System.err.println( "IOException while sending confirm"
					   + " over network stream:\n" + e);
                }
                new Move(
                        ((NetworkMove) inputObj).startLocation(), ((NetworkMove) inputObj).endLocation());
                return;

            } else {
                // The command received was bad; ask them to resend it
                outputObj = RESEND;
                //DEBUG
		System.err.println( "Bad input received over network." );
                try {
                        out.writeObject( outputObj );
                } catch ( IOException e ) {
                       System.err.println( "IOException while sending confirm"
					   + " over network stream:\n" + e);
                }
                return;
            }
       } while (outputObj == RESEND );
   }

    
   /**
    * Process an incoming command, and take the appropriate action.
    *
    */
   private Integer processCommand(int command) {
        Integer result = ROGER;
        int answer;
        System.out.println( "Entered processCommand." );
        
        // If a draw offer is received, fire off an action event to inform the
   	//    GUI.
       switch (command) {
           case DRAWOFFER:
               answer = JOptionPane.showConfirmDialog(null, "The remote player"
                               + " has requested a draw."
                               + "\n\nWill you agree to a"
                               + " draw?",
                       "Draw offer",
                       JOptionPane.YES_NO_OPTION);

               // Make the answer readable by the remote player object
               if (answer == JOptionPane.YES_OPTION) {
                   result = ACCEPTDRAW;
               } else {
                   result = REFUSEDRAW;
               }

               // Locally, this answer will be returned and sent by waitForPlayer()
               break;

           // If a draw accept is received, fire off an action event to
           //    inform the GUI and call endGame in theButtonHandler.
           case ACCEPTDRAW:
               JOptionPane.showMessageDialog(null,
                       "The remote player has accepted your draw offer."
                               + "\n\nClick OK to end the program.",
                       "Draw offer accepted",
                       JOptionPane.INFORMATION_MESSAGE);
               // theButtonHandler.endGame();
               break;

           // If a draw is refused, let the user know and proceed with the game.
           case REFUSEDRAW:
               JOptionPane.showMessageDialog(null,
                       "The remote player has refused your draw offer."
                               + "\n\nClick OK to continue the game.",
                       "Draw offer refused",
                       JOptionPane.INFORMATION_MESSAGE);
               break;

           // If a resign command is received, inform the user and exit the game
           case RESIGN:
               JOptionPane.showMessageDialog(null,
                       "The remote player has resigned."
                               + "\n\nClick OK to end the program.",
                       "Remote resignation",
                       JOptionPane.WARNING_MESSAGE);
               cleanup();
               // theButtonHandler.endGame();
               break;

           // If an end of game command is received, wait for a text message to
           //    be sent over and then fire off an action event to tell the GUI
           //    to display the message in a dialogue box when the user clicks OK,
           //    endGame should be called in theButtonHandler.
           case ENDOFGAME:
               String endMessage = null;
               try {
                   endMessage = (String) in.readObject();
               } catch (Exception e) {
                   // add better error detect later
                   System.err.println("Error reading end of game message from"
                           + " network stream:\n" + e);
               }
               JOptionPane.showMessageDialog(null,
                       endMessage
                               + "n\nClick OK to end the program.",
                       "End of game",
                       JOptionPane.INFORMATION_MESSAGE);

               cleanup();
               break;
           default:

               System.err.println("Bad command received over network stream."
                       + "  Unrecoverable error.  Program exiting...");
               //theButtonHandler.endGame();
               break;
       }
        return result;
   }


    /**
    * The move is sent to the remote player
    *
    * param move - move that was made by the local player
    *
    * @pre move is a legal move
    * @post the move is sent to the network player
    */
   public void sendMove()
   {
       Integer inputObj = ROGER;
       do {
            // send the given move across the active socket
           try {
                out.writeObject( new NetworkMove( currentStart, currentEnd ) );
		
		//DEBUG
		System.out.println( "Sent move." );
           } catch ( IOException e ) {
               System.err.println( "IOException while sending move over"
                                       + " network stream: " + e);
           }
	   
	   // get the confirmation
	   try {
	       inputObj = (Integer) in.readObject();
	   } catch ( Exception e ) {
	       // add better error detect later
	       System.err.println( "Error reading confirm from network"
				   + " stream." );
	   }
       } while (inputObj == RESEND );
   }


    /**
    * Method that is invoked when the end of game conditions have been
    * met.  Send the ENDOFGAME command over to the remote player and then
    * send the message endMessage over.
    *
    * @param endMessage  Message indicating the end of the game.
    */
   public void endOfGame(String endMessage){
       try {
            out.writeObject(ENDOFGAME);
            out.writeObject( endMessage );
            cleanup();
       } catch ( IOException e ) {
            System.err.println( "IOException while sending end of game"
                                + " message over network stream.");
       }
   }


    /**
    * Closes the streams & sockets
    */
    private void cleanup() {
       
       // DEBUG
       System.out.println( "Attempting to run cleanup." );
       
       if ( out != null ) {
           try {
                out.close();
           } catch( IOException e ) {
               System.err.println( "Error:  " + getPlayerName() +
                                   " couldn't close output stream.\n" 
                                   + e );
           }
       }
       
       if ( in != null ) {
           try {
                in.close();
           } catch( IOException e ) {
               System.err.println( "Error:  " + getPlayerName() +
                                   " couldn't close input stream.\n" 
                                   + e );
           }
       }
       
       if ( clientSocket != null ) {
           try {
                clientSocket.close();
           } catch ( IOException e ) {
               System.err.println( "Error:  " + getPlayerName() +
                                   " couldn't close clientSocket.\n" 
                                   + e );
           }
       }
       
       if ( serverSocket != null ) {
           try {
                serverSocket.close();
           } catch ( IOException e ) {
               System.err.println( "Error:  " + getPlayerName() +
                                   " couldn't close clientSocket.\n" 
                                   + e );
           }
       }
       
       System.out.println( "Finished cleanup." );
   }

    
   // BELOW: TEMP METHODS FOR COMPILE

    public void endInDraw( Player player ) {
   }
   
}

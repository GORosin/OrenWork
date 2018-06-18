/*
  Rules.java

  Version:
     $Id: Rules.java,v 1.1 2002/10/22 21:12:53 se362 Exp $

  Revisions:
     $Log: Rules.java,v $
     Revision 1.1  2002/10/22 21:12:53  se362
     Initial creation of case study

 */

import java.util.*;
import java.awt.*;

/**
 * This class is used to check the validity
 * of the moves made by the players.  It also 
 * checks to see if the conditions for the end
 * of the game have been met.
 *
 * @author
 * @author
 * 
 */
class Rules {
	private final Game gameStart;
    //private Move currentMove; // The current move in the game.
    private final int[] adjacentSpots = { -9, -7, 7, 9 }; // An array of adjacent
                                                    // spots to check.
    private final int[] secondSpots = { -18, -14, 14, 18 }; // An array of spots
	      					    // adj. to adjacentSpots.
    private int middle = 0;  // The space of a piece that gets jumped
	private final Vector leftWallPieces = new Vector(); // Positions of the left
	       				         // wall spaces.
    private final Vector rightWallPieces = new Vector(); // Positions of the right
						   // wall spaces.
    
    /**
     * The constructor for the Rules class.
     *
     */
    public Rules( Game gameStart) {
	
    	
	this.gameStart = gameStart;
	fillWallVector();
	
    }
    
    /**
     *  This method checks to see if the move that was just made
     *  was valid and returns a boolean indicating that.
     *
     *  @param move  The move that is to be validated.
     *
     *  @return Boolean indicating if it was valid.
     * 
     *  @pre a player has made a move
     *  @post the player knows if the move has legal
     */
    public void validateMove(Move move ) {

	boolean retval = false;
	
        try {
	    
	    boolean anotherMove;  // If there is another move that
	                                  // must be made.
	    boolean gameOver;     // If the game is over.
	    Player player = gameStart.getCurrentPlayer(); // Current player.
	    Player oppositePlayer = gameStart.getOppositePlayer();
	    int start = move.startLocation(); // Start of piece.
	    int end = move.endLocation();  // Attempted end location
	                                             // of the piece.
	    int pieceType = gameStart.getTheBoard().getPieceAt( start ).getType();// Type of
	                                                      // the piece.
	    // Contains any possible moves if the piece is on the wall.
            Vector tempVec;
	    Vector startVec = new Vector();
	    //Vector possibleJumps = new Vector();
	    Vector possibleJumps = checkForPossibleJumps( start, pieceType,
							  player );
	    // Check all pieces for jumps.
	    //if ( player.getColor() == Color.white ) {
		//pieces = gameStart.getTheBoard().whitePieces(); 
	    //} else {
		//pieces = gameStart.getTheBoard().bluePieces();
	    //}
		
		// For each piece of the current color, see if there are forced jumps.
		for ( int count = 1; count < 64; count++ ) {
			if ( gameStart.getTheBoard().occupied( count ) ) {
				if ( gameStart.getTheBoard().getPieceAt( count ).getColor() == player.getColor() ) {
					tempVec = checkForPossibleJumps( count, pieceType, player );
					if ( !tempVec.isEmpty() ) {
						startVec.addElement(count);
						possibleJumps.addAll( tempVec );
					}
				}
			}
											
		}

	    // Only proceed if player is trying to move one of his own pieces
													  
		if ( !gameStart.getTheBoard().colorAt( start ).equals(
			        gameStart.getOppositePlayer().getColor() ) ) {
		// If there is a possible jump it must be made so the end 
		// position must match one of the possible jumps.
		if ( startVec.contains(start) ) {
		    possibleJumps = checkForPossibleJumps( start, pieceType, 
							   player );
		    if ( possibleJumps.contains(end) ) {
			// Move the piece
			gameStart.getTheBoard().movePiece( start, end ); 
			// Remove the jumped piece
			gameStart.getTheBoard().removePiece( middle );
			middle = 0;		
			// And if there is a possible multiple jump.
			anotherMove = checkForOtherPossibleJump( end, pieceType,
								 player );
			// If there is another jump to make, next turn will be
			// current player's and he must move last piece moved.
			if ( anotherMove ) {
			    gameStart.getTurn().endTurn( player, end );
			}
			// Otherwise, next turn should be the opposite player's.
			else {
			    gameStart.getTurn().endTurn( oppositePlayer, 0 );
			}
			retval = true;
		    } // There is no required jump.
		}
		// Otherwise the player is free to make any move that is legal.
		else if ( possibleJumps.isEmpty() ) {
                    
		    // If the piece starts on a wall and it's end position is 
		    // valid then the move is legal.
		    if ( leftWallPieces.contains(start) ||
			 rightWallPieces.contains(start) ) {
                Vector wallMoves = new Vector(wallPieceMoves(start, false,
                        pieceType, player));
			if ( wallMoves.contains(end) ) {
			    retval = true;
			}
		    }
		    
		    // If the end position is not occupied then validate move.
		    if ( !gameStart.getTheBoard().occupied( end ) ) { 
			retval = validateRegularMove( start, end, player );
		    }
                
		    // If move was OK check end conditions. If game was not won,
		    // end turn.
		    if ( retval ) {
                        
			// If a move was made, see if the piece should be kinged
			checkForMakeKing( end, pieceType );
                        gameOver = checkEndCond();
			if ( gameOver ) {
			    gameStart.endGame( player.getPlayerName() +
					       " won the game." );
			} else {			    
			    gameStart.getTheBoard().movePiece( start, end );
			    gameStart.getTurn().endTurn( oppositePlayer, 0 );
			}
			
		    } 
                } // Move is either valid or not. 
	    } // end if piece on start space is the correct color
	    
	    // If the move was not valid, tell the player. 
	    if ( !retval ) {
		gameStart.getTurn().endTurn( player, -1 );
	    }
            checkForMakeKing(end, pieceType);
        } catch ( Exception ignored) { }

    }
    
    /**
     *  Used after a move has been completed to check to see
     *  if the conditions have been met for ending the game.
     *  a boolean is returned indicating whether or not those
     *  conditions have been met.
     *
     *  @return retval true indicating the game is to end.
     * 
     *  @pre a capture was successful
     */
    private boolean checkEndCond() {
	
	boolean retval = false;
	
	if ( checkForNoPieces() ) {
	    retval = true;
	}	
	
	return retval;
 	
    }

	/**
     *  Will check if there are any more pieces left for the player 
     *  whose turn it is not.
     * 
     *  @return true if the other player has no more pieces.
     *
     *  @pre A capture was successful.
     */ 
    private boolean checkForNoPieces() {
	
	boolean retval = false;
	Player oppositePlayer = gameStart.getOppositePlayer();
	
	// If the board does not have any pieces of the opposite player,
	// the current player has captured all pieces and won.
	if ( !gameStart.getTheBoard().hasPieceOf( oppositePlayer.getColor() ) ) {
	    retval = true;
	}
	
	return retval;
	
    }

    
    /**
     *  Will check the board for any jumps that are open to the current 
     *  player. If there are any possible jumps the valid end positions 
     *  will be added to the vector.
     *
     *  @param piecePosition - start position of piece. 
     *  @param pieceType     - type of piece.
     *
     *  @return possibleJumps which contains end positions of possible jumps.
     */	
    private Vector checkForPossibleJumps( int piecePosition, int pieceType, 
					  Player aPlayer ) {

        boolean adjacentSpace;
	boolean endSpace;

        Piece aPiece = new Piece( aPlayer.getColor());
	int i;
        int loop;
	
	// Get available moves if the piece is on a wall.
        Vector possibleJumps = new Vector(wallPieceMoves(piecePosition,
                true, pieceType, aPlayer));
	
	// If the piece is a king.
	if ( pieceType == Board.KING) {
	    
	    // King is on top wall.
            if ( piecePosition <= 7 ) {
                i = 2;
                loop = adjacentSpots.length;
            } 
	    // King is on bottom wall.
	    else if ( piecePosition >= 56 ) {
                i = 0;
                loop = 2;
            } else {
                i = 0;
                loop = adjacentSpots.length;
            }
	    
	    // Check to see if piece is adjacent to piece of opposite color.
	    // If there are, add possible end locations to vector.
		checkSpaces(piecePosition, aPlayer, i, loop, possibleJumps);
	}
	// If piece is white...        
	else if ( aPlayer.getColor() == Color.white ) {
	    
	    
	    for ( int j = 0; j <= 1; j++ ) {
		if ( !leftWallPieces.contains(piecePosition +
                adjacentSpots[j]) &&
		     !rightWallPieces.contains(piecePosition +
                     adjacentSpots[j]) ) {
		    // Check to see if there is a piece this piece can jump over.
		    adjacentSpace = gameStart.getTheBoard().occupied( piecePosition + 
						       adjacentSpots[j] );
		    // Get that piece
                    
                    if( adjacentSpace ){
		    aPiece = gameStart.getTheBoard().getPieceAt( piecePosition +
						  adjacentSpots[j] );
		    //player = aPlayer;
                    }
		    // Make sure the piece is the right color
		    if ( adjacentSpace && aPiece.getColor() !=
			 aPlayer.getColor() ) {
			// Check space diagonal to piece to see if it is empty.
			endSpace = gameStart.getTheBoard().occupied( piecePosition + 
						      secondSpots[j] );
			// If space is empty, there is a jump that must be made.
			if ( !endSpace ) {
			    possibleJumps.addElement(piecePosition
                        + secondSpots[j]);
			}
			// If the player has selected to make this jump,
			// let other methods access the piece to be jumped
			if ( gameStart.getCurrentPlayer().currentEnd == piecePosition
			                                 + secondSpots[j] ) {
			    middle = piecePosition + adjacentSpots[j];
			}   
		    }
        } // end for
	    }
	}
	// else the Color is blue and can only move down.
	else {
		checkSpaces(piecePosition, aPlayer, 2, adjacentSpots.length, possibleJumps);
	}
	
	return possibleJumps;
	
    }

	private void checkSpaces(int piecePosition, Player aPlayer, int i, int end, Vector possibleJumps) {
		boolean adjacentSpace;
		Piece aPiece;
		boolean endSpace;
		for (; i < end; i++ ) {
        if ( !leftWallPieces.contains(piecePosition +
                adjacentSpots[i]) &&
             !rightWallPieces.contains(piecePosition +
                     adjacentSpots[i]) ) {

            adjacentSpace = gameStart.getTheBoard().occupied( piecePosition +
                               adjacentSpots[i] );
            aPiece = gameStart.getTheBoard().getPieceAt( piecePosition +
                          adjacentSpots[i] );

            if ( adjacentSpace &&
             ( aPiece.getColor() != aPlayer.getColor() ) ) {
            // Check space diagonal to the piece to see if it is empty.
            endSpace = gameStart.getTheBoard().occupied( piecePosition +
                              secondSpots[i] );
            /// If the space is empty, the jump is valid and it is
            // added to vector of possible jumps.
            if ( !endSpace ) {
                possibleJumps.addElement(piecePosition
                        + secondSpots[i]);
                // If the player has selected to make this jump,
                // let other methods access the piece to be jumped
                if ( gameStart.getCurrentPlayer().currentEnd == piecePosition +
                 secondSpots[i] ) {
                middle = piecePosition + adjacentSpots[i];
                }
            }
            }
        } // end for
        } // end if (the piece is not a king).
	}

	/**
     *   After a piece has made a jump, check to see if another jump can
     *  be made with the same piece.
     *
     *  @param piecePosition - start position of the piece.
     *  @param pieceType     - the type of the piece.
     *
     *  @return retval true if there is a no other jump that must be made.
     *
     *  @pre A jump has been made.
     */   
    private boolean checkForOtherPossibleJump( int piecePosition,
					       int pieceType,
					       Player aPlayer ) {
	
	boolean retval = false;
	
    	try{
	    
	    boolean adjacentSpace;
            Piece aPiece;
	    Player player = aPlayer;
	    boolean endSpace;

	    // If the piece is a king.
	    if ( pieceType == Board.KING) {
		
		// Check to see if piece is adjacent to piece of opposite color.
		// If there are, add possible end locations to vector.
		for ( int i = 0; i <= adjacentSpots.length; i++ ) {
		    if ( !leftWallPieces.contains(piecePosition +
                    adjacentSpots[i]) &&
			 !rightWallPieces.contains(piecePosition +
                     adjacentSpots[i]) ) {
			
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition +
							   adjacentSpots[i] );
			aPiece = gameStart.getTheBoard().getPieceAt( piecePosition +
						      adjacentSpots[i] );
			
			if ( adjacentSpace && ( aPiece.getColor() !=
						player.getColor() ) ) {
			    
			    // Check space diagonal to piece to see if its empty.
			    endSpace = gameStart.getTheBoard().occupied( piecePosition + 
							  secondSpots[i] );
			    
			    // If space is empty, a multiple jump must be made.
			    if ( !endSpace ) {
				
				retval = true;
				
			    }			 
			    
			}

            } // end for
		}
	    } 
	    // Else the piece is regular.
	    else {
		// If it is white the player can only move up.
		if ( player.getColor() == Color.white ) {
		    for ( int j = 0; j <= 1; j++ ) {
			if ( !leftWallPieces.contains(piecePosition + adjacentSpots[j]) &&
			     !rightWallPieces.contains(piecePosition + adjacentSpots[j]) ) {
			    adjacentSpace = gameStart.getTheBoard().occupied( piecePosition +
							    adjacentSpots[j] );
			    aPiece = gameStart.getTheBoard().getPieceAt( piecePosition +
							  adjacentSpots[j] );
			    player = gameStart.getCurrentPlayer();
			    if ( adjacentSpace && ( aPiece.getColor() != 
						    player.getColor() ) ) {
				// Check space diagonal to the piece to see if
				// it is empty.
				endSpace = gameStart.getTheBoard().occupied( piecePosition +
							      secondSpots[j] );
				// If the space is empty, there is a 
				// multiple jump that must be made.
				if ( !endSpace ) {
				    retval = true;
				}			 
			    }
            } // end for
		    }
		} 
		// else the Color is blue and can only move down.
		else {
		    for ( int k = 2; k <= adjacentSpots.length; k++ ) {
			if ( !leftWallPieces.contains(piecePosition + adjacentSpots[k]) &&
			     !rightWallPieces.contains(piecePosition + adjacentSpots[k]) ) {
			    int a = piecePosition + adjacentSpots[k];
			    adjacentSpace = gameStart.getTheBoard().occupied( a );
			    aPiece = gameStart.getTheBoard().getPieceAt( piecePosition +
							  adjacentSpots[k] );
			    
			    player = gameStart.getCurrentPlayer();
			    if ( adjacentSpace && ( aPiece.getColor() !=
						    player.getColor() ) ) {
				// Check the space diagonal to the piece to see 
				// if it is empty.
				endSpace = gameStart.getTheBoard().occupied( piecePosition +
							      secondSpots[k] );
				
				// If the space is empty, there is a multiple
				// jump that must be made.
				if ( !endSpace ) {
				    retval = true;
				}			 
			    }
            } // end for
		    }
		}
	    }
	    
        } catch( Exception ignored) { }
	
	return retval;
	
    }
    
    /*
     * Validate a regular, non jumping move.
     *
     * @param piecePosition - the starting piece position.
     * @param end - the end piece position.
     *
     * @return true if the move is valid.
     */
    private boolean validateRegularMove( int piecePosition, int end, 
					 Player aPlayer ) {
	
	boolean retval = false;

        // If piece is a king
	if ( gameStart.getTheBoard().getPieceAt( piecePosition ).getType() == Board.KING) {
	    //Check if piece is on wall. If it is it's movement is restricted.
	    if ( leftWallPieces.contains(piecePosition) ||
                 rightWallPieces.contains(piecePosition)) {
		// Check if end position is valid.
		if ( end == piecePosition + 7 || end == piecePosition - 7 ) {
		    retval = true;
		}
		// Piece is not on left or right wall.
	    } else {
		// Check if end position is valid.
		if ( end == piecePosition - 7 || end == piecePosition - 9 ||
		     end == piecePosition + 7 || end == piecePosition + 9 ) {
		    retval = true;
		}
	    }
	} // end if (piece is king).
	
	// If the piece is regular piece then it can only move in one
	// direction and has at most 2 possible moves.
	else {
	    // If the piece is white it may only move up (down the array).
	    // If it is on the wall it can only move to one spot.
	    if ( aPlayer.getColor() == Color.white &&
		 ( leftWallPieces.contains(piecePosition) ||
                   rightWallPieces.contains(piecePosition) ) &&
		 ( end == piecePosition - 9 ) ) {
		
		retval = true;
	    } 
	    // Otherwise the piece can move to one of two places.
	    else if ( aPlayer.getColor() == Color.white &&
		    ( end == piecePosition - 7 || end == piecePosition - 9 ) ) {
		
		retval = true;
	    }	
	    // Otherwise the player is blue and can only move down (up array).
	    // If the player is on the wall it can only move to one spot.
	    else if ( aPlayer.getColor() == Color.blue &&
		      ( leftWallPieces.contains(piecePosition)
		 || rightWallPieces.contains(piecePosition) ) &&
		      ( end == piecePosition + 9 ) ) { 
		
		retval = true;
	    }
	    // The piece is free to move to one of two spots down the board.
	    else if ( aPlayer.getColor() == Color.blue &&
		    ( end == piecePosition + 7 || end == piecePosition + 9 ) ) {
		retval = true;
	    }
	} 
	
	return retval;
	
    }

	/*
     *  Fill the vectors that contain wall pieces.
     *
     *  @pre  The vectors are empty.
     *  @post The vectors are filled.
     */
    private void fillWallVector() {
	
	rightWallPieces.addElement(7);
	leftWallPieces.addElement(8);
	rightWallPieces.addElement(23);
	leftWallPieces.addElement(24);
	rightWallPieces.addElement(39);
	leftWallPieces.addElement(40);
	rightWallPieces.addElement(55);
	leftWallPieces.addElement(56);
	
    }
    
    /*
     *  This method will check the available moves for pieces if the piece
     *  is on the left or right walls. 
     *
     *  @param piecePosition - the starting position of the piece.
     *  @param jump          - true if the piece is making a jump.
     *  @param pieceType     - type of piece.
     *
     *  @return moves, a vector of end positions for the piece.
     */
    private Vector wallPieceMoves( int piecePosition, boolean jump, 
				   int pieceType , Player aPlayer ) {
	
	Vector moves = new Vector();
	
        try{
	    
	    boolean adjacentSpace;
	    Piece aPiece;
            boolean endSpace;
	    
	    if ( pieceType == Board.KING) {
		if ( leftWallPieces.contains(piecePosition) ) {
		    if ( jump ) {
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition - 7 );
			aPiece = gameStart.getTheBoard().getPieceAt( piecePosition - 7 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						aPlayer.getColor() ) ) {
			    // Check the space diagonal to the piece to see
			    // if it is empty.
			    endSpace = gameStart.getTheBoard().occupied( piecePosition - 14 );
			    // If the space is empty, there is a multiple
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement(piecePosition
                        - 14);
			    }
			}
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition + 9 );
			aPiece = gameStart.getTheBoard().getPieceAt( piecePosition + 9 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						aPlayer.getColor() ) ) {
			    // Check the space diagonal to the piece to see if
			    // it is empty.
			    endSpace = gameStart.getTheBoard().occupied( piecePosition + 18 );
			    // If the space is empty, there is a multiple 
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement(piecePosition
                        + 18);
			    }
			}
		    } 
		    // Otherwise check for a regular move, not a jump.
		    else {
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition - 7 );
			if ( !adjacentSpace ) {
			    moves.addElement(piecePosition - 7);
			}
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition + 9 );
			if ( !adjacentSpace ) {
			    moves.addElement(piecePosition + 9);
			}
		    }
		}
		// If the piece is on the right wall.
                if ( rightWallPieces.contains(piecePosition) ) {
	            if ( jump ) {
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition - 9 );
			aPiece = gameStart.getTheBoard().getPieceAt( piecePosition - 9 );
			if ( adjacentSpace && ( aPiece.getColor() != 
						aPlayer.getColor() ) ) {
			    // Check the space diagonal to the piece to 
			    // see if it is empty.
			    endSpace = gameStart.getTheBoard().occupied( piecePosition - 18 );
			    // If the space is empty, there is a multiple
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement(piecePosition
                        - 18);
			    }
			}
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition + 7 );
			aPiece = gameStart.getTheBoard().getPieceAt( piecePosition + 7 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						aPlayer.getColor() ) ) {
			    // Check the space diagonal to the piece to see
			    // if it is empty.
			    endSpace = gameStart.getTheBoard().occupied( piecePosition + 14 );
			    // If the space is empty, there is a multiple 
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement(piecePosition
                        + 14);
			    }
			}
                    }
		    else {
                        // Otherwise check for a regular move, not a jump.
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition - 9 );
			if ( !adjacentSpace ) {
			    moves.addElement(piecePosition - 9);
			}
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition + 7 );
			if ( !adjacentSpace ) {
			    moves.addElement(piecePosition + 7);
			}
                    }
                    
	        } // end if the piece is on the right wall.
	    }	
	    
	    // The piece is not a king.  If its color is white...
	    else if ( aPlayer.getColor() == Color.white ) {
		if ( leftWallPieces.contains(piecePosition) ) {
		    if ( jump ) {
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition - 7 );
			aPiece = gameStart.getTheBoard().getPieceAt( piecePosition - 7 );
			if ( adjacentSpace && ( aPiece.getColor() != 
						aPlayer.getColor() ) ) {
			    // Check space diagonal to piece to see if
			    // it is empty.
			    endSpace = gameStart.getTheBoard().occupied( piecePosition - 14 );
			    // If the space is empty, there is a multiple
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement(piecePosition
                        - 14);
			    }
			}
		    } 
		    // Otherwise check for a regular move, not a jump.
		    else {
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition - 7 );
			if ( !adjacentSpace ) {
			    moves.addElement(piecePosition - 7);
			}
		    }
		}
		if ( rightWallPieces.contains(piecePosition) ) {
		    if ( jump ) {
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition - 9 );
			aPiece = gameStart.getTheBoard().getPieceAt( piecePosition - 9 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						aPlayer.getColor() ) ) {
			    // Check the space diagonal to the piece to see
			    // if it is empty.
			    endSpace = gameStart.getTheBoard().occupied( piecePosition - 18 );
			    // If the space is empty, there is a multiple
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement(piecePosition
                        - 18);
			    }
			}
                        // Regular move, not a jump.
		    } else {
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition - 9 );
			if ( !adjacentSpace ) {
			    moves.addElement(piecePosition - 9);
			}
		    }
		} // end if the piece is on the right wall.
	    } // end if the piece is white.
	    // The piece must be blue.
	    else { 
		if ( leftWallPieces.contains(piecePosition) ) {
		    if ( jump ) {
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition + 9 );
			aPiece = gameStart.getTheBoard().getPieceAt( piecePosition + 9 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						aPlayer.getColor() ) ) {
			    // Check the space diagonal to the piece to 
			    // see if it is empty.
			    endSpace = gameStart.getTheBoard().occupied( piecePosition + 18 );
			    // If the space is empty, there is a
			    // multiple jump that must be made.
			    if ( !endSpace ) {
				moves.addElement(piecePosition
                        + 18);
			    }
			}
		    } 
		    // Otherwise check for a regular move, not a jump.
		    else {
			adjacentSpace = gameStart.getTheBoard().occupied(  piecePosition + 9 );
			if ( !adjacentSpace ) {
			    moves.addElement(piecePosition + 9);
			}
		    }
		}
		if ( rightWallPieces.contains(piecePosition) ) {
		    if ( jump ) {
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition + 7 );
			aPiece = gameStart.getTheBoard().getPieceAt( piecePosition + 7 );
			if ( adjacentSpace && ( aPiece.getColor() !=
						aPlayer.getColor() ) ) {
			    // Check the space diagonal to the piece to see
			    // if it is empty.
			    endSpace = gameStart.getTheBoard().occupied( piecePosition + 14 );
			    // If the space is empty, there is a multiple 
			    // jump that must be made.
			    if ( !endSpace ) {
				moves.addElement(piecePosition
                        + 14);
			    }
			}
		    } else {
			adjacentSpace = gameStart.getTheBoard().occupied( piecePosition + 7 );
			if ( !adjacentSpace ) {
			    moves.addElement(piecePosition + 7);
			}
		    }
		} // end if the piece is on the right wall.
	    }    
	    
        } catch( Exception ignored) {}

        return moves;
   
    }
    
    /*
     * Will check if a single piece needs to be kinged.
     *
     * @param end - the piece position.
     * @param pieceType - the type of piece it is.
     *
     * @return true if the piece needs to be kinged.
     */
    private void checkForMakeKing(int end, int pieceType ) {

	try {
	    if ( pieceType == Board.SINGLE ) {
		if ( gameStart.getCurrentPlayer().getColor() == Color.white ) {
		    if ( end == 1 || end == 3 || end == 5 || end == 7 ) {
			gameStart.getTheBoard().kingPiece( end );
		    }
		} else {
		    if ( end == 56 || end == 58 || end == 60 || end == 62 ) {
			gameStart.getTheBoard().kingPiece( end );
		    }
		}
		
	    } // if single
	    
	} catch ( Exception ignored) {}

    }
    
}//Rules.java

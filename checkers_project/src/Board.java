/*
  Board.java

  Version:
      $Id: Board.java,v 1.1 2002/10/22 21:12:52 se362 Exp $

  Revisions:
      $Log: Board.java,v $
      Revision 1.1  2002/10/22 21:12:52  se362
      Initial creation of case study

 */
import java.awt.*;


/**
 *  This class represents the board on which checkers is being played.
 *  The board holds a collection of pieces.
 *
 *  @invariant all variables have valid values
 *
 *  @author
 */
public class Board {

   private final Piece[] pieces; // the pieces that are on the board
    public static final int SINGLE = 0;
   public static final int KING = 1;


   /**
    * This constructor creates a new board at the beginning of the game
    */

   public Board() {
  
	   // create a array of size 64, generate piece objects and
	   // put them in the correct location in the array
	   // Set the values of numWhites and numBlues to 12 each
	   pieces = new Piece[64];

	   // create blue pieces
	   pieces[1] = new Piece( Color.blue );
	   pieces[3] = new Piece( Color.blue );
	   pieces[5] = new Piece( Color.blue );
	   pieces[7] = new Piece( Color.blue );
	   pieces[8] = new Piece( Color.blue );
	   pieces[10] = new Piece( Color.blue );
	   pieces[12] = new Piece( Color.blue );
	   pieces[14] = new Piece( Color.blue );
	   pieces[17] = new Piece( Color.blue );
	   pieces[19] = new Piece( Color.blue );
	   pieces[21] = new Piece( Color.blue );
	   pieces[23] = new Piece( Color.blue );

	   // create the white pieces
	   pieces[40] = new Piece( Color.white );
	   pieces[42] = new Piece( Color.white );
	   pieces[44] = new Piece( Color.white );
	   pieces[46] = new Piece( Color.white );
	   pieces[49] = new Piece( Color.white );
	   pieces[51] = new Piece( Color.white );
	   pieces[53] = new Piece( Color.white );
	   pieces[55] = new Piece( Color.white );
	   pieces[56] = new Piece( Color.white );
	   pieces[58] = new Piece( Color.white );
	   pieces[60] = new Piece( Color.white );
	   pieces[62] = new Piece( Color.white );

   }

   

   /**
    * Move the piece at the start position to the end position
    * 
    * @param start - current location of the piece
    * @param end - the position where piece is moved
    * 
    * @return -1 if there is a piece in the end position
    */
   public void movePiece(int start, int end) {


	   // check if the end position of the piece is occupied
	   if( !occupied( end ) ) {
		   // if it is not set a start position in the array to null
		   // put a piece object at the end position in the array
		   // that was at the start position before
		   pieces[end] = pieces[start];
		   pieces[start] = null;
	   }
   }

   

   /**
    * This method checks if the space on the board contains a piece
    * 
    * @param space - the space that needs to be checked
    * 
    * @return true or false depending on the situation
    */
   public boolean occupied(int space) {

	   // if it's in the bounds of the array,
	   	   // return true if the space is occupied
	   	   // false if the space is empty
	   // if it's outside the bounds of the array,
	   	   // return true

       return space < 1 || space > 63 || pieces[space] != null;
	   

	   
   }

   
   /**
    * This method removes piece at the position space
    * 
    * @param space - the position of the piece to be removed
    */
   public void removePiece(int space) {
   
	   // go to the space position in the array
	   // set it equal to null
	   
	   pieces[ space ] = null;

   }
   
   
   /**
    * This method creates a king piece 
    * 
    * @param space - the position at which the king piece is created
    */
   public void kingPiece(int space) {
   
	   // create a new king piece
	   // go to the space position in the array and place it there
	   // if the position is not occupied
	   pieces[space].makeKing();
	   
   }
   
   
   /**
    * This method returns the color of the piece at a certain space
    * 
    * @param space - the position of the piece on the board
    * 
    * @return the color of this piece
    */
   public Color colorAt(int space) {
	   
	   Color returnValue = null;
	   // go to the space position in the array
	   // check if there is a piece at that position
	   // if there is none, return null
	   // else return the color of the piece
	   
	   if( occupied( space ) ) {
		   
		   returnValue = pieces[space].getColor();
		   
	   }
   
       return returnValue;
	   
   }
   

   /**
    * This method returns the piece at the certain position
    * 
    * @param space - the space of the piece
    * 
    * @return the piece at that space
    */
   public Piece getPieceAt(int space) {

	   Piece returnValue = new Piece(Color.red);
	   
	   try{
	   	   // check if there is piece at space position
	   	   // if there is none, return null
	   	   // else return the piece at that position
	   
	      if( occupied(  space ) ) {
		   
	   	   	   returnValue = pieces[space];
                           
	   	   }
	   
	   } catch( ArrayIndexOutOfBoundsException | NullPointerException ignored) {
	   
                          
	   }

       return returnValue;
	   
   }
   
   
   
   /**
    * This method returns if there is a piece of color on the board
    * 
    * @param color - the color of the piece
    * 
    * @return true if there is a piece of color left on the board
    *				else return false	
    */
   public boolean hasPieceOf( Color color) {
   	

	   boolean returnValue = false;

	   // go through the whole array
	   // if there is a piece of color in the array return true
	   // else return false
	   for( int i =1; i < pieces.length; i++ ) {
		   
	   	   if( pieces[i] != null && pieces[i].getColor() == color ) {

                  
	   	   	   	   returnValue = true;
	   	   	   	   i = pieces.length;
	   	   	   }
	   }

           
      return returnValue;
	   
   }
   

   /**
    * This method returns the size of the board
    * 
    * @return the size of the board, always 64
    */
   public int sizeOf() {
	   return 64;
   }


}//Board


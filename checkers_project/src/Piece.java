/*
  Piece.java

  Version:
    $Id: Piece.java,v 1.1 2002/10/22 21:12:53 se362 Exp $

  Revisions:
    $Log: Piece.java,v $
    Revision 1.1  2002/10/22 21:12:53  se362
    Initial creation of case study

 */

/*
  This is an abstract class representing any piece that
  know about it's color and possible moves and captures

  @author

 */

import java.awt.*;

public class Piece {
	
   private Color color; // the color of the piece

    private void setColor(Color color) {
        this.color = color;
    }

    private enum Type {Single, King} // the type of the piece
   private Type type = Type.Single;
      
   /**
    * The constructor for this piece
    * 
    * @param c - the color for this piece
    */
   public Piece( Color c ) {

	   // set the color
	   setColor(c);
   }

   /**
    * The method returns the type of this piece
    * 
    * @return the type of the piece
    */
   public int getType() {
   	   if (type == Type.Single) {
           return 0;
       } else {
           return 1;
       }
   }

   /**
    * The method changes the type of this piece to King
    */
   public void makeKing() {
       this.type = Type.King;
   }
   
   /**
    * This method returns the color of this piece
    * 
    * @return the color for this piece
    */
   public Color getColor() {
	   return color;
   }

}// Piece

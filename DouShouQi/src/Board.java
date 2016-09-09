/*
 * file: Board.java
 * 
 * rule book:
 *   http://veryspecial.us/free-downloads/AncientChess.com-DouShouQi.pdf
 * video of game play & nice picture of initial board setup:
 *   http://ancientchess.com/page/play-doushouqi.htm
 * play online:
 *   http://liacs.leidenuniv.nl/~visjk/doushouqi/
 *   
 * Copyright � George J. Grevera, 2016. All rights reserved.
 */
public class Board {
    
    //the low order 5 bits is one of the following playing/moveable pieces
    // (or none):
    public static final byte  rbNone    =  0;
    //constants for red pieces
    public static final byte  rRat      =  1;
    public static final byte  rCat      =  2;
    public static final byte  rDog      =  3;
    public static final byte  rWolf     =  4;
    public static final byte  rLeopard  =  5;
    public static final byte  rTiger    =  6;
    public static final byte  rLion     =  7;
    public static final byte  rElephant =  8;
    //constants for black (or blue, if you prefer) pieces
    public static final byte  bRat      =  9;
    public static final byte  bCat      = 10;
    public static final byte  bDog      = 11;
    public static final byte  bWolf     = 12;
    public static final byte  bLeopard  = 13;
    public static final byte  bTiger    = 14;
    public static final byte  bLion     = 15;
    public static final byte  bElephant = 16;

    public static final byte  fPieceMask = (byte)0x1f;  //to isolate piece bits
    
    //the high order 3 bits indicates one of these board position type values.
    // note: these never move but are part of the board itself.
    public static final byte  cNone   = 0;               //not used/out of bounds
    public static final byte  cWater  = (byte)(1 << 5);  //water
    public static final byte  cGround = (byte)(2 << 5);  //ordinary ground
    public static final byte  cRTrap  = (byte)(3 << 5);  //red (side of board) trap
    public static final byte  cBTrap  = (byte)(4 << 5);  //black (side of board) trap
    public static final byte  cRDen   = (byte)(5 << 5);  //red (side of board) den
    public static final byte  cBDen   = (byte)(6 << 5);  //black (side of board) den
    
    public static final byte  fBoardMask = (byte)0xe0;   //to isolate type bits

    public static final int   fRows = 9;  //# of board rows
    public static final int   fCols = 7;  //# of board cols
    
    public static enum Color { None, Red, Black };  //color of piece (or none)
    //-----------------------------------------------------------------------
    //the playing board.  mBoard[0][0] is the upper left corner.
    protected byte     mBoard[][]  = new byte[ fRows ][ fCols ];
    protected boolean  mBlacksTurn = true;  //by convention, black goes first
    //-----------------------------------------------------------------------
    // init the board.  by convention, red will initially be in the top half
    // (0,0) of the board, and black will start in the bottom half.
    // be careful.  the opposite sides do not mirror each other!
    public Board ( ) {
        // \todo v1
    }
    //-----------------------------------------------------------------------
    // return the specific (moveable) piece (e.g., bWolf or rbNone) at the
    // indicated position.
    public int getPiece ( int r, int c ) {
        // \todo v1
        return rbNone;
    }
    //-----------------------------------------------------------------------
    // given a piece, return its rank (or 0 for an unknown piece).
    // rat is 1, cat is 2, dog is 3, wolf is 4, leopard is 5, tiger is 6, lion is 7, elephant is 8.
    public int getRank ( int p ) {
        // \todo v1
        return 0;
    }
    //-----------------------------------------------------------------------
    // return the rank of the piece at the specified position (or 0 for none).
    // rat is 1, cat is 2, dog is 3, wolf is 4, leopard is 5, tiger is 6, lion is 7, elephant is 8.
    public int getRank ( int r, int c ) {
        // \todo v1
        return 0;
    }
    //-----------------------------------------------------------------------
    // returns what appears on the underlying board at the specified position
    // (e.g., cWater), or cNone if out of bounds.
    public int getBoard ( int r, int c ) {
        // \todo v1
        return 0;
    }
    //-----------------------------------------------------------------------
    // returns the color of the piece (or Color.None) at the specified location.
    public Color getColor( int r, int c ) {
        // \todo v1
        return Color.None;
    }
    //-----------------------------------------------------------------------
    // returns t if this spot does not have any (moveable) piece on it;
    // f otherwise or if out of bounds.
    public boolean isEmpty ( int r, int c ) {
        // \todo v1
        return false;
    }
    //-----------------------------------------------------------------------
    // returns a string representing the board that can be pretty-printed.
    // it should look something like the following:
    //
    //     --...-        --...-     \n
    //    |      |      |      |    \n
    //    .      .      .      .     .
    //    .      .      .      .     .
    //    .      .      .      .     .
    //    |      |      |      |    \n
    //     --...-        --...-     \n
    //
    // the left side of the string should be the underlying board.
    // the right side should be the pieces at their specific locations.
    // put the first 3 characters of the name at each location
    // (e.g., rLi for the red lion, and bEl for the black elephant).
    //
    // if you have a better idea, please let me know!
    @Override
    public String toString ( ) {
        // \todo v1
        return "";
    }

}  //end class Board

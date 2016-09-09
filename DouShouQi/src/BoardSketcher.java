/*
 * file: BoardSketcher.java
 * 
 * Copyright © George J. Grevera, 2016. All rights reserved.
 */
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

class BoardSketcher extends JPanel {

    private static final long serialVersionUID = -375357054354551873L;

    public static final int  size = 50;
    
    private Board b = null;

    BoardSketcher ( Board b ) {
        this.b = b;
    }
    
    public void paint ( Graphics g ) {
        if (b == null)    return;
        
        System.out.println( b );
        
        for (int r=0; r<Board.fRows; r++) {
            for (int c=0; c<Board.fCols; c++) {
                int  v = b.getBoard( r, c );
                switch (v) {
                    case Board.cBDen :
                        g.setColor( Color.black );
                        g.fillRect( size*c, size*r, size, size );
                        break;
                    case Board.cBTrap :
                        g.setColor( Color.gray );
                        g.fillRect( size*c, size*r, size, size );
                        break;
                    case Board.cGround :
                        g.setColor( new Color(10,120,50) );
                        g.fillRect( size*c, size*r, size, size );
                        break;
                    case Board.cRDen :
                        g.setColor( Color.red );
                        g.fillRect( size*c, size*r, size, size );
                        break;
                    case Board.cRTrap :
                        g.setColor( Color.pink );
                        g.fillRect( size*c, size*r, size, size );
                        break;
                    case Board.cWater :
                        g.setColor( new Color(10,120,150) );
                        g.fillRect( size*c, size*r, size, size );
                        break;
                }
            }
        }
        
        g.setColor( Color.lightGray );
        for (int r=0; r<Board.fRows; r++) {
            for (int c=0; c<Board.fCols; c++) {
                int  v = b.getPiece( r, c );
                String  p = null;
                switch (v) {
                    case Board.bCat      :  p = "bCat";  break;
                    case Board.bDog      :  p = "bDog";  break;
                    case Board.bElephant :  p = "bEle";  break;
                    case Board.bLeopard  :  p = "bLeo";  break;
                    case Board.bLion     :  p = "bLio";  break;
                    case Board.bRat      :  p = "bRat";  break;
                    case Board.bTiger    :  p = "bTig";  break;
                    case Board.bWolf     :  p = "bWol";  break;

                    case Board.rCat      :  p = "rCat";  break;
                    case Board.rDog      :  p = "rDog";  break;
                    case Board.rElephant :  p = "rEle";  break;
                    case Board.rLeopard  :  p = "rLeo";  break;
                    case Board.rLion     :  p = "rLio";  break;
                    case Board.rRat      :  p = "rRat";  break;
                    case Board.rTiger    :  p = "rTig";  break;
                    case Board.rWolf     :  p = "rWol";  break;
                }
                if (p != null) {
                    if (p.charAt( 0 ) == 'r')
                        g.setColor( Color.red );
                    else
                        g.setColor( Color.darkGray );
                        
                    g.drawString( p,          c*size+15, r*size+20 );
                    int  rank = b.getRank( r, c );
                    g.drawString( "   "+rank, c*size+15, r*size+40 );
                }
            }
        }
        g.setColor( Color.darkGray );
        for (int r=0; r<=Board.fRows; r++) {
            g.drawLine( 0, size*r, size*Board.fCols, size*r );
        }
        for (int c=0; c<=Board.fCols; c++) {
            g.drawLine(  size*c,  0,  size*c, size*Board.fRows );
        }
    }

}

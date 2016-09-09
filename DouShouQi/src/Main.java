/*
 * file: Main.java
 * 
 * Copyright © George J. Grevera, 2016. All rights reserved.
 */
import java.awt.BorderLayout;
import java.awt.Insets;
import javax.swing.JFrame;

public class Main {

    public static void main ( String[] args ) {
        Board  b = new Board();
        
        JFrame  frame = new JFrame( "Dou Shou Qi / Jungle Chess" );
        frame.getContentPane().add( new BoardSketcher(b), BorderLayout.CENTER );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( BoardSketcher.size*Board.fCols,
                       BoardSketcher.size*Board.fRows );
        frame.setLocation( 500, 250 );
        frame.setVisible( true );
        
        //insets aren't available until visible so we need to resize again
        Insets  in = frame.getInsets();
        //System.out.println( "bottom = " + in.bottom );
        //System.out.println( "top = " + in.top );
        //System.out.println( "left = " + in.left );
        //System.out.println( "right = " + in.right );
        frame.setSize( BoardSketcher.size*Board.fCols + in.left + in.right  + 1,
                       BoardSketcher.size*Board.fRows + in.top  + in.bottom + 1 );
    }

}

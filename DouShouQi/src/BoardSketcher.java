/*
 * file: BoardSketcher.java
 *
 * Copyright George J. Grevera, 2016. All rights reserved.
 */

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

class BoardSketcher extends JPanel implements MouseInputListener {

    private static final long serialVersionUID = -375357054354551873L;

    public static final int size = 80;
    private Board b = null;
    private int fromR = -1, fromC = -1, toR = -1, toC = -1;
    public boolean soundOn = true;
    private Font f = new Font("Courier New", Font.BOLD, 18);

    private enum Sound {firstClick, secondClick, badMove, capture, winner}

    private Image piece[] = new Image[17];

    //-----------------------------------------------------------------------
    BoardSketcher(Board b) {
        this.b = b;
        //load the images for the (moveable) board pieces
        piece[Board.rbNone] = null;

        piece[Board.rRat] = Toolkit.getDefaultToolkit().getImage("graphics/rRat.png");
        piece[Board.rCat] = Toolkit.getDefaultToolkit().getImage("graphics/rCat.png");
        piece[Board.rDog] = Toolkit.getDefaultToolkit().getImage("graphics/rDog.png");
        piece[Board.rWolf] = Toolkit.getDefaultToolkit().getImage("graphics/rWolf.png");
        piece[Board.rLeopard] = Toolkit.getDefaultToolkit().getImage("graphics/rLeopard.png");
        piece[Board.rTiger] = Toolkit.getDefaultToolkit().getImage("graphics/rTiger.png");
        piece[Board.rLion] = Toolkit.getDefaultToolkit().getImage("graphics/rLion.png");
        piece[Board.rElephant] = Toolkit.getDefaultToolkit().getImage("graphics/rElephant.png");

        piece[Board.bRat] = Toolkit.getDefaultToolkit().getImage("graphics/bRat.png");
        piece[Board.bCat] = Toolkit.getDefaultToolkit().getImage("graphics/bCat.png");
        piece[Board.bDog] = Toolkit.getDefaultToolkit().getImage("graphics/bDog.png");
        piece[Board.bWolf] = Toolkit.getDefaultToolkit().getImage("graphics/bWolf.png");
        piece[Board.bLeopard] = Toolkit.getDefaultToolkit().getImage("graphics/bLeopard.png");
        piece[Board.bTiger] = Toolkit.getDefaultToolkit().getImage("graphics/bTiger.png");
        piece[Board.bLion] = Toolkit.getDefaultToolkit().getImage("graphics/bLion.png");
        piece[Board.bElephant] = Toolkit.getDefaultToolkit().getImage("graphics/bElephant.png");

        addMouseListener(this);
    }

    //-----------------------------------------------------------------------
    public void paint(Graphics g) {
        System.out.println("paint");
        if (b == null) return;

        System.out.println(b);

        g.setFont(f);
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int r = 0; r < Board.fRows; r++) {
            for (int c = 0; c < Board.fCols; c++) {
                int v = b.getBoard(r, c);
                switch (v) {
                    case Board.cBDen:
                        g.setColor(Color.black);
                        g.fillRect(size * c, size * r, size, size);
                        break;
                    case Board.cBTrap:
                        g.setColor(Color.gray);
                        g.fillRect(size * c, size * r, size, size);
                        break;
                    case Board.cGround:
                        g.setColor(new Color(10, 120, 50));
                        g.fillRect(size * c, size * r, size, size);
                        break;
                    case Board.cRDen:
                        g.setColor(Color.red);
                        g.fillRect(size * c, size * r, size, size);
                        break;
                    case Board.cRTrap:
                        g.setColor(Color.pink);
                        g.fillRect(size * c, size * r, size, size);
                        break;
                    case Board.cWater:
                        g.setColor(new Color(10, 120, 150));
                        g.fillRect(size * c, size * r, size, size);
                        break;
                }
            }
        }

        Graphics2D g2 = (Graphics2D)g;
        g.setColor(Color.lightGray);
        for (int r = 0; r < Board.fRows; r++) {
            for (int c = 0; c < Board.fCols; c++) {
                int v = b.getPiece(r, c);
                String p = null;
                switch (v) {
                    case Board.bCat:
                        p = "bCat";
                        break;
                    case Board.bDog:
                        p = "bDog";
                        break;
                    case Board.bElephant:
                        p = "bEle";
                        break;
                    case Board.bLeopard:
                        p = "bLeo";
                        break;
                    case Board.bLion:
                        p = "bLio";
                        break;
                    case Board.bRat:
                        p = "bRat";
                        break;
                    case Board.bTiger:
                        p = "bTig";
                        break;
                    case Board.bWolf:
                        p = "bWol";
                        break;

                    case Board.rCat:
                        p = "rCat";
                        break;
                    case Board.rDog:
                        p = "rDog";
                        break;
                    case Board.rElephant:
                        p = "rEle";
                        break;
                    case Board.rLeopard:
                        p = "rLeo";
                        break;
                    case Board.rLion:
                        p = "rLio";
                        break;
                    case Board.rRat:
                        p = "rRat";
                        break;
                    case Board.rTiger:
                        p = "rTig";
                        break;
                    case Board.rWolf:
                        p = "rWol";
                        break;
                    default:
                        p = null;
                        break;
                }
                if (p != null) {
                    g2.drawImage(this.piece[v], c * size, r * size, size, size, this);

                    if (p.charAt(0) == 'r') g.setColor(Color.red);
                    else g.setColor(Color.darkGray);
                    int rank = b.getRank(r, c);
                    g.drawString(p, c * size + 6, r * size + 15);
                    g.drawString("" + rank, (c + 1) * size - 12, (r + 1) * size - 5);

                }
            }
        }

        //draw grid
        g.setColor(Color.darkGray);
        for (int r = 0; r <= Board.fRows; r++) {
            g.drawLine(0, size * r, size * Board.fCols, size * r);
        }
        for (int c = 0; c <= Board.fCols; c++) {
            g.drawLine(size * c, 0, size * c, size * Board.fRows);
        }

        if (fromR != -1) {
            g.setColor(Color.yellow);
            //g.drawRect( size*fromC, size*fromR, size, size );
            g.draw3DRect(size * fromC, size * fromR, size, size, true);
        }
    }

    //-----------------------------------------------------------------------
    private void play(Sound s) {
        if (!soundOn) return;
        try {
            InputStream in = null;
            switch (s) {
                case badMove:
                    in = new FileInputStream("sounds/BananaImpact.wav");
                    break;
                case capture:
                    in = new FileInputStream("sounds/croc_chomp_x.wav");
                    break;
                case firstClick:
                    in = new FileInputStream("sounds/click_one.wav");
                    break;
                case secondClick:
                    in = new FileInputStream("sounds/Mousclik.wav");
                    break;
                case winner:
                    in = new FileInputStream("sounds/cheering.wav");
                    break;
                default:
                    System.err.println("bad sound");
                    return;
            }
            AudioStream as = new AudioStream(in);
            AudioPlayer.player.start(as);
        } catch (Exception e) {
            System.err.println("play sound failed");
        }
    }

    //-----------------------------------------------------------------------
    @Override
    public void mouseClicked(MouseEvent e) {
        int r = e.getY() / size;
        int c = e.getX() / size;
        System.out.println("mouseClicked: r=" + r + ", c=" + c);
        if (fromR == -1) {  //are we are waiting for a click on the first (from) piece?
            toR = toC = -1;
            Board.Color clr = b.getColor(r, c);
            if ((b.mBlacksTurn && clr == Board.Color.Black) ||
                    (!b.mBlacksTurn && clr == Board.Color.Red)) {
                fromR = r;
                fromC = c;
                toR = toC = -1;  //next state is waiting for the "to" click
                repaint();
                play(Sound.firstClick);
            } else {
                fromR = fromC = -1;
                repaint();
            }
            return;
        }

        if (toR == -1) {  //are we are waiting for the second click (to)?
            toR = r;
            toC = c;
            boolean pieceThere = b.getPiece(toR, toC) != Board.rbNone;  //capture?
            if (b.doMove(fromR, fromC, toR, toC)) {
                b.mBlacksTurn = !b.mBlacksTurn;
                if (pieceThere) play(Sound.capture);      //capture
                else play(Sound.secondClick);  //ordinary move
            } else {
                System.out.println("bad move");
                play(Sound.badMove);
            }
            fromR = fromC = toR = toC = -1;
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}

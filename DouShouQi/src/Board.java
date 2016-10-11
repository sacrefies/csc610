//------------------------------------------------------------------------------
//  File       : Board.java
//  Course     : DouShouQi
//  Date:      : 09/21/2016
//  Author     : Jason Qiao Meng
//  Author ID  : 10652564
//  Description: This file contains Board and piece control for DouShouQi game.
//------------------------------------------------------------------------------
// version 2
//   use these rules for game play (no variations except for the one noted
//   below):
//     http://veryspecial.us/free-downloads/AncientChess.com-DouShouQi.pdf
//
//   required variation (from https://en.wikipedia.org/wiki/Jungle_(board_game)):
//     "All traps are universal. If an animal goes into a trap in its own
//     region, an opponent animal is able to capture it regardless of rank
//     difference if it is beside the trapped animal."  this avoids the
//     known draw described in http://www.chessvariants.com/other.dir/shoudouqi2.html.
//
//   clarification: do not allow moves/captures where the attacker "loses."
//     for example, do not allow the mouse to attack the lion and "lose"
//     to the lion on purpose and be removed.
//   as much as possible, use the functions that you have already defined.
//------------------------------------------------------------------------------


import java.util.Arrays;

import static java.lang.Math.abs;

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
 * Copyright George J. Grevera, 2016. All rights reserved.
 */
public class Board {

    /**
     * No piece or an invalid piece
     */
    public static final byte rbNone = 0;
    /**
     * A red Rat
     */
    public static final byte rRat = 1;
    /**
     * A red Cat
     */
    public static final byte rCat = 2;
    /**
     * A red Dog
     */
    public static final byte rDog = 3;
    /**
     * A red Wolf
     */
    public static final byte rWolf = 4;
    /**
     * A red Leopard
     */
    public static final byte rLeopard = 5;
    /**
     * A red Tiger
     */
    public static final byte rTiger = 6;
    /**
     * A red Lion
     */
    public static final byte rLion = 7;
    /**
     * A red Elephant
     */
    public static final byte rElephant = 8;
    /**
     * A black Rat
     */
    public static final byte bRat = 9;
    /**
     * A black Cat
     */
    public static final byte bCat = 10;
    /**
     * A black Dog
     */
    public static final byte bDog = 11;
    /**
     * A black Wolf
     */
    public static final byte bWolf = 12;
    /**
     * A black Leopard
     */
    public static final byte bLeopard = 13;
    /**
     * A black Tiger
     */
    public static final byte bTiger = 14;
    /**
     * A black Lion
     */
    public static final byte bLion = 15;
    /**
     * A black Elephant
     */
    public static final byte bElephant = 16;
    /**
     * A bit mask to isolate a piece: {@code 0001 1111}.
     * An instance value fetched from a cell consists of both cell's type and a piece.
     * This mask separates the piece's value from the instance value.
     */
    public static final byte fPieceMask = (byte)0x1f;
    /**
     * Not used or out of bounds
     */
    public static final byte cNone = 0;
    /**
     * A water cell
     */
    public static final byte cWater = (byte)(1 << 5);
    /**
     * An ordinary ground
     */
    public static final byte cGround = (byte)(2 << 5);
    /**
     * A red trap
     */
    public static final byte cRTrap = (byte)(3 << 5);
    /**
     * A black trap
     */
    public static final byte cBTrap = (byte)(4 << 5);
    /**
     * A red Den
     */
    public static final byte cRDen = (byte)(5 << 5);
    /**
     * A black Den
     */
    public static final byte cBDen = (byte)(6 << 5);  //black (side of board) den
    /**
     * A bit mask to isolate a cell's type: {@code 1110 0000}.
     * An instance value fetched from a cell consists of both cell's type and a piece.
     * The high order 3 bits indicates one of these cell type values.
     * This mask separates the cell's type from the instance value.
     */
    public static final byte fBoardMask = (byte)0xe0;
    /**
     * The count of rows of this board
     */
    public static final int fRows = 9;  //# of board rows
    /**
     * The count of columns of this board
     */
    public static final int fCols = 7;  //# of board cols

    /**
     * Color code of a piece
     */
    public enum Color {
        /**
         * No color if no piece or invalid
         */
        None,
        /**
         * Red color
         */
        Red,
        /**
         * Black color
         */
        Black
    }

    /**
     * A 2D {@code Array} of {@code Integer} that holds the current playing board configuration.
     * {@code mBoard[0][0]} is the top-left corner.
     */
    protected byte mBoard[][] = new byte[fRows][fCols];
    /**
     * A {@code bool} value which specify whether it is black side's turn.
     * By convention, black goes first.
     */
    protected boolean mBlacksTurn = true;


    /**
     * Create an instance of class {@link Board} and initialize the board.<br/>
     * By convention, red will initially be in the top half (0,0) of the board,
     * and black will start in the bottom half.
     */
    public Board() {
        int mRow = getBoardRowMedian();
        int mCol = getBoardColumnMedian();
        // set red den
        mBoard[0][mCol] = cRDen;
        // set black den
        mBoard[fRows - 1][mCol] = cBDen;
        // set red trap
        for (int c = mCol - 1; c <= mCol + 1; c++) {
            if (c != mCol) {
                mBoard[0][c] = cRTrap;
                mBoard[fRows - 1][c] = cBTrap;
            } else {
                mBoard[1][c] = cRTrap;
                mBoard[fRows - 2][c] = cBTrap;
            }
        }
        // set water
        for (int r = mRow - 1; r <= mRow + 1; r++) {
            for (int c = 1; c < mCol; c++)
                mBoard[r][c] = cWater;
            for (int c = mCol + 1; c < fCols - 1; c++)
                mBoard[r][c] = cWater;
        }
        // set ground
        for (int r = 0; r < fRows; r++)
            for (int c = 0; c < fCols; c++)
                if (mBoard[r][c] == 0) mBoard[r][c] = cGround;

        // set pieces for red
        setPiece(0, 0, rLion);
        setPiece(0, fCols - 1, rTiger);
        setPiece(1, 1, rDog);
        setPiece(1, fCols - 2, rCat);
        setPiece(2, 0, rRat);
        setPiece(2, 2, rLeopard);
        setPiece(2, fCols - 3, rWolf);
        setPiece(2, fCols - 1, rElephant);
        // set pieces for black, diagonal-wise reversed.
        setPiece(fRows - 1, fCols - 1, bLion);
        setPiece(fRows - 1, 0, bTiger);
        setPiece(fRows - 2, fCols - 2, bDog);
        setPiece(fRows - 2, 1, bCat);
        setPiece(fRows - 3, fCols - 1, bRat);
        setPiece(fRows - 3, fCols - 3, bLeopard);
        setPiece(fRows - 3, 2, bWolf);
        setPiece(fRows - 3, 0, bElephant);
    }

    /**
     * Test whether a coming move is valid or not regardless of whose turn it is.
     *
     * @param fromRow The row index of the piece's current position.
     * @param fromCol The column index of the piece's current position.
     * @param toRow   The row index of the destination position.
     * @param toCol   The column index of the destination position.
     * @return Returns {@code true) if the proposed move is valid; returns {@code false} otherwise.
     */
    protected boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // legal position at FROM?
        // legal position at TO?
        // is to == from?
        if (!isLegalPosition(fromRow, fromCol) ||
                !isLegalPosition(toRow, toCol) ||
                isPositionNotChanged(fromRow, fromCol, toRow, toCol))
            return false;
        // is there a piece at FROM?
        if (getPiece(fromRow, fromCol) == rbNone) return false;
        // can it move to the desired position?
        if (!canMove(fromRow, fromCol, toRow, toCol)) return false;
        // is there a piece at TO?
        return getPiece(toRow, toCol) == rbNone || canCapture(fromRow, fromCol, toRow, toCol);
    }

    /**
     * Perform the specified move but only if it's valid.
     *
     * @param fromRow The row index of the piece's current position.
     * @param fromCol The column index of the piece's current position.
     * @param toRow   The row index of the destination position.
     * @param toCol   The column index of the destination position.
     * @return Returns {@code true) if the proposed move is valid; returns {@code false} otherwise.
     */
    public boolean doMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidMove(fromRow, fromCol, toRow, toCol))
            return false;

        // get piece
        int p = getPiece(fromRow, fromCol);
        // remove pieces at both from and to
        removePiece(fromRow, fromCol);
        removePiece(toRow, toCol);
        // set piece at to
        setPiece(toRow, toCol, p);
        return true;
    }

    /**
     * Return the specific (moveable) piece (e.g., bWolf or rbNone) at the
     * indicated position.
     *
     * @param c Row index
     * @param r Column index
     * @return Return the specific piece at the indicated position.
     */
    public int getPiece(int r, int c) {
        return (isLegalPosition(r, c)) ?
                fPieceMask & mBoard[r][c] :
                rbNone;
    }

    /**
     * given a piece, return its rank (or 0 for an unknown piece).
     * rat is 1, cat is 2, dog is 3, wolf is 4, leopard is 5, tiger is 6, lion is 7, elephant is 8.
     *
     * @param p An {@code Integer} which is a piece
     * @return Returns the rank of the given piece.
     */
    public int getRank(int p) {
        return (p <= rbNone || p > bElephant) ? rbNone :
                (p % rElephant == 0) ? rElephant : p % rElephant;
    }

    /**
     * return the rank of the piece at the specified position (or 0 for none).
     * rat is 1, cat is 2, dog is 3, wolf is 4, leopard is 5, tiger is 6, lion is 7, elephant is 8.
     *
     * @param c Row index
     * @param r Column index
     * @return Returns the rank of the piece at the given position.
     */
    public int getRank(int r, int c) {
        if (!isLegalPosition(r, c)) return rbNone;
        int p = fPieceMask & mBoard[r][c];
        if (p == rbNone) return rbNone;
        return (p % rElephant == 0) ? rElephant : p % rElephant;
    }

    /**
     * Returns what appears on the underlying board at the specified position
     *
     * @param r Row index
     * @param c Column index
     * @return Returns the cell type at the specified position
     */
    public int getBoard(int r, int c) {
        return (isLegalPosition(r, c)) ?
                fBoardMask & mBoard[r][c] :
                cNone;
    }

    /**
     * Returns the value of {@link Color} of the piece at the specified location.
     *
     * @param r Row index
     * @param c Column index
     * @return Returns the {@link Color} of the piece (or Color.None) at the specified location.
     * @see Color
     */
    public Color getColor(int r, int c) {
        if (!isLegalPosition(r, c))
            return Color.None;
        // get piece first
        int p = fPieceMask & mBoard[r][c];
        return (p == rbNone) ? Color.None :
                (p - rElephant > rbNone) ? Color.Black : Color.Red;
    }

    /**
     * Test whether it's an empty cell at the specified position.
     *
     * @param r Row index
     * @param c Column index
     * @return Returns {@code true} if the position has no piece on it; {@code false} otherwise or if out of bounds.
     */
    public boolean isEmpty(int r, int c) {
        return isLegalPosition(r, c) &&
                rbNone == (fPieceMask & mBoard[r][c]);
    }

    /**
     * Returns a string representing the board that can be pretty-printed.
     * It should look something like the following:
     * <pre>
     *     --...-        --...-     \n
     *    |      |      |      |    \n
     *    .      .      .      .     .
     *    .      .      .      .     .
     *    .      .      .      .     .
     *    |      |      |      |    \n
     *     --...-        --...-     \n
     * </pre>
     * The left side of the string should be the underlying board.
     * The right side should be the pieces at their specific locations.
     * Put the first 3 characters of the name at each location
     * (e.g., rLi for the red lion, and bEl for the black elephant).
     * If you have a better idea, please let me know!
     *
     * @return A {@link String} which represents the current board configuration.
     */
    @Override
    public String toString() {
        String verticalGap = "          ";
        String[] cells = getPrintableBoardCells();
        String[] pieces = getPrintablePieceConfig();
        StringBuffer sb = new StringBuffer();
        for (int r = 0; r < fRows; r++)
            sb.append(String.format("%1$s%2$s%3$s%n", cells[r], verticalGap, pieces[r]));
        return sb.toString() + "\n" + "hash: " + String.valueOf(hashCode());
    }

    /**
     * Get the number of black pieces remaining on the board.
     *
     * @return An integer number which represents the number of black pieces on the board.
     */
    public int countBlack() {
        int count = 0;
        for (int r = 0; r < fRows; r++)
            for (int c = 0; c < fCols; c++)
                count += (getColor(r, c) == Color.Black) ? 1 : 0;
        return count;
    }

    /**
     * Get the number of red pieces remaining on the board.
     *
     * @return An integer number which represents the number of red pieces on the board.
     */
    public int countRed() {
        int count = 0;
        for (int r = 0; r < fRows; r++)
            for (int c = 0; c < fCols; c++)
                count += (getColor(r, c) == Color.Red) ? 1 : 0;
        return count;
    }

    /**
     * Test whether Red is a winner (regardless of whose turn it is)
     *
     * @return Return {@code true} if red wins, false otherwise
     */
    public boolean isRedWinner() {
        return getColor(fRows - 1, getBoardColumnMedian()) == Color.Red;
    }

    /**
     * Test whether Black is a winner (regardless of whose turn it is)
     *
     * @return Return {@code true} if red wins, false otherwise
     */
    public boolean isBlackWinner() {
        return getColor(0, getBoardColumnMedian()) == Color.Black;
    }

    /**
     * Create and initialize an instance of class {@code Board}
     * by copying another instance of class {@code Board}.
     *
     * @param other Another {@code Board} instance to copy
     */
    public Board(final Board other) {
        if (other == null)
            throw new IllegalArgumentException("The given source Board object is null");
        // a copy constructor should do the following:
        // 1. copy board configuration
        // 2. copy the current turn
        for (int r = 0; r < fRows; r++)
            System.arraycopy(other.mBoard[r], 0, mBoard[r], 0, fCols);
        mBlacksTurn = other.mBlacksTurn;
    }

    /**
     * Test whether two {@code Board} instances have identical configurations.
     *
     * @param other The {@code Board} instance to compare with
     * @return Return {@code true} if identical, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        // comparison of hashcodes is not enough because the board configurations
        // can be different while the hashcodes are identical.
        // So have to do comparison between 2 boards and current turns
        return (other == this) || other != null && other.getClass() == getClass() &&
                ((Board)other).mBlacksTurn == mBlacksTurn &&
                equalsBoard((Board)other);
    }

    /**
     * Compare two {@code Board} instances whether they have identical board contents.
     *
     * @param other The {@code Board} instance to compare with
     * @return Return {@code true} if identical, false otherwise.
     */
    public boolean equalsBoard(Board other) {
        return Arrays.deepEquals(other.mBoard, mBoard);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // from "Effective Java" by J. Bloch:
        // "Item 9: Always override hashCode when you override equals"
        //
        // use the string algorithm from
        // https://en.wikipedia.org/wiki/Java_hashCode%28%29#The_java.lang.String_hash_function.
        // simply treat the individual board values (i.e., mBoard[r][c]) as the
        // individual character values.  (DO NOT use charAt on the string returned
        // from toString for this function.)  also include the value of mBlacksTurn
        // as described below as the last (i.e., nth) character.
        //
        //include individual board values
        int hash = 0, i = 0;
        for (int r = 0; r < Board.fRows; r++) {
            for (int c = 0; c < Board.fCols; c++) {
                // hash += (int)Math.pow(31, fRows * fCols - 1 - i) * mBoard[r][c];
                // Math.pow() always gets wrong answer because of the number is too big
                int power = 1;
                if (i > 0) {
                    for (int p = 1; p <= fRows * fCols - 1 - i; p++)
                        power *= 31;
                }
                hash += power * mBoard[r][c];
                i++;
            }
        }
//        for (int r = 0; r < Board.fRows; r++)
//            for (int c = 0; c < Board.fCols; c++)
//                hash = hash * 31 + mBoard[r][c];

        //include mBlacksTurn as the last character value by using a value of 1 if it
        // is true, and a value of 2 if it is false.
        return hash + ((mBlacksTurn) ? 1 : 2);
    }

    /**
     * Get the median row number of the board definition.
     *
     * @return An {@link Integer} which is the median number of row count.
     */
    static int getBoardRowMedian() {
        return getMedian(fRows);
    }

    /**
     * Get the median row number of the board definition.
     *
     * @return An {@link Integer} which is the median number of column count.
     */
    static int getBoardColumnMedian() {
        return getMedian(fCols);
    }

    /**
     * Gets the median number.
     * <pre>
     *     Example:
     *     <code>getBoardMedian(8) returns 3</code>
     *     <code>getBoardMedian(9) returns 4</code>
     *     <code>getBoardMedian(10) returns 4</code>
     *     <code>getBoardMedian(11) returns 5</code>
     * </pre>
     *
     * @param count An integer which is a countf a range whose lower bound is 0.
     * @return An integer which is the median number.
     */
    private static int getMedian(int count) {
        return (count <= 0 || count == 1) ? 0 :
                (count % 2 == 0) ? (count - 1) >> 1 : count >> 1;
    }

    /**
     * Place a piece at an empty board cell.
     * Note: This method is for internal use merely.
     * It doesn't do check against game rules.<br/>
     * If game rules are considered, {@link #doMove(int, int, int, int)}
     * should be used instead.
     *
     * @param r The row number of the cell.
     * @param c The column number of the cell.
     * @param p The piece to be placed.
     */
    private void setPiece(int r, int c, int p) {
        mBoard[r][c] |= p;
    }

    /**
     * Removes a piece at the specified cell
     *
     * @param r Row index of the position
     * @param c Column index of the position
     */
    private void removePiece(int r, int c) {
        mBoard[r][c] &= fBoardMask;
    }

    /**
     * Check against rules whether one piece can take another out.
     * <pre>
     *     Note:
     *         1. Both {@code attacker} and {@code attacked} values would be modified inside.
     *         2. This method simply compares the revised values of attacker and attacked.
     *         3. The 2 values are revised according to the types of cells where the 2 pieces are,
     *         with regards to the rules.
     *         4. This method does not check whether the 2 pieces' values are valid or not.
     *         5. Invokers have to validate whether the 2 pieces' values are valid.
     * </pre>
     *
     * @param fr The row index of the piece's current position.
     * @param fc The column index of the piece's current position.
     * @param tr The row index of the destination position.
     * @param tc The column index of the destination position.
     * @return Returns {@code true} if {@code attacker} wins; {@code false} otherwise.
     */
    private boolean canCapture(int fr, int fc, int tr, int tc) {
        int attacker = getPiece(fr, fc);
        int attacked = getPiece(tr, tc);
        Color attackerColor = getColor(fr, fc);
        Color attackedColor = getColor(tr, tc);
        int attackerCell = getBoard(fr, fc);
        int attackedCell = getBoard(tr, tc);

        // general: neither of the pieces can be rbNone.
        // this is tested by the invoker
        // if (attacker == rbNone || attacked == rbNone) return false;
        // rule 0: same color, no attack
        if (attackerColor == attackedColor) return false;

        // -------- set both values into range rat...elephant --------- //
        attacker = (attacker % rElephant == 0) ? rElephant : attacker % rElephant;
        attacked = (attacked % rElephant == 0) ? rElephant : attacked % rElephant;

        // rule 1: only rat can attack from anywhere, just simply do value comparison
        attacker = (attackerCell == cWater && attacker != rRat) ? rbNone : attacker;
        // rule 1-1: but, a rat in a water cell cannot attack an elephant
        attacker = (attackerCell == cWater && attacker == rRat && attacked == rElephant) ?
                rbNone : attacker;
        // rule 2: none except a rat, can attack a piece in a water cell
        attacker = (attacker != rRat && attackedCell == cWater) ? rbNone : attacker;
        // rule 3: a piece can be attacked in a trap regardless of its power
        attacked = (attackedCell == cRTrap || attackedCell == cBTrap) ? rbNone : attacked;
        // rule 4: a rat can attack an elephant but an elephant cannot attack a rat
        attacker = (attacker == rRat && attacked == rElephant) ? rElephant :
                (attacker == rElephant && attacked == rRat) ? rbNone : attacker;
        // rule 5: lion and tiger cannot attack a piece at the other side of river, only when a rat is in the river
        //         --- don't care what color of that rat is in
        attacker = ((attacker == rLion || attacker == rTiger) && !noPieceStandingInWater(fr, fc, tr, tc)) ?
                rbNone : attacker;
        // comparing 2 ranks
        return attacker - attacked >= 0;
    }

    /**
     * Test if a piece can move to the destination cell as it wishes.
     * The destination cell must be empty.
     *
     * @param fr Row index of the cell where the piece stands
     * @param fc Column index of the cell where the piece stands
     * @param tr Row index of the destination cell
     * @param tc Column index of the destination cell
     * @return Returns {@code true} if the move is valid; {@code false} otherwise.
     */
    private boolean canMove(int fr, int fc, int tr, int tc) {
        // set piece into range rat... elephant
        int piece = getPiece(fr, fc);
        piece = (piece % rElephant == 0) ? rElephant : piece % rElephant;

        // get board cell type first
        int src = getBoard(fr, fc);
        Color srcColor = getColor(fr, fc);
        int dst = getBoard(tr, tc);
        int rowDist = abs(fr - tr);
        int colDist = abs(fc - tc);

        // rule 0: no diagonal move
        if (fr != tr && fc != tc) return false;
        // rule 1: no piece can move 2 cells further away except tiger & lion
        if (piece != rTiger && piece != rLion && (rowDist > 1 || colDist > 1))
            return false;
        // rule 2: tiger or lion jump from one river bank to the other
        if ((rowDist > 1 || colDist > 1) && (piece == rTiger || piece == rLion)) {
            // only one bank to another
            if (!isRiverBank(fr, fc) || !isRiverBank(tr, tc)) return false;
            // only one river at a time
            if (!onlyWaterInBetween(fr, fc, tr, tc)) return false;
            // no other piece standing in the river
            if (!noPieceStandingInWater(fr, fc, tr, tc)) return false;
        }
        // rule 3: only rat can enter a water cell
        if (dst == cWater && piece != rRat) return false;
        // rule 4: none stands in a DEN when they are in the same color
        return !(dst == cBDen && srcColor == Color.Black || dst == cRDen && srcColor == Color.Red);
    }

    /**
     * Test whether a board cell is adjacent to a water cell
     *
     * @param r Row index
     * @param c Column index
     * @return Return {@code true} if the cell is adjacent to a water cell; return {@code false} otherwise;
     */
    private boolean isRiverBank(int r, int c) {
        return (getBoard(r - 1, c) == cWater || getBoard(r + 1, c) == cWater || getBoard(r, c - 1) == cWater || getBoard
                (r, c + 1) == cWater) && getBoard(r, c) != cWater;
    }

    /**
     * Test whether a piece is actually placed at the same position where it is at.
     *
     * @param fr Row index of the cell where the piece stands
     * @param fc Column index of the cell where the piece stands
     * @param tr Row index of the destination cell
     * @param tc Column index of the destination cell
     * @return Returns {@code true} if the move is valid; {@code false} otherwise.
     */
    private boolean isPositionNotChanged(int fr, int fc, int tr, int tc) {
        return fr == tr && fc == tc;
    }

    /**
     * Test if only water cells are in between 2 positions
     *
     * @param fr Row index of the cell where the piece stands
     * @param fc Column index of the cell where the piece stands
     * @param tr Row index of the destination cell
     * @param tc Column index of the destination cell
     * @return Returns {@code true} if the move is valid; {@code false} otherwise.
     */
    private boolean onlyWaterInBetween(int fr, int fc, int tr, int tc) {
        int startCol = ((tc > fc) ? fc : tc) + 1;
        int endCol = ((tc > fc) ? tc : fc) - 1;
        int startRow = ((tr > fr) ? fr : tr) + 1;
        int endRow = ((tr > fr) ? tr : fr) - 1;
        boolean checkRow = fr == tr;
        boolean checkCol = fc == tc;

        if (!checkCol && !checkRow) return false;

        // check rows
        if (checkRow) {
            for (int c = startCol; c <= endCol; c++) {
                checkRow = checkRow && (getBoard(fr, c) == cWater);
            }
            return checkRow;
        }
        // check columns
        if (checkCol) {
            for (int r = startRow; r <= endRow; r++) {
                checkCol = checkCol && (getBoard(r, fc) == cWater);
            }
            return checkCol;
        }

        return false;
    }

    /**
     * Test whether there is a piece in a water cell in the way from (fr, fc) to (tr, tc)
     *
     * @param fr Row index of the cell where the piece stands
     * @param fc Column index of the cell where the piece stands
     * @param tr Row index of the destination cell
     * @param tc Column index of the destination cell
     * @return Returns {@code true} if the move is valid; {@code false} otherwise.
     */
    private boolean noPieceStandingInWater(int fr, int fc, int tr, int tc) {
        int startCol = ((tc > fc) ? fc : tc) + 1;
        int endCol = ((tc > fc) ? tc : fc) - 1;
        int startRow = ((tr > fr) ? fr : tr) + 1;
        int endRow = ((tr > fr) ? tr : fr) - 1;
        boolean checkRow = fr == tr;
        boolean checkCol = fc == tc;

        if (!checkCol && !checkRow) return false;

        // check rows
        if (checkRow) {
            for (int c = startCol; c <= endCol; c++) {
                checkRow = checkRow && (getPiece(fr, c) == rbNone);
            }
            return checkRow;
        }
        // check columns
        if (checkCol) {
            for (int r = startRow; r <= endRow; r++) {
                checkCol = checkCol && (getPiece(r, fc) == rbNone);
            }
            return checkCol;
        }

        return false;
    }

    /**
     * Test whether a position is legal on board
     *
     * @param r Row index
     * @param c Column index
     * @return Return {@code true} if the position is legal; return {@code false} otherwise;
     */
    private boolean isLegalPosition(int r, int c) {
        return (r >= 0 && r < fRows && c >= 0 && c < fCols);
    }

    /**
     * Get a cell's underling board's String form.
     *
     * @param r The row number of the board cell.
     * @param c The column number of the board cell.
     * @return A {@link String} which represents the underling board.
     */
    private String getBoardType(int r, int c) {
        switch (getBoard(r, c)) {
            case cWater:
                return "cWa";
            case cGround:
                return "cGr";
            case cBDen:
                return "cBd";
            case cRDen:
                return "cRd";
            case cRTrap:
                return "cRt";
            case cBTrap:
                return "cBt";
            default:
                return "cNo";
        }
    }

    /**
     * Get a piece's String form.
     *
     * @param r The row number of the cell where the piece is.
     * @param c The column number of the cell where the piece is.
     * @return A {@link String} which represents the piece.
     */
    private String getPieceType(int r, int c) {
        switch (getPiece(r, c)) {
            case rCat:
                return "rCa";
            case rDog:
                return "rDo";
            case rElephant:
                return "rEl";
            case rLeopard:
                return "rLe";
            case rLion:
                return "rLi";
            case rRat:
                return "rRa";
            case rTiger:
                return "rTi";
            case rWolf:
                return "rWo";
            case bCat:
                return "bCa";
            case bDog:
                return "bDo";
            case bElephant:
                return "bEl";
            case bLeopard:
                return "bLe";
            case bLion:
                return "bLi";
            case bRat:
                return "bRa";
            case bTiger:
                return "bTi";
            case bWolf:
                return "bWo";
            default:
                return "";
        }
    }

    /**
     * Get a {@link String} array of the board's cells (not including pieces) for print or log purpose.
     *
     * @return An array of {@code String} of all cells.
     */
    private String[] getPrintableBoardCells() {
        String placeHolder = " . ";
        String[] rows = new String[fRows];
        String[] row = new String[fCols];
        for (int r = 0; r < fRows; r++) {
            for (int c = 0; c < fCols; c++)
                row[c] = getBoardType(r, c);
            rows[r] = String.join(placeHolder, row);
        }
        // help GC
        row = null;

        return rows;
    }

    /**
     * Get a {@link String} array of the current board piece configuration for print or log
     * purpose.
     *
     * @return An array of {@code String} of all pieces.
     */
    private String[] getPrintablePieceConfig() {
        String placeHolder = " . ";
        String[] rows = new String[fRows];
        String[] row = new String[fCols];
        for (int r = 0; r < fRows; r++) {
            for (int c = 0; c < fCols; c++)
                row[c] = getPieceType(r, c);
            rows[r] = String.join(placeHolder, row);
        }
        // help GC
        row = null;

        return rows;
    }

    /**
     * Build an array of {@code String} of piece names as
     * a textual output table's headers in order of piece values;
     *
     * @return An array of {@code String} which represents the piece names.
     */
    static String[] buildPieceLogHeader() {
        String[] header = new String[8];
        header[Board.rRat - 1] = "rat";
        header[Board.rCat - 1] = "cat";
        header[Board.rDog - 1] = "dog";
        header[Board.rLion - 1] = "lion";
        header[Board.rElephant - 1] = "elephant";
        header[Board.rWolf - 1] = "wolf";
        header[Board.rTiger - 1] = "tiger";
        header[Board.rLeopard - 1] = "leopard";

        return header;
    }

    /**
     * Build an array of {@code String} of cell types as
     * a textual output table's headers in order of types values;
     *
     * @return An array of {@code String} which represents the cell type names.
     */
    static String[] buildCellHeader() {
        String[] header = new String[6];
        header[(Board.cGround - 1) >> 5] = "ground";
        header[(Board.cWater - 1) >> 5] = "water";
        header[(Board.cBTrap - 1) >> 5] = "bTrap";
        header[(Board.cRTrap - 1) >> 5] = "rTrap";
        header[(Board.cRDen - 1) >> 5] = "rDen";
        header[(Board.cBDen - 1) >> 5] = "bDen";

        return header;
    }


}  //end class Board

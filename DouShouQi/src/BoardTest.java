//------------------------------------------------------------------------------
//  File       : BoardTest.java
//  Course     : CSC610
//  Date:      : 09/09/2016
//  Author     : Jason Qiao Meng
//  Author ID  : 10652564
//  Description: This file contains unit tests against Board.java
//------------------------------------------------------------------------------

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;


/**
 * A unit test class against class {@link Board}
 */
public class BoardTest {

    /**
     * Set up the test environment for each single test method.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        System.out.println("Creating and initializing a board instance");
        board = new Board();
    }


    /**
     * Tear down the test environment for each single test method.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        System.out.println("Finalizing Board Test");
        board = null;
    }


    /**
     * Test whether the Board's constructor correctly initialized the RED pieces.
     */
    @Test
    public void testBoardInitializedForRed() {
        System.out.println("Test whether the board is initialized correctly for RED");

        Boolean[] results = new Boolean[8];
        results[Board.rLeopard - 1] =
                board.mBoard[2][2] == (Board.cGround | Board.rLeopard);
        results[Board.rLion - 1] =
                board.mBoard[0][0] == (Board.cGround | Board.rLion);
        results[Board.rWolf - 1] =
                board.mBoard[1][1] == (Board.cGround | Board.rDog);
        results[Board.rTiger - 1] =
                board.mBoard[0][Board.fCols - 1] == (Board.cGround | Board.rTiger);
        results[Board.rCat - 1] =
                board.mBoard[1][Board.fCols - 2] == (Board.cGround | Board.rCat);
        results[Board.rRat - 1] =
                board.mBoard[2][0] == (Board.cGround | Board.rRat);
        results[Board.rDog - 1] =
                board.mBoard[2][Board.fCols - 3] == (Board.cGround | Board.rWolf);
        results[Board.rElephant - 1] =
                board.mBoard[2][Board.fCols - 1] == (Board.cGround | Board.rElephant);

        // console/log output
        // set the min column width to 5 because len('false') == 5
        String header = formatTableHeader(Board.buildPieceLogHeader(), 8);
        System.out.println(header);
        System.out.println(getRowSeparator('-', header.length()));
        System.out.println(formatTableRow(results, 8));

        // test result
        boolean actual = true;
        for (Boolean v : results) actual = actual && v;
        String msg = "All pieces are initialized correctly? Expected: %1$b, Actual: %2$b";
        msg = String.format(msg, true, actual);
        assertEquals(msg, true, actual);
    }


    /**
     * Test whether the Board's constructor correctly initialized the BLACK pieces.
     */
    @Test
    public void testBoardInitializedForBlack() {
        System.out.println("Test whether the board is initialized correctly for BLACK");

        Boolean[] results = new Boolean[8];
        results[Board.bElephant - Board.rElephant - 1] =
                board.mBoard[Board.fRows - 3][0] == (Board.cGround | Board.bElephant);
        results[Board.bCat - Board.rElephant - 1] =
                board.mBoard[Board.fRows - 2][1] == (Board.cGround | Board.bCat);
        results[Board.bDog - Board.rElephant - 1] =
                board.mBoard[Board.fRows - 3][2] == (Board.cGround | Board.bWolf);
        results[Board.bLion - Board.rElephant - 1] =
                board.mBoard[Board.fRows - 1][Board.fCols - 1] == (Board.cGround | Board.bLion);
        results[Board.bTiger - Board.rElephant - 1] =
                board.mBoard[Board.fRows - 1][0] == (Board.cGround | Board.bTiger);
        results[Board.bWolf - Board.rElephant - 1] =
                board.mBoard[Board.fRows - 2][Board.fCols - 2] == (Board.cGround | Board.bDog);
        results[Board.bRat - Board.rElephant - 1] =
                board.mBoard[Board.fRows - 3][Board.fCols - 1] == (Board.cGround | Board.bRat);
        results[Board.bLeopard - Board.rElephant - 1] =
                board.mBoard[Board.fRows - 3][Board.fCols - 3] == (Board.cGround | Board.bLeopard);

        // console/log output
        // set the min column width to 8 because len(elephant) == 8
        String header = formatTableHeader(Board.buildPieceLogHeader(), 8);
        System.out.println(header);
        System.out.println(getRowSeparator('-', header.length()));
        System.out.println(formatTableRow(results, 8));

        // test result
        boolean actual = true;
        for (Boolean v : results) actual = actual && v;
        String msg = "All pieces are initialized correctly? Expected: %1$b, Actual: %2$b";
        msg = String.format(msg, true, actual);
        assertEquals(msg, true, actual);
    }


    /**
     * Test whether the Board's constructor correctly initialized the cells underneath.
     */
    @Test
    public void testBoardWaterCellInitialized() {
        System.out.println("Test whether the Board's constructor correctly initialized the water cells.");

        int rowMed = Board.getBoardRowMedian();
        int colMed = Board.getBoardColumnMedian();
        int waterRowStart = rowMed - 1;
        int waterRowEnd = rowMed + 1;
        int waterColStart1 = 1;
        int waterColStart2 = colMed + 1;
        int waterColEnd1 = colMed - 1;
        int waterColEnd2 = Board.fCols - 2;

        boolean[] region1 = new boolean[
                (waterRowEnd - waterRowStart + 1) * (waterColEnd1 - waterColStart1 + 1)];
        boolean[] region2 = new boolean[
                (waterRowEnd - waterRowStart + 1) * (waterColEnd2 - waterColStart2 + 1)];

        for (int row = waterRowStart; row <= waterRowEnd; row++) {
            // test region 1
            for (int col = waterColStart1; col <= waterColEnd1; col++)
                region1[(row - waterRowStart) * (waterColEnd1 - waterColStart1 + 1) + col - waterColStart1] =
                        (board.mBoard[row][col] & Board.fBoardMask) == Board.cWater;
            // test region 2
            for (int col = waterColStart2; col <= waterColEnd2; col++)
                region2[(row - waterRowStart) * (waterColEnd2 - waterColStart2 + 1) + col - waterColStart2] =
                        (board.mBoard[row][col] & Board.fBoardMask) == Board.cWater;
        }

        boolean region1Actual = true;
        boolean region2Actual = true;
        for (boolean v : region1) region1Actual &= v;
        for (boolean v : region2) region2Actual &= v;
        boolean actual = region1Actual & region2Actual;

        // console/log output
        String header = formatTableHeader(new String[]{"Region 1", "Region 2"}, 10);
        System.out.println(header);
        System.out.println(getRowSeparator('-', header.length()));
        System.out.println(formatTableRow(new Boolean[]{region1Actual, region2Actual}, 10));

        // test result
        String msg = "All water cell are initialized correctly? Expected: %1$b, Actual: %2$b";
        msg = String.format(msg, true, actual);
        assertEquals(msg, true, actual);
    }


    /**
     * Test whether the Board's constructor correctly initialized the cells underneath.
     */
    @Test
    public void testBoardTrapCellsInitialized() {
        System.out.println("Test whether the Board's constructor correctly initialized the trap cells.");
        int colMed = Board.getBoardColumnMedian();
        int rRow = 0;
        int bRow = Board.fRows - 1;
        int colStart = colMed - 1;
        int colEnd = colMed + 1;

        boolean[] red = new boolean[3];
        boolean[] blk = new boolean[3];

        for (int c = colStart; c <= colEnd; c++) {
            red[c - colStart] = (c == colMed) ?
                    (board.mBoard[rRow + 1][c] & Board.fBoardMask) == Board.cRTrap :
                    (board.mBoard[rRow][c] & Board.fBoardMask) == Board.cRTrap;
            blk[c - colStart] = (c == colMed) ?
                    (board.mBoard[bRow - 1][c] & Board.fBoardMask) == Board.cBTrap :
                    (board.mBoard[bRow][c] & Board.fBoardMask) == Board.cBTrap;
        }

        boolean rActual = true;
        boolean bActual = true;
        for (boolean v : red) rActual &= v;
        for (boolean v : blk) bActual &= v;
        boolean actual = rActual & bActual;

        // console/log output
        String header = formatTableHeader(new String[]{"Red Traps", "Blk Traps"}, 10);
        System.out.println(header);
        System.out.println(getRowSeparator('-', header.length()));
        System.out.println(formatTableRow(new Boolean[]{rActual, bActual}, 10));

        // test result
        String msg = "All trap cells are initialized correctly? Expected: %1$b, Actual: %2$b";
        msg = String.format(msg, true, actual);
        assertEquals(msg, true, actual);
    }


    /**
     * Test whether the Board's constructor correctly initialized the cells underneath.
     */
    @Test
    public void testBoardDenCellsInitialized() {
        System.out.println("Test whether the Board's constructor correctly initialized the den cells.");

        int colMed = Board.getBoardColumnMedian();
        int rRow = 0;
        int bRow = Board.fRows - 1;

        boolean red = (board.mBoard[rRow][colMed] & Board.fBoardMask) == Board.cRDen;
        boolean blk = (board.mBoard[bRow][colMed] & Board.fBoardMask) == Board.cBDen;

        // console/log output
        String header = formatTableHeader(new String[]{"Red Den", "Blk Den"}, 10);
        System.out.println(header);
        System.out.println(getRowSeparator('-', header.length()));
        System.out.println(formatTableRow(new Boolean[]{red, blk}, 10));

        // test result
        String msg = "All trap cells are initialized correctly? Expected: %1$b, Actual: %2$b";
        msg = String.format(msg, true, red & blk);
        assertEquals(msg, true, red & blk);
    }


    /**
     * Test getPiece(r, c) at a random legal board cell.
     */
    @Test
    public void testGetPieceAtRandomPosition() {
        System.out.println("Testing get piece at a random cell.");

        int[] pos = getRandomNotEmptyCellPosition();
        System.out.println(String.format("Found a cell with a piece at (%1$d, %2$d).", pos[0], pos[1]));
        System.out.println(String.format("Get the piece at (%1$d, %2$d).", pos[0], pos[1]));
        int piece = board.getPiece(pos[0], pos[1]);
        int desired = board.mBoard[pos[0]][pos[1]] & Board.fPieceMask;
        String msg = "getPiece() gets the desired piece? Expected: %1$d, Actual: %2$d";
        msg = String.format(msg, desired, piece);
        assertEquals(msg, desired, piece);
    }


    /**
     * Test getPiece(r, c) at an illegal board cell.
     */
    @Test
    public void testGetPieceAtIllegalPosition() {
        System.out.println("Testing get piece at an illegal cell.");
        int[] pos = getIllegalCellPosition();
        System.out.println(String.format("Get the piece at (%1$d, %2$d).", pos[0], pos[1]));
        int piece = board.getPiece(pos[0], pos[1]);
        int desired = Board.rbNone;
        String msg = "rbNone is returned? Expected: %1$d, Actual: %2$d";
        msg = String.format(msg, desired, piece);
        assertEquals(msg, desired, piece);
    }


    /**
     * Testing get board cell value at a random cell which has a piece.
     */
    @Test
    public void testGetBoardCellAtRandomPositionWithPiece() {
        System.out.println("Testing get board cell value at a random cell which has a piece.");

        int[] pos = getRandomNotEmptyCellPosition();
        System.out.println(String.format("Found a cell with a piece at (%1$d, %2$d).", pos[0], pos[1]));
        System.out.println(String.format("Get the board cell at (%1$d, %2$d).", pos[0], pos[1]));
        int cell = board.getBoard(pos[0], pos[1]);
        int desired = board.mBoard[pos[0]][pos[1]] & Board.fBoardMask;
        String msg = "getPiece() gets the desired cell? Expected: %1$d, Actual: %2$d";
        msg = String.format(msg, desired, cell);
        assertEquals(msg, desired, cell);
    }


    /**
     * Testing get board cell value at a random cell which has a piece.
     */
    @Test
    public void testGetBoardCellAtRandomEmptyPosition() {
        System.out.println("Testing get board cell value at a random cell which has no piece.");

        int[] pos = getRandomEmptyCellPosition();

        System.out.println(String.format("Found an empty cell at (%1$d, %2$d).", pos[0], pos[1]));
        System.out.println(String.format("Get the board at (%1$d, %2$d).", pos[0], pos[1]));
        int cell = board.getBoard(pos[0], pos[1]);
        int desired = board.mBoard[pos[0]][pos[1]] & Board.fBoardMask;
        String msg = "getPiece() gets the desired cell? Expected: %1$d, Actual: %2$d";
        msg = String.format(msg, desired, cell);
        assertEquals(msg, desired, cell);
    }


    /**
     * Testing get board cell at an illegal cell
     */
    @Test
    public void testGetBoardCellAtIllegalPosition() {
        System.out.println("Testing get board cell at an illegal cell.");

        int[] pos = getIllegalCellPosition();
        System.out.println(String.format("Get the board cell at (%1$d, %2$d).", pos[0], pos[1]));
        int cell = board.getBoard(pos[0], pos[1]);
        int desired = Board.cNone;
        String msg = "cNone is returned? Expected: %1$d, Actual: %2$d";
        msg = String.format(msg, desired, cell);
        assertEquals(msg, desired, cell);
    }


    /**
     * Test board configuration's String representative. This test always passes
     */
    @Test
    public void testToString() {
        System.out.println("Test board configuration's String representative. This test always passes.");
        System.out.println("It outputs the result of toString() only.");
        System.out.print(board.toString());
        assertTrue("Test always passes.", true);
    }


    /**
     * Test isEmpty() at a random cell which has a piece
     */
    @Test
    public void testIsEmptyAtNotEmptyCell() {
        System.out.println("Test isEmpty() at a random cell which has a piece.");

        int[] pos = getRandomNotEmptyCellPosition();
        System.out.println(String.format("Found a cell with a piece at (%1$d, %2$d).", pos[0], pos[1]));
        boolean notEmpty = board.isEmpty(pos[0], pos[1]);
        boolean desired = false;
        String msg = "isEmpty()? Expected: %1$b, Actual: %2$b";
        msg = String.format(msg, desired, notEmpty);
        assertEquals(msg, desired, notEmpty);

    }


    /**
     * Test isEmpty() at a random cell which is empty
     */
    @Test
    public void testIsEmptyAtEmptyCell() {
        System.out.println("Test isEmpty() at a random cell which is empty");

        int[] pos = getRandomEmptyCellPosition();
        System.out.println(String.format("Found an empty cell at (%1$d, %2$d).", pos[0], pos[1]));
        boolean empty = board.isEmpty(pos[0], pos[1]);
        boolean desired = true;
        String msg = "isEmpty()? Expected: %1$b, Actual: %2$b";
        msg = String.format(msg, desired, empty);
        assertEquals(msg, desired, empty);
    }


    /**
     * Test isEmpty() at an illegal cell
     */
    @Test
    public void testIsEmptyAtIllegalCell() {
        System.out.println("Test isEmpty() at an illegal cell");

        int[] pos = getIllegalCellPosition();
        boolean notEmpty = board.isEmpty(pos[0], pos[1]);
        boolean desired = false;
        String msg = "isEmpty()? Expected: %1$b, Actual: %2$b";
        msg = String.format(msg, desired, notEmpty);
        assertEquals(msg, desired, notEmpty);
    }


    /**
     * Test getRank() by given a specific Piece
     */
    @Test
    public void testGetRankBySpecificPiece() {
        System.out.println("Test getRank() by given a specific Piece");

        // pick rRat, bWolf
        System.out.println("Getting rank of a red rat");
        int rankRat = board.getRank(Board.rRat);
        System.out.println("Getting rank of a black wolf");
        int rankWolf = board.getRank(Board.bWolf);
        int expectedRat = 1;
        int expectedWolf = 4;
        String msg = "getRank() on red rat and black wolf correctly?";
        msg += " Expected: [%1$d, %2$d], Actual: [%3$d, %4$d]";
        msg = String.format(msg, rankRat, rankWolf, expectedRat, expectedWolf);
        assertArrayEquals(msg, new int[]{rankRat, rankWolf},
                new int[]{expectedRat, expectedWolf});
    }

    /**
     * Test getRank() by given a specific piece at a random cell
     */
    @Test
    public void testGetRankByRandomPiece() {
        System.out.println("Test getRank() by given a random Piece");

        System.out.println("Generating a random Piece");
        Random r = new Random(new Date().getTime());
        int piece = r.nextInt(Board.bElephant) + 1;
        System.out.printf("Generated: %d%n", piece);
        int expected = getExpectedRank(piece);
        int rank = board.getRank(piece);
        String msg = "getRank() correctly? Expected: %1$d, Actual: %2$d";
        msg = String.format(msg, expected, rank);
        assertEquals(msg, expected, rank);
    }


    /**
     * Test getRank() by given an illegal piece
     */
    @Test
    public void testGetRankByIllegalPiece() {
        System.out.println("Test getRank() by given an illegal piece");

        Random r = new Random(new Date().getTime());
        System.out.println("Generating a random illegal Piece");
        int piece = r.nextInt() + Board.bElephant + 1;
        System.out.printf("Generated: %d%n", piece);
        int rank = board.getRank(piece);
        int expected = 0;
        String msg = "getRank() correctly? Expected: %1$d, Actual: %2$d";
        msg = String.format(msg, expected, rank);
        assertEquals(msg, expected, rank);
    }


    /**
     * Test getRank() by fed a random cell position where exists a piece
     */
    @Test
    public void testGetRankByPosAtRandomCellWithPiece() {
        System.out.println("Test getRank() by fed a random cell position where exists a piece");

        int[] pos = getRandomNotEmptyCellPosition();
        System.out.println(String.format("Found a cell with a piece at (%1$d, %2$d).", pos[0], pos[1]));
        int piece = board.mBoard[pos[0]][pos[1]] & Board.fPieceMask;
        int expected = getExpectedRank(piece);
        System.out.printf("Got the expected rank %d%n", expected);
        int rank = board.getRank(pos[0], pos[1]);
        System.out.printf("Got the actual rank %d%n", rank);
        String msg = "getRank() correctly? Expected: %1$d, Actual: %2$d";
        msg = String.format(msg, expected, rank);
        assertEquals(msg, expected, rank);
    }


    /**
     * Test getRank() by fed a random cell which is empty
     */
    @Test
    public void testGetRankByPosAtRandomEmptyCell() {
        System.out.println("Test getRank() by fed a random cell which is empty");

        int[] pos = getRandomEmptyCellPosition();
        System.out.println(String.format("Found an empty cell at (%1$d, %2$d).", pos[0], pos[1]));
        int expected = getExpectedRank(Board.rbNone);
        System.out.printf("Got the expected rank %d%n", expected);
        int rank = board.getRank(pos[0], pos[1]);
        System.out.printf("Got the actual rank %d%n", rank);
        String msg = "getRank() correctly? Expected: %1$d, Actual: %2$d";
        msg = String.format(msg, expected, rank);
        assertEquals(msg, expected, rank);
    }


    /**
     * Test getRank() by fed an illegal cell position
     */
    @Test
    public void testGetRankByPosAtIllegalCell() {
        System.out.println("Test getRank() by fed an illegal cell position");

        int[] pos = getIllegalCellPosition();
        int expected = getExpectedRank(Board.rbNone);
        System.out.printf("Got the expected rank %d%n", expected);
        int rank = board.getRank(pos[0], pos[1]);
        System.out.printf("Got the actual rank %d%n", rank);
        String msg = "getRank() correctly? Expected: %1$d, Actual: %2$d";
        msg = String.format(msg, expected, rank);
        assertEquals(msg, expected, rank);
    }


    /**
     * Test getColor() by fed a random not-empty cell
     */
    @Test
    public void testGetPieceColorByPosAtRandomCellWithPiece() {
        System.out.println("Test getColor() by fed a random not-empty cell");

        int[] pos = getRandomNotEmptyCellPosition();
        System.out.println(String.format("Found a cell with a piece at (%1$d, %2$d).", pos[0], pos[1]));
        int piece = board.mBoard[pos[0]][pos[1]] & Board.fPieceMask;
        Board.Color expected = getExpectedColor(piece);
        System.out.printf("Got the expected color: %s%n", expected.toString());
        Board.Color actual = board.getColor(pos[0], pos[1]);
        System.out.printf("Got the actual color: %s%n", actual.toString());
        String msg = "getColor() correctly? Expected: %1$s, Actual: %2$s";
        msg = String.format(msg, expected.toString(), actual.toString());
        assertEquals(msg, expected, actual);
    }


    /**
     * Test getColor() by fed a random empty cell
     */
    @Test
    public void testGetPieceColorByPosAtRandomEmptyCell() {
        System.out.println("Test getColor() by fed a random empty cell");

        int[] pos = getRandomEmptyCellPosition();
        System.out.println(String.format("Found an empty cell at (%1$d, %2$d).", pos[0], pos[1]));
        Board.Color expected = getExpectedColor(Board.rbNone);
        System.out.printf("Got the expected color: %s%n", expected.toString());
        Board.Color actual = board.getColor(pos[0], pos[1]);
        System.out.printf("Got the actual color: %s%n", actual.toString());
        String msg = "getColor() correctly? Expected: %1$s, Actual: %2$s";
        msg = String.format(msg, expected.toString(), actual.toString());
        assertEquals(msg, expected, actual);
    }


    /**
     * Test getColor() by fed an illegal cell position
     */
    @Test
    public void testGetCellColorByPosAtIllegalCell() {
        System.out.println("Test getColor() by fed an illegal cell position");

        int[] pos = getIllegalCellPosition();
        Board.Color expected = getExpectedColor(Board.rbNone);
        System.out.printf("Got the expected color: %s%n", expected.toString());
        Board.Color actual = board.getColor(pos[0], pos[1]);
        System.out.printf("Got the actual color: %s%n", actual.toString());
        String msg = "getColor() correctly? Expected: %1$s, Actual: %2$s";
        msg = String.format(msg, expected.toString(), actual.toString());
        assertEquals(msg, expected, actual);
    }


    /**
     * Generate a header row for a console/log message table
     *
     * @param headers     An array of {@code String} which contains the headers.
     * @param colMinWidth An {@code Integer} for the minimum width of each column.
     * @return A {@code String} with column separators added as a table's header row.
     */
    private String formatTableHeader(String[] headers, int colMinWidth) {
        String line = "|";
        for (String header : headers) {
            String pat = "%-" + String.valueOf(colMinWidth) + "s";
            line += " " + String.format(pat, header) + " |";
        }
        return line;
    }


    /**
     * Generate a table row. This method invokes {@code toString()} method of each item.
     *
     * @param row         An array of {@code T} which contains the column items.
     * @param colMinWidth An {@code Integer} for the minimum width of each column.
     * @param <T>         A reference type of the row's items.
     * @return A {@code String} with column separators added as a table's row.
     */
    private <T> String formatTableRow(T[] row, int colMinWidth) {
        String line = "|";
        for (T col : row) {
            String field = col.toString();
            String pat = "%-" + String.valueOf(colMinWidth) + "s";
            line += " " + String.format(pat, field) + " |";
        }
        return line;
    }


    /**
     * Generate a row splitter
     *
     * @param sep    A {@code char} for the separator symbol
     * @param length An {@code Integer} for the length of the line to be generated.
     * @return A {@code String} consists of the {@code sep}
     */
    private String getRowSeparator(char sep, int length) {
        return (length <= 0) ? "" : new String(new char[length]).replace('\0', sep);
    }


    /**
     * Generate a random legal board cell position where is not empty.
     *
     * @return An array of {@code Integer} of which [0] is row index and [1] is column index.
     */
    private int[] getRandomNotEmptyCellPosition() {
        // look for random a cell with a piece
        Random r = new Random(new Date().getTime());
        int row = 0, col = 0;
        boolean found = false;
        System.out.println("Looking for a cell which has a piece");
        for (int count = 0; count < (Board.fCols * Board.fRows); count++) {
            row = r.nextInt(Board.fRows);
            col = r.nextInt(Board.fCols);
            System.out.println(String.format("Looking at (%1$d, %2$d)", row, col));
            if ((board.mBoard[row][col] & Board.fPieceMask) > 0) {
                found = true;
                break;
            }
        }
        return (found) ? new int[]{row, col} : new int[]{0, 0};
    }


    /**
     * Generate a random legal board cell position where is empty.
     *
     * @return An array of {@code Integer} of which [0] is row index and [1] is column index.
     */
    private int[] getRandomEmptyCellPosition() {
        // look for random a cell with a piece
        Random r = new Random(new Date().getTime());
        int row = 0, col = 0;
        boolean found = false;
        System.out.println("Looking for a cell which has a piece");
        for (int count = 0; count < (Board.fCols * Board.fRows); count++) {
            row = r.nextInt(Board.fRows);
            col = r.nextInt(Board.fCols);
            System.out.println(String.format("Looking at (%1$d, %2$d)", row, col));
            if ((board.mBoard[row][col] & Board.fPieceMask) == 0) {
                found = true;
                break;
            }
        }
        return (found) ? new int[]{row, col} : new int[]{0, 0};
    }


    /**
     * Generate a random illegal board cell position (outside of board region).
     *
     * @return An array of {@code Integer} of which [0] is row index and [1] is column index.
     */
    private int[] getIllegalCellPosition() {
        System.out.println("Generating an illegal cell position (out of board)");
        Random r = new Random(new Date().getTime());
        int row = r.nextInt() + Board.fRows;
        int col = r.nextInt() + Board.fCols;
        System.out.println(String.format("Generated cell position: (%1$d, %2$d)", row, col));
        return new int[]{row, col};
    }


    /**
     * Compute a piece's rank in a hardcoded way.
     *
     * @param piece The piece value whose rank is to be computed.
     * @return An {@code Integer} which stands for the rank of the piece.
     * @see Board#getRank(int)
     * @see Board#getRank(int, int)
     */
    private int getExpectedRank(int piece) {
        // use a hardcoded way to verify
        // rat is 1, cat is 2, dog is 3, wolf is 4, leopard is 5, tiger is 6,
        // lion is 7, elephant is 8.
        switch (piece) {
            case Board.rCat:
            case Board.bCat:
                return 2;
            case Board.rDog:
            case Board.bDog:
                return 3;
            case Board.rElephant:
            case Board.bElephant:
                return 8;
            case Board.rLeopard:
            case Board.bLeopard:
                return 5;
            case Board.rLion:
            case Board.bLion:
                return 7;
            case Board.rRat:
            case Board.bRat:
                return 1;
            case Board.rTiger:
            case Board.bTiger:
                return 6;
            case Board.rWolf:
            case Board.bWolf:
                return 4;
            default:
                return 0;
        }
    }


    /**
     * Compute a piece's color in a hardcoded way.
     *
     * @param piece The piece value whose color is to be computed.
     * @return A {@link Board.Color} which stands for the color of the piece.
     * @see Board.Color
     * @see Board#getColor(int, int)
     */
    private Board.Color getExpectedColor(int piece) {
        // use a hardcoded way to verify
        // rat is 1, cat is 2, dog is 3, wolf is 4, leopard is 5, tiger is 6,
        // lion is 7, elephant is 8.
        switch (piece) {
            case Board.rCat:
            case Board.rDog:
            case Board.rElephant:
            case Board.rLeopard:
            case Board.rLion:
            case Board.rRat:
            case Board.rTiger:
            case Board.rWolf:
                return Board.Color.Red;
            case Board.bCat:
            case Board.bDog:
            case Board.bElephant:
            case Board.bLeopard:
            case Board.bLion:
            case Board.bRat:
            case Board.bTiger:
            case Board.bWolf:
                return Board.Color.Black;
            default:
                return Board.Color.None;
        }
    }


    /**
     * An instance of class {@link Board}
     */
    private Board board;

}

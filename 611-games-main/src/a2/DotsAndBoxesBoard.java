package a2;

import common.Board;
import common.Tile;
import engine.TextUI;

/**
 * Dots and Boxes board implementation.
 * Extends the common Board class to provide dots and boxes functionality.
 *
 * This board manages edges between dots and box ownership.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-10-25
 */
public class DotsAndBoxesBoard extends Board {
    private int[][] horizontalEdges;  // Horizontal edges between dots
    private int[][] verticalEdges;    // Vertical edges between dots
    private int[][] boxOwners;        // Who owns each box

    // Edge states
    private static final int EMPTY = 0;
    private static final int PLAYER1 = 1;
    private static final int PLAYER2 = 2;

    /**
     * Create a new Dots and Boxes board.
     * @param rows Number of box rows
     * @param cols Number of box columns
     */
    public DotsAndBoxesBoard(int rows, int cols) {
        super(rows, cols, false);
        this.horizontalEdges = new int[rows + 1][cols];
        this.verticalEdges = new int[rows][cols + 1];
        this.boxOwners = new int[rows][cols];
        initializeBoard();
    }

    @Override
    protected void initializeBoard() {
        // Initialize tiles (boxes) on the board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new DotsAndBoxesTile(r, c);
            }
        }

        // Initialize edges and owners
        for (int r = 0; r <= rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r < rows + 1) {
                    horizontalEdges[r][c] = EMPTY;
                }
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c <= cols; c++) {
                verticalEdges[r][c] = EMPTY;
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                boxOwners[r][c] = EMPTY;
            }
        }
    }

    /**
     * Check if an edge is available.
     * @param move The move to check
     * @return true if the edge is free
     */
    public boolean isEdgeFree(DotsAndBoxesMove move) {
        if (move.isHorizontal()) {
            if (move.getRow() < 0 || move.getRow() > rows ||
                    move.getCol() < 0 || move.getCol() >= cols) {
                return false;
            }
            return horizontalEdges[move.getRow()][move.getCol()] == EMPTY;
        } else {
            if (move.getRow() < 0 || move.getRow() >= rows ||
                    move.getCol() < 0 || move.getCol() > cols) {
                return false;
            }
            return verticalEdges[move.getRow()][move.getCol()] == EMPTY;
        }
    }

    /**
     * Apply an edge placement for a player.
     * @param move The move to apply
     * @param playerNumber The player number (1 or 2)
     * @return Number of boxes completed (0, 1, or 2)
     */
    public int applyEdge(DotsAndBoxesMove move, int playerNumber) {
        if (!isEdgeFree(move)) {
            return 0;
        }

        int boxesCompleted = 0;

        if (move.isHorizontal()) {
            horizontalEdges[move.getRow()][move.getCol()] = playerNumber;

            // Check box above this edge
            if (move.getRow() > 0) {
                if (isBoxComplete(move.getRow() - 1, move.getCol())) {
                    boxOwners[move.getRow() - 1][move.getCol()] = playerNumber;
                    DotsAndBoxesTile tile = (DotsAndBoxesTile) grid[move.getRow() - 1][move.getCol()];
                    tile.setOwner(playerNumber);
                    boxesCompleted++;
                }
            }

            // Check box below this edge
            if (move.getRow() < rows) {
                if (isBoxComplete(move.getRow(), move.getCol())) {
                    boxOwners[move.getRow()][move.getCol()] = playerNumber;
                    DotsAndBoxesTile tile = (DotsAndBoxesTile) grid[move.getRow()][move.getCol()];
                    tile.setOwner(playerNumber);
                    boxesCompleted++;
                }
            }
        } else {
            verticalEdges[move.getRow()][move.getCol()] = playerNumber;

            // Check box to the left of this edge
            if (move.getCol() > 0) {
                if (isBoxComplete(move.getRow(), move.getCol() - 1)) {
                    boxOwners[move.getRow()][move.getCol() - 1] = playerNumber;
                    DotsAndBoxesTile tile = (DotsAndBoxesTile) grid[move.getRow()][move.getCol() - 1];
                    tile.setOwner(playerNumber);
                    boxesCompleted++;
                }
            }

            // Check box to the right of this edge
            if (move.getCol() < cols) {
                if (isBoxComplete(move.getRow(), move.getCol())) {
                    boxOwners[move.getRow()][move.getCol()] = playerNumber;
                    DotsAndBoxesTile tile = (DotsAndBoxesTile) grid[move.getRow()][move.getCol()];
                    tile.setOwner(playerNumber);
                    boxesCompleted++;
                }
            }
        }

        return boxesCompleted;
    }

    /**
     * Check if a box has all four edges.
     * @param row Box row
     * @param col Box column
     * @return true if box is complete
     */
    private boolean isBoxComplete(int row, int col) {
        return horizontalEdges[row][col] != EMPTY &&      // Top
                horizontalEdges[row + 1][col] != EMPTY &&  // Bottom
                verticalEdges[row][col] != EMPTY &&        // Left
                verticalEdges[row][col + 1] != EMPTY;      // Right
    }

    /**
     * Calculate score for a player.
     * @param playerNumber Player number (1 or 2)
     * @return Number of boxes owned
     */
    public int getScore(int playerNumber) {
        int score = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (boxOwners[r][c] == playerNumber) {
                    score++;
                }
            }
        }
        return score;
    }

    @Override
    public boolean isGameOver() {
        // Game is over when all boxes are claimed
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (boxOwners[r][c] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String render() {
        return render(null);
    }

    /**
     * Render the board with optional color support.
     * @param ui TextUI for color support (null for no colors)
     * @return String representation of the board
     */
    public String render(TextUI ui) {
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r <= rows; r++) {
            // Horizontal edges
            for (int c = 0; c < cols; c++) {
                sb.append("+");
                if (r < rows + 1 && horizontalEdges[r][c] != EMPTY) {
                    String edge = "---";
                    if (ui != null && ui.isColor()) {
                        edge = horizontalEdges[r][c] == PLAYER1 ? ui.red(edge) : ui.blue(edge);
                    }
                    sb.append(edge);
                } else {
                    sb.append("   ");
                }
            }
            sb.append("+\n");

            // Vertical edges and box contents
            if (r < rows) {
                for (int c = 0; c <= cols; c++) {
                    if (verticalEdges[r][c] != EMPTY) {
                        String edge = "|";
                        if (ui != null && ui.isColor()) {
                            edge = verticalEdges[r][c] == PLAYER1 ? ui.red(edge) : ui.blue(edge);
                        }
                        sb.append(edge);
                    } else {
                        sb.append(" ");
                    }

                    if (c < cols) {
                        if (boxOwners[r][c] != EMPTY) {
                            String owner = " " + boxOwners[r][c] + " ";
                            if (ui != null && ui.isColor()) {
                                owner = boxOwners[r][c] == PLAYER1 ? ui.red(owner) : ui.blue(owner);
                            }
                            sb.append(owner);
                        } else {
                            sb.append("   ");
                        }
                    }
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
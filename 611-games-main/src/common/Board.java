package common;

/**
 * Abstract base class for all game boards in the CS611 Game Suite.
 * Provides common functionality for board-based games.
 *
 * This class demonstrates proper inheritance and code reuse across different games.
 * Each game can extend this class to create their specific board implementation.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public abstract class Board {
    protected final int rows;
    protected final int cols;
    protected Tile[][] grid;  // Grid of tiles that make up the board

    /**
     * Constructor for creating a board with specified dimensions.
     * @param rows Number of rows in the board
     * @param cols Number of columns in the board
     */
    public Board(int rows, int cols) {
        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException("Board must have at least 1 row and 1 column");
        }
        this.rows = rows;
        this.cols = cols;
        this.grid = new Tile[rows][cols];
        initializeBoard();
    }

    /**
     * Initialize the board with tiles. Must be implemented by subclasses.
     */
    protected abstract void initializeBoard();

    /**
     * Check if a position is valid within the board boundaries.
     * @param row Row position
     * @param col Column position
     * @return true if position is valid, false otherwise
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Get the tile at a specific position.
     * @param row Row position
     * @param col Column position
     * @return The tile at the specified position
     */
    public Tile getTile(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException("Invalid position: " + row + ", " + col);
        }
        return grid[row][col];
    }

    /**
     * Set a tile at a specific position.
     * @param row Row position
     * @param col Column position
     * @param tile The tile to place
     */
    public void setTile(int row, int col, Tile tile) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException("Invalid position: " + row + ", " + col);
        }
        grid[row][col] = tile;
    }

    /**
     * Get the number of rows in the board.
     * @return Number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get the number of columns in the board.
     * @return Number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Render the board as a string. Must be implemented by subclasses.
     * @return String representation of the board
     */
    public abstract String render();

    /**
     * Check if the game is in a winning state. Must be implemented by subclasses.
     * @return true if the game is won, false otherwise
     */
    public abstract boolean isGameOver();

    /**
     * Reset the board to its initial state.
     */
    public void reset() {
        initializeBoard();
    }
}
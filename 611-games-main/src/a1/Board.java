package a1;

/**
 * Represents the puzzle board for the sliding puzzle game.
 * Stores tiles in a 2D array, tracks the blank position,
 * validates moves, performs tile slides, and checks if the puzzle is solved.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class Board {
    // Configuration constants
    private static final int MIN_SIZE = 2;

    private final int rows, cols;
    private final Tile[][] grid;
    private int br, bc;  // blank row/col

    /**
     * Create a new board in solved state.
     * @param rows number of rows (minimum 2)
     * @param cols number of columns (minimum 2)
     */
    public Board(int rows, int cols) {
        if (rows < MIN_SIZE || cols < MIN_SIZE) {
            throw new IllegalArgumentException("Minimum board size is " + MIN_SIZE + "x" + MIN_SIZE);
        }
        this.rows = rows;
        this.cols = cols;
        this.grid = new Tile[rows][cols];

        // Initialize in solved order (1, 2, 3, ..., blank)
        int val = 1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Tile(val++);
            }
        }
        grid[rows - 1][cols - 1] = new Tile(0);
        br = rows - 1;
        bc = cols - 1;
    }

    public int rows() { return rows; }
    public int cols() { return cols; }
    public int get(int r, int c) { return grid[r][c].value(); }

    /**
     * Check if a tile number can slide into the blank.
     * The tile must be adjacent to the blank (not diagonal).
     */
    public boolean canSlide(int number) {
        if (number <= 0) return false; // blank or invalid

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].value() == number) {
                    // Check if adjacent to blank (horizontally or vertically)
                    return (r == br && Math.abs(c - bc) == 1) ||
                            (c == bc && Math.abs(r - br) == 1);
                }
            }
        }
        return false;
    }

    /**
     * Slide a tile into the blank position if possible.
     * @param number the tile number to slide
     * @return true if the slide was successful
     */
    public boolean slide(int number) {
        if (!canSlide(number)) return false;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].value() == number) {
                    // Swap tile with blank
                    grid[br][bc] = new Tile(number);
                    grid[r][c] = new Tile(0);
                    br = r;
                    bc = c;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the puzzle is solved.
     * Tiles should be in order 1, 2, 3, ..., with blank at the end.
     */
    public boolean isSolved() {
        int k = 1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == rows - 1 && c == cols - 1) {
                    return grid[r][c].value() == 0;
                }
                if (grid[r][c].value() != k++) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Render the puzzle in a boxed grid format.
     * Displays tiles with borders for better visualization.
     */
    public String renderBoxed() {
        int max = rows * cols - 1;
        int w = Math.max(2, Integer.toString(max).length());
        String hEdge = "+" + repeat("-", w + 2);

        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            // Top border of row
            for (int c = 0; c < cols; c++) {
                sb.append(hEdge);
            }
            sb.append("+\n");

            // Cell contents
            for (int c = 0; c < cols; c++) {
                int v = grid[r][c].value();
                String cell = (v == 0) ? repeat(" ", w) : String.format("%" + w + "d", v);
                sb.append("| ").append(cell).append(" ");
            }
            sb.append("|\n");
        }

        // Bottom border
        for (int c = 0; c < cols; c++) {
            sb.append(hEdge);
        }
        sb.append("+\n");

        return sb.toString();
    }

    /**
     * Move the blank tile in a direction (used for shuffling).
     * @param dr row delta (-1, 0, or 1)
     * @param dc column delta (-1, 0, or 1)
     * @return true if the move was successful
     */
    boolean moveBlank(int dr, int dc) {
        int nr = br + dr;
        int nc = bc + dc;

        // Check bounds
        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
            return false;
        }

        // Swap blank with adjacent tile
        int v = grid[nr][nc].value();
        grid[br][bc] = new Tile(v);
        grid[nr][nc] = new Tile(0);
        br = nr;
        bc = nc;
        return true;
    }

    /**
     * Helper method to repeat a string n times.
     * Compatible replacement for String.repeat() (Java 11+).
     */
    private static String repeat(String s, int n) {
        if (n <= 0) return "";
        StringBuilder sb = new StringBuilder(s.length() * n);
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }
}
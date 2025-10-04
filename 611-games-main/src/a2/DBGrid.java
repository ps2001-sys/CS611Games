package a2;

import engine.TextUI;

/**
 * Represents the Dots and Boxes game board state.
 * Stores edge ownership (horizontal and vertical) and box ownership.
 * Enforces the game rule: completing a box grants ownership to the player.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class DBGrid {
    // Edge ownership values
    private static final int FREE = 0;
    private static final int PLAYER1 = 1;
    private static final int PLAYER2 = 2;

    private final int rows, cols;
    private final int[][] H;     // Horizontal edges (rows+1 x cols)
    private final int[][] V;     // Vertical edges (rows x cols+1)
    private final int[][] owner; // Box ownership (rows x cols)

    /**
     * Create a new empty game board.
     * @param rows number of box rows
     * @param cols number of box columns
     */
    public DBGrid(int rows, int cols) {
        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException("Minimum board size is 1x1");
        }

        this.rows = rows;
        this.cols = cols;
        this.H = new int[rows + 1][cols];
        this.V = new int[rows][cols + 1];
        this.owner = new int[rows][cols];
    }

    public int rows() { return rows; }
    public int cols() { return cols; }

    /**
     * Check if coordinates are valid for horizontal edge.
     */
    private boolean isValidHorizontal(int r, int c) {
        return r >= 0 && r <= rows && c >= 0 && c < cols;
    }

    /**
     * Check if coordinates are valid for vertical edge.
     */
    private boolean isValidVertical(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c <= cols;
    }

    /**
     * Check if an edge is free (not yet claimed).
     */
    public boolean isEdgeFree(DBMove m) {
        if (m.dir == 'H') {
            return isValidHorizontal(m.r, m.c) && H[m.r][m.c] == FREE;
        }
        if (m.dir == 'V') {
            return isValidVertical(m.r, m.c) && V[m.r][m.c] == FREE;
        }
        return false;
    }

    /**
     * Apply an edge placement for a player.
     * Automatically assigns box ownership if the edge completes a box.
     *
     * @param m the move to apply
     * @param player the player making the move (1 or 2)
     * @return true if at least one box was completed
     */
    public boolean applyEdge(DBMove m, int player) {
        boolean completedBox = false;

        if (m.dir == 'H' && isEdgeFree(m)) {
            H[m.r][m.c] = player;

            // Check box above this horizontal edge
            if (m.r > 0 && isBoxComplete(m.r - 1, m.c)) {
                owner[m.r - 1][m.c] = player;
                completedBox = true;
            }

            // Check box below this horizontal edge
            if (m.r < rows && isBoxComplete(m.r, m.c)) {
                owner[m.r][m.c] = player;
                completedBox = true;
            }

        } else if (m.dir == 'V' && isEdgeFree(m)) {
            V[m.r][m.c] = player;

            // Check box to the left of this vertical edge
            if (m.c > 0 && isBoxComplete(m.r, m.c - 1)) {
                owner[m.r][m.c - 1] = player;
                completedBox = true;
            }

            // Check box to the right of this vertical edge
            if (m.c < cols && isBoxComplete(m.r, m.c)) {
                owner[m.r][m.c] = player;
                completedBox = true;
            }
        }

        return completedBox;
    }

    /**
     * Check if all four edges of a box are claimed.
     */
    private boolean isBoxComplete(int r, int c) {
        return H[r][c] > FREE &&       // Top edge
                H[r + 1][c] > FREE &&   // Bottom edge
                V[r][c] > FREE &&       // Left edge
                V[r][c + 1] > FREE;     // Right edge
    }

    /**
     * Calculate score for a player (number of boxes owned).
     */
    public int score(int player) {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (owner[r][c] == player) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Check if all boxes have been claimed (game over).
     */
    public boolean isFull() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (owner[r][c] == FREE) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Render the board without colors.
     */
    public String render() {
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r <= rows; r++) {
            // Horizontal edges
            for (int c = 0; c < cols; c++) {
                sb.append("+");
                sb.append(H[r][c] > FREE ? "---" : "   ");
            }
            sb.append("+\n");

            // Vertical edges and box contents
            if (r < rows) {
                for (int c = 0; c <= cols; c++) {
                    sb.append(V[r][c] > FREE ? "|" : " ");
                    if (c < cols) {
                        int o = owner[r][c];
                        sb.append(o == FREE ? "   " : " " + o + " ");
                    }
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Render the board with colors (Player 1 = red, Player 2 = blue).
     */
    public String renderColored(TextUI ui) {
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r <= rows; r++) {
            // Horizontal edges
            for (int c = 0; c < cols; c++) {
                sb.append("+");
                int who = H[r][c];
                if (who == FREE) {
                    sb.append("   ");
                } else {
                    sb.append(colorEdge(ui, who, "---"));
                }
            }
            sb.append("+\n");

            // Vertical edges and box contents
            if (r < rows) {
                for (int c = 0; c <= cols; c++) {
                    int who = V[r][c];
                    sb.append(who == FREE ? " " : colorEdge(ui, who, "|"));

                    if (c < cols) {
                        int own = owner[r][c];
                        if (own == FREE) {
                            sb.append("   ");
                        } else {
                            sb.append(" ").append(colorBox(ui, own, Integer.toString(own))).append(" ");
                        }
                    }
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Apply color to edge based on player ownership.
     */
    private String colorEdge(TextUI ui, int player, String s) {
        return (player == PLAYER1) ? ui.red(s) : ui.blue(s);
    }

    /**
     * Apply color to box based on player ownership.
     */
    private String colorBox(TextUI ui, int player, String s) {
        return (player == PLAYER1) ? ui.red(s) : ui.blue(s);
    }
}
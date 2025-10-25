package a2;

/**
 * Configuration and rules for Dots and Boxes game.
 * Defines board dimensions and game rules.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class Rules {
    private static final int MIN_SIZE = 1;

    public final int rows;
    public final int cols;
    public final boolean extraTurnOnBox = true;

    /**
     * Create rules with specified board dimensions.
     */
    public Rules(int rows, int cols) {
        if (rows < MIN_SIZE || cols < MIN_SIZE) {
            throw new IllegalArgumentException("Minimum board size is " + MIN_SIZE + "x" + MIN_SIZE);
        }
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Create standard rules with given dimensions.
     */
    public static Rules standard(int r, int c) {
        return new Rules(r, c);
    }
}
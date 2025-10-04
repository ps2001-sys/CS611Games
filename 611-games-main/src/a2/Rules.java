package a2;

/**
 * Rules/config for the game
 * For a2 we only need rows/cols and the standard
 * rule "extra turn when you complete a box"
 */
public class Rules {
    public final int rows;
    public final int cols;
    public final boolean extraTurnOnBox = true;
    //size have to > 1 * 1 or throw exception
    public Rules(int rows, int cols) {
        if (rows < 1 || cols < 1) throw new IllegalArgumentException("min 1x1");
        this.rows = rows;
        this.cols = cols;
    }
    // initialize
    public static Rules standard(int r, int c) { return new Rules(r, c); }
}

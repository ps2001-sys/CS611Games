package a2;

/**
 * Represents a single edge placement move in Dots and Boxes.
 * Immutable value object.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class DBMove {
    public final int r, c;
    public final char dir;

    /**
     * Create a new move.
     * @param r row coordinate
     * @param c column coordinate
     * @param dir direction ('H' for horizontal, 'V' for vertical)
     */
    public DBMove(int r, int c, char dir) {
        this.r = r;
        this.c = c;
        this.dir = Character.toUpperCase(dir);
    }

    @Override
    public String toString() {
        return dir + " " + r + " " + c;
    }
}
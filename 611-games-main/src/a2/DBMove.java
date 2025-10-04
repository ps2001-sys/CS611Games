package a2;

/**
 * Immutable value object representing one edge placement.
 * dir: 'H' for horizontal edge, 'V' for vertical edge; and r,c: origin coordinate of the edge as defined by the grid.
 */
public class DBMove {
    public final int r, c;
    public final char dir;

    public DBMove(int r, int c, char dir) {
        this.r = r;
        this.c = c;
        this.dir = Character.toUpperCase(dir);
    }

    @Override public String toString() { return dir + " " + r + " " + c; }
}

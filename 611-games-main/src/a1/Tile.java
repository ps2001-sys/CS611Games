package a1;

/**
 * Concrete implementation of Piece.
 * Holds a single integer value (0 = blank).
 * Provides string representation for display.
 */
public class Tile extends Piece {
    private final int v; // 0 means blank

    public Tile(int v){ this.v=v; }

    @Override public boolean isBlank(){ return v==0; }
    @Override public int value(){ return v; }

    @Override public String toString(){
        return isBlank()? " " : Integer.toString(v);
    }
}

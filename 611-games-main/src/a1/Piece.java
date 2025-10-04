package a1;

/**
 * Abstract class for puzzle pieces.
 * Can be extended for different piece types.
 * In this puzzle, implemented by Tile.
 */
public abstract class Piece {
    // Returns true if this is the blank (empty) piece
    public abstract boolean isBlank();

    // Returns the numeric value of the piece (0 means blank)
    public abstract int value();
}

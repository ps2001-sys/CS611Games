package a1;

/**
 * Abstract base class for puzzle pieces.
 * Can be extended for different piece types in future puzzle variants.
 * In the sliding puzzle, this is implemented by the Tile class.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public abstract class Piece {
    /**
     * Check if this piece is blank (empty space).
     */
    public abstract boolean isBlank();

    /**
     * Get the numeric value of this piece.
     * Returns 0 for blank pieces.
     */
    public abstract int value();
}
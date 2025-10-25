package a1;

import common.Tile;
import common.Piece;

/**
 * Tile implementation for the Sliding Puzzle game.
 * Each tile contains a numbered piece (or blank).
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-10-25
 */
public class SlidingPuzzleTile extends Tile {
    private int value;  // The number on this tile (0 for blank)

    /**
     * Create a new sliding puzzle tile.
     * @param row Row position
     * @param col Column position
     * @param value The number on this tile (0 for blank)
     */
    public SlidingPuzzleTile(int row, int col, int value) {
        super(row, col);
        this.value = value;
        // Create and set the appropriate piece
        this.piece = new SlidingPuzzlePiece(value);
    }

    /**
     * Get the numeric value of this tile.
     * @return The value (0 for blank)
     */
    public int getValue() {
        return value;
    }

    /**
     * Check if this is the blank tile.
     * @return true if blank (value == 0)
     */
    public boolean isBlank() {
        return value == 0;
    }

    @Override
    public String toString() {
        return isBlank() ? " " : Integer.toString(value);
    }

    @Override
    public boolean canAcceptPiece(Piece piece) {
        // In sliding puzzle, tiles are fixed with their pieces
        return false;
    }
}
package a1;

import common.Piece;

/**
 * Piece implementation for the Sliding Puzzle game.
 * Represents a numbered piece that can slide around the board.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class SlidingPuzzlePiece extends Piece {

    /**
     * Create a new sliding puzzle piece.
     * @param value The number on this piece (0 for blank)
     */
    public SlidingPuzzlePiece(int value) {
        super(null, value);  // No owner in sliding puzzle
    }

    @Override
    public boolean isBlank() {
        return value == 0;
    }

    @Override
    public String toString() {
        return isBlank() ? " " : Integer.toString(value);
    }

    @Override
    public String getDisplayChar() {
        return toString();
    }
}
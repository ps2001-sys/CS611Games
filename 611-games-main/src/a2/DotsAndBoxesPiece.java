package a2;

import common.Piece;

/**
 * Piece implementation for Dots and Boxes game.
 * Represents a player's ownership marker for a box.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-10-25
 */
public class DotsAndBoxesPiece extends Piece {

    /**
     * Create a new Dots and Boxes piece (ownership marker).
     * @param playerNumber The player who owns this box (1 or 2)
     */
    public DotsAndBoxesPiece(int playerNumber) {
        super("Player " + playerNumber, playerNumber);
    }

    @Override
    public boolean isBlank() {
        return false;  // Dots and Boxes pieces are never blank
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public String getDisplayChar() {
        return toString();
    }
}
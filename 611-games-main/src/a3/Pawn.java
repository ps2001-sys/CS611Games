package a3;

import common.Piece;
import common.Tile;

/**
 * Pawn piece for Quoridor game.
 * Extends the common Piece class to represent a player's pawn.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class Pawn extends Piece {
    private int playerNumber;
    private Position currentPosition;

    /**
     * Create a new pawn.
     * @param owner The player who owns this pawn
     * @param playerNumber The player's number (1-4)
     */
    public Pawn(String owner, int playerNumber) {
        super(owner, playerNumber);
        this.playerNumber = playerNumber;
    }

    /**
     * Get the player number.
     * @return Player number
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Set the current position of this pawn.
     * @param pos The new position
     */
    public void setPosition(Position pos) {
        this.currentPosition = pos;
    }

    /**
     * Get the current position of this pawn.
     * @return Current position
     */
    public Position getPosition() {
        return currentPosition;
    }

    @Override
    public boolean isBlank() {
        return false;  // Pawns are never blank
    }

    @Override
    public String toString() {
        return "P" + playerNumber;
    }

    @Override
    public String getDisplayChar() {
        return String.valueOf(playerNumber);
    }

    @Override
    public boolean canMoveTo(Tile tile) {
        if (tile == null) return false;

        // Can only move to empty tiles
        return tile.isEmpty();
    }

    /**
     * Check if this pawn has reached its goal.
     * @param boardSize Size of the board
     * @return true if at goal
     */
    public boolean hasReachedGoal(int boardSize) {
        if (currentPosition == null) return false;

        switch (playerNumber) {
            case 1: // North player - goal is south edge
                return currentPosition.row == boardSize - 1;
            case 2: // South player - goal is north edge
                return currentPosition.row == 0;
            case 3: // West player - goal is east edge
                return currentPosition.col == boardSize - 1;
            case 4: // East player - goal is west edge
                return currentPosition.col == 0;
            default:
                return false;
        }
    }
}
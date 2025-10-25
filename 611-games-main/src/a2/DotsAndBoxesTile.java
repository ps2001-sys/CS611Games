package a2;

import common.Tile;
import common.Piece;

/**
 * Tile implementation for Dots and Boxes game.
 * Each tile represents a box that can be claimed by a player.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class DotsAndBoxesTile extends Tile {
    private int owner;  // 0 = unclaimed, 1 = player 1, 2 = player 2

    /**
     * Create a new Dots and Boxes tile (box).
     * @param row Row position
     * @param col Column position
     */
    public DotsAndBoxesTile(int row, int col) {
        super(row, col);
        this.owner = 0;
    }

    /**
     * Set the owner of this box.
     * @param playerNumber Player number (1 or 2)
     */
    public void setOwner(int playerNumber) {
        this.owner = playerNumber;
        if (playerNumber > 0) {
            // Create and set a piece to represent ownership
            this.piece = new DotsAndBoxesPiece(playerNumber);
        } else {
            this.piece = null;
        }
    }

    /**
     * Get the owner of this box.
     * @return Owner number (0 if unclaimed)
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Check if this box has been claimed.
     * @return true if claimed by a player
     */
    public boolean isClaimed() {
        return owner > 0;
    }

    @Override
    public String toString() {
        return owner > 0 ? Integer.toString(owner) : " ";
    }

    @Override
    public boolean isSpecial() {
        return isClaimed();  // Claimed boxes are "special"
    }
}
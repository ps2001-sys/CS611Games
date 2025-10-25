package a3;

import common.Tile;

/**
 * Quoridor tile implementation that extends the common Tile class.
 * Each tile can have walls on its borders and can contain a pawn.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-10-25
 */
public class QuoridorTile extends Tile {
    private boolean wallNorth;
    private boolean wallSouth;
    private boolean wallEast;
    private boolean wallWest;

    /**
     * Create a new Quoridor tile.
     * @param row Row position
     * @param col Column position
     */
    public QuoridorTile(int row, int col) {
        super(row, col);
        this.wallNorth = false;
        this.wallSouth = false;
        this.wallEast = false;
        this.wallWest = false;
    }

    /**
     * Check if there's a wall on the north edge.
     * @return true if wall exists
     */
    public boolean hasWallNorth() {
        return wallNorth;
    }

    /**
     * Set wall on the north edge.
     * @param hasWall true to place wall
     */
    public void setWallNorth(boolean hasWall) {
        this.wallNorth = hasWall;
    }

    /**
     * Check if there's a wall on the south edge.
     * @return true if wall exists
     */
    public boolean hasWallSouth() {
        return wallSouth;
    }

    /**
     * Set wall on the south edge.
     * @param hasWall true to place wall
     */
    public void setWallSouth(boolean hasWall) {
        this.wallSouth = hasWall;
    }

    /**
     * Check if there's a wall on the east edge.
     * @return true if wall exists
     */
    public boolean hasWallEast() {
        return wallEast;
    }

    /**
     * Set wall on the east edge.
     * @param hasWall true to place wall
     */
    public void setWallEast(boolean hasWall) {
        this.wallEast = hasWall;
    }

    /**
     * Check if there's a wall on the west edge.
     * @return true if wall exists
     */
    public boolean hasWallWest() {
        return wallWest;
    }

    /**
     * Set wall on the west edge.
     * @param hasWall true to place wall
     */
    public void setWallWest(boolean hasWall) {
        this.wallWest = hasWall;
    }

    /**
     * Check if this tile has any walls.
     * @return true if any wall exists
     */
    public boolean hasAnyWall() {
        return wallNorth || wallSouth || wallEast || wallWest;
    }

    /**
     * Check if movement is blocked in a specific direction.
     * @param direction Direction to check (N, S, E, W)
     * @return true if blocked
     */
    public boolean isBlockedInDirection(char direction) {
        switch (direction) {
            case 'N': return wallNorth;
            case 'S': return wallSouth;
            case 'E': return wallEast;
            case 'W': return wallWest;
            default: return false;
        }
    }

    @Override
    public String toString() {
        if (piece != null) {
            return piece.getDisplayChar();
        }
        return " ";
    }

    @Override
    public boolean isSpecial() {
        // Goal tiles could be marked as special
        return (row == 0 || row == 8 || col == 0 || col == 8);
    }
}
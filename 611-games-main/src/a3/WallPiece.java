package a3;

import common.Piece;

/**
 * Wall piece for Quoridor game.
 * Extends the common Piece class to represent a wall segment.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-10-25
 */
public class WallPiece extends Piece {
    private final Position position;   // The top-left coordinate of the wall
    private final boolean horizontal;  // Orientation flag (true = horizontal, false = vertical)
    private final int owner;           // Player number (1–4) who placed the wall

    // Create a new wall piece.
    public WallPiece(Position position, boolean horizontal) {
        super(null, 0);  // Walls have no owner in the traditional sense
        this.position = position;
        this.horizontal = horizontal;
        this.owner = 0;
    }

    // creat the wall with owner
    // Main constructor (used in QuoridorGame, supports player ownership)
    public WallPiece(Position pos, char orientation, int owner) {
        super("Player" + owner, owner);  
        this.position = pos;
        this.horizontal = (orientation == 'H');
        this.owner = owner;
    }


    public WallPiece(Position position, char orientation) {
        this(position, orientation == 'H');
    }

    // ---- Getter methods ----

    // Get wall position
    public Position getPosition() {
        return position;
    }

    // Check if wall is horizontal
    public boolean isHorizontal() {
        return horizontal;
    }

    // Check if wall is vertical
    public boolean isVertical() {
        return !horizontal;
    }

    // Get the player number who owns this wall
    public int getOwnerNumber() {
        return owner;
    }

    // ---- Logic methods ----

    // Return the wall symbol for rendering
    @Override
    public String getDisplayChar() {
        return horizontal ? "═" : "║";
    }

    // Check if this wall overlaps another wall
    public boolean overlaps(WallPiece other) {
        // Same position and same orientation → exact overlap
        if (position.equals(other.position) && horizontal == other.horizontal)
            return true;

        // Adjacent overlap in same row/column (prevents touching walls)
        if (horizontal == other.horizontal) {
            if (horizontal) {
                // Horizontal walls in same row
                if (position.row == other.position.row)
                    return Math.abs(position.col - other.position.col) < 2;
            } else {
                // Vertical walls in same column
                if (position.col == other.position.col)
                    return Math.abs(position.row - other.position.row) < 2;
            }
        }
        return false;
    }

    // Walls are never blank
    @Override
    public boolean isBlank() {
        return false;
    }

    // ---- Utility methods ----

    @Override
    public String toString() {
        return "Wall[" + position + ", " + (horizontal ? "H" : "V") + ", owner=" + owner + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WallPiece)) return false;
        WallPiece wall = (WallPiece) obj;
        return horizontal == wall.horizontal && position.equals(wall.position);
    }

    @Override
    public int hashCode() {
        return 31 * position.hashCode() + (horizontal ? 1 : 0);
    }
}
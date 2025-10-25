package a3;

import common.Piece;

/**
 * Wall piece for Quoridor game.
 * Extends the common Piece class to represent a wall segment.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class WallPiece extends Piece {
    private Position position;
    private boolean horizontal;

    /**
     * Create a new wall piece.
     * @param position The position where the wall is placed
     * @param horizontal true for horizontal wall, false for vertical
     */
    public WallPiece(Position position, boolean horizontal) {
        super(null, 0);  // Walls have no owner in the traditional sense
        this.position = position;
        this.horizontal = horizontal;
    }

    /**
     * Create a new wall piece with character orientation.
     * @param position The position where the wall is placed
     * @param orientation 'H' for horizontal, 'V' for vertical
     */
    public WallPiece(Position position, char orientation) {
        this(position, orientation == 'H');
    }

    /**
     * Get the position of this wall.
     * @return Wall position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Check if this wall is horizontal.
     * @return true if horizontal
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * Check if this wall is vertical.
     * @return true if vertical
     */
    public boolean isVertical() {
        return !horizontal;
    }

    /**
     * Get the positions blocked by this wall.
     * A wall blocks two adjacent spaces.
     * @return Array of blocked positions
     */
    public Position[] getBlockedPositions() {
        if (horizontal) {
            return new Position[] {
                    position,
                    new Position(position.row, position.col + 1)
            };
        } else {
            return new Position[] {
                    position,
                    new Position(position.row + 1, position.col)
            };
        }
    }

    /**
     * Check if this wall overlaps with another wall.
     * @param other The other wall
     * @return true if they overlap
     */
    public boolean overlaps(WallPiece other) {
        // Same position and orientation
        if (position.equals(other.position) && horizontal == other.horizontal) {
            return true;
        }

        // Crossing at same intersection
        if (position.equals(other.position) && horizontal != other.horizontal) {
            return true;
        }

        // Check adjacent overlap for same orientation
        if (horizontal == other.horizontal) {
            if (horizontal) {
                // Horizontal walls
                if (position.row == other.position.row) {
                    return Math.abs(position.col - other.position.col) < 2;
                }
            } else {
                // Vertical walls
                if (position.col == other.position.col) {
                    return Math.abs(position.row - other.position.row) < 2;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isBlank() {
        return false;  // Walls are never blank
    }

    @Override
    public String toString() {
        return "Wall[" + position + ", " + (horizontal ? "H" : "V") + "]";
    }

    @Override
    public String getDisplayChar() {
        return horizontal ? "═" : "║";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WallPiece wall = (WallPiece) obj;
        return horizontal == wall.horizontal && position.equals(wall.position);
    }

    @Override
    public int hashCode() {
        return 31 * position.hashCode() + (horizontal ? 1 : 0);
    }
}
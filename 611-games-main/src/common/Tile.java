package common;

/**
 * Abstract base class representing a tile/space on a game board.
 * This class can be extended for different types of tiles in various games.
 * Tiles are containers that can hold pieces.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public abstract class Tile {
    protected int row;
    protected int col;
    protected Piece piece;  // The piece currently on this tile (can be null)

    /**
     * Constructor for creating a tile at a specific position.
     * @param row Row position of the tile
     * @param col Column position of the tile
     */
    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
        this.piece = null;
    }

    /**
     * Place a piece on this tile.
     * @param piece The piece to place (can be null to clear)
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Get the piece on this tile.
     * @return The piece on this tile (null if empty)
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Check if this tile is empty (no piece on it).
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return piece == null;
    }

    /**
     * Get the row position of this tile.
     * @return Row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the column position of this tile.
     * @return Column position
     */
    public int getCol() {
        return col;
    }

    /**
     * Check if this tile can accept a piece. Can be overridden by subclasses
     * for special tile types that might not accept pieces.
     * @param piece The piece to check
     * @return true if the piece can be placed here, false otherwise
     */
    public boolean canAcceptPiece(Piece piece) {
        return true;  // Default: all tiles can accept pieces
    }

    /**
     * Get a string representation of this tile.
     * @return String representation
     */
    public abstract String toString();

    /**
     * Check if this tile is a special tile (e.g., goal, start, etc.).
     * Default is false, can be overridden by subclasses.
     * @return true if special, false otherwise
     */
    public boolean isSpecial() {
        return false;
    }
}
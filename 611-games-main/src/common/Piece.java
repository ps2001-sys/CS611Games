package common;

/**
 * Abstract base class for all game pieces in the CS611 Game Suite.
 * Pieces are the movable/placeable elements in games that sit on tiles.
 *
 * This separation of Piece and Tile allows for flexible game design where
 * different types of pieces can be placed on different types of tiles.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-10-25
 */
public abstract class Piece {
    protected String owner;  // The player who owns this piece (can be null for neutral pieces)
    protected int value;     // Numeric value of the piece (game-specific meaning)

    /**
     * Constructor for creating a piece.
     * @param owner The player who owns this piece (null for neutral)
     * @param value The value of this piece
     */
    public Piece(String owner, int value) {
        this.owner = owner;
        this.value = value;
    }

    /**
     * Get the owner of this piece.
     * @return Owner name or null if neutral
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Set the owner of this piece.
     * @param owner New owner name
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Get the numeric value of this piece.
     * @return The value
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value of this piece.
     * @param value New value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Check if this piece is blank/empty (game-specific meaning).
     * @return true if blank, false otherwise
     */
    public abstract boolean isBlank();

    /**
     * Check if this piece can move to a specific tile.
     * Default implementation returns true, can be overridden.
     * @param tile The target tile
     * @return true if the move is valid, false otherwise
     */
    public boolean canMoveTo(Tile tile) {
        return tile != null && tile.canAcceptPiece(this);
    }

    /**
     * Get a string representation of this piece.
     * @return String representation
     */
    public abstract String toString();

    /**
     * Get the display character for this piece (for rendering).
     * @return Display character
     */
    public abstract String getDisplayChar();

    /**
     * Check if this piece is owned by a specific player.
     * @param playerName Name of the player to check
     * @return true if owned by the specified player, false otherwise
     */
    public boolean isOwnedBy(String playerName) {
        return owner != null && owner.equals(playerName);
    }
}
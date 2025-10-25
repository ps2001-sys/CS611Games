package common;

/**
 * Tile class — represents a single square or space on the game board.
 * Think of it as a little container that might hold a game piece.
 *
 * You can extend this class for special tile types in your games,
 * like start, goal, or obstacle tiles.
 *
 * Created by Zhuojun Lyu & Priyanshu Singh, 2025-10-25
 */
public abstract class Tile {

    protected int row;       // row position on the board
    protected int col;       // column position on the board
    protected Piece piece;   // the piece sitting on this tile, null if empty

    /**
     * Create a tile at the given spot.
     * Starts off empty (no piece).
     *
     * @param row Row index on the board
     * @param col Column index on the board
     */
    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
        this.piece = null; // nothing here yet
    }

    /**
     * Put a piece on this tile.
     * Pass null if you want to clear it.
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Grab the piece currently here.
     * Might be null if the tile is empty.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Is this tile empty, with no piece at all?
     */
    public boolean isEmpty() {
        return piece == null;
    }

    /**
     * Which row is this tile in?
     */
    public int getRow() {
        return row;
    }

    /**
     * … and which column?
     */
    public int getCol() {
        return col;
    }

    /**
     * Can we put this piece here?
     * By default, yes, but subclasses can say “no way” if needed.
     */
    public boolean canAcceptPiece(Piece piece) {
        return true;  // default says yes, game rules might override
    }

    /**
     * Return a string version of this tile.
     * Subclasses *must* implement this (abstract method).
     */
    @Override
    public abstract String toString();

    /**
     * Is this a “special” tile? Start, goal, trap, whatever.
     * Most tiles are not special by default.
     * Override in your special tiles if you want.
     */
    public boolean isSpecial() {
        return false;
    }
}

package a1;

/**
 * Concrete implementation of Piece for the sliding puzzle.
 * Represents a single tile with a numeric value (0 = blank).
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class Tile extends Piece {
    private final int v; // 0 means blank

    public Tile(int v) {
        this.v = v;
    }

    @Override
    public boolean isBlank() {
        return v == 0;
    }

    @Override
    public int value() {
        return v;
    }

    @Override
    public String toString() {
        return isBlank() ? " " : Integer.toString(v);
    }
}
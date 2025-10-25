package a2;

/**
 * Represents a move in Dots and Boxes game.
 * A move is placing an edge between two dots.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class DotsAndBoxesMove {
    private final int row;
    private final int col;
    private final boolean horizontal;

    /**
     * Create a new move.
     * @param row Row coordinate
     * @param col Column coordinate
     * @param horizontal true for horizontal edge, false for vertical
     */
    public DotsAndBoxesMove(int row, int col, boolean horizontal) {
        this.row = row;
        this.col = col;
        this.horizontal = horizontal;
    }

    /**
     * Create a new move from character direction.
     * @param row Row coordinate
     * @param col Column coordinate
     * @param direction 'H' for horizontal, 'V' for vertical
     */
    public DotsAndBoxesMove(int row, int col, char direction) {
        this.row = row;
        this.col = col;
        this.horizontal = Character.toUpperCase(direction) == 'H';
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public boolean isVertical() {
        return !horizontal;
    }

    @Override
    public String toString() {
        return (horizontal ? "H" : "V") + " " + row + " " + col;
    }
}
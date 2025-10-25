package a1;

import common.Board;
import common.Tile;

/**
 * Sliding Puzzle specific board implementation.
 * Extends the common Board class to provide sliding puzzle functionality.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-10-25
 */
public class SlidingPuzzleBoard extends Board {
    private int blankRow;
    private int blankCol;

    /**
     * Create a new sliding puzzle board.
     * @param rows Number of rows
     * @param cols Number of columns
     */
    public SlidingPuzzleBoard(int rows, int cols) {
        super(rows, cols);
    }

    @Override
    protected void initializeBoard() {
        int value = 1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new SlidingPuzzleTile(r, c, value++);
            }
        }
        // Set last tile as blank
        grid[rows - 1][cols - 1] = new SlidingPuzzleTile(rows - 1, cols - 1, 0);
        blankRow = rows - 1;
        blankCol = cols - 1;
    }

    /**
     * Check if a numbered tile can slide into the blank.
     * @param number The tile number to check
     * @return true if the tile can slide
     */
    public boolean canSlide(int number) {
        if (number <= 0 || number >= rows * cols) return false;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                SlidingPuzzleTile tile = (SlidingPuzzleTile) grid[r][c];
                if (tile.getValue() == number) {
                    // Check if adjacent to blank (not diagonal)
                    return (r == blankRow && Math.abs(c - blankCol) == 1) ||
                            (c == blankCol && Math.abs(r - blankRow) == 1);
                }
            }
        }
        return false;
    }

    /**
     * Slide a numbered tile into the blank space.
     * @param number The tile number to slide
     * @return true if the slide was successful
     */
    public boolean slide(int number) {
        if (!canSlide(number)) return false;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                SlidingPuzzleTile tile = (SlidingPuzzleTile) grid[r][c];
                if (tile.getValue() == number) {
                    // Swap with blank
                    grid[blankRow][blankCol] = tile;
                    grid[r][c] = new SlidingPuzzleTile(r, c, 0);
                    blankRow = r;
                    blankCol = c;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Move the blank in a direction (for shuffling).
     * @param dr Row delta
     * @param dc Column delta
     * @return true if the move was successful
     */
    public boolean moveBlank(int dr, int dc) {
        int newRow = blankRow + dr;
        int newCol = blankCol + dc;

        if (!isValidPosition(newRow, newCol)) {
            return false;
        }

        // Swap blank with adjacent tile
        SlidingPuzzleTile adjacentTile = (SlidingPuzzleTile) grid[newRow][newCol];
        grid[blankRow][blankCol] = adjacentTile;
        grid[newRow][newCol] = new SlidingPuzzleTile(newRow, newCol, 0);
        blankRow = newRow;
        blankCol = newCol;
        return true;
    }

    @Override
    public boolean isGameOver() {
        int expectedValue = 1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == rows - 1 && c == cols - 1) {
                    // Last position should be blank (0)
                    return ((SlidingPuzzleTile) grid[r][c]).getValue() == 0;
                }
                if (((SlidingPuzzleTile) grid[r][c]).getValue() != expectedValue++) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder();
        int maxValue = rows * cols - 1;
        int width = Math.max(2, Integer.toString(maxValue).length());

        for (int r = 0; r < rows; r++) {
            // Top border
            for (int c = 0; c < cols; c++) {
                sb.append("+");
                for (int i = 0; i < width + 2; i++) {
                    sb.append("-");
                }
            }
            sb.append("+\n");

            // Cell contents
            for (int c = 0; c < cols; c++) {
                sb.append("| ");
                SlidingPuzzleTile tile = (SlidingPuzzleTile) grid[r][c];
                if (tile.getValue() == 0) {
                    for (int i = 0; i < width; i++) {
                        sb.append(" ");
                    }
                } else {
                    sb.append(String.format("%" + width + "d", tile.getValue()));
                }
                sb.append(" ");
            }
            sb.append("|\n");
        }

        // Bottom border
        for (int c = 0; c < cols; c++) {
            sb.append("+");
            for (int i = 0; i < width + 2; i++) {
                sb.append("-");
            }
        }
        sb.append("+\n");

        return sb.toString();
    }
}
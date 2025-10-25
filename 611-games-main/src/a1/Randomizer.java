package a1;

import java.util.Random;

/**
 * Shuffle generator for the sliding puzzle.
 * Uses a "blank walk" method: randomly moves the blank tile many times
 * to ensure the generated puzzle is always solvable.
 *
 * Updated to work with SlidingPuzzleBoard instead of Board.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class Randomizer {
    private final Random rng = new Random();

    // Possible movement directions (down, up, right, left)
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private static final int MAX_RETRIES = 10;

    /**
     * Shuffle the board by performing random blank moves.
     * This guarantees a solvable configuration.
     *
     * @param board the board to shuffle
     * @param steps number of random moves to perform
     */
    public void shuffle(SlidingPuzzleBoard board, int steps) {
        int lastDr = 0, lastDc = 0;

        for (int i = 0; i < steps; i++) {
            int tries = 0;

            // Try to make a valid move (avoid undoing the last move)
            while (tries++ < MAX_RETRIES) {
                int[] direction = DIRECTIONS[rng.nextInt(DIRECTIONS.length)];
                int dr = direction[0];
                int dc = direction[1];

                // Avoid immediately undoing the last move
                if (dr == -lastDr && dc == -lastDc) {
                    continue;
                }

                // Attempt the move
                if (board.moveBlank(dr, dc)) {
                    lastDr = dr;
                    lastDc = dc;
                    break;
                }
            }
        }
    }
}
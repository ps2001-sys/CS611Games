package a1;

import java.util.Random;

/**
 * Shuffle generator for the puzzle.
 * in this class we use "blank walk" method: move the blank randomly many times to make sure the puzzle is solvable
 */
public class Randomizer {
    private final Random rng = new Random();
    //we use "blank walk" method: move the blank randomly many times to make sure the puzzle is solvable
    public void shuffle(Board b, int steps){
        int lastDr=0,lastDc=0;
        for(int i=0;i<steps;i++){
            int[][] dirs={{1,0},{-1,0},{0,1},{0,-1}};
            int tries=0;
            while(tries++<10){
                int[] d = dirs[rng.nextInt(4)];
                // Avoid immediate undo of last move
                if (d[0]==-lastDr && d[1]==-lastDc) continue;
                if (b.moveBlank(d[0], d[1])) {
                    lastDr=d[0]; lastDc=d[1];
                    break;
                }
            }
        }
    }
}

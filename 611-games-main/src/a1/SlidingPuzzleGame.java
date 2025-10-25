package a1;

import engine.Game;
import engine.TextUI;
import common.Player;
import common.InputValidator;
import java.util.Random;

/**
 * Sliding Puzzle game implementation.
 * Extends the Game base class to provide sliding puzzle functionality.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class SlidingPuzzleGame extends Game {
    private SlidingPuzzleBoard board;
    private InputValidator validator;
    private int rows;
    private int cols;
    private int difficulty;
    private Random random;

    // Difficulty settings
    private static final int EASY_MULTIPLIER = 10;
    private static final int NORMAL_MULTIPLIER = 20;
    private static final int HARD_MULTIPLIER = 50;

    /**
     * Create a new Sliding Puzzle game.
     * @param ui The text UI for input/output
     */
    public SlidingPuzzleGame(TextUI ui) {
        super(ui);
        this.validator = new InputValidator(ui);
        this.random = new Random();
    }

    @Override
    public String getName() {
        return "Sliding Puzzle";
    }

    @Override
    protected int getNumberOfPlayers() {
        return 1;  // Single player game
    }

    @Override
    protected void initializeGame() {
        // Get board dimensions
        rows = validator.readBoundedInt("Rows (2-10, default 3): ", 3, 2, 10);
        cols = validator.readBoundedInt("Cols (2-10, default 3): ", 3, 2, 10);

        // Get difficulty
        difficulty = selectDifficulty();

        // Create and shuffle board
        board = new SlidingPuzzleBoard(rows, cols);
        shuffleBoard();
    }

    @Override
    protected boolean processTurn() {
        ui.print("\nEnter tile number to slide (h=help, r=reset, q=quit): ");
        String input = ui.nextLine().trim();

        if (input.equalsIgnoreCase("q")) {
            return false;  // End game
        }

        if (input.equalsIgnoreCase("h")) {
            displayHelp();
            return true;
        }

        if (input.equalsIgnoreCase("r")) {
            board.reset();
            shuffleBoard();
            ui.println(ui.cyan("Board reset and reshuffled."));
            return true;
        }

        // Try to parse as number
        Integer tileNumber = ui.tryParseInt(input);
        if (tileNumber == null) {
            ui.println(ui.red("Please enter a valid tile number or command."));
            return true;
        }

        if (!board.canSlide(tileNumber)) {
            ui.println(ui.red("That tile cannot slide. Choose one adjacent to the blank."));
            return true;
        }

        board.slide(tileNumber);
        getCurrentPlayer().addScore(1);  // Track moves as score
        return true;
    }

    @Override
    protected boolean isGameOver() {
        return board.isGameOver();
    }

    @Override
    protected void displayGameState() {
        ui.println("\n" + board.render());
        ui.println("Moves: " + getCurrentPlayer().getScore());
    }

    @Override
    protected void handleGameEnd() {
        long duration = getGameDuration();
        Player player = getCurrentPlayer();

        ui.println(ui.green("\n🎉 Congratulations, " + player.getName() + "! You solved the puzzle!"));
        ui.println("Final stats: " + player.getScore() + " moves in " + (duration / 1000) + " seconds");

        // Record game
        player.recordGame(true, duration);
        ui.println("\n" + player.getStatsSummary());
    }

    @Override
    protected void displayHelp() {
        ui.println("\n=== Sliding Puzzle Help ===");
        ui.println("Goal: Arrange tiles in numerical order with blank at the end");
        ui.println("Commands:");
        ui.println("  <number> - Slide the numbered tile into the blank");
        ui.println("  h        - Show this help");
        ui.println("  r        - Reset and reshuffle the board");
        ui.println("  q        - Quit the game");
    }

    /**
     * Select difficulty level.
     * @return 1 for Easy, 2 for Normal, 3 for Hard
     */
    private int selectDifficulty() {
        while (true) {
            ui.println("\nSelect difficulty:");
            ui.println("  [1] Easy");
            ui.println("  [2] Normal");
            ui.println("  [3] Hard");
            ui.print("> ");

            String input = ui.nextLine().trim();
            if (input.equals("1")) return 1;
            if (input.equals("2")) return 2;
            if (input.equals("3")) return 3;

            ui.println(ui.red("Please enter 1, 2, or 3."));
        }
    }

    /**
     * Shuffle the board based on difficulty.
     */
    private void shuffleBoard() {
        int steps = calculateShuffleSteps();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int i = 0; i < steps; i++) {
            // Try random directions until one works
            int attempts = 0;
            while (attempts < 10) {
                int[] dir = directions[random.nextInt(4)];
                if (board.moveBlank(dir[0], dir[1])) {
                    break;
                }
                attempts++;
            }
        }
    }

    /**
     * Calculate number of shuffle steps based on difficulty.
     * @return Number of shuffle steps
     */
    private int calculateShuffleSteps() {
        int base = rows * cols;
        int multiplier;

        switch (difficulty) {
            case 1:  multiplier = EASY_MULTIPLIER; break;
            case 2:  multiplier = NORMAL_MULTIPLIER; break;
            case 3:  multiplier = HARD_MULTIPLIER; break;
            default: multiplier = NORMAL_MULTIPLIER;
        }

        return Math.max(40, base * multiplier);
    }
}
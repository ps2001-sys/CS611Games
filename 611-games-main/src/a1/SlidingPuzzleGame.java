package a1;

import engine.Game;
import engine.TextUI;
import common.Player;
import common.Statistics;
import common.InputValidator;

/**
 * Main game loop for the Sliding Puzzle.
 * Handles user interaction, board setup, move processing, and win detection.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class SlidingPuzzleGame implements Game {
    // Difficulty configuration constants
    private static final int EASY_MULTIPLIER = 10;
    private static final int NORMAL_MULTIPLIER = 20;
    private static final int HARD_MULTIPLIER = 50;
    private static final int MIN_SHUFFLE_STEPS = 40;

    private final TextUI ui;
    private final Statistics stats = new Statistics();
    private final InputValidator validator;

    public SlidingPuzzleGame(TextUI ui) {
        this.ui = ui;
        this.validator = new InputValidator(ui);
    }

    @Override
    public String getName() {
        return "Sliding Puzzle";
    }

    @Override
    public void start() {
        ui.println(ui.bold("== A1: Sliding Puzzle =="));
        ui.println("Welcome! This is a single-player sliding puzzle running in the terminal.");
        ui.println("Enter a tile number next to the blank to slide it. Type 'h' for help.\n");

        while (true) {
            // Read player info and puzzle configuration
            String playerName = validator.readValidName(
                    "Player name (default Player): ", "Player"
            );
            Player player = new Player(playerName);

            int rows = validator.readBoundedInt(
                    "Rows (>=2, default 3): ", 3, 2, InputValidator.MAX_BOARD_SIZE
            );
            int cols = validator.readBoundedInt(
                    "Cols (>=2, default 3): ", 3, 2, InputValidator.MAX_BOARD_SIZE
            );

            int difficulty = askDifficulty();
            int steps = calculateShuffleSteps(rows, cols, difficulty);

            // Create board and shuffle (guaranteed solvable)
            Board board = new Board(rows, cols);
            Randomizer rnd = new Randomizer();
            rnd.shuffle(board, steps);

            // Game loop
            long startNs = System.nanoTime();
            int moves = 0;

            while (true) {
                ui.println(board.renderBoxed());

                if (board.isSolved()) {
                    long durMs = (System.nanoTime() - startNs) / 1_000_000;
                    ui.println(ui.green("Congrats, " + player.getName() + "! You solved it."));
                    ui.println("Stats: moves=" + moves + ", time=" + durMs + "ms");

                    // Record statistics
                    stats.recordGame(player.getName(), true, moves, durMs);
                    ui.println("\n" + stats.getStats(player.getName()));
                    break;
                }

                ui.println("Enter a tile number to slide (or 'n'=new, 'r'=reset, 's'=stats, 'h'=help, 'q'=quit):");
                ui.print("> ");
                String input = ui.nextLine().trim();

                // Handle commands
                if (input.equalsIgnoreCase("q")) {
                    ui.println(ui.bold("Quit. See you next time!"));
                    ui.println("\n" + stats.getAllStats());
                    return;
                }

                if (input.equalsIgnoreCase("h")) {
                    printHelp();
                    continue;
                }

                if (input.equalsIgnoreCase("s")) {
                    ui.println(stats.getAllStats());
                    continue;
                }

                if (input.equalsIgnoreCase("n")) {
                    // Generate a new shuffled board with same settings
                    board = new Board(rows, cols);
                    rnd.shuffle(board, steps);
                    moves = 0;
                    startNs = System.nanoTime();
                    ui.println(ui.cyan("New board generated."));
                    continue;
                }

                if (input.equalsIgnoreCase("r")) {
                    // Reset and reshuffle
                    board = new Board(rows, cols);
                    rnd.shuffle(board, steps);
                    moves = 0;
                    startNs = System.nanoTime();
                    ui.println(ui.cyan("Board reset & reshuffled."));
                    continue;
                }

                // Process tile slide
                Integer num = ui.tryParseInt(input);
                if (num == null) {
                    ui.println(ui.red("Please enter a tile number or a command."));
                    continue;
                }

                if (!board.canSlide(num)) {
                    ui.println(ui.red("Illegal move. Choose a tile adjacent to the blank."));
                    continue;
                }

                board.slide(num);
                moves++;
            }

            // Ask to play again
            String choice = validator.readChoice(
                    "\nPlay again? [1] same settings  [2] change settings  [3] back to main",
                    "1|2|3"
            );

            if ("3".equals(choice)) {
                return;
            }
            // If "1" or "2", continue outer loop
        }
    }

    /**
     * Ask user to select difficulty level.
     * @return 1 for Easy, 2 for Normal, 3 for Hard
     */
    private int askDifficulty() {
        while (true) {
            ui.println("Choose difficulty: [1] Easy  [2] Normal  [3] Hard");
            ui.print("> ");
            String s = ui.nextLine().trim();

            if (s.equals("1") || s.equalsIgnoreCase("easy")) return 1;
            if (s.equals("2") || s.equalsIgnoreCase("normal")) return 2;
            if (s.equals("3") || s.equalsIgnoreCase("hard")) return 3;

            ui.println(ui.red("Please enter 1/2/3."));
        }
    }

    /**
     * Calculate the number of shuffle steps based on difficulty.
     * Higher difficulty = more shuffle steps.
     */
    private int calculateShuffleSteps(int rows, int cols, int difficulty) {
        int base = rows * cols;
        int multiplier;

        switch (difficulty) {
            case 1:  multiplier = EASY_MULTIPLIER; break;
            case 2:  multiplier = NORMAL_MULTIPLIER; break;
            case 3:  multiplier = HARD_MULTIPLIER; break;
            default: multiplier = NORMAL_MULTIPLIER;
        }

        return Math.max(MIN_SHUFFLE_STEPS, base * multiplier);
    }

    /**
     * Display help message with available commands.
     */
    private void printHelp() {
        ui.println("Commands:");
        ui.println("  <number>  slide the numbered tile into the blank (must be adjacent)");
        ui.println("  n         new random board (same size/difficulty)");
        ui.println("  r         reset to solved, then reshuffle");
        ui.println("  s         show player statistics");
        ui.println("  h         show help");
        ui.println("  q         quit the game");
    }
}
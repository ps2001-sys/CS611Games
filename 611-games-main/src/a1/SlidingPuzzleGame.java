package a1;

import engine.Game;
import engine.TextUI;
import common.Player;

/**
 * Main game loop for Sliding Puzzle.
 * this class is about:
     * 1.Ask user for board size
     * 2.Randomizer to shuffle the board
     * 3.Using loop to keep the process of the game: render → read input → make move
     * 4.Detect solved state or quit
 */
public class SlidingPuzzleGame implements Game {
    private final TextUI ui;
    public SlidingPuzzleGame(TextUI ui){ this.ui = ui; }

    @Override public String getName(){ return "Sliding Puzzle"; }

    @Override
    public void start(){
        ui.println(ui.bold("== A1: Sliding Puzzle =="));
        ui.println("Welcome! This is a single-player sliding puzzle running in the terminal."); // spec welcome
        ui.println("Enter a tile number next to the blank to slide it. Type 'h' for help.\n");

        while (true) {
            // read player & puzzle config
            Player player = new Player(readNonEmpty("Player name (default Player): ", "Player"));
            int rows = askInt("Rows (>=2, default 3): ", 3, 2, 10);  // cap to avoid layout issues
            int cols = askInt("Cols (>=2, default 3): ", 3, 2, 10);

            int difficulty = askDifficulty(); // 1 easy, 2 normal, 3 hard
            int steps = shuffleSteps(rows, cols, difficulty);

            // create board & shuffle (guaranteed solvable)
            Board board = new Board(rows, cols);
            Randomizer rnd = new Randomizer();
            rnd.shuffle(board, steps);

            // game loop
            long startNs = System.nanoTime();
            int moves = 0;

            while (true) {
                ui.println(board.renderBoxed()); // pretty rendering per spec example
                if (board.isSolved()){
                    long durMs = (System.nanoTime() - startNs) / 1_000_000;
                    ui.println(ui.green("Congrats, "+player.name+"! You solved it."));
                    ui.println("Stats: moves="+moves+", time="+durMs+"ms");
                    break;
                }

                ui.println("Enter a tile number to slide (or 'n'=new, 'r'=reset, 'h'=help, 'q'=quit):");
                ui.print("> ");
                String s = ui.nextLine().trim();

                // commands
                if (s.equalsIgnoreCase("q")) {
                    ui.println(ui.bold("Quit. See you next time!"));
                    return; // graceful exit
                }
                if (s.equalsIgnoreCase("h")) {
                    printHelp();
                    continue;
                }
                if (s.equalsIgnoreCase("n")) {
                    // regenerate a new shuffled board with same size/difficulty
                    board = new Board(rows, cols);
                    rnd.shuffle(board, steps);
                    moves = 0; startNs = System.nanoTime();
                    ui.println(ui.cyan("New board generated."));
                    continue;
                }
                if (s.equalsIgnoreCase("r")) {
                    // reset to solved then reshuffle (shows that reset ability exists)
                    board = new Board(rows, cols);
                    rnd.shuffle(board, steps);
                    moves = 0; startNs = System.nanoTime();
                    ui.println(ui.cyan("Board reset & reshuffled."));
                    continue;
                }

                // slide a tile
                Integer num = ui.tryParseInt(s);
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

            // replay or not
            ui.println("\nPlay again? [1] same settings  [2] change settings  [3] back to main");
            ui.print("> ");
            String again = ui.nextLine().trim();
            if ("1".equals(again)) {
                // loop again with same settings (do nothing here)
            } else if ("2".equals(again)) {
                // outer while continues; we'll re-read settings
            } else {
                return; // back to menu
            }
        }
    }

    // ask difficulty (1 easy, 2 normal, 3 hard) 1=Easy, 2=Normal, 3=Hard (The difficulty level is determined by randomly shuffling the steps.)
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

    // convert difficulty to shuffle steps
    private int shuffleSteps(int rows, int cols, int difficulty){
        int base = rows * cols;
        // easy = 5, normal = 20, hard = 50 times
        int factor = (difficulty == 1 ? 10 : (difficulty == 2 ? 20 : 50));
        int steps = Math.max(40, base * factor);
        return steps;
    }

    // read player name, default if empty, and check the input is illegal or not
    private String readNonEmpty(String prompt, String dft){
        ui.print(prompt);
        String s = ui.nextLine().trim();
        if (s.isEmpty()) s = dft;
        if (s.length() > 15) s = s.substring(0, 15);
        if (!s.matches("[A-Za-z0-9_]+")) s = s.replaceAll("[^A-Za-z0-9_]", "_");
        return s;
    }

    // ask integer with bounds, fallback to default
    private int askInt(String prompt, int dft, int min, int max){
        ui.print(prompt);
        Integer v = ui.tryParseInt(ui.nextLine());
        if (v == null || v < min || v > max) {
            ui.println(ui.red("Using default: " + dft));
            return dft;
        }
        return v;
    }

    //print help message
    private void printHelp(){
        ui.println("Commands:");
        ui.println("  <number>  slide the numbered tile into the blank (must be adjacent)");
        ui.println("  n         new random board (same size/difficulty)");
        ui.println("  r         reset to solved, then reshuffle");
        ui.println("  h         show help");
        ui.println("  q         quit the game");
    }
}
package a2;

import engine.Game;
import engine.TextUI;
import common.Player;
import common.InputValidator;

/**
 * Dots and Boxes game implementation.
 * Extends the Game base class to provide dots and boxes functionality.
 *
 * This class properly uses instance variables for board and players,
 * demonstrating correct object-oriented design.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class DotsAndBoxesGame extends Game {
    // Instance variables for game state
    private DotsAndBoxesBoard board;
    private InputValidator validator;
    private boolean extraTurnOnBox;
    private int lastBoxesCompleted;

    /**
     * Create a new Dots and Boxes game.
     * @param ui The text UI for input/output
     */
    public DotsAndBoxesGame(TextUI ui) {
        super(ui);
        this.validator = new InputValidator(ui);
        this.extraTurnOnBox = true;  // Standard rule: completing a box grants extra turn
    }

    @Override
    public String getName() {
        return "Dots & Boxes";
    }

    @Override
    protected int getNumberOfPlayers() {
        return 2;  // Always 2 players
    }

    @Override
    protected void setupPlayers() {
        ui.println("Setting up players for Dots & Boxes...\n");

        while (true) {
            String name1 = validator.readValidName("Player 1 name (default P1): ", "P1");
            String name2 = validator.readValidName("Player 2 name (default P2): ", "P2");

            if (name1.equalsIgnoreCase(name2)) {
                ui.println(ui.red("Player names must be different. Please try again.\n"));
                continue;
            }

            players.add(new Player(name1, 1));
            players.add(new Player(name2, 2));
            break;
        }
    }

    @Override
    protected void initializeGame() {
        // Get board dimensions
        int rows = validator.readBoundedInt(
                "Number of box rows (1-10, default 2): ", 2, 1, 10
        );
        int cols = validator.readBoundedInt(
                "Number of box columns (1-10, default 2): ", 2, 1, 10
        );

        // Create the board
        board = new DotsAndBoxesBoard(rows, cols);

        ui.println("\nGame started! Players take turns drawing edges between dots.");
        ui.println("Complete a box to claim it and get an extra turn.\n");
    }

    @Override
    protected boolean processTurn() {
        Player currentPlayer = getCurrentPlayer();
        int playerNumber = currentPlayer.getPlayerNumber();

        ui.println("\n" + currentPlayer.getName() + "'s turn (Player " + playerNumber + ")");
        ui.println("Enter: [H/V] <row> <col>  (h=help, s=stats, q=quit)");
        ui.print("> ");

        String input = ui.nextLine().trim();

        if (input.equalsIgnoreCase("q")) {
            return false;  // End game
        }

        if (input.equalsIgnoreCase("h")) {
            displayHelp();
            return true;
        }

        if (input.equalsIgnoreCase("s")) {
            displayStats();
            return true;
        }

        // Parse move
        String[] tokens = input.split("\\s+");
        if (tokens.length != 3) {
            ui.println(ui.red("Invalid format. Use: H <row> <col> or V <row> <col>"));
            return true;
        }

        char direction = Character.toUpperCase(tokens[0].charAt(0));
        Integer row = ui.tryParseInt(tokens[1]);
        Integer col = ui.tryParseInt(tokens[2]);

        if (row == null || col == null || (direction != 'H' && direction != 'V')) {
            ui.println(ui.red("Invalid input. Direction must be H or V, followed by numbers."));
            return true;
        }

        // Create and validate move
        DotsAndBoxesMove move = new DotsAndBoxesMove(row, col, direction);

        if (!board.isEdgeFree(move)) {
            ui.println(ui.red("That edge is already taken or out of bounds."));
            return true;
        }

        // Apply the move
        lastBoxesCompleted = board.applyEdge(move, playerNumber);
        currentPlayer.addScore(lastBoxesCompleted);

        // Check for extra turn
        if (lastBoxesCompleted > 0) {
            ui.println(ui.green("Box completed! " + currentPlayer.getName() + " gets another turn."));
            if (!extraTurnOnBox) {
                nextPlayer();  // Move to next player if extra turn rule is disabled
            }
            // If extraTurnOnBox is true, don't change player (they get another turn)
        } else {
            nextPlayer();  // Normal turn change
        }

        return true;
    }

    @Override
    protected boolean isGameOver() {
        return board.isGameOver();
    }

    @Override
    protected void displayGameState() {
        ui.println("\n" + board.render(ui));

        // Display scores
        ui.println("Scores: ");
        for (Player player : players) {
            int score = board.getScore(player.getPlayerNumber());
            player.resetScore();  // Reset to ensure accurate count
            player.addScore(score);
            ui.println("  " + player.getName() + ": " + score + " boxes");
        }
    }

    @Override
    protected void handleGameEnd() {
        long duration = getGameDuration();

        // Determine winner
        Player player1 = players.get(0);
        Player player2 = players.get(1);
        int score1 = board.getScore(1);
        int score2 = board.getScore(2);

        ui.println("\n" + ui.bold("=== GAME OVER ==="));
        ui.println("Final scores:");
        ui.println("  " + player1.getName() + ": " + score1 + " boxes");
        ui.println("  " + player2.getName() + ": " + score2 + " boxes");

        if (score1 > score2) {
            ui.println(ui.green("\n🎉 " + player1.getName() + " wins!"));
            player1.recordGame(true, duration);
            player2.recordGame(false, duration);
        } else if (score2 > score1) {
            ui.println(ui.green("\n🎉 " + player2.getName() + " wins!"));
            player1.recordGame(false, duration);
            player2.recordGame(true, duration);
        } else {
            ui.println(ui.yellow("\n🤝 It's a tie!"));
            player1.recordGame(false, duration);
            player2.recordGame(false, duration);
        }

        // Display statistics
        ui.println("\nPlayer Statistics:");
        for (Player player : players) {
            ui.println(player.getStatsSummary());
        }
    }

    @Override
    protected void displayHelp() {
        ui.println("\n=== Dots & Boxes Help ===");
        ui.println("Goal: Claim more boxes than your opponent");
        ui.println("Rules:");
        ui.println("  - Draw edges between dots to form boxes");
        ui.println("  - Complete a box to claim it and get another turn");
        ui.println("  - The player with the most boxes wins");
        ui.println("Commands:");
        ui.println("  H <row> <col> - Draw horizontal edge");
        ui.println("  V <row> <col> - Draw vertical edge");
        ui.println("  h             - Show this help");
        ui.println("  s             - Show statistics");
        ui.println("  q             - Quit the game");
    }

    /**
     * Display player statistics.
     */
    private void displayStats() {
        ui.println("\nPlayer Statistics:");
        for (Player player : players) {
            ui.println(player.getStatsSummary());
        }
    }
}
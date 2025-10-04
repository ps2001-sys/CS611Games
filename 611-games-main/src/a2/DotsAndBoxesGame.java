package a2;

import engine.Game;
import engine.TextUI;
import common.Player;
import common.Statistics;
import common.InputValidator;

/**
 * Terminal-based two-player Dots and Boxes game.
 * Features colored rendering (Player 1 = red, Player 2 = blue).
 * Tracks statistics across multiple games.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class DotsAndBoxesGame implements Game {
    private final TextUI ui;
    private final Statistics stats = new Statistics();
    private final InputValidator validator;

    public DotsAndBoxesGame(TextUI ui) {
        this.ui = ui;
        this.validator = new InputValidator(ui);
    }

    @Override
    public String getName() {
        return "Dots & Boxes";
    }

    @Override
    public void start() {
        ui.println(ui.bold("-- A2: Dots & Boxes --"));

        while (true) {
            Rules rules = readRules();

            // Get player names with validation
            Player p1, p2;
            while (true) {
                String name1 = validator.readValidName("Player 1 name (default P1): ", "P1");
                String name2 = validator.readValidName("Player 2 name (default P2): ", "P2");

                // Disallow duplicate names
                if (name1.equalsIgnoreCase(name2)) {
                    ui.println(ui.red("Player names cannot be the same. Please enter again.\n"));
                    continue;
                }

                p1 = new Player(name1);
                p2 = new Player(name2);
                break;
            }

            // Play the match
            boolean again = playMatch(rules, p1, p2);

            if (!again) {
                String choice = validator.readChoice(
                        "\n[1] Change settings  [2] Back to main menu",
                        "1|2"
                );
                if (choice.equals("2")) {
                    ui.println("\n" + stats.getAllStats());
                    return;
                }
            }
        }
    }

    /**
     * Play a single match of Dots and Boxes.
     * @return true if players want to play again with same settings
     */
    private boolean playMatch(Rules rules, Player p1, Player p2) {
        DBGrid grid = new DBGrid(rules.rows, rules.cols);
        int currentPlayer = 1;
        long startNs = System.nanoTime();
        int totalMoves = 0;  // ← FIXED: Added move counter

        while (true) {
            // Render board
            ui.println(ui.isColor() ? grid.renderColored(ui) : grid.render());
            ui.println("Scores: " + p1.getName() + "=" + grid.score(1) +
                    "  " + p2.getName() + "=" + grid.score(2));

            // Check for game end
            if (grid.isFull()) {
                long durMs = (System.nanoTime() - startNs) / 1_000_000;
                int s1 = grid.score(1);
                int s2 = grid.score(2);

                if (s1 == s2) {
                    ui.println(ui.bold("Tie! Final: " + s1 + "-" + s2));
                    // ← FIXED: Record moves and time for both players in a tie
                    stats.recordGame(p1.getName(), false, totalMoves / 2, durMs / 2);
                    stats.recordGame(p2.getName(), false, totalMoves / 2, durMs / 2);
                } else {
                    String winner = (s1 > s2) ? p1.getName() : p2.getName();
                    String loser = (s1 > s2) ? p2.getName() : p1.getName();
                    ui.println(ui.bold("Winner: " + winner + "  Final: " + s1 + "-" + s2));

                    // ← FIXED: Record actual move count and time for both players
                    stats.recordGame(winner, true, totalMoves, durMs);
                    stats.recordGame(loser, false, totalMoves, durMs);
                }

                // Display current statistics
                ui.println("\n" + stats.getStats(p1.getName()));
                ui.println(stats.getStats(p2.getName()));

                String choice = validator.readChoice(
                        "[1] Play again (same settings)  [2] Change settings  [3] Back to main",
                        "1|2|3"
                );

                if ("1".equals(choice)) return true;
                if ("2".equals(choice)) return false;
                if ("3".equals(choice)) {
                    ui.println("\n" + stats.getAllStats());
                    return false;
                }
            }

            // Get current player's move
            String currentName = (currentPlayer == 1) ? p1.getName() : p2.getName();
            ui.println(currentName + " turn. Enter: H r c  or  V r c   (s=stats, q=quit)");
            ui.print("> ");
            String line = ui.nextLine().trim();

            // Handle quit command
            if (line.equalsIgnoreCase("q")) {
                ui.println(ui.bold("Quit. Summary: " +
                        p1.getName() + "=" + grid.score(1) + ", " +
                        p2.getName() + "=" + grid.score(2)));
                ui.println("\n" + stats.getAllStats());
                return false;
            }

            // Handle stats command
            if (line.equalsIgnoreCase("s")) {
                ui.println(stats.getAllStats());
                continue;
            }

            // Parse move input
            String[] tokens = line.split("\\s+");
            if (tokens.length != 3) {
                ui.println(ui.red("Format: H r c  or  V r c"));
                continue;
            }

            char dir = Character.toUpperCase(tokens[0].charAt(0));
            Integer r = ui.tryParseInt(tokens[1]);
            Integer c = ui.tryParseInt(tokens[2]);

            if (r == null || c == null || (dir != 'H' && dir != 'V')) {
                ui.println(ui.red("Invalid input."));
                continue;
            }

            // Apply the move
            DBMove move = new DBMove(r, c, dir);
            if (!grid.isEdgeFree(move)) {
                ui.println(ui.red("Edge occupied or out of range."));
                continue;
            }

            boolean completedBox = grid.applyEdge(move, currentPlayer);
            totalMoves++;  // ← FIXED: Increment move counter

            // Switch players (unless they completed a box and get another turn)
            if (!(completedBox && rules.extraTurnOnBox)) {
                currentPlayer = 3 - currentPlayer; // Toggle between 1 and 2
            }
        }
    }

    /**
     * Read board dimensions from user.
     */
    private Rules readRules() {
        int rows = validator.readBoundedInt(
                "Rows (>=1, default 2): ", 2, 1, InputValidator.MAX_BOARD_SIZE
        );
        int cols = validator.readBoundedInt(
                "Cols (>=1, default 2): ", 2, 1, InputValidator.MAX_BOARD_SIZE
        );
        return Rules.standard(rows, cols);
    }
}
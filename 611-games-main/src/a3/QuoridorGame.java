package a3;

import engine.Game;
import engine.TextUI;
import common.Player;
import common.Statistics;
import common.InputValidator;
import java.util.*;

/**
 * Main game controller for Quoridor.
 * Manages game flow, player turns, wall placement, pawn movement, and victory conditions.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class QuoridorGame implements Game {
    private final TextUI ui;
    private final Statistics stats = new Statistics();
    private final InputValidator validator;

    private static final int BOARD_SIZE = 9;
    private static final int WALLS_PER_PLAYER_2 = 10;
    private static final int WALLS_PER_PLAYER_4 = 5;

    public QuoridorGame(TextUI ui) {
        this.ui = ui;
        this.validator = new InputValidator(ui);
    }

    @Override
    public String getName() {
        return "Quoridor";
    }

    @Override
    public void start() {
        ui.println(ui.bold("== A3: Quoridor =="));
        ui.println("Welcome to Quoridor!");
        ui.println("Race to reach the opposite side while placing walls to block opponents.");
        ui.println("Type 'h' for help at any time.\n");

        while (true) {
            // Game setup
            int numPlayers = selectNumberOfPlayers();
            List<Player> players = getPlayerNames(numPlayers);

            // Initialize game
            QuoridorBoard board = new QuoridorBoard(BOARD_SIZE);
            QuoridorRules rules = new QuoridorRules(board);
            initializePlayerPositions(board, players);

            // Calculate walls per player
            int wallsPerPlayer = (numPlayers == 2) ? WALLS_PER_PLAYER_2 : WALLS_PER_PLAYER_4;
            Map<Player, Integer> wallCounts = new HashMap<>();
            for (Player p : players) {
                wallCounts.put(p, wallsPerPlayer);
            }

            // Play the game
            boolean playAgain = playMatch(board, rules, players, wallCounts);

            if (!playAgain) {
                String choice = validator.readChoice(
                        "\n[1] New game with different settings  [2] Back to main menu",
                        "1|2"
                );
                if (choice.equals("2")) {
                    ui.println("\n" + stats.getAllStats());
                    return;
                }
            }
        }
    }

    private boolean playMatch(QuoridorBoard board, QuoridorRules rules,
                              List<Player> players, Map<Player, Integer> wallCounts) {
        int currentPlayerIndex = 0;
        long startNs = System.nanoTime();
        int totalMoves = 0;
        Map<Player, Integer> moveCount = new HashMap<>();
        for (Player p : players) {
            moveCount.put(p, 0);
        }

        while (true) {
            // Display board
            ui.println(board.render(ui));
            displayGameStatus(players, wallCounts, currentPlayerIndex);

            Player currentPlayer = players.get(currentPlayerIndex);

            // Check for victory
            if (rules.hasWon(board.getPlayerPosition(currentPlayerIndex), currentPlayerIndex, players.size())) {
                long durMs = (System.nanoTime() - startNs) / 1_000_000;
                ui.println(ui.green("\n🎉 " + currentPlayer.getName() + " wins! 🎉"));
                ui.println("Game completed in " + totalMoves + " moves, time: " + durMs + "ms\n");

                // Record statistics
                for (int i = 0; i < players.size(); i++) {
                    Player p = players.get(i);
                    boolean won = (i == currentPlayerIndex);
                    stats.recordGame(p.getName(), won, moveCount.get(p), durMs / players.size());
                }

                // Display stats
                for (Player p : players) {
                    ui.println(stats.getStats(p.getName()));
                }

                String choice = validator.readChoice(
                        "\n[1] Play again (same settings)  [2] Change settings  [3] Back to main",
                        "1|2|3"
                );

                if ("1".equals(choice)) return true;
                if ("2".equals(choice)) return false;
                if ("3".equals(choice)) {
                    ui.println("\n" + stats.getAllStats());
                    return false;
                }
            }

            // Get player move
            ui.println("\n" + currentPlayer.getName() + "'s turn (Player " + (currentPlayerIndex + 1) + ")");
            ui.println("Enter: [M]ove <dir>, [W]all <r> <c> <H/V>, [H]elp, [S]tats, [Q]uit");
            ui.print("> ");

            String input = ui.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                ui.println(ui.bold("Game ended."));
                ui.println("\n" + stats.getAllStats());
                return false;
            }

            if (input.equalsIgnoreCase("h")) {
                printHelp();
                continue;
            }

            if (input.equalsIgnoreCase("s")) {
                ui.println(stats.getAllStats());
                continue;
            }

            // Process move
            String[] tokens = input.split("\\s+");
            if (tokens.length == 0) {
                ui.println(ui.red("Invalid input. Type 'h' for help."));
                continue;
            }

            String action = tokens[0].toUpperCase();

            if (action.startsWith("M")) {
                // Pawn movement
                if (tokens.length != 2) {
                    ui.println(ui.red("Move format: M <direction> (N/S/E/W/NE/NW/SE/SW)"));
                    continue;
                }

                String dir = tokens[1].toUpperCase();
                Position currentPos = board.getPlayerPosition(currentPlayerIndex);
                Position newPos = calculateNewPosition(currentPos, dir);

                if (newPos == null) {
                    ui.println(ui.red("Invalid direction. Use N/S/E/W/NE/NW/SE/SW"));
                    continue;
                }

                Position finalPos = rules.validateMove(currentPos, newPos, board, currentPlayerIndex);
                if (finalPos == null) {
                    ui.println(ui.red("Invalid move. Check for walls or board boundaries."));
                    continue;
                }

                // Execute move
                board.movePlayer(currentPlayerIndex, finalPos);
                totalMoves++;
                moveCount.put(currentPlayer, moveCount.get(currentPlayer) + 1);

                // Next player
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

            } else if (action.startsWith("W")) {
                // Wall placement
                if (tokens.length != 4) {
                    ui.println(ui.red("Wall format: W <row> <col> <H/V>"));
                    continue;
                }

                Integer row = ui.tryParseInt(tokens[1]);
                Integer col = ui.tryParseInt(tokens[2]);
                String orientation = tokens[3].toUpperCase();

                if (row == null || col == null || (!orientation.equals("H") && !orientation.equals("V"))) {
                    ui.println(ui.red("Invalid wall placement format."));
                    continue;
                }

                if (wallCounts.get(currentPlayer) <= 0) {
                    ui.println(ui.red("No walls remaining!"));
                    continue;
                }

                Wall wall = new Wall(new Position(row, col), orientation.charAt(0));

                if (!rules.canPlaceWall(wall, board)) {
                    ui.println(ui.red("Invalid wall placement. Walls cannot overlap or block all paths."));
                    continue;
                }

                // Place wall
                board.placeWall(wall);
                wallCounts.put(currentPlayer, wallCounts.get(currentPlayer) - 1);
                totalMoves++;
                moveCount.put(currentPlayer, moveCount.get(currentPlayer) + 1);

                // Next player
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

            } else {
                ui.println(ui.red("Invalid command. Use M for move or W for wall."));
            }
        }
    }

    private int selectNumberOfPlayers() {
        while (true) {
            ui.println("Number of players (2 or 4): ");
            ui.print("> ");
            String input = ui.nextLine().trim();

            if (input.equals("2") || input.equals("4")) {
                return Integer.parseInt(input);
            }

            ui.println(ui.red("Please enter 2 or 4."));
        }
    }

    private List<Player> getPlayerNames(int numPlayers) {
        List<Player> players = new ArrayList<>();
        Set<String> usedNames = new HashSet<>();

        for (int i = 1; i <= numPlayers; i++) {
            while (true) {
                String name = validator.readValidName(
                        "Player " + i + " name (default P" + i + "): ",
                        "P" + i
                );

                if (!usedNames.contains(name.toLowerCase())) {
                    players.add(new Player(name));
                    usedNames.add(name.toLowerCase());
                    break;
                }

                ui.println(ui.red("Name already taken. Please choose a different name."));
            }
        }

        return players;
    }

    private void initializePlayerPositions(QuoridorBoard board, List<Player> players) {
        if (players.size() == 2) {
            board.setPlayerPosition(0, new Position(0, 4));
            board.setPlayerPosition(1, new Position(8, 4));
        } else {
            board.setPlayerPosition(0, new Position(0, 4));
            board.setPlayerPosition(1, new Position(8, 4));
            board.setPlayerPosition(2, new Position(4, 0));
            board.setPlayerPosition(3, new Position(4, 8));
        }
    }

    private void displayGameStatus(List<Player> players, Map<Player, Integer> wallCounts, int currentIndex) {
        ui.println("\n" + ui.bold("Game Status:"));
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            String marker = (i == currentIndex) ? " ← " : "   ";
            String playerInfo = String.format("Player %d (%s): Walls: %d%s",
                    i + 1, p.getName(), wallCounts.get(p), marker);

            if (i == currentIndex) {
                ui.println(ui.cyan(playerInfo));
            } else {
                ui.println(playerInfo);
            }
        }
    }

    private Position calculateNewPosition(Position current, String direction) {
        int row = current.row;
        int col = current.col;

        switch (direction) {
            case "N":  return new Position(row - 1, col);
            case "S":  return new Position(row + 1, col);
            case "E":  return new Position(row, col + 1);
            case "W":  return new Position(row, col - 1);
            case "NE": return new Position(row - 1, col + 1);
            case "NW": return new Position(row - 1, col - 1);
            case "SE": return new Position(row + 1, col + 1);
            case "SW": return new Position(row + 1, col - 1);
            default:   return null;
        }
    }

    private void printHelp() {
        ui.println("\n" + ui.bold("=== Quoridor Help ==="));
        ui.println("OBJECTIVE: Be the first to reach the opposite side of the board.");
        ui.println("\nCOMMANDS:");
        ui.println("  M <dir>       - Move pawn (N/S/E/W/NE/NW/SE/SW)");
        ui.println("  W <r> <c> <o> - Place wall at row r, column c (o = H/V)");
        ui.println("  H             - Show this help");
        ui.println("  S             - Show statistics");
        ui.println("  Q             - Quit game");
        ui.println("\nRULES:");
        ui.println("- Pawns move one space orthogonally (not diagonally normally)");
        ui.println("- Jump over adjacent opponents if no wall blocks");
        ui.println("- Walls are 2 spaces long and block movement");
        ui.println("- Walls cannot completely block a player's path to goal");
        ui.println("- Each player starts with " + WALLS_PER_PLAYER_2 + " walls (2-player) or " +
                WALLS_PER_PLAYER_4 + " walls (4-player)");
    }
}
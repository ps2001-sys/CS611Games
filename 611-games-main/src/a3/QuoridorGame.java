package a3;

import engine.Game;
import engine.TextUI;
import common.Player;
import common.InputValidator;
import java.util.*;

/**
 * Quoridor game implementation that properly extends the Game abstract class.
 * This demonstrates proper OOP design with instance variables and inheritance.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class QuoridorGame extends Game {
    // Instance variables for game state
    private QuoridorBoard board;
    private QuoridorRules rules;
    private Map<Player, Integer> wallsRemaining;
    private InputValidator validator;

    private static final int WALLS_PER_PLAYER_2 = 10;
    private static final int WALLS_PER_PLAYER_4 = 5;

    /**
     * Create a new Quoridor game.
     * @param ui The text UI for input/output
     */
    public QuoridorGame(TextUI ui) {
        super(ui);
        this.validator = new InputValidator(ui);
        this.wallsRemaining = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Quoridor";
    }

    @Override
    protected int getNumberOfPlayers() {
        // Ask for 2 or 4 players
        while (true) {
            ui.println("Number of players (2 or 4): ");
            ui.print("> ");
            String input = ui.nextLine().trim();

            if (input.equals("2")) return 2;
            if (input.equals("4")) return 4;

            ui.println(ui.red("Please enter 2 or 4."));
        }
    }

    @Override
    protected void setupPlayers() {
        int numPlayers = getNumberOfPlayers();

        ui.println("Setting up " + numPlayers + " players for Quoridor...\n");

        Set<String> usedNames = new HashSet<>();

        for (int i = 1; i <= numPlayers; i++) {
            while (true) {
                String name = validator.readValidName(
                        "Player " + i + " name (default P" + i + "): ",
                        "P" + i
                );

                if (!usedNames.contains(name.toLowerCase())) {
                    Player player = new Player(name, i);

                    // Create and assign pawn to player
                    Pawn pawn = new Pawn(name, i);
                    player.addPiece(pawn);

                    players.add(player);
                    usedNames.add(name.toLowerCase());
                    break;
                }

                ui.println(ui.red("Name already taken. Please choose a different name."));
            }
        }
    }

    @Override
    protected void initializeGame() {
        // Create board and rules
        board = new QuoridorBoard();
        rules = new QuoridorRules(board);

        // Initialize pawns on board
        board.initializePawns(players.size());

        // Set up walls for each player
        int wallsPerPlayer = (players.size() == 2) ? WALLS_PER_PLAYER_2 : WALLS_PER_PLAYER_4;
        for (Player player : players) {
            wallsRemaining.put(player, wallsPerPlayer);
        }

        ui.println("\n" + ui.bold("Game Started!"));
        ui.println("Each player gets " + wallsPerPlayer + " walls.");
        ui.println("Goal: Be first to reach the opposite side!");
        ui.println("Type 'h' for help at any time.\n");
    }

    @Override
    protected boolean processTurn() {
        Player currentPlayer = getCurrentPlayer();
        int playerNumber = currentPlayer.getPlayerNumber();

        ui.println("\n" + ui.cyan(currentPlayer.getName() + "'s turn (Player " + playerNumber + ")"));
        ui.println("Walls remaining: " + wallsRemaining.get(currentPlayer));
        ui.println("Enter: [M]ove <dir>, [W]all <r> <c> <H/V>, [H]elp, [Q]uit");
        ui.print("> ");

        String input = ui.nextLine().trim();

        if (input.equalsIgnoreCase("q")) {
            return false;  // End game
        }

        if (input.equalsIgnoreCase("h")) {
            displayHelp();
            return true;
        }

        String[] tokens = input.split("\\s+");
        if (tokens.length == 0) {
            ui.println(ui.red("Invalid input. Type 'h' for help."));
            return true;
        }

        String action = tokens[0].toUpperCase();

        if (action.startsWith("M")) {
            return processPawnMove(tokens, currentPlayer, playerNumber);
        } else if (action.startsWith("W")) {
            return processWallPlacement(tokens, currentPlayer, playerNumber);
        } else {
            ui.println(ui.red("Invalid command. Use M for move or W for wall."));
            return true;
        }
    }

    /**
     * Process a pawn move command.
     */
    private boolean processPawnMove(String[] tokens, Player currentPlayer, int playerNumber) {
        if (tokens.length != 2) {
            ui.println(ui.red("Move format: M <direction> (N/S/E/W)"));
            return true;
        }

        String direction = tokens[1].toUpperCase();
        Position currentPos = board.getPawnPosition(playerNumber);
        Position newPos = calculateNewPosition(currentPos, direction);

        if (newPos == null) {
            ui.println(ui.red("Invalid direction. Use N/S/E/W"));
            return true;
        }

        // Validate move with rules
        Position finalPos = rules.validateMove(currentPos, newPos, board, playerNumber);
        if (finalPos == null) {
            ui.println(ui.red("Invalid move. Check for walls or board boundaries."));
            return true;
        }

        // Execute move
        board.movePawn(playerNumber, finalPos.row, finalPos.col);
        currentPlayer.addScore(1);  // Track moves

        // Check for victory
        if (rules.hasWon(finalPos, playerNumber, players.size())) {
            return true;  // Will be caught by isGameOver()
        }

        // Move to next player
        nextPlayer();
        return true;
    }

    /**
     * Process a wall placement command.
     */
    private boolean processWallPlacement(String[] tokens, Player currentPlayer, int playerNumber) {
        if (tokens.length != 4) {
            ui.println(ui.red("Wall format: W <row> <col> <H/V>"));
            return true;
        }

        Integer row = ui.tryParseInt(tokens[1]);
        Integer col = ui.tryParseInt(tokens[2]);
        String orientation = tokens[3].toUpperCase();

        if (row == null || col == null || (!orientation.equals("H") && !orientation.equals("V"))) {
            ui.println(ui.red("Invalid wall placement format."));
            return true;
        }

        if (wallsRemaining.get(currentPlayer) <= 0) {
            ui.println(ui.red("No walls remaining!"));
            return true;
        }

        // Create wall piece
        WallPiece wall = new WallPiece(new Position(row, col), orientation.charAt(0));

        // Validate wall placement
        if (!rules.canPlaceWall(wall, board)) {
            ui.println(ui.red("Invalid wall placement. Walls cannot overlap or block all paths."));
            return true;
        }

        // Place wall
        board.placeWall(wall);
        wallsRemaining.put(currentPlayer, wallsRemaining.get(currentPlayer) - 1);
        currentPlayer.addScore(1);  // Track actions

        // Move to next player
        nextPlayer();
        return true;
    }

    @Override
    protected boolean isGameOver() {
        return board.isGameOver();
    }

    @Override
    protected void displayGameState() {
        ui.println("\n" + board.render());

        // Display player status
        ui.println(ui.bold("Game Status:"));
        for (Player player : players) {
            String status = String.format("  %s (P%d): Walls: %d, Moves: %d",
                    player.getName(),
                    player.getPlayerNumber(),
                    wallsRemaining.get(player),
                    player.getScore()
            );

            if (player.isActive()) {
                ui.println(ui.cyan("→ " + status));
            } else {
                ui.println("  " + status);
            }
        }
    }

    @Override
    protected void handleGameEnd() {
        long duration = getGameDuration();

        // Find winner
        Player winner = null;
        for (Player player : players) {
            Position pos = board.getPawnPosition(player.getPlayerNumber());
            if (rules.hasWon(pos, player.getPlayerNumber(), players.size())) {
                winner = player;
                break;
            }
        }

        if (winner != null) {
            ui.println(ui.green("\n🎉 " + winner.getName() + " wins! 🎉"));
            ui.println("Victory in " + winner.getScore() + " moves!");

            // Record statistics
            for (Player player : players) {
                boolean won = player.equals(winner);
                player.recordGame(won, duration / players.size());
            }
        }

        // Display final statistics
        ui.println("\nFinal Statistics:");
        for (Player player : players) {
            ui.println(player.getStatsSummary());
        }
    }

    @Override
    protected void displayHelp() {
        ui.println("\n" + ui.bold("=== Quoridor Help ==="));
        ui.println("OBJECTIVE: Be the first to reach the opposite side of the board.");
        ui.println("\nCOMMANDS:");
        ui.println("  M <dir>       - Move pawn (N/S/E/W)");
        ui.println("  W <r> <c> <o> - Place wall at row r, column c (o = H/V)");
        ui.println("  H             - Show this help");
        ui.println("  Q             - Quit game");
        ui.println("\nRULES:");
        ui.println("- Pawns move one space orthogonally");
        ui.println("- Jump over adjacent opponents");
        ui.println("- Walls block movement (2 spaces long)");
        ui.println("- Cannot completely block a player's path");
        ui.println("- Each player starts with " + WALLS_PER_PLAYER_2 + " walls (2-player) or " +
                WALLS_PER_PLAYER_4 + " walls (4-player)");
        ui.println("\nVICTORY CONDITIONS:");
        if (players.size() == 2) {
            ui.println("- Player 1 (starts North): Reach South edge");
            ui.println("- Player 2 (starts South): Reach North edge");
        } else {
            ui.println("- Player 1 (North): Reach South edge");
            ui.println("- Player 2 (South): Reach North edge");
            ui.println("- Player 3 (West): Reach East edge");
            ui.println("- Player 4 (East): Reach West edge");
        }
    }

    /**
     * Calculate new position based on direction.
     */
    private Position calculateNewPosition(Position current, String direction) {
        if (current == null) return null;

        switch (direction) {
            case "N":  return new Position(current.row - 1, current.col);
            case "S":  return new Position(current.row + 1, current.col);
            case "E":  return new Position(current.row, current.col + 1);
            case "W":  return new Position(current.row, current.col - 1);
            default:   return null;
        }
    }
}
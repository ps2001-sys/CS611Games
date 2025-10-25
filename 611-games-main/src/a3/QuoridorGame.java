package a3;

import engine.Game;
import engine.TextUI;
import common.Player;
import common.InputValidator;

import java.util.*;

import common.Statistics;

/**
 * Quoridor game implementation with a custom turn loop.
 * This class overrides start() so that we only rotate turns
 * when a player completes a valid action (move or wall placement).
 * Invalid input / invalid wall / invalid move => same player continues.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-10-25
 */
public class QuoridorGame extends Game {
    // Core state
    private QuoridorBoard board;
    private QuoridorRules rules;
    private final Map<Player, Integer> wallsRemaining = new HashMap<>();
    private final InputValidator validator;
    private Player winner = null;

    // Optional: for global statistics aggregation
    private final Statistics stats;

    private static final int WALLS_PER_PLAYER_2 = 10;
    private static final int WALLS_PER_PLAYER_4 = 5;

    public QuoridorGame(TextUI ui, Statistics stats) {
        super(ui);
        this.validator = new InputValidator(ui);
        this.stats = stats;
    }

    @Override
    public String getName() {
        return "Quoridor";
    }

    @Override
    protected int getNumberOfPlayers() {
        // Ask user to choose 2 or 4 players
        while (true) {
            ui.println("Number of players (2 or 4): ");
            ui.print("> ");
            String input = ui.nextLine().trim();
            if ("2".equals(input)) return 2;
            if ("4".equals(input)) return 4;
            ui.println(ui.red("Please enter 2 or 4."));
        }
    }

    @Override
    protected void setupPlayers() {
        int numPlayers = getNumberOfPlayers();
        ui.println("Setting up " + numPlayers + " players for Quoridor...\n");

        Set<String> used = new HashSet<>();
        for (int i = 1; i <= numPlayers; i++) {
            while (true) {
                String name = validator.readValidName(
                        "Player " + i + " name (default P" + i + "): ", "P" + i);
                String key = name.toLowerCase(Locale.ROOT);
                if (!used.contains(key)) {
                    Player p = new Player(name, i);
                    // Give each player a pawn piece
                    Pawn pawn = new Pawn(name, i);
                    p.addPiece(pawn);
                    players.add(p);
                    used.add(key);
                    break;
                }
                ui.println(ui.red("Name already taken. Please choose a different name."));
            }
        }
    }

    @Override
    protected void initializeGame() {
        board = new QuoridorBoard();
        rules = new QuoridorRules(board);

        // Place pawns according to player count
        board.initializePawns(players.size());

        // Initialize wall budgets
        int per = (players.size() == 2) ? WALLS_PER_PLAYER_2 : WALLS_PER_PLAYER_4;
        for (Player p : players) {
            wallsRemaining.put(p, per);
        }

        ui.println("\n" + ui.bold("Game Started!"));
        ui.println("Each player gets " + per + " walls.");
        ui.println("Goal: Be first to reach the opposite side!");
        ui.println("Type 'h' for help at any time.\n");
    }

    // ---------- CUSTOM LOOP (does NOT affect A1/A2) ----------

    @Override
    public void start() {
        ui.println(ui.bold("=== " + getName() + " ==="));

        // players
        setupPlayers();
        // board/rules
        initializeGame();

        gameActive = true;
        gameStartTime = System.nanoTime();

        while (gameActive && !isGameOver()) {
            displayGameState();

            // mark active
            Player current = getCurrentPlayer();
            current.setActive(true);
            TurnResult res = takeTurn(current);
            current.setActive(false);

            if (res.quit) {
                gameActive = false;
                break;
            }

            // Only rotate on success
            if (res.success) {
                totalMoves++;

                // Check if the current player has reached the finish line (immediately determine victory)
                Position pos = board.getPawnPosition(current.getPlayerNumber());
                if (rules.hasWon(pos, current.getPlayerNumber() - 1, players.size())) {
                    gameActive = false;
                    ui.println(ui.green("\n " + current.getName() + " wins! "));
                    ui.println("Victory in " + current.getScore() + " actions!");
                    // record for handlegameend
                    winner = current;
                    break;
                }

                // 
                nextPlayer();
            }
            // else: same player goes again
        }

        handleGameEnd();
    }

    // A small struct to carry the result of a turn
    private static class TurnResult {
        final boolean success; // valid action performed (move or wall)
        final boolean quit;    // player requested to quit
        TurnResult(boolean success, boolean quit) {
            this.success = success;
            this.quit = quit;
        }
        static TurnResult success() { return new TurnResult(true, false); }
        static TurnResult retry()   { return new TurnResult(false, false); }
        static TurnResult quit()    { return new TurnResult(false, true); }
    }

    // Parse & execute exactly one command from the current player.
    private TurnResult takeTurn(Player currentPlayer) {
        int n = currentPlayer.getPlayerNumber();

        ui.println("\n" + ui.cyan(currentPlayer.getName() + "'s turn (Player " + n + ")"));
        ui.println("Walls remaining: " + wallsRemaining.getOrDefault(currentPlayer, 0));
        ui.println("Enter: [M]ove <dir>, [W]all <r> <c> <H/V>, [H]elp, [Q]uit");
        ui.print("> ");

        String raw = ui.nextLine().trim();
        if (raw.isEmpty()) return TurnResult.retry();

        if (raw.equalsIgnoreCase("q")) {
            return TurnResult.quit();
        }
        if (raw.equalsIgnoreCase("h")) {
            displayHelp();
            return TurnResult.retry();
        }

        String[] tok = raw.split("\\s+");
        String action = tok[0].toUpperCase(Locale.ROOT);

        if (action.startsWith("M")) {
            // Move: M N|S|E|W
            if (tok.length != 2) {
                ui.println(ui.red("Move format: M <direction> (N/S/E/W)"));
                return TurnResult.retry();
            }
            String dir = tok[1].toUpperCase(Locale.ROOT);
            Position cur = board.getPawnPosition(n);
            Position target = calculateNewPosition(cur, dir);
            if (target == null) {
                ui.println(ui.red("Invalid direction. Use N/S/E/W."));
                return TurnResult.retry();
            }

            Position finalPos = rules.validateMove(cur, target, board, n);
            if (finalPos == null) {
                ui.println(ui.red("Invalid move. Check for walls, bounds, or jump rules."));
                return TurnResult.retry();
            }

            board.movePawn(n, finalPos.row, finalPos.col);
            currentPlayer.addScore(1); // count actions/moves if you like
            return TurnResult.success();
        }

        if (action.startsWith("W")) {
            // Wall: W <row> <col> <H/V>
            if (tok.length != 4) {
                ui.println(ui.red("Wall format: W <row> <col> <H/V>"));
                return TurnResult.retry();
            }

            Integer r = ui.tryParseInt(tok[1]);
            Integer c = ui.tryParseInt(tok[2]);
            if (r == null || c == null) {
                ui.println(ui.red("Row/Col must be numbers."));
                return TurnResult.retry();
            }
            char ori = Character.toUpperCase(tok[3].charAt(0));
            if (ori != 'H' && ori != 'V') {
                ui.println(ui.red("Orientation must be H or V."));
                return TurnResult.retry();
            }

            int left = wallsRemaining.getOrDefault(currentPlayer, 0);
            if (left <= 0) {
                ui.println(ui.red("No walls remaining!"));
                return TurnResult.retry();
            }

            WallPiece wall = new WallPiece(new Position(r, c), ori, n);

            if (!rules.canPlaceWall(wall, board)) {
                ui.println(ui.red("Invalid wall placement. Walls cannot overlap or block all paths."));
                return TurnResult.retry();
            }

            board.placeWall(wall);
            wallsRemaining.put(currentPlayer, left - 1);
            currentPlayer.addScore(1); // count actions if desired
            return TurnResult.success();
        }

        ui.println(ui.red("Invalid command. Use M for move or W for wall (or H/Q)."));
        return TurnResult.retry();
    }

    // We still implement abstract methods, but in our custom loop we don't rely on them.
    @Override
    protected boolean processTurn() {
        // Not used by the custom loop; keep for abstract contract.
        return true;
    }

    @Override
    protected boolean isGameOver() {
        return board.isGameOver();
    }

    @Override
    protected void displayGameState() {
        // Prefer colored render if available
        try {
            ui.println("\n" + board.render(ui));
        } catch (Throwable t) {
            // fallback to non-colored version if board doesn't support TextUI
            ui.println("\n" + board.render());
        }

        // Status line
        ui.println(ui.bold("Game Status:"));
        for (Player p : players) {
            String status = String.format("  %s (P%d): Walls: %d, Moves: %d",
                    p.getName(),
                    p.getPlayerNumber(),
                    wallsRemaining.getOrDefault(p, 0),
                    p.getScore());
            if (p.isActive()) {
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
        for (Player p : players) {
            Position pos = board.getPawnPosition(p.getPlayerNumber());
            if (rules.hasWon(pos, p.getPlayerNumber() - 1, players.size())) {
                winner = p;
                break;
            }
        }

        if (winner != null) {
            ui.println(ui.green("\n " + winner.getName() + " wins! "));
            ui.println("Victory in " + winner.getScore() + " actions!");
        } else {
            ui.println(ui.yellow("\nGame ended."));
        }

        // Unified record statistics
        for (Player p : players) {
            boolean won = (winner != null && p.equals(winner));

            // Use the full duration and do not divide by the number of players
            p.recordGame(won, duration);
            if (stats != null) {
                stats.recordGame(p.getName(), won, p.getScore(), duration);
            }
        }

        // Show final summaries
        ui.println("\nFinal Statistics:");
        for (Player p : players) {
            ui.println(p.getStatsSummary());
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
        ui.println("- Jump over adjacent opponents; if blocked, move diagonally as per rules");
        ui.println("- Walls are 2 segments long; cannot overlap or fully block all paths");
        ui.println("- Each player starts with " + WALLS_PER_PLAYER_2 + " walls (2 players) or " +
                WALLS_PER_PLAYER_4 + " walls (4 players)");
        if (players.size() == 2) {
            ui.println("\nVICTORY:");
            ui.println("- P1 (North) reaches South edge");
            ui.println("- P2 (South) reaches North edge");
        } else {
            ui.println("\nVICTORY (4 players):");
            ui.println("- P1 (North) reaches South edge");
            ui.println("- P2 (South) reaches North edge");
            ui.println("- P3 (West)  reaches East edge");
            ui.println("- P4 (East)  reaches West edge");
        }
    }

    // Helpers
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

package engine;

import common.Player;
import java.util.List;
import java.util.ArrayList;

/**
 * Abstract base class for all games in the CS611 Game Suite.
 * Changed from interface to abstract class to provide common functionality.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public abstract class Game {
    protected TextUI ui;
    protected List<Player> players;
    protected int currentPlayerIndex;
    protected boolean gameActive;
    protected long gameStartTime;
    protected int totalMoves;

    /**
     * Constructor for creating a new game.
     * @param ui The text UI for input/output
     */
    public Game(TextUI ui) {
        this.ui = ui;
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameActive = false;
        this.totalMoves = 0;
    }

    /**
     * Get the display name of this game.
     * Must be implemented by each game.
     * @return Game name
     */
    public abstract String getName();

    /**
     * Initialize the game-specific components.
     * Called before starting the game loop.
     */
    protected abstract void initializeGame();

    /**
     * Process a single game turn.
     * @return true if the game should continue, false if it should end
     */
    protected abstract boolean processTurn();

    /**
     * Check if the game has ended.
     * @return true if the game is over, false otherwise
     */
    protected abstract boolean isGameOver();

    /**
     * Display the current game state.
     */
    protected abstract void displayGameState();

    /**
     * Handle the end of the game (winner announcement, stats, etc.).
     */
    protected abstract void handleGameEnd();

    /**
     * Start the game loop.
     * This is the main entry point for each game.
     */
    public void start() {
        ui.println(ui.bold("=== " + getName() + " ==="));

        // Setup players
        setupPlayers();

        // Initialize game-specific components
        initializeGame();

        // Main game loop
        gameActive = true;
        gameStartTime = System.nanoTime();

        while (gameActive && !isGameOver()) {
            displayGameState();

            // Process current player's turn
            getCurrentPlayer().setActive(true);
            boolean continueGame = processTurn();
            getCurrentPlayer().setActive(false);

            if (!continueGame) {
                gameActive = false;
                break;
            }

            totalMoves++;
            nextPlayer();
        }

        // Handle game end
        handleGameEnd();
    }

    /**
     * Set up players for the game.
     * Can be overridden by games that need custom player setup.
     */
    protected void setupPlayers() {
        int numPlayers = getNumberOfPlayers();

        for (int i = 1; i <= numPlayers; i++) {
            ui.print("Enter name for Player " + i + " (or press Enter for default): ");
            String name = ui.nextLine().trim();
            if (name.isEmpty()) {
                name = "Player " + i;
            }
            Player player = new Player(name, i);
            players.add(player);
        }
    }

    /**
     * Get the number of players for this game.
     * Can be overridden by games with variable player counts.
     * @return Number of players
     */
    protected int getNumberOfPlayers() {
        return 2;  // Default to 2 players
    }

    /**
     * Get the current player.
     * @return Current player
     */
    protected Player getCurrentPlayer() {
        if (players.isEmpty()) return null;
        return players.get(currentPlayerIndex);
    }

    /**
     * Move to the next player.
     */
    protected void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Get all players in the game.
     * @return List of players
     */
    protected List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Calculate game duration in milliseconds.
     * @return Game duration
     */
    protected long getGameDuration() {
        return (System.nanoTime() - gameStartTime) / 1_000_000;
    }

    /**
     * Display help information for the game.
     * Can be overridden by each game to provide specific help.
     */
    protected void displayHelp() {
        ui.println("Help for " + getName());
        ui.println("This is a turn-based game.");
        ui.println("Type 'q' to quit at any time.");
    }

    /**
     * Reset the game to initial state.
     * Can be overridden by games that support reset.
     */
    protected void resetGame() {
        currentPlayerIndex = 0;
        totalMoves = 0;
        gameStartTime = System.nanoTime();
        for (Player player : players) {
            player.resetScore();
        }
        initializeGame();
    }
}
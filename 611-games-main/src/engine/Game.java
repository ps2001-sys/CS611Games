package engine;

/**
 * Common interface for all games in the project.
 * Each game must implement getName() and start() methods.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public interface Game {
    /**
     * Get the display name of this game.
     */
    String getName();

    /**
     * Start the game loop.
     * Handles all game logic until the user exits.
     */
    void start();
}
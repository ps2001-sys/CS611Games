package common;

import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced Player class that represents a player in any game.
 * Stores player information, game state, and statistics.
 *
 * This class provides comprehensive player management functionality
 * that can be used across all games in the suite.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class Player {
    private final String name;
    private int score;
    private int wins;
    private int losses;
    private int gamesPlayed;
    private long totalPlayTime;
    private List<Piece> pieces;  // Pieces owned by this player
    private boolean isActive;    // Whether it's currently this player's turn
    private int playerNumber;    // Player number (1, 2, 3, 4, etc.)

    /**
     * Constructor for creating a new player.
     * @param name The player's name
     */
    public Player(String name) {
        this.name = validateName(name);
        this.score = 0;
        this.wins = 0;
        this.losses = 0;
        this.gamesPlayed = 0;
        this.totalPlayTime = 0;
        this.pieces = new ArrayList<>();
        this.isActive = false;
        this.playerNumber = 0;
    }

    /**
     * Constructor with player number.
     * @param name The player's name
     * @param playerNumber The player's number
     */
    public Player(String name, int playerNumber) {
        this(name);
        this.playerNumber = playerNumber;
    }

    /**
     * Validate and clean the player name.
     * @param name Raw name input
     * @return Cleaned name
     */
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Player";
        }
        String cleaned = name.trim();
        if (cleaned.length() > 15) {
            cleaned = cleaned.substring(0, 15);
        }
        return cleaned;
    }

    /**
     * Get the player's name.
     * @return Player name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the player's current score.
     * @return Current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Add to the player's score.
     * @param points Points to add
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Reset the player's score to zero.
     */
    public void resetScore() {
        this.score = 0;
    }

    /**
     * Record a game result for this player.
     * @param won Whether the player won
     * @param timeMs Time taken in milliseconds
     */
    public void recordGame(boolean won, long timeMs) {
        gamesPlayed++;
        totalPlayTime += timeMs;
        if (won) {
            wins++;
        } else {
            losses++;
        }
    }

    /**
     * Get the player's win rate.
     * @return Win rate as a percentage (0-100)
     */
    public double getWinRate() {
        if (gamesPlayed == 0) return 0.0;
        return (double) wins / gamesPlayed * 100.0;
    }

    /**
     * Get average play time per game.
     * @return Average time in milliseconds
     */
    public long getAveragePlayTime() {
        if (gamesPlayed == 0) return 0;
        return totalPlayTime / gamesPlayed;
    }

    /**
     * Add a piece to this player's collection.
     * @param piece The piece to add
     */
    public void addPiece(Piece piece) {
        if (piece != null) {
            piece.setOwner(this.name);
            pieces.add(piece);
        }
    }

    /**
     * Remove a piece from this player's collection.
     * @param piece The piece to remove
     */
    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    /**
     * Get all pieces owned by this player.
     * @return List of pieces
     */
    public List<Piece> getPieces() {
        return new ArrayList<>(pieces);
    }

    /**
     * Clear all pieces from this player.
     */
    public void clearPieces() {
        pieces.clear();
    }

    /**
     * Check if it's this player's turn.
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Set whether it's this player's turn.
     * @param active true if it's this player's turn
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Get the player number.
     * @return Player number
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Set the player number.
     * @param number New player number
     */
    public void setPlayerNumber(int number) {
        this.playerNumber = number;
    }

    /**
     * Get a statistics summary for this player.
     * @return Statistics string
     */
    public String getStatsSummary() {
        return String.format("%s - Games: %d, Wins: %d, Win Rate: %.1f%%, Avg Time: %.2fs",
                name, gamesPlayed, wins, getWinRate(), getAveragePlayTime() / 1000.0);
    }

    /**
     * Reset all statistics for this player.
     */
    public void resetStats() {
        score = 0;
        wins = 0;
        losses = 0;
        gamesPlayed = 0;
        totalPlayTime = 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
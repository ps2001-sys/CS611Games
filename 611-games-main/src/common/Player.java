package common;

import java.util.ArrayList;
import java.util.List;

/**
 * Player class — captures info about someone playing the game.
 * Keeps track of scores, wins/losses, pieces they own, and all that jazz.
 *
 * Feel free to add more stats if you want,
 * or make it fancier for your own game use cases!
 *
 * Created by Zhuojun Lyu & Priyanshu Singh, 2025-01-05
 */
public class Player {

    private final String name;
    private int score;
    private int wins;
    private int losses;
    private int gamesPlayed;
    private long totalPlayTime;    // in milliseconds

    private List<Piece> pieces;    // Pieces owned by this player
    private boolean isActive;      // True if player’s turn right now
    private int playerNumber;      // e.g. Player 1, Player 2...

    /**
     * Create a player with just a name.
     * If name is empty or too long, it gets cleaned up.
     *
     * @param name the player's chosen name
     */
    public Player(String name) {
        this.name = cleanName(name);
        score = 0;
        wins = 0;
        losses = 0;
        gamesPlayed = 0;
        totalPlayTime = 0;
        pieces = new ArrayList<>();
        isActive = false;
        playerNumber = 0;  // default, can be set later
    }

    /**
     * Same as above but also assign a player number
     */
    public Player(String name, int playerNumber) {
        this(name);
        this.playerNumber = playerNumber;
    }

    /**
     * Make sure the name is reasonable:
     * no empty strings, no long nonsense
     */
    private String cleanName(String rawName) {
        if (rawName == null || rawName.trim().isEmpty()) {
            return "Player";  // fallback name
        }
        String trimmed = rawName.trim();
        if (trimmed.length() > 15) {
            trimmed = trimmed.substring(0, 15);
        }
        return trimmed;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    /**
     * Bumps the player's score by the given points.
     * Negative is allowed if you want to deduct.
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Resets score back to zero,
     * usually at start of a new game or round.
     */
    public void resetScore() {
        score = 0;
    }

    /**
     * Call this after a game finishes to update stats.
     * @param won Did the player win or lose?
     * @param timeMs How long they spent playing (in ms)
     */
    public void recordGame(boolean won, long timeMs) {
        gamesPlayed++;
        totalPlayTime += timeMs;
        if (won) wins++;
        else losses++;
    }

    /**
     * Returns win rate between 0 and 100%.
     * If no games played yet, returns 0.
     */
    public double getWinRate() {
        if (gamesPlayed == 0) return 0.0;
        return (wins * 100.0) / gamesPlayed;
    }

    /**
     * Average playtime per game in milliseconds.
     * Returns 0 if no games played.
     */
    public long getAveragePlayTime() {
        if (gamesPlayed == 0) return 0;
        return totalPlayTime / gamesPlayed;
    }

    /**
     * Adds a piece to the player’s stash.
     * Also sets the piece’s owner field.
     */
    public void addPiece(Piece piece) {
        if (piece == null) return;
        piece.setOwner(this.name);
        pieces.add(piece);
    }

    /**
     * Removes a piece from the player.
     * If piece isn’t owned, nothing happens.
     */
    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    /**
     * Returns a copy of the player's pieces list.
     * So external callers can’t modify internal list directly.
     */
    public List<Piece> getPieces() {
        return new ArrayList<>(pieces);
    }

    /**
     * Clears all pieces from the player.
     */
    public void clearPieces() {
        pieces.clear();
    }

    /**
     * Are we up? Checks if it’s this player's turn.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Set whether this player is active (their turn).
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int num) {
        playerNumber = num;
    }

    /**
     * Shows a quick summary of this player's stats.
     * Example: "Alice - Games: 10, Wins: 7, Win Rate: 70.0%, Avg Time: 55.23s"
     */
    public String getStatsSummary() {
        return String.format("%s - Games: %d, Wins: %d, Win Rate: %.1f%%, Avg Time: %.2fs",
                name, gamesPlayed, wins, getWinRate(), getAveragePlayTime() / 1000.0);
    }

    /**
     * Wipes all statistics clean, score & gameplay history reset.
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
        if (!(obj instanceof Player)) return false;
        Player other = (Player) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

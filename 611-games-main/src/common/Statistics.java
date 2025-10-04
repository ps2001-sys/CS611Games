package common;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of game stats for each player.
 */
public class Statistics {

    // Store player stats keyed by player name
    private final Map<String, PlayerStats> stats = new HashMap<>();

    /**
     * Saves the result of a finished game.
     *
     * @param playerName Name of the player
     * @param won Did the player win?
     * @param moves How many moves were made
     * @param timeMs Time taken (in milliseconds)
     */
    public void recordGame(String playerName, boolean won, int moves, long timeMs) {
        PlayerStats playerStats = stats.get(playerName);
        if (playerStats == null) {
            playerStats = new PlayerStats();
            stats.put(playerName, playerStats);
        }

        playerStats.gamesPlayed++;
        if (won) {
            playerStats.gamesWon++;
            playerStats.totalMoves += moves;
            playerStats.totalTimeMs += timeMs;
        }
    }

    /**
     * Get a summary of this player's stats.
     *
     * @param playerName Name of the player
     * @return A simple report on the player's games
     */
    public String getStats(String playerName) {
        PlayerStats playerStats = stats.get(playerName);
        if (playerStats == null) {
            return playerName + ": No games played yet.";
        }

        double avgMoves = playerStats.gamesWon == 0 ? 0 : (double) playerStats.totalMoves / playerStats.gamesWon;
        double avgTimeSec = playerStats.gamesWon == 0 ? 0 : (double) playerStats.totalTimeMs / playerStats.gamesWon / 1000.0;

        return String.format(
                "%s's stats: Games played: %d, Games won: %d, Average moves: %.2f, Average time: %.2fs",
                playerName,
                playerStats.gamesPlayed,
                playerStats.gamesWon,
                avgMoves,
                avgTimeSec);
    }

    /**
     * Get stats of all players in a readable format.
     *
     * @return Summary of all players' stats
     */
    public String getAllStats() {
        if (stats.isEmpty()) {
            return "No statistics recorded yet.";
        }
        StringBuilder allStats = new StringBuilder("All player stats:\n");
        for (Map.Entry<String, PlayerStats> entry : stats.entrySet()) {
            allStats.append(getStats(entry.getKey())).append("\n");
        }
        return allStats.toString();
    }

    // Internal class storing stats for a single player
    private static class PlayerStats {
        int gamesPlayed = 0;
        int gamesWon = 0;
        int totalMoves = 0;
        long totalTimeMs = 0;
    }
}

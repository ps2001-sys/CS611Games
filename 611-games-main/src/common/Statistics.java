package common;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks game statistics per player.
 */
public class Statistics {

    // Maps player names to their statistics
    private final Map<String, PlayerStats> stats = new HashMap<>();

    /**
     * Records a finished game for the given player.
     * @param playerName The name of the player.
     * @param won Whether the player has won the game.
     * @param moves The number of moves made in the game.
     * @param timeMs The duration of the game in milliseconds.
     */
    public void recordGame(String playerName, boolean won, int moves, long timeMs) {
        PlayerStats pStats = stats.getOrDefault(playerName, new PlayerStats());
        pStats.gamesPlayed++;
        if (won) {
            pStats.gamesWon++;
            pStats.totalMoves += moves;
            pStats.totalTimeMs += timeMs;
        }
        stats.put(playerName, pStats);
    }

    /**
     * Returns statistics summary string for a specific player.
     * @param playerName The name of the player.
     * @return Summary string of the player's statistics.
     */
    public String getStats(String playerName) {
        PlayerStats pStats = stats.get(playerName);
        if (pStats == null) {
            return playerName + ": No stats recorded yet.";
        }

        double avgMoves = pStats.gamesWon == 0 ? 0.0 : (double) pStats.totalMoves / pStats.gamesWon;
        double avgTimeSec = pStats.gamesWon == 0 ? 0.0 : pStats.totalTimeMs / (pStats.gamesWon * 1000.0);

        return String.format(
                "%s stats - Games Played: %d, Games Won: %d, Avg Moves: %.2f, Avg Time: %.2fs",
                playerName, pStats.gamesPlayed, pStats.gamesWon, avgMoves, avgTimeSec
        );
    }

    /**
     * Returns statistics summary string for all players.
     * @return Summary string of all players' statistics.
     */
    public String getAllStats() {
        if (stats.isEmpty()) {
            return "No statistics available.";
        }

        StringBuilder sb = new StringBuilder("All player statistics:\n");
        for (Map.Entry<String, PlayerStats> entry : stats.entrySet()) {
            sb.append(getStats(entry.getKey())).append("\n");
        }
        return sb.toString();
    }

    /**
     * Internal class to hold stats per player.
     */
    private static class PlayerStats {
        int gamesPlayed = 0;
        int gamesWon = 0;
        int totalMoves = 0;
        long totalTimeMs = 0;
    }
}

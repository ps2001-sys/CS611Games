package common;

import java.util.HashMap;
import java.util.Map;

/**
 * Hey there! This keeps score for players over a bunch of games.
 * We track wins, moves, and how long they took each time.
 *
 * Written by: Zhuojun Lyu & Priyanshu Singh
 * When: 2025-01-05
 */
public class Statistics {

    // Map to hold each player's stats — easy to get and update.
    private final Map<String, PlayerStats> playerStatsMap = new HashMap<>();

    /**
     * Call this whenever a player finishes a game.
     * We'll update their stats accordingly.
     *
     * @param playerName - who played
     * @param won - did they win?
     * @param moves - how many moves did they make?
     * @param timeMs - how long did it take in milliseconds?
     */
    public void addGameResult(String playerName, boolean won, int moves, long timeMs) {
        PlayerStats stats = playerStatsMap.computeIfAbsent(playerName, k -> new PlayerStats());

        // One more game for this player!
        stats.gamesPlayed++;

        // If they won, give ‘em credit!
        if (won) {
            stats.wins++;
        }

        // Add up moves and time for averages later
        stats.totalMoves += moves;
        stats.totalTimeMs += timeMs;
    }

    /**
     * This is your go-to for seeing how a player is doing.
     * Pretty straightforward summary.
     *
     * @param playerName - the player you want info on
     * @return a nice, easy-to-read stats summary
     */
    public String getPlayerStats(String playerName) {
        PlayerStats stats = playerStatsMap.get(playerName);

        if (stats == null || stats.gamesPlayed == 0) {
            return playerName + " hasn’t hit the leaderboard yet — no games played.";
        }

        double avgMoves = (double) stats.totalMoves / stats.gamesPlayed;
        double avgTime = (double) stats.totalTimeMs / stats.gamesPlayed;

        String gameWord = stats.gamesPlayed > 1 ? "games" : "game";

        return String.format(
                "%s played %d %s, winning %d times. Average moves: %.1f, average time: %.0f ms.",
                playerName, stats.gamesPlayed, gameWord, stats.wins, avgMoves, avgTime
        );
    }

    /**
     * Want to see how *everyone* is doing? This spits out everyone's stats.
     *
     * @return all players’ summaries, nicely formatted
     */
    public String getAllPlayerStats() {
        if (playerStatsMap.isEmpty()) {
            return "Nothing to show here yet — no games recorded!";
        }

        StringBuilder report = new StringBuilder("=== All Player Stats ===\n");

        for (String playerName : playerStatsMap.keySet()) {
            report.append(getPlayerStats(playerName)).append("\n");
        }

        return report.toString();
    }

    // This little helper class just holds stats for one player.
    private static class PlayerStats {
        int gamesPlayed = 0;
        int wins = 0;
        int totalMoves = 0;
        long totalTimeMs = 0;
    }
}

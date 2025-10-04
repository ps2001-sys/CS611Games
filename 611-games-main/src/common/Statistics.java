package common;

import java.util.HashMap;
import java.util.Map;

/**
 * Statistics tracker for player performance across multiple games.
 * Tracks wins, losses, total moves, and total time for each player.
 *
 * Author: Zhuojun Lyu & Priyanshu Singh
 * Date: 2025-01-05
 */
public class Statistics {
    private final Map<String, PlayerStats> stats = new HashMap<>();

    /**
     * Record a game result for a player
     */
    public void recordGame(String playerName, boolean won, int moves, long timeMs) {
        PlayerStats ps = stats.computeIfAbsent(playerName, k -> new PlayerStats());
        ps.gamesPlayed++;
        if (won) ps.wins++;
        ps.totalMoves += moves;
        ps.totalTime += timeMs;
    }

    /**
     * Get statistics for a specific player
     */
    public String getStats(String playerName) {
        PlayerStats ps = stats.get(playerName);
        if (ps == null || ps.gamesPlayed == 0) {
            return playerName + ": No games played yet.";
        }
        double avgMoves = (double) ps.totalMoves / ps.gamesPlayed;
        double avgTime = (double) ps.totalTime / ps.gamesPlayed;
        return String.format("%s: Games=%d, Wins=%d, Avg Moves=%.1f, Avg Time=%.0fms",
                playerName, ps.gamesPlayed, ps.wins, avgMoves, avgTime);
    }

    /**
     * Get all player statistics
     */
    public String getAllStats() {
        if (stats.isEmpty()) return "No statistics available.";
        StringBuilder sb = new StringBuilder("=== Player Statistics ===\n");
        for (String name : stats.keySet()) {
            sb.append(getStats(name)).append("\n");
        }
        return sb.toString();
    }

    private static class PlayerStats {
        int gamesPlayed = 0;
        int wins = 0;
        int totalMoves = 0;
        long totalTime = 0;
    }
}
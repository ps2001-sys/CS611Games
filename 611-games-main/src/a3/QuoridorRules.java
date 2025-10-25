package a3;

import engine.TextUI;
import java.util.*;

/**
 * Implements the rules and validation logic for Quoridor.
 * Updated to support diagonal jump decision when blocked by wall.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-10-25
 */
public class QuoridorRules {
    private final QuoridorBoard board;
    private final TextUI ui = new TextUI(); // for player interaction

    public QuoridorRules(QuoridorBoard board) {
        this.board = board;
    }

    /**
     * Validate a move from one position to another.
     */
    public Position validateMove(Position from, Position to, QuoridorBoard board, int playerIndex) {
        if (!isWithinBounds(to)) return null;

        int rowDiff = Math.abs(to.row - from.row);
        int colDiff = Math.abs(to.col - from.col);

        // ----- Normal one-step orthogonal move -----
        if (rowDiff + colDiff == 1) {
            if (board.isWallBlocking(from, to)) return null;
            if (board.isOccupied(to.row, to.col)) return calculateJumpPosition(from, to, board);
            return to;
        }

        // ----- Jump (two steps) -----
        if ((rowDiff == 2 && colDiff == 0) || (rowDiff == 0 && colDiff == 2)) {
            Position middle = new Position((from.row + to.row) / 2, (from.col + to.col) / 2);
            if (!board.isOccupied(middle.row, middle.col)) return null;
            if (board.isWallBlocking(from, middle) || board.isWallBlocking(middle, to)) return null;
            if (board.isOccupied(to.row, to.col)) return null;
            return to;
        }

        // ----- Diagonal move -----
        if (rowDiff == 1 && colDiff == 1) {
            // Check horizontal-first and vertical-first diagonal logic
            Position horizontalFirst = new Position(from.row, to.col);
            Position verticalFirst = new Position(to.row, from.col);

            if (isDiagonalValid(from, to, horizontalFirst, board)) return to;
            if (isDiagonalValid(from, to, verticalFirst, board)) return to;
        }

        return null;
    }

    /**
     * Calculate jump position, allowing diagonal choice if front is blocked.
     */
    private Position calculateJumpPosition(Position from, Position to, QuoridorBoard board) {
        int rowDir = to.row - from.row;
        int colDir = to.col - from.col;

        // Straight jump position
        Position straightJump = new Position(to.row + rowDir, to.col + colDir);

        // If straight jump possible → allow it
        if (isWithinBounds(straightJump)
                && !board.isWallBlocking(to, straightJump)
                && !board.isOccupied(straightJump.row, straightJump.col)) {
            return straightJump;
        }

        // Straight jump blocked → try diagonal
        List<Position> diagonalOptions = new ArrayList<>();

        if (rowDir != 0) { // Moving vertically
            Position leftDiag = new Position(to.row, to.col - 1);
            Position rightDiag = new Position(to.row, to.col + 1);
            if (isValidDiagonalOption(to, leftDiag, board)) diagonalOptions.add(leftDiag);
            if (isValidDiagonalOption(to, rightDiag, board)) diagonalOptions.add(rightDiag);
        } else { // Moving horizontally
            Position upDiag = new Position(to.row - 1, to.col);
            Position downDiag = new Position(to.row + 1, to.col);
            if (isValidDiagonalOption(to, upDiag, board)) diagonalOptions.add(upDiag);
            if (isValidDiagonalOption(to, downDiag, board)) diagonalOptions.add(downDiag);
        }

        // If one diagonal is available → use it
        if (diagonalOptions.size() == 1) return diagonalOptions.get(0);

        // If both available → ask player
        if (diagonalOptions.size() == 2) {
            ui.println("\nDiagonal jump possible: Left or Right?");
            ui.println("Enter L for Left or R for Right:");
            ui.print("> ");
            String choice = ui.nextLine().trim().toUpperCase();
            if (choice.startsWith("L")) return diagonalOptions.get(0);
            return diagonalOptions.get(1);
        }

        return null; // No valid jump
    }

    /**
     * Helper to check if a diagonal path is valid.
     */
    private boolean isValidDiagonalOption(Position from, Position to, QuoridorBoard board) {
        return isWithinBounds(to)
                && !board.isWallBlocking(from, to)
                && !board.isOccupied(to.row, to.col);
    }

    /**
     * Helper for diagonal validation (horizontal-first or vertical-first path).
     */
    private boolean isDiagonalValid(Position from, Position to, Position middle, QuoridorBoard board) {
        return board.isOccupied(middle.row, middle.col)
                && !board.isWallBlocking(from, middle)
                && !board.isWallBlocking(middle, to)
                && !board.isOccupied(to.row, to.col);
    }

    /**
     * Check if wall placement is valid (no overlap, in bounds, doesn’t block paths).
     */
    public boolean canPlaceWall(WallPiece wall, QuoridorBoard board) {
        int row = wall.getPosition().row;
        int col = wall.getPosition().col;

        if (row < 0 || row >= board.getSize() - 1 || col < 0 || col >= board.getSize() - 1) return false;

        for (WallPiece existing : board.getWalls()) {
            if (wall.overlaps(existing)) return false;
        }

        QuoridorBoard tempBoard = board.copy();
        tempBoard.placeWall(wall);

        for (int i = 0; i < getNumberOfPlayers(board); i++) {
            Position pos = board.getPawnPosition(i);
            if (pos != null && !hasPathToGoal(pos, i, getNumberOfPlayers(board), tempBoard)) {
                return false;
            }
        }
        return true;
    }

    /**
     * BFS check if a player still has a valid path to goal.
     */
    private boolean hasPathToGoal(Position start, int playerIndex, int numPlayers, QuoridorBoard board) {
        Queue<Position> queue = new LinkedList<>();
        Set<Position> visited = new HashSet<>();
        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Position curr = queue.poll();
            if (hasWon(curr, playerIndex, numPlayers)) return true;

            Position[] neighbors = {
                new Position(curr.row - 1, curr.col),
                new Position(curr.row + 1, curr.col),
                new Position(curr.row, curr.col - 1),
                new Position(curr.row, curr.col + 1)
            };

            for (Position next : neighbors) {
                if (isWithinBounds(next)
                        && !visited.contains(next)
                        && !board.isWallBlocking(curr, next)) {
                    visited.add(next);
                    queue.offer(next);
                }
            }
        }
        return false;
    }

    /**
     * Check if player has reached the goal.
     */
    public boolean hasWon(Position pos, int playerIndex, int numPlayers) {
        int size = board.getSize() - 1;
        if (numPlayers == 2) {
            if (playerIndex == 0) return pos.row == size;
            else return pos.row == 0;
        } else {
            switch (playerIndex) {
                case 0: return pos.row == size;
                case 1: return pos.row == 0;
                case 2: return pos.col == size;
                case 3: return pos.col == 0;
                default: return false;
            }
        }
    }

    private boolean isWithinBounds(Position pos) {
        return pos.row >= 0 && pos.row < board.getSize() && pos.col >= 0 && pos.col < board.getSize();
    }

    private int getNumberOfPlayers(QuoridorBoard board) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (board.getPawnPosition(i) != null) count++;
        }
        return count;
    }

    public int getSize() {
        return board.getSize();
    }
}

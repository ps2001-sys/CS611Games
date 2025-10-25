package a3;

import java.util.*;

/**
 * Implements the rules and validation logic for Quoridor.
 * Updated to work with the new QuoridorBoard that extends Board.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class QuoridorRules {
    private final QuoridorBoard board;

    public QuoridorRules(QuoridorBoard board) {
        this.board = board;
    }

    /**
     * Validate a move from one position to another.
     */
    public Position validateMove(Position from, Position to, QuoridorBoard board, int playerIndex) {
        // Check board boundaries
        if (!isWithinBounds(to)) {
            return null;
        }

        // Check if move is orthogonal (not diagonal for normal moves)
        int rowDiff = Math.abs(to.row - from.row);
        int colDiff = Math.abs(to.col - from.col);

        // Normal move: one space orthogonally
        if (rowDiff + colDiff == 1) {
            // Check for walls blocking the path
            if (board.isWallBlocking(from, to)) {
                return null;
            }

            // Check if destination is occupied
            if (board.isOccupied(to.row, to.col)) {
                // Try to jump over the opponent
                return calculateJumpPosition(from, to, board);
            }

            // Valid normal move
            return to;
        }

        // Check for valid jump move (two spaces in same direction)
        if ((rowDiff == 2 && colDiff == 0) || (rowDiff == 0 && colDiff == 2)) {
            Position middle = new Position(
                    (from.row + to.row) / 2,
                    (from.col + to.col) / 2
            );

            // Middle position must be occupied by opponent
            if (!board.isOccupied(middle.row, middle.col)) {
                return null;
            }

            // Check no walls block the jump
            if (board.isWallBlocking(from, middle) || board.isWallBlocking(middle, to)) {
                return null;
            }

            // Destination must be free
            if (board.isOccupied(to.row, to.col)) {
                return null;
            }

            return to;
        }

        // Diagonal jump (when straight jump is blocked)
        if (rowDiff == 1 && colDiff == 1) {
            // Check if this is a valid diagonal jump situation
            Position horizontalFirst = new Position(from.row, to.col);
            if (board.isOccupied(horizontalFirst.row, horizontalFirst.col) &&
                    !board.isWallBlocking(from, horizontalFirst) &&
                    !board.isWallBlocking(horizontalFirst, to)) {

                // Check if straight jump would be blocked
                Position beyondHorizontal = new Position(from.row, to.col + (to.col - from.col));
                if (!isWithinBounds(beyondHorizontal) ||
                        board.isWallBlocking(horizontalFirst, beyondHorizontal) ||
                        board.isOccupied(beyondHorizontal.row, beyondHorizontal.col)) {

                    if (!board.isOccupied(to.row, to.col)) {
                        return to; // Valid diagonal jump
                    }
                }
            }

            // Check vertical then horizontal path
            Position verticalFirst = new Position(to.row, from.col);
            if (board.isOccupied(verticalFirst.row, verticalFirst.col) &&
                    !board.isWallBlocking(from, verticalFirst) &&
                    !board.isWallBlocking(verticalFirst, to)) {

                // Check if straight jump would be blocked
                Position beyondVertical = new Position(to.row + (to.row - from.row), from.col);
                if (!isWithinBounds(beyondVertical) ||
                        board.isWallBlocking(verticalFirst, beyondVertical) ||
                        board.isOccupied(beyondVertical.row, beyondVertical.col)) {

                    if (!board.isOccupied(to.row, to.col)) {
                        return to; // Valid diagonal jump
                    }
                }
            }
        }

        return null; // Invalid move
    }

    private Position calculateJumpPosition(Position from, Position to, QuoridorBoard board) {
        int rowDir = to.row - from.row;
        int colDir = to.col - from.col;

        // Try straight jump first
        Position straightJump = new Position(to.row + rowDir, to.col + colDir);

        if (isWithinBounds(straightJump) &&
                !board.isWallBlocking(to, straightJump) &&
                !board.isOccupied(straightJump.row, straightJump.col)) {
            return straightJump;
        }

        // If straight jump is blocked, try diagonal jumps
        List<Position> diagonalOptions = new ArrayList<>();

        if (rowDir != 0) {
            // Moving vertically, try left and right diagonals
            Position leftDiag = new Position(to.row, to.col - 1);
            Position rightDiag = new Position(to.row, to.col + 1);

            if (isWithinBounds(leftDiag) && !board.isWallBlocking(to, leftDiag) &&
                    !board.isOccupied(leftDiag.row, leftDiag.col)) {
                diagonalOptions.add(leftDiag);
            }
            if (isWithinBounds(rightDiag) && !board.isWallBlocking(to, rightDiag) &&
                    !board.isOccupied(rightDiag.row, rightDiag.col)) {
                diagonalOptions.add(rightDiag);
            }
        } else {
            // Moving horizontally, try up and down diagonals
            Position upDiag = new Position(to.row - 1, to.col);
            Position downDiag = new Position(to.row + 1, to.col);

            if (isWithinBounds(upDiag) && !board.isWallBlocking(to, upDiag) &&
                    !board.isOccupied(upDiag.row, upDiag.col)) {
                diagonalOptions.add(upDiag);
            }
            if (isWithinBounds(downDiag) && !board.isWallBlocking(to, downDiag) &&
                    !board.isOccupied(downDiag.row, downDiag.col)) {
                diagonalOptions.add(downDiag);
            }
        }

        // For simplicity, return first available diagonal option
        if (!diagonalOptions.isEmpty()) {
            return diagonalOptions.get(0);
        }

        return null; // No valid jump available
    }

    public boolean canPlaceWall(WallPiece wall, QuoridorBoard board) {
        int row = wall.getPosition().row;
        int col = wall.getPosition().col;

        // Check bounds (walls are placed on intersections)
        if (row < 0 || row >= board.getSize() - 1 || col < 0 || col >= board.getSize() - 1) {
            return false;
        }

        // Check for overlapping walls
        for (WallPiece existing : board.getWalls()) {
            if (wall.overlaps(existing)) {
                return false;
            }
        }

        // Create a temporary board with the new wall to check paths
        QuoridorBoard tempBoard = board.copy();
        tempBoard.placeWall(wall);

        // Check that all players can still reach their goal
        for (int playerIndex = 0; playerIndex < getNumberOfPlayers(board); playerIndex++) {
            Position playerPos = board.getPawnPosition(playerIndex);
            if (playerPos != null) {
                if (!hasPathToGoal(playerPos, playerIndex, getNumberOfPlayers(board), tempBoard)) {
                    return false; // Wall would block this player's path
                }
            }
        }

        return true;
    }

    private boolean hasPathToGoal(Position start, int playerIndex, int numPlayers, QuoridorBoard board) {
        Queue<Position> queue = new LinkedList<>();
        Set<Position> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            // Check if we've reached the goal
            if (hasWon(current, playerIndex, numPlayers)) {
                return true;
            }

            // Try all four directions
            Position[] neighbors = {
                    new Position(current.row - 1, current.col), // North
                    new Position(current.row + 1, current.col), // South
                    new Position(current.row, current.col - 1), // West
                    new Position(current.row, current.col + 1)  // East
            };

            for (Position next : neighbors) {
                if (isWithinBounds(next) &&
                        !visited.contains(next) &&
                        !board.isWallBlocking(current, next)) {

                    visited.add(next);
                    queue.offer(next);
                }
            }
        }

        return false; // No path found
    }

    public boolean hasWon(Position position, int playerIndex, int numPlayers) {
        if (numPlayers == 2) {
            // 2-player: reach opposite side
            if (playerIndex == 0) {
                return position.row == board.getSize() - 1; // Player 1 reaches bottom
            } else {
                return position.row == 0; // Player 2 reaches top
            }
        } else {
            // 4-player: each player has their target side
            switch (playerIndex) {
                case 0: return position.row == board.getSize() - 1; // North player reaches south
                case 1: return position.row == 0;                    // South player reaches north
                case 2: return position.col == board.getSize() - 1; // West player reaches east
                case 3: return position.col == 0;                    // East player reaches west
                default: return false;
            }
        }
    }

    private boolean isWithinBounds(Position pos) {
        return pos.row >= 0 && pos.row < board.getSize() &&
                pos.col >= 0 && pos.col < board.getSize();
    }

    private int getNumberOfPlayers(QuoridorBoard board) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (board.getPawnPosition(i) != null) {
                count++;
            }
        }
        return count;
    }

    // Add this method that's needed by the refactored board
    public int getSize() {
        return board.getSize();
    }
}
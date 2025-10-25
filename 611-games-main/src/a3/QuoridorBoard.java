package a3;

import common.Board;
import common.Tile;
import engine.TextUI;
import java.util.*;

/**
 * Quoridor board implementation that properly extends the common Board class.
 * This demonstrates proper OOP design and inheritance.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class QuoridorBoard extends Board {
    private Map<Integer, Pawn> pawns;
    private List<WallPiece> walls;
    private Map<Integer, Position> pawnPositions;

    private static final int BOARD_SIZE = 9;

    /**
     * Create a new Quoridor board.
     */
    public QuoridorBoard() {
        super(BOARD_SIZE, BOARD_SIZE);
        this.pawns = new HashMap<>();
        this.walls = new ArrayList<>();
        this.pawnPositions = new HashMap<>();
    }

    @Override
    protected void initializeBoard() {
        // Initialize all tiles
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new QuoridorTile(r, c);
            }
        }
    }

    /**
     * Initialize player pawns for the game.
     * @param numPlayers Number of players (2 or 4)
     */
    public void initializePawns(int numPlayers) {
        if (numPlayers == 2) {
            // Player 1 starts at top center
            Pawn p1 = new Pawn("Player 1", 1);
            placePawn(p1, 0, 4);

            // Player 2 starts at bottom center
            Pawn p2 = new Pawn("Player 2", 2);
            placePawn(p2, 8, 4);
        } else if (numPlayers == 4) {
            // Player 1 - North
            Pawn p1 = new Pawn("Player 1", 1);
            placePawn(p1, 0, 4);

            // Player 2 - South
            Pawn p2 = new Pawn("Player 2", 2);
            placePawn(p2, 8, 4);

            // Player 3 - West
            Pawn p3 = new Pawn("Player 3", 3);
            placePawn(p3, 4, 0);

            // Player 4 - East
            Pawn p4 = new Pawn("Player 4", 4);
            placePawn(p4, 4, 8);
        }
    }

    /**
     * Place a pawn on the board.
     * @param pawn The pawn to place
     * @param row Row position
     * @param col Column position
     */
    private void placePawn(Pawn pawn, int row, int col) {
        QuoridorTile tile = (QuoridorTile) grid[row][col];
        tile.setPiece(pawn);
        pawns.put(pawn.getPlayerNumber(), pawn);
        pawnPositions.put(pawn.getPlayerNumber(), new Position(row, col));
    }

    /**
     * Move a pawn to a new position.
     * @param playerNumber The player number
     * @param newRow New row position
     * @param newCol New column position
     */
    public void movePawn(int playerNumber, int newRow, int newCol) {
        if (!isValidPosition(newRow, newCol)) {
            throw new IllegalArgumentException("Invalid position");
        }

        Position oldPos = pawnPositions.get(playerNumber);
        if (oldPos != null) {
            // Clear old position
            QuoridorTile oldTile = (QuoridorTile) grid[oldPos.row][oldPos.col];
            oldTile.setPiece(null);
        }

        // Set new position
        Pawn pawn = pawns.get(playerNumber);
        QuoridorTile newTile = (QuoridorTile) grid[newRow][newCol];
        newTile.setPiece(pawn);
        pawnPositions.put(playerNumber, new Position(newRow, newCol));
    }

    /**
     * Compatibility method - same as movePawn.
     * @param playerNumber The player number
     * @param newPosition New position
     */
    public void movePlayer(int playerNumber, Position newPosition) {
        movePawn(playerNumber, newPosition.row, newPosition.col);
    }

    /**
     * Place a wall on the board.
     * @param wall The wall to place
     */
    public void placeWall(WallPiece wall) {
        walls.add(wall);

        Position pos = wall.getPosition();
        if (wall.isHorizontal()) {
            // Set wall on affected tiles
            if (isValidPosition(pos.row, pos.col)) {
                ((QuoridorTile) grid[pos.row][pos.col]).setWallSouth(true);
            }
            if (isValidPosition(pos.row, pos.col + 1)) {
                ((QuoridorTile) grid[pos.row][pos.col + 1]).setWallSouth(true);
            }
            if (isValidPosition(pos.row + 1, pos.col)) {
                ((QuoridorTile) grid[pos.row + 1][pos.col]).setWallNorth(true);
            }
            if (isValidPosition(pos.row + 1, pos.col + 1)) {
                ((QuoridorTile) grid[pos.row + 1][pos.col + 1]).setWallNorth(true);
            }
        } else {
            // Vertical wall
            if (isValidPosition(pos.row, pos.col)) {
                ((QuoridorTile) grid[pos.row][pos.col]).setWallEast(true);
            }
            if (isValidPosition(pos.row + 1, pos.col)) {
                ((QuoridorTile) grid[pos.row + 1][pos.col]).setWallEast(true);
            }
            if (isValidPosition(pos.row, pos.col + 1)) {
                ((QuoridorTile) grid[pos.row][pos.col + 1]).setWallWest(true);
            }
            if (isValidPosition(pos.row + 1, pos.col + 1)) {
                ((QuoridorTile) grid[pos.row + 1][pos.col + 1]).setWallWest(true);
            }
        }
    }

    /**
     * Check if there's a wall blocking movement between two positions.
     * @param from Starting position
     * @param to Target position
     * @return true if blocked by wall
     */
    public boolean isWallBlocking(Position from, Position to) {
        QuoridorTile fromTile = (QuoridorTile) grid[from.row][from.col];

        // Check horizontal movement
        if (from.row == to.row) {
            if (from.col < to.col) {
                // Moving right
                return fromTile.hasWallEast();
            } else {
                // Moving left
                return fromTile.hasWallWest();
            }
        }

        // Check vertical movement
        if (from.col == to.col) {
            if (from.row < to.row) {
                // Moving down
                return fromTile.hasWallSouth();
            } else {
                // Moving up
                return fromTile.hasWallNorth();
            }
        }

        return false;
    }

    /**
     * Get the position of a pawn.
     * @param playerNumber The player number
     * @return The pawn's position
     */
    public Position getPawnPosition(int playerNumber) {
        return pawnPositions.get(playerNumber);
    }

    /**
     * Compatibility method - same as getPawnPosition.
     * @param playerNumber The player number
     * @return The player's position
     */
    public Position getPlayerPosition(int playerNumber) {
        return getPawnPosition(playerNumber);
    }

    /**
     * Get the size of the board.
     * @return Board size (9 for Quoridor)
     */
    public int getSize() {
        return BOARD_SIZE;
    }

    /**
     * Check if a position is occupied by a pawn.
     * @param row Row position
     * @param col Column position
     * @return true if occupied
     */
    public boolean isOccupied(int row, int col) {
        if (!isValidPosition(row, col)) return false;
        QuoridorTile tile = (QuoridorTile) grid[row][col];
        return !tile.isEmpty();
    }

    /**
     * Get all placed walls.
     * @return List of walls
     */
    public List<WallPiece> getWalls() {
        return new ArrayList<>(walls);
    }

    @Override
    public boolean isGameOver() {
        // Check victory conditions for each player
        for (Map.Entry<Integer, Position> entry : pawnPositions.entrySet()) {
            int playerNum = entry.getKey();
            Position pos = entry.getValue();

            if (pawns.size() == 2) {
                // 2-player victory conditions
                if (playerNum == 1 && pos.row == 8) return true;  // Player 1 reaches bottom
                if (playerNum == 2 && pos.row == 0) return true;  // Player 2 reaches top
            } else {
                // 4-player victory conditions
                if (playerNum == 1 && pos.row == 8) return true;  // North player reaches south
                if (playerNum == 2 && pos.row == 0) return true;  // South player reaches north
                if (playerNum == 3 && pos.col == 8) return true;  // West player reaches east
                if (playerNum == 4 && pos.col == 0) return true;  // East player reaches west
            }
        }
        return false;
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder();

        // Column numbers
        sb.append("    ");
        for (int c = 0; c < cols; c++) {
            sb.append(c).append("   ");
        }
        sb.append("\n");

        for (int r = 0; r < rows; r++) {
            // Horizontal walls and borders
            sb.append("  ");
            for (int c = 0; c < cols; c++) {
                sb.append("+");
                QuoridorTile tile = (QuoridorTile) grid[r][c];
                if (r > 0 && tile.hasWallNorth()) {
                    sb.append("═══");
                } else {
                    sb.append("---");
                }
            }
            sb.append("+\n");

            // Vertical walls and pawns
            sb.append(r).append(" ");
            for (int c = 0; c < cols; c++) {
                QuoridorTile tile = (QuoridorTile) grid[r][c];

                if (c > 0 && tile.hasWallWest()) {
                    sb.append("║");
                } else {
                    sb.append("|");
                }

                if (!tile.isEmpty()) {
                    sb.append(" ").append(tile.getPiece().getDisplayChar()).append(" ");
                } else {
                    sb.append("   ");
                }
            }

            // Right border
            QuoridorTile lastTile = (QuoridorTile) grid[r][cols - 1];
            if (lastTile.hasWallEast()) {
                sb.append("║");
            } else {
                sb.append("|");
            }
            sb.append("\n");
        }

        // Bottom border
        sb.append("  ");
        for (int c = 0; c < cols; c++) {
            sb.append("+");
            QuoridorTile tile = (QuoridorTile) grid[rows - 1][c];
            if (tile.hasWallSouth()) {
                sb.append("═══");
            } else {
                sb.append("---");
            }
        }
        sb.append("+\n");

        return sb.toString();
    }

    /**
     * Create a copy of the board for AI or validation.
     * @return A copy of the board
     */
    public QuoridorBoard copy() {
        QuoridorBoard copy = new QuoridorBoard();

        // Copy pawns
        for (Map.Entry<Integer, Position> entry : pawnPositions.entrySet()) {
            Position pos = entry.getValue();
            Pawn original = pawns.get(entry.getKey());
            Pawn pawnCopy = new Pawn(original.getOwner(), original.getPlayerNumber());
            copy.placePawn(pawnCopy, pos.row, pos.col);
        }

        // Copy walls
        for (WallPiece wall : walls) {
            copy.placeWall(new WallPiece(wall.getPosition(), wall.isHorizontal()));
        }

        return copy;
    }
}
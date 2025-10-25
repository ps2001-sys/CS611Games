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
 * Date: 2025-10-25
 */
public class QuoridorBoard extends Board {
    private Map<Integer, Pawn> pawns;
    private List<WallPiece> walls;
    private Map<Integer, Position> pawnPositions;

    private static final int BOARD_SIZE = 9;

    // Create a new Quoridor board.
    public QuoridorBoard() {
        super(BOARD_SIZE, BOARD_SIZE);
        this.pawns = new HashMap<>();
        this.walls = new ArrayList<>();
        this.pawnPositions = new HashMap<>();
    }

    @Override
    protected void initializeBoard() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new QuoridorTile(r, c);
            }
        }
    }

    /**
     * Initialize player pawns for the game.
     */
    public void initializePawns(int numPlayers) {
        if (numPlayers == 2) {
            Pawn p1 = new Pawn("Player 1", 1);
            placePawn(p1, 0, 4);

            Pawn p2 = new Pawn("Player 2", 2);
            placePawn(p2, 8, 4);
        } else if (numPlayers == 4) {
            Pawn p1 = new Pawn("Player 1", 1);
            placePawn(p1, 0, 4);

            Pawn p2 = new Pawn("Player 2", 2);
            placePawn(p2, 8, 4);

            Pawn p3 = new Pawn("Player 3", 3);
            placePawn(p3, 4, 0);

            Pawn p4 = new Pawn("Player 4", 4);
            placePawn(p4, 4, 8);
        }
    }

    private void placePawn(Pawn pawn, int row, int col) {
        QuoridorTile tile = (QuoridorTile) grid[row][col];
        tile.setPiece(pawn);
        pawns.put(pawn.getPlayerNumber(), pawn);
        pawnPositions.put(pawn.getPlayerNumber(), new Position(row, col));
    }

    public void movePawn(int playerNumber, int newRow, int newCol) {
        if (!isValidPosition(newRow, newCol)) {
            throw new IllegalArgumentException("Invalid position");
        }

        Position oldPos = pawnPositions.get(playerNumber);
        if (oldPos != null) {
            QuoridorTile oldTile = (QuoridorTile) grid[oldPos.row][oldPos.col];
            oldTile.setPiece(null);
        }

        Pawn pawn = pawns.get(playerNumber);
        QuoridorTile newTile = (QuoridorTile) grid[newRow][newCol];
        newTile.setPiece(pawn);
        pawnPositions.put(playerNumber, new Position(newRow, newCol));
    }

    public void movePlayer(int playerNumber, Position newPosition) {
        movePawn(playerNumber, newPosition.row, newPosition.col);
    }

    public void placeWall(WallPiece wall) {
        walls.add(wall);
        Position pos = wall.getPosition();

        if (wall.isHorizontal()) {
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

    public boolean isWallBlocking(Position from, Position to) {
        QuoridorTile fromTile = (QuoridorTile) grid[from.row][from.col];
        if (from.row == to.row) {
            return from.col < to.col ? fromTile.hasWallEast() : fromTile.hasWallWest();
        }
        if (from.col == to.col) {
            return from.row < to.row ? fromTile.hasWallSouth() : fromTile.hasWallNorth();
        }
        return false;
    }

    public Position getPawnPosition(int playerNumber) {
        return pawnPositions.get(playerNumber);
    }

    public Position getPlayerPosition(int playerNumber) {
        return getPawnPosition(playerNumber);
    }

    public int getSize() {
        return BOARD_SIZE;
    }

    public boolean isOccupied(int row, int col) {
        if (!isValidPosition(row, col)) return false;
        QuoridorTile tile = (QuoridorTile) grid[row][col];
        return !tile.isEmpty();
    }

    public List<WallPiece> getWalls() {
        return new ArrayList<>(walls);
    }

    @Override
    public boolean isGameOver() {
        for (Map.Entry<Integer, Position> entry : pawnPositions.entrySet()) {
            int playerNum = entry.getKey();
            Position pos = entry.getValue();

            if (pawns.size() == 2) {
                if (playerNum == 1 && pos.row == 8) return true;
                if (playerNum == 2 && pos.row == 0) return true;
            } else {
                if (playerNum == 1 && pos.row == 8) return true;
                if (playerNum == 2 && pos.row == 0) return true;
                if (playerNum == 3 && pos.col == 8) return true;
                if (playerNum == 4 && pos.col == 0) return true;
            }
        }
        return false;
    }

    // ====== NEW FIXED COLOR RENDERING SECTION ======

    // ✅ Add this overload — this fixes QuoridorGame.java compile error
    public String render(TextUI ui) {
        return renderWithColor(ui);
    }

    // 彩色版本：根据玩家墙颜色渲染
    public String renderWithColor(TextUI ui) {
        StringBuilder sb = new StringBuilder();

        sb.append("    ");
        for (int c = 0; c < cols; c++) sb.append(c).append("   ");
        sb.append("\n");

        for (int r = 0; r < rows; r++) {
            sb.append("  ");
            for (int c = 0; c < cols; c++) {
                sb.append("+");
                QuoridorTile tile = (QuoridorTile) grid[r][c];
                if (r > 0 && tile.hasWallNorth()) {
                    int owner = getWallOwnerAt(r - 1, c, 'H');
                    sb.append(ui.colorize("═══", owner));
                } else {
                    sb.append("---");
                }
            }
            sb.append("+\n");

            sb.append(r).append(" ");
            for (int c = 0; c < cols; c++) {
                QuoridorTile tile = (QuoridorTile) grid[r][c];
                if (c > 0 && tile.hasWallWest()) {
                    int owner = getWallOwnerAt(r, c - 1, 'V');
                    sb.append(ui.colorize("║", owner));
                } else {
                    sb.append("|");
                }

                if (!tile.isEmpty()) {
                    sb.append(" ").append(tile.getPiece().getDisplayChar()).append(" ");
                } else {
                    sb.append("   ");
                }
            }

            QuoridorTile lastTile = (QuoridorTile) grid[r][cols - 1];
            sb.append(lastTile.hasWallEast() ? "║" : "|");
            sb.append("\n");
        }

        sb.append("  ");
        for (int c = 0; c < cols; c++) {
            sb.append("+");
            QuoridorTile tile = (QuoridorTile) grid[rows - 1][c];
            if (tile.hasWallSouth()) sb.append("═══");
            else sb.append("---");
        }
        sb.append("+\n");

        return sb.toString();
    }

    private int getWallOwnerAt(int row, int col, char orientation) {
        for (WallPiece w : walls) {
            Position p = w.getPosition();
            if (w.isHorizontal() && orientation == 'H') {
                if (p.row == row && (p.col == col || p.col + 1 == col)) {
                    return w.getOwnerNumber();
                }
            } else if (w.isVertical() && orientation == 'V') {
                if (p.col == col && (p.row == row || p.row + 1 == row)) {
                    return w.getOwnerNumber();
                }
            }
        }
        return 0;
    }

    @Override
    public String render() {
        return renderWithColor(new engine.TextUI());
    }

    public QuoridorBoard copy() {
        QuoridorBoard copy = new QuoridorBoard();
        for (Map.Entry<Integer, Position> entry : pawnPositions.entrySet()) {
            Position pos = entry.getValue();
            Pawn original = pawns.get(entry.getKey());
            Pawn pawnCopy = new Pawn(original.getOwner(), original.getPlayerNumber());
            copy.placePawn(pawnCopy, pos.row, pos.col);
        }
        for (WallPiece wall : walls) {
            copy.placeWall(new WallPiece(wall.getPosition(), wall.isHorizontal()));
        }
        return copy;
    }
}

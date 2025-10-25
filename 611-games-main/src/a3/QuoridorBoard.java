package a3;

import engine.TextUI;
import java.util.*;

/**
 * Represents the Quoridor game board.
 * Manages a 9x9 grid with player positions and wall placements.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class QuoridorBoard {
    private final int size;
    private final Map<Integer, Position> playerPositions;
    private final List<Wall> walls;
    private final boolean[][] horizontalWalls;
    private final boolean[][] verticalWalls;

    private static final char[] PLAYER_CHARS = {'1', '2', '3', '4'};

    public QuoridorBoard(int size) {
        this.size = size;
        this.playerPositions = new HashMap<>();
        this.walls = new ArrayList<>();
        this.horizontalWalls = new boolean[size - 1][size - 1];
        this.verticalWalls = new boolean[size - 1][size - 1];
    }

    public int getSize() {
        return size;
    }

    public void setPlayerPosition(int playerIndex, Position position) {
        playerPositions.put(playerIndex, position);
    }

    public Position getPlayerPosition(int playerIndex) {
        return playerPositions.get(playerIndex);
    }

    public void movePlayer(int playerIndex, Position newPosition) {
        playerPositions.put(playerIndex, newPosition);
    }

    public void placeWall(Wall wall) {
        walls.add(wall);

        int row = wall.position.row;
        int col = wall.position.col;

        if (wall.orientation == 'H') {
            if (isValidWallPosition(row, col)) {
                horizontalWalls[row][col] = true;
                if (col + 1 < size - 1) {
                    horizontalWalls[row][col + 1] = true;
                }
            }
        } else {
            if (isValidWallPosition(row, col)) {
                verticalWalls[row][col] = true;
                if (row + 1 < size - 1) {
                    verticalWalls[row + 1][col] = true;
                }
            }
        }
    }

    private boolean isValidWallPosition(int row, int col) {
        return row >= 0 && row < size - 1 && col >= 0 && col < size - 1;
    }

    public List<Wall> getWalls() {
        return new ArrayList<>(walls);
    }

    public boolean isWallBlocking(Position from, Position to) {
        int fromRow = from.row;
        int fromCol = from.col;
        int toRow = to.row;
        int toCol = to.col;

        // Vertical movement
        if (fromCol == toCol) {
            if (fromRow < toRow) {
                // Moving down
                for (int r = fromRow; r < toRow; r++) {
                    if (r < size - 1 && fromCol > 0 && horizontalWalls[r][fromCol - 1]) return true;
                    if (r < size - 1 && fromCol < size - 1 && horizontalWalls[r][fromCol]) return true;
                }
            } else {
                // Moving up
                for (int r = toRow; r < fromRow; r++) {
                    if (r < size - 1 && fromCol > 0 && horizontalWalls[r][fromCol - 1]) return true;
                    if (r < size - 1 && fromCol < size - 1 && horizontalWalls[r][fromCol]) return true;
                }
            }
        }

        // Horizontal movement
        if (fromRow == toRow) {
            if (fromCol < toCol) {
                // Moving right
                for (int c = fromCol; c < toCol; c++) {
                    if (c < size - 1 && fromRow > 0 && verticalWalls[fromRow - 1][c]) return true;
                    if (c < size - 1 && fromRow < size - 1 && verticalWalls[fromRow][c]) return true;
                }
            } else {
                // Moving left
                for (int c = toCol; c < fromCol; c++) {
                    if (c < size - 1 && fromRow > 0 && verticalWalls[fromRow - 1][c]) return true;
                    if (c < size - 1 && fromRow < size - 1 && verticalWalls[fromRow][c]) return true;
                }
            }
        }

        return false;
    }

    public boolean isOccupied(Position position) {
        for (Position pos : playerPositions.values()) {
            if (pos.equals(position)) {
                return true;
            }
        }
        return false;
    }

    public int getPlayerAt(Position position) {
        for (Map.Entry<Integer, Position> entry : playerPositions.entrySet()) {
            if (entry.getValue().equals(position)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public String render() {
        StringBuilder sb = new StringBuilder();

        // Column numbers
        sb.append("    ");
        for (int c = 0; c < size; c++) {
            sb.append(c).append("   ");
        }
        sb.append("\n");

        for (int r = 0; r < size; r++) {
            // Horizontal wall row
            sb.append("  ");
            for (int c = 0; c < size; c++) {
                sb.append("+");
                if (c < size - 1 && r > 0 && horizontalWalls[r - 1][c]) {
                    sb.append("===");
                } else {
                    sb.append("---");
                }
            }
            sb.append("+\n");

            // Cell row
            sb.append(r).append(" ");
            for (int c = 0; c < size; c++) {
                // Check for vertical wall
                if (c > 0 && r < size - 1 && verticalWalls[r][c - 1]) {
                    sb.append("║");
                } else {
                    sb.append("|");
                }

                // Check for player
                Position pos = new Position(r, c);
                int playerIndex = getPlayerAt(pos);
                if (playerIndex >= 0) {
                    sb.append(" ").append(PLAYER_CHARS[playerIndex]).append(" ");
                } else {
                    sb.append("   ");
                }
            }
            sb.append("|\n");
        }

        // Bottom border
        sb.append("  ");
        for (int c = 0; c < size; c++) {
            sb.append("+---");
        }
        sb.append("+\n");

        return sb.toString();
    }

    public String render(TextUI ui) {
        if (!ui.isColor()) {
            return render();
        }

        StringBuilder sb = new StringBuilder();

        // Column numbers
        sb.append("    ");
        for (int c = 0; c < size; c++) {
            sb.append(c).append("   ");
        }
        sb.append("\n");

        for (int r = 0; r < size; r++) {
            // Horizontal wall row
            sb.append("  ");
            for (int c = 0; c < size; c++) {
                sb.append("+");
                if (c < size - 1 && r > 0 && horizontalWalls[r - 1][c]) {
                    sb.append(ui.magenta("═══"));
                } else {
                    sb.append("---");
                }
            }
            sb.append("+\n");

            // Cell row
            sb.append(r).append(" ");
            for (int c = 0; c < size; c++) {
                // Check for vertical wall
                if (c > 0 && r < size - 1 && verticalWalls[r][c - 1]) {
                    sb.append(ui.magenta("║"));
                } else {
                    sb.append("|");
                }

                // Check for player
                Position pos = new Position(r, c);
                int playerIndex = getPlayerAt(pos);
                if (playerIndex >= 0) {
                    String playerStr = " " + PLAYER_CHARS[playerIndex] + " ";
                    // Apply color based on player
                    switch (playerIndex) {
                        case 0: sb.append(ui.red(playerStr)); break;
                        case 1: sb.append(ui.blue(playerStr)); break;
                        case 2: sb.append(ui.green(playerStr)); break;
                        case 3: sb.append(ui.yellow(playerStr)); break;
                        default: sb.append(playerStr);
                    }
                } else {
                    sb.append("   ");
                }
            }
            sb.append("|\n");
        }

        // Bottom border
        sb.append("  ");
        for (int c = 0; c < size; c++) {
            sb.append("+---");
        }
        sb.append("+\n");

        return sb.toString();
    }

    public QuoridorBoard copy() {
        QuoridorBoard copy = new QuoridorBoard(this.size);
        copy.playerPositions.putAll(this.playerPositions);
        copy.walls.addAll(this.walls);

        for (int r = 0; r < size - 1; r++) {
            for (int c = 0; c < size - 1; c++) {
                copy.horizontalWalls[r][c] = this.horizontalWalls[r][c];
                copy.verticalWalls[r][c] = this.verticalWalls[r][c];
            }
        }

        return copy;
    }
}
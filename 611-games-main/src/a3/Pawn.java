package a3;

import common.Piece;
import common.Tile;

/**
 * Represents a pawn in the Quoridor game associated with a player.
 */
public class Pawn extends Piece {
    private int playerNumber;
    private Position currentPosition;

    public Pawn(String owner, int playerNumber) {
        super(owner, playerNumber);
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPosition(Position pos) {
        this.currentPosition = pos;
    }

    public Position getPosition() {
        return currentPosition;
    }

    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public String toString() {
        return "P" + playerNumber;
    }

    @Override
    public String getDisplayChar() {
        return String.valueOf(playerNumber);
    }

    @Override
    public boolean canMoveTo(Tile tile) {
        return tile != null && tile.isEmpty();
    }

    public boolean hasReachedGoal(int boardSize) {
        if (currentPosition == null) {
            return false;
        }
        switch (playerNumber) {
            case 1:
                return currentPosition.row == boardSize - 1;
            case 2:
                return currentPosition.row == 0;
            case 3:
                return currentPosition.col == boardSize - 1;
            case 4:
                return currentPosition.col == 0;
            default:
                return false;
        }
    }
}

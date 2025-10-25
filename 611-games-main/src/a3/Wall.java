package a3;

public class Wall {
    public final Position position;
    public final char orientation;

    public Wall(Position position, char orientation) {
        this.position = position;
        this.orientation = Character.toUpperCase(orientation);

        if (orientation != 'H' && orientation != 'V') {
            throw new IllegalArgumentException("Wall orientation must be 'H' or 'V'");
        }
    }

    public Position[] getBlockedPositions() {
        if (orientation == 'H') {
            return new Position[] {
                    position,
                    new Position(position.row, position.col + 1)
            };
        } else {
            return new Position[] {
                    position,
                    new Position(position.row + 1, position.col)
            };
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Wall wall = (Wall) obj;
        return orientation == wall.orientation && position.equals(wall.position);
    }

    @Override
    public int hashCode() {
        return 31 * position.hashCode() + orientation;
    }

    @Override
    public String toString() {
        return "Wall[" + position + ", " + orientation + "]";
    }
}
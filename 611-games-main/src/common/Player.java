package common;

/**
 * Simple player representation.
 * Just stores the player's name, and use toString to return the name
 */
public class Player {
    public final String name;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

package game.state;

/**
 * Class representing the empty cell, the black cell, the red and the blue pucks.
 */
public enum Cell {

    EMPTY,
    RED,
    BLUE,
    BLACK;

    /**
     * Returns the instance represented by the value specified.
     *
     * @param value the value representing the instance
     * @return the instance represented by the value specified
     * @throws IllegalArgumentException if the value specified does not
     * represent an instance
     */
    public static Cell of(int value) {
        if (value < 0 || value >= values().length) {
            throw new IllegalArgumentException();
        }
        return values()[value];
    }

    /**
     * Returns
     * the integer value that represents this instance.
     *
     * @return the integer value that represents the instance
     */
    public int getValue() {
        return ordinal();
    }

    /**
     * Returns the opposite puck of the given puck.
     *
     * @return the opposite puck
     */
    public Cell opposite() {
        switch (this) {
            case RED: return BLUE;
            case BLUE: return RED;
        }
        throw new AssertionError();
    }

    public String toString() {
        return Integer.toString(ordinal());
    }
}

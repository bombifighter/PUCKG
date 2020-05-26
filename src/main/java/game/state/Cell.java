package game.state;

/**
 * Class representing the empty cell, the black cell, the red and the blue pucks.
 */
public enum Cell {

    /**
     * Enum constant referring to a cell that is empty.
     */
    EMPTY,
    /**
     * Enum constant referring to a cell that contains a red puck.
     */
    RED,
    /**
     * Enum constant referring to a cell that contains a blue puck.
     */
    BLUE,
    /**
     * Enum constant referring to the black cell.
     */
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

    /**
     * Returns the string format the specified enum constant's ordinal number.
     *
     * @return the string value that represents the specified enum constant's
     * ordinal number
     */
    public String toString() {
        return Integer.toString(ordinal());
    }
}

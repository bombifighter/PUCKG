package game.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Class representing the state of the game.
 */
@Data
@Slf4j
public class TableState implements Cloneable {

    /**
     * The array representing the initial configuration of the table.
     */
    public static final int[][] INITIAL = {
            {1, 0, 0, 0, 0, 2},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 3, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {2, 0, 0, 0, 0, 1}
    };

    /**
     * The array storing the current configuration of the table.
     */
    @Setter(AccessLevel.NONE)
    private Cell[][] table;

    /**
     * A variable to store the previous player.
     */
    @Setter(AccessLevel.NONE)
    private int previousPlayer = 2;

    /**
     * Creates a {@code TableState} object representing the (original)
     * initial state of the puzzle.
     */
    public TableState() {
        this(INITIAL);
    }

    /**
     * Creates a {@code TableState} object that is initialized it with a specific array.
     * @param a and array of size 6&#xd7;6 representing the initial configuration of the table
     * @throws IllegalArgumentException if the array does not represent a valid configuration of the table
     */
    public TableState(int[][] a) {
        if(!isValidTable(a)) {
            throw new IllegalArgumentException();
        }
        initTable(a);
    }

    /**
     * Checks whether the specified table is valid.
     * @param a the table to be checked
     * @return {@code true} if the specified table is valid, {@code false} otherwise
     */
    public boolean isValidTable(int[][] a) {
        if(a == null || a.length != 6) {
            return false;
        }
        boolean foundBlack = false;
        for(int[] row : a) {
            if(row == null || row.length != 6) {
                return false;
            }
            for(int space : row) {
                if(space < 0 || space >= Cell.values().length) {
                    return false;
                }
                if(space == Cell.BLACK.getValue()) {
                    if(foundBlack) {
                        return false;
                    }
                    foundBlack = true;
                }
            }
        }
        return foundBlack;
    }

    private void initTable(int[][] a) {
        this.table = new Cell[6][6];
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 6; ++j) {
                this.table[i][j] = Cell.of(a[i][j]);
            }
        }
    }

    /**
     * Checks whether the game is finished with respect to the player who is next.
     *
     * @param player is the player who is next
     * @return {@code true} if the player can not take actions, {@code false} otherwise
     */
    public boolean isFinished(int player) {
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 6; j++) {
                if(isNewPuckAvailable(player, i, j)) {
                    return false;
                }
                if(isPuckOfPlayer(player, i, j) && isMoveAvailable(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks whether is it possible to move the puck from the specified position.
     *
     * @param row the row of the puck to be moved
     * @param col the column of the puck to be moved
     * @return {@code true} if there is at least one possible position, where the puck
     * can be moved to, {@code false} otherwise
     */
    public boolean isMoveAvailable(int row, int col) {
        if(row-2 >= 0) {
            if(table[row-2][col] == Cell.EMPTY) {
                return true;
            }
            if(col-2 >= 0) {
                if(table[row-2][col-2] == Cell.EMPTY) {
                    return true;
                }
            }
            if(col+2 < 6) {
                if(table[row-2][col+2] == Cell.EMPTY) {
                    return true;
                }
            }
        }
        if(row+2 < 6) {
            if(table[row+2][col] == Cell.EMPTY) {
                return true;
            }
            if(col-2 >= 0) {
                if(table[row+2][col-2] == Cell.EMPTY) {
                    return true;
                }
            }
            if(col+2 < 6) {
                if(table[row+2][col+2] == Cell.EMPTY) {
                    return true;
                }
            }
        }
        if(col-2 >= 0) {
            if(table[row][col-2] == Cell.EMPTY) {
                return true;
            }
        }
        if(col+2 < 6) {
            if(table[row][col+2] == Cell.EMPTY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the specified puck is able to be moved to the specified position with
     * the respect to the specified player.
     *
     * @param player the player who is willing to move the puck
     * @param rowfrom the row of the puck to be moved
     * @param colFrom the column of the puck to be moved
     * @param rowTo the row of the desired position
     * @param colTo the column of the desired position
     * @return {@code true} if the specified puck is able to be moved to the specified
     * position by the specified player, {@code false} otherwise
     */
    public boolean isMovableTo(int player, int rowfrom, int colFrom, int rowTo, int colTo) {
        if(player == previousPlayer) {
            return false;
        }
        if(Math.abs(rowfrom-rowTo) > 2 || Math.abs(colFrom-colTo) > 2) {
            return false;
        }
        if(!isEmptyCell(rowTo,colTo)) {
            return false;
        }
        if(Math.abs(rowfrom-rowTo) == 2 && Math.abs(colFrom-colTo) == 0) {
            return true;
        }
        if(Math.abs(rowfrom-rowTo) == 0 && Math.abs(colFrom-colTo) == 2) {
            return true;
        }
        if(Math.abs(rowfrom-rowTo) == 2 && Math.abs(colFrom-colTo) == 2) {
            return true;
        }
        return false;
    }

    /**
     * Returns the number of empty cells in the table. Used in
     * calculating points.
     *
     * @return the number of empty cells in the table
     */
    public int numberOfEmptyCells () {
        int result = 0;
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 6; j++) {
                if(isEmptyCell(i, j)) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * Checks whether the specified cell is an empty cell.
     *
     * @param row the row of the cell to be checked
     * @param col the column of the cell to be checked
     * @return {@code true} if the specified cell is empty, {@code false} otherwise
     */
    public boolean isEmptyCell (int row, int col) {
        return table[row][col] == Cell.EMPTY;
    }

    /**
     * Checks whether the specified cell is black cell.
     *
     * @param row the row of the cell to be checked
     * @param col the column of the cell to be checked
     * @return {@code true} if the specified cell is black, {@code false} otherwise
     */
    public boolean isBlackCell (int row, int col) {
        return table[row][col] == Cell.BLACK;
    }
    /**
     * Moves a puck from a given position to another position by the specific player.
     *
     * @param player the player taking action
     * @param rowFrom the row of the original puck
     * @param colFrom the column of the original puck
     * @param rowTo the row of the new puck
     * @param colTo the column of the new puck
     * @throws IllegalArgumentException if the specified player is not the next player in the order
     */
    public void movePuck (int player, int rowFrom, int colFrom, int rowTo, int colTo) {
        if(!isMovableTo(player,rowFrom,colFrom,rowTo,colTo)) {
            throw new IllegalArgumentException();
        }
        table[rowFrom][colFrom] = Cell.EMPTY;
        table[rowTo][colTo] = Cell.of(player);
        afterStep(player, rowTo, colTo);
        previousPlayer = player;
        log.info("Puck is moved from ({}, {}) to ({}, {})", rowFrom, colFrom, rowTo, colTo);
    }

    /**
     * After a step, changes all puck belonging to the other player in the eight adjacent cells.
     *
     * @param player the player who took the previous step
     * @param row the row of the new or replaced puck
     * @param col the column of the new or replaced puck
     */
    public void afterStep (int player, int row, int col) {
        for(int i = row-1; i<= row+1; i++) {
            if(i < 0 || i >= table.length) {
                continue;
            }
            for(int j = col-1; j<= col+1; j++) {
                if(j < 0 || j >= table.length) {
                    continue;
                }
                if(table[i][j] == Cell.of(player).opposite()) {
                    table[i][j] = Cell.of(player);
                }
            }
        }
    }

    /**
     * Returns whether the puck to be placed has another puck from the same player
     * in the eight adjacent cells.
     * @param player the player who wants to place the new puck
     * @param row the row of the puck to be placed
     * @param col the column of the puck to be placed
     * @return {@code true} if the desired place of the new puck has at least one puck
     * in the eight adjacent cells, {@code false} otherwise
     */
    public boolean isNewPuckAvailable (int player, int row, int col) {
        if(player == previousPlayer) {
            return false;
        }
        if(isBlackCell(row,col)) {
            return false;
        }
        if(!isEmptyCell(row,col)) {
            return false;
        }
        for(int i = row-1; i <= row+1; ++i) {
            if(i < 0 || i >= table.length) {
                continue;
            }
            for(int j = col-1; j <= col+1; ++j) {
                if(j < 0 || j >= table.length) {
                    continue;
                }
                if(table[i][j] == Cell.of(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Places a new puck by the specified player to the specified position.
     *
     * @param player the player who takes action
     * @param row the row of the puck to be placed
     * @param col the column of the puck to be placed
     * @throws IllegalArgumentException if a new puck is available for the specified player,
     * in the specified position
     */
    public void newPuck (int player, int row, int col) {
        if(!isNewPuckAvailable(player, row, col)) {
            throw new IllegalArgumentException();
        }
        table[row][col] = Cell.of(player);
        afterStep(player, row, col);
        previousPlayer = player;
        log.info("New puck is placed at ({}, {})", row, col);
    }

    /**
     * Checks whether the puck at the specified position belongs to the player specified.
     * @param player the player who may own the specified puck
     * @param row the row of the puck to be checked
     * @param col the column of the puck to be checked
     * @return {@code true} if the specified puck belongs to the specified player,
     * {@code false} otherwise
     */
    public boolean isPuckOfPlayer (int player, int row, int col) {
        return table[row][col] == Cell.of(player);
    }

    /**
     * Returns the score of the specified player with respect to the actual state
     * of {@code table}.
     *
     * @param player the player who's points are going to be calculated
     * @return the actual points of the player specified
     */
    public int pointsOfPlayer (int player) {
        int result = 0;
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 6; ++j) {
                if(table[i][j] == Cell.of(player))
                {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * Returns the clone of {@code this} object.
     *
     * @return the clone of {@code this} object
     */
    public TableState clone() {
        TableState copy = null;
        try {
            copy = (TableState) super.clone();
        } catch (CloneNotSupportedException e) {
        }
        copy.table = new Cell[table.length][];
        for (int i = 0; i < table.length; ++i) {
            copy.table[i] = table[i].clone();
        }
        return copy;
    }

    /**
     * Returns the string format of {@code this.table}.
     *
     * @return the string foramt of {@code this.table}
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cell[] row : table) {
            for (Cell cell : row) {
                sb.append(cell).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        TableState state = new TableState();
        System.out.println(state);
        state.newPuck(1,0,1);
        state.isMoveAvailable(0,0);
        System.out.println(state);
    }
}

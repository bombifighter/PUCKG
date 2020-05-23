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
     * A variable to represent the current player who is taking actions.
     */
    @Setter(AccessLevel.NONE)
    private int player;

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

    private boolean isValidTable(int[][] a) {
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
     * Checks whether the game is finished.
     * @return {@code true} if the game is finished, {@code false} otherwise
     */
    public boolean isFinished() {
        for(Cell[] row : table) {
            for(Cell cell : row) {
                if(cell == Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isMoveAvailable (int player, int rowfrom, int colFrom, int rowTo, int colTo) {
        if(Math.abs(rowfrom-rowTo) > 2 || Math.abs(colFrom-colTo) > 2) {
            return false;
        }
        if(!isEmptyCell(rowTo,colTo)) {
            return false;
        }
        if(isBlackCell(rowTo,colTo)) {
            return false;
        }
        if(player == previousPlayer) {
            return false;
        }
        return true;
    }

    public boolean isEmptyCell (int row, int col) {
        return table[row][col] == Cell.EMPTY;
    }

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
        if(!isMoveAvailable(player,rowFrom,colFrom,rowTo,colTo)) {
            throw new IllegalArgumentException();
        }
        table[rowFrom][colFrom] = Cell.EMPTY;
        table[rowTo][colTo] = Cell.of(player);
        afterStep(player, rowTo, colTo);
        previousPlayer = player;
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
     * Places a new puck by the specified player to the specified position
     * @param player the player who takes action
     * @param row the row of the puck to be placed
     * @param col the column of the puck to be placed
     */
    public void newPuck (int player, int row, int col) {
        if(!isNewPuckAvailable(player, row, col)) {
            throw new IllegalArgumentException();
        }
        table[row][col] = Cell.of(player);
        afterStep(player, row, col);
        previousPlayer = player;
    }

    public boolean isPuckOfPlayer (int player, int row, int col) {
        return table[row][col] == Cell.of(player);
    }

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
        state.movePuck(2, 0,5,0,4);
        System.out.println(state);
    }
}

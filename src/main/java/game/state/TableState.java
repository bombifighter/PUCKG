package game.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class TableState implements Cloneable {

    public static final int[][] INITIAL = {
            {1, 0, 0, 0, 0, 2},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 3, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {2, 0, 0, 0, 0, 1}
    };

    @Setter(AccessLevel.NONE)
    private Cell[][] table;

    @Setter(AccessLevel.NONE)
    private int blackRow;

    @Setter(AccessLevel.NONE)
    private int blackCol;

    @Setter(AccessLevel.NONE)
    private int player;

    public TableState() {
        this(INITIAL);
    }

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
        for(int i = 0; i < 6; ++i) {
            for(int j = 0; j < 6; ++j) {
                if((this.table[i][j] = Cell.of(a[i][j])) == Cell.BLACK) {
                    blackRow = i;
                    blackCol = j;
                }
            }
        }
    }

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

    public void movePuck (int player, int rowFrom, int colFrom, int rowTo, int colTo) {
        table[rowFrom][colFrom] = Cell.EMPTY;
        table[rowTo][colTo] = Cell.of(player);
        afterStep(player, rowTo, colTo);
    }

    public void afterStep (int player, int row, int col) {
        for(int i = row-1; i<= row+1; i++) {
            for(int j = col-1; i<= col+1; j++) {
                if(table[i][j] == Cell.of(player).opposite()) {
                    table[i][j] = Cell.of(player);
                }
            }
        }
    }
}

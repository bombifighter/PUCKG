package game.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableStateTest {

    @Test
    void testIsFinished() {
        assertFalse(new TableState().isFinished(1));
        assertTrue(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 0},
                {0, 0, 0, 0, 2, 2},
                {2, 0, 0, 0, 2, 2}}).isFinished(1));
    }

    @Test
    void testIsMoveAvailable() {
        assertTrue(new TableState().isMoveAvailable(0, 0));
        assertTrue(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 2, 2},
                {2, 0, 0, 0, 2, 1}}).isMoveAvailable(5, 5));
        assertTrue(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 0},
                {0, 0, 0, 0, 2, 2},
                {2, 0, 0, 2, 2, 1}}).isMoveAvailable(5, 5));
        assertTrue(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 2, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 2, 0, 1, 2},
                {2, 0, 0, 2, 2, 2}}).isMoveAvailable(4, 4));
        assertFalse(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 2, 2},
                {2, 0, 0, 2, 2, 1}}).isMoveAvailable(5, 5));
    }

    @Test
    void testIsMovableTo() {
        assertTrue(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 1, 2},
                {2, 0, 0, 2, 2, 1}}).isMovableTo(1, 4, 4, 2, 2));
        assertFalse(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 1, 2},
                {2, 0, 0, 2, 2, 1}}).isMovableTo(2, 1, 4, 2, 4));
    }

    @Test
    void testNumberOfEmptyCells() {
        assertEquals(31, new TableState().numberOfEmptyCells());
        assertEquals(0, new TableState(new int[][] {
                {2, 2, 2, 2, 2, 2},
                {2, 2, 1, 1, 2, 1},
                {2, 1, 2, 2, 1, 2},
                {2, 1, 2, 3, 1, 2},
                {2, 1, 2, 2, 1, 2},
                {2, 2, 2, 2, 2, 1}}).numberOfEmptyCells());
    }

    @Test
    void testIsEmptyCell() {
        assertTrue(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 1, 2},
                {2, 0, 0, 2, 2, 1}}).isEmptyCell(4, 2));
        assertFalse(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 1, 2},
                {2, 0, 0, 2, 2, 1}}).isEmptyCell(0, 0));
    }

    @Test
    void testIsBlackCell() {
        assertTrue(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 1, 2},
                {2, 0, 0, 2, 2, 1}}).isBlackCell(3, 3));
        assertFalse(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 1, 2},
                {2, 0, 0, 2, 2, 1}}).isBlackCell(2, 2));
    }

    @Test
    void testMovePuck() {
        TableState state = new TableState();
        state.movePuck(1,0,0,2,0);
        assertEquals(Cell.RED, state.getTable()[2][0]);
        assertThrows(IllegalArgumentException.class, () -> state.movePuck(2, 0, 5, 3, 5));
    }

    @Test
    void testAfterStep() {
        TableState state = new TableState(new int[][] {
                {2, 2, 2, 0, 0, 2},
                {2, 1, 2, 0, 2, 0},
                {2, 2, 2, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 1, 2},
                {2, 0, 0, 2, 2, 1}});
        state.afterStep(1,1,1);
        assertEquals(Cell.RED, state.getTable()[0][0]);
        assertEquals(Cell.RED, state.getTable()[2][2]);
        assertEquals(Cell.RED, state.getTable()[0][1]);
        assertEquals(Cell.RED, state.getTable()[1][2]);
    }

    @Test
    void testIsNewPuckAvailable() {
        assertTrue(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 1, 2},
                {2, 0, 0, 2, 2, 1}}).isNewPuckAvailable(1, 4, 3));
        assertFalse(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 1, 2},
                {2, 0, 0, 2, 2, 1}}).isNewPuckAvailable(1, 5, 2));
    }

    @Test
    void testNewPuck() {
        TableState state = new TableState();
        state.newPuck(1,0,1);
        assertEquals(Cell.RED, state.getTable()[0][1]);
        assertThrows(IllegalArgumentException.class, () -> state.newPuck(2, 5, 3));
    }

    @Test
    void testIsPuckOfPlayer() {
        assertTrue(new TableState().isPuckOfPlayer(1,0,0));
        assertFalse(new TableState().isPuckOfPlayer(2,0,0));
        assertFalse(new TableState().isPuckOfPlayer(1,3,3));
        assertFalse(new TableState().isPuckOfPlayer(2,3,3));
    }

    @Test
    void testPointsOfPlayer() {
        TableState state = new TableState();
        assertEquals(2, state.pointsOfPlayer(1));
        assertEquals(2, state.pointsOfPlayer(2));
        state.newPuck(1,0,1);
        assertEquals(3, state.pointsOfPlayer(1));
        assertEquals(2, state.pointsOfPlayer(2));
    }

    @Test
    void testToString() {
        TableState state = new TableState();
        assertEquals("1 0 0 0 0 2 \n"
                + "0 0 0 0 0 0 \n"
                + "0 0 0 0 0 0 \n"
                + "0 0 0 3 0 0 \n"
                + "0 0 0 0 0 0 \n"
                + "2 0 0 0 0 1 \n", state.toString());
    }
}
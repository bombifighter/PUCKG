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
    void isMovable() {
        assertTrue(new TableState().isMovable(0, 0));
        assertFalse(new TableState(new int[][] {
                {2, 2, 0, 0, 0, 2},
                {0, 2, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 3, 0, 2},
                {0, 0, 0, 0, 2, 2},
                {2, 0, 0, 2, 2, 1}}).isMovable(5, 5));
    }

    @Test
    void isMoveAvailable() {
    }

    @Test
    void isEmptyCell() {
    }

    @Test
    void isBlackCell() {
    }

    @Test
    void movePuck() {
    }

    @Test
    void afterStep() {
    }

    @Test
    void isNewPuckAvailable() {
    }

    @Test
    void newPuck() {
    }

    @Test
    void isPuckOfPlayer() {
    }

    @Test
    void pointsOfPlayer() {
    }

    @Test
    void testToString() {
    }
}
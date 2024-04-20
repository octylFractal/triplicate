package net.octyl.triplicate;

/**
 * Represents a cell in a Mini Cactpot game.
 */
public enum Cell {
    TOP_LEFT(0, 0),
    TOP_CENTER(1, 0),
    TOP_RIGHT(2, 0),
    MIDDLE_LEFT(0, 1),
    MIDDLE_CENTER(1, 1),
    MIDDLE_RIGHT(2, 1),
    BOTTOM_LEFT(0, 2),
    BOTTOM_CENTER(1, 2),
    BOTTOM_RIGHT(2, 2),
    ;

    private final int x;
    private final int y;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public String toString() {
        return "Cell{x=" + x + ", y=" + y + '}';
    }
}

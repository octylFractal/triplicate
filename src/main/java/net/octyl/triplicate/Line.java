package net.octyl.triplicate;

import java.util.Set;

/**
 * A line of three numbers in a Mini Cactpot game.
 */
public enum Line {
    BOTTOM_ROW(Cell.BOTTOM_LEFT, Cell.BOTTOM_CENTER, Cell.BOTTOM_RIGHT),
    MIDDLE_ROW(Cell.MIDDLE_LEFT, Cell.MIDDLE_CENTER, Cell.MIDDLE_RIGHT),
    TOP_ROW(Cell.TOP_LEFT, Cell.TOP_CENTER, Cell.TOP_RIGHT),
    LEFT_DIAGONAL(Cell.TOP_LEFT, Cell.MIDDLE_CENTER, Cell.BOTTOM_RIGHT),
    LEFT_COLUMN(Cell.TOP_LEFT, Cell.MIDDLE_LEFT, Cell.BOTTOM_LEFT),
    CENTER_COLUMN(Cell.TOP_CENTER, Cell.MIDDLE_CENTER, Cell.BOTTOM_CENTER),
    RIGHT_COLUMN(Cell.TOP_RIGHT, Cell.MIDDLE_RIGHT, Cell.BOTTOM_RIGHT),
    RIGHT_DIAGONAL(Cell.TOP_RIGHT, Cell.MIDDLE_CENTER, Cell.BOTTOM_LEFT),
    ;

    private final Set<Cell> cells;

    Line(Cell... cells) {
        this.cells = Set.of(cells);
    }

    public Set<Cell> cells() {
        return cells;
    }
}

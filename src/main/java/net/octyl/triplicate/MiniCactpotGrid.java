package net.octyl.triplicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SplittableRandom;

/**
 * Represents a 3x3 grid of numbers in a Mini Cactpot game.
 */
public record MiniCactpotGrid(Map<Cell, Integer> grid) {
    public static final Set<Integer> VALID_NUMBERS = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    public static MiniCactpotGrid generate() {
        var random = new SplittableRandom();
        var cells = new ArrayList<>(Arrays.asList(Cell.values()));
        Collections.shuffle(cells, random);
        return new MiniCactpotGrid(Map.of(
            cells.get(0), 1,
            cells.get(1), 2,
            cells.get(2), 3,
            cells.get(3), 4,
            cells.get(4), 5,
            cells.get(5), 6,
            cells.get(6), 7,
            cells.get(7), 8,
            cells.get(8), 9
        ));
    }

    public MiniCactpotGrid {
        if (grid.size() != 9) {
            throw new IllegalArgumentException("Grid must have 9 cells");
        }
        if (!grid.keySet().containsAll(Arrays.asList(Cell.values()))) {
            throw new IllegalArgumentException("Grid must have all cells");
        }
        if (!grid.values().containsAll(VALID_NUMBERS)) {
            throw new IllegalArgumentException("Grid must have all numbers 1-9");
        }
    }
}

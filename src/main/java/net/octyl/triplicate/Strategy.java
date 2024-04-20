package net.octyl.triplicate;

import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A strategy for playing the mini cactpot game.
 */
public enum Strategy {
    /**
     * Plays a random line every time.
     */
    RANDOM_LINE {
        @Override
        public PlayerView play(PlayerView.Playing playing) {
            return playing.selectLine(Randomizer.randomEnum(Line.class));
        }
    },
    /**
     * Picks random cells, then plays the line with the best expected value.
     */
    RANDOM_CELLS {
        @Override
        public PlayerView play(PlayerView.Playing playing) {
            if (playing.revealed().size() < PlayerView.Playing.MAX_REVEALED) {
                Cell toReveal = Randomizer.randomEnum(Cell.class);
                while (playing.revealed().contains(toReveal)) {
                    toReveal = Randomizer.randomEnum(Cell.class);
                }
                return playing.reveal(toReveal);
            }
            var bestLine = Arrays.stream(Line.values())
                .max((line1, line2) -> Float.compare(
                    expectedValue(line1, playing),
                    expectedValue(line2, playing)
                ))
                .orElseThrow(() -> new IllegalStateException("No lines found"));
            return playing.selectLine(bestLine);
        }
    },
    /**
     * Picks a random cell in the line with the best expected value, then plays the best line.
     */
    BEST_CELL_SINGLE_LINE {
        @Override
        public PlayerView play(PlayerView.Playing playing) {
            var bestLine = Arrays.stream(Line.values())
                .max((line1, line2) -> Float.compare(
                    expectedValue(line1, playing),
                    expectedValue(line2, playing)
                ))
                .orElseThrow(() -> new IllegalStateException("No lines found"));
            if (playing.revealed().size() < PlayerView.Playing.MAX_REVEALED) {
                var cellToPick = bestLine.cells().stream()
                    .filter(cell -> !playing.revealed().contains(cell))
                    .findAny();
                if (cellToPick.isPresent()) {
                    return playing.reveal(cellToPick.get());
                }
            }
            return playing.selectLine(bestLine);
        }
    },
    ;

    /**
     * Calculate expected value of a line given the known values in the line and grid.
     *
     * @param line the line to calculate the expected value of
     * @param playerView the information to use for the calculation
     * @return the expected value of the line in MGP
     */
    public static float expectedValue(Line line, PlayerView.Playing playerView) {
        int numUnknown = 0;
        int baseSum = 0;
        for (Cell cell : line.cells()) {
            if (playerView.revealed().contains(cell)) {
                baseSum += playerView.grid().grid().get(cell);
            } else {
                numUnknown++;
            }
        }
        if (numUnknown == 0) {
            // We know the exact value, so just use that
            return value(baseSum);
        }
        // We don't know the exact value, so calculate the expected value

        // Figure out what cell values could be under the hidden cells
        Set<Integer> visibleValues = playerView.grid().grid().entrySet().stream()
            .filter(entry -> playerView.revealed().contains(entry.getKey()))
            .map(Map.Entry::getValue)
            .collect(Collectors.toSet());
        Set<Integer> hiddenValues = MiniCactpotGrid.VALID_NUMBERS.stream()
            .filter(value -> !visibleValues.contains(value))
            .collect(Collectors.toSet());

        // Try every possible combination of hidden values (order doesn't matter)
        int numCombinations = 0;
        int totalValue = 0;
        for (Set<Integer> combination : Sets.combinations(hiddenValues, numUnknown)) {
            int sum = baseSum + combination.stream().mapToInt(Integer::intValue).sum();
            totalValue += value(sum);
            numCombinations++;
        }
        return (float) totalValue / numCombinations;
    }

    /**
     * {@return the value of a sum in MGP}
     *
     * @param sum the sum to get the value of
     */
    public static int value(int sum) {
        return switch (sum) {
            case 6 -> 10_000;
            case 7, 19 -> 36;
            case 8 -> 720;
            case 9 -> 360;
            case 10 -> 80;
            case 11 -> 252;
            case 12 -> 108;
            case 13, 16 -> 72;
            case 14 -> 54;
            case 15, 17 -> 180;
            case 18 -> 119;
            case 20 -> 306;
            case 21 -> 1080;
            case 22 -> 144;
            case 23 -> 1800;
            case 24 -> 3600;
            default -> throw new IllegalArgumentException("Invalid sum: " + sum);
        };
    }

    public abstract PlayerView play(PlayerView.Playing playing);
}

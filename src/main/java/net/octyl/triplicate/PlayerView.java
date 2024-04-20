package net.octyl.triplicate;

import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public sealed interface PlayerView {
    record NotPlaying() implements PlayerView {
        public Playing startGame() {
            return startGame(MiniCactpotGrid.generate(), Set.of(Randomizer.randomEnum(Cell.class)));
        }

        public Playing startGame(MiniCactpotGrid grid, Set<Cell> revealed) {
            return new Playing(grid, revealed, null, null);
        }
    }

    record Playing(
        MiniCactpotGrid grid,
        Set<Cell> revealed,
        @Nullable Cell hoveredCell,
        @Nullable Line hoveredLine
    ) implements PlayerView {
        public static final int MAX_REVEALED = 4;

        public Playing {
            if (revealed.size() > MAX_REVEALED) {
                throw new IllegalArgumentException("Too many revealed cells");
            }
        }

        public Set<Cell> hoveredCells() {
            var cells = new HashSet<Cell>();
            if (hoveredCell != null) {
                cells.add(hoveredCell);
            }
            if (hoveredLine != null) {
                cells.addAll(hoveredLine.cells());
            }
            return Set.copyOf(cells);
        }

        public Playing hoverCell(@Nullable Cell cell) {
            return new Playing(grid, revealed, cell, null);
        }

        public Playing hoverLine(@Nullable Line line) {
            return new Playing(grid, revealed, null, line);
        }

        public PlayerView reveal(Cell cell) {
            if (revealed.size() >= MAX_REVEALED) {
                return this;
            }
            if (revealed.contains(cell)) {
                return this;
            }
            var newRevealed = new HashSet<>(revealed);
            newRevealed.add(cell);
            var hoveredCell = this.hoveredCell;
            if (newRevealed.size() == MAX_REVEALED) {
                hoveredCell = null;
            }
            return new Playing(grid, Set.copyOf(newRevealed), hoveredCell, hoveredLine);

        }

        public Finished selectLine(Line line) {
            return new Finished(grid, line);
        }
    }

    record Finished(
        MiniCactpotGrid grid,
        Line selected
    ) implements PlayerView {
    }
}

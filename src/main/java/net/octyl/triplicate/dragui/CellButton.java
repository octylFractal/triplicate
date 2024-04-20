package net.octyl.triplicate.dragui;

import net.octyl.triplicate.Cell;
import net.octyl.triplicate.MiniCactpotGrid;
import net.octyl.triplicate.PlayerView;
import net.octyl.triplicate.Triplicate;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgCircle;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgStroke;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeColor;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeWidth;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextAlign;

public class CellButton implements Component {
    /**
     * Adjustment to make the text look nice in the center of the circles. Almost certainly subjective.
     */
    private static final float MAKE_IT_LOOK_NICE_ADJUSTMENT = 2f;

    private final Cell cell;
    private final float centerX;
    private final float centerY;
    private final AtomicReference<PlayerView> playerViewRef;

    public CellButton(
        Cell cell, float centerX, float centerY, AtomicReference<PlayerView> playerViewRef
    ) {
        this.cell = cell;
        this.centerX = centerX;
        this.centerY = centerY;
        this.playerViewRef = playerViewRef;
    }

    @Override
    public boolean onMouseMove(float x, float y) {
        if (new Circle(centerX, centerY, Triplicate.CIRCLE_SIZE / 2).contains(x, y)) {
            playerViewRef.updateAndGet(view -> {
                if (view instanceof PlayerView.Playing playing
                    && playing.revealed().size() < PlayerView.Playing.MAX_REVEALED
                    && !playing.revealed().contains(cell)) {
                    return playing.hoverCell(cell);
                }
                return view;
            });
        } else {
            playerViewRef.updateAndGet(view -> {
                if (view instanceof PlayerView.Playing playing && playing.hoveredCell() == cell) {
                    return playing.hoverCell(null);
                }
                return view;
            });
        }
        return false;
    }

    @Override
    public boolean onMouseLeftClick(float x, float y) {
        if (new Circle(centerX, centerY, Triplicate.CIRCLE_SIZE / 2).contains(x, y)) {
            playerViewRef.updateAndGet(view -> {
                if (view instanceof PlayerView.Playing playing
                    && playing.revealed().size() < PlayerView.Playing.MAX_REVEALED) {
                    return playing.reveal(cell);
                }
                return view;
            });
            return true;
        }
        return false;
    }

    @Override
    public void render(long ctx, double deltaTime) {
        PlayerView playerView = playerViewRef.get();
        MiniCactpotGrid grid = switch (playerView) {
            case PlayerView.Playing playing -> playing.grid();
            case PlayerView.Finished finished -> finished.grid();
            default -> null;
        };
        if (grid == null) {
            return;
        }
        Set<Cell> hoveredCells = playerView instanceof PlayerView.Playing playing ? playing.hoveredCells() : Set.of();
        Set<Cell> selectedCells = playerView instanceof PlayerView.Finished finished ? finished.selected().cells() : Set.of();
        nvgStrokeWidth(ctx, 5f);
        nvgFillColor(ctx, hoveredCells.contains(cell) ? Triplicate.HOVER_COLOR : Triplicate.BASE_COLOR);
        nvgStrokeColor(ctx, selectedCells.contains(cell) ? Triplicate.SELECTED_COLOR : Triplicate.BASE_COLOR);
        nvgBeginPath(ctx);
        nvgCircle(
            ctx,
            centerX,
            centerY,
            Triplicate.CIRCLE_SIZE / 2
        );
        nvgFill(ctx);
        nvgStroke(ctx);
        nvgFillColor(ctx, Triplicate.TEXT_COLOR);
        nvgFontSize(ctx, 48);
        nvgFontFace(ctx, Triplicate.NVG_NUMBERS_FONT);
        nvgTextAlign(ctx, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
        if ((playerView instanceof PlayerView.Playing playing && playing.revealed().contains(cell))
            || playerView instanceof PlayerView.Finished) {
            nvgText(
                ctx,
                centerX,
                centerY + MAKE_IT_LOOK_NICE_ADJUSTMENT,
                String.valueOf(grid.grid().get(cell))
            );
        }
    }
}

package net.octyl.triplicate.dragui;

import net.octyl.triplicate.Line;
import net.octyl.triplicate.PlayerView;
import net.octyl.triplicate.Triplicate;
import org.lwjgl.nanovg.NVGColor;

import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgCircle;
import static org.lwjgl.nanovg.NanoVG.nvgClosePath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgLineTo;
import static org.lwjgl.nanovg.NanoVG.nvgMoveTo;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgRotate;
import static org.lwjgl.nanovg.NanoVG.nvgStroke;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeColor;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeWidth;
import static org.lwjgl.nanovg.NanoVG.nvgTranslate;

public final class LineSelectorButton implements Component {
    private static final float LINE_SELECTOR_SIZE = Triplicate.CIRCLE_SIZE / 1.75f;
    private static final float LINE_SELECTOR_STROKE = LINE_SELECTOR_SIZE / 16;

    private final Line line;
    private final float centerX;
    private final float centerY;
    private final float rotationDeg;
    private final AtomicReference<PlayerView> playerViewRef;

    public LineSelectorButton(
        Line line, float centerX, float centerY, float rotationDeg, AtomicReference<PlayerView> playerViewRef
    ) {
        this.line = line;
        this.centerX = centerX;
        this.centerY = centerY;
        this.rotationDeg = rotationDeg;
        this.playerViewRef = playerViewRef;
    }

    @Override
    public boolean onMouseMove(float x, float y) {
        if (new Circle(centerX, centerY, LINE_SELECTOR_SIZE / 2).contains(x, y)) {
            playerViewRef.updateAndGet(view -> {
                if (view instanceof PlayerView.Playing playing) {
                    return playing.hoverLine(line);
                }
                return view;
            });
        } else {
            playerViewRef.updateAndGet(view -> {
                if (view instanceof PlayerView.Playing playing && playing.hoveredLine() == line) {
                    return playing.hoverLine(null);
                }
                return view;
            });
        }
        return false;
    }

    @Override
    public boolean onMouseLeftClick(float x, float y) {
        if (new Circle(centerX, centerY, LINE_SELECTOR_SIZE / 2).contains(x, y)) {
            playerViewRef.updateAndGet(view -> {
                if (view instanceof PlayerView.Playing playing) {
                    return playing.selectLine(line);
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
        if (playerView instanceof PlayerView.NotPlaying) {
            return;
        }
        nvgTranslate(ctx, centerX, centerY);
        nvgRotate(ctx, (float) Math.toRadians(rotationDeg));
        NVGColor color;
        if (playerView instanceof PlayerView.Finished finished && finished.selected() == line) {
            color = Triplicate.SELECTED_COLOR;
        } else if (playerView instanceof PlayerView.Playing playing && playing.hoveredLine() == line) {
            color = Triplicate.HOVER_COLOR;
        } else {
            color = Triplicate.BASE_COLOR;
        }
        nvgStrokeColor(ctx, color);
        nvgFillColor(ctx, color);
        // Draw the circle
        nvgStrokeWidth(ctx, LINE_SELECTOR_STROKE);
        nvgBeginPath(ctx);
        nvgCircle(ctx, 0, 0, LINE_SELECTOR_SIZE / 2);
        nvgStroke(ctx);
        // Draw the arrow pointing to the right
        nvgBeginPath(ctx);
        nvgMoveTo(ctx, LINE_SELECTOR_SIZE / 4 - 2, -LINE_SELECTOR_SIZE / 4);
        nvgLineTo(ctx, LINE_SELECTOR_SIZE / 2 - 2, 0);
        nvgLineTo(ctx, LINE_SELECTOR_SIZE / 4 - 2, LINE_SELECTOR_SIZE / 4);
        nvgLineTo(ctx, LINE_SELECTOR_SIZE / 4 - 2, -LINE_SELECTOR_SIZE / 4);
        nvgClosePath(ctx);
        nvgRect(
            ctx,
            -LINE_SELECTOR_SIZE / 4 - 2,
            -LINE_SELECTOR_STROKE / 2,
            LINE_SELECTOR_SIZE / 2,
            LINE_SELECTOR_STROKE
        );
        nvgFill(ctx);
    }
}

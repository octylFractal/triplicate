
package net.octyl.triplicate.dragui;

import net.octyl.triplicate.Strategy;
import net.octyl.triplicate.Triplicate;
import org.jspecify.annotations.Nullable;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgRoundedRect;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextAlign;
import static org.lwjgl.nanovg.NanoVG.nvgTextBounds;

public final class TryStrategyButton implements Component {
    private final Strategy strategy;
    private final float centerX;
    private final float centerY;
    private final float padding;
    private final AtomicReference<Strategist.State> stateRef;
    private boolean hovered = false;

    private transient @Nullable Rectangle bounds;

    public TryStrategyButton(
        Strategy strategy, float centerX, float centerY, float padding, AtomicReference<Strategist.State> stateRef
    ) {
        this.strategy = strategy;
        this.centerX = centerX;
        this.centerY = centerY;
        this.padding = padding;
        this.stateRef = stateRef;
    }

    private void init(long ctx) {
        try (var stack = MemoryStack.stackPush()) {
            FloatBuffer boundsBuffer = stack.mallocFloat(4);
            nvgTextAlign(ctx, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
            nvgTextBounds(ctx, centerX, centerY, strategy.toString(), boundsBuffer);
            float xMin = boundsBuffer.get(0);
            float xMax = boundsBuffer.get(2);
            float yMin = boundsBuffer.get(1);
            float yMax = boundsBuffer.get(3);
            bounds = new Rectangle(
                xMin - padding,
                yMin - padding,
                xMax - xMin + 2 * padding,
                yMax - yMin + 2 * padding
            );
        }
    }

    @Override
    public boolean onMouseMove(float x, float y) {
        if (bounds != null) {
            hovered = bounds.contains(x, y);
        }
        return false;
    }

    @Override
    public boolean onMouseLeftClick(float x, float y) {
        if (bounds != null && bounds.contains(x, y)) {
            stateRef.updateAndGet(state -> switch (state.strategy()) {
                case Strategy s when s == strategy -> state.withStrategy(null);
                case null, default -> new Strategist.State(strategy);
            });
            return true;
        }
        return false;
    }

    @Override
    public void render(long ctx, double deltaTime) {
        if (bounds == null) {
            init(ctx);
        }
        nvgBeginPath(ctx);
        nvgRoundedRect(ctx, bounds.x(), bounds.y(), bounds.width(), bounds.height(), 5);
        NVGColor color;
        if (stateRef.get().strategy() == strategy) {
            color = Triplicate.SELECTED_COLOR;
        } else if (hovered) {
            color = Triplicate.HOVER_COLOR;
        } else {
            color = Triplicate.BASE_COLOR;
        }
        nvgFillColor(ctx, color);
        nvgFill(ctx);
        nvgFillColor(ctx, Triplicate.TEXT_COLOR);
        nvgTextAlign(ctx, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
        nvgText(ctx, centerX, centerY, strategy.toString());
    }
}

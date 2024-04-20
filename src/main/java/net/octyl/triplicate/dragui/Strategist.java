package net.octyl.triplicate.dragui;

import net.octyl.triplicate.PlayerView;
import net.octyl.triplicate.Strategy;
import net.octyl.triplicate.Triplicate;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_TOP;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgTextAlign;
import static org.lwjgl.nanovg.NanoVG.nvgTextBox;

public class Strategist implements Component {
    public record State(
        @Nullable Strategy strategy,
        double timeUntilActive,
        int numTries,
        int totalScore
    ) {
        public State(@Nullable Strategy strategy) {
            this(strategy, 0, 0, 0);
        }

        public float averageScore() {
            return numTries == 0 ? 0 : totalScore / (float) numTries;
        }

        public State withTimeUntilActive(double timeUntilActive) {
            return new State(strategy, timeUntilActive, numTries, totalScore);
        }

        public State addTry(int score) {
            return new State(strategy, timeUntilActive, numTries + 1, totalScore + score);
        }

        public State withStrategy(@Nullable Strategy strategy) {
            return new State(strategy, timeUntilActive, numTries, totalScore);
        }
    }

    private final float topLeftX;
    private final float topLeftY;
    private final AtomicReference<State> stateRef;
    private final AtomicReference<PlayerView> playerViewRef;

    public Strategist(
        float topLeftX, float topLeftY, AtomicReference<State> stateRef, AtomicReference<PlayerView> playerViewRef
    ) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.stateRef = stateRef;
        this.playerViewRef = playerViewRef;
    }

    @Override
    public void render(long ctx, double deltaTime) {
        nvgFontSize(ctx, 24);
        nvgFillColor(ctx, Triplicate.LIGHT_TEXT_COLOR);
        nvgTextAlign(ctx, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
        var state = stateRef.get();
        nvgTextBox(
            ctx, topLeftX, topLeftY, 1080,
            """
            Strategy: %s
            Attempts: %d
            Average: %.2f
            """.formatted(
                state.strategy == null ? "None running" : state.strategy.toString(),
                state.numTries,
                state.averageScore()
            )
        );

        updateState(deltaTime);
    }

    private void updateState(double deltaTime) {
        State currentState = stateRef.updateAndGet(state -> state.withTimeUntilActive(state.timeUntilActive - deltaTime));
        if (currentState.strategy == null) {
            return;
        }
        if (currentState.timeUntilActive > 0) {
            return;
        }
        var currentPlayerView = playerViewRef.updateAndGet(view -> switch (view) {
            case PlayerView.NotPlaying notPlaying -> notPlaying.startGame();
            case PlayerView.Playing playing -> currentState.strategy.play(playing);
            case PlayerView.Finished ignored -> new PlayerView.NotPlaying().startGame();
        });
        var newState = currentState.withTimeUntilActive(currentState.timeUntilActive + 0.4);
        if (currentPlayerView instanceof PlayerView.Finished finished) {
            newState = newState.addTry(
                Strategy.value(finished.selected().cells().stream().mapToInt(finished.grid().grid()::get).sum())
            );
        }
        if (!stateRef.compareAndSet(currentState, newState)) {
            // This shouldn't happen yet, since it's single-threaded, but just in case I forget...
            throw new IllegalStateException("State changed while finishing");
        }
    }
}

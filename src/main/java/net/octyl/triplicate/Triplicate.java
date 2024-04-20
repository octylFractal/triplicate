/*
 * This source file was generated by the Gradle 'init' task
 */
package net.octyl.triplicate;

import net.octyl.triplicate.dragui.CellButton;
import net.octyl.triplicate.dragui.Component;
import net.octyl.triplicate.dragui.LineSelectorButton;
import net.octyl.triplicate.dragui.StartGameButton;
import net.octyl.triplicate.dragui.Strategist;
import net.octyl.triplicate.dragui.TryStrategyButton;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static net.octyl.triplicate.NVGUtil.nvgTemp;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SCALE_TO_MONITOR;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgCreateFontMem;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.nanovg.NanoVG.nvgScale;
import static org.lwjgl.nanovg.NanoVG.nvgTranslate;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
import static org.lwjgl.nanovg.NanoVGGL3.nvgCreate;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Triplicate {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private static final Color BG_COLOR = Color.fromHex(0x224B6D);
    public static final NVGColor BASE_COLOR = Color.fromHex(0xFDFECB).toNVGColor();
    public static final NVGColor HOVER_COLOR = Color.fromHex(0xF29559).toNVGColor();
    public static final NVGColor SELECTED_COLOR = Color.fromHex(0xBBB6DF).toNVGColor();
    public static final NVGColor TEXT_COLOR = Color.fromHex(0xA52D27).toNVGColor();
    public static final NVGColor LIGHT_TEXT_COLOR = Color.fromHex(0xF6DCDA).toNVGColor();

    public static void main(String[] args) {
        GLFWErrorCallback.createPrint(System.err).set();
        try {
            if (!glfwInit()) {
                throw new IllegalStateException("Unable to initialize GLFW");
            }

            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // pending event support
            glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);

            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

            long window = glfwCreateWindow(WIDTH, HEIGHT, "Triplicate", NULL, NULL);
            if (window == NULL) {
                throw new IllegalStateException("Failed to create the GLFW window");
            }
            center(window);
            glfwMakeContextCurrent(window);
            glfwSwapInterval(1);
            glfwShowWindow(window);

            GL.createCapabilities();

            glClearColor(BG_COLOR.redF(), BG_COLOR.greenF(), BG_COLOR.blueF(), 1.0f);

            long ctx = initNvg();

            AtomicReference<SizeState> sizeStateRef = initSizeState(window);

            loop(window, ctx, sizeStateRef);
        } finally {
            Objects.requireNonNull(glfwSetErrorCallback(null)).free();
            glfwTerminate();
        }
    }

    private static AtomicReference<SizeState> initSizeState(long window) {
        var ref = new AtomicReference<>(new SizeState(new Dimensions(0, 0), new Dimensions(0, 0)));
        GLFWWindowSizeCallback.create((win, width, height) -> ref.updateAndGet(state -> state.updateWin(width, height)))
            .set(window);
        GLFWFramebufferSizeCallback.create((win, fbWidth, fbHeight) -> ref.updateAndGet(state -> state.updateFb(fbWidth, fbHeight)))
            .set(window);
        try (var stack = MemoryStack.stackPush()) {
            var pWidth = stack.mallocInt(1);
            var pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            ref.updateAndGet(state -> state.updateWin(pWidth.get(0), pHeight.get(0)));
            glfwGetFramebufferSize(window, pWidth, pHeight);
            ref.updateAndGet(state -> state.updateFb(pWidth.get(0), pHeight.get(0)));
        }
        return ref;
    }

    private static void center(long window) {
        try (var stack = MemoryStack.stackPush()) {
            var pWidth = stack.mallocInt(1);
            var pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            if (vidmode == null) {
                throw new IllegalStateException("Failed to get the video mode");
            }

            glfwSetWindowPos(
                window,
                (vidmode.width() - pWidth.get(0)) / 2,
                (vidmode.height() - pHeight.get(0)) / 2
            );
        }
    }

    public static final String NVG_NUMBERS_FONT = "numbers";
    private static final ByteBuffer NUMBERS_FONT_DATA;

    static {
        try {
            NUMBERS_FONT_DATA = IO.loadResource("Dosis-Regular.ttf");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load font", e);
        }
    }

    private static long initNvg() {
        long ctx = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
        if (ctx == NULL) {
            throw new IllegalStateException("Failed to create NanoVG context");
        }
        nvgCreateFontMem(ctx, NVG_NUMBERS_FONT, NUMBERS_FONT_DATA, false);
        return ctx;
    }

    private record SizeState(
        Dimensions win,
        Dimensions fb
    ) {
        SizeState updateWin(int width, int height) {
            return new SizeState(new Dimensions(width, height), fb);
        }

        SizeState updateFb(int width, int height) {
            return new SizeState(win, new Dimensions(width, height));
        }

        float pxRatio() {
            return (float) fb.width() / win.width();
        }

        /**
         * Fit the given dimensions into the current window size, retaining aspect ratio.
         *
         * @param expected the expected dimensions
         * @return the fitted dimensions
         */
        Dimensions fit(Dimensions expected) {
            var winRatio = (float) win.width() / win.height();
            var expectedRatio = (float) expected.width() / expected.height;

            if (winRatio > expectedRatio) {
                // window is wider than expected
                int height = win.height();
                var width = (int) (height * expectedRatio);
                return new Dimensions(width, height);
            } else {
                // window is taller than expected
                int width = win.width();
                var height = (int) (width / expectedRatio);
                return new Dimensions(width, height);
            }
        }
    }

    private record Dimensions(int width, int height) {
    }

    private static void loop(
        long window, long ctx, AtomicReference<SizeState> sizeStateRef
    ) {
        var playerViewRef = new AtomicReference<PlayerView>(new PlayerView.NotPlaying());
        var stateRef = new AtomicReference<>(new Strategist.State(null));

        var components = new HashSet<Component>();
        components.add(new StartGameButton(
            "Start New Game",
            (float) WIDTH / 2,
            100,
            10,
            playerViewRef
        ));
        for (Strategy strategy : Strategy.values()) {
            components.add(new TryStrategyButton(
                strategy,
                100,
                100 + 50 * strategy.ordinal(),
                10,
                stateRef
            ));
        }
        components.add(new Strategist(0, 0, stateRef, playerViewRef));
        components.add(new LineSelectorButton(
            Line.BOTTOM_ROW,
            gridCellCenterX(0) - CIRCLE_SIZE,
            gridCellCenterY(2),
            0,
            playerViewRef
        ));
        components.add(new LineSelectorButton(
            Line.MIDDLE_ROW,
            gridCellCenterX(0) - CIRCLE_SIZE,
            gridCellCenterY(1),
            0,
            playerViewRef
        ));
        components.add(new LineSelectorButton(
            Line.TOP_ROW,
            gridCellCenterX(0) - CIRCLE_SIZE,
            gridCellCenterY(0),
            0,
            playerViewRef
        ));
        components.add(new LineSelectorButton(
            Line.LEFT_DIAGONAL,
            gridCellCenterX(0) - CIRCLE_SIZE,
            gridCellCenterY(0) - CIRCLE_SIZE,
            45,
            playerViewRef
        ));
        components.add(new LineSelectorButton(
            Line.LEFT_COLUMN,
            gridCellCenterX(0),
            gridCellCenterY(0) - CIRCLE_SIZE,
            90,
            playerViewRef
        ));
        components.add(new LineSelectorButton(
            Line.CENTER_COLUMN,
            gridCellCenterX(1),
            gridCellCenterY(0) - CIRCLE_SIZE,
            90,
            playerViewRef
        ));
        components.add(new LineSelectorButton(
            Line.RIGHT_COLUMN,
            gridCellCenterX(2),
            gridCellCenterY(0) - CIRCLE_SIZE,
            90,
            playerViewRef
        ));
        components.add(new LineSelectorButton(
            Line.RIGHT_DIAGONAL,
            gridCellCenterX(2) + CIRCLE_SIZE,
            gridCellCenterY(0) - CIRCLE_SIZE,
            135,
            playerViewRef
        ));
        for (Cell cell : Cell.values()) {
            components.add(new CellButton(
                cell,
                gridCellCenterX(cell.x()),
                gridCellCenterY(cell.y()),
                playerViewRef
            ));
        }

        GLFWMouseButtonCallback.create((win, button, action, mods) -> {
            if (action == GLFW_RELEASE && button == 0) {
                try (var stack = MemoryStack.stackPush()) {
                    var x = stack.mallocDouble(1);
                    var y = stack.mallocDouble(1);
                    glfwGetCursorPos(window, x, y);
                    // TODO: Move cursor coordinates into our fixed coordinate system
                    for (Component component : components) {
                        if (component.onMouseLeftClick((float) x.get(0), (float) y.get(0))) {
                            break;
                        }
                    }
                }
            }
        }).set(window);
        GLFWCursorPosCallback.create((win, x, y) -> {
            // TODO: Move cursor coordinates into our fixed coordinate system
            for (Component component : components) {
                if (component.onMouseMove((float) x, (float) y)) {
                    break;
                }
            }
        }).set(window);

        double lastTime = glfwGetTime();

        while (!glfwWindowShouldClose(window)) {
            var sizeState = sizeStateRef.get();
            double currentTime = glfwGetTime();
            double deltaTime = currentTime - lastTime;
            lastTime = currentTime;

            glViewport(0, 0, sizeState.fb().width(), sizeState.fb().height());
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

            nvgBeginFrame(ctx, sizeState.win().width(), sizeState.win().height(), sizeState.pxRatio());

            Dimensions fitted = sizeState.fit(new Dimensions(WIDTH, HEIGHT));
            // Correct nanoVG coordinate system to be as-if the window is WIDTH x HEIGHT
            nvgTranslate(
                ctx,
                (float) (sizeState.win().width() - fitted.width()) / 2,
                (float) (sizeState.win().height() - fitted.height()) / 2
            );
            nvgScale(ctx, (float) fitted.width() / WIDTH, (float) fitted.height() / HEIGHT);

            for (Component component : components) {
                nvgTemp(ctx, () -> component.render(ctx, deltaTime));
            }

            nvgEndFrame(ctx);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static final float CIRCLE_SIZE = 60f;
    public static final float CIRCLE_SPACING = 10f;
    public static final float GRID_WIDTH = 3 * CIRCLE_SIZE + 2 * CIRCLE_SPACING;
    public static final float GRID_HEIGHT = 3 * CIRCLE_SIZE + 2 * CIRCLE_SPACING;
    public static final float GRID_X = (WIDTH - GRID_WIDTH) / 2;
    public static final float GRID_Y = (HEIGHT - GRID_HEIGHT) / 2;

    private static float gridCellCenterX(int i) {
        return GRID_X + i * (CIRCLE_SIZE + CIRCLE_SPACING) + CIRCLE_SIZE / 2;
    }

    private static float gridCellCenterY(int j) {
        return GRID_Y + j * (CIRCLE_SIZE + CIRCLE_SPACING) + CIRCLE_SIZE / 2;
    }
}

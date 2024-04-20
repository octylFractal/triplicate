package net.octyl.triplicate;

import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgSave;

public final class NVGUtil {
    public static void nvgTemp(long nvg, Runnable runnable) {
        nvgSave(nvg);
        runnable.run();
        nvgRestore(nvg);
    }
}

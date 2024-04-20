package net.octyl.triplicate.dragui;

public interface Component {
    /**
     * Handle a mouse move event.
     *
     * @param x the x coordinate of the mouse
     * @param y the y coordinate of the mouse
     * @return true if the event was handled and should not be passed to other components
     */
    default boolean onMouseMove(float x, float y) {
        return false;
    }

    /**
     * Handle a mouse left click event.
     *
     * @param x the x coordinate of the mouse
     * @param y the y coordinate of the mouse
     * @return true if the event was handled and should not be passed to other components
     */
    default boolean onMouseLeftClick(float x, float y) {
        return false;
    }

    /**
     * Render the component.
     *
     * @param ctx the rendering context
     * @param deltaTime the time since the last frame, in seconds
     */
    void render(long ctx, double deltaTime);
}

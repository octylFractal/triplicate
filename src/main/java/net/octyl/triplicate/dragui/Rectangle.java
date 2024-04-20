package net.octyl.triplicate.dragui;

public record Rectangle(float x, float y, float width, float height) {
    public boolean contains(float x, float y) {
        return x >= this.x && x < this.x + width && y >= this.y && y < this.y + height;
    }
}

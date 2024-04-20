package net.octyl.triplicate.dragui;

public record Circle(float centerX, float centerY, float radius) {
    public boolean contains(float x, float y) {
        var dx = x - centerX;
        var dy = y - centerY;
        return dx * dx + dy * dy < radius * radius;
    }
}

package net.octyl.triplicate;

import org.lwjgl.nanovg.NVGColor;

public record Color(int red, int green, int blue) {
    public static Color fromHex(int hex) {
        return new Color((hex >> 16) & 0xFF, (hex >> 8) & 0xFF, hex & 0xFF);
    }

    public Color {
        if (red < 0 || red > 255) {
            throw new IllegalArgumentException("Red must be between 0 and 255");
        }
        if (green < 0 || green > 255) {
            throw new IllegalArgumentException("Green must be between 0 and 255");
        }
        if (blue < 0 || blue > 255) {
            throw new IllegalArgumentException("Blue must be between 0 and 255");
        }
    }

    public float redF() {
        return red / 255.0f;
    }

    public float greenF() {
        return green / 255.0f;
    }

    public float blueF() {
        return blue / 255.0f;
    }

    public NVGColor toNVGColor() {
        return NVGColor.create().r(redF()).g(greenF()).b(blueF()).a(1.0f);
    }

    @Override
    public String toString() {
        return String.format("#%02x%02x%02x", red, green, blue);
    }
}

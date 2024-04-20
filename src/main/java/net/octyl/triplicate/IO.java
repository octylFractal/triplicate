package net.octyl.triplicate;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

public final class IO {
    public static ByteBuffer loadResource(String name) throws IOException {
        try (var resource = IO.class.getResourceAsStream(name)) {
            if (resource == null) {
                throw new IllegalArgumentException("Resource not found: " + name);
            }
            byte[] allData = resource.readAllBytes();
            var buffer = BufferUtils.createByteBuffer(allData.length);
            buffer.put(allData);
            buffer.flip();
            return buffer;
        }
    }
}

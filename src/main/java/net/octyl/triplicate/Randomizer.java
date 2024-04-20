package net.octyl.triplicate;

import java.util.SplittableRandom;

public final class Randomizer {
    private static final SplittableRandom RANDOM = new SplittableRandom();

    public static <E extends Enum<E>> E randomEnum(Class<E> enumClass) {
        E[] values = enumClass.getEnumConstants();
        return values[RANDOM.nextInt(values.length)];
    }

    private Randomizer() {
    }
}

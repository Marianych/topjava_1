package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;

public final class Util {
    private Util() {
    }

    public static <T extends Comparable<? super T>> boolean isBetweenInclusive(T value, @Nullable T start, @Nullable T end) {
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }
}

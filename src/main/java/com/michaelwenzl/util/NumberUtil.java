package com.michaelwenzl.util;

public final class NumberUtil {
    private NumberUtil() {
    }

    public static int floor(double value) {
        return (int) Math.floor(value);
    }

    public static int round(double value) {
        return (int) Math.round(value);
    }
}

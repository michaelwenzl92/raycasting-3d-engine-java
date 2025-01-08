package com.michaelwenzl.model;

import java.util.function.Function;

public record Vector<T>(T x, T y) {
    public <U> Vector<U> bimap(Function<T, U> f, Function<T, U> g) {
        return new Vector<>(f.apply(x), g.apply(y));
    }
}
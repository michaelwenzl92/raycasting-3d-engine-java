package com.michaelwenzl.model;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public record Vector<T>(T x, T y) {
    public <U> Vector<U> bimap(Function<T, U> f, Function<T, U> g) {
        return new Vector<>(f.apply(x), g.apply(y));
    }

    public Vector<T> xmap(UnaryOperator<T> f) {
        return new Vector<>(f.apply(x), y);
    }

    public Vector<T> ymap(UnaryOperator<T> f) {
        return new Vector<>(x, f.apply(y));
    }
}
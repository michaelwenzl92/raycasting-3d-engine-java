package com.michaelwenzl.model;

public record Color(int r, int g, int b) {
    public Color darken() {
        return new Color(r / 2, g / 2, b / 2);
    }
}
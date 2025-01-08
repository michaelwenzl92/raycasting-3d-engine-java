package com.michaelwenzl.model;

public record MazeWall(int number, boolean isHorizontalWall) {
    public MazeWall {
        if(!isWall(number)) {
            throw new IllegalArgumentException("Number needs to be != 0 to be a wall");
        }
    }

    public static boolean isWall(int number) {
        return number != 0;
    }
}

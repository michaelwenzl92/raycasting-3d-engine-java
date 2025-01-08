package com.michaelwenzl.model;

public class Player {
    private Vector<Double> position;
    private Vector<Double> direction;
    private Vector<Double> cameraPlane = new Vector<>(0d, 0.66);

    public Player(Vector<Double> position, Vector<Double> direction) {
        this.position = position;
        this.direction = direction;
    }

    public Vector<Double> getPosition() {
        return position;
    }

    public void setPosition(Vector<Double> position) {
        this.position = position;
    }

    public Vector<Double> getDirection() {
        return direction;
    }

    public void setDirection(Vector<Double> direction) {
        this.direction = direction;
    }

    public Vector<Double> getCameraPlane() {
        return cameraPlane;
    }

    public void setCameraPlane(Vector<Double> cameraPlane) {
        this.cameraPlane = cameraPlane;
    }
}
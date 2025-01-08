package com.michaelwenzl.sdl;

import com.michaelwenzl.model.Player;
import com.michaelwenzl.model.Vector;
import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.event.SdlEvents;
import io.github.libsdl4j.api.keycode.SDL_Keycode;

import java.util.HashSet;
import java.util.Set;

import static com.michaelwenzl.Constants.MOVEMENT_SPEED;
import static com.michaelwenzl.Constants.ROTATION_SPEED;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_KEYDOWN;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_KEYUP;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_QUIT;

public class EventHandler {
    private final SDL_Event evt;

    private Set<Integer> pressedKeys = new HashSet<>();

    public EventHandler() {
        evt = new SDL_Event();
    }

    public boolean pollEvents(Player player, double frameTime) {
        double moveSpeed = MOVEMENT_SPEED.applyAsDouble(frameTime);
        double rotationSpeed = ROTATION_SPEED.applyAsDouble(frameTime);

        while (SdlEvents.SDL_PollEvent(evt) != 0) {
            switch (evt.type) {
                case SDL_QUIT:
                    return false;
                case SDL_KEYUP:
                    pressedKeys.remove(evt.key.keysym.sym);
                    break;
                case SDL_KEYDOWN:
                    pressedKeys.add(evt.key.keysym.sym);
                    break;
                default:
                    break;
            }
        }

        move(player, moveSpeed, rotationSpeed);

        return true;
    }

    private void move(Player player, double moveSpeed, double rotationSpeed) {

        for (var pressedKey : pressedKeys) {
            if (pressedKey == SDL_Keycode.SDLK_UP) {
                player.setPosition(new Vector<>(player.getPosition().x() + player.getDirection().x() * moveSpeed, player.getPosition().y() + player.getDirection().y() * moveSpeed));
            }
            if (pressedKey == SDL_Keycode.SDLK_DOWN) {
                player.setPosition(new Vector<>(player.getPosition().x() - player.getDirection().x() * moveSpeed, player.getPosition().y() - player.getDirection().y() * moveSpeed));
            }
            if (pressedKey == SDL_Keycode.SDLK_LEFT) {
                player.setDirection(new Vector<>(
                        rotateXCoordinate(player.getDirection(), rotationSpeed),
                        rotateYCoordinate(player.getDirection(), rotationSpeed)));

                player.setCameraPlane(new Vector<>(
                        rotateXCoordinate(player.getCameraPlane(), rotationSpeed),
                        rotateYCoordinate(player.getCameraPlane(), rotationSpeed)));
            }
            if (pressedKey == SDL_Keycode.SDLK_RIGHT) {
                player.setDirection(new Vector<>(
                        rotateXCoordinate(player.getDirection(), -rotationSpeed),
                        rotateYCoordinate(player.getDirection(), -rotationSpeed)));

                player.setCameraPlane(new Vector<>(
                        rotateXCoordinate(player.getCameraPlane(), -rotationSpeed),
                        rotateYCoordinate(player.getCameraPlane(), -rotationSpeed)));
            }
        }
    }

    private double rotateXCoordinate(Vector<Double> direction, double rotationSpeed) {
        return direction.x() * Math.cos(rotationSpeed) - direction.y() * Math.sin(rotationSpeed);
    }

    private double rotateYCoordinate(Vector<Double> direction, double rotationSpeed) {
        return direction.x() * Math.sin(rotationSpeed) + direction.y() * Math.cos(rotationSpeed);
    }
}
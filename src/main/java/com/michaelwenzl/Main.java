package com.michaelwenzl;

import com.michaelwenzl.sdl.EventHandler;
import com.michaelwenzl.sdl.SdlWrapper;

/**
 * Post to work through
 * <p>
 * https://lodev.org/cgtutor/raycasting.html
 */
public class Main {
    private final EventHandler eventHandler = new EventHandler();
    private final SdlWrapper sdlWrapper = new SdlWrapper();
    private final GameLoop gameLoop = new GameLoop(sdlWrapper, eventHandler);

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        gameLoop.runLoop();
        sdlWrapper.quit();
    }
}
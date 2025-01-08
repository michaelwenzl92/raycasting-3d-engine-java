package com.michaelwenzl;

import com.michaelwenzl.model.Player;
import com.michaelwenzl.model.Vector;
import com.michaelwenzl.renderer.MapRenderer;
import com.michaelwenzl.renderer.SceneRenderer;
import com.michaelwenzl.sdl.EventHandler;
import com.michaelwenzl.sdl.SdlWrapper;

import java.time.Instant;

import static com.michaelwenzl.Constants.MILLISECONDS_PER_FRAME;

public class GameLoop {
    private final SdlWrapper sdlWrapper;
    private final EventHandler eventHandler;
    private final MapRenderer mapRenderer;
    private final SceneRenderer sceneRenderer;

    private final Player player = new Player(new Vector<>(20d, 10d), new Vector<>(-1d, 0d));
    private double frameTime = 0;

    public GameLoop(SdlWrapper sdlWrapper, EventHandler eventHandler) {
        this.sdlWrapper = sdlWrapper;
        this.eventHandler = eventHandler;
        this.mapRenderer =  new MapRenderer(sdlWrapper);
        this.sceneRenderer =  new SceneRenderer(sdlWrapper);
    }

    public void runLoop() {
        var lastRenderAt = Instant.now();

        while (true) {
            sdlWrapper.clearCanvas();

            sceneRenderer.render(player);
            mapRenderer.render(player);
            sdlWrapper.render();

            delayNextFrame(lastRenderAt);
            lastRenderAt = Instant.now();

            if (!eventHandler.pollEvents(player, frameTime)) {
                return;
            }
        }
    }

    private void delayNextFrame(Instant lastRenderAt) {
        var delta = Instant.now().minusMillis(lastRenderAt.toEpochMilli()).toEpochMilli();

        frameTime = delta / 1000d;

        try {
            var delay = MILLISECONDS_PER_FRAME - delta;
            if (delay > 0) {
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

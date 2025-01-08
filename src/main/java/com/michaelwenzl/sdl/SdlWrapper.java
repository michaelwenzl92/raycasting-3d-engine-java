package com.michaelwenzl.sdl;

import com.michaelwenzl.model.Color;
import com.michaelwenzl.model.Vector;
import io.github.libsdl4j.api.rect.SDL_Rect;
import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.video.SDL_Window;

import static com.michaelwenzl.Constants.APP_NAME;
import static com.michaelwenzl.Constants.SCREEN_HEIGHT;
import static com.michaelwenzl.Constants.SCREEN_WIDTH;
import static io.github.libsdl4j.api.Sdl.SDL_Init;
import static io.github.libsdl4j.api.Sdl.SDL_Quit;
import static io.github.libsdl4j.api.SdlSubSystemConst.SDL_INIT_EVERYTHING;
import static io.github.libsdl4j.api.error.SdlError.SDL_GetError;
import static io.github.libsdl4j.api.render.SDL_RendererFlags.SDL_RENDERER_ACCELERATED;
import static io.github.libsdl4j.api.render.SdlRender.SDL_CreateRenderer;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderClear;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderDrawLine;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderFillRect;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderPresent;
import static io.github.libsdl4j.api.render.SdlRender.SDL_SetRenderDrawColor;
import static io.github.libsdl4j.api.video.SDL_WindowFlags.SDL_WINDOW_RESIZABLE;
import static io.github.libsdl4j.api.video.SDL_WindowFlags.SDL_WINDOW_SHOWN;
import static io.github.libsdl4j.api.video.SdlVideo.SDL_CreateWindow;
import static io.github.libsdl4j.api.video.SdlVideoConst.SDL_WINDOWPOS_CENTERED;

public class SdlWrapper {
    private SDL_Renderer renderer;

    public SdlWrapper() {
        int result = SDL_Init(SDL_INIT_EVERYTHING);
        if (result != 0) {
            throw new IllegalStateException("Unable to initialize SDL library (Error code " + result + "): " + SDL_GetError());
        }

        SDL_Window window = SDL_CreateWindow(
                APP_NAME,
                SDL_WINDOWPOS_CENTERED,
                SDL_WINDOWPOS_CENTERED,
                SCREEN_WIDTH,
                SCREEN_HEIGHT,
                SDL_WINDOW_SHOWN | SDL_WINDOW_RESIZABLE);
        if (window == null) {
            throw new IllegalStateException("Unable to create SDL window: " + SDL_GetError());
        }

        renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED);
        if (renderer == null) {
            throw new IllegalStateException("Unable to create SDL renderer: " + SDL_GetError());
        }
    }

    public void render() {
        SDL_RenderPresent(renderer);
    }

    public void quit() {
        SDL_Quit();
    }

    public void clearCanvas() {
        setRenderColor(renderer, new Color(30, 31, 34));
        SDL_RenderClear(renderer);
    }

    public void fillRectangle(Color color, Vector<Integer> position, Vector<Integer> dimension) {
        setRenderColor(renderer, color);

        var rect = new SDL_Rect();
        rect.x = position.x();
        rect.y = position.y();
        rect.w = dimension.x();
        rect.h = dimension.y();

        SDL_RenderFillRect(renderer, rect);
    }

    public void drawLine(Color color, Vector<Integer> startPosition, Vector<Integer> endPosition) {
        setRenderColor(renderer, color);
        SDL_RenderDrawLine(renderer, startPosition.x(), startPosition.y(), endPosition.x(), endPosition.y());
    }

    public void setRenderColor(SDL_Renderer renderer, Color color) {
        SDL_SetRenderDrawColor(renderer, (byte) color.r(), (byte) color.g(), (byte) color.b(), (byte) 255);
    }
}

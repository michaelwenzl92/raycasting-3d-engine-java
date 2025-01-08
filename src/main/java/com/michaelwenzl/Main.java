package com.michaelwenzl;

import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.rect.SDL_Rect;
import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.video.SDL_Window;

import java.time.Instant;

import static io.github.libsdl4j.api.Sdl.SDL_Init;
import static io.github.libsdl4j.api.Sdl.SDL_Quit;
import static io.github.libsdl4j.api.SdlSubSystemConst.SDL_INIT_EVERYTHING;
import static io.github.libsdl4j.api.error.SdlError.SDL_GetError;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_KEYDOWN;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_QUIT;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_WINDOWEVENT;
import static io.github.libsdl4j.api.event.SdlEvents.SDL_PollEvent;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_A;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_D;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_DOWN;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_LEFT;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_RIGHT;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_S;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_SPACE;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_UP;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_W;
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

/**
 * Post to work through
 * <p>
 * https://lodev.org/cgtutor/raycasting.html
 */
public class Main {

    private static final String APP_NAME = "3D Raycasting";

    private static final int MAZE_WIDTH = 24;
    private static final int MAZE_HEIGHT = 24;
    private static final int SCREEN_WIDTH = 640;
    public static final int MAP_HEIGHT = Math.round(SCREEN_WIDTH / 4f);
    public static final int MAP_WIDTH = Math.round(SCREEN_WIDTH / 4f);
    private static final int SCREEN_HEIGHT = 480;
    public static final int MILLISECONDS_PER_FRAME = 1000 / 30;

    private int[][] maze = new int[][]{
            new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 2, 2, 0, 2, 2, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 4, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 4, 0, 0, 0, 0, 5, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 4, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 4, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    record Vector<T>(T x, T y) {
    }

    record Color(int r, int g, int b) {
        public Color darken() {
            return new Color(r / 2, g / 2, b / 2);
        }
    }

    class Player {
        public Vector<Double> position;
        public Vector<Double> direction;
        public Vector<Double> cameraPlane = new Vector<>(0d, 0.66);

        public Player(Vector<Double> position, Vector<Double> direction) {
            this.position = position;
            this.direction = direction;
        }
    }

    private Player player = new Player(new Vector<>(12d, 12d), new Vector<>(-1d, 0d));
    private double frameTime = 0;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        // Initialize SDL
        int result = SDL_Init(SDL_INIT_EVERYTHING);
        if (result != 0) {
            throw new IllegalStateException("Unable to initialize SDL library (Error code " + result + "): " + SDL_GetError());
        }

        // Create and init the window
        SDL_Window window = SDL_CreateWindow(
                APP_NAME,
                SDL_WINDOWPOS_CENTERED,
                SDL_WINDOWPOS_CENTERED,
                SCREEN_WIDTH, SCREEN_HEIGHT,
                SDL_WINDOW_SHOWN | SDL_WINDOW_RESIZABLE);
        if (window == null) {
            throw new IllegalStateException("Unable to create SDL window: " + SDL_GetError());
        }

        // Create and init the renderer
        SDL_Renderer renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED);
        if (renderer == null) {
            throw new IllegalStateException("Unable to create SDL renderer: " + SDL_GetError());
        }

        // Start an event loop and react to events
        SDL_Event evt = new SDL_Event();
        runRenderLoop(renderer, evt);

        SDL_Quit();
    }

    private void runRenderLoop(SDL_Renderer renderer, SDL_Event evt) {
        var shouldRun = true;
        Instant lastRenderAt;
        var counter = 0d;

        while (shouldRun) {
            lastRenderAt = Instant.now();

            clearCanvas(renderer);

            draw3DScene(renderer);
            drawMap(renderer);

            SDL_RenderPresent(renderer);

            delayNextFrame(lastRenderAt);
            counter += 0.5;

            if (counter >= 100) {
                counter = 0;
            }

            shouldRun = pollEvents(evt);
        }
    }

    private void draw3DScene(SDL_Renderer renderer) {

        for (int x = 0; x < SCREEN_WIDTH; x++) {
            double cameraX = 2d * x / SCREEN_WIDTH - 1;

            var rayDirection = new Vector<>(
                    player.direction.x + player.cameraPlane.x * cameraX,
                    player.direction.y + player.cameraPlane.y * cameraX);

            var mapX = (int) Math.floor(player.position.x);
            var mapY = (int) Math.floor(player.position.y);

            double sideDistX;
            double sideDistY;

            var deltaDist = new Vector<>(
                    (rayDirection.x == 0) ? 1e30 : Math.abs(1 / rayDirection.x),
                    (rayDirection.y == 0) ? 1e30 : Math.abs(1 / rayDirection.y));

            double perpWallDist;

            //what direction to step in x or y-direction (either +1 or -1)
            int stepX;
            int stepY;

            int hit = 0; //was there a wall hit?
            int side = 0; //was a NS or a EW wall hit?
            //calculate step and initial sideDist
            if (rayDirection.x < 0) {
                stepX = -1;
                sideDistX = (player.position.x - mapX) * deltaDist.x;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - player.position.x) * deltaDist.x;
            }
            if (rayDirection.y < 0) {
                stepY = -1;
                sideDistY = (player.position.y - mapY) * deltaDist.y;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - player.position.y) * deltaDist.y;
            }
            //perform DDA
            while (hit == 0) {
                //jump to next map square, either in x-direction, or in y-direction
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDist.x;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDist.y;
                    mapY += stepY;
                    side = 1;
                }
                //Check if ray has hit a wall
                if (maze[mapX][mapY] > 0) hit = 1;
            }

            if (side == 0) perpWallDist = (sideDistX - deltaDist.x);
            else perpWallDist = (sideDistY - deltaDist.y);

            //Calculate height of line to draw on screen
            int lineHeight = (int) (SCREEN_HEIGHT / perpWallDist);

            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight / 2 + SCREEN_HEIGHT / 2;
            if (drawStart < 0) drawStart = 0;
            int drawEnd = lineHeight / 2 + SCREEN_HEIGHT / 2;
            if (drawEnd >= SCREEN_HEIGHT) drawEnd = SCREEN_HEIGHT - 1;

            var color = switch (maze[mapX][mapY]) {
                case 1 -> new Color(17, 73, 87);
                case 2 -> new Color(255, 73, 87);
                case 3 -> new Color(17, 255, 87);
                case 4 -> new Color(17, 73, 255);
                default -> new Color(70, 70, 70);
            };

            //give x and y sides different brightness
            if (side == 1) {
                color = color.darken();
            }

            SDL_SetRenderDrawColor(renderer, (byte) color.r, (byte) color.g, (byte) color.b, (byte) 255);
            SDL_RenderDrawLine(renderer, x, drawStart, x, drawEnd);
        }
    }

    private void drawMap(SDL_Renderer renderer) {
        SDL_SetRenderDrawColor(renderer, (byte) 188, (byte) 190, (byte) 196, (byte) 255);

        var blockWidth = Math.round((float) MAP_WIDTH / MAZE_WIDTH);
        var blockHeight = Math.round((float) MAP_HEIGHT / MAZE_HEIGHT);

        var playerMapPositionX = (int) Math.floor(player.position.x);
        var playerMapPositionY = (int) Math.floor(player.position.y);

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] != 0) {
                    var rect = new SDL_Rect();
                    rect.x = blockWidth * j;
                    rect.y = blockHeight * i;
                    rect.w = blockWidth;
                    rect.h = blockHeight;

                    SDL_RenderFillRect(renderer, rect);
                }
            }
        }

        SDL_SetRenderDrawColor(renderer, (byte) 102, (byte) 49, (byte) 63, (byte) 255);
        var rect = new SDL_Rect();
        rect.x = playerMapPositionX * blockWidth;
        rect.y = playerMapPositionY * blockHeight;
        rect.w = blockWidth;
        rect.h = blockHeight;
        SDL_RenderFillRect(renderer, rect);

        SDL_RenderDrawLine(renderer,
                playerMapPositionX * blockWidth + (blockWidth / 2),
                playerMapPositionY * blockHeight + (blockHeight / 2),
                (int) Math.round(player.position.x * blockWidth + (blockWidth / 2d) + (player.direction.x * blockWidth * 5)),
                (int) Math.round(player.position.y * blockHeight + (blockHeight / 2d) + (player.direction.y * blockHeight * 5)));

        SDL_SetRenderDrawColor(renderer, (byte) 255, (byte) 0, (byte) 0, (byte) 255);
        SDL_RenderDrawLine(renderer,
                playerMapPositionX * blockWidth + (blockWidth / 2),
                playerMapPositionY * blockHeight + (blockHeight / 2),
                (int) Math.round(player.position.x * blockWidth + (blockWidth / 2d) + (player.cameraPlane.x * blockWidth * 5)),
                (int) Math.round(player.position.y * blockHeight + (blockHeight / 2d) + (player.cameraPlane.y * blockHeight * 5)));
    }

    private void clearCanvas(SDL_Renderer renderer) {
        SDL_SetRenderDrawColor(renderer, (byte) 30, (byte) 31, (byte) 34, (byte) 255);
        SDL_RenderClear(renderer);
    }

    private boolean pollEvents(SDL_Event evt) {
        while (SDL_PollEvent(evt) != 0) {
            switch (evt.type) {
                case SDL_QUIT:
                    return false;
                case SDL_KEYDOWN:
                    double moveSpeed = frameTime * 20.0; //the constant value is in squares/second
                    double rotSpeed = frameTime * 20.0; //the constant value is in radians/second

                    if (evt.key.keysym.sym == SDLK_UP) {
                        player.position = new Vector<>(player.position.x + player.direction.x * moveSpeed, player.position.y + player.direction.y * moveSpeed);
                    }
                    if (evt.key.keysym.sym == SDLK_DOWN) {
                        player.position = new Vector<>(player.position.x - player.direction.x * moveSpeed, player.position.y - player.direction.y * moveSpeed);
                    }
                    if (evt.key.keysym.sym == SDLK_LEFT) {
                        player.direction = new Vector<>(
                                player.direction.x * Math.cos(rotSpeed) - player.direction.y * Math.sin(rotSpeed),
                                player.direction.x * Math.sin(rotSpeed) + player.direction.y * Math.cos(rotSpeed));

                        player.cameraPlane = new Vector<>(
                                player.cameraPlane.x * Math.cos(rotSpeed) - player.cameraPlane.y * Math.sin(rotSpeed),
                                player.cameraPlane.x * Math.sin(rotSpeed) + player.cameraPlane.y * Math.cos(rotSpeed));
                    }
                    if (evt.key.keysym.sym == SDLK_RIGHT) {
                        player.direction = new Vector<>(
                                player.direction.x * Math.cos(-rotSpeed) - player.direction.y * Math.sin(-rotSpeed),
                                player.direction.x * Math.sin(-rotSpeed) + player.direction.y * Math.cos(-rotSpeed));

                        player.cameraPlane = new Vector<>(
                                player.cameraPlane.x * Math.cos(-rotSpeed) - player.cameraPlane.y * Math.sin(-rotSpeed),
                                player.cameraPlane.x * Math.sin(-rotSpeed) + player.cameraPlane.y * Math.cos(-rotSpeed));
                    }
                    break;
                default:
                    break;
            }
        }

        return true;
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
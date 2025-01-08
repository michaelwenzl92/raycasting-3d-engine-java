package com.michaelwenzl.renderer;

import com.michaelwenzl.model.Color;
import com.michaelwenzl.model.Player;
import com.michaelwenzl.model.Vector;
import com.michaelwenzl.sdl.SdlWrapper;
import com.michaelwenzl.util.NumberUtil;

import java.util.function.Function;

import static com.michaelwenzl.Constants.MAZE;
import static com.michaelwenzl.Constants.SCREEN_HEIGHT;
import static com.michaelwenzl.Constants.SCREEN_WIDTH;
import static com.michaelwenzl.Constants.WALL_COLORS;

public class SceneRenderer {
    private final SdlWrapper sdlWrapper;

    public SceneRenderer(SdlWrapper sdlWrapper) {
        this.sdlWrapper = sdlWrapper;
    }

    public void render(Player player) {
        for (int columnToCalculate = 0; columnToCalculate < SCREEN_WIDTH; columnToCalculate++) {
            double cameraX = 2d * columnToCalculate / SCREEN_WIDTH - 1;

            var rayDirection = player.getDirection().bimap(
                    pX -> pX + player.getCameraPlane().x() * cameraX,
                    pY -> pY + player.getCameraPlane().y() * cameraX);

            var mapPosition = player.getPosition().bimap(NumberUtil::floor, NumberUtil::floor);

            var deltaDistanceForOneSquare = new Vector<>(
                    (rayDirection.x() == 0) ? SCREEN_WIDTH : Math.abs(1 / rayDirection.x()),
                    (rayDirection.y() == 0) ? SCREEN_HEIGHT : Math.abs(1 / rayDirection.y()));

            double sideDistX;
            double sideDistY;
            //what direction to step in x or y-direction (either +1 or -1)
            int stepX;
            int stepY;

            //calculate step and initial sideDist
            if (rayDirection.x() < 0) {
                stepX = -1;
                sideDistX = (player.getPosition().x() - mapPosition.x()) * deltaDistanceForOneSquare.x();
            } else {
                stepX = 1;
                sideDistX = (mapPosition.x() + 1.0 - player.getPosition().x()) * deltaDistanceForOneSquare.x();
            }
            if (rayDirection.y() < 0) {
                stepY = -1;
                sideDistY = (player.getPosition().y() - mapPosition.y()) * deltaDistanceForOneSquare.y();
            } else {
                stepY = 1;
                sideDistY = (mapPosition.y() + 1.0 - player.getPosition().y()) * deltaDistanceForOneSquare.y();
            }
            Result result = getResult(sideDistX, sideDistY, deltaDistanceForOneSquare, mapPosition, stepX, stepY);

            double perpWallDist;
            if (result.side() == 0) perpWallDist = (result.sideDistX() - deltaDistanceForOneSquare.x());
            else perpWallDist = (result.sideDistY() - deltaDistanceForOneSquare.y());

            renderWallLine(result.mapPosition(), perpWallDist, result.side(), columnToCalculate);
        }
    }

    private Result getResult(double sideDistX, double sideDistY, Vector<Double> deltaDistanceForOneSquare, Vector<Integer> mapPosition, int stepX, int stepY) {
        var side = 0;
        var scanPosition = mapPosition.bimap(Function.identity(), Function.identity());

        while (true) {
            if (sideDistX < sideDistY) {
                sideDistX += deltaDistanceForOneSquare.x();
                scanPosition = scanPosition.bimap(mX -> mX + stepX, mY -> mY);
                side = 0;
            } else {
                sideDistY += deltaDistanceForOneSquare.y();
                scanPosition = scanPosition.bimap(mX -> mX, mY -> mY + stepY);
                side = 1;
            }

            if (MAZE[scanPosition.x()][scanPosition.y()] > 0) {
                return new Result(scanPosition, sideDistX, sideDistY, side);
            }
        }
    }

    private record Result(Vector<Integer> mapPosition, double sideDistX, double sideDistY, int side) {
    }

    private void renderWallLine(Vector<Integer> mapPosition, double perpWallDist, int side, int columnToCalculate) {
        //Calculate height of line to draw on screen
        int lineHeight = (int) (SCREEN_HEIGHT / perpWallDist);

        //calculate lowest and highest pixel to fill in current stripe
        int drawStart = -lineHeight * 2 + SCREEN_HEIGHT / 2;
        if (drawStart < 0) drawStart = 0;
        int drawEnd = lineHeight / 2 + SCREEN_HEIGHT / 2;
        if (drawEnd >= SCREEN_HEIGHT) drawEnd = SCREEN_HEIGHT - 1;

        var wallColor = WALL_COLORS.getOrDefault(MAZE[mapPosition.x()][mapPosition.y()], new Color(255, 255, 255));
        sdlWrapper.drawLine(
                side == 1 ? wallColor.darken() : wallColor,
                new Vector<>(columnToCalculate, drawStart),
                new Vector<>(columnToCalculate, drawEnd));
    }
}

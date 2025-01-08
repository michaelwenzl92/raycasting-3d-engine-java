package com.michaelwenzl.renderer;

import com.michaelwenzl.model.Color;
import com.michaelwenzl.model.MazeWall;
import com.michaelwenzl.model.Player;
import com.michaelwenzl.model.Vector;
import com.michaelwenzl.sdl.SdlWrapper;
import com.michaelwenzl.util.NumberUtil;

import static com.michaelwenzl.Constants.MAZE;
import static com.michaelwenzl.Constants.SCREEN_HEIGHT;
import static com.michaelwenzl.Constants.SCREEN_WIDTH;
import static com.michaelwenzl.Constants.WALL_COLORS;
import static com.michaelwenzl.util.NumberUtil.round;

public class SceneRenderer {
    private final SdlWrapper sdlWrapper;

    public SceneRenderer(SdlWrapper sdlWrapper) {
        this.sdlWrapper = sdlWrapper;
    }

    public void render(Player player) {
        for (int columnToCalculate = 0; columnToCalculate < SCREEN_WIDTH; columnToCalculate++) {
            var cameraX = 2d * columnToCalculate / SCREEN_WIDTH - 1;

            var rayDirection = player.getDirection().bimap(
                    pX -> pX + player.getCameraPlane().x() * cameraX,
                    pY -> pY + player.getCameraPlane().y() * cameraX);

            var mapPosition = player.getPosition().bimap(NumberUtil::floor, NumberUtil::floor);

            var deltaDistanceForOneSquare = new Vector<>(
                    (rayDirection.x() == 0) ? SCREEN_WIDTH : Math.abs(1 / rayDirection.x()),
                    (rayDirection.y() == 0) ? SCREEN_HEIGHT : Math.abs(1 / rayDirection.y()));

            var stepDirection = new Vector<>(rayDirection.x() < 0 ? -1 : 1, rayDirection.y() < 0 ? -1 : 1);

            var distanceToNextGridLine = new Vector<>(
                    rayDirection.x() < 0 ?
                            (player.getPosition().x() - mapPosition.x()) * deltaDistanceForOneSquare.x()
                            : (mapPosition.x() + 1.0 - player.getPosition().x()) * deltaDistanceForOneSquare.x(),
                    rayDirection.y() < 0 ?
                            (player.getPosition().y() - mapPosition.y()) * deltaDistanceForOneSquare.y()
                            : (mapPosition.y() + 1.0 - player.getPosition().y()) * deltaDistanceForOneSquare.y());

            var distanceToWall = calculateDistanceToWall(distanceToNextGridLine, deltaDistanceForOneSquare, mapPosition, stepDirection);

            double perpendicularWallDistance = distanceToWall.mazeWall().isHorizontalWall() ?
                    (distanceToWall.distanceToWall().x() - deltaDistanceForOneSquare.x())
                    : (distanceToWall.distanceToWall().y() - deltaDistanceForOneSquare.y());

            renderWallLine(distanceToWall.mazeWall(), perpendicularWallDistance, columnToCalculate);
        }
    }

    private DistanceToWall calculateDistanceToWall(Vector<Double> distanceToNextGridLine, Vector<Double> deltaDistanceForOneSquare, Vector<Integer> scanPosition, Vector<Integer> stepDirection) {
        boolean isHorizontalGridLineNearer = distanceToNextGridLine.x() < distanceToNextGridLine.y();

        var cumulativeDistanceToNextGridLine = isHorizontalGridLineNearer ?
                distanceToNextGridLine.xmap(x -> x + deltaDistanceForOneSquare.x())
                : distanceToNextGridLine.ymap(y -> y + deltaDistanceForOneSquare.y());

        var nextScanPosition = isHorizontalGridLineNearer ?
                scanPosition.xmap(x -> x + stepDirection.x())
                : scanPosition.ymap(y -> y + stepDirection.y());

        var wallTypeNumber = MAZE[nextScanPosition.x()][nextScanPosition.y()];

        if (MazeWall.isWall(wallTypeNumber)) {
            return new DistanceToWall(new MazeWall(wallTypeNumber, isHorizontalGridLineNearer), cumulativeDistanceToNextGridLine);
        }

        return calculateDistanceToWall(cumulativeDistanceToNextGridLine, deltaDistanceForOneSquare, nextScanPosition, stepDirection);
    }

    private void renderWallLine(MazeWall mazeWall, double perpendicularWallDistance, int columnToCalculate) {
        int lineHeight = round(SCREEN_HEIGHT / perpendicularWallDistance);

        int drawStart = Math.max(-lineHeight * 2 + SCREEN_HEIGHT / 2, 0);
        int drawEnd = Math.min(lineHeight / 2 + SCREEN_HEIGHT / 2, SCREEN_HEIGHT - 1);

        var wallColor = WALL_COLORS.getOrDefault(mazeWall.number(), new Color(255, 255, 255));
        sdlWrapper.drawLine(
                !mazeWall.isHorizontalWall() ? wallColor.darken() : wallColor,
                new Vector<>(columnToCalculate, drawStart),
                new Vector<>(columnToCalculate, drawEnd));
    }

    private record DistanceToWall(MazeWall mazeWall, Vector<Double> distanceToWall) {
    }
}

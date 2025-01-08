package com.michaelwenzl.renderer;

import com.michaelwenzl.model.Color;
import com.michaelwenzl.model.Player;
import com.michaelwenzl.model.Vector;
import com.michaelwenzl.sdl.SdlWrapper;

import static com.michaelwenzl.Constants.MAP_HEIGHT;
import static com.michaelwenzl.Constants.MAP_WIDTH;
import static com.michaelwenzl.Constants.MAZE;
import static com.michaelwenzl.Constants.PLAYER_COLOR;
import static com.michaelwenzl.Constants.WALL_COLORS;
import static com.michaelwenzl.util.NumberUtil.floor;

public class MapRenderer {
    private SdlWrapper sdlWrapper;

    public MapRenderer(SdlWrapper sdlWrapper) {
        this.sdlWrapper = sdlWrapper;
    }

    public void render(Player player) {
        var blockDimension = new Vector<>(
                Math.round((float) MAP_WIDTH / MAZE.length),
                Math.round((float) MAP_HEIGHT / MAZE[0].length));

        var playerMapPosition = player.getPosition().bimap(
                x -> floor(x * blockDimension.x()),
                y -> floor(y * blockDimension.y())
        );

        renderWalls(blockDimension);
        renderPlayer(player, playerMapPosition, blockDimension);
        renderCameraPlane(player, playerMapPosition, blockDimension);
    }

    private void renderWalls(Vector<Integer> blockDimension) {
        for (int i = 0; i < MAZE.length; i++) {
            for (int j = 0; j < MAZE[i].length; j++) {
                if (MAZE[i][j] != 0) {
                    sdlWrapper.fillRectangle(
                            WALL_COLORS.getOrDefault(MAZE[i][j], new Color(255, 255, 255)),
                            new Vector<>(blockDimension.x() * i, blockDimension.y() * j),
                            blockDimension);
                }
            }
        }
    }

    private void renderPlayer(Player player, Vector<Integer> playerMapPosition, Vector<Integer> blockDimension) {
        sdlWrapper.fillRectangle(
                PLAYER_COLOR,
                playerMapPosition,
                blockDimension);

        sdlWrapper.drawLine(
                PLAYER_COLOR,
                new Vector<>(playerMapPosition.x() + (blockDimension.x() / 2), playerMapPosition.y() + (blockDimension.y() / 2)),
                new Vector<>(
                        (int) Math.round(playerMapPosition.x() + (blockDimension.x() / 2d) + (player.getDirection().x() * blockDimension.x() * 5)),
                        (int) Math.round(playerMapPosition.y() + (blockDimension.y() / 2d) + (player.getDirection().y() * blockDimension.y() * 5))));
    }

    private void renderCameraPlane(Player player, Vector<Integer> playerMapPosition, Vector<Integer> blockDimension) {
        sdlWrapper.drawLine(
                new Color(255, 0, 0),
                new Vector<>((int) Math.round(playerMapPosition.x() + (blockDimension.x() / 2d) - (player.getCameraPlane().x() * blockDimension.x() * 5)),
                        (int) Math.round(playerMapPosition.y() + (blockDimension.y() / 2d) - (player.getCameraPlane().y() * blockDimension.y() * 5))),
                new Vector<>((int) Math.round(playerMapPosition.x() + (blockDimension.x() / 2d) + (player.getCameraPlane().x() * blockDimension.x() * 5)),
                        (int) Math.round(playerMapPosition.y() + (blockDimension.y() / 2d) + (player.getCameraPlane().y() * blockDimension.y() * 5))));
    }
}

package com.mygdx.game.level;

import com.mygdx.game.entities.Wall;
import com.mygdx.game.managers.GameStateManager;

import java.io.File;
import java.util.List;

import static com.mygdx.game.entities.Player.Facing;

public class LevelManager {
    private static LevelData level;

    public static void init() {
        level = new Level1();
    }

    public static void levelUp() {
        level = level.nextLevel;
    }

    public static int getID() {
        return level.getID();
    }

    public static int getFruitToNextLevel() {
        if (GameStateManager.getPlayMode() == GameStateManager.PlayMode.INFINITE_TAIL)
            return 9999;
        return level.getFruitToNextLevel();
    }

    private static boolean saveFileExists(String filename) {
        File f = new File(filename);
        return f.exists();
    }

    public static int getGrid() {
        return level.getGridCell();
    }

    public static float getStartX() {
        return level.getStartX();
    }

    public static float getStartY() {
        return level.getStartY();
    }

    public static Facing getFacing() {
        return level.getFacing();
    }

    public static List<Wall> getWalls() {
        return level.getWalls();
    }

    public static int getRows() {
        return level.getRows();
    }

    public static int getColumns() {
        return level.getColumns();
    }
}

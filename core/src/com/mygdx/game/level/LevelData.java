package com.mygdx.game.level;

import com.mygdx.game.Game;
import com.mygdx.game.entities.Wall;

import java.util.ArrayList;
import java.util.List;

public class LevelData {
    protected int rows;
    protected int columns;
    protected final int gridCell = 20;

    protected int levelID;

    protected int fruitToNextLevel;

    protected List<Wall> walls;

    protected float startX;
    protected float startY;
    protected LevelData nextLevel;

    protected void line(int x1, int y1, int x2, int y2) {

        int dx = x1 - x2;
        int dy = y1 - y2;
        //distance between to points
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        //the distance between the two points must be different then zero, and dx or dy must be zero.
        if (distance == 0 || dx != 0 && dy != 0)
            return;

        for (int i = (dx == 0 ? y1 : x1);
             i <= (dx == 0 ? y2 : x2);
             i++) {
            walls.add(new Wall(
                    (dx == 0 ? x1 : i) * gridCell,
                    (dy == 0 ? y1 : i) * gridCell,
                    gridCell));
        }

    }

    void init() {
        //setup board
        Game.WIDTH = columns * gridCell;
        Game.HEIGHT = rows * gridCell;

        Game.camera.position.set(Game.WIDTH / 2, Game.HEIGHT / 2, 0);
        Game.camera.update();
    }

    int getRows() {
        return rows;
    }

    int getColumns() {
        return columns;
    }

    int getGridCell() {
        return gridCell;
    }

    int getID() {
        return levelID;
    }

    int getFruitToNextLevel() {
        return fruitToNextLevel;
    }

    List<Wall> getWalls() {
        return walls;
    }

    float getStartX() {
        return startX;
    }

    float getStartY() {
        return startY;
    }
}

class Level1 extends LevelData {
    public Level1() {
        walls = new ArrayList<>();

        rows = 20;
        columns = 20;
        levelID = 1;
        fruitToNextLevel = 1;

        startX = 5;
        startY = 5;
        nextLevel = new Level2();

        init();

        line(0, 0, 19, 0);
        line(0, 19, 19, 19);
        line(0, 1, 0, 18);
        line(19, 1, 19, 18);
    }
}

class Level2 extends LevelData {
    public Level2() {
        walls = new ArrayList<>();

        rows = 20;
        columns = 20;
        levelID = 2;
        fruitToNextLevel = 1;

        startX = 5;
        startY = 5;
        nextLevel = null;

        init();

        line(0, 0, 6, 0);
        line(13, 0, 19, 0);
        line(0, 19, 6, 19);
        line(13, 19, 19, 19);

        line(0, 1, 0, 6);
        line(0, 13, 0, 18);
        line(19, 1, 19, 6);
        line(19, 13, 19, 18);
    }
}

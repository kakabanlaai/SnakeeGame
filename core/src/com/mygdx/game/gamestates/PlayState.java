package com.mygdx.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Game;
import com.mygdx.game.entities.Apple;
import com.mygdx.game.entities.Player;
import com.mygdx.game.entities.Tail;
import com.mygdx.game.entities.Wall;
import com.mygdx.game.level.LevelManager;
import com.mygdx.game.managers.Font;
import com.mygdx.game.managers.GameFile;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.managers.Jukebox;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.managers.GameStateManager.PlayMode;
import static com.mygdx.game.managers.GameStateManager.getPlayMode;

public class PlayState extends GameState {
    private SpriteBatch sb;
    private ShapeRenderer sr;
    private BitmapFont titleFont;
    private BitmapFont font;

    private float tempGameWidth;
    private float tempGameHeight;


    private Apple apple;

    private List<Player> extraLives;
    private Apple remainingApples;

    private boolean playTime;
    private boolean exitMessage;

    private boolean beat1;

    public PlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
    }

    @Override
    public void init() {
        sr = gameStateManager.renderer;
        sb = gameStateManager.batch;
        titleFont = gameStateManager.titleFont;

        //removed from draw, causes a huge consumption of memory in there
        Color color = new Color(0, 1, 1, 1);
        titleFont = Font.MANAGER.set(30, color);
        font = Font.MANAGER.set(15);

        setupLevel();


    }


    private void setupLevel() {
        tempGameWidth = Game.WIDTH;
        tempGameHeight = Game.HEIGHT;
        LevelManager.init();
    }

    @Override
    public void draw() {
        sr.setProjectionMatrix(Game.camera.combined);
        sb.setProjectionMatrix(Game.camera.combined);

        drawGrid();

        LevelManager.getWalls().forEach(pWall -> pWall.draw(sr));

        extraLives.forEach(extraLive -> extraLive.draw(sr));

        if (apple != null) {
            apple.draw(sr);
        }

        remainingApples.draw(sr);
        drawText();
    }

    @Override
    public void update(float dt) {
        //get user input
        handleInput();

        if (!isPlayTime()) {
            return;
        }

        //create apple
        if (apple == null) {
            apple = newApple();
        } else {
            apple.update(dt);
            if (apple.shouldRemove())
                apple = null;
        }
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            playTime = !playTime;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            playTime = false;
            exitMessage = !exitMessage;
        }
        if (exitMessage && Gdx.input.isKeyJustPressed(Input.Keys.Y))
            gameStateManager.setState(GameStateManager.State.MENU);

    }


    private void drawGrid() {
        sr.setProjectionMatrix(Game.camera.combined);

        sr.setColor(0, 0, 0.35f, 1);
        sr.begin(ShapeRenderer.ShapeType.Line);

        for (int i = 0; i <= Game.WIDTH; i += LevelManager.getGrid()) {
            sr.line(i, 0, i, Game.HEIGHT);
        }

        for (int i = 0; i <= Game.HEIGHT; i += LevelManager.getGrid()) {
            sr.line(0, i, Game.WIDTH, i);
        }

        sr.end();
    }

    private Apple newApple() {
        float x;
        float y;
        boolean containsHead, containsFruit = false, containsWall = false;
        do {
            // first get the position random
            x = MathUtils.random(LevelManager.getColumns() - 1) * LevelManager.getGrid();
            y = MathUtils.random(LevelManager.getRows() - 1) * LevelManager.getGrid();

            // check if space is free


            for (Wall pWall : LevelManager.getWalls()) {
                containsWall = pWall.contains(x, y);
                if (containsWall)
                    break;
            }
        } while ( containsFruit || containsWall);

        return new Apple(x, y);
    }

    private void drawText() {
        Font.MANAGER.centered(sb, titleFont,
                MenuState.title,
                Game.WIDTH / 2,
                Game.HEIGHT + 80);

        Font.MANAGER.left(sb, font,
                "Score: " + (int) 0,
                0,
                Game.HEIGHT + 20);

        Font.MANAGER.right(sb, font,
                "Level: " + LevelManager.getID(),
                Game.WIDTH,
                -12);

        if (!playTime && !exitMessage) {
            Font.MANAGER.centered(sb, font,
                    "Hit space to continue ...",
                    Game.WIDTH / 2,
                    Game.HEIGHT / 2);
        }

        if (exitMessage) {
            Font.MANAGER.centered(sb, font,
                    "Are you sure you want",
                    Game.WIDTH / 2,
                    Game.HEIGHT / 2 + 20);

            Font.MANAGER.centered(sb, font,
                    "to quit the game?",
                    Game.WIDTH / 2,
                    Game.HEIGHT / 2);

            Font.MANAGER.centered(sb, font,
                    "(Y to exit)",
                    Game.WIDTH / 2,
                    Game.HEIGHT / 2 - 20);
        }
        Font.MANAGER.left(sb, font,
                "x " + (LevelManager.getFruitToNextLevel() ),
                22,
                -12);
    }

    private boolean isPlayTime() {
        return playTime;
    }

    @Override
    public void dispose() {
        Game.WIDTH = tempGameWidth;
        Game.HEIGHT = tempGameHeight;
        Game.setCameraPosition();
    }
}

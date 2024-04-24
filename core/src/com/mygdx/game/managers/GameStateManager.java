package com.mygdx.game.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Game;
import com.mygdx.game.gamestates.GameState;
import com.mygdx.game.gamestates.MenuState;

public class GameStateManager {
    private GameState gameState;
    public enum State {
        MENU, PLAY, HIGHS_CORES, OPTIONS, GAME_OVER
    }

    public enum OptionKeys {
        SNAKE, PLAYER
    }

    public enum PlayMode {
        LEVEL_UP, INFINITE_TAIL
    }

    private static OptionKeys optionKeys;

    private static PlayMode playMode;

    public final SpriteBatch batch = new SpriteBatch();
    public final ShapeRenderer renderer = new ShapeRenderer();
    public final BitmapFont titleFont = new BitmapFont();
    public final BitmapFont font = new BitmapFont();

    public GameStateManager() {
        optionKeys = OptionKeys.PLAYER;
        playMode = PlayMode.INFINITE_TAIL;
        setState(State.MENU);
    }

    public void setState(State state) {
        if (gameState != null) {
            gameState.dispose();
            gameState = null;
            System.gc();

        }

        switch (state) {
            case MENU:
                Game.setCameraPosition();
                gameState = new MenuState(this);
                break;
            case PLAY:
                break;
            case HIGHS_CORES:
                break;
            case OPTIONS:
                break;
            case GAME_OVER:
                break;
        }
    }

    public void update(float dt) {
        gameState.update(dt);
    }

    public void draw() {
        gameState.draw();
    }
}

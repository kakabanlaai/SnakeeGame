package com.mygdx.game.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Game;
import com.mygdx.game.gamestates.*;

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

    private static boolean newGame;

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
                Game.setCameraPosition();
                if (isNewGame())
                    gameState = new OptionsState(this);
                else
                    gameState = new PlayState(this);
                break;
            case HIGHS_CORES:
                Game.setCameraPosition();
                gameState = new HighScoreState(this);
                break;
            case OPTIONS:
                Game.setCameraPosition();
                gameState = new OptionsState(this);
                break;
            case GAME_OVER:
                Game.setCameraPosition();
                gameState = new GameOverState(this);
                break;
        }
    }

    public void update(float dt) {
        gameState.update(dt);
    }

    public void draw() {
        gameState.draw();
    }

    public static PlayMode getPlayMode() {
        return playMode;
    }

    public static void setPlayMode(PlayMode playMode) {
        GameStateManager.playMode = playMode;
    }

    public static OptionKeys getOptionsKeys() {
        return GameStateManager.optionKeys;
    }

    public void setOptionsKeys(OptionKeys optionKeys) {
        GameStateManager.optionKeys = optionKeys;
    }

    public static void startNewGame() {
        newGame = !newGame;
    }

    public static boolean isNewGame() {
        return newGame;
    }
}

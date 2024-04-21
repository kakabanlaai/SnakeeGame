package com.mygdx.game.managers;

import com.mygdx.game.Game;

public class GameStateManager {
    public enum State {
        MENU, PLAY, HIGHS_CORES, OPTIONS, GAME_OVER
    }

    public enum OptionKeys {
        SNAKE, PLAYER
    }

    public enum PlayMode {
        LEVEL_UP, INFINITE_TAIL
    }

    public GameStateManager() {
        setState(State.MENU);
    }

    public void setState(State state) {
        switch (state) {
            case MENU:
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
}

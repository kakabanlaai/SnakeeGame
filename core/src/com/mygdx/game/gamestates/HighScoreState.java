package com.mygdx.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Game;
import com.mygdx.game.entities.PlayerScore;
import com.mygdx.game.managers.Font;
import com.mygdx.game.managers.GameFile;
import com.mygdx.game.managers.GameStateManager;
import com.mygdx.game.managers.Jukebox;

public class HighScoreState extends GameState {

    private SpriteBatch batch;
    private PlayerScore[] highScores;
    private BitmapFont font;

    public HighScoreState(GameStateManager gameStateManager) {
        super(gameStateManager);
    }

    @Override
    public void init() {
        batch = gameStateManager.batch;
        font = Font.MANAGER.set(20);

        GameFile.MANAGER.load();
        highScores = GameFile.MANAGER.gameData.getHighScores();
    }

    @Override
    public void update(float dt) {

        handleInput();
    }

    @Override
    public void draw() {
        batch.setProjectionMatrix(Game.camera.combined);

        Font.MANAGER.centered(batch, font, "High Scores", Game.WIDTH / 2, Game.HEIGHT - 30);

        float row = Game.HEIGHT - 55;

        for (int i = 0; i < highScores.length; i++) {
            row -= 30;
            String s = String.format("%2d. %5s %s", i + 1, highScores[i].getScore(), highScores[i].getName());
            Font.MANAGER.centered(batch, font, s, Game.WIDTH / 2, row);
        }

        Font.MANAGER.centered(batch, font, "Press ESC to exit", Game.WIDTH / 2, Game.HEIGHT - 400);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Jukebox.MANAGER.play("accept");
            gameStateManager.setState(GameStateManager.State.MENU);
        }
    }

    @Override
    public void dispose() {
        //dispose of objects is manipulated by the Game class
    }

}
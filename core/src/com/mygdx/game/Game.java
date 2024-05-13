package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.managers.GameStateManager;

public class Game extends ApplicationAdapter {
	public static float WIDTH;
	public static float HEIGHT;

	public static OrthographicCamera camera;
	private static Vector3 cameraPosition; //Vector to save the original position of the camera

	private static GameStateManager gameStateManager;
	
	@Override
	public void create () {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.translate(WIDTH / 2, HEIGHT / 2);
		cameraPosition = camera.position.cpy();

		gameStateManager = new GameStateManager();
	}

	public static void setCameraPosition() {
		camera.position.set(cameraPosition);
		camera.update();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.1f, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameStateManager.update(Gdx.graphics.getDeltaTime());
		gameStateManager.draw();
	}
	
	@Override
	public void dispose () {
		gameStateManager.batch.dispose();
		gameStateManager.renderer.dispose();
		gameStateManager.titleFont.dispose();
		gameStateManager.font.dispose();
	}
}
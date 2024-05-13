package com.mygdx.game.managers;

import com.badlogic.gdx.Gdx;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public enum GameFile {
    MANAGER;

    public GameData gameData;
    private final String filename = "highscores.dat";

    public void save() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(filename)));
            out.writeObject(gameData);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Gdx.app.exit();
        }

    }

    /**
     * Loads file width saved high scores.
     */
    public void load() {
        try {
            if (!saveFileExists()) {
                init();
                return;
            }
            ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(filename)));
            gameData = (GameData) in.readObject();
            in.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    private boolean saveFileExists() {
        File f = new File(filename);
        return f.exists();
    }


    private void init() {
        gameData = new GameData();
        gameData.init();
        save();
    }

}

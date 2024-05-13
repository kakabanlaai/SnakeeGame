package com.mygdx.game.managers;

import com.mygdx.game.entities.PlayerScore;

import java.io.Serializable;

public class GameData implements Serializable {
    /**
     * Serializable identifier version
     */
    private static final long serialVersionUID = 201809111959L;

    private final int MAX_SCORES = 10;

    private final PlayerScore[] highScores;

    private long tentativeScore;

    GameData() {
        highScores = new PlayerScore[MAX_SCORES];

    }

    void init() {
        for (int i = 0; i < MAX_SCORES; i++) {
            highScores[i] = new PlayerScore("---", 0);
        }
    }

    public PlayerScore[] getHighScores() {
        return highScores;
    }

    public long getTentativeScore() {
        return tentativeScore;
    }

    public void setTentativeScore(long l) {
        this.tentativeScore = l;
    }

    public boolean isHighScore(long score) {
        return score > highScores[MAX_SCORES - 1].getScore();
    }

    public void addHighScore(long newScore, String newName) {
        if (isHighScore(newScore)) {
            highScores[MAX_SCORES - 1].setScore(newScore);
            highScores[MAX_SCORES - 1].setName(newName);
            sortHighScore();
        }
    }

    private void sortHighScore() {
        for (int i = 0; i < MAX_SCORES; i++) {
            long score = highScores[i].getScore();
            String name = highScores[i].getName();

            int j;
            for (j = i - 1; j >= 0 && highScores[j].getScore() < score; j--) {
                highScores[j + 1].setScore(highScores[j].getScore());
                highScores[j + 1].setName(highScores[j].getName());

            }
            highScores[j + 1].setScore(score);
            highScores[j + 1].setName(name);
        }

    }

}

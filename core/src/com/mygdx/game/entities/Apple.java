package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Apple extends GameObject {

    private float removeTimer;
    private final float removeTime;
    private boolean remove;
    private final int score;
    private boolean bonusApple; //this fruit decrease speed of the game by 10%


    public Apple(float x, float y) {
        setPosition(x, y);
        getRandomFruit();
        removeTimer = 0;
        removeTime = 10;

        //set WIDTH and HEIGHT
        width = height = 15;

        //sets the score, if bonus fruit it adds 100 points, else 10 points.
        score = bonusApple ? 100 : 10;
    }

    public Apple(float x, float y, boolean bonus) {
        this(x, y);
        this.bonusApple = bonus;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public boolean contains(float x, float y) {
        remove = super.contains(x, y);
        return remove;
    }

    public int getScore() {
        return score;
    }

    private void getRandomFruit() {
        bonusApple = MathUtils.random(10) < 1;
    }

    public boolean isBonus() {
        return bonusApple;
    }

    @Override
    public void update(float dt) {
        removeTimer += dt;
        if (removeTimer > removeTime) {
            removeTimer = 0;
            remove = true;
        }
    }

    @Override
    public void draw(ShapeRenderer sr) {
        sr.setColor(bonusApple ? Color.YELLOW : Color.RED);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.ellipse(x + width / 4, y, width / 2, height * 4 / 6);
        sr.ellipse(x + 2f * (width / 4), y, width / 2, height * 4 / 6);
        sr.setColor(0, 0.55f, 0, 1);
        sr.ellipse(x + width / 6, y + height / 2, width / 2, height * 2 / 6);
        sr.setColor(1, 1, 1, 1);
        sr.end();
    }
}

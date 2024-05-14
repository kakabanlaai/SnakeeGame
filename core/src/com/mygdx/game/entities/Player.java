package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player extends GameObject {
    public enum Facing {
        UP, DOWN, LEFT, RIGHT
    }

    private Facing facing;

    private boolean rotateLeft;
    private boolean rotateRight;

    private boolean dead;
    private boolean eat;

    private double score;
    private int lives;

    private int fruitAte;

    public Player(int size) {

        //set WIDTH and HEIGHT
        width = height = size;

        //initial speed
        speed = width;

        lives = 3;

        reset();
    }

    private void reset() {
        //initial orientation
        radians = (float) Math.PI / 2;
        facing = Facing.UP;

        dead = false;
    }

    public void update(float dt) {

        eat = false;

        //rotating
        if (rotateLeft) {
            radians += (float) Math.PI / 2;
            rotateLeft = false;
        }
        if (rotateRight) {
            radians -= (float) Math.PI / 2;
            rotateRight = false;
        }


        //calculate direction
        dx = Math.round(Math.cos(radians)) * speed;
        dy = Math.round(Math.sin(radians)) * speed;

        //set new position
        x += dx;
        y += dy;

    }

    @Override
    public void draw(ShapeRenderer sr) {
        sr.setColor(0, 0.15f, 0, 1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(x, y, width, height);
        sr.end();
        sr.setColor(0, 1f, 0, 0.5f);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.rect(x, y, width, height);
        sr.end();

        sr.setColor(0, 0.45f, 0, 1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(x + 4, y + 4, width - 8, height - 8);
        sr.end();
        sr.end();

    }

    public boolean setRotateLeft(boolean rotateLeft) {
        if (!this.rotateLeft && !rotateRight) {
            this.rotateLeft = rotateLeft;
            return true;
        }
        return false;
    }

    public boolean setRotateRight(boolean rotateRight) {
        if (!this.rotateRight && !rotateLeft) {
            this.rotateRight = rotateRight;
            return true;
        }
        return false;
    }


    public int getLives() {
        return lives;
    }

    public void hit() {
        dead = !dead;
        lives -= 1;
    }

    public boolean isDead() {
        return dead;
    }

    public double getScore() {
        return score;
    }

    public int fruitsAte() {
        return fruitAte;
    }

    public boolean hasEat() {
        return eat;
    }
}

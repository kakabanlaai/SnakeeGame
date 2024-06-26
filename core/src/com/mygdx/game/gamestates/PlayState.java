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

    private float moveTimer;
    private float moveTime;

    private Player player;
    private List<Tail> body;
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

        moveTimer = 0;
        moveTime = 0.1f;

        player = new Player(LevelManager.getGrid());

        extraLives = new ArrayList<>();
        remainingApples = new Apple(0, -25, false);

        if (getPlayMode() == PlayMode.INFINITE_TAIL) {
            for (int i = player.getLives(); i > 0; i--) {
                player.hit();
            }
        }

        for (int i = 0; i < player.getLives(); i++) {
            Player newLive = new Player(LevelManager.getGrid());
            extraLives.add(newLive);
        }

        updateExtraLives();

        resetBody();
    }

    private void resetBody() {
        player.resetToPosition(
                LevelManager.getStartX() * LevelManager.getGrid(),
                LevelManager.getStartY() * LevelManager.getGrid(),
                LevelManager.getFacing());

        body = new ArrayList<>();

        //factor variables for initialize tail considering orientation
        int factorX = LevelManager.getFacing() == Player.Facing.UP ||
                LevelManager.getFacing() == Player.Facing.DOWN ? 0 :
                LevelManager.getFacing() == Player.Facing.LEFT ? -1 : 1;

        int factorY = LevelManager.getFacing() == Player.Facing.LEFT ||
                LevelManager.getFacing() == Player.Facing.RIGHT ? 0 :
                LevelManager.getFacing() == Player.Facing.DOWN ? -1 : 1;

        //add 2 body
        body.add(new Tail(
                player.getX() - LevelManager.getGrid() * factorX,
                player.getY() - LevelManager.getGrid() * factorY));

        body.add(new Tail(
                body.get(0).getX() - LevelManager.getGrid() * factorX,
                body.get(0).getY() - LevelManager.getGrid() * factorY));

        body.add(new Tail(
                body.get(1).getX() - LevelManager.getGrid() * factorX,
                body.get(1).getY() - LevelManager.getGrid() * factorY));


    }

    private void updateExtraLives() {
        if (player.isDead() && !extraLives.isEmpty()) {
            extraLives.remove(extraLives.size() - 1);
        }

        //updates the position whatever the board size
        int i = 0;
        for (Player extraLive : extraLives) {
            extraLive.setPosition(Game.WIDTH - 15 - i++ * 20, Game.HEIGHT + 5);
        }
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

        player.draw(sr);

        //draw body
        body.forEach(bodyPart -> bodyPart.draw(sr));

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

        moveTimer += dt;

        //if is paused doesn't update the rest
        if (!isPlayTime()) {
            return;
        }

        //only moves every time defined by moveTime
        if (moveTimer > moveTime) {
            moveTimer = 0; //reset timer

            checkCollision();

            //update body position
            updateBodyPosition();

            //update player
            player.update(dt);

            player.wrap();

            updatesLives();
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
        if (isPlayTime()) {
            //user preferences input keys
            switch (GameStateManager.getOptionsKeys()) {
                case SNAKE://snake perspective
                    player.setRotateLeft(Gdx.input.isKeyJustPressed(Input.Keys.LEFT));
                    player.setRotateRight(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT));
                    break;
                case PLAYER: //player perspective
                    player.setLeft(Gdx.input.isKeyJustPressed(Input.Keys.LEFT));
                    player.setRight(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT));
                    player.setUp(Gdx.input.isKeyJustPressed(Input.Keys.UP));
                    player.setDown(Gdx.input.isKeyJustPressed(Input.Keys.DOWN));
                    break;
            }
        }
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

    private void checkCollision() {
        body.stream().filter(
                bodyPart -> bodyPart.contains(player.getX(), player.getY())
        ).forEach(bodyPart -> player.hit());

        LevelManager.getWalls().stream().filter(
                pWall -> pWall.contains(player.getX(), player.getY())
        ).forEach(pWall -> player.hit());

        //player to apple
        if (apple != null) {
            if (player.eat(apple.contains(player.getX(), player.getY()), apple.getScore())) {
                if (player.fruitsAte() >= LevelManager.getFruitToNextLevel()) {
                    // if isn't bonus apple, decreases 10% time to update, increasing speed.
                    LevelManager.levelUp();
                    playTime = !playTime; //pause the game
                    resetBody();
                    moveTime = 0.1f; // reset speed
                    Jukebox.MANAGER.play("levelup");
                }
                //if fruits is bonus, then the update time increases 10%, decreasing speed.
                if (apple.isBonus() && getPlayMode() == PlayMode.LEVEL_UP) {
                    moveTime += moveTime * 0.10f;
                }
                // else, each 5 decrease update time, increasing speed.
                else if (player.fruitsAte() % 5 == 0 && getPlayMode() == PlayMode.LEVEL_UP) {
                    moveTime += moveTime * -0.10f;
                }
                Jukebox.MANAGER.play(apple.isBonus() ? "bonus" : "hiss");
            }

            if (apple.shouldRemove() || player.isDead())
                apple = null;
        }

        updateExtraLives();

    }

    private void updateBodyPosition() {
        if (!body.isEmpty()) {
            if (player.hasEat())
                body.add(new Tail(0, 0));

            for (int i = body.size() - 1; i >= 0; i--) {
                if (i == 0)
                    body.get(i).setPosition(player.getX(), player.getY());
                else
                    body.get(i).setPosition(body.get(i - 1).getX(), body.get(i - 1).getY());
            }
        }
    }

    private void updatesLives() {
        if (player.isDead()) {
            if (player.getLives() < 0) {
                GameFile.MANAGER.gameData.setTentativeScore((long) player.getScore());
                gameStateManager.setState(GameStateManager.State.GAME_OVER);
            } else {
                resetBody();
                playTime = !playTime;
            }
        }
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
            containsHead = player.contains(x, y);

            for (Tail bodyPart : body) {
                containsFruit = bodyPart.contains(x, y);
                if (containsFruit) {
                    break; //if contains exit for loop
                }
            }

            for (Wall pWall : LevelManager.getWalls()) {
                containsWall = pWall.contains(x, y);
                if (containsWall)
                    break;
            }
        } while (containsHead || containsFruit || containsWall);

        return new Apple(x, y);
    }

    private void drawText() {
        Font.MANAGER.centered(sb, titleFont,
                MenuState.title,
                Game.WIDTH / 2,
                Game.HEIGHT + 80);

        Font.MANAGER.left(sb, font,
                "Score: " + (int) player.getScore(),
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
                "x " + (LevelManager.getFruitToNextLevel() - player.fruitsAte()),
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

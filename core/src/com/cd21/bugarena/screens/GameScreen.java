package com.cd21.bugarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cd21.bugarena.GameMain;
import com.cd21.bugarena.helpers.GameInfo;
import com.cd21.bugarena.managers.InputManager;
import com.cd21.bugarena.managers.WorldManager;
import com.cd21.bugarena.player.Player;

public class GameScreen implements Screen {

    /* Own objects */
    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final WorldManager world;
    private final InputManager inputManager;
    private final Player player;

    /* External references */
    private final GameMain game;
    private final SpriteBatch batch;

    public GameScreen(GameMain game) {
        this.game = game;
        this.batch = game.getSpriteBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameInfo.VIEWPORT_WIDTH / GameInfo.PPM, GameInfo.VIEWPORT_HEIGHT / GameInfo.PPM, camera);
        camera.viewportWidth = viewport.getWorldWidth();
        camera.viewportHeight = viewport.getWorldHeight();
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);

        world = new WorldManager(batch, camera);
        player = new Player(world.getWorld(), 0, 0);
        inputManager = new InputManager(player, camera);
    }

    public void update(float deltaTime) {
        inputManager.handleInput(deltaTime);
        updateCamera();
        player.update(deltaTime);
        world.update(deltaTime);
    }

    @Override
    public void render(float deltaTime) {
        update(deltaTime);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void updateCamera() {
        camera.position.set(player.getX(), player.getY(), 0);

        float worldTop = GameInfo.WORLD_HEIGHT;
        float worldRight = GameInfo.WORLD_WIDTH;
        float worldDown = 0;
        float worldLeft = 0;

        float cameraTop = (camera.position.y + camera.viewportHeight / 2f) * GameInfo.PPM;
        float cameraRight = (camera.position.x + camera.viewportWidth / 2f) * GameInfo.PPM;
        float cameraDown = (camera.position.y - camera.viewportHeight / 2f) * GameInfo.PPM;
        float cameraLeft = (camera.position.x - camera.viewportWidth / 2f) * GameInfo.PPM;

        float newX = camera.position.x * GameInfo.PPM, newY = camera.position.y * GameInfo.PPM;

        if (cameraTop > worldTop) {
            newY = worldTop - (camera.viewportHeight / 2f) * GameInfo.PPM;
        }
        if (cameraDown < worldDown) {
            newY = worldDown + (camera.viewportHeight / 2f) * GameInfo.PPM;
        }

        if (cameraRight > worldRight) {
            newX = worldRight - (camera.viewportWidth / 2f) * GameInfo.PPM;
        }
        if (cameraLeft < worldLeft) {
            newX = worldLeft + (camera.viewportWidth / 2f) * GameInfo.PPM;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.T)) {
            Gdx.app.log("Camera", String.format("T:%2.0f | R: %2.0f | D: %2.0f | L: %2.0f", cameraTop, cameraRight, cameraDown, cameraLeft));
            Gdx.app.log("World ", String.format("T:%2.0f | R: %2.0f | D: %2.0f | L: %2.0f\n\n", worldTop, worldRight, worldDown, worldLeft));
        }

        camera.position.set(newX / GameInfo.PPM, newY / GameInfo.PPM, 0);
        camera.update();
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
    }
}

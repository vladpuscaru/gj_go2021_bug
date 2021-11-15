package com.cd21.bugarena.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.cd21.bugarena.helpers.GameInfo;

public class WorldManager {

    /* Own objects */
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;

    private World world;

    private final Box2DDebugRenderer box2DDebugRenderer;

    /* External references */
    private final Batch batch;
    private final OrthographicCamera camera;

    public WorldManager(Batch batch, OrthographicCamera camera) {
        this.batch = batch;
        this.camera = camera;

        // Tiled Map
        map = new TmxMapLoader().load("world/map_1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / GameInfo.PPM, batch);

        // Box2D
        world = new World(new Vector2(0.0f, 0.0f), true);
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    public void update(float deltaTime) {
        world.step(1 / 60.0f, 6, 2);
        mapRenderer.setView(camera);
    }

    public void render() {
        mapRenderer.render();
        box2DDebugRenderer.render(world, camera.combined);
    }

    public World getWorld() {
        return world;
    }

    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
    }
}
